package cn.krait.nabo.module.markdown.utils;


import cn.krait.nabo.R;

/**
 * Created by zk on 2018/1/27.
 */

public enum OperationType {

    BOLD("bold", R.string.format_bold, R.drawable.selector_format_bold, 8),
    ITALIC("italic", R.string.format_italic, R.drawable.selector_format_italic, 7),
    HEADER_1("header_1", R.string.format_header_1, R.drawable.selector_format_header_1, 0),
    HEADER_2("header_2", R.string.format_header_2, R.drawable.selector_format_header_2, 0),
    HEADER_3("header_3", R.string.format_header_3, R.drawable.selector_format_header_3, 0),
    HEADER_4("header_4", R.string.format_header_4, R.drawable.selector_format_header_4, 0),
    HEADER_5("header_5", R.string.format_header_5, R.drawable.selector_format_header_5, 0),
    QUOTE("quote", R.string.format_quote, R.drawable.selector_format_quote, 5),
    STRIKE("strike", R.string.format_strike, R.drawable.selector_format_strike, 6),
    IMG("img", R.string.edit_img, R.drawable.selector_edit_img, 10),
    LINK("link", R.string.link, R.drawable.selector_link, 9),
    LIST("list", R.string.list, R.drawable.selector_list, 4),
    UNDO("undo", R.string.undo, R.drawable.selector_undo, 100001),
    REDO("redo", R.string.redo, R.drawable.selector_redo, 100000),
    TIME("time", R.string.ot_time, R.drawable.selector_time, 3);

    public String key;
    public int describe;
    public int src;
    public int order = 0;

    OperationType(String key, int describe, int src, int order) {
        this.key = key;
        this.describe = describe;
        this.src = src;
        this.order = order;
    }

}
