package cn.krait.nabo.util;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.krait.nabo.activity.page.WebViewActivity;
import cn.krait.nabo.module.ACache.ACache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @author 权那他(Kraity)
 * @date 2019/7/27.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class OtherUtil {

    /**
     * 32位MD5加密
     *
     * @param content -- 待加密内容
     * @return
     */
    public static String md5Decode(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        }
        //对生成的16字节数组进行补零操作
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 是否为空
     *
     * @param str 字符串
     * @return true 空 false 非空
     */
    public static Boolean isEmpty(String str) {
        return str == null || str.equals("");
    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static Date stringToDate(String strTime) {
        SimpleDateFormat sf1 = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy", Locale.ENGLISH);
        try {
            return sf1.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getGravatar(String email, ACache personageACache, String name) throws Exception {
        StringBuilder url = new StringBuilder();
        if (email.toLowerCase().contains("qq.com") &&
                (Pattern.compile("[0-9]*")).matcher(email.split("@")[0]).matches()) {
            url.append("https://q2.qlogo.cn/headimg_dl?dst_uin=").append(email.split("@")[0]).append("&spec=100");
        } else {
            url.append("https://gravatar.loli.net/avatar/").append(md5Decode(email)).append("?s=200&d=mm");
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url.toString())
                .build();
        Response response = okHttpClient.newCall(request).execute();
        InputStream inputStream = response.body().byteStream();//得到图片的流
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        personageACache.put(name, bitmap);
        return bitmap;
    }

    public static void toast(Context mContent, String text) {
        try {
            Toast.makeText(
                    mContent,
                    text,
                    Toast.LENGTH_SHORT
            ).show();
        } catch (Exception e) {
            //解决在子线程中调用Toast的异常情况处理
            Looper.prepare();
            Toast.makeText(mContent,
                    text,
                    Toast.LENGTH_SHORT
            ).show();
            Looper.loop();
        }

    }

    public static void openWebView(Context mContent, String url) {
        Intent intent = new Intent(mContent, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("link", url);
        intent.putExtras(bundle);
        mContent.startActivity(intent);
    }

    public static String[] splitSameStr(String[] arrStr) {
        Map<String, Object> map = new HashMap<String, Object>();
        for (String str : arrStr) {
            map.put(str, str);
        }
        //返回一个包含所有对象的指定类型的数组
        return map.keySet().toArray(new String[1]);
    }


    public static String getTimeFormatText(Date date, boolean gmt) {
        return timestampFormatText(date.getTime(), gmt);
    }

    /**
     * 时间差
     *
     * @param date
     * @return
     */
    public static String timestampFormatText(long date, boolean gmt) {
        long minute = 60 * 1000;// 1分钟
        long hour = 60 * minute;// 1小时
        long day = 24 * hour;// 1天
        long month = 31 * day;// 月
        long year = 12 * month;// 年

        long diff;
        if (gmt) {
            /* date_created_gmt 由于new IXR_Date($this->options->timezone + $comments->created)所以16*60*60*1000*/
            diff = new Date().getTime() - date + 8 * 60 * 60 * 1000;
        } else {
            diff = new Date().getTime() - date;
        }

        long r = 0;
        if (diff > year) {
            r = (diff / year);
            return r + "年前";
        }
        if (diff > month) {
            r = (diff / month);
            return r + "个月前";
        }
        if (diff > day) {
            r = (diff / day);
            return r + "天前";
        }
        if (diff > hour) {
            r = (diff / hour);
            return r + "小时前";
        }
        if (diff > minute) {
            r = (diff / minute);
            return r + "分钟前";
        }
        return "刚刚";
    }

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
     * 仅保留文件名不保留后缀
     */
    public static String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }
    }

    /**
     * 保留文件名及后缀
     */
    public static String getFileNameWithSuffix(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        if (start != -1) {
            return pathandname.substring(start + 1);
        } else {
            return null;
        }
    }

    /**
     * 后缀
     */
    public static String getFileNameWithSuffixPrefix(String pathandname) {
        int start = pathandname.lastIndexOf(".");
        if (start != -1) {
            return pathandname.substring(start + 1);
        } else {
            return null;
        }
    }

    public static void copyText(Context context, String text, String toastStr) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cmb != null) {
            cmb.setPrimaryClip(ClipData.newPlainText(null, text));
        }
        Toast.makeText(context, toastStr, Toast.LENGTH_SHORT).show();
    }

    /**
     * px转换为dp
     *
     * @param context
     * @param value
     * @return
     */
    public static int px2dp(Context context, int value) {
        float v = context.getResources().getDisplayMetrics().density;
        return (int) (value / v + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static String timeStamp2date(Integer ms, String p) {
        if (ms == null) {
            ms = 0;
        }
        long msl = (long) ms * 1000;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(p);
        String temp = null;
        try {
            String str = sdf.format(msl);
            temp = sdf.format(sdf.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public static String timeStamp2date(Integer ms) {
        return timeStamp2date(ms, "yyyy/MM/dd/HH/mm/ss");
    }

    public static String array2string(String[] array) {
        StringBuilder s = new StringBuilder();
        if (array.length > 0) {
            for (Object a : array) {
                s.append(a).append(",");
            }
        } else {
            s.append(",");
        }
        return s.substring(0, s.toString().length() - 1);
    }

    public static Object[] mergeObject(Object[] o) {
        int length = 0,
                cid = 0;
        for (Object value : o) {
            length = length + ((Object[]) value).length;
        }
        Object[] last = new Object[length];
        for (Object value : o) {
            Object[] oi = (Object[]) value;
            for (Object item : oi) {
                last[cid] = item;
                cid++;
            }
        }
        return last;
    }

}
