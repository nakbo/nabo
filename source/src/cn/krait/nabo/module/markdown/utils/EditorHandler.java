package cn.krait.nabo.module.markdown.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import cn.krait.nabo.R;
import cn.krait.nabo.module.markdown.ui.EditorActivity;
import cn.krait.nabo.util.Log;
import ren.qinc.edit.PerformEdit;


/**
 * Created by zk on 2017/9/9.
 */

public class EditorHandler implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = EditorHandler.class.getSimpleName();

    private Activity activity;

    private EditText editText;
    private View view;

    private Toast toast;

    private PerformEdit performEdit;
    private SharedPreferences preferences;

    public static final String TAG_OPERATION = "ot_operation";

    public EditorHandler(Activity activity, View view, EditText editText) {
        this.editText = editText;
        this.view = view;
        this.activity = activity;

        performEdit = new PerformEdit(editText);
        performEdit.setDefaultText(editText.getText());

        preferences = activity.getSharedPreferences(TAG_OPERATION, Context.MODE_PRIVATE);

        long time = System.currentTimeMillis();
        initData();
        initView();
        Log.d(TAG, "time:" + (System.currentTimeMillis() - time));
    }

    private void initView() {
        LinearLayout linearLayout = view.findViewById(R.id.linear);

        final LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater == null) return;

        // 简单排序
        OperationType[] types = OperationType.values();
        for (int i = 0; i < types.length; i++) {
            for (int j = i + 1; j < types.length; j++) {
                if (types[j].order > types[i].order) {
                    OperationType type = types[i];
                    types[i] = types[j];
                    types[j] = type;
                }
            }
        }

        for (OperationType type : types) {
            final FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.linear_item, linearLayout, false);
            ImageView imageView = (ImageView) frameLayout.findViewById(R.id.imageView);
            imageView.setVisibility(View.VISIBLE);
            imageView.setOnClickListener(this);
            imageView.setOnLongClickListener(this);
            imageView.setTag(type.key);
            imageView.setImageResource(type.src);
            linearLayout.addView(frameLayout);
        }

    }

    private void initData() {
        for (OperationType type : OperationType.values()) {
            int order = preferences.getInt(type.key, 0);
            if (type.order < order)
                type.order = order;
        }
    }

    @Override
    public void onClick(View v) {
        editText.requestFocus();

        if (OperationType.IMG.key.equals(v.getTag())) {
            openImg();
        } else if (OperationType.BOLD.key.equals(v.getTag())) {
            addFormatBold();
        } else if (OperationType.ITALIC.key.equals(v.getTag())) {
            addFormatItalic();
        } else if (OperationType.HEADER_1.key.equals(v.getTag())) {
            addFormatHeader1();
        } else if (OperationType.HEADER_2.key.equals(v.getTag())) {
            addFormatHeader2();
        } else if (OperationType.HEADER_3.key.equals(v.getTag())) {
            addFormatHeader3();
        } else if (OperationType.HEADER_4.key.equals(v.getTag())) {
            addFormatHeader4();
        } else if (OperationType.HEADER_5.key.equals(v.getTag())) {
            addFormatHeader5();
        } else if (OperationType.QUOTE.key.equals(v.getTag())) {
            addFormatQuote();
        } else if (OperationType.LINK.key.equals(v.getTag())) {
            addLink();
        } else if (OperationType.LIST.key.equals(v.getTag())) {
            addList();
        } else if (OperationType.UNDO.key.equals(v.getTag())) {
            performEdit.undo();
        } else if (OperationType.REDO.key.equals(v.getTag())) {
            performEdit.redo();
        } else if (OperationType.STRIKE.key.equals(v.getTag())) {
            addStrike();
        } else if (OperationType.TIME.key.equals(v.getTag())) {
            addTime();
        }

        for (OperationType type : OperationType.values()) {
            if (type.key.equals(v.getTag())) {
                type.order = type.order + 1;
                preferences.edit().putInt(type.key, type.order).apply();
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        for (OperationType type : OperationType.values()) {
            if (type.key.equals(v.getTag())) {
                showToast(type.describe);
            }
        }
        return false;
    }

    public void openImg() {
        if (!PermissionUtils.storage(activity)) return;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, EditorActivity.REQUEST_CODE_IMG);
    }

    public void addImg(Uri uri) {
        String path = MarkdownUtils.getRealFilePath(activity, uri);
        if (path == null) {
            Toast.makeText(activity, "获取图片失败", Toast.LENGTH_LONG).show();
        } else {
            String string = "\n![](file://" + path + ")";
            editText.getText().insert(editText.getSelectionStart(), string);
        }
    }

    public void replace(String former, String latter) {
        String text = editText.getText().toString();
        editText.setText(text.replaceAll(former,latter));
    }

    public void addImg(String url) {
        String string = "\n![](" + url + ")";
        editText.getText().insert(editText.getSelectionStart(), string);
    }

    public void addFormatBold() {
        editText.getText().insert(editText.getSelectionStart(), "****");
        editText.setSelection(editText.getSelectionStart() - 2);
    }

    public void addFormatItalic() {
        editText.getText().insert(editText.getSelectionStart(), "**");
        editText.setSelection(editText.getSelectionStart() - 1);
    }

    public void addFormatHeader1() {
        editText.getText().insert(editText.getSelectionStart(), "\n# ");
    }

    public void addFormatHeader2() {
        editText.getText().insert(editText.getSelectionStart(), "\n## ");
    }

    public void addFormatHeader3() {
        editText.getText().insert(editText.getSelectionStart(), "\n### ");
    }

    public void addFormatHeader4() {
        editText.getText().insert(editText.getSelectionStart(), "\n#### ");
    }

    public void addFormatHeader5() {
        editText.getText().insert(editText.getSelectionStart(), "\n##### ");
    }

    public void addFormatQuote() {
        editText.getText().insert(editText.getSelectionStart(), "\n>");
    }

    public void addLink() {
        editText.getText().insert(editText.getSelectionStart(), "[]()");
        editText.setSelection(editText.getSelectionStart() - 3);
    }

    public void addList() {
        editText.getText().insert(editText.getSelectionStart(), "\n- ");
    }

    public void addStrike() {
        editText.getText().insert(editText.getSelectionStart(), "<del></del>");
        editText.setSelection(editText.getSelectionStart() - 6);
    }

    public void addTime() {
        editText.getText().insert(editText.getSelectionStart(), MarkdownUtils.getCurrentTime());
    }


    private void showToast(final int text) {
        if (toast == null) {
            toast = Toast.makeText(activity, text, Toast.LENGTH_LONG);
            toast.show();
        } else {
            toast.cancel();
            toast = Toast.makeText(activity, text, Toast.LENGTH_LONG);
            toast.show();
        }
    }


}
