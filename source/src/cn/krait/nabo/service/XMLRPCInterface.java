package cn.krait.nabo.service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 权那他(Kraity)
 * @date 2019/7/23.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public interface XMLRPCInterface {

    /**
     * listMethods
     *
     * @return
     */
    Object[] listMethods(

    ) throws Exception;

    /**
     * 资料
     *
     * @return
     */
    Object[] getUser(

    ) throws Exception;

    /**
     * 博客系统配置清单
     *
     * @param option
     * @return
     */
    Object[] getOptions(
            Object[] option
    );

    /**
     * 设置博客系统配置
     *
     * @param option
     * @return
     */
    Object[] setOptions(
            Object[] option
    );

    /**
     * 根据id获取独立页面
     *
     * @param struct
     * @return
     */
    Object[] getPage(
            int postId,
            Map<String, Object> struct
    ) throws Exception;

    /**
     * 获取所有独立页面
     *
     * @return
     */
    Object[] getPages(
            Map<String, Object> struct
    ) throws Exception;

    /**
     * 撰写一个独立页面
     *
     * @param content
     * @param publish
     * @return
     */
    Object newPage(
            HashMap<String, Object> content,
            boolean publish
    );

    /**
     * 根据id删除独立页面
     *
     * @param pageId
     * @return
     */
    boolean deletePage(
            int pageId
    );

    /**
     * 根据id编辑独立页面
     * @param content
     * @throws Exception
     */
    void editPage(
            Map<String, Object> content
    ) throws Exception;

    /**
     * 撰写一个文章
     *
     * @param content
     * @return
     */
    Object[] newPost(
            Map<String, Object> content
    ) throws Exception;

    /**
     * 根据id编辑文章
     *
     * @param content
     * @return
     */
    Object[] editPost(
            Map<String, Object> content
    ) throws Exception;

    /**
     * 根据id删除文章
     *
     * @param postId
     * @return
     */
    Object[] deletePost(
            int postId
    ) throws Exception;

    /**
     * 根据id获取文章
     *
     * @param postId
     * @return
     */
    Object[] getPost(
            int postId,
            Map<String, Object> struct
    ) throws Exception;

    /**
     * 根据id修改文章的归属分类
     *
     * @param postId
     * @param categories
     * @return
     */
    boolean editPostCategories(
            int postId,
            String categories
    );

    /**
     * 获取文章
     *
     * @param struct
     * @return
     */
    Object[] getPosts(
            Map<String, Object> struct
    ) throws Exception;

    /**
     * 常用统计
     *
     * @return
     */
    Object[] getStat(
    ) throws Exception;

    /**
     * 获取postNum个文章post title
     *
     * @param postsNum
     * @return
     */
    Object[] getRecentPostTitles(
            int postsNum
    ) throws Exception;

    /**
     * 获取所有分类
     *
     * @return
     */
    Object[] getCategories(

    ) throws Exception;

    /**
     * 根据id删除分类
     *
     * @param categoriesId
     * @return
     */
    boolean deleteCategories(
            int categoriesId
    ) throws Exception;

    /**
     * 根据path增加一个评论
     *
     * @param path
     * @param struct
     * @return
     */
    Object[] newComment(
            String path,
            Map<String, Object> struct
    ) throws Exception;

    /**
     * 根据id获取评论
     *
     * @param commentId
     * @return
     */
    Object getComment(
            int commentId
    ) throws Exception;

    /**
     * 根据id编辑一个评论
     *
     * @param commentId
     * @param struct
     * @return
     */
    Object[] editComment(
            int commentId,
            Map<String, Object> struct
    ) throws Exception;

    /**
     * 根据要求获取评论
     *
     * @param struct
     * @return
     */
    Object[] getComments(
            Map<String, Object> struct
    ) throws Exception;

    /**
     * 根据id获取文章评论各状态个数
     *
     * @param postId
     */
    Object getCommentCount(
            int postId
    ) throws Exception;

    /**
     * 根据id删除评论
     *
     * @param commentId
     * @return
     */
    Object[] deleteComment(
            int commentId
    ) throws Exception;

    /**
     * 根据要求上次一个文件
     *
     * @return
     */
    Object[] newMedia(
            Map<String, Object> data
    ) throws Exception;

    /**
     * 根据id获取文件
     *
     * @param attachmentId
     * @return
     */
    Object getMedia(
            int attachmentId
    );

    /**
     * 根据要求获取所有文件
     *
     * @param struct
     * @return
     */
    Object[] getMedias(
            HashMap<String, Object> struct
    ) throws Exception;

    /**
     * 获取标签
     *
     * @return
     */
    Object[] getTags() throws Exception;

}
