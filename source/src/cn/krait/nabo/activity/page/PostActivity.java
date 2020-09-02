package cn.krait.nabo.activity.page;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.krait.nabo.R;
import cn.krait.nabo.activity.inherit.InitialActivity;
import cn.krait.nabo.module.markdown.widget.MarkdownView;
import cn.krait.nabo.util.OtherUtil;
import cn.krait.nabo.module.object.NewPostObject;
import cn.krait.nabo.module.object.PostObject;

public class PostActivity extends InitialActivity {

    protected PostObject post;
    private int postId, position, editChange;
    private String type;
    private boolean isPost;
    private ProgressBar progressBar;
    private MarkdownView markdownView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Toolbar postToolbar = (Toolbar) findViewById(R.id.post_toolbar);
        postToolbar.setTitle("");
        this.setSupportActionBar(postToolbar);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        editChange = 0;
        postId = bundle.getInt("id", 0);
        position = bundle.getInt("position", -1);
        type = bundle.getString("type", "post");
        isPost = type.contains("post");
        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        Sprite threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);

        markdownView = (MarkdownView) findViewById(R.id.markdownView);

        new getPostAsyncTask().execute(postId);

        ((TextView) findViewById(R.id.author_name)).setText(
                personage.getScreenName()
        );


        (findViewById(R.id.post_btn_share)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent textIntent = new Intent(Intent.ACTION_SEND);
                textIntent.setType("text/plain");
                textIntent.putExtra(Intent.EXTRA_TEXT, post.title() + "\n" + post.permalink());
                startActivity(Intent.createChooser(textIntent, "分享"));
            }
        });

        (findViewById(R.id.post_btn_comment)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(PostActivity.this, CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("pattern", 1);
                bundle.putInt("number", Integer.parseInt(post.commentsNum()));
                bundle.putInt("cid", post.cid());
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });

        (findViewById(R.id.post_btn_info)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringBuilder message = new StringBuilder();
                message.append("标题：").append(post.title()).append("\n");
                message.append("作者：").append(post.author.screenName()).append("\n");
                message.append("类型：").append(type).append("\n");
                message.append("状态：").append(post.status()).append("\n");
                if (isPost && !post.categories.name().isEmpty())
                    message.append("分类：").append(post.categories.name()).append("\n");
                if (isPost && !post.tags.name().isEmpty())
                    message.append("标签：").append(post.tags.name()).append("\n");
                message.append("Slug：").append(post.slug()).append("\n");
                message.append("评论许可：").append(post.allowComment() == 1 ? "允许评论" : "禁止评论").append("\n");
                if (post.password() != null && !post.password().isEmpty())
                    message.append("访问密码：").append(post.password()).append("\n");
                message.append("撰写日期：").append(OtherUtil.timeStamp2date(Integer.valueOf(post.created()), "yyyy/MM/dd  HH:mm:ss")).append("\n");
                if (type.equals("POST"))
                    message.append("最后修改：").append(OtherUtil.timestampFormatText(Long.parseLong(post.created()) * 1000, false)).append("\n");
                AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this)
                        .setTitle("详情")
                        .setMessage(message);
                builder.create().show();
            }
        });

        postToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.post, menu);
        if (!isPost) menu.findItem(R.id.menu_delete).setVisible(false);
        return true;
    }

    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                if (setting.getArticleDeleteAble()) {
                    OtherUtil.toast(PostActivity.this, "没有权限,请在个性设置里关闭文章删除保险");
                } else {
                    new AlertDialog.Builder(PostActivity.this)
                            .setTitle("删除文章")
                            .setMessage("你确定要删除这篇文章吗?")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    OtherUtil.toast(PostActivity.this, "正在发送请求");
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent intent1 = new Intent();
                                                    intent1.putExtra("position", position);
                                                    setResult(3001, intent1);
                                                    PostActivity.this.finish();
                                                }
                                            });
                                            try {
                                                Object[] call = XMLRPCService.deletePost(postId);
                                                OtherUtil.toast(PostActivity.this, "已发送请求");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Log.e("e", e.toString());
                                            }
                                        }
                                    }

                                    ).start();
                                }
                            })

                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .create()
                            .show();
                }

                break;

            case R.id.menu_edit:
                if (post != null) {
                    Intent intent1 = new Intent(PostActivity.this, EditPostActivity.class);
                    intent1.putExtra("title", post.title());
                    intent1.putExtra("text", post.text.markdown());
                    Bundle extras = new Bundle();
                    NewPostObject p = new NewPostObject();
                    p.setType(post.type());
                    p.setPattern("edit");
                    p.setTemplate(post.template());
                    p.setCid(post.cid());
                    p.setTitle(post.title());
                    p.setText(post.text.markdown());
                    p.setSlug(post.slug());
                    p.setPassword(post.password());
                    p.setCategories(post.categories.name().split(","));
                    p.setTags(post.tags.name().split(","));
                    p.setAllowPing(1);
                    p.setAllowComment(1);
                    extras.putSerializable("object", p);
                    intent1.putExtras(extras);
                    intent1.putExtra("requestCode", 1001);//文章页面到编辑文章页面 暂时代号1001
                    startActivityForResult(intent1, 1001);
                } else {
                    Toast.makeText(PostActivity.this, "等加载完后才可编辑", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if ((result != null) & (resultCode == 1001)) {
            final String title = Objects.requireNonNull(result.getExtras()).getString("title");
            final String content = Objects.requireNonNull(result.getExtras()).getString("text");
            editChange = 1;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setMarkdownView(markdownView, content);
                    ((TextView) findViewById(R.id.post_title)).setText(title);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        if (editChange == 1) {
            Intent intent1 = new Intent();
            intent1.putExtra("position", position);
            intent1.putExtra("refreshType", type.equals("POST") ? 1 : 2);
            setResult(3002, intent1);
        }
        super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @SuppressLint("StaticFieldLeak")
    class getPostAsyncTask extends AsyncTask<Object, Void, Object[]> {

        @SuppressLint("WrongThread")
        @Override
        protected Object[] doInBackground(Object[] params) {
            try {
                Map<String, Object> struct = new HashMap<>();
                struct.put("text_description", false);
                if (isPost) {
                    return XMLRPCService.getPost(postId, struct);
                } else {
                    return XMLRPCService.getPage(postId, struct);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object[] p) {
            if (p != null) {
                post = new PostObject(p[1]);

                ((TextView) findViewById(R.id.post_title)).setText(post.title());
                ((TextView) findViewById(R.id.post_comment_number)).setText(post.commentsNum());
                setMarkdownView(markdownView, post.text.markdown());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                }, 700);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    public void setMarkdownView(MarkdownView markdownView, String content) {
        markdownView.setTextInBackground(content);
        markdownView.setLinkClickListener(new MarkdownView.LinkClickListener() {
            @Override
            public void click(String url) {
                if (url.startsWith("mailto:")) {
                    Intent mailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url)); //打开发邮件窗口
                    startActivity(mailIntent);
                } else {
                    OtherUtil.openWebView(PostActivity.this, url);
                }
            }
        });
        markdownView.setImgClickListener(new MarkdownView.ImgClickListener() {
            @Override
            public void click(String[] urls, int index) {
                //OtherUtil.openWebView(PostActivity.this, urls[index]);
            }
        });
        this.markdownView = markdownView;
    }
}
