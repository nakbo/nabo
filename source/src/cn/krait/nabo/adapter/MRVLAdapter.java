package cn.krait.nabo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ocnyang.contourview.ContourView;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.krait.nabo.R;
import cn.krait.nabo.activity.page.WebViewActivity;
import cn.krait.nabo.module.ACache.ACache;
import cn.krait.nabo.util.OtherUtil;
import cn.krait.nabo.module.personage.Personage;
import cn.krait.nabo.module.object.RssObject;
import cn.krait.nabo.module.object.SettingObject;


/**
 * @author 权那他(Kraity)
 * @date 2019/8/2.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class MRVLAdapter extends RecyclerView.Adapter<MRVLAdapter.VerticalViewHolder> {

    private Context mContext;
    private Personage personage;
    private ACache personageACache;
    private SettingObject setting;
    private RssObject[] rssArray;

    public MRVLAdapter(Context context, Personage s, ACache a) {
        mContext = context;
        personage = s;
        personageACache = a;
        setting = personage.getSetting();
    }

    public void orientation(RssObject[] rss) {
        rssArray = rss;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_recycle_other_item, parent, false);
        return new VerticalViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VerticalViewHolder holder, int position) {

        final RssObject rss = rssArray[position];

        holder.contourView.setBackgroundColor(setting.getColorPrimary());
        holder.contourView.setShaderEndColor(setting.getColorAccent());
        holder.contourView.setShaderStartColor(setting.getColorPrimary());

        String[] date = dateStandardRss(rss.getDate());

        holder.title.setText(rss.getTitle());
        holder.date.setText(
                OtherUtil.getTimeFormatText(rss.getDate(),false)
        );
        holder.date_mon.setText(date[1]);
        holder.date_day.setText(date[2]);
        holder.markdownView.setText(Html.fromHtml(rss.getDescription()));
        try {
            java.net.URL Url = new java.net.URL(rss.getSiteUrl());
            holder.link.setText(Url.getHost());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        holder.siteTitle.setText(rss.getSiteTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Uri uri = Uri.parse(link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);*/
                Intent intent = new Intent(mContext, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("link", rss.getLink());

                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

    }

    private static String[] dateStandardRss(Date date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy/MMM/dd/HH/mm/ss");
        return sf2.format(date).split("/");
    }

    @Override
    public int getItemCount() {
        return rssArray == null ? 0 : rssArray.length;
    }

    class VerticalViewHolder extends RecyclerView.ViewHolder {

        TextView title,date,link,markdownView,siteTitle,date_mon,date_day;
        ContourView contourView;

        VerticalViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.rss_title);
            date = itemView.findViewById(R.id.rss_date);
            date_day = itemView.findViewById(R.id.date_day);
            date_mon = itemView.findViewById(R.id.date_mon);
            markdownView = itemView.findViewById(R.id.rss_excerpt);
            link = itemView.findViewById(R.id.rss_link);
            siteTitle = itemView.findViewById(R.id.rss_siteTitle);

            contourView = itemView.findViewById(R.id.contourView);
        }
    }
}
