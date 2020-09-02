package cn.krait.nabo.activity.page;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.krait.nabo.R;
import cn.krait.nabo.module.markdown.ui.EditorActivity;
import cn.krait.nabo.module.markdown.ui.fragment.EditorFragment;
import cn.krait.nabo.util.OtherUtil;
import cn.krait.nabo.module.object.NewPostObject;
import id.zelory.compressor.Compressor;


/**
 * @author 权那他(Kraity)
 * @date 2019/7/29.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class EditPostActivity extends EditorActivity {

    private NewPostObject post;
    private int requestCode, selectInt;
    private ArrayList<String> picList;
    private String[] statusString = {"publish", "draft", "private"};
    private String[] cautionString = {"发布", "保存为云端草稿", "发布为私密状态"};
    private String[] toastString = {"发布成功", "保存云端草稿草稿成功", "私密发布成功"};
    private boolean isPost, isNew;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Intent intent1 = getIntent();

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        post = (NewPostObject) extras.get("object");
        assert post != null;

        requestCode = intent1.getIntExtra("requestCode", 1000);
        isPost = post.getType().contains("post");
        isNew = post.getPattern().equals("new");

        picList = new ArrayList<>();

        String subtitle = isNew ? isPost ? "撰写文章" : "撰写独页" : isPost ? "编辑文章" : "编辑独页";
        toolbar.setSubtitle(subtitle);

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
    protected void clickReference() {
        super.clickReference();
        Intent intent = new Intent(this, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("link", "https://segmentfault.com/markdown");

        intent.putExtras(bundle);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.edit, menu);
        if (!isPost) {
            menu.findItem(R.id.action_private).setVisible(false);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 16908332) {
            if (super.viewPager.getCurrentItem() != 1) {
                onBackPressed();
                return false;
            }
        } else if (item.getItemId() == R.id.action_save) {
            selectInt = 0;
            clickSave();
            return false;
        } else if (item.getItemId() == R.id.action_draft) {
            selectInt = 1;
            clickSave();
        } else if (item.getItemId() == R.id.action_private) {
            if (!post.passwordIsEmpty()) {
                OtherUtil.toast(EditPostActivity.this, "此文章存在访问密码,不能发布为私密状态");
                return false;
            }
            selectInt = 2;
            clickSave();
        } else if (item.getItemId() == R.id.action_media) {
            Intent intent = new Intent(EditPostActivity.this, MediaActivity.class);
            EditPostActivity.this.startActivity(intent);
        } else if (item.getItemId() == R.id.action_setting) {

            Intent intent = new Intent(EditPostActivity.this, EditPostSetActivity.class);
            Bundle extras = new Bundle();
            extras.putSerializable("setObject", post);
            intent.putExtras(extras);

            EditPostActivity.this.startActivityForResult(intent, 2001);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isNew) {
            if (getMarkdownText().isEmpty() & getMarkdownTitle().isEmpty()) {
                super.onBackPressed();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("返回")
                        .setMessage("当前存在已编辑内容,客官你确定退出编辑而不保存吗?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditPostActivity.super.onBackPressed();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                builder.create().show();
            }
        } else {
            if (getMarkdownText().equals(post.getText()) & getMarkdownTitle().equals(post.getTitle())) {
                super.onBackPressed();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("返回")
                        .setMessage("当前已变更了编辑内容,客官你确定要退出编辑而不保存吗?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditPostActivity.super.onBackPressed();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                builder.create().show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if ((result != null) & (resultCode == 2001)) {
            Bundle extras = result.getExtras();
            assert extras != null;
            post = (NewPostObject) extras.get("callSetObject");
        }

        if (resultCode == -1 && requestCode == 4000) {
            if (result != null) {
                new uploadImgAsyncTask().execute(editorFragment, result);
            }
        }
    }

    @Override
    public String getMarkdownText() {
        return super.getMarkdownText();
    }

    @Override
    public String getMarkdownTitle() {
        return super.getMarkdownTitle();
    }

    /**
     * 点击了保存
     */
    @Override
    public void clickSave() {
        super.clickSave();
        post.setTitle(getMarkdownTitle());
        post.setText(getMarkdownText());

        AlertDialog.Builder builder = new AlertDialog.Builder(EditPostActivity.this)
                .setTitle("准备好了吗?")
                .setMessage("此文章将立即" + cautionString[selectInt])
                .setPositiveButton("立即执行", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        publish();
                    }
                })
                .setNegativeButton("继续编辑", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();

    }

    public void publish() {
        if (post.titleIsEmpty() || post.textIsEmpty()) {
            OtherUtil.toast(EditPostActivity.this, "标题和内容均不能为空");
            return;
        }

        if (!picList.isEmpty()) {
            Log.e("e", picList.toString());
            for (int i = 0; i < picList.size(); i++) {
                String[] pic = picList.get(i).split("@");
                post.setText(post.getText().replaceAll(pic[0], pic[1]));
            }
        }

        OtherUtil.toast(EditPostActivity.this, "正在请求");

        new publish().execute();

    }

    @SuppressLint("StaticFieldLeak")
    class publish extends AsyncTask<Object, Void, Boolean> {
        private Map<String, Object> struct;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            struct = new HashMap<>();
            struct.put("type", isPost ? "post" : "page");

            struct.put("title", post.getTitle());

            if (!post.slugIsEmpty()) struct.put("slug", post.getSlug());

            struct.put("text", post.getText());

            struct.put("publish", "publish");

            struct.put("callback", "true");

            struct.put("status", statusString[selectInt]);

            struct.put("visibility", post.passwordIsEmpty() ? statusString[selectInt] : "password");
            if (!post.passwordIsEmpty() && isPost)
                struct.put("password", post.getPassword());

            if (!post.tagsIsEmpty() && isPost)
                struct.put("tags", OtherUtil.array2string(post.getTags()));

            if (!post.categoriesIsEmpty() && isPost)
                struct.put("categories", post.getCategories());

            if (!isPost)
                struct.put("template", post.getTemplate());

            struct.put("allow_pings", post.getAllowComment() == 1 ? "open" : "closed");

            struct.put("allow_comments", post.getAllowPing() == 1 ? "open" : "closed");
        }

        @Override
        protected Boolean doInBackground(Object[] o) {
            try {
                if (requestCode == 1001) {
                    struct.put("cid", post.getCid());
                    if (isPost) {
                        XMLRPCService.editPost(struct);
                    } else {
                        XMLRPCService.editPage(struct);
                    }
                } else if (requestCode == 1002) {
                    XMLRPCService.newPost(struct);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (!aBoolean) {
                OtherUtil.toast(EditPostActivity.this, "发布文章失败");
                return;
            }
            OtherUtil.toast(EditPostActivity.this, toastString[selectInt]);
            if (requestCode == 1001) {
                Intent intent1 = new Intent();
                intent1.putExtra("title", post.getTitle());
                intent1.putExtra("text", post.getText());
                setResult(1001, intent1);
                finish();
            } else if (requestCode == 1002) {
                Intent intent1 = new Intent();
                intent1.putExtra("type", "publish");
                setResult(3002, intent1);
                finish();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    class uploadImgAsyncTask extends AsyncTask<Object, Void, Object[]> {
        String path;
        EditorFragment editorFragment;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            OtherUtil.toast(EditPostActivity.this, "正在上传图片");
        }

        @Override
        protected Object[] doInBackground(Object[] o) {
            editorFragment = (EditorFragment) o[0];
            Intent result = (Intent) o[1];
            path = OtherUtil.getRealFilePath(EditPostActivity.this, result.getData());
            if (path == null) return null;

            try {
                File file = new File(path);
                Map<String, Object> data = new HashMap<String, Object>();

                Bitmap compressedImageBitmap = new Compressor(EditPostActivity.this).compressToBitmap(file);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                compressedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                byte[] pic = byteArrayOutputStream.toByteArray();

                data.put("name", OtherUtil.getFileNameWithSuffix(path));
                data.put("bytes", pic);
                data.put("size", pic.length);
                data.put("mime", "image/jpeg");

                return XMLRPCService.newMedia(data);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object[] o) {
            if (o == null) {
                OtherUtil.toast(EditPostActivity.this, "图片上传失败,可能图片太大了");
            } else {
                HashMap<?, ?> files = (HashMap<?, ?>) o[1];
                picList.add("file://" + path + "@" + files.get("url"));
                if (setting.getImageUrlReplaceAble()) {
                    if (editorFragment.editorHandler != null)
                        editorFragment.editorHandler.replace("file://" + path, (String) files.get("url"));
                }
                OtherUtil.toast(EditPostActivity.this, "图片上传成功");
            }

        }
    }

}
