package cn.krait.nabo.activity.page;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.krait.nabo.R;
import cn.krait.nabo.activity.inherit.InitialActivity;
import cn.krait.nabo.adapter.page.MediaAdapter;
import cn.krait.nabo.util.ConstUtils;

/**
 * @author 权那他(Kraity)
 * @date 2019/8/11.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class MediaActivity extends InitialActivity {

    private RecyclerView rcvGrid;
    private Toolbar mediaToolbar;

    private List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        mediaToolbar = (Toolbar) findViewById(R.id.media_toolbar);
        mediaToolbar.setTitle("高级管理");
        mediaToolbar.setSubtitle("附件管理");
        this.setSupportActionBar(mediaToolbar);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        rcvGrid = findViewById(R.id.rcv_vertical);
        final SwipeRefreshLayout mRefreshLayout = findViewById(R.id.srl_refresh);
        final MediaAdapter adapter = new MediaAdapter(this, personage, personageACache, XMLRPCService);

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
                new onMediaAsyncTask().execute(adapter, mRefreshLayout);
            }
        });


        GridLayoutManager managerGrid = new GridLayoutManager(this, 2);

        rcvGrid.setLayoutManager(managerGrid);
        rcvGrid.setHasFixedSize(true);
        rcvGrid.setAdapter(adapter);

        if (personageACache.getAsJSONArray(ConstUtils.MEDIA_ACACHE) != null)
            adapter.setGridDataList(personageACache.getAsJSONArray(ConstUtils.MEDIA_ACACHE));

        mRefreshLayout.setRefreshing(true);
        new onMediaAsyncTask().execute(adapter, mRefreshLayout);

        mediaToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    @SuppressLint("StaticFieldLeak")
    class onMediaAsyncTask extends AsyncTask<Object, Void, Boolean> {

        private MediaAdapter adapter;
        private SwipeRefreshLayout mRefreshLayout;

        /**
         * 后台耗时操作,存在于子线程中
         *
         * @param params
         * @return
         */
        @Override
        protected Boolean doInBackground(Object[] params) {
            adapter = (MediaAdapter) params[0];
            mRefreshLayout = (SwipeRefreshLayout) params[1];
            boolean able;
            try {
                XMLRPCUtils.setMediasRecent();
                able = true;
            } catch (Exception e) {
                able = false;
                e.printStackTrace();
            }
            return able;
        }

        /**
         * 耗时方法结束后执行该方法,主线程中
         *
         * @param result
         */
        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                rcvGrid.setAdapter(adapter);
                JSONArray medias = personageACache.getAsJSONArray(ConstUtils.MEDIA_ACACHE);
                adapter.setGridDataList(medias);
                mRefreshLayout.setRefreshing(false);
            }
        }
    }
}
