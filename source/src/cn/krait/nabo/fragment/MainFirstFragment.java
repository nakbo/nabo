package cn.krait.nabo.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import cn.krait.nabo.R;
import cn.krait.nabo.activity.page.EditPostActivity;
import cn.krait.nabo.adapter.MRVFAdapter;
import cn.krait.nabo.service.XMLRPCService;
import cn.krait.nabo.module.ACache.ACache;
import cn.krait.nabo.util.ConstUtils;
import cn.krait.nabo.util.OtherUtil;
import cn.krait.nabo.module.personage.Personage;
import cn.krait.nabo.module.object.NewPostObject;
import cn.krait.nabo.module.object.SettingObject;

/**
 * @author 权那他(Kraity)
 * @date 2019/7/28.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class MainFirstFragment extends Fragment {

    private Personage personage;
    private ACache personageACache;
    private XMLRPCService XMLRPCService;
    private RecyclerView mRecyclerView;
    private MRVFAdapter adapter;
    private SwipeRefreshLayout mRefreshLayout;
    private SettingObject setting;
    private int addRequestStatus;
    private boolean unLastLoadingItem, adapterAble;


    public MainFirstFragment(Personage p, ACache a, XMLRPCService x, SettingObject s) {
        personage = p;
        personageACache = a;
        XMLRPCService = x;
        setting = s;

    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(inflater.getContext(), R.layout.fragment_first, null);

        adapter = new MRVFAdapter(
                getContext(),
                personage,
                personageACache,
                XMLRPCService
        );
        mRefreshLayout = view.findViewById(R.id.srl_refresh);
        mRecyclerView = view.findViewById(R.id.rcv_header);
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
                new onRefreshAsyncTask().execute(adapter, mRefreshLayout, 0, true);

            }
        });

        setAdapterAble();

        addRequestStatus = 0;
        unLastLoadingItem = true;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(null);
        final StaggeredGridLayoutManager managerVertical = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        managerVertical.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.setLayoutManager(managerVertical);
        mRecyclerView.setAdapter(adapter);
        if (adapterAble)
            adapter.orientation(
                    (Object[]) personageACache.getAsObject(ConstUtils.POSTS_CACHE_NAME),
                    ((Object[]) personageACache.getAsObject(ConstUtils.STAT_CACHE_NAME))[0]
            );
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int s) {
                super.onScrollStateChanged(recyclerView, s);
                if (unLastLoadingItem) {
                    int[] lastPositions = new int[managerVertical.getSpanCount()];
                    managerVertical.findLastVisibleItemPositions(lastPositions);
                    if (addRequestStatus == 0 && findMax(lastPositions) + 1 == adapter.getItemCount()) {
                        addRequestStatus = 1;
                        mRefreshLayout.setRefreshing(true);
                        new onRefreshAsyncTask().execute(adapter, mRefreshLayout, 1, true);
                    }
                }
            }

            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                managerVertical.invalidateSpanAssignments();

            }
        });

        mRefreshLayout.setRefreshing(true);
        new onRefreshAsyncTask().execute(adapter, mRefreshLayout, 0, !adapterAble);

        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditPostActivity.class);
                Bundle extras = new Bundle();
                NewPostObject post = new NewPostObject();
                post.setCid(1);
                post.setPattern("new");
                post.setType("post");
                post.setAllowComment(1);
                post.setAllowPing(1);
                extras.putSerializable("object", post);
                intent.putExtras(extras);
                intent.putExtra("requestCode", 1002);//主页面到编辑文章页面 暂时代号1002
                startActivityForResult(intent, 3002);
            }
        });
        view.findViewById(R.id.fab).setBackgroundTintList((ColorStateList.valueOf(setting.getColorPrimary())));
        return view;
    }

    private void setAdapterAble() {
        adapterAble = personageACache.getAsObject(ConstUtils.POSTS_CACHE_NAME) != null && personageACache.getAsObject(ConstUtils.STAT_CACHE_NAME) != null;
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if ((result != null) & (resultCode == 3001)) {
            int position = result.getIntExtra("position", 0);
            if (position < 1) return;
            adapter.removeItem(position);
            mRefreshLayout.setRefreshing(true);
            new onRefreshAsyncTask().execute(adapter, mRefreshLayout, 0, false);
        } else if ((result != null) & (resultCode == 3002)) {
            mRefreshLayout.setRefreshing(true);
            new onRefreshAsyncTask().execute(adapter, mRefreshLayout, 0, true);
        }
    }

    /**
     * 刷新
     */
    @SuppressLint("StaticFieldLeak")
    class onRefreshAsyncTask extends AsyncTask<Object, Void, Object[]> {

        private MRVFAdapter adapter;
        private SwipeRefreshLayout mRefreshLayout;
        private int status;
        private boolean refreshPost;

        /**
         * 后台耗时操作,存在于子线程中
         *
         * @param params
         * @return
         */
        @Override
        protected Object[] doInBackground(Object[] params) {

            adapter = (MRVFAdapter) params[0];
            mRefreshLayout = (SwipeRefreshLayout) params[1];
            status = (int) params[2];
            refreshPost = (boolean) params[3];
            try {
                if (refreshPost) {
                    Map<String, Object> struct = new HashMap<>();
                    struct.put("number", setting.getPostsPageSize());
                    struct.put("text_html", false);
                    struct.put("text_markdown", false);
                    if (status == 1) {
                        struct.put("offset", adapter.getItemCount() - 1 + setting.getPostsPageSize());
                    }
                    return new Object[]{
                            XMLRPCService.getPosts(struct),
                            XMLRPCService.getStat()
                    };
                } else {
                    return new Object[]{null, XMLRPCService.getStat()};
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
            Object stats = ((Object[]) result[1])[1];
            personageACache.put(ConstUtils.STAT_CACHE_NAME, new Object[]{stats});
            if (refreshPost) {
                if (((Object[]) ((Object[]) result[0])[1]).length > 0) {
                    Object[] posts = (Object[]) ((Object[]) result[0])[1];
                    if (status == 1) {
                        addRequestStatus = 0;
                        adapter.addItem(
                                posts, stats
                        );
                    } else {
                        unLastLoadingItem = true;
                        adapter.orientation(
                                posts, stats

                        );
                    }
                } else {
                    if (status == 1) {
                        addRequestStatus = 0;
                        unLastLoadingItem = false;
                    }
                    OtherUtil.toast(getContext(), "没有更多的了");
                }
            } else {
                adapter.refreshStat(stats);
            }

            mRefreshLayout.setRefreshing(false);
        }
    }

}
