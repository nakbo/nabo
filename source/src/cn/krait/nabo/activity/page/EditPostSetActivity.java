package cn.krait.nabo.activity.page;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;

import cn.krait.nabo.R;
import cn.krait.nabo.activity.inherit.InitialActivity;
import cn.krait.nabo.util.OtherUtil;
import cn.krait.nabo.module.object.NewPostObject;

public class EditPostSetActivity extends InitialActivity {

    private NewPostObject post;
    private boolean isPost;
    private TextView slugText, categoryText, tagText, passwordText;
    private Switch allowComment;
    private Switch allowPing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post_set);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        post = (NewPostObject) extras.get("setObject");
        assert post != null;

        isPost = post.getType().contains("post");

        Toolbar postToolbar = findViewById(R.id.post_toolbar);
        postToolbar.setTitle("高级选项");

        postToolbar.setSubtitle(isPost ? "文章设置" : "独页设置");
        this.setSupportActionBar(postToolbar);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        slugText = findViewById(R.id.publish_slug);
        tagText = findViewById(R.id.publish_tag);
        categoryText = findViewById(R.id.publish_category);
        passwordText = findViewById(R.id.publish_password);
        allowComment = findViewById(R.id.publish_allowComment);
        allowPing = findViewById(R.id.publish_allowPing);

        if (!post.slugIsEmpty()) slugText.setText(post.getSlug());
        if (!post.tagsIsEmpty()) tagText.setText(OtherUtil.array2string(post.getTags()));
        if (!post.categoriesIsEmpty())
            categoryText.setText(OtherUtil.array2string(post.getCategories()));
        if (!post.passwordIsEmpty()) passwordText.setText(post.getPassword());
        if (post.getAllowComment() == 1) allowComment.setChecked(true);
        if (post.getAllowPing() == 1) allowPing.setChecked(true);

        if (!isPost) {
            (findViewById(R.id.btn_tag)).setVisibility(View.GONE);
            (findViewById(R.id.btn_category)).setVisibility(View.GONE);
            (findViewById(R.id.btn_password)).setVisibility(View.GONE);
        }

        onClickListener();

        postToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Intent intent1 = new Intent();
        Bundle extras = new Bundle();
        extras.putSerializable("callSetObject", post);
        intent1.putExtras(extras);
        setResult(2001, intent1);
        super.onBackPressed();
    }

    public void onClickListener() {
        (findViewById(R.id.btn_slug)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(EditPostSetActivity.this);
                if (!post.slugIsEmpty()) editText.setText(post.getSlug());
                AlertDialog.Builder builder = new AlertDialog.Builder(EditPostSetActivity.this)
                        .setTitle("SLUG")
                        .setView(editText)
                        .setMessage("slug是文章标题的URL友好型版本")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String slugString = editText.getText().toString();
                                if (slugString.isEmpty()) return;
                                post.setSlug(slugString);
                                slugText.setText(slugString);
                            }
                        });
                builder.create().show();
            }
        });

        (findViewById(R.id.btn_tag)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(EditPostSetActivity.this);
                if (post.getTags() != null)
                    editText.setText(OtherUtil.array2string(post.getTags()));
                editText.setHint("用英文逗号分隔标签");
                AlertDialog.Builder builder = new AlertDialog.Builder(EditPostSetActivity.this)
                        .setTitle("标签")
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String text = editText.getText().toString();
                                String tagString;
                                if (!text.isEmpty()) {
                                    tagString = text.replace(" ", "");
                                } else {
                                    return;
                                }
                                String[] spitArray = OtherUtil.splitSameStr(tagString.split(","));
                                StringBuilder println = new StringBuilder();
                                for (String s : spitArray) {
                                    println.append(s);
                                    println.append(",");
                                }
                                String tags = println.substring(0, println.length() - 1);
                                post.setTags(tags.split(","));
                                tagText.setText(tags);
                            }
                        });

                builder.create().show();
            }
        });

        (findViewById(R.id.btn_category)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getCategoryList().execute();
            }
        });

        (findViewById(R.id.btn_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(EditPostSetActivity.this);
                if (!post.passwordIsEmpty()) editText.setText(post.getPassword());
                AlertDialog.Builder builder = new AlertDialog.Builder(EditPostSetActivity.this)
                        .setTitle("访问密码")
                        .setMessage("只有知道此密码的他人才能查看这篇文章")
                        .setView(editText)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String text = editText.getText().toString();
                                if (text.isEmpty()) {
                                    passwordText.setText("默认");
                                } else {
                                    passwordText.setText(text);
                                    post.setPassword(text);
                                }

                            }
                        });
                builder.create().show();
            }
        });

        allowComment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    post.setAllowComment(1);
                } else {
                    post.setAllowComment(0);
                }
            }
        });

        allowPing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    post.setAllowPing(1);
                } else {
                    post.setAllowPing(0);
                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    class getCategoryList extends AsyncTask<Object, Void, Object[]> {
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(EditPostSetActivity.this);
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
                boolean[] isSelect = new boolean[categories.length];
                for (int i = 0; i < categories.length; i++) {
                    HashMap<?, ?> c = (HashMap<?, ?>) categories[i];
                    items[i] = String.valueOf(c.get("name"));
                    isSelect[i] = false;
                }

                final StringBuilder[] list = {new StringBuilder()};
                AlertDialog.Builder builder = new AlertDialog.Builder(EditPostSetActivity.this)
                        .setTitle("分类")
                        .setMultiChoiceItems(items, isSelect, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                if (b) {
                                    list[0].append(items[i]);
                                    list[0].append(",");
                                } else {
                                    list[0] = new StringBuilder(("" + list[0]).replace(items[i] + ",", ""));
                                }
                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean isList = list[0].length() > 1;
                                StringBuilder stringBuilder;
                                if (isList) {
                                    stringBuilder = new StringBuilder(("" + list[0]).substring(0, ("" + list[0]).length() - 1));
                                } else {
                                    stringBuilder = new StringBuilder();
                                }
                                String categoryString = stringBuilder.toString();
                                String[] spitArray = OtherUtil.splitSameStr(categoryString.split(","));
                                StringBuilder println = new StringBuilder();
                                for (String s : spitArray) {
                                    println.append(s);
                                    println.append(",");
                                }
                                String categories = println.substring(0, println.length() - 1);
                                post.setCategories(categories.split(","));
                                categoryText.setText(categories);
                            }
                        });

                builder.create().show();
            }
        }
    }
}
