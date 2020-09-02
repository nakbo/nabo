package cn.krait.nabo.module.object;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author 权那他(Kraity)
 * @date 2019/10/1.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class StatObject implements Serializable {
    private HashMap<?, ?> stat;
    public post post;
    public page page;
    public comment comment;
    public tags tags;
    public categories categories;

    public StatObject(Object o) {
        stat = (HashMap<?, ?>) o;
        post = new post();
        page = new page();
        comment = new comment();
        tags = new tags();
        categories = new categories();
    }

    public class post {
        HashMap<?, ?> post;

        post() {
            post = (HashMap<?, ?>) stat.get("post");
        }

        public int all() {
            return Integer.parseInt(String.valueOf(post.get("all")));
        }

        public int publish() {
            return Integer.parseInt(String.valueOf(post.get("publish")));
        }

        public int waiting() {
            return Integer.parseInt(String.valueOf(post.get("waiting")));
        }

        public int draft() {
            return Integer.parseInt(String.valueOf(post.get("draft")));
        }

        public int hidden() {
            return Integer.parseInt(String.valueOf(post.get("hidden")));
        }

        public int privated() {
            return Integer.parseInt(String.valueOf(post.get("private")));
        }
    }

    public class page {
        HashMap<?, ?> page;

        page() {
            page = (HashMap<?, ?>) stat.get("page");
        }

        public int all() {
            return Integer.parseInt(String.valueOf(page.get("all")));
        }

        public int publish() {
            return Integer.parseInt(String.valueOf(page.get("publish")));
        }

        public int draft() {
            return Integer.parseInt(String.valueOf(page.get("draft")));
        }

        public int hidden() {
            return Integer.parseInt(String.valueOf(page.get("hidden")));
        }
    }

    public class comment {
        HashMap<?, ?> comment;

        comment() {
            comment = (HashMap<?, ?>) stat.get("comment");
        }

        public int all() {
            return Integer.parseInt(String.valueOf(comment.get("all")));
        }

        public int publish() {
            return Integer.parseInt(String.valueOf(comment.get("publish")));
        }

        public int waiting() {
            return Integer.parseInt(String.valueOf(comment.get("waiting")));
        }

        public int spam() {
            return Integer.parseInt(String.valueOf(comment.get("spam")));
        }

        public int delete() {
            return Integer.parseInt(String.valueOf(comment.get("delete")));
        }
    }

    public class tags {
        public int all() {
            return Integer.parseInt(String.valueOf(stat.get("tags")));
        }
    }

    public class categories {
        public int all() {
            return Integer.parseInt(String.valueOf(stat.get("categories")));
        }
    }
}
