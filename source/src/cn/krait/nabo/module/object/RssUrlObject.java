package cn.krait.nabo.module.object;

import java.util.ArrayList;

/**
 * @author 权那他(Kraity)
 * @date 2019/11/3.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class RssUrlObject {
    private ArrayList<Object[]> rssItems = new ArrayList<Object[]>();

    public void initialization() {
        add("https://segmentfault.com/articles/feeds");
        add("http://forum.typecho.org/feed.php");
    }

    public void add(String url) {
        rssItems.add(new Object[]{true, url});
    }

    public ArrayList<Object[]> getRssItems() {
        return rssItems;
    }

    public String[] getRssItem() {
        String[] array = new String[rssItems.size()];
        for (int i = 0; i < rssItems.size(); i++) {
            array[i] = (String) rssItems.get(i)[1];
        }
        return array;
    }

    public String[] getRssItemAble() {
        ArrayList<String> list = new ArrayList<String>();
        for (Object[] o : rssItems) {
            if ((boolean) o[0]) list.add((String) o[1]);
        }
        String[] array = new String[list.size()];
        list.toArray(array);
        return array;
    }

    public boolean[] getItemAble() {
        boolean[] array = new boolean[rssItems.size()];
        for (int i = 0; i < rssItems.size(); i++) {
            array[i] = (boolean) rssItems.get(i)[0];
        }
        return array;
    }

    public void change(int i, Object[] o) {
        rssItems.set(i, o);
    }

    public void remove(int i){
        rssItems.remove(i);
    }
}
