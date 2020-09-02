package cn.krait.nabo.module.markdown.utils;


import android.os.Handler;
import android.os.Looper;

import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.krait.nabo.util.Log;

/**
 * Created by zk on 2017/9/13.
 */

public class MarkdownHandler {

    private static final String TAG = MarkdownHandler.class.getSimpleName();

    private Parser parser;
    private HtmlRenderer renderer;

    private static MarkdownHandler instance;

    public static MarkdownHandler getInstance() {
        if (instance == null)
            instance = new MarkdownHandler();
        return instance;
    }

    private MarkdownHandler() {
        MutableDataSet options = new MutableDataSet();
        parser = Parser.builder(options).build();
        renderer = HtmlRenderer.builder(options).build();

    }

    /**
     * 匹配图片
     *
     * @param markdownText
     * @return
     */
    public List<String> getImgUrls(String markdownText) {
        ArrayList<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile("!\\[.*?\\]\\(.+?\\)");
        Matcher matcher = pattern.matcher(markdownText);

        while (matcher.find()) {
            Matcher ms = Pattern.compile("\\(.+\\)").matcher(matcher.group());
            while (ms.find()) {
                String str = ms.group();
                if (str.startsWith("("))
                    str = str.substring(1, str.length());
                if (str.endsWith(")"))
                    str = str.substring(0, str.length() - 1);
                list.add(str);
            }
        }
        return list;
    }

    public String toHtml(String markdownText) {
        Log.d(TAG, markdownText);
        Node document = parser.parse(markdownText);
        String htm = renderer.render(document);
        htm = disposeHtml(htm);
        htm = disposeImgClick(htm);
        Log.d(TAG, htm);
        return htm;
    }

    public void toHtml(final String markdownText, final Callback callback) {
        if (markdownText == null)
            return;

        new Thread() {
            @Override
            public void run() {
                final String html = toHtml(markdownText);
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callback.done(html);
                        }
                    });
                }
            }
        }.start();
    }

    private String disposeHtml(String htm) {
        return "<html>" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/otStyle.css\" />" +
                "<body>" +
                disposeP(htm) +
                "</body></html>";
    }

    /**
     * 处理P标签内部换行问题
     *
     * @param htm
     * @return
     */
    private String disposeP(String htm) {
        Pattern pattern = Pattern.compile("<p>[\\s\\S]*?</p>");
        Matcher matcher = pattern.matcher(htm);
        List<Integer> integers = new ArrayList<>();
        integers.add(0);
        while (matcher.find()) {
            integers.add(matcher.start());
            integers.add(matcher.end());
//            Log.d(TAG, matcher.group());
        }
        integers.add(htm.length());

        List<String> list = new ArrayList<>();
        for (int i = 1; i < integers.size(); i++) {
            list.add(htm.substring(integers.get(i - 1), integers.get(i)));
        }

        StringBuilder sb = new StringBuilder();
        for (String string : list) {
            if (string.startsWith("<p>")) {
                sb.append(string.replaceAll("\\n", "<br>"));
            } else {
                sb.append(string);
            }
        }
        return sb.toString();
    }

    private String disposeImgClick(String htm) {
        Pattern pattern = Pattern.compile("<img[\\s\\S]*?/>");
        Matcher matcher = pattern.matcher(htm);
        List<Integer> integers = new ArrayList<>();
        integers.add(0);
        while (matcher.find()) {
            integers.add(matcher.start());
            integers.add(matcher.end());
//            Log.d(TAG, matcher.group());
        }
        integers.add(htm.length());

        List<String> list = new ArrayList<>();
        for (int i = 1; i < integers.size(); i++) {
            list.add(htm.substring(integers.get(i - 1), integers.get(i)));
        }

        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String string : list) {
            if (string.startsWith("<img")) {
                sb.append(string.replaceAll("img src",
                        "img onclick=\"window.android.callAndroidImg(" + (i++) + ")\" src"));
            } else {
                sb.append(string);
            }
        }
        return sb.toString();
    }

    public interface Callback {
        void done(String html);
    }
}
