package cn.krait.nabo.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.net.URLEncoder;

import cn.krait.nabo.activity.inherit.InitialActivity;

/**
 * @author 权那他(Kraity)
 * @date 2019/7/27.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class AliPayUtil {

    /*付宝包名*/
    private static final String ALIPAY_PACKAGE_NAME = "com.eg.android.AlipayGphone";

    /**
     * 第一步：检查支付宝是否安装
     * @param context
     * @return
     */
    public static boolean hasInstalledAlipayClient(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(ALIPAY_PACKAGE_NAME, 0);
            return info != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 第二步：调用者调用此方法跳转到支付宝
     * @param activity
     * @param urlCode
     * @return
     */
    public static boolean startAlipayClient(InitialActivity activity, String urlCode) {
        return startIntentUrl(activity, doFormUri(urlCode));
    }

    /**
     * 格式化urlCode
     * @param urlCode
     * @return
     */
    private static String doFormUri(String urlCode) {
        try {
            urlCode = URLEncoder.encode(urlCode, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String alipayqr = "alipayqr://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=" + urlCode;
        String openUri = alipayqr + "%3F_s%3Dweb-other&_t=" + System.currentTimeMillis();
        return openUri;
    }

    /**
     * 主要功能代码：跳转到支付宝
     * @param activity
     * @param intentFullUrl
     * @return
     */
    private static boolean startIntentUrl(InitialActivity activity, String intentFullUrl) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(intentFullUrl));
            activity.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
