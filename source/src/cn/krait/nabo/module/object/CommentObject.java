package cn.krait.nabo.module.object;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author 权那他(Kraity)
 * @date 2019/10/2.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class CommentObject implements Serializable {
    private HashMap<?, ?> comment;

    CommentObject(Object c) {
        comment = (HashMap<?, ?>) c;
    }

    public String created() {
        return String.valueOf(comment.get("created"));
    }

    public int authorId() {
        return Integer.parseInt(String.valueOf(comment.get("authorId")));
    }

    public int coid() {
        return Integer.parseInt(String.valueOf(comment.get("coid")));
    }

    public int parent() {
        return Integer.parseInt(String.valueOf(comment.get("parent")));
    }

    public String status() {
        return String.valueOf(comment.get("status"));
    }

    public String text() {
        return String.valueOf(comment.get("text"));
    }

    public String permalink() {
        return String.valueOf(comment.get("permalink"));
    }

    public int cid() {
        return Integer.parseInt(String.valueOf(comment.get("cid")));
    }

    public String title() {
        return String.valueOf(comment.get("title"));
    }

    public int postId() {
        return Integer.parseInt(String.valueOf(comment.get("post_id")));
    }

    public String agent() {
        return String.valueOf(comment.get("agent"));
    }

    public String author() {
        return String.valueOf(comment.get("author"));
    }

    public String authorUrl() {
        return String.valueOf(comment.get("author_url"));
    }

    public String authorMail() {
        return String.valueOf(comment.get("author_mail"));
    }

    public String authorIp() {
        return String.valueOf(comment.get("author_ip"));
    }

    public String type() {
        return String.valueOf(comment.get("type"));
    }

}
