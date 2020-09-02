package cn.krait.nabo.module.markdown.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.krait.nabo.util.Log;
import cn.krait.nabo.module.markdown.utils.MarkdownHandler;
import cn.krait.nabo.module.markdown.utils.MarkdownUtils;

import java.util.List;

/**
 * Created by zk on 2017/9/14.
 */

public class MarkdownView extends WebView {

    private static final String TAG = MarkdownView.class.getSimpleName();

    private String text;
    private List<String> imgList;

    private LinkClickListener linkClickListener;
    private ImgClickListener imgClickListener;

    public MarkdownView(Context context) {
        super(context);
        addClient();
    }

    public MarkdownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addClient();
    }

    public MarkdownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addClient();
    }

    public void setTextInBackground(String text) {
        this.text = text;
        MarkdownHandler.getInstance().toHtml(text, new MarkdownHandler.Callback() {
            @Override
            public void done(String html) {
                if (html != null) {
                    imgList = MarkdownUtils.getImgUrl(html);
                    loadDataWithBaseURL(null, html, "text/html", "utf8mb4", null);
                }
            }
        });
    }

    /**
     * 使用此方法会导致界面卡顿
     *
     * @param text
     */
    @Deprecated
    public void setText(String text) {
        this.text = text;
        String html = MarkdownHandler.getInstance().toHtml(text);
        imgList = MarkdownUtils.getImgUrl(html);
        loadDataWithBaseURL(null, html, "text/html", "utf8mb4", null);
    }

    public String getText() {
        return text;
    }

    private void addClient() {
        setWebViewClient(new MyWebClient());

        setHorizontalScrollBarEnabled(false);
        setVerticalScrollBarEnabled(false);

        getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        getSettings().setJavaScriptEnabled(true);
        addJavascriptInterface(new AndroidBridge(), "android");
    }

    public void setLinkClickListener(LinkClickListener listener) {
        this.linkClickListener = listener;
    }

    public void setImgClickListener(ImgClickListener listener) {
        this.imgClickListener = listener;
    }

    private final class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (linkClickListener != null) {
                linkClickListener.click(url);
            }
            return true;
        }
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void callAndroidImg(String arg) {
            Log.d(TAG, arg);
            int index = Integer.parseInt(arg);
            if (index >= 0 && index < imgList.size()) {
                if (imgClickListener != null) {
                    String[] strs = new String[imgList.size()];
                    strs = imgList.toArray(strs);
                    imgClickListener.click(strs, index);
                }
            }
        }
    }

    public interface LinkClickListener {
        void click(String url);
    }

    public interface ImgClickListener {
        void click(String[] urls, int index);
    }
}
