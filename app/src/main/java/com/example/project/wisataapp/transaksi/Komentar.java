package com.example.project.wisataapp.transaksi;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.project.wisataapp.R;
import com.example.project.wisataapp.adapter.AdpKomentar;
import com.example.project.wisataapp.model.KomentarModel;
import com.example.project.wisataapp.utilities.AppController;
import com.example.project.wisataapp.utilities.CheckValidation;
import com.example.project.wisataapp.utilities.CircleTransform;
import com.example.project.wisataapp.utilities.Link;
import com.example.project.wisataapp.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Komentar extends AppCompatActivity {

    private ImageView imgBack, imgProfile;
    private ListView lsvComent;
    private TextView txtStatus, txtKirim;
    private ProgressBar prbStatus;
    private EditText edComent;
    private AdpKomentar adapter;
    private ArrayList<KomentarModel>columnlist= new ArrayList<KomentarModel>();
    private String idBerita, idUser, profile, login;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);
        Bundle bundle	 = getIntent().getExtras();
        if (bundle!=null){
            idBerita	= bundle.getString("idBerita");
            idUser		= bundle.getString("idUser");
            profile     = bundle.getString("profile");
            login       = bundle.getString("login");
        }
        imgBack = (ImageView)findViewById(R.id.img_comment_back);
        lsvComent = (ListView)findViewById(R.id.LsvComment);
        txtStatus = (TextView)findViewById(R.id.TvCommentStatus);
        prbStatus = (ProgressBar)findViewById(R.id.PbrCommentStatus);
        imgProfile = (ImageView)findViewById(R.id.img_coment_profile);
        edComent = (EditText)findViewById(R.id.EComment);
        txtKirim = (TextView)findViewById(R.id.txt_comment_kirim);

        adapter		= new AdpKomentar(Komentar.this, R.layout.col_comment, columnlist, login);
        lsvComent.setAdapter(adapter);
        getDataComment(Link.BASE_URL_API+"getListKomentar.php?idBerita="+idBerita);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Glide.with(this).load(profile)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()){
                    edComent.setEnabled(false);
                    txtKirim.setEnabled(false);
                    txtKirim.setTextColor(Color.parseColor("#757575"));
                    sendComment(Link.BASE_URL_API + "addKomentar.php");
                }
            }
        });

        edComent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                    txtKirim.setEnabled(true);
                    txtKirim.setTextColor(Color.parseColor("#6495ED"));
                }else{
                    txtKirim.setEnabled(false);
                    txtKirim.setTextColor(Color.parseColor("#757575"));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        columnlist = new ArrayList<KomentarModel>();
        adapter		= new AdpKomentar(Komentar.this, R.layout.col_comment, columnlist, login);
        lsvComent.setAdapter(adapter);
        getDataComment(Link.BASE_URL_API+"getListKomentar.php?idBerita="+idBerita);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private boolean validation(){
        boolean val =true;
        if(!CheckValidation.hasText(edComent, getResources().getString(R.string.ValComment)))val= false;
        return val;
    }

    private void sendComment(String save){
        pDialog.setMessage("Please Wait....");
        showDialog();
        StringRequest register = new StringRequest(Request.Method.POST, save,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // TODO Auto-generated method stub
                        VolleyLog.d("Respone", response.toString());

                        try {
                            JSONObject jsonrespon = new JSONObject(response);
                            int Sucsess = jsonrespon.getInt("success");
                            Log.i("Suceses", String.valueOf(Sucsess));
                            if (Sucsess == 1 ){
                                edComent.setEnabled(true);
                                txtKirim.setEnabled(true);
                                txtKirim.setTextColor(Color.parseColor("#6495ED"));
                                edComent.setText("");
                                getDataComment(Link.BASE_URL_API+"getListKomentar.php?idBerita="+idBerita);
                            }else{
                                edComent.setEnabled(true);
                                txtKirim.setEnabled(true);
                                txtKirim.setTextColor(Color.parseColor("#6495ED"));
                                Toast.makeText(Komentar.this,
                                        jsonrespon.getString("message"), Toast.LENGTH_SHORT)
                                        .show();
                            }
                            hideDialog();
                        } catch (Exception e) {
                            edComent.setEnabled(true);
                            txtKirim.setEnabled(true);
                            txtKirim.setTextColor(Color.parseColor("#6495ED"));
                            hideDialog();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("idBerita", idBerita);
                params.put("idUser", idUser);
                params.put("isi", edComent.getText().toString());
                params.put("device", Utils.getDeviceName());
                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(register);
    }

    private void getDataComment(String Url){
        txtStatus.setVisibility(View.GONE);
        prbStatus.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonget = new JsonObjectRequest(Request.Method.GET, Url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int sucses= response.getInt("success");
                            Log.i("Status", String.valueOf(sucses));
                            if (sucses==1){
                                txtStatus.setVisibility(View.GONE);
                                prbStatus.setVisibility(View.GONE);
                                adapter.clear();
                                JSONArray JsonArray = response.getJSONArray("Comment");
                                for (int i = 0; i < JsonArray.length(); i++) {
                                    JSONObject object = JsonArray.getJSONObject(i);
                                    KomentarModel colums 	= new KomentarModel();
                                    colums.setIdComent(object.getInt("idKomentar"));
                                    colums.setUserName(object.getString("idUser"));
                                    colums.setIdBerita(object.getInt("idBerita"));
                                    colums.setIsiComent(object.getString("komentar"));
                                    colums.setTglKomentar(object.getString("tglKomentar"));
                                    colums.setFoto(object.getString("fotoUser"));
                                    colums.setCstat(object.getString("cstat"));
                                    columnlist.add(colums);
                                }
                            }else{
                                txtStatus.setVisibility(View.VISIBLE);
                                txtStatus.setText("Tidak Ada Komentar");
                                prbStatus.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                        lsvComent.invalidate();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    txtStatus.setVisibility(View.VISIBLE);
                    txtStatus.setText("Check Koneksi Internet Anda");
                    prbStatus.setVisibility(View.GONE);
                } else if (error instanceof AuthFailureError) {
                    txtStatus.setVisibility(View.VISIBLE);
                    txtStatus.setText("AuthFailureError");
                    prbStatus.setVisibility(View.GONE);
                } else if (error instanceof ServerError) {
                    txtStatus.setVisibility(View.VISIBLE);
                    txtStatus.setText("Check ServerError");
                    prbStatus.setVisibility(View.GONE);
                } else if (error instanceof NetworkError) {
                    txtStatus.setVisibility(View.VISIBLE);
                    txtStatus.setText("Check NetworkError");
                    prbStatus.setVisibility(View.GONE);
                } else if (error instanceof ParseError) {
                    txtStatus.setVisibility(View.VISIBLE);
                    txtStatus.setText("Check ParseError");
                    prbStatus.setVisibility(View.GONE);
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
