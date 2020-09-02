package cn.krait.nabo.module.object;

import android.graphics.Color;

/**
 * @author 权那他(Kraity)
 * @date 2019/10/3.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class SettingObject {
    private int postsPageSize;
    private int commentsPageSize,
            postTextLength;
    private boolean allowCommentsRefresh,
            checkVersionAble,
            imageUrlReplaceAble,
            articleDeleteAble;
    private int colorPrimary, colorAccent;

    public void initialization() {
        setPostsPageSize(9);
        setCommentsPageSize(12);
        setPostTextLength(512);
        setAllowCommentsRefresh(true);
        setImageUrlReplaceAble(true);
        setCheckVersionAble(true);
        setArticleDeleteAble(true);
        setColorPrimary("#673AB7");
        setColorAccent("#40C4FF");

    }

    public void setCheckVersionAble(boolean checkVersionAble) {
        this.checkVersionAble = checkVersionAble;
    }

    public boolean getCheckVersionAble() {
        return checkVersionAble;
    }

    public void setPostsPageSize(int postsPageSize) {
        this.postsPageSize = postsPageSize;
    }

    public void setCommentsPageSize(int commentsPageSize) {
        this.commentsPageSize = commentsPageSize;
    }

    public void setAllowCommentsRefresh(boolean allowCommentsRefresh) {
        this.allowCommentsRefresh = allowCommentsRefresh;
    }

    public int getCommentsPageSize() {
        return commentsPageSize;
    }

    public int getPostsPageSize() {
        return postsPageSize;
    }

    public String getStringCommentsPageSize() {
        return String.valueOf(commentsPageSize);
    }

    public String getStringPostsPageSize() {
        return String.valueOf(postsPageSize);
    }

    public boolean getAllowCommentsRefresh() {
        return allowCommentsRefresh;
    }

    public void setColorAccent(String colorAccent) {
        this.colorAccent = Color.parseColor(colorAccent);
    }

    public void setColorPrimary(String colorPrimary) {
        this.colorPrimary = Color.parseColor(colorPrimary);
    }

    public int getColorAccent() {
        return colorAccent;
    }

    public int getColorPrimary() {
        return colorPrimary;
    }

    public void setImageUrlReplaceAble(boolean imageUrlReplaceAble) {
        this.imageUrlReplaceAble = imageUrlReplaceAble;
    }

    public boolean getImageUrlReplaceAble() {
        return imageUrlReplaceAble;
    }

    public void setArticleDeleteAble(boolean articleDeleteAble) {
        this.articleDeleteAble = articleDeleteAble;
    }

    public boolean getArticleDeleteAble() {
        return articleDeleteAble;
    }

    public void setPostTextLength(int postTextLength) {
        this.postTextLength = postTextLength;
    }

    public int getPostTextLength() {
        return postTextLength;
    }

    public String getStringPostTextLength() {
        return String.valueOf(postTextLength);
    }
}
