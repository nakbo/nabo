package cn.krait.nabo.activity.personage.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import cn.krait.nabo.R;
import cn.krait.nabo.activity.inherit.InitialActivity;
import cn.krait.nabo.util.AliPayUtil;
import cn.krait.nabo.util.ManifestUtils;
import cn.krait.nabo.util.OtherUtil;

public class AboutActivity extends InitialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar aboutToolbar = (Toolbar) findViewById(R.id.about_toolbar);
        aboutToolbar.setTitle("");
        this.setSupportActionBar(aboutToolbar);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        aboutToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ((RelativeLayout) findViewById(R.id.btn_sponsorship)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog aliyPay = new AlertDialog.Builder(AboutActivity.this)
                        .setTitle("赞助我们")
                        .setMessage("客官你是否确定用支付宝或爱发电以此来赞助我们?")
                        .setPositiveButton("打赏", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this)
                                        .setTitle("赞助方式")
                                        .setMessage("客官,赞助我们的方式有两种\n支付宝和爱发电,建议用爱发电赞助噢!\n点击下边按钮即可去赞助噢!")
                                        .setPositiveButton("支付宝", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (AliPayUtil.hasInstalledAlipayClient(AboutActivity.this)) {
                                                    AliPayUtil.startAlipayClient(AboutActivity.this, "https://qr.alipay.com/fkx04317yq3xp7zvyrsjs10");
                                                } else {
                                                    Toast.makeText(AboutActivity.this, "客官你还没有安装支付宝呢！", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }).setNegativeButton("爱发电", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Uri uri = Uri.parse("https://afdian.net/@krait/plan");
                                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                startActivity(intent);
                                                dialogInterface.dismiss();
                                            }
                                        });
                                builder.create().show();

                            }
                        })
                        .setNegativeButton("考虑", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(AboutActivity.this, "客官你真的忍心不赞助我们吗！", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create();
                aliyPay.show();
            }
        });

        ((TextView) findViewById(R.id.about_versionName)).setText(ManifestUtils.getVersionName(AboutActivity.this));

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void about(View view) {
        switch (view.getId()) {
            case R.id.btn_official_siteUrl:
                OtherUtil.openWebView(AboutActivity.this, "https://" + getResources().getString(R.string.official_siteUrl));
                break;
            case R.id.btn_developer:
            case R.id.btn_developer_github:
                OtherUtil.openWebView(AboutActivity.this, "https://" + getResources().getString(R.string.developer_github));
                break;
        }
    }
}
