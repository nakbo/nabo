package cn.krait.nabo.module.object;

import java.io.Serializable;

/**
 * @author 权那他(Kraity)
 * @date 2019/10/2.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class NewPostObject implements Serializable {
    private String
            type,
            pattern,
            title,
            text,
            slug,
            password,
            template;
    private int
            cid,
            allowComment,
            allowPing;

    private String[] categories, tags;

    public void setCid(int cid) {
        this.cid = cid;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setAllowComment(int allowComment) {
        this.allowComment = allowComment;
    }

    public void setAllowPing(int allowPing) {
        this.allowPing = allowPing;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public int getCid() {
        return cid;
    }

    public String getSlug() {
        return slug;
    }

    public String getPattern() {
        return pattern;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getPassword() {
        return password;
    }

    public String getTemplate() {
        return template;
    }

    public String getType() {
        return type;
    }

    public int getAllowComment() {
        return allowComment;
    }

    public int getAllowPing() {
        return allowPing;
    }

    public String[] getCategories() {
        return categories;
    }

    public String[] getTags() {
        return tags;
    }

    public boolean check() {
        return !(title.isEmpty() || text.isEmpty());
    }

    public boolean typeIsEmpty(){
        return type == null || type.isEmpty();
    }

    public boolean patternIsEmpty(){
        return pattern == null || pattern.isEmpty();
    }

    public boolean titleIsEmpty(){
        return title == null || title.isEmpty();
    }

    public boolean textIsEmpty(){
        return text == null || text.isEmpty();
    }

    public boolean slugIsEmpty(){
        return slug == null || slug.isEmpty();
    }

    public boolean passwordIsEmpty(){
        return password == null || password.isEmpty();
    }

    public boolean templateIsEmpty(){
        return template == null || template.isEmpty();
    }

    public boolean cidIsEmpty(){
        return cid == 0;
    }

    public boolean categoriesIsEmpty(){
        return categories == null;
    }

    public boolean tagsIsEmpty(){
        return tags == null;
    }

}
