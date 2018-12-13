package com.example.project.wisataapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.example.project.wisataapp.R;
import com.example.project.wisataapp.adapter.AdpNearby;
import com.example.project.wisataapp.model.DataBeritaNearby;
import com.example.project.wisataapp.transaksi.InfoData;
import com.example.project.wisataapp.utilities.AppController;
import com.example.project.wisataapp.utilities.Link;
import com.example.project.wisataapp.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class FrgNearby extends Fragment{

    private String login, idUser, profile;
    private double latPosisi, longtPosisi;
    private View vupload;
    private ListView lsvupload;
    private ArrayList<DataBeritaNearby> columnlist= new ArrayList<DataBeritaNearby>();
    private TextView tvstatus;
    private ProgressBar prbstatus;
    private AdpNearby adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle	 = this.getArguments();
        if (bundle!=null){
            login = bundle.getString("login");
            idUser = bundle.getString("idUser");
            profile = bundle.getString("profile");
            latPosisi = bundle.getDouble("latt");
            longtPosisi = bundle.getDouble("longt");
        }
        vupload     = inflater.inflate(R.layout.list_nearby,container,false);
        lsvupload	= (ListView)vupload.findViewById(R.id.LsvNearby);
        tvstatus	= (TextView)vupload.findViewById(R.id.TvStatusNearby);
        prbstatus	= (ProgressBar)vupload.findViewById(R.id.PrbStatusNearby);
        adapter		= new AdpNearby(getActivity(), R.layout.col_nearby, columnlist, login);
        lsvupload.setAdapter(adapter);
        getDataUpload(Link.BASE_URL_API+"getAllData.php");

        lsvupload.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), InfoData.class);
                i.putExtra("idBerita", columnlist.get(position).getIdBerita());
                i.putExtra("namaTempat", columnlist.get(position).getNamaTempat());
                i.putExtra("judul", columnlist.get(position).getJudul());
                i.putExtra("isi", columnlist.get(position).getIsi());
                i.putExtra("alamat", columnlist.get(position).getAlamat());
                i.putExtra("latt", columnlist.get(position).getLatt());
                i.putExtra("longt", columnlist.get(position).getLongt());
                i.putExtra("gambar", columnlist.get(position).getPathGbr());
                i.putExtra("idKategori", columnlist.get(position).getIdKategori());
                i.putExtra("kategori", columnlist.get(position).getKategori());
                i.putExtra("idSubKategori", columnlist.get(position).getIdSubKategori());
                i.putExtra("subKategori", columnlist.get(position).getNamaSubKategori());
                i.putExtra("rating", columnlist.get(position).getRating());
                i.putExtra("countUser", columnlist.get(position).getCountUser());
                i.putExtra("jamBuka", columnlist.get(position).getJamBuka());
                i.putExtra("jamTutup", columnlist.get(position).getJamTutup());
                i.putExtra("harga", columnlist.get(position).getHarga());
                i.putExtra("tglEvent", columnlist.get(position).getTglEvent());
                i.putExtra("login", login);
                i.putExtra("idUser", idUser);
                i.putExtra("profile", profile);
                getActivity().startActivity(i);
            }
        });
        return vupload;
    }

    @Override
    public void onResume() {
        super.onResume();
        columnlist = new ArrayList<DataBeritaNearby>();
        adapter		= new AdpNearby(getActivity(), R.layout.col_nearby, columnlist, login);
        lsvupload.setAdapter(adapter);
        getDataUpload(Link.BASE_URL_API+"getAllData.php");
    }

    private void getDataUpload(String Url){
        tvstatus.setVisibility(View.GONE);
        prbstatus.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonget = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int sucses= response.getInt("success");
                            Log.i("Status", String.valueOf(sucses));
                            if (sucses==1){
                                tvstatus.setVisibility(View.GONE);
                                prbstatus.setVisibility(View.GONE);
                                adapter.clear();
                                JSONArray JsonArray = response.getJSONArray("uploade");
                                for (int i = 0; i < JsonArray.length(); i++) {
                                    JSONObject object = JsonArray.getJSONObject(i);
                                    DataBeritaNearby colums 	= new DataBeritaNearby();
                                    colums.setIdBerita(object.getInt("idBerita"));
                                    colums.setNamaTempat(object.getString("namaTempat"));
                                    colums.setJudul(object.getString("judul"));
                                    colums.setIsi(object.getString("isi"));
                                    colums.setPathGbr(object.getString("gambar"));
                                    colums.setJamBuka(object.getString("jamBuka"));
                                    colums.setJamTutup(object.getString("jamTutup"));
                                    colums.setTglEvent(object.getString("tglEvent"));
                                    colums.setHarga(object.getDouble("harga"));
                                    colums.setLatt(object.getDouble("latt"));
                                    colums.setLongt(object.getDouble("longt"));
                                    colums.setAlamat(object.getString("alamat"));
                                    colums.setIdKategori(object.getInt("idKategori"));
                                    colums.setKategori(object.getString("kategori"));
                                    colums.setIdSubKategori(object.getInt("idSubKategori"));
                                    colums.setNamaSubKategori(object.getString("subKategori"));
                                    colums.setRating(object.getDouble("rating"));
                                    colums.setCountUser(object.getInt("countUser"));
                                    double jarak = distance(latPosisi, longtPosisi, object.getDouble("latt"),
                                            object.getDouble("longt"));
                                    colums.setJarak(Utils.round(jarak,2));
                                    columnlist.add(colums);
                                }
                                Collections.sort(columnlist, new Urut());
                                //pengurutan columlist
                            }else{
                                tvstatus.setVisibility(View.VISIBLE);
                                tvstatus.setText("Tidak Ada Data");
                                prbstatus.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                        lsvupload.invalidate();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("Check Koneksi Internet Anda");
                    prbstatus.setVisibility(View.GONE);
                } else if (error instanceof AuthFailureError) {
                    tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("AuthFailureError");
                    prbstatus.setVisibility(View.GONE);
                } else if (error instanceof ServerError) {
                    tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("Check ServerError");
                    prbstatus.setVisibility(View.GONE);
                } else if (error instanceof NetworkError) {
                    tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("Check NetworkError");
                    prbstatus.setVisibility(View.GONE);
                } else if (error instanceof ParseError) {
                    tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("Check ParseError");
                    prbstatus.setVisibility(View.GONE);
                }
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json");
                return params;
            }
        };
        AppController.getInstance().getRequestQueue().getCache().invalidate(Url, true);
        AppController.getInstance().addToRequestQueue(jsonget);
    }

    static class Urut implements Comparator {

        @Override
        public int compare(Object o1, Object o2) {
            Double a = Double.valueOf(((DataBeritaNearby)o1).getJarak());
            Double b = Double.valueOf(((DataBeritaNearby)o2).getJarak());
            return a.compareTo(b); //desc. Kalo asc     a.compareTo(b)
        }
    }

    private double distance(double latUser, double lonUser, double latLokasiData, double lonLokasiData) {
        double theta = lonUser - lonLokasiData;
        double dist = Math.sin(deg2rad(latUser))
                * Math.sin(deg2rad(latLokasiData))
                + Math.cos(deg2rad(latUser))
                * Math.cos(deg2rad(latLokasiData))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
