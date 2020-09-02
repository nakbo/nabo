package cn.krait.nabo.util;

/**
 * @author 权那他(Kraity)
 * @date 2019/8/13.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class ConstUtils {

    private ConstUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static final String API_listMethods = "system.listMethods";
    public static final String API_getUser = "typecho.getUser";
    public static final String API_newPost = "typecho.newPost";
    public static final String API_editPost = "typecho.editPost";
    public static final String API_deletePost = "typecho.deletePost";
    public static final String API_getPost = "typecho.getPost";
    public static final String API_getRecentPosts = "typecho.getPosts";
    public static final String API_getPage = "typecho.getPage";
    public static final String API_editPage = "typecho.editPage";
    public static final String API_getRecentPages = "typecho.getPages";
    public static final String API_getComments = "typecho.getComments";
    public static final String API_deleteComment = "typecho.deleteComment";
    public static final String API_newComment = "typecho.newComment";
    public static final String API_editComment = "typecho.editComment";
    public static final String API_newMedia = "typecho.newMediaObject";
    public static final String API_getCategories = "typecho.getCategories";

    public static final String API_getRecentPostTitles = "mt.getRecentPostTitles";
    public static final String API_deleteCategories = "wp.deleteCategory";
    public static final String API_getComment = "wp.getComment";
    public static final String API_getCommentCount = "wp.getCommentCount";
    public static final String API_getMedias = "wp.getMediaLibrary";
    public static final String API_getTags = "wp.getTags";

    public static final String[][][] ColorArray = {
            {
                    {"#F44336", "红色"},
                    {"#E91E63", "粉红色"},
                    {"#9C27B0", "紫色"},
                    {"#673AB7", "深紫色"},
                    {"#3F51B5", "靛蓝色"},
                    {"#2196F3", "蓝色"},
                    {"#03A9F4", "浅蓝色"},
                    {"#00BCD4", "蓝绿色"},
                    {"#009688", "水鸭蓝"},
                    {"#4CAF50", "绿色"},
                    {"#8BC34A", "浅绿色"},
                    {"#CDDC39", "青橙绿色"},
                    {"#FFEB3B", "黄色"},
                    {"#FFC107", "黄褐色"},
                    {"#FF9800", "橙黄色"},
                    {"#FF5722", "深橙色"},
                    {"#795548", "棕色"},
                    {"#9E9E9E", "灰色"},
                    {"#607D8B", "蓝灰色"},
                    {"#0e0f1a", "暗金黑"}
            },
            {
                    {"#FF5252", "红色"},
                    {"#FF4081", "粉红色"},
                    {"#E040FB", "紫色"},
                    {"#7C4DFF", "深紫色"},
                    {"#536DFE", "靛蓝色"},
                    {"#448AFF", "蓝色"},
                    {"#40C4FF", "浅蓝色"},
                    {"#18FFFF", "蓝绿色"},
                    {"#64FFDA", "水鸭蓝"},
                    {"#69F0AE", "绿色"},
                    {"#B2FF59", "浅绿色"},
                    {"#EEFF41", "青橙绿色"},
                    {"#FFFF00", "黄色"},
                    {"#FFD740", "黄褐色"},
                    {"#FFAB40", "橙黄色"},
                    {"#FF6E40", "深橙色"},
                    {"#8D6E63", "棕色"},
                    {"#BDBDBD", "灰色"},
                    {"#78909C", "蓝灰色"},
                    {"#2c2c2c", "暗金黑"}
            }
    };


    public static final String POSTS_CACHE_NAME = "post_cache";
    public static final String COMMENTS_CACHE_NAME = "comment_cache";
    public static final String STAT_CACHE_NAME = "stat_cache";

    /**
     * PERSONAGE
     */
    public static final String PERSONAGE_ACACHE_ID = "personage_id";
    public static final String PERSONAGE_ACACHE_NAME = "personage_name";
    public static final String PERSONAGE_ACACHE_PASSWORD = "personage_password";
    public static final String PERSONAGE_ACACHE_SCREENNAME = "personage_screenName";
    public static final String PERSONAGE_ACACHE_EMAIL = "personage_email";
    public static final String PERSONAGE_ACACHE_SITEURL = "personage_siteUrl";
    public static final String PERSONAGE_ACACHE_GRAVATAR = "personage_gravatar";

    public static final String PERSONAGE_XMLRPC_NAME = "username";
    public static final String PERSONAGE_XMLRPC_SCREENNAME = "display_name";
    public static final String PERSONAGE_XMLRPC_EMAIL = "email";


    public static final String CATEGORY_ACACHE = "categorys_recent";
    public static final String CATEGORY_ACACHE_NAME = "name";

    public static final String MEDIA_XMLRPC_ID = "attachment_id";
    public static final String MEDIA_XMLRPC_DATECREATED = "date_created_gmt";
    public static final String MEDIA_XMLRPC_PARENTID = "parent";
    public static final String MEDIA_XMLRPC_URL = "link";
    public static final String MEDIA_XMLRPC_NAME = "title";
    public static final String MEDIA_XMLRPC_CAPTION = "caption";
    public static final String MEDIA_XMLRPC_DESCRIPTION = "description";
    public static final String MEDIA_XMLRPC_THUMBNAIL = "thumbnail";

    public static final String MEDIA_ACACHE = "medias_recent";
    public static final String MEDIA_ACACHE_ID = "id";
    public static final String MEDIA_ACACHE_DATECREATED = "date_created";
    public static final String MEDIA_ACACHE_PARENTID = "parent_id";
    public static final String MEDIA_ACACHE_URL = "url";
    public static final String MEDIA_ACACHE_NAME = "name";
    public static final String MEDIA_ACACHE_CAPTION = "caption";
    public static final String MEDIA_ACACHE_DESCRIPTION = "description";
    public static final String MEDIA_ACACHE_THUMBNAIL = "thumbnail";


    public static final String RSS_ACACHE = "rss_list";
    public static final String RSS_LIST_STATUS = "rss_list_status";

}
