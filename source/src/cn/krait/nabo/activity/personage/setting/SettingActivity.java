package cn.krait.nabo.activity.personage.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import cn.krait.nabo.R;
import cn.krait.nabo.StartActivity;
import cn.krait.nabo.activity.inherit.InitialActivity;
import cn.krait.nabo.util.ConstUtils;
import cn.krait.nabo.util.OtherUtil;
import cn.krait.nabo.module.object.SettingObject;

public class SettingActivity extends InitialActivity {

    private TextView postSize, commentSize, postTextLength;
    private Switch allowCommentsRefresh,
            checkVersionAble,
            imageUrlReplaceAble,
            articleDeleteAble;
    private CardView colorAccentView, colorPrimaryView;
    private boolean hasChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar aboutToolbar = (Toolbar) findViewById(R.id.about_toolbar);
        aboutToolbar.setTitle("");
        this.setSupportActionBar(aboutToolbar);
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        hasChange = false;
        postSize = (TextView) findViewById(R.id.btn_setting_postPageSize_1);
        commentSize = (TextView) findViewById(R.id.btn_setting_commentPageSize_1);
        postTextLength = (TextView) findViewById(R.id.btn_setting_postTextLength_1);
        allowCommentsRefresh = findViewById(R.id.btn_allowCommentsRefresh_1);
        colorPrimaryView = findViewById(R.id.colorPrimaryView);
        colorAccentView = findViewById(R.id.colorAccentView);
        checkVersionAble = findViewById(R.id.btn_checkVersionAble_1);
        imageUrlReplaceAble = findViewById(R.id.btn_imageUrlReplaceAble_1);
        articleDeleteAble = findViewById(R.id.btn_articleDeleteAble_1);

        init(setting);

        allowCommentsRefresh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setting.setAllowCommentsRefresh(isChecked);
                handleMessage(true);
            }
        });

        checkVersionAble.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setting.setCheckVersionAble(b);
                handleMessage(true);
            }
        });

        imageUrlReplaceAble.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setting.setImageUrlReplaceAble(isChecked);
                handleMessage(true);
            }
        });

        articleDeleteAble.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setting.setArticleDeleteAble(b);
                handleMessage(true);
            }
        });

        aboutToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.btn_colorPrimary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[][] c = ConstUtils.ColorArray[0];
                final String[] color = new String[c.length];
                String[] name = new String[c.length];
                for (int i = 0; i < c.length; i++) {
                    color[i] = c[i][0];
                    name[i] = c[i][1];
                }
                final int[] choice = {0};
                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("选择主色")
                        .setSingleChoiceItems(name, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                choice[0] = i;
                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setting.setColorPrimary(color[choice[0]]);
                        colorPrimaryView.setCardBackgroundColor(setting.getColorPrimary());
                        handleMessage(true);
                    }
                })
                        .create().show();
            }
        });


        findViewById(R.id.btn_colorAccent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[][] c = ConstUtils.ColorArray[1];
                final String[] color = new String[c.length];
                String[] name = new String[c.length];
                for (int i = 0; i < c.length; i++) {
                    color[i] = c[i][0];
                    name[i] = c[i][1];
                }
                final int[] choice = {0};
                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("选择强调色")
                        .setSingleChoiceItems(name, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                choice[0] = i;
                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setting.setColorAccent(color[choice[0]]);
                        colorAccentView.setCardBackgroundColor(setting.getColorAccent());
                        handleMessage(true);
                    }
                })
                        .create().show();
            }
        });

        findViewById(R.id.btn_recover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("恢复默认设置")
                        .setMessage("是否恢复到默认设置")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setting.initialization();
                                handleMessage(true);
                                init(personage.getSetting());
                            }
                        })
                        .create().show();
            }
        });

    }

    public void init(SettingObject setting) {
        colorPrimaryView.setCardBackgroundColor(setting.getColorPrimary());
        colorAccentView.setCardBackgroundColor(setting.getColorAccent());
        postSize.setText(setting.getStringPostsPageSize());
        commentSize.setText(setting.getStringCommentsPageSize());
        postTextLength.setText(setting.getStringPostTextLength());
        allowCommentsRefresh.setChecked(setting.getAllowCommentsRefresh());
        checkVersionAble.setChecked(setting.getCheckVersionAble());
        imageUrlReplaceAble.setChecked(setting.getImageUrlReplaceAble());
        articleDeleteAble.setChecked(setting.getArticleDeleteAble());
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(SettingActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 0:
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void about(View view) {
        final EditText editText = new EditText(SettingActivity.this);
        switch (view.getId()) {
            case R.id.btn_setting_commentPageSize:
                editText.setText(setting.getStringCommentsPageSize());
                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("每页评论数目")
                        .setView(editText)
                        .setMessage("这里的输入每页评论数目")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String s = editText.getText().toString();
                                if (s.isEmpty() || !OtherUtil.isNumeric(s)) return;
                                int number = Integer.parseInt(s);
                                if (number < 7 || number > 50) {
                                    handleMessage("建议设置在7~50范围内");
                                    return;
                                }
                                setting.setCommentsPageSize(number);
                                commentSize.setText(s);
                                handleMessage();
                            }
                        })
                        .create().show();
                break;
            case R.id.btn_setting_postPageSize:
                editText.setText(setting.getStringPostsPageSize());
                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("每页文章数目")
                        .setView(editText)
                        .setMessage("这里的输入每页文章数目")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String s = editText.getText().toString();
                                if (s.isEmpty() || !OtherUtil.isNumeric(s)) return;
                                int number = Integer.parseInt(s);
                                if (number < 7 || number > 50) {
                                    handleMessage("建议设置在7~50范围内");
                                    return;
                                }
                                setting.setPostsPageSize(number);
                                postSize.setText(s);
                                handleMessage();
                            }
                        })
                        .create().show();

                break;

            case R.id.btn_setting_postTextLength:
                editText.setText(setting.getStringPostTextLength());
                new AlertDialog.Builder(SettingActivity.this)
                        .setTitle("文章瀑布流字数")
                        .setView(editText)
                        .setMessage("这里的输入文章瀑布流字数\n文章内容字符数小于该数则显示小瀑布流块")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String s = editText.getText().toString();
                                if (s.isEmpty() || !OtherUtil.isNumeric(s)) return;
                                int number = Integer.parseInt(s);
                                if (number < 12) {
                                    handleMessage("建议设置不小于12");
                                    return;
                                }
                                setting.setPostTextLength(number);
                                postTextLength.setText(s);
                                handleMessage();
                            }
                        })
                        .create().show();

                break;
        }
    }

    public void handleMessage() {
        Message msg = new Message();
        msg.what = 1;
        msg.obj = "保存成功";
        handler.handleMessage(msg);
        save();
    }

    public void handleMessage(String s) {
        Message msg = new Message();
        msg.what = 1;
        msg.obj = s;
        handler.handleMessage(msg);
    }

    public void handleMessage(boolean b) {
        if (b) save();
    }

    public void save() {
        hasChange = true;
        personage.setSetting(setting);
    }

    @Override
    public void onBackPressed() {
        if (hasChange) {
            Intent intent = new Intent(
                    SettingActivity.this,
                    StartActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            super.onBackPressed();
        }
    }
}
