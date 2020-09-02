package cn.krait.nabo.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import cn.krait.nabo.R;
import cn.krait.nabo.StartActivity;
import cn.krait.nabo.module.method.ObtainExperience;
import cn.krait.nabo.service.XMLRPCService;
import cn.krait.nabo.util.OtherUtil;
import cn.krait.nabo.module.personage.Personage;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @author 权那他(Kraity)
 * @date 2019/8/23.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class RegisterFirstFragment extends Fragment {

    private ProgressDialog progress;
    private Call call;
    private String siteUrl;
    private Object[] domain;
    private Personage personage;

    public RegisterFirstFragment(Personage p) {
        personage = p;
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
                    assert getFragmentManager() != null;
                    getFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)  //将当前fragment加入到返回栈中
                            .replace(R.id.container, new RegisterLastFragment(personage, domain)).commit();
                    break;
                case 2:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                            .setMessage("客官你的博客的XmlRpc文件过旧\n需前往 nabo 官网按照要求对你的博客XmlRpc进行升级")
                            .setPositiveButton("前往升级", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Uri uri = Uri.parse("https://" + getResources().getString(R.string.official_siteUrl));
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);
                                }
                            });
                    builder.create().show();
            }
            progress.dismiss();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_first, container, false);

        progress = new ProgressDialog(getContext());
        progress.setMessage("加载中...");
        progress.setIndeterminate(true);
        progress.setCancelable(true);

        final String[] httpString = {""};
        Spinner mSpinner = (Spinner) view.findViewById(R.id.Spinner_id);
        final ArrayList<String> list = new ArrayList<String>();
        list.add("https://");
        list.add("http://");
        final ArrayAdapter<String> ad = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, list);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(ad);
        mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                httpString[0] = list.get(arg2);
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        final String[] staticAllowString = {"/action/xmlrpc"};
        Switch staticAllow = view.findViewById(R.id.allow_static);
        staticAllow.setChecked(true);
        staticAllow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    staticAllowString[0] = "/action/xmlrpc";
                } else {
                    staticAllowString[0] = "/index.php/action/xmlrpc";
                }
            }
        });

        final EditText et = view.findViewById(R.id.et_siteUrl);
        view.findViewById(R.id.btn_show_other).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (et.getText().toString().isEmpty()) {
                    OtherUtil.toast(getContext(), "客官必须输入域名");
                    return;
                }

                String domainName = et.getText().toString().toLowerCase();

                if (domainName.contains("://")) {
                    OtherUtil.toast(getContext(), "客官不能在域名中输入响应协议头");
                    return;
                }

                if ((domainName + "/:").contains("//:")) {
                    OtherUtil.toast(getContext(), "客官不能在末尾中输入反斜杠");
                    return;
                }

                if (!Patterns.WEB_URL.matcher(httpString[0] + domainName).matches()) {
                    OtherUtil.toast(getContext(), "客官必须输入合法的域名噢");
                    return;
                }

                siteUrl = httpString[0] + domainName + staticAllowString[0];
                domain = new Object[]{
                        httpString[0].equals("https://"),
                        domainName,
                        staticAllowString[0].equals("/action/xmlrpc"),
                        siteUrl
                };

                progress.show();
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(siteUrl + "?rsd")
                        .get()
                        .build();
                call = okHttpClient.newCall(request);
                new checkThread().start();

            }
        });

        view.findViewById(R.id.btn_show_obtain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new ObtainExperienceAsyncTask().execute();
            }
        });
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    class ObtainExperienceAsyncTask extends AsyncTask<Object, Void, Boolean> {
        JSONObject o;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.show();
        }

        @Override
        protected Boolean doInBackground(Object[] objects) {
            new ObtainExperience(getContext()) {

                @Override
                protected void successful(JSONObject oo) {
                    o = oo;
                }

                @Override
                protected void failing() {

                }

                @Override
                protected void Continue() {

                }
            };
            return true;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            progress.dismiss();
            try {
                personage.setId(o.getInt("uid"));
                personage.setName(o.getString("name"));
                personage.setScreenName("null");
                personage.setEmail("null");
                personage.setPassword(o.getString("password"));
                personage.setSiteUrl(o.getString("siteurl"));
                personage.setDomain(o.getString("domain"));
                personage.setSslAble(o.getBoolean("sslable"));
                personage.setPseudoAble(o.getBoolean("pseudoable"));
                personage.addAccount();
                personage.setLogin(true);
                startActivity(new Intent(getActivity(), StartActivity.class));
                Objects.requireNonNull(getActivity()).finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public class checkThread extends Thread {

        public void run() {
            Message msg = handler.obtainMessage();
            try {
                //同步调用,返回Response,会抛出IO异常
                Response response = call.execute();
                String back = Objects.requireNonNull(response.body()).string();

                //检查博客程序类型...
                if (!back.contains("<engineName>")) {
                    msg.obj = "客官你博客的XMLRPC地址不存在";
                    msg.what = 1;
                    handler.sendMessage(msg);
                    return;
                }

                //检查博客程序类型...
                if (!back.contains("engineName>Typecho</engineName")) {
                    msg.obj = "客官,那博仅许可使用博客程序为 typecho";
                    msg.what = 1;
                    handler.sendMessage(msg);
                    return;
                }

                XMLRPCService XMLRPCService = new XMLRPCService();
                XMLRPCService.init(
                        siteUrl,
                        1,
                        null,
                        null
                );

                Object[] listMethods = XMLRPCService.listMethods();
                boolean able = false;
                for (Object method : listMethods) {
                    if (method.toString().contains("typecho.getUser")) {
                        able = true;
                    }
                }

                if (!able) {
                    msg.what = 2;
                    handler.sendMessage(msg);
                    return;
                }

                msg.what = 0;
                handler.sendMessage(msg);

            } catch (Exception e) {
                e.printStackTrace();
                msg.obj = "检测失败,请重新检查域名";
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }
    }
}
