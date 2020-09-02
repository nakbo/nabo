package cn.krait.nabo.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.einmalfel.earl.EarlParser;
import com.einmalfel.earl.Feed;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.DataFormatException;

import cn.krait.nabo.R;
import cn.krait.nabo.adapter.MRVLAdapter;
import cn.krait.nabo.service.XMLRPCService;
import cn.krait.nabo.module.ACache.ACache;
import cn.krait.nabo.util.ConstUtils;
import cn.krait.nabo.util.OtherUtil;
import cn.krait.nabo.module.personage.Personage;
import cn.krait.nabo.module.object.RssObject;
import cn.krait.nabo.module.object.RssUrlObject;
import cn.krait.nabo.module.object.RssesObject;
import cn.krait.nabo.module.object.SettingObject;

/**
 * @author 权那他(Kraity)
 * @date 2019/7/28.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class MainLastFragment extends Fragment {

    private Personage personage;
    private ACache personageACache;
    private XMLRPCService XMLRPCService;
    private SettingObject setting;
    private RecyclerView rcvVertical;
    private RssesObject rssesObject;
    private SwipeRefreshLayout mRefreshLayout;
    private MRVLAdapter adapter;

    public MainLastFragment(Personage p, ACache a, XMLRPCService x, SettingObject s) {
        personage = p;
        personageACache = a;
        XMLRPCService = x;
        setting = s;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(inflater.getContext(), R.layout.fragment_last, null);


        rcvVertical = view.findViewById(R.id.rcv_vertical);
        mRefreshLayout = view.findViewById(R.id.srl_refresh);
        adapter = new MRVLAdapter(
                getContext(),
                personage,
                personageACache
        );
        rssesObject = new RssesObject();
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
                startAsyncTask();
            }
        });

        try {
            LinearLayoutManager managerVertical = new LinearLayoutManager(getContext());
            managerVertical.setOrientation(RecyclerView.VERTICAL);
            rcvVertical.setLayoutManager(managerVertical);
            rcvVertical.setHasFixedSize(true);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (personageACache.getAsString(ConstUtils.RSS_LIST_STATUS) == null) {
            startAsyncTask();
        } else {
            rcvVertical.setAdapter(adapter);
            adapter.orientation((RssObject[]) personageACache.getAsObject(ConstUtils.RSS_ACACHE));
        }

        view.findViewById(R.id.fab_add).setBackgroundTintList((ColorStateList.valueOf(setting.getColorPrimary())));
        view.findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choiceDialog();
            }
        });
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    class onRssAsyncTask extends AsyncTask<Object, Void, Feed> {
        String url;

        @Override
        protected Feed doInBackground(Object[] params) {
            url = (String) params[0];
            try {
                InputStream inputStream = new URL(url).openConnection().getInputStream();
                return EarlParser.parseOrThrow(inputStream, 0);
            } catch (XmlPullParserException | DataFormatException | IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Feed feed) {
            mRefreshLayout.setRefreshing(false);
            if (feed != null) {
                accomplishAsyncTask(feed);
            } else {
                Toast.makeText(getContext(), "无效Rss源 " + url, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startAsyncTask() {
        rssesObject.clear();
        String[] rss = personage.getRssUrlItem().getRssItemAble();
        for (String url : rss) {
            mRefreshLayout.setRefreshing(true);
            new onRssAsyncTask().execute(url);
        }
    }

    private void accomplishAsyncTask(Feed feed) {
        if (feed.getItems().size() > 0) {
            rssesObject.add(feed);
            personageACache.put(ConstUtils.RSS_ACACHE, rssesObject.getRssArray());
            personageACache.put(ConstUtils.RSS_LIST_STATUS, true);
            rcvVertical.setAdapter(adapter);
            adapter.orientation(rssesObject.getRssArray());
        }
    }

    private void manangeDialog() {
        final String[] items = personage.getRssUrlItem().getRssItem();
        final int[] choice = {-1};
        new AlertDialog.Builder(getContext())
                .setTitle("订阅管理")
                .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        choice[0] = i;
                    }
                }).setPositiveButton("确定删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (choice[0] != -1) {
                    final String urlDelete = items[choice[0]];
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext())
                            .setTitle("删除")
                            .setMessage("你是否确定删除\n" + urlDelete)
                            .setNegativeButton("确定删除", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    RssUrlObject rssUrlObject1 = personage.getRssUrlItem();
                                    if (rssUrlObject1.getRssItem().length <= 1) {
                                        OtherUtil.toast(getContext(), "无法删除唯一的订阅源");
                                        return;
                                    }
                                    rssUrlObject1.remove(choice[0]);
                                    personage.setRssUrlItem(rssUrlObject1);
                                    Toast.makeText(getContext(), "删除成功,请下拉刷新", Toast.LENGTH_SHORT).show();
                                }
                            });
                    builder1.create().show();
                }
            }
        })
                .create().show();
    }

    private void addDialog() {
        final EditText editText = new EditText(getContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setTitle("添加Rss地址").setView(editText)
                .setPositiveButton("确认添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (editText.getText().toString().isEmpty()) return;
                        String text = editText.getText().toString().replaceAll("\\s*", "");
                        String[] ss = text.split("@");
                        boolean saveAble = false;
                        for (String s : ss) {
                            Log.e("e", s);
                            if (Patterns.WEB_URL.matcher(s).matches()) {
                                saveAble = true;
                            } else {
                                saveAble = false;
                                break;
                            }
                        }
                        if (saveAble) {
                            RssUrlObject rssUrlObject1 = personage.getRssUrlItem();
                            for (String s : ss) {
                                rssUrlObject1.add(s);
                            }
                            personage.setRssUrlItem(rssUrlObject1);
                            Toast.makeText(getContext(), "保存成功,请下拉刷新", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "含非法网址,保存失败", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        builder.create().show();
    }

    private void choiceDialog() {
        final RssUrlObject rssUrlObject = personage.getRssUrlItem();
        final String[] items = rssUrlObject.getRssItem();
        boolean[] isSelect = rssUrlObject.getItemAble();
        new AlertDialog.Builder(getContext())
                .setTitle("切换订阅")
                .setMultiChoiceItems(items, isSelect, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            rssUrlObject.change(i, new Object[]{true, items[i]});
                        } else {
                            rssUrlObject.change(i, new Object[]{false, items[i]});
                        }
                    }
                }).setPositiveButton("确定订阅", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                personage.setRssUrlItem(rssUrlObject);
                startAsyncTask();
            }
        }).setNeutralButton("订阅管理", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                manangeDialog();
            }
        }).setNegativeButton("添加订阅", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addDialog();
            }
        }).create().show();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}