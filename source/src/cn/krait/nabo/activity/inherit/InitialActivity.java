package cn.krait.nabo.activity.inherit;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.krait.nabo.module.ACache.ACache;
import cn.krait.nabo.service.XMLRPCService;
import cn.krait.nabo.util.OtherUtil;
import cn.krait.nabo.module.personage.Personage;
import cn.krait.nabo.module.object.SettingObject;
import cn.krait.nabo.util.XMLRPCUtils;

/**
 * @author 权那他(Kraity)
 * @date 2019/7/1.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
@SuppressLint("Registered")
public class InitialActivity extends AppCompatActivity {

    protected Personage personage;
    protected ACache personageACache;
    protected XMLRPCService XMLRPCService;
    protected XMLRPCUtils XMLRPCUtils;
    protected SettingObject setting;
    protected OtherUtil OUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        /*init*/
        personage = Personage.getInstance(
                getApplicationContext()
        );
        personageACache = ACache.get(
                getApplicationContext()
        );
        /*实例化 XMLRPCService 接口*/
        XMLRPCService = new XMLRPCService();
        if (personage.getLogin()) XMLRPCService.init(
                personage.getSiteUrl(),
                personage.getId(),
                personage.getName(),
                personage.getPassword()
        );
        XMLRPCUtils = new XMLRPCUtils(personage, personageACache, XMLRPCService);

        setting = personage.getSetting();
        OUtil = new OtherUtil();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
