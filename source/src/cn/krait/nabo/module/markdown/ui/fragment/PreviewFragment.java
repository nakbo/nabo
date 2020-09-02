package cn.krait.nabo.module.markdown.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.krait.nabo.R;
import cn.krait.nabo.module.markdown.ui.EditorActivity;
import cn.krait.nabo.module.markdown.widget.MarkdownView;

/**
 * Created by zk on 2017/12/11.
 */

public class PreviewFragment extends Fragment {

    private LinearLayout main;

    public MarkdownView markdownView;

    private EditorActivity activity;

    private static final String TAG = PreviewFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (EditorActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (main == null) {
            main = (LinearLayout) inflater.inflate(R.layout.fragment_preview, container, false);
            initViews(main);
        }
        return main;
    }

    private void initViews(LinearLayout main) {
        markdownView = (MarkdownView) main.findViewById(R.id.markdownView);

        markdownView.setLinkClickListener(activity.getLinkClickListener());
        markdownView.setImgClickListener(activity.getImgClickListener());
    }

    public void load(String text) {
        markdownView.setTextInBackground(text);
    }
}
