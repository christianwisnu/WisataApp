package com.example.project.wisataapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.wisataapp.R;
import com.example.project.wisataapp.model.DataBeritaModel;
import com.example.project.wisataapp.transaksi.TambahInfo;
import com.example.project.wisataapp.utilities.Link;
import com.example.project.wisataapp.utilities.Utils;

import java.util.List;

public class AdpData extends ArrayAdapter<DataBeritaModel> {

    private List<DataBeritaModel> columnslist;
    private LayoutInflater vi;
    private int Resource;
    private int lastPosition = -1;
    private ViewHolder holder;
    private Context context;
    private String statusku;

    public AdpData(Context context, int resource, List<DataBeritaModel> objects, String status) {
        super(context, resource,  objects);
        this.context = context;
        vi	=	(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource		= resource;
        columnslist		= objects;
        statusku = status;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v	=	convertView;
        if (v == null){
            holder	=	new ViewHolder();
            v= vi.inflate(Resource, null);
            holder.ImgGmbar		= 	 (ImageView)v.findViewById(R.id.ImgColBeritaGbr1);
            //holder.TvDate		=	 (TextView)v.findViewById(R.id.TvColBeritaDate);
            holder.TvJudul		=	 (TextView)v.findViewById(R.id.TvColBeritaJudul);
            holder.TvAlamat		=	 (TextView)v.findViewById(R.id.TvColBeritaAlamat);
            holder.imgDel		= 	 (ImageView)v.findViewById(R.id.ImgColBeritaDelete);
            holder.imgEdit  	= 	 (ImageView)v.findViewById(R.id.ImgColBeritaEdit);
            v.setTag(holder);
        }else{
            holder 	= (ViewHolder)v.getTag();
        }
        if(statusku == null){
            holder.imgEdit.setVisibility(View.GONE);
            holder.imgDel.setVisibility(View.GONE);
        }else if(statusku.equals("U")){
            holder.imgEdit.setVisibility(View.GONE);
            holder.imgDel.setVisibility(View.GONE);
        }else {
            holder.imgEdit.setVisibility(View.VISIBLE);
            holder.imgDel.setVisibility(View.VISIBLE);
        }
        String url 		= Link.BASE_URL_IMAGE_BERITA+columnslist.get(position).getPathGbr();
        //Utils.CurrentTime(columnslist.get(position).getTglTerbit(), holder.TvDate);
        holder.TvJudul.setText(columnslist.get(position).getNamaTempat());
        holder.TvAlamat.setText("Alamat: "+columnslist.get(position).getAlamat());
        //String hari = Utils.getHari(columnslist.get(position).getTglTerbit());
        //holder.TvDate.setText(hari+", "+columnslist.get(position).getTglTerbit()+" WIB");
        if (holder.ImgGmbar.getTag()==null||!holder.ImgGmbar.getTag().equals(url)){
            Utils.getImage(url, holder.ImgGmbar, getContext());
            //holder.ImgGmbar.setTag(url);
        }

        holder.imgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(statusku.equals("A")){
                    Intent i = new Intent(getContext(), TambahInfo.class);
                    i.putExtra("status","DEL");
                    i.putExtra("namaTempat",columnslist.get(position).getNamaTempat());
                    i.putExtra("idBerita",String.valueOf(columnslist.get(position).getIdBerita()));
                    i.putExtra("judul", columnslist.get(position).getJudul());
                    i.putExtra("isi", columnslist.get(position).getIsi());
                    i.putExtra("idKategori", String.valueOf(columnslist.get(position).getIdKategori()));
                    i.putExtra("kategori", columnslist.get(position).getKategori());
                    i.putExtra("idSubKategori", String.valueOf(columnslist.get(position).getIdSubKategori()));
                    i.putExtra("subKategori", columnslist.get(position).getNamaSubKategori());
                    i.putExtra("alamat", columnslist.get(position).getAlamat());
                    i.putExtra("latt", columnslist.get(position).getLatt());
                    i.putExtra("longt", columnslist.get(position).getLongt());
                    i.putExtra("gambar", columnslist.get(position).getPathGbr());
                    i.putExtra("tglEvent", columnslist.get(position).getTglEvent());
                    i.putExtra("jamBuka", columnslist.get(position).getJamBuka());
                    i.putExtra("jamTutup", columnslist.get(position).getJamTutup());
                    i.putExtra("harga", columnslist.get(position).getHarga());
                    getContext().startActivity(i);
                }
            }
        });

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(statusku.equals("A")){
                    Intent i = new Intent(getContext(), TambahInfo.class);
                    i.putExtra("status","EDIT");
                    i.putExtra("namaTempat",columnslist.get(position).getNamaTempat());
                    i.putExtra("idBerita",String.valueOf(columnslist.get(position).getIdBerita()));
                    i.putExtra("judul", columnslist.get(position).getJudul());
                    i.putExtra("isi", columnslist.get(position).getIsi());
                    i.putExtra("idKategori", String.valueOf(columnslist.get(position).getIdKategori()));
                    i.putExtra("kategori", columnslist.get(position).getKategori());
                    i.putExtra("idSubKategori", String.valueOf(columnslist.get(position).getIdSubKategori()));
                    i.putExtra("subKategori", columnslist.get(position).getNamaSubKategori());
                    i.putExtra("alamat", columnslist.get(position).getAlamat());
                    i.putExtra("latt", columnslist.get(position).getLatt());
                    i.putExtra("longt", columnslist.get(position).getLongt());
                    i.putExtra("gambar", columnslist.get(position).getPathGbr());
                    i.putExtra("tglEvent", columnslist.get(position).getTglEvent());
                    i.putExtra("jamBuka", columnslist.get(position).getJamBuka());
                    i.putExtra("jamTutup", columnslist.get(position).getJamTutup());
                    i.putExtra("harga", columnslist.get(position).getHarga());
                    getContext().startActivity(i);
                }
            }
        });

        return v;
    }

    static class ViewHolder{
        private ImageView ImgGmbar;
        private TextView TvJudul;
        private TextView TvAlamat;
        //private TextView TvDate;
        private ImageView imgDel;
        private ImageView imgEdit;
    }
}
