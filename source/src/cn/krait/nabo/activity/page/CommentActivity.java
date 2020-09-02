package cn.krait.nabo.activity.page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.krait.nabo.R;
import cn.krait.nabo.activity.inherit.InitialActivity;
import cn.krait.nabo.adapter.page.CommentAdapter;
import cn.krait.nabo.util.ConstUtils;
import cn.krait.nabo.util.OtherUtil;
import cn.krait.nabo.module.object.CommentObject;

public class CommentActivity extends InitialActivity {

    private RecyclerView rcvVertical;
    private Toolbar commentToolbar;
    private SwipeRefreshLayout mRefreshLayout;
    private CommentAdapter adapter;
    private CommentObject commentObject;
    private boolean noneComment, unAllComment;
    private int lastVisibleItem,
            addRequestStatus,
            number,
            cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        commentToolbar = (Toolbar) findViewById(R.id.comment_toolbar);
        commentToolbar.setTitle("所有评论");
        commentToolbar.setSubtitle("闲谈评论");
        this.setSupportActionBar(commentToolbar);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        addRequestStatus = 0;
        noneComment = true;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        unAllComment = bundle.getInt("pattern", 0) != 0;
        number = bundle.getInt("number", 0);
        cid = bundle.getInt("cid", 0);

        if (unAllComment) commentToolbar.setTitle(number + "条评论");

