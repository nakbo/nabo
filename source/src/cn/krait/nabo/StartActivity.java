package cn.krait.nabo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.ocnyang.contourview.ContourView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.jetbrains.annotations.NotNull;

import cn.krait.nabo.activity.register.LoginActivity;
import cn.krait.nabo.activity.MainActivity;
import cn.krait.nabo.activity.inherit.InitialActivity;
import cn.krait.nabo.service.XMLRPCService;
import cn.krait.nabo.util.XMLRPCUtils;
import io.reactivex.functions.Consumer;

/**
 * @author 权那他(Kraity)
 * @date 2019/7/1.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class StartActivity extends InitialActivity {

    private ProgressBar progressBar;

    @Override
    @SuppressLint("CheckResult")
    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_start);
        Initialization();

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        ).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean granted) throws Exception {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        switch (personage.getLoginInt()) {
                            case 1:
                                XMLRPCService XMLRPCService = new XMLRPCService();
                                XMLRPCService.init(
                                        personage.getSiteUrl(),
                                        personage.getId(),
                                        personage.getName(),
                                        personage.getPassword()
                                );
                                try {
                                    XMLRPCUtils util = new XMLRPCUtils(personage, personageACache, XMLRPCService);
                                    util.setPersonage();
                                    if (personageACache.getAsString("unRefresh") == null) {
                                        util.setStat();
                                        util.setGravatar();
                                        personageACache.put("unRefresh", true);
                                    }
                                    runOnUiThread(1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Message msg = handler.obtainMessage();
                                    msg.what = 2;
                                    handler.sendMessage(msg);
                                }
                                break;
                            case 0:
                                runOnUiThread(0);
                        }
                    }
                }.start();
            }
        });

    }

    @SuppressLint("HandlerLeak")
    private
    Handler handler = new Handler() {
        public void handleMessage(@NotNull final android.os.Message msg) {
            final int what = msg.what;
            if (what == 2) {
                AlertDialog();
            } else {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent;
                        if (what == 1) {
                            intent = new Intent(StartActivity.this, MainActivity.class);
                        } else {
                            intent = new Intent(StartActivity.this, LoginActivity.class);
                        }
                        startActivity(intent);
                        StartActivity.this.finish();

                    }
                }, 100);
            }
        }
    };

    private void runOnUiThread(final int s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = s;
                handler.sendMessage(msg);
            }
        });
    }

    private void AlertDialog() {
        new AlertDialog.Builder(StartActivity.this)
                .setTitle("请求失败")
                .setMessage("尝试请求连接客官的博客失败\n可能是因为密码错误或者博客发生异常")
                .setPositiveButton("再试连接", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(
                                StartActivity.this,
                                StartActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).setNegativeButton("修改账户", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(
                        StartActivity.this,
                        LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    /**
     * 状态栏和导航栏消失且全屏
     */
    public void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        window.setAttributes(params);

    }

    public void Initialization() {
        ContourView contourView = findViewById(R.id.contourView);
        contourView.setBackgroundColor(setting.getColorPrimary());
        contourView.setShaderEndColor(setting.getColorAccent());
        contourView.setShaderStartColor(setting.getColorPrimary());

        progressBar = (ProgressBar) findViewById(R.id.spin_kit);
        Sprite threeBounce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBounce);
    }

}
