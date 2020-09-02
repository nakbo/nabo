package cn.krait.nabo.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONObject;

import cn.krait.nabo.R;
import cn.krait.nabo.activity.inherit.InitialActivity;
import cn.krait.nabo.activity.personage.PersonageActivity;
import cn.krait.nabo.fragment.MainFirstFragment;
import cn.krait.nabo.fragment.MainLastFragment;
import cn.krait.nabo.service.XMLRPCService;
import cn.krait.nabo.module.ACache.ACache;
import cn.krait.nabo.module.method.CheckVersion;
import cn.krait.nabo.util.ConstUtils;
import cn.krait.nabo.util.OtherUtil;
import cn.krait.nabo.module.personage.Personage;

/**
 * @author 权那他(Kraity)
 * @date 2019/7/1.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class MainActivity extends InitialActivity {

    private TabLayout mTl;
    private ViewPager mVp;
    private MainFirstFragment mainFirstFragment;
    private MainLastFragment mainLastFragment;
    private String[] titles = {"概要", "订阅"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTab();
        tabSelectedListener();
        mainFirstFragment = new MainFirstFragment(
                personage,
                personageACache,
                XMLRPCService,
                setting
        );
        mainLastFragment = new MainLastFragment(
                personage,
                personageACache,
                XMLRPCService,
                setting
        );
        MFAdapter mfadapter = new MFAdapter(
                getSupportFragmentManager(),
                personage,
                personageACache,
                XMLRPCService,
                titles
        );
        mVp.setAdapter(mfadapter);
        mTl.setupWithViewPager(mVp);

        clickListener();
        new onGravatarAsyncTask().execute();
        if (setting.getCheckVersionAble()) new onCheckAsyncTask().execute();
    }

    /**
     * 初始化init tab
     */
    @SuppressLint("ResourceAsColor")
    public void initTab() {
        mTl = findViewById(R.id.material_tabLayout);
        mVp = findViewById(R.id.material_viewPager);
        mTl.addTab(mTl.newTab().setText(titles[0]));
        mTl.addTab(mTl.newTab().setText(titles[1]));
        mTl.setSelectedTabIndicatorColor(setting.getColorPrimary());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * tabSelectedListener()
     */
    public void tabSelectedListener() {
        mTl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.fragment_tab_title);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextColor(mTl.getTabTextColors());
                textView.setTextSize(20);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.fragment_tab_title);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextSize(16);
                textView.setTypeface(Typeface.DEFAULT);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if ((result != null) & (resultCode == 3001)) {
            mainFirstFragment.onActivityResult(requestCode, resultCode, result);
        } else if ((result != null) & (resultCode == 3002)) {
            mainFirstFragment.onActivityResult(requestCode, resultCode, result);
        }
    }

    /**
     * clickListener
     */
    public void clickListener() {

        ((ImageView) findViewById(R.id.personage_image)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PersonageActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    class onGravatarAsyncTask extends AsyncTask<Object, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Object[] objects) {
            try {
                if (personageACache.getAsBitmap(ConstUtils.PERSONAGE_ACACHE_GRAVATAR) == null) {
                    return OtherUtil.getGravatar(personage.getEmail(), personageACache, ConstUtils.PERSONAGE_ACACHE_GRAVATAR);
                } else {
                    return personageACache.getAsBitmap(ConstUtils.PERSONAGE_ACACHE_GRAVATAR);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ((ImageView) findViewById(R.id.personage_image)).setImageBitmap(bitmap);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class onCheckAsyncTask extends AsyncTask<Object, Void, JSONObject> {
        JSONObject jsonObject;

        @Override
        protected JSONObject doInBackground(Object[] objects) {
            new CheckVersion(MainActivity.this) {
                @Override
                protected void successful(JSONObject o) {
                    jsonObject = o;
                }

                @Override
                protected void failing() {

                }

                @Override
                protected void Continue() {

                }
            };
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject o) {
            if (o != null) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("检查更新")
                        .setMessage("检测到新版本\n是否前往官网更新")
                        .setPositiveButton("前往更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Uri uri = Uri.parse("https://" + getResources().getString(R.string.official_siteUrl));
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        }).create().show();
            }
        }
    }

    public class MFAdapter extends FragmentPagerAdapter {

        private String[] mTitles;
        private Personage personage;
        private ACache personageACache;
        private cn.krait.nabo.service.XMLRPCService XMLRPCService;

        MFAdapter(FragmentManager fm, Personage p, ACache a, XMLRPCService x, String[] titles) {
            super(fm);
            personage = p;
            personageACache = a;
            XMLRPCService = x;
            mTitles = titles;


        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return mainFirstFragment;
                case 1:
                    return mainLastFragment;
            }
            return mainFirstFragment;

        }

        @Override
        public int getCount() {
            return mTitles.length;
        }


        /*
         *
         * 该函数是搭配TabLayout 布局所需重写的 ,如若不绑定TabLayout 布局，那么可以不重写
         *   mTl.setupWithViewPager(mVp);
         * */
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }
}
