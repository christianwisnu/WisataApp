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
import com.example.project.wisataapp.adapter.AdpData;
import com.example.project.wisataapp.model.DataBeritaModel;
import com.example.project.wisataapp.transaksi.InfoData;
import com.example.project.wisataapp.utilities.AppController;
import com.example.project.wisataapp.utilities.Link;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FrgData extends Fragment {

    private String jenisData, login, idUser, profile, subKategori, statusSearch, namaSearch;
    private View vupload;
    private ListView lsvupload;
    private ArrayList<DataBeritaModel> columnlist= new ArrayList<DataBeritaModel>();
    private TextView tvstatus;
    private ProgressBar prbstatus;
    private AdpData adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle	 = this.getArguments();
        if (bundle!=null){
            jenisData	= bundle.getString("data");
            login = bundle.getString("login");
            idUser = bundle.getString("idUser");
            profile = bundle.getString("profile");
            subKategori = bundle.getString("subKategori");
            statusSearch = bundle.getString("search");
            namaSearch = bundle.getString("namaSearch");
        }
        vupload     = inflater.inflate(R.layout.list_data,container,false);
        lsvupload	= (ListView)vupload.findViewById(R.id.LsvData);
        tvstatus	= (TextView)vupload.findViewById(R.id.TvStatusData);
        prbstatus	= (ProgressBar)vupload.findViewById(R.id.PrbStatusData);
        adapter		= new AdpData(getActivity(), R.layout.col_berita, columnlist, login);
        lsvupload.setAdapter(adapter);
        if(statusSearch.equals("N")){
            getDataUpload(Link.BASE_URL_API+"getData2.php?idKategori="+jenisData);
        }else{
            String query="";
            if(namaSearch.equals("%")){
                query="ALL";
            }else{
                query=namaSearch;
            }
            getDataUpload(Link.BASE_URL_API+"getSearch.php?idKategori="+jenisData+"&subKategori="+subKategori
            +"&search="+query);
        }


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
        columnlist = new ArrayList<DataBeritaModel>();
        adapter		= new AdpData(getActivity(), R.layout.col_berita, columnlist, login);
        lsvupload.setAdapter(adapter);
        if(statusSearch.equals("N")){
            getDataUpload(Link.BASE_URL_API+"getData2.php?idKategori="+jenisData);
        }else{
            String query="";
            if(namaSearch.equals("%")){
                query="ALL";
            }else{
                query=namaSearch;
            }
            getDataUpload(Link.BASE_URL_API+"getSearch.php?idKategori="+jenisData+"&subKategori="+subKategori
                    +"&search="+query);
        }
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
                                    DataBeritaModel colums 	= new DataBeritaModel();
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
                                    columnlist.add(colums);
                                }
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
                // TODO Auto-generated method stub
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("Check Koneksi Internet Anda");
                    prbstatus.setVisibility(View.GONE);
                } else if (error instanceof AuthFailureError) {
                    //TODO
                    tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("AuthFailureError");
                    prbstatus.setVisibility(View.GONE);
                } else if (error instanceof ServerError) {
                    //TODO
                    tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("Check ServerError");
                    prbstatus.setVisibility(View.GONE);
                } else if (error instanceof NetworkError) {
                    //TODO
                    tvstatus.setVisibility(View.VISIBLE);
                    tvstatus.setText("Check NetworkError");
                    prbstatus.setVisibility(View.GONE);
                } else if (error instanceof ParseError) {
                    //TODO
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
}
//https://android-arsenal.com/details/1/6143
//https://android-arsenal.com/details/1/5836