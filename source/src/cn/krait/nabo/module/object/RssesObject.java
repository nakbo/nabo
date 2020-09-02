package cn.krait.nabo.module.object;

import android.annotation.SuppressLint;

import com.einmalfel.earl.Feed;
import com.einmalfel.earl.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author 权那他(Kraity)
 * @date 2019/11/2.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class RssesObject {
    @SuppressLint("UseSparseArrays")
    private List<RssObject> rssObjects = new ArrayList<RssObject>();
    private RssObject[] rssArray;

    public void add(Feed feed) {
        String siteTitle = feed.getTitle();
        String siteUrl = feed.getLink();
        for (Item item : feed.getItems()) {
            RssObject rssObject = new RssObject();
            rssObject.setSiteTitle(siteTitle);
            rssObject.setSiteUrl(siteUrl);
            rssObject.setTitle(item.getTitle());
            rssObject.setAuthor(item.getAuthor());
            rssObject.setDescription(item.getDescription());
            rssObject.setDate(item.getPublicationDate());
            rssObject.setLink(item.getLink());
            rssObjects.add(rssObject);
        }
        Collections.sort(rssObjects, new Comparator<RssObject>() {
            @Override
            public int compare(RssObject o1, RssObject o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        rssArray = new RssObject[rssObjects.size()];
        rssObjects.toArray(rssArray);
    }

    public void add(RssObject[] r) {
        rssObjects.addAll(Arrays.asList(r));
        rssArray = r;
    }

    public RssObject getRss(int lid) {
        return rssObjects.get(lid);
    }

    public List<RssObject> getRssObjects() {
        return rssObjects;
    }

    public RssObject[] getRssArray() {
        return rssArray;
    }

    public void clear() {
        rssObjects.clear();
        rssArray = new RssObject[]{};
    }

}
