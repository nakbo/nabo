package cn.krait.nabo.module.object;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 权那他(Kraity)
 * @date 2019/10/2.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class CommentsObject {
    @SuppressLint("UseSparseArrays")
    private Map<Integer, CommentObject> comments = new HashMap<>();
    private Object[] commentsObject;
    private List<Integer> cidList = new ArrayList<Integer>();
    private List<Integer> authorIdList = new ArrayList<Integer>();
    private List<Integer> parentList = new ArrayList<Integer>();
    private int[] state = new int[]{0, 0, 0};

    public CommentsObject(Object[] objects) {
        setData(objects);
    }

    public void setData(Object[] objects) {
        commentsObject = objects;
        for (Object object : objects) {
            CommentObject commentObject = new CommentObject(object);
            comments.put(commentObject.coid(), commentObject);
            cidList.add(commentObject.coid());
            authorIdList.add(commentObject.authorId());
            parentList.add(commentObject.parent());
            switch (commentObject.status()) {
                case "approved":
                    state[2]++;
                    break;
                case "spam":
                    state[0]++;
                    break;
                case "waiting":
                    state[1]++;
                    break;
            }
        }
    }

    public void removeItem(int position) {
        Object[] last = new Object[commentsObject.length - 1];
        int ii = 0;
        for (int i = 0; i < commentsObject.length; i++) {
            if (position != i) {
                last[ii] = commentsObject[i];
                ii++;
            }
        }
        setData(last);
    }

    public Map<Integer, CommentObject> get() {
        return comments;
    }

    public List<Integer> getCidList() {
        return cidList;
    }

    public Object[] getCommentsObject() {
        return commentsObject;
    }

    public int[] getState() {
        return state;
    }

    public CommentObject getComment(int cid) {
        return comments.get(cid);
    }

    public List<Integer> getAuthorIdList() {
        return authorIdList;
    }

    public List<Integer> getParentList() {
        return parentList;
    }
}
