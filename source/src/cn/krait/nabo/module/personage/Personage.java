package cn.krait.nabo.module.personage;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.krait.nabo.module.object.AccountObject;
import cn.krait.nabo.module.object.RssUrlObject;
import cn.krait.nabo.module.object.SettingObject;

/**
 * @author 权那他(Kraity)
 * @date 2019/7/24.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class Personage {

    private static SharedPreferences sp;
    private static SharedPreferences.Editor e;
    private static Personage spu;

    /**
     * 构造方法
     *
     * @param context
     */
    @SuppressLint("CommitPrefEdits")
    private Personage(Context context) {
        sp = context.getSharedPreferences("personage_d", Context.MODE_PRIVATE);
        e = sp.edit();
    }

    /**
     * 单例模式
     *
     * @param context
     * @return
     */
    public static Personage getInstance(Context context) {
        if (spu == null) {
            spu = new Personage(context);
        }
        return spu;
    }

    /**
     * 保存数据到SharedPreferences
     *
     * @param key   键
     * @param value 需要保存的数据
     * @return 保存结果
     */
    public boolean put(String key, Object value) {
        boolean result;
        SharedPreferences.Editor editor = sp.edit();
        String type = value.getClass().getSimpleName();
        try {
            switch (type) {
                case "Boolean":
                    editor.putBoolean(key, (Boolean) value);
                    break;
                case "Long":
                    editor.putLong(key, (Long) value);
                    break;
                case "Float":
                    editor.putFloat(key, (Float) value);
                    break;
                case "String":
                    editor.putString(key, (String) value);
                    break;
                case "Integer":
                    editor.putInt(key, (Integer) value);
                    break;
                default:
                    Gson gson = new Gson();
                    String json = gson.toJson(value);
                    editor.putString(key, json);
                    break;
            }
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }

    /**
     * 获取SharedPreferences中保存的数据
     *
     * @param key          键
     * @param defaultValue 获取失败默认值
     * @return 从SharedPreferences读取的数据
     */
    public Object get(String key, Object defaultValue) {
        Object result;
        String type = defaultValue.getClass().getSimpleName();
        try {
            switch (type) {
                case "Boolean":
                    result = sp.getBoolean(key, (Boolean) defaultValue);
                    break;
                case "Long":
                    result = sp.getLong(key, (Long) defaultValue);
                    break;
                case "Float":
                    result = sp.getFloat(key, (Float) defaultValue);
                    break;
                case "String":
                    result = sp.getString(key, (String) defaultValue);
                    break;
                case "Integer":
                    result = sp.getInt(key, (Integer) defaultValue);
                    break;
                default:
                    Gson gson = new Gson();
                    String json = sp.getString(key, "");
                    if (!json.equals("") && json.length() > 0) {
                        result = gson.fromJson(json, defaultValue.getClass());
                    } else {
                        result = defaultValue;
                    }
                    break;
            }
        } catch (Exception e) {
            result = null;
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 用于保存集合
     *
     * @param key  key
     * @param list 集合数据
     * @return 保存结果
     */
    public <T> boolean putListData(String key, List<T> list) {
        boolean result;
        String type = list.get(0).getClass().getSimpleName();
        SharedPreferences.Editor editor = sp.edit();
        JsonArray array = new JsonArray();
        try {
            switch (type) {
                case "Boolean":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Boolean) list.get(i));
                    }
                    break;
                case "Long":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Long) list.get(i));
                    }
                    break;
                case "Float":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Float) list.get(i));
                    }
                    break;
                case "String":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((String) list.get(i));
                    }
                    break;
                case "Integer":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Integer) list.get(i));
                    }
                    break;
                default:
                    Gson gson = new Gson();
                    for (int i = 0; i < list.size(); i++) {
                        JsonElement obj = gson.toJsonTree(list.get(i));
                        array.add(obj);
                    }
                    break;
            }
            editor.putString(key, array.toString());
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }

    /**
     * 获取保存的List
     *
     * @param key key
     * @return 对应的Lis集合
     */
    public <T> List<T> getListData(String key, Class<T> cls) {
        List<T> list = new ArrayList<>();
        String json = sp.getString(key, "");
        if (!json.equals("") && json.length() > 0) {
            Gson gson = new Gson();
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement elem : array) {
                list.add(gson.fromJson(elem, cls));
            }
        }
        return list;
    }

    /**
     * 用于保存集合
     *
     * @param key key
     * @param map map数据
     * @return 保存结果
     */
    public <K, V> boolean putHashMapData(String key, Map<K, V> map) {
        boolean result;
        SharedPreferences.Editor editor = sp.edit();
        try {
            Gson gson = new Gson();
            String json = gson.toJson(map);
            editor.putString(key, json);
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }

    /**
     * 用于保存集合
     *
     * @param key key
     * @return HashMap
     */
    public <V> HashMap<String, V> getHashMapData(String key, Class<V> clsV) {
        String json = sp.getString(key, "");
        HashMap<String, V> map = new HashMap<>();
        Gson gson = new Gson();
        JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entrySet = obj.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            String entryKey = entry.getKey();
            JsonObject value = (JsonObject) entry.getValue();
            map.put(entryKey, gson.fromJson(value, clsV));
        }
        Log.e("Personage", obj.toString());
        return map;
    }

    /**
     * 设置登录状态
     *
     * @param bool
     */
    public void setLogin(boolean bool) {
        e.putBoolean("login", bool);
        e.commit();
    }

    /**
     * 获取登录状态
     *
     * @return
     */
    public boolean getLogin() {
        return sp.getBoolean("login", false);
    }

    /**
     * 获取登录状态
     *
     * @return
     */
    public int getLoginInt() {
        if (this.getLogin()) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * 设置用户名
     *
     * @param name
     */
    public void setScreenName(String name) {
        e.putString("screenName", name);
        e.commit();
    }

    /**
     * 获取用户名
     *
     * @return
     */
    public String getScreenName() {
        return sp.getString("screenName", null);
    }

    /**
     * 设置用户名
     *
     * @param name
     */
    public void setName(String name) {
        e.putString("name", name);
        e.commit();
    }

    /**
     * 获取用户名
     *
     * @return
     */
    public String getName() {
        return sp.getString("name", null);
    }

    /**
     * 设置密码
     *
     * @param password
     */
    public void setPassword(String password) {
        e.putString("password", password);
        e.commit();
    }

    /**
     * 获取密码
     *
     * @return
     */
    public String getPassword() {
        return sp.getString("password", null);
    }

    /**
     * 设置博客地址
     *
     * @param url
     */
    public void setSiteUrl(String url) {
        e.putString("siteUrl", url);
        e.commit();
    }

    /**
     * 获取博客地址
     *
     * @return
     */
    public String getSiteUrl() {
        return sp.getString("siteUrl", null);
    }

    /**
     * 设置id
     *
     * @param id
     */
    public void setId(int id) {
        e.putInt("id", id);
        e.commit();
    }

    /**
     * 获取id
     *
     * @return
     */
    public int getId() {
        return sp.getInt("id", 1);
    }

    /**
     * 设置邮箱地址
     *
     * @param mail
     */
    public void setEmail(String mail) {
        e.putString("email", mail);
        e.commit();
    }

    /**
     * 获取邮箱地址
     *
     * @return
     */
    public String getEmail() {
        return sp.getString("email", null);
    }

    /**
     * 设置账户集合
     *
     * @param account
     */
    public void setAccount(AccountObject account) {
        Gson gson = new Gson();
        String g = gson.toJson(account);
        e.putString("accountGatherL", g);
        e.commit();
    }

    /**
     * 获取账户集合
     *
     * @return
     */
    public AccountObject getAccount() {
        Gson gson = new Gson();
        String call = sp.getString("accountGatherL", null);
        if (call == null) {
            AccountObject s = new AccountObject();
            s.add(
                    getDomain(),
                    getName(),
                    getPassword(),
                    getSslAble(),
                    getPseudoAble()
            );
            setAccount(s);
            return s;
        } else {
            return gson.fromJson(call, AccountObject.class);
        }
    }

    public void addAccount() {
        AccountObject accountObject = getAccount();
        accountObject.add(
                getDomain(),
                getName(),
                getPassword(),
                getSslAble(),
                getPseudoAble()
        );
        setAccount(accountObject);
    }


    /**
     * 设置
     */
    public void setSetting(SettingObject s) {
        Gson gson = new Gson();
        String g = gson.toJson(s);
        e.putString("settingL", g);
        e.commit();
    }

    /**
     * 获取
     *
     * @return
     */
    public SettingObject getSetting() {
        Gson gson = new Gson();
        String call = sp.getString("settingL", null);
        if (call == null) {
            SettingObject s = new SettingObject();
            s.initialization();
            setSetting(s);
            return s;
        } else {
            return gson.fromJson(call, SettingObject.class);
        }

    }

    /**
     * RssUrl
     */
    public void setRssUrlItem(RssUrlObject s) {
        Gson gson = new Gson();
        String g = gson.toJson(s);
        e.putString("rssUrlo0", g);
        e.commit();
    }

    /**
     * 获取
     *
     * @return
     */
    public RssUrlObject getRssUrlItem() {
        Gson gson = new Gson();
        String call = sp.getString("rssUrlo0", null);
        if (call == null) {
            RssUrlObject s = new RssUrlObject();
            s.initialization();
            setRssUrlItem(s);
            return s;
        } else {
            return gson.fromJson(call, RssUrlObject.class);
        }

    }

    public void setDomain(String s) {
        e.putString("domain", s);
        e.commit();
    }

    public String getDomain() {
        return sp.getString("domain", null);
    }

    public void setSslAble(boolean b) {
        e.putBoolean("sslAble", b);
        e.commit();
    }

    public boolean getSslAble() {
        return sp.getBoolean("sslAble", false);
    }

    public void setPseudoAble(boolean b) {
        e.putBoolean("pseudoAble", b);
        e.commit();
    }

    public boolean getPseudoAble() {
        return sp.getBoolean("pseudoAble", false);
    }

}
