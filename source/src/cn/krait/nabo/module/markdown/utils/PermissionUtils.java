package cn.krait.nabo.module.markdown.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

/**
 * Created by zk on 2017/6/16.
 * 权限请求管理
 */

public class PermissionUtils {

    public static final int REQUEST_EXTERNAL_STORAGE = 101;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * SD卡读写权限
     *
     * @param activity
     * @return 是否获得改权限
     */
    public static boolean storage(Activity activity) {
        if (!(ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
            return false;
        }
        return true;
    }
}
