package cn.krait.nabo.module.object;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 权那他(Kraity)
 * @date 2019/10/1.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class PostsObject {
    private Object[] postsObject;
    @SuppressLint("UseSparseArrays")
    private Map<Integer, PostObject> posts = new HashMap<>();
    private List<Integer> cidList = new ArrayList<Integer>();

    public PostsObject(Object[] objects) {
        setData(objects);
    }

    public void setData(Object[] o) {
        postsObject = o;
        for (Object object : o) {
            PostObject postObject = new PostObject(object);
            posts.put(postObject.cid(), postObject);
            cidList.add(postObject.cid());
        }
    }

    public void removeItem(int position) {
        Object[] last = new Object[postsObject.length - 1];
        int ii = 0;
        for (int i = 0; i < postsObject.length; i++) {
            if (position != i) {
                last[ii] = postsObject[i];
                ii++;
            }
        }
        setData(last);
    }

    public Map<Integer, PostObject> get() {
        return posts;
    }

    public List<Integer> getCidList() {
        return cidList;
    }

    public PostObject getPost(int cid) {
        return posts.get(cid);
    }

    public Object[] getPostsObject() {
        return postsObject;
    }

    public String[] getTitleList() {
        String[] titles = new String[posts.size()];
        for (int i = 0; i < posts.size(); i++) {
            PostObject post = posts.get(cidList.get(i));
            assert post != null;
            titles[i] = post.title();
        }
        return titles;
    }
}