        rcvVertical = findViewById(R.id.rcv_vertical);
        mRefreshLayout = findViewById(R.id.srl_refresh);
        adapter = new CommentAdapter(this, personage, personageACache, XMLRPCService, handler);
        mRefreshLayout.setColorSchemeResources(
                R.color.brown_1,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light,
                android.R.color.holo_blue_light,
                android.R.color.holo_purple
        );
        mRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new onCommentAsyncTask().execute(adapter, mRefreshLayout, 0);
            }
        });

        final LinearLayoutManager managerVertical = new LinearLayoutManager(this);
        managerVertical.setOrientation(RecyclerView.VERTICAL);
        managerVertical.setReverseLayout(true);
        rcvVertical.setLayoutManager(managerVertical);
        rcvVertical.setHasFixedSize(true);
        rcvVertical.setAdapter(adapter);
        if (!unAllComment && personageACache.getAsObject(ConstUtils.COMMENTS_CACHE_NAME) != null)
            adapter.setVerticalDataList((Object[]) personageACache.getAsObject(ConstUtils.COMMENTS_CACHE_NAME));

        if (setting.getAllowCommentsRefresh()) {
            mRefreshLayout.setRefreshing(true);
            new onCommentAsyncTask().execute(adapter, mRefreshLayout, 0);
        }

        rcvVertical.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int s) {
                super.onScrollStateChanged(recyclerView, s);

                if (s == RecyclerView.SCROLL_STATE_IDLE &&
                        addRequestStatus == 0 &&
                        lastVisibleItem + 1 == adapter.getItemCount()) {
                    addRequestStatus = 1;
                    mRefreshLayout.setRefreshing(true);
                    new onCommentAsyncTask().execute(adapter, mRefreshLayout, 1);
                }
            }

            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = managerVertical.findLastVisibleItemPosition();
            }
        });

        commentToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.call_sent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noneComment && cid == 0) return;
                final EditText editText = (EditText) findViewById(R.id.et_callComment);
                if (editText.getText().toString().isEmpty())
                    return;
                AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this)
                        .setTitle(!noneComment ? "回复 " + commentObject.author() : "评论")
                        .setMessage(editText.getText().toString())
                        .setPositiveButton("确认回复", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (editText.getText().toString().isEmpty())
                                    return;
                                OtherUtil.toast(CommentActivity.this, "正在请求");
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String text = editText.getText().toString();
                                        if (!text.isEmpty()) {
                                            Map<String, Object> content = new HashMap<String, Object>();
                                            content.put("author", personage.getScreenName());
                                            content.put("mail", personage.getEmail());
                                            //content.put("url", personage.getSiteUrl());
                                            if (!noneComment)
                                                content.put("parent", commentObject.coid());
                                            content.put("text", text);
                                            //content.put("callback", true);
                                            try {
                                                Object[] coid = XMLRPCService.newComment(String.valueOf(!noneComment ? commentObject.cid() : cid), content);
                                                Message msg = new Message();
                                                msg.what = 1;
                                                handler.sendMessage(msg);
                                                noneComment = true;
                                                hideSoftInputFromWindow(editText);
                                                OtherUtil.toast(CommentActivity.this, "回复成功");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                }).start();
                            }
                        });
                builder.create().show();
            }
        });

    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.obj != null && msg.what != 2)
                Toast.makeText(CommentActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    mRefreshLayout.setRefreshing(true);
                    new onCommentAsyncTask().execute(adapter, mRefreshLayout, 0);
                    break;
                case 2:
                    Bundle bundle2 = (Bundle) msg.obj;
                    assert bundle2 != null;
                    CommentObject comment = (CommentObject) bundle2.get("call");
                    assert comment != null;
                    commentObject = comment;
                    noneComment = false;
                    showSoftInputFromWindow((EditText) findViewById(R.id.et_callComment));
                default:
            }
        }
    };

    /**
     * EditText获取焦点并显示软键盘
     */
    public void showSoftInputFromWindow(EditText editText) {
        if (noneComment) return;
        editText.setHint("回复" + commentObject.author());
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        try {
            ((InputMethodManager) Objects.requireNonNull(getSystemService(INPUT_METHOD_SERVICE))).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public void hideSoftInputFromWindow(final EditText editText) {
        editText.setHint("发泄你的牢骚");
        editText.setText("");
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    editText.clearFocus();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
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
    class onCommentAsyncTask extends AsyncTask<Object, Void, Object[]> {

        private CommentAdapter adapter;
        private SwipeRefreshLayout mRefreshLayout;
        private int status;

        /**
         * 后台耗时操作,存在于子线程中
         *
         * @param params
         * @return
         */
        @Override
        protected Object[] doInBackground(Object[] params) {
            adapter = (CommentAdapter) params[0];
            mRefreshLayout = (SwipeRefreshLayout) params[1];
            status = (int) params[2];
            try {
                if (status == 1) {
                    Map<String, Object> struct = new HashMap<>();
                    struct.put("status", "approved");
                    struct.put("number", setting.getCommentsPageSize());
                    if (unAllComment) struct.put("cid", cid);
                    struct.put("offset", adapter.getPublishItemCount() + setting.getCommentsPageSize());
                    return new Object[]{new Object[]{(XMLRPCService.getComments(struct))[1]}};
                } else if (status == 0) {
                    String[] statusArray = new String[]{"spam", "waiting", "approved"};
                    String[] strNumber = new String[]{"99", "99", setting.getStringCommentsPageSize()};
                    Object[] comments = new Object[statusArray.length];
                    int i = 0;
                    for (String array : statusArray) {
                        Map<String, Object> struct = new HashMap<>();
                        struct.put("status", array);
                        struct.put("number", strNumber[i]);
                        if (unAllComment) struct.put("cid", cid);
                        comments[i] = XMLRPCService.getComments(struct)[1];
                        i++;
                    }
                    return new Object[]{comments};
                } else {
                    return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * 耗时方法结束后执行该方法,主线程中
         *
         * @param result
         */
        @Override
        protected void onPostExecute(Object[] result) {
            if (((Object[]) result[0]).length > 0) {
                Object[] comments = OtherUtil.mergeObject((Object[]) result[0]);
                if (status == 1) {
                    addRequestStatus = 0;
                    adapter.addItem(comments);
                } else {
                    if (!unAllComment)
                        personageACache.put(ConstUtils.COMMENTS_CACHE_NAME, comments);
                    adapter.setVerticalDataList(comments);
                }
            } else {
                if (status == 1) addRequestStatus = 0;
                OtherUtil.toast(CommentActivity.this, "没有更多的评论了");
            }
            mRefreshLayout.setRefreshing(false);
        }
    }
}
