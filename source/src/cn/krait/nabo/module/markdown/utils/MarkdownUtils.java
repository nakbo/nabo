package cn.krait.nabo.module.markdown.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zk on 2017/10/1.
 */

public class MarkdownUtils {

    private static final String TAG = MarkdownUtils.class.getSimpleName();

    /**
     * 获取真实文件路径
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 得到html标签中的链接地址
     *
     * @param html
     * @return
     */
    public static List<String> getLinkUrl(String html) {
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile("<a[^<]+</a>");
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            Matcher ms = Pattern.compile("href=['\"].+['\"]").matcher(matcher.group());
            while (ms.find()) {
                list.add(ms.group().replaceAll("['\"]", "").replace("href=", ""));
            }
        }
        return list;
    }


    /***
     * 得到html标签中的图片地址
     * @param html
     * @return
     */
    public static List<String> getImgUrl(String html) {
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile("<img.+?/>");
        Matcher matcher = pattern.matcher(html);
        while (matcher.find()) {
            Matcher ms = Pattern.compile("src=['\"].+?['\"]").matcher(matcher.group());
            while (ms.find()) {
                list.add(ms.group().replaceAll("['\"]", "").replace("src=", ""));
            }
        }
        return list;
    }


    /**
     * 匹配图片，用于上传图片
     * <p>
     * 注意，仅仅匹配带有后缀名的图片url
     *
     * @param markText
     * @return
     */
    public static List<String> getImgUrlFromMarkDownText(String markText) {
        ArrayList<String> list = new ArrayList<>();
//        !\[.*?\]\(.*?\)
        Pattern pattern = Pattern.compile("!\\[.*?\\]\\(.*?\\..*?\\)");
        Matcher matcher = pattern.matcher(markText);

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

    /**
     * 获得系统当前时间
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getCurrentTime() {
        SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return mDateFormat.format(new Date());
    }
}
