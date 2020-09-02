package cn.krait.nabo.module.method;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import cn.krait.nabo.util.ManifestUtils;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author 权那他(Kraity)
 * @date 2019/11/30.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public abstract class ObtainExperience {
    protected ObtainExperience(Context context) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://lib.krait.cn/release/obtainExperience.json")
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String back = Objects.requireNonNull(response.body()).string();
            JSONObject object = new JSONObject(back);
            successful(object);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            failing();
        }
    }

    protected abstract void successful(JSONObject o);

    protected abstract void failing();

    protected abstract void Continue();

}
