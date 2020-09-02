package cn.krait.nabo.adapter.page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.ocnyang.contourview.ContourView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.krait.nabo.R;
import cn.krait.nabo.service.XMLRPCService;
import cn.krait.nabo.module.ACache.ACache;
import cn.krait.nabo.util.ConstUtils;
import cn.krait.nabo.util.OtherUtil;
import cn.krait.nabo.module.personage.Personage;
import cn.krait.nabo.module.object.SettingObject;

/**
 * @author 权那他(Kraity)
 * @date 2019/8/11.
 * GitHub：https://github.com/kraity
 * WebSite：https://krait.cn
 * email：kraits@qq.com
 */
public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.GridViewHolder> {

    private Context mContext;
    private ACache personageACache;
    private XMLRPCService XMLRPCService;
    private Personage personage;
    private JSONArray medias;
    private SettingObject setting;

    public MediaAdapter(Context context, Personage p, ACache a, XMLRPCService x) {
        mContext = context;
        personageACache = a;
        XMLRPCService = x;
        personage = p;
        setting = p.getSetting();
    }

    public void setGridDataList(JSONArray m) {
        medias = m;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.page_recycle_media_item, parent, false);
        return new GridViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull GridViewHolder holder, int position) {

        JSONObject media;

        holder.contourView.setBackgroundColor(setting.getColorPrimary());
        holder.contourView.setShaderEndColor(setting.getColorAccent());
        holder.contourView.setShaderStartColor(setting.getColorPrimary());

        try {
            final String title,url,lastUpdate,type;
            media = medias.getJSONObject(position);
            title = media.getString(ConstUtils.MEDIA_ACACHE_NAME);
            url = media.getString(ConstUtils.MEDIA_ACACHE_URL);
            //bitmapName = "media_" + media.getString("attachment_id");
            lastUpdate = OtherUtil.getTimeFormatText(OtherUtil.stringToDate(media.getString(ConstUtils.MEDIA_ACACHE_DATECREATED)), true);
            type = OtherUtil.getFileNameWithSuffixPrefix(url);

            holder.date.setText(type);
            holder.title.setText(title);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    StringBuilder  stringBuilder = new StringBuilder();
                    stringBuilder.append("类型: ").append(type).append("\n\n");
                    stringBuilder.append("上传于").append(lastUpdate);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                            .setTitle(title)
                            .setMessage(stringBuilder)
                            .setPositiveButton("复制链接", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    OtherUtil.copyText(mContext,url,"复制成功");
                                }
                            })
                            .setNegativeButton("查看", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    OtherUtil.openWebView(mContext,url);
                                }
                            });
                    builder.create().show();
               }
            });

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("e", e.toString());
        }

    }

    @Override
    public int getItemCount() {
        return medias == null ? 0 : medias.length();
    }

    class GridViewHolder extends RecyclerView.ViewHolder {

        TextView date, title;
        ContourView contourView;

        GridViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            title = itemView.findViewById(R.id.title);
            contourView = itemView.findViewById(R.id.ContourView);
        }
    }
}
