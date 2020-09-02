package cn.krait.nabo.activity.personage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

import cn.krait.nabo.R;
import cn.krait.nabo.StartActivity;
import cn.krait.nabo.activity.inherit.InitialActivity;
import cn.krait.nabo.activity.personage.setting.AboutActivity;
import cn.krait.nabo.activity.personage.setting.SettingActivity;
import cn.krait.nabo.activity.register.LoginActivity;
import cn.krait.nabo.util.ConstUtils;
import cn.krait.nabo.util.OtherUtil;
import cn.krait.nabo.module.object.AccountObject;

/**
 * @author 权那他(Kraity)
 * @date 2019/7/23.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class PersonageActivity extends InitialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personage);

        Toolbar personageToolbar = (Toolbar) findViewById(R.id.personage_toolbar);
        personageToolbar.setTitle("");
        this.setSupportActionBar(personageToolbar);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        personageToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ((ImageView) findViewById(R.id.personage_gravatar)).setImageBitmap(
                personageACache.getAsBitmap(ConstUtils.PERSONAGE_ACACHE_GRAVATAR)
        );
        ((TextView) findViewById(R.id.personage_screenName)).setText(
                personage.getScreenName()
        );
        ((TextView) findViewById(R.id.set_name)).setText(personage.getName());
        ((TextView) findViewById(R.id.set_password)).setText("••••••");//personage.getPassword()
        ((TextView) findViewById(R.id.set_email)).setText(personage.getEmail());
        ((TextView) findViewById(R.id.set_siteUrl)).setText(personage.getDomain());

        ((RelativeLayout) findViewById(R.id.btn_about)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonageActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        ((RelativeLayout) findViewById(R.id.btn_setting)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PersonageActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        ((TextView) findViewById(R.id.set_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog password = new AlertDialog.Builder(PersonageActivity.this)
                        .setTitle("密码")
                        .setMessage("客官你的密码为:" + personage.getPassword())
                        .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create();
                password.show();
            }
        });

        ((RelativeLayout) findViewById(R.id.btn_accountString)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<Object[]> accountArray = personage.getAccount().getAccountItems();
                final String[] items = new String[accountArray.size()];
                for (int i = 0; i < accountArray.size(); i++) {
                    Object[] account = accountArray.get(i);
                    items[i] = (String) account[0];
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonageActivity.this)
                        .setTitle("切换账户")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                final int position = i;
                                final Object[] account = accountArray.get(i);
                                final String name = (String) account[1];
                                final String password = (String) account[2];
                                final String xmlrpc = AccountObject.getXMLRpcUrl(account);
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("账号：").append(name).append("\n");
                                stringBuilder.append("密码：").append(password).append("\n");
                                stringBuilder.append("请求地址：").append(xmlrpc);
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(PersonageActivity.this)
                                        .setTitle("账户")
                                        .setMessage(stringBuilder.toString())
                                        .setPositiveButton("切换", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (personage.getName().toLowerCase().equals(name.toLowerCase()) && personage.getSiteUrl().toLowerCase().equals(xmlrpc.toLowerCase())) {
                                                    OtherUtil.toast(PersonageActivity.this, "无需切换,当前登录账户为该账户");
                                                    return;
                                                }
                                                AlertDialog.Builder builder2 = new AlertDialog.Builder(PersonageActivity.this)
                                                        .setTitle("切换账户")
                                                        .setMessage("客官你确定要切换为该账户吗?")
                                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                personage.setId(1);//定义id
                                                                personage.setName(name);//定义用户名
                                                                personage.setPassword(password);//定义密码
                                                                personage.setSiteUrl(xmlrpc);//定义博客网站
                                                                personage.setDomain((String) account[0]);
                                                                personage.setSslAble((boolean) account[3]);
                                                                personage.setPseudoAble((boolean) account[4]);
                                                                personage.setLogin(true);
                                                                personageACache.remove("unRefresh");
                                                                Intent intent = new Intent(
                                                                        PersonageActivity.this,
                                                                        StartActivity.class)
                                                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                startActivity(intent);
                                                            }
                                                        });
                                                builder2.create().show();
                                            }
                                        })
                                        .setNeutralButton("删除", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (personage.getName().toLowerCase().equals(name.toLowerCase()) && personage.getSiteUrl().toLowerCase().equals(xmlrpc.toLowerCase())) {
                                                    OtherUtil.toast(PersonageActivity.this, "请先切换账户,再删除该账户");
                                                    return;
                                                }
                                                AlertDialog.Builder builder2 = new AlertDialog.Builder(PersonageActivity.this)
                                                        .setTitle("删除账户")
                                                        .setMessage("客官你确定要删除该账户吗?")
                                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                AccountObject accountObject = personage.getAccount();
                                                                accountObject.delete(position);
                                                                personage.setAccount(accountObject);
                                                                OtherUtil.toast(PersonageActivity.this, "删除成功");
                                                            }
                                                        });
                                                builder2.create().show();
                                            }
                                        });
                                builder1.create().show();
                            }
                        })
                        .setPositiveButton("添加账户", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                logout();
                            }
                        });
                builder.create().show();
            }
        });

        ((RelativeLayout) findViewById(R.id.btn_logout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonageActivity.this)
                        .setTitle("退出登录")
                        .setMessage("客官你确定要退出登录吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                personage.setLogin(false);
                                logout();
                            }
                        });
                builder.create().show();
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

    public void logout() {
        personageACache.remove("unRefresh");
        Intent intent = new Intent(
                PersonageActivity.this,
                LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
