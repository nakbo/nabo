package cn.krait.nabo.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.ocnyang.contourview.ContourView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.krait.nabo.R;
import cn.krait.nabo.activity.page.CommentActivity;
import cn.krait.nabo.activity.page.PostActivity;
import cn.krait.nabo.service.XMLRPCService;
import cn.krait.nabo.module.ACache.ACache;
import cn.krait.nabo.util.ConstUtils;
import cn.krait.nabo.util.OtherUtil;
import cn.krait.nabo.module.personage.Personage;
import cn.krait.nabo.module.object.PostObject;
import cn.krait.nabo.module.object.PostsObject;
import cn.krait.nabo.module.object.SettingObject;
import cn.krait.nabo.module.object.StatObject;

/**
 * @author 权那他(Kraity)
 * @date 2019/7/28.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class MRVFAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Personage personage;
    private ACache personageACache;
    private XMLRPCService XMLRPCService;
    private PostsObject posts;
    private Object[] postsArrays;
    private List<Integer> cids;
    private Context mContext;
    private StatObject stat;
    private SettingObject setting;
    private static final int ITEM_TYPE_HEADER = 0;
    private static final int ITEM_TYPE_CONTENT = 1;

    private int headerCount = 1;    // 头部 View 个数

    public MRVFAdapter(Context context, Personage s, ACache a, XMLRPCService x) {
        mContext = context;
        personage = s;
        personageACache = a;
        setting = personage.getSetting();
        XMLRPCService = x;
    }

    public void orientation(Object[] p, Object s) {
        posts = new PostsObject(p);
        postsArrays = p;
        personageACache.put(ConstUtils.POSTS_CACHE_NAME, p);
        cids = posts.getCidList();
        stat = new StatObject(s);
        notifyDataSetChanged();

    }

    public void refreshStat(Object s) {
        stat = new StatObject(s);
        notifyDataSetChanged();
    }

    public void addItem(Object[] p, Object s) {
        orientation(OtherUtil.mergeObject(new Object[]{postsArrays, p}), s);
    }

    public void removeItem(int position) {
        posts.removeItem(position - headerCount);
        postsArrays = posts.getPostsObject();
        posts = new PostsObject(postsArrays);
        cids = posts.getCidList();
        notifyItemRemoved(position);
        notifyItemRangeChanged(
                position,
                getItemCount() - position
        );
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_recycle_header_item, parent, false);
            return new HeaderViewHolder(view);
        } else if (viewType == ITEM_TYPE_CONTENT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_recycle_content_item, parent, false);
            return new ContentViewHolder(view);
        }
        return null;
    }

    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {

            ((StaggeredGridLayoutManager.LayoutParams) ((HeaderViewHolder) holder).header.getLayoutParams()).setFullSpan(true);

            if (stat != null) {
                ((HeaderViewHolder) holder).post_sum.setText(String.valueOf(stat.post.all()));
                ((HeaderViewHolder) holder).post_sum_draft.setText(String.valueOf(stat.post.draft()));
                ((HeaderViewHolder) holder).post_sum_private.setText(String.valueOf(stat.post.privated()));
                ((HeaderViewHolder) holder).comment_sum.setText(String.valueOf(stat.comment.all()));
                ((HeaderViewHolder) holder).category_sum.setText(String.valueOf(stat.categories.all()));
                ((HeaderViewHolder) holder).page_sum.setText(String.valueOf(stat.page.all()));
                ((HeaderViewHolder) holder).postAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                                .setTitle("概要")
                                .setMessage("目前有 " + stat.post.all() + " 篇文章\n并有 " + stat.comment.all() + " 条关于你的评论在 " + stat.categories.all() + " 个分类中");
                        builder.create().show();
                    }
                });
            }

            ((HeaderViewHolder) holder).contourViewFirst.setBackgroundColor(setting.getColorPrimary());
            ((HeaderViewHolder) holder).contourViewFirst.setShaderEndColor(setting.getColorAccent());
            ((HeaderViewHolder) holder).contourViewFirst.setShaderStartColor(setting.getColorPrimary());

            ((HeaderViewHolder) holder).contourViewLast.setBackgroundColor(setting.getColorPrimary());
            ((HeaderViewHolder) holder).contourViewLast.setShaderEndColor(setting.getColorAccent());
            ((HeaderViewHolder) holder).contourViewLast.setShaderStartColor(setting.getColorPrimary());


            ((HeaderViewHolder) holder).postPrivate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new getPostList().execute(0, true);
                }
            });

            ((HeaderViewHolder) holder).postDraft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new getPostList().execute(1, true);
                }
            });

            ((HeaderViewHolder) holder).cardPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new getPostList().execute(2, false);
                }
            });

            ((HeaderViewHolder) holder).cardCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new getCategoryList().execute();
                }
            });

            (((HeaderViewHolder) holder).cardComment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, CommentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("pattern", 0);
                    bundle.putInt("number", stat.comment.all());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });


        } else if (holder instanceof ContentViewHolder) {

            final int count = position - headerCount;
            final PostObject post;
            post = posts.getPost(cids.get(count));

            final ViewGroup.LayoutParams lp1 = ((ContentViewHolder) holder).post_excerpt.getLayoutParams();
            ((ContentViewHolder) holder).contourView.setBackgroundColor(setting.getColorPrimary());
            ((ContentViewHolder) holder).contourView.setShaderEndColor(setting.getColorAccent());
            ((ContentViewHolder) holder).contourView.setShaderStartColor(setting.getColorPrimary());

            switch (count) {
                case 0:
                    lp1.height = OtherUtil.dip2px(mContext, 130);
                    ((ContentViewHolder) holder).post_excerpt.setText(post.description());
                    break;
                case 1:
                    lp1.height = 0;
                    ((ContentViewHolder) holder).post_excerpt.setText("");
                    break;
                default:
                    lp1.height = post.text.lengthString() > setting.getPostTextLength() ? OtherUtil.dip2px(mContext, 130) : 0;
                    ((ContentViewHolder) holder).post_excerpt.setText(post.text.lengthString() > setting.getPostTextLength() ? post.description() : "");
            }

            String[] data = timeStamp2date(Integer.valueOf(post.created()));
            ((ContentViewHolder) holder).post_title.setText(post.title());
            if (data != null) {
                ((ContentViewHolder) holder).post_date.setText(post.status().substring(0, 3) + " " + data[0]);
                ((ContentViewHolder) holder).post_date_mon.setText(data[1]);
                ((ContentViewHolder) holder).post_date_day.setText(data[2]);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PostActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", post.cid());
                    bundle.putInt("position", position);
                    intent.putExtras(bundle);
                    ((Activity) mContext).startActivityForResult(intent, 3001);

                }
            });

        }
    }

    private static String[] timeStamp2date(Integer ms) {
        if (ms == null) {
            ms = 0;
        }
        long msl = (long) ms * 1000;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MMM/dd/HH/mm/ss");
        try {
            return sdf.format(Objects.requireNonNull(sdf.parse(sdf.format(msl)))).split("/");
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    @SuppressLint("StaticFieldLeak")
    class getPostList extends AsyncTask<Object, Void, Object[]> {
        ProgressDialog progress;
        String[] status = {"private", "draft", "publish"};
        String[] statusTitle = {"私密", "草稿", "独立页面"};
        int anInt;
        boolean isPost;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(mContext);
            progress.setMessage("加载中...");
            progress.setIndeterminate(true);
            progress.setCancelable(true);
            progress.show();
        }

        @Override
        protected Object[] doInBackground(Object[] params) {
            anInt = (int) params[0];
            isPost = (boolean) params[1];
            Map<String, Object> struct = new HashMap<>();
            if (isPost) struct.put("status", status[anInt]);
            struct.put("text_html", false);
            struct.put("text_markdown", false);
            struct.put("text_description", false);
            try {
                if (isPost) {
                    return XMLRPCService.getPosts(struct);
                } else {
                    return XMLRPCService.getPages(struct);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object[] o) {
            super.onPostExecute(o);
            progress.dismiss();
            if ((boolean) o[0]) {
                PostPrivate postPrivate = new PostPrivate((Object[]) o[1]);
                postPrivate.show(statusTitle[anInt]);
            }
        }
    }

    class PostPrivate {
        private PostsObject posts;
        private List<Integer> cids;

        PostPrivate(Object[] p) {
            posts = new PostsObject(p);
            cids = posts.getCidList();
        }

        public void show(String title) {
            final String[] items = posts.getTitleList();
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                    .setTitle(title)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            PostObject post = posts.getPost(cids.get(i));
                            int id = post.parent() != 0 ? post.parent() : post.cid();
                            Intent intent = new Intent(mContext, PostActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", id);
                            bundle.putString("type", post.type());
                            intent.putExtras(bundle);
                            ((Activity) mContext).startActivityForResult(intent, 3001);
                        }
                    });
            builder.create().show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    class getCategoryList extends AsyncTask<Object, Void, Object[]> {
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(mContext);
            progress.setMessage("加载中...");
            progress.setIndeterminate(true);
            progress.setCancelable(true);
            progress.show();
        }

        @Override
        protected Object[] doInBackground(Object[] params) {
            try {
                return XMLRPCService.getCategories();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object[] o) {
            super.onPostExecute(o);
            progress.dismiss();
            if ((boolean) o[0]) {
                Object[] categories = (Object[]) o[1];
                final String[] items = new String[categories.length];
                for (int i = 0; i < categories.length; i++) {
                    HashMap<?, ?> c = (HashMap<?, ?>) categories[i];
                    items[i] = String.valueOf(c.get("name"));
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                        .setTitle("分类")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create().show();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (headerCount != 0 && position < headerCount) {
            //头部View
            return ITEM_TYPE_HEADER;
        } else {
            //内容View
            return ITEM_TYPE_CONTENT;
        }
    }

    @Override
    public int getItemCount() {
        return headerCount + getContentItemCount();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView post_sum,
                post_sum_draft,
                post_sum_private,
                comment_sum,
                category_sum,
                page_sum;

        ContourView contourViewFirst, contourViewLast;

        LinearLayout cardPost, cardComment, cardCategory, cardPage, postAll, postPrivate, postDraft, header;

        HeaderViewHolder(View itemView) {
            super(itemView);
            post_sum = itemView.findViewById(R.id.post_sum);
            post_sum_draft = itemView.findViewById(R.id.post_sum_draft);
            post_sum_private = itemView.findViewById(R.id.post_sum_private);
            comment_sum = itemView.findViewById(R.id.comment_sum);
            category_sum = itemView.findViewById(R.id.category_sum);
            page_sum = itemView.findViewById(R.id.page_sum);
            cardPost = itemView.findViewById(R.id.card_post);
            cardComment = itemView.findViewById(R.id.card_comment);
            cardCategory = itemView.findViewById(R.id.card_category);
            cardPage = itemView.findViewById(R.id.card_page);
            postAll = itemView.findViewById(R.id.post_all);
            postPrivate = itemView.findViewById(R.id.post_private);
            postDraft = itemView.findViewById(R.id.post_draft);
            header = itemView.findViewById(R.id.header_layout);

            contourViewFirst = itemView.findViewById(R.id.contourViewFirst);
            contourViewLast = itemView.findViewById(R.id.contourViewLast);
        }
    }

    public class ContentViewHolder extends RecyclerView.ViewHolder {

        TextView post_title,
                post_excerpt,
                post_date,
                post_date_mon,
                post_date_day;

        ContourView contourView;

        ContentViewHolder(View itemView) {
            super(itemView);
            post_title = itemView.findViewById(R.id.post_title);
            post_excerpt = itemView.findViewById(R.id.post_excerpt);
            post_date = itemView.findViewById(R.id.post_date);
            post_date_mon = itemView.findViewById(R.id.post_date_mon);
            post_date_day = itemView.findViewById(R.id.post_date_day);

            contourView = itemView.findViewById(R.id.contourView);
        }
    }

    /**
     * 内容长度
     */
    private int getContentItemCount() {
        return cids == null ? 0 : cids.size();
    }

    /**
     * 判断当前 item 是否是 HeadView
     */
    public boolean isHeaderView(int position) {
        return headerCount != 0 && position < headerCount;
    }

}
