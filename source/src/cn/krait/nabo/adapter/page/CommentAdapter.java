package cn.krait.nabo.adapter.page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.krait.nabo.R;
import cn.krait.nabo.service.XMLRPCService;
import cn.krait.nabo.module.ACache.ACache;
import cn.krait.nabo.util.OtherUtil;
import cn.krait.nabo.module.personage.Personage;
import cn.krait.nabo.module.object.CommentObject;
import cn.krait.nabo.module.object.CommentsObject;

/**
 * @author 权那他(Kraity)
 * @date 2019/8/4.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private CommentsObject comments;
    private Object[] commentsArrays;
    private List<Integer> cids;
    private List<Integer> authorIdList;
    private ACache personageACache;
    private XMLRPCService XMLRPCService;
    private Personage personage;
    private Handler handler;

    public CommentAdapter(Context context, Personage p, ACache a, XMLRPCService x, Handler h) {
        mContext = context;
        personageACache = a;
        XMLRPCService = x;
        personage = p;
        handler = h;

    }

    public void setVerticalDataList(Object[] c) {
        comments = new CommentsObject(c);
        commentsArrays = c;
        cids = comments.getCidList();
        authorIdList = comments.getAuthorIdList();
        notifyDataSetChanged();

    }

    public void addItem(Object[] c) {
        setVerticalDataList(OtherUtil.mergeObject(new Object[]{commentsArrays, c}));
    }

    public void removeItem(int position) {
        comments.removeItem(position);
        commentsArrays = comments.getCommentsObject();
        comments = new CommentsObject(commentsArrays);
        cids = comments.getCidList();
        authorIdList = comments.getAuthorIdList();
        notifyItemRemoved(position);
        notifyItemRangeChanged(
                position,
                getItemCount() - position
        );

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.TYPE_FRIEND_MSG.ordinal()) {
            return new VerticalViewHolder(LayoutInflater.from(mContext).inflate(R.layout.page_recycle_comment_item, parent, false));
        } else {
            return new authorVerticalViewHolder(LayoutInflater.from(mContext).inflate(R.layout.page_recycle_comment_author_item, parent, false));
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final CommentObject comment;
        final String bitmapName, statusMark;
        final boolean resetState;
        comment = comments.getComment(cids.get(position));
        bitmapName = "avatar_" + comment.authorMail();
        Bitmap imageBitmap = personageACache.getAsBitmap(bitmapName);
        resetState = comment.status().equals("approved");
        statusMark = resetState ? "" : (comment.status().equals("waiting") ? "待审核" : (comment.status().equals("spam") ? "垃圾" : ""));

        int itemViewType = getItemViewType(position);
        if (itemViewType == ITEM_TYPE.TYPE_FRIEND_MSG.ordinal()) {
            if (imageBitmap == null) {
                new getAvatarAsyncTask().execute(0, holder, comment.authorMail(), bitmapName);
            } else {
                ((VerticalViewHolder) holder).image.setImageBitmap(personageACache.getAsBitmap(bitmapName));

            }
            ((VerticalViewHolder) holder).title.setText("in " + comment.title());
            ((VerticalViewHolder) holder).content.setText(comment.text());
            ((VerticalViewHolder) holder).author.setText(comment.author());
            ((VerticalViewHolder) holder).date.setText("on " + OtherUtil.timestampFormatText(Long.parseLong(comment.created()) * 1000, false));
            ((VerticalViewHolder) holder).status.setText(statusMark);
            ((VerticalViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickViewItem(comment, position, resetState);
                }
            });
        } else {
            if (imageBitmap == null) {
                new getAvatarAsyncTask().execute(1, holder, comment.authorMail(), bitmapName);
            } else {
                ((authorVerticalViewHolder) holder).image.setImageBitmap(personageACache.getAsBitmap(bitmapName));

            }
            ((authorVerticalViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickViewItem(comment, position, resetState);
                }
            });
            ((authorVerticalViewHolder) holder).content.setText(comment.text());
        }


    }

    public void clickViewItem(final CommentObject comment, final int position, final boolean resetState) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(comment.author()).append("\n");
        stringBuilder.append("by ").append(comment.authorMail()).append("\n");
        stringBuilder.append("at ").append(comment.authorIp()).append("\n");
        stringBuilder.append("in ").append(comment.title()).append("\n");
        stringBuilder.append("on ").append(OtherUtil.timeStamp2date(Integer.valueOf(comment.created()),"yyyy/MM/dd  HH:mm:ss")).append("\n");
        final String resetMessage = (resetState ? "待审核" : "通过");
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setMessage(stringBuilder)
                .setPositiveButton(resetMessage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setTitle("标记为" + resetMessage)
                                .setMessage("客官你确定要标记这条评论为" + resetMessage + "状态吗?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        OtherUtil.toast(mContext, "正在请求");
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Message msg = new Message();
                                                    Map<String, Object> content = new HashMap<String, Object>();
                                                    content.put("status", resetState ? "waiting" : "approved");
                                                    Object[] able = XMLRPCService.editComment(comment.coid(), content);
                                                    if ((boolean) able[0]) {
                                                        msg.what = 1;
                                                        msg.obj = "标记为" + resetMessage + "成功";
                                                        handler.sendMessage(msg);
                                                    } else {
                                                        msg.what = 0;
                                                        msg.obj = "标记为" + resetMessage + "失败";
                                                        handler.sendMessage(msg);
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                        builder.create().show();
                    }
                }).setNeutralButton("回复", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("call", comment);
                        Message message = handler.obtainMessage(2, bundle);
                        handler.sendMessage(message);
                    }
                }).setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setTitle("删除评论")
                                .setMessage("客官你确定要删除这条评论吗?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        OtherUtil.toast(mContext, "正在请求");
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Message msg = new Message();
                                                    msg.what = 0;
                                                    Object[] able = XMLRPCService.deleteComment(comment.coid());
                                                    if ((boolean) able[0]) {
                                                        msg.obj = "删除成功";
                                                        handler.sendMessage(msg);
                                                        removeItem(position);
                                                    } else {
                                                        msg.obj = "删除失败";
                                                        handler.sendMessage(msg);
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();

                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                        builder.create().show();

                        dialogInterface.dismiss();
                    }
                });

        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return cids == null ? 0 : cids.size();
    }

    public int getPublishItemCount() {
        return comments.getState()[2];
    }

    @SuppressLint("StaticFieldLeak")
    class getAvatarAsyncTask extends AsyncTask<Object, Void, Bitmap> {

        private RecyclerView.ViewHolder holder;
        private boolean isAuthor;
        private String email;
        private String bitmapName;

        @Override
        protected Bitmap doInBackground(Object[] params) {
            isAuthor = (int) params[0] == 1;
            if (isAuthor) {
                holder = (authorVerticalViewHolder) params[1];
            } else {
                holder = (VerticalViewHolder) params[1];
            }
            email = (String) params[2];
            bitmapName = (String) params[3];

            try {
                return OtherUtil.getGravatar(email, personageACache, bitmapName);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isAuthor) {
                ((authorVerticalViewHolder) holder).image.setImageBitmap(bitmap);
            } else {
                ((VerticalViewHolder) holder).image.setImageBitmap(bitmap);
            }
        }
    }

    //定义一个枚举，分别表示接受消息和发送消息
    public enum ITEM_TYPE {
        TYPE_FRIEND_MSG,
        TYPE_ME_MSG
    }

    //根据消息的属性返回不同的viewType
    @Override
    public int getItemViewType(int position) {
        if (authorIdList.get(position) == 1) {
            return ITEM_TYPE.TYPE_ME_MSG.ordinal();
        } else {
            return ITEM_TYPE.TYPE_FRIEND_MSG.ordinal();
        }
    }

    class VerticalViewHolder extends RecyclerView.ViewHolder {

        TextView author, content, date, status, title;
        ImageView image;

        VerticalViewHolder(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);
            image = itemView.findViewById(R.id.image);
            status = itemView.findViewById(R.id.status);
            title = itemView.findViewById(R.id.title);
        }
    }

    class authorVerticalViewHolder extends RecyclerView.ViewHolder {

        TextView content;
        ImageView image;

        authorVerticalViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            image = itemView.findViewById(R.id.image);
        }
    }
}