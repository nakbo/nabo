package cn.krait.nabo.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.krait.nabo.module.ACache.ACache;
import cn.krait.nabo.module.personage.Personage;
import cn.krait.nabo.service.XMLRPCService;
import cn.krait.nabo.module.object.PersonageObject;

/**
 * @author 权那他(Kraity)
 * @date 2019/7/28.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class XMLRPCUtils {

    private Personage personage;
    private ACache personageACache;
    private XMLRPCService XMLRPCService;

    public XMLRPCUtils(Personage p, ACache a, XMLRPCService x) {
        personage = p;
        personageACache = a;
        XMLRPCService = x;
    }

    public void setPersonage() throws Exception {
        Object[] personageUser = XMLRPCService.getUser();
        PersonageObject p = new PersonageObject(personageUser[1]);
        personage.setId(p.uid());
        personage.setName(p.name());
        personage.setScreenName(p.screenName());
        personage.setEmail(p.mail());
    }

    public void setStat() throws Exception {
        personageACache.put(ConstUtils.STAT_CACHE_NAME, new Object[]{XMLRPCService.getStat()[1]});
    }

    public void setGravatar() throws Exception {
        personageACache.put(
                ConstUtils.PERSONAGE_ACACHE_GRAVATAR,
                OtherUtil.getGravatar(
                        personage.getEmail(),
                        personageACache,
                        ConstUtils.PERSONAGE_ACACHE_GRAVATAR)
        );
    }

    public void setMediasRecent() {
        HashMap<String, Object> struct = new HashMap<>();
        struct.put("number", 9999);
        try {
            Object[] medias = XMLRPCService.getMedias(struct);
            JSONArray mediaArray = new JSONArray();
            for (Object media : medias) {
                HashMap<?, ?> get = (HashMap<?, ?>) media;
                JSONObject obj = new JSONObject();
                try {
                    obj.put(ConstUtils.MEDIA_ACACHE_ID, get.get(ConstUtils.MEDIA_XMLRPC_ID));
                    obj.put(ConstUtils.MEDIA_ACACHE_DATECREATED, get.get(ConstUtils.MEDIA_XMLRPC_DATECREATED));
                    obj.put(ConstUtils.MEDIA_ACACHE_PARENTID, get.get(ConstUtils.MEDIA_XMLRPC_PARENTID));
                    obj.put(ConstUtils.MEDIA_ACACHE_URL, get.get(ConstUtils.MEDIA_XMLRPC_URL));
                    obj.put(ConstUtils.MEDIA_ACACHE_NAME, get.get(ConstUtils.MEDIA_XMLRPC_NAME));
                    obj.put(ConstUtils.MEDIA_ACACHE_CAPTION, get.get(ConstUtils.MEDIA_XMLRPC_CAPTION));
                    obj.put(ConstUtils.MEDIA_ACACHE_DESCRIPTION, get.get(ConstUtils.MEDIA_XMLRPC_DESCRIPTION));
                    obj.put(ConstUtils.MEDIA_ACACHE_THUMBNAIL, get.get(ConstUtils.MEDIA_XMLRPC_THUMBNAIL));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mediaArray.put(obj);
            }
            personageACache.put(ConstUtils.MEDIA_ACACHE, mediaArray);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("e", e.toString());
        }
    }

    /*public Object newMedia(String path) {
        File file = new File(path);
        if (!file.exists()) {
            Log.e("e", "File doesn't exist!");
            return null;
        }
        Map<String, Object> data = new HashMap<String, Object>();
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[fis.available()];
            int rc;
            while ((rc = fis.read(buff)) != -1) {
                swapStream.write(buff, 0, rc);
            }
            byte[] in2b = swapStream.toByteArray();
            swapStream.close();
            fis.close();
            data.put("name", OtherUtil.getFileNameWithSuffix(path));
            data.put("bytes", in2b);
            return XMLRPCService.newMedia(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/

}
