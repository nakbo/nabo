package cn.krait.nabo.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import cn.krait.nabo.R;
import cn.krait.nabo.StartActivity;
import cn.krait.nabo.service.XMLRPCService;
import cn.krait.nabo.util.OtherUtil;
import cn.krait.nabo.module.personage.Personage;
import cn.krait.nabo.module.object.PersonageObject;

/**
 * @author 权那他(Kraity)
 * @date 2019/8/23.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class RegisterLastFragment extends Fragment {

    private Personage personage;
    private ProgressDialog progress;
    private String XMLRPCUrl;
    private String name;
    private Object[] domain;
    private String password;

    RegisterLastFragment(Personage p, Object[] u) {
        personage = p;
        domain = u;
        XMLRPCUrl = (String) u[3];
    }

    @SuppressLint("HandlerLeak")
    private
    Handler handler = new Handler() { //全局共享
        public void handleMessage(android.os.Message msg) {
            // 处理消息时需要知道是成功的消息还是失败的消息
            switch (msg.what) {
                case 1:
                    OtherUtil.toast(getContext(), (String) msg.obj);
                    break;
                case 0:
                    PersonageObject p = (PersonageObject) msg.obj;
                    personage.setId(p.uid());
                    personage.setName(p.name());
                    personage.setScreenName(p.screenName());
                    personage.setEmail(p.mail());
                    personage.setPassword(password);
                    personage.setSiteUrl(XMLRPCUrl);
                    personage.setDomain((String) domain[1]);
                    personage.setSslAble((boolean) domain[0]);
                    personage.setPseudoAble((boolean) domain[2]);
                    personage.addAccount();
                    personage.setLogin(true);

                    /* 转跳到 StartActivity 重新启动 */
                    startActivity(new Intent(getActivity(), StartActivity.class));
                    Objects.requireNonNull(getActivity()).finish();

            }
            progress.dismiss();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_last, container, false);

        progress = new ProgressDialog(getContext());
        progress.setMessage("加载中...");
        progress.setIndeterminate(true);
        progress.setCancelable(true);

        final EditText etName = view.findViewById(R.id.et_name);
        final EditText etPassword = view.findViewById(R.id.et_password);
        view.findViewById(R.id.btn_show_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (etName.getText().toString().isEmpty()) {
                    OtherUtil.toast(getContext(), "客官必须输入账号");
                    return;
                }

                if (etPassword.getText().toString().isEmpty()) {
                    OtherUtil.toast(getContext(), "客官必须输入密码");
                    return;
                }

                name = etName.getText().toString();
                password = etPassword.getText().toString();

                progress.show();
                new loginThread().start();
            }
        });

        return view;
    }

    public class loginThread extends Thread {

        public void run() {
            Message msg = handler.obtainMessage();
            Looper.prepare();//增加部分
            try {
                XMLRPCService XMLRPCService = new XMLRPCService();
                XMLRPCService.init(
                        XMLRPCUrl,
                        1,
                        name,
                        password
                );

                Object[] obj = XMLRPCService.getUser();
                PersonageObject p = new PersonageObject(obj[1]);

                /* 检查登录的账户是否为管理员权限 */
                if (!p.group().contains("administrator")) {
                    msg.obj = "客官,你的账号非管理员权限";
                    msg.what = 1;
                    handler.sendMessage(msg);
                    return;
                }

                msg.what = 0;
                msg.obj = p;
                handler.sendMessage(msg);

            } catch (Exception e) {
                e.printStackTrace();
                msg.obj = "客官,无法登陆, 密码错误";
                msg.what = 1;
                handler.sendMessage(msg);
            }
            Looper.loop();//增加部分
        }
    }
}
