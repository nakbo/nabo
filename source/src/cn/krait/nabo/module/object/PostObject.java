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
public class PostObject implements Serializable {
    private HashMap<?, ?> post;
    public author author;
    public text text;
    public categories categories;
    public tags tags;

    public PostObject(Object p) {
        post = (HashMap<?, ?>) p;
        author = new author();
        text = new text();
        categories = new categories();
        tags = new tags();
    }

    public int cid() {
        return Integer.parseInt(String.valueOf(post.get("cid")));
    }

    public String slug() {
        return String.valueOf(post.get("slug"));
    }

    public String title() {
        return String.valueOf(post.get("title"));
    }

    public String type() {
        return String.valueOf(post.get("type"));
    }

    public class author {
        HashMap<?, ?> author;

        author() {
            author = (HashMap<?, ?>) post.get("author");
        }

        public String screenName() {
            return String.valueOf(author.get("screenName"));
        }

    }

    public class text {
        HashMap<?, ?> text;
        HashMap<?, ?> length;

        text() {
            text = (HashMap<?, ?>) post.get("text");
            length = (HashMap<?, ?>) text.get("length");
        }

        public String markdown() {
            return String.valueOf(text.get("markdown"));
        }

        public String html() {
            return String.valueOf(text.get("html"));
        }

        public int lengthString() {
            return Integer.parseInt(String.valueOf(length.get("string")));
        }

        public int lengthUtf8() {
            return Integer.parseInt(String.valueOf(length.get("utf8")));
        }

    }

    public class categories {
        Object[] categories;

        categories() {
            categories = (Object[]) post.get("categories");
        }

        public String name() {
            StringBuilder s = new StringBuilder();
            if (categories.length > 0) {
                for (Object category : categories) {
                    HashMap<?, ?> c = (HashMap<?, ?>) category;
                    s.append(c.get("name")).append(",");
                }
            } else {
                s.append(",");
            }
            return s.substring(0, s.toString().length() - 1);
        }

    }

    public class tags {
        Object[] tags;

        tags() {
            tags = (Object[]) post.get("tags");
        }

        public String name() {
            StringBuilder s = new StringBuilder();
            if (tags.length > 0) {
                for (Object tag : tags) {
                    HashMap<?, ?> c = (HashMap<?, ?>) tag;
                    s.append(c.get("name")).append(",");
                }
            } else {
                s.append(",");
            }
            return s.substring(0, s.toString().length() - 1);
        }

    }

    public boolean hasSaved() {
        return Boolean.parseBoolean(String.valueOf(post.get("hasSaved")));
    }

    public String status() {
        return String.valueOf(post.get("status"));
    }

    public String permalink() {
        return String.valueOf(post.get("permalink"));
    }

    public String description() {
        return String.valueOf(post.get("description"));
    }

    public String commentsNum() {
        return String.valueOf(post.get("commentsNum"));
    }

    public String created() {
        return String.valueOf(post.get("created"));
    }

    public String modified() {
        return String.valueOf(post.get("modified"));
    }

    public String password() {
        return String.valueOf(post.get("password"));
    }

    public int parent() {
        return Integer.parseInt(String.valueOf(post.get("parent")));
    }

    public String template() {
        return String.valueOf(post.get("template"));
    }

    public int order() {
        return Integer.parseInt(String.valueOf(post.get("order")));
    }

    public int allowFeed() {
        return Integer.parseInt(String.valueOf(post.get("allowFeed")));
    }

    public int allowComment() {
        return Integer.parseInt(String.valueOf(post.get("allowComment")));
    }

    public int allowPing() {
        return Integer.parseInt(String.valueOf(post.get("allowPing")));
    }
}
