package cn.krait.nabo.module.object;

import java.util.ArrayList;

/**
 * @author 权那他(Kraity)
 * @date 2019/11/3.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class AccountObject {
    private ArrayList<Object[]> accountItems = new ArrayList<Object[]>();

    public void add(String domain, String name, String password, boolean ssl, boolean pseudo) {
        if (isCurrent(domain, name)) {
            accountItems.add(new Object[]{
                    domain,
                    name,
                    password,
                    ssl,
                    pseudo
            });
        }
    }

    public ArrayList<Object[]> getAccountItems() {
        return accountItems;
    }

    public void delete(int position) {
        accountItems.remove(position);
    }

    public int getPosition(String domain, String name, int Default) {
        int[] position = {Default};
        for (int i = 0; i < accountItems.size(); i++) {
            Object[] account = accountItems.get(i);
            String d = ((String) account[0]).toLowerCase();
            String n = ((String) account[1]).toLowerCase();
            if (d.equals(domain.toLowerCase()) && n.equals(name.toLowerCase())) {
                position[0] = i;
                break;
            }
        }
        return position[0];
    }

    public boolean isCurrent(Object[] o) {
        return isCurrent((String) o[0], (String) o[1]);
    }

    public boolean isCurrent(String domain, String name) {
        return getPosition(domain, name, -1) == -1;
    }

    public static String getXMLRpcUrl(String domain, boolean ssl, boolean pseudo) {
        return (ssl ? "https://" : "http://") + (domain) + (pseudo ? "/action/xmlrpc" : "/index.php/action/xmlrpc");
    }

    public static String getXMLRpcUrl(Object[] o) {
        return getXMLRpcUrl((String) o[0], (boolean) o[3], (boolean) o[4]);
    }
}
