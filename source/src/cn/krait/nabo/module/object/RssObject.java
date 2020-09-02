package cn.krait.nabo.module.object;

import com.einmalfel.earl.Item;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 权那他(Kraity)
 * @date 2019/11/2.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class RssObject implements Serializable {
    Item item;
    String title;
    String link;
    String description;
    String author;
    String siteTitle;
    String siteUrl;
    Date date;

    public void setSiteTitle(String siteTitle) {
        this.siteTitle = siteTitle;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

    public String getSiteTitle() {
        return siteTitle;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public Item getItem() {
        return item;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }
}
