package com.example.project.wisataapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.project.wisataapp.R;
import com.example.project.wisataapp.model.ListSubKategoriModel;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class AdpSubKategori extends RecyclerView.Adapter<AdpSubKategori.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<ListSubKategoriModel> mArrayList;
    private ArrayList<ListSubKategoriModel> mFilteredList;

    public AdpSubKategori(Context contextku, ArrayList<ListSubKategoriModel> arrayList) {
        context = contextku;
        mArrayList = arrayList;
        mFilteredList = arrayList;
    }

    @Override
    public AdpSubKategori.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_sub_kategori, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdpSubKategori.ViewHolder viewHolder, int i) {
        viewHolder.tv_kode.setText(String.valueOf(mFilteredList.get(i).getKodeSubKategori()));
        viewHolder.tv_nama.setText(mFilteredList.get(i).getNamaSubKategori());
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = mArrayList;
                } else {
                    ArrayList<ListSubKategoriModel> filteredList = new ArrayList<>();
                    for (ListSubKategoriModel entity : mArrayList) {
                        if (entity.getNamaSubKategori().toLowerCase().contains(charString) ) {
                            filteredList.add(entity);
                        }
                    }
                    mFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<ListSubKategoriModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tv_kode,tv_nama;

        public ViewHolder(View view) {
            super(view);
            tv_kode = (TextView)view.findViewById(R.id.txt_view_sub_kategori_kode);
            tv_nama = (TextView)view.findViewById(R.id.txt_view_sub_kategori_nama);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.putExtra("kodeSubKategori", tv_kode.getText().toString());
            intent.putExtra("namaSubKategori", tv_nama.getText().toString());
            ((Activity)context).setResult(RESULT_OK, intent);
            ((Activity)context).finish();
        }
    }
}
