package com.example.project.wisataapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.wisataapp.R;
import com.example.project.wisataapp.model.DataBeritaNearby;
import com.example.project.wisataapp.utilities.Link;
import com.example.project.wisataapp.utilities.Utils;

import java.util.List;

public class AdpNearby extends ArrayAdapter<DataBeritaNearby> {

    private List<DataBeritaNearby> columnslist;
    private LayoutInflater vi;
    private int Resource;
    private ViewHolder holder;
    private Context context;
    private String statusku;

    public AdpNearby(Context context, int resource, List<DataBeritaNearby> objects, String status) {
        super(context, resource,  objects);
        this.context = context;
        vi	=	(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource		= resource;
        columnslist		= objects;
        statusku = status;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v	=	convertView;
        if (v == null){
            holder	=	new ViewHolder();
            v= vi.inflate(Resource, null);
            holder.ImgGmbar		= 	 (ImageView)v.findViewById(R.id.ImgColNearbyGbr1);
            //holder.TvDate		=	 (TextView)v.findViewById(R.id.TvColBeritaDate);
            holder.TvJudul		=	 (TextView)v.findViewById(R.id.TvColNearbyJudul);
            holder.TvJarak		=	 (TextView)v.findViewById(R.id.TvColNearbyJarak);
            v.setTag(holder);
        }else{
            holder 	= (ViewHolder)v.getTag();
        }

        String url 		= Link.BASE_URL_IMAGE_BERITA+columnslist.get(position).getPathGbr();
        holder.TvJudul.setText(columnslist.get(position).getNamaTempat());
        holder.TvJarak.setText("Jarak: "+ String.valueOf(columnslist.get(position).getJarak())+" KM");
        if (holder.ImgGmbar.getTag()==null||!holder.ImgGmbar.getTag().equals(url)){
            Utils.getImage(url, holder.ImgGmbar, getContext());
        }
        return v;
    }

    static class ViewHolder{
        private ImageView ImgGmbar;
        private TextView TvJudul;
        private TextView TvJarak;
    }
}
