package cn.krait.nabo.module.object;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author 权那他(Kraity)
 * @date 2019/10/4.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class PersonageObject implements Serializable {
    private HashMap<?, ?> personage;

    public PersonageObject(Object p) {
        personage = (HashMap<?, ?>) p;
    }

    public int uid() {
        return Integer.parseInt(String.valueOf(personage.get("uid")));
    }

    public String name() {
        return String.valueOf(personage.get("name"));
    }

    public String screenName() {
        return String.valueOf(personage.get("screenName"));
    }

    public String mail() {
        return String.valueOf(personage.get("mail"));
    }

    public String created() {
        return String.valueOf(personage.get("created"));
    }

    public String activated() {
        return String.valueOf(personage.get("activated"));
    }

    public String logged() {
        return String.valueOf(personage.get("logged"));
    }

    public String group() {
        return String.valueOf(personage.get("group"));
    }

    public String authCode() {
        return String.valueOf(personage.get("authCode"));
    }


}
