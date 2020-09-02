package cn.krait.nabo.service;

import org.xmlrpc.android.XMLRPCClient;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import cn.krait.nabo.util.ConstUtils;

/**
 * @author 权那他(Kraity)
 * @date 2019/7/23.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class XMLRPCService implements XMLRPCInterface {

    private Personage personage;
    private XMLRPCClient client;

    /**
     * init 初始化
     *
     * @param url
     * @param id
     * @param name
     * @param password
     */
    public void init(String url, int id, String name, String password) {
        personage = new Personage();
        personage.setUrl(url);
        personage.setId(id);
        personage.setName(name);
        personage.setPassword(password);

        client = new XMLRPCClient(
                URI.create(
                        personage.getUrl()
                )
        );
    }

    public class Personage {
        private String url;
        private int id;
        private String name;
        private String password;

        public void setUrl(String url) {
            this.url = url;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUrl() {
            return this.url;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public String getPassword() {
            return this.password;
        }
    }

    /**
     * listMethods
     *
     * @return
     */
    @Override
    public Object[] listMethods() throws Exception {
        return (Object[]) client.callEx(ConstUtils.API_listMethods, new Object[0]);
    }

    /**
     * 获取博主资料
     *
     * @return
     */
    @Override
    public Object[] getUser() throws Exception {
        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword()
        };
        return (Object[]) client.callEx(ConstUtils.API_getUser, params);
    }

    /**
     * 博客系统配置清单
     *
     * @param option
     * @return
     */
    @Override
    public Object[] getOptions(Object[] option) {
        return new Object[0];
    }

    /**
     * 设置博客系统配置
     *
     * @param option
     * @return
     */
    @Override
    public Object[] setOptions(Object[] option) {
        return new Object[0];
    }


    /**
     * 获取文章
     *
     * @param struct
     * @return
     */
    @Override
    public Object[] getPosts(Map<String, Object> struct) throws Exception {
        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword(),
                struct
        };
        return (Object[]) client.callEx(ConstUtils.API_getRecentPosts, params);
    }

    /**
     * 常用统计
     *
     * @return
     */
    @Override
    public Object[] getStat() throws Exception {
        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword()
        };
        return (Object[]) client.callEx("typecho.getStat", params);
    }

    /**
     * 获取postNum个文章post title
     *
     * @param postsNum
     * @return
     */
    @Override
    public Object[] getRecentPostTitles(int postsNum) throws Exception {
        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword(),
                postsNum
        };
        return (Object[]) client.callEx(ConstUtils.API_getRecentPostTitles, params);
    }

    /**
     * 获取所有的分类
     *
     * @return
     */
    @Override
    public Object[] getCategories() throws Exception {
        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword()
        };
        return (Object[]) client.callEx(ConstUtils.API_getCategories, params);
    }

    /**
     * 根据id删除分类
     *
     * @param categoriesId
     * @return
     */
    @Override
    public boolean deleteCategories(int categoriesId) throws Exception {
        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword(),
                categoriesId
        };
        return (boolean) client.callEx(ConstUtils.API_deleteCategories, params);
    }

    /**
     * 根据path增加一个评论
     *
     * @param path
     * @param struct
     * @return
     */
    @Override
    public Object[] newComment(String path, Map<String, Object> struct) throws Exception {
        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword(),
                path,
                struct
        };
        return (Object[]) client.callEx(ConstUtils.API_newComment, params);
    }

    /**
     * 根据id获取评论
     *
     * @param commentId
     * @return
     */
    @Override
    public Object getComment(int commentId) throws Exception {
        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword(),
                commentId
        };
        return (Object) client.callEx(ConstUtils.API_getComment, params);
    }

    /**
     * 根据id编辑一个评论
     *
     * @param commentId
     * @param struct
     * @return
     */
    @Override
    public Object[] editComment(int commentId, Map<String, Object> struct) throws Exception {
        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword(),
                commentId,
                struct
        };
        return (Object[]) client.callEx(ConstUtils.API_editComment, params);
    }

    /**
     * 根据要求获取评论
     *
     * @param struct
     * @return
     */
    @Override
    public Object[] getComments(Map<String, Object> struct) throws Exception {

        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword(),
                struct
        };
        return (Object[]) client.callEx(ConstUtils.API_getComments, params);
    }

    /**
     * 根据id获取文章评论各状态个数
     *
     * @param postId
     */
    @Override
    public Object getCommentCount(int postId) throws Exception {
        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword(),
                postId
        };
        return (Object) client.callEx(ConstUtils.API_getCommentCount, params);
    }

    /**
     * 根据id删除评论
     *
     * @param commentId
     * @return
     */
    @Override
    public Object[] deleteComment(int commentId) throws Exception {
        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword(),
                commentId
        };
        return (Object[]) client.callEx(ConstUtils.API_deleteComment, params);
    }

    /**
     * 根据要求上次一个文件
     *
     * @return
     */
    @Override
    public Object[] newMedia(Map<String, Object> data) throws Exception {
        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword(),
                data
        };
        return (Object[]) client.callEx(ConstUtils.API_newMedia, params);
    }

    /**
     * 根据id获取文件
     *
     * @param attachmentId
     * @return
     */
    @Override
    public Object getMedia(int attachmentId) {
        return null;
    }

    /**
     * 根据要求获取所有文件
     *
     * @param struct
     * @return
     */
    @Override
    public Object[] getMedias(HashMap<String, Object> struct) throws Exception {
        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword(),
                struct
        };
        return (Object[]) client.callEx(ConstUtils.API_getMedias, params);
    }

    /**
     * 获取标签
     *
     * @return
     */
    @Override
    public Object[] getTags() throws Exception {

        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword()
        };
        return (Object[]) client.callEx(ConstUtils.API_getTags, params);
    }

    /**
     * 通过pageId获取指定独立页面
     *
     * @param struct
     * @return
     */
    @Override
    public Object[] getPage(int postId, Map<String, Object> struct) throws Exception {
        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword(),
                postId,
                struct
        };
        return (Object[]) client.callEx(ConstUtils.API_getPage, params);
    }

    /**
     * 获取所有的独立页面
     *
     * @return
     */
    @Override
    public Object[] getPages(Map<String, Object> struct) throws Exception {
        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword(),
                struct
        };
        return (Object[]) client.callEx(ConstUtils.API_getRecentPages, params);
    }

    /**
     * 撰写一个独立页面
     *
     * @param content
     * @param publish
     * @return
     */
    @Override
    public Object newPage(HashMap<String, Object> content, boolean publish) {
        return null;
    }

    /**
     * 根据id删除独立页面
     *
     * @param pageId
     * @return
     */
    @Override
    public boolean deletePage(int pageId) {
        return false;
    }

    /**
     * 根据id编辑独立页面
     * @param content
     * @throws Exception
     */
    @Override
    public void editPage(Map<String, Object> content) throws Exception {
        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword(),
                content
        };
        client.callEx(ConstUtils.API_editPage, params);
    }

    /**
     * 撰写一个文章
     *
     * @param content
     * @return
     */
    @Override
    public Object[] newPost(Map<String, Object> content) throws Exception {
        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword(),
                content
        };
        return (Object[]) client.callEx(ConstUtils.API_newPost, params);
    }

    /**
     * 根据id编辑文章
     *
     * @param content
     * @return
     */
    @Override
    public Object[] editPost(Map<String, Object> content) throws Exception {
        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword(),
                content
        };
        return (Object[]) client.callEx(ConstUtils.API_editPost, params);
    }

    /**
     * 根据id删除文章
     *
     * @param postId
     * @return
     */
    @Override
    public Object[] deletePost(int postId) throws Exception {
        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword(),
                postId
        };
        return (Object[]) client.callEx(ConstUtils.API_deletePost, params);
    }

    /**
     * 根据id获取文章
     *
     * @param postId
     * @return
     */
    @Override
    public Object[] getPost(int postId, Map<String, Object> struct) throws Exception {
        Object[] params = new Object[]{
                personage.getId(),
                personage.getName(),
                personage.getPassword(),
                postId,
                struct
        };
        return (Object[]) client.callEx(ConstUtils.API_getPost, params);
    }

    /**
     * 根据id修改文章的归属分类
     *
     * @param postId
     * @param categories
     * @return
     */
    @Override
    public boolean editPostCategories(int postId, String categories) {
        return false;
    }

}
