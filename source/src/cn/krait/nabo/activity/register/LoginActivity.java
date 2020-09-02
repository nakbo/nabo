package cn.krait.nabo.activity.register;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import cn.krait.nabo.activity.inherit.InitialActivity;
import cn.krait.nabo.R;
import cn.krait.nabo.fragment.RegisterFirstFragment;

/**
 * @author 权那他(Kraity)
 * @date 2019/7/25.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class LoginActivity extends InitialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new RegisterFirstFragment(personage))
                    .commit();
        }

    }
}
