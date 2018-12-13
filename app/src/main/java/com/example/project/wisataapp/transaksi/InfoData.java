package com.example.project.wisataapp.transaksi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.project.wisataapp.R;
import com.example.project.wisataapp.model.DataBeritaDetailModel;
import com.example.project.wisataapp.model.DataBeritaModel;
import com.example.project.wisataapp.servise.BaseApiService;
import com.example.project.wisataapp.utilities.AppController;
import com.example.project.wisataapp.utilities.Link;
import com.example.project.wisataapp.utilities.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;
import com.willy.ratingbar.ScaleRatingBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoData extends AppCompatActivity implements OnMapReadyCallback,
        BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, RatingDialogListener {

    private GoogleMap mMap;
    private ImageView imgBack, imgGiveRate, imgAddImage;
    private float zoomLevel = (float) 16.0;
    private SliderLayout sliderLayout;
    private TreeMap<Integer,String> treeMap;
    private Integer idBerita, idKategori, a=0, countUser, idSubKategori;
    private String judul, isi, alamat, gbr1, gbr2, gbr3, gbr4, kategori, namaTempat, login, idUser,
        profile, sImage, encodedString, jamBuka, jamTutup, tglEvent, subKategori;
    private double latt, longt, rating=0, harga;
    private BigDecimal hasilRating = BigDecimal.ZERO;
    private TextView txtJudul, txtIsi, txtAlamat, txtNamaTmpt, txtCountUser, txtNilai, txtJamBuka, txtJamTutup, txtHarga,
        txtTglEvent, txtKategori, txtSubKategori;
    private Button btnKoment;
    private ScaleRatingBar ratingDatabase;
    private ProgressDialog pDialog;
    private BaseApiService mApiService, mUploadService;
    private ArrayList<DataBeritaDetailModel> columnlist= new ArrayList<DataBeritaDetailModel>();
    private Uri uImage;
    private LinearLayout linHarga, linTglEvent;
    private DecimalFormat decim = new DecimalFormat("#,###.##");

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imfo_data);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        treeMap = new TreeMap<Integer,String>();
        Bundle i = getIntent().getExtras();
        if (i != null){
            try {
                idBerita = i.getInt("idBerita");
                idKategori = i.getInt("idKategori");
                idSubKategori = i.getInt("idSubKategori");
                kategori = i.getString("kategori");
                subKategori = i.getString("subKategori");
                namaTempat = i.getString("namaTempat");
                judul = i.getString("judul");
                isi = i.getString("isi");
                alamat = i.getString("alamat");
                gbr1 = i.getString("gambar");
                latt = i.getDouble("latt");
                longt = i.getDouble("longt");
                login = i.getString("login");
                idUser = i.getString("idUser");
                profile = i.getString("profile");
                rating = i.getDouble("rating");
                countUser = i.getInt("countUser");
                harga = i.getDouble("harga");
                jamBuka = i.getString("jamBuka");
                jamTutup = i.getString("jamTutup");
                tglEvent = i.getString("tglEvent");
            } catch (Exception e) {}
        }
        getDataUpload(Link.BASE_URL_API+"getListGambar.php?idBerita="+String.valueOf(idBerita));

        txtJudul		= (TextView)findViewById(R.id.txtInfoDataJudul);
        txtIsi = (TextView)findViewById(R.id.txtInfoDataIsi);
        txtNamaTmpt = (TextView)findViewById(R.id.txtInfoDataNamaTempat);
        txtAlamat = (TextView)findViewById(R.id.txtInfoDataAlamat);
        txtCountUser = (TextView)findViewById(R.id.txtUserRating);
        txtNilai = (TextView)findViewById(R.id.txtNilaiRating);
        imgBack = (ImageView)findViewById(R.id.ImbBackInfoData);
        imgGiveRate = (ImageView)findViewById(R.id.imgGiveRating);
        imgAddImage = (ImageView)findViewById(R.id.imgAddNewImage);
        ratingDatabase = (ScaleRatingBar)findViewById(R.id.simpleRatingBar);
        btnKoment = (Button)findViewById(R.id.btnDetailKomentar);
        txtJamBuka = (TextView)findViewById(R.id.txtInfoDataJamBuka);
        txtJamTutup = (TextView)findViewById(R.id.txtInfoDataJamTutup);
        txtHarga = (TextView)findViewById(R.id.txtInfoDataHarga);
        txtTglEvent = (TextView)findViewById(R.id.txtInfoTglEvent);
        txtKategori = (TextView)findViewById(R.id.txtInfoKategori);
        txtSubKategori = (TextView)findViewById(R.id.txtInfoSubKategori);
        linHarga = (LinearLayout)findViewById(R.id.linInfoHarga);
        linTglEvent = (LinearLayout)findViewById(R.id.linInfoTglEventAgenda);
        sliderLayout = (SliderLayout) findViewById(R.id.slider);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/planer_reg_webfont.ttf");
        txtNilai.setTypeface(face);
        mApiService         = Link.getAPIService();
        mUploadService         = Link.getImageServiceBerita();

        reloadDataRating();

        if(idKategori>2){
            linHarga.setVisibility(View.GONE);
        }else{
            linHarga.setVisibility(View.VISIBLE);
        }
        try{
            Date tanggalEvent=new SimpleDateFormat("yyyy-MM-dd").parse(tglEvent);
            txtTglEvent.setText(new SimpleDateFormat("dd-MM-yyyy").format(tanggalEvent));
        }catch (Exception ex){}

        if(idKategori==6){
            linTglEvent.setVisibility(View.VISIBLE);
        }else{
            linTglEvent.setVisibility(View.GONE);
        }
        txtJamBuka.setText(jamBuka.substring(0, jamBuka.length()-3));
        txtJamTutup.setText(jamTutup.substring(0, jamTutup.length()-3));
        String ket="";
        if(idKategori==1){
            ket= "Tiket Masuk: ";
        }else if(idKategori==2){
            ket= "Harga: ";
        }
        txtHarga.setText(ket+" Rp. "+ decim.format(harga));
        txtNamaTmpt.setText(namaTempat);
        txtJudul.setText(judul);
        txtIsi.setText(isi);
        txtAlamat.setText(alamat);
        txtKategori.setText(kategori);
        txtSubKategori.setText(subKategori);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frgInfoDataMaps);
        fm.getMapAsync(this);

        btnKoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login==null){
                    Toast.makeText(InfoData.this, "Harap login terlebih dahulu", Toast.LENGTH_LONG).show();
                }else{
                    Intent i  = new Intent(getWindow().getContext(), Komentar.class);
                    i.putExtra("idBerita", String.valueOf(idBerita));
                    i.putExtra("idUser", idUser);
                    i.putExtra("profile", profile);
                    i.putExtra("login", login);
                    startActivity(i);
                }
            }
        });

        imgAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login==null){
                    Toast.makeText(InfoData.this, "Harap login terlebih dahulu", Toast.LENGTH_LONG).show();
                }else{
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, 21);
                }
            }
        });

        imgGiveRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login==null){
                    Toast.makeText(InfoData.this, "Harap login terlebih dahulu", Toast.LENGTH_LONG).show();
                }else{
                    selectCountUser();
                }
            }
        });

        if(login == null){
            imgGiveRate.setVisibility(View.GONE);
        }else{
            imgGiveRate.setVisibility(View.VISIBLE);
        }
    }

    private void reloadDataRating(){
        if(rating>0){
            hasilRating = new BigDecimal(rating).divide(new BigDecimal(countUser),1, RoundingMode.HALF_UP);
        }
        if(hasilRating.compareTo(BigDecimal.ZERO) == 0){
            txtNilai.setText("0.0");
        }else{
            txtNilai.setText(String.valueOf(hasilRating));
        }
        txtCountUser.setText(String.valueOf(countUser));

        ratingDatabase.setClickable(false);
        ratingDatabase.setScrollable(false);
        ratingDatabase.setRating(hasilRating.floatValue());
    }

    private void getDataUpload(String Url){
        pDialog.setMessage("Please Wait, Load Image Data...");
        showDialog();
        JsonObjectRequest jsonget = new JsonObjectRequest(Request.Method.GET, Url, null,
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int sucses= response.getInt("success");
                            Log.i("Status", String.valueOf(sucses));
                            if (sucses==1){
                                JSONArray JsonArray = response.getJSONArray("uploade");
                                for (int i = 0; i < JsonArray.length(); i++) {
                                    JSONObject object = JsonArray.getJSONObject(i);
                                    DataBeritaDetailModel colums 	= new DataBeritaDetailModel();
                                    colums.setIdBerita(object.getInt("idBerita"));
                                    colums.setLine(object.getInt("line"));
                                    colums.setPathFoto(object.getString("pathGambar"));
                                    columnlist.add(colums);
                                    treeMap.put(i+1, object.getString("pathGambar")==null||object.getString("pathGambar").equals("null")
                                            ||object.getString("pathGambar").equals("")?"":Link.BASE_URL_IMAGE_BERITA+object.getString("pathGambar"));
                                }
                                loadGambar(treeMap);
                                hideDialog();
                            }else{
                                Toast.makeText(InfoData.this, "Tidak Ada Gambar yg disimpan", Toast.LENGTH_LONG).show();
                                hideDialog();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            hideDialog();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(InfoData.this, "Check Koneksi Internet Anda", Toast.LENGTH_LONG).show();
                    hideDialog();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(InfoData.this, "AuthFailureError", Toast.LENGTH_LONG).show();
                    hideDialog();
                } else if (error instanceof ServerError) {
                    Toast.makeText(InfoData.this, "Check ServerError", Toast.LENGTH_LONG).show();
                    hideDialog();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(InfoData.this, "Check NetworkError", Toast.LENGTH_LONG).show();
                    hideDialog();
                } else if (error instanceof ParseError) {
                    Toast.makeText(InfoData.this, "Check ParseError", Toast.LENGTH_LONG).show();
                    hideDialog();
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

    private void loadGambar(TreeMap<Integer, String> listTree){
        for(Integer name : listTree.keySet()){
            String gambar=listTree.get(name);
            TextSliderView textSliderView = new TextSliderView(InfoData.this);
            if(gambar.equals("")){
                textSliderView
                        .description("Gambar "+String.valueOf(name))
                        .image(R.drawable.no_image)
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);
            }else{
                textSliderView
                        .description("Gambar "+String.valueOf(name))
                        .image(gambar)
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);
            }

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra","Gambar "+String.valueOf(name));
            sliderLayout.addSlider(textSliderView);
            a++;
        }

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(6000);
        sliderLayout.addOnPageChangeListener(this);
    }

    @Override
    public void onPositiveButtonClicked(int rate, String comment) {
        requestRegister(idBerita, idUser, comment, rate);
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(latt, longt);
        mMap.addMarker(new MarkerOptions().position(latLng)
                .title(namaTempat)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }

    @Override
    protected void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    private void selectCountUser(){
        pDialog.setMessage("Please Wait, Load Data...");
        showDialog();
        mApiService.countUser(idBerita, idUser).
                enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            try{
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("value").equals("false")){
                                    Integer nilaiCount = jsonRESULTS.getJSONObject("user").getInt("nilai");
                                    if(nilaiCount.intValue()>0){
                                        Toast.makeText(InfoData.this, "Anda sudah berpartisipasi memberikan rating terhadap konten ini!", Toast.LENGTH_LONG).show();
                                        hideDialog();
                                    }else{//<=0
                                        hideDialog();
                                        showDialogRate();
                                    }
                                }else{
                                    String error_message = jsonRESULTS.getString("message");
                                    Toast.makeText(InfoData.this, error_message, Toast.LENGTH_LONG).show();
                                    hideDialog();
                                }
                            }catch (JSONException e) {
                                hideDialog();
                                e.printStackTrace();
                                Toast.makeText(InfoData.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                hideDialog();
                                e.printStackTrace();
                                Toast.makeText(InfoData.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }else{
                            hideDialog();
                            Toast.makeText(InfoData.this, "GAGAL LOAD DATA", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        hideDialog();
                        Toast.makeText(InfoData.this, "GAGAL LOAD DATA", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void showDialogRate() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNeutralButtonText("Later")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(3)
                .setTitle("Rate this Place")
                .setDescription("Please select some stars and give your feedback")
                //.setDefaultComment("This app is pretty cool !")
                .setStarColor(R.color.md_cyan_a400)
                .setNoteDescriptionTextColor(R.color.md_black)
                .setTitleTextColor(R.color.md_black)
                .setDescriptionTextColor(R.color.md_black)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.md_grey_500)
                .setCommentTextColor(R.color.md_white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .create(InfoData.this)
                .show();
    }

    private void requestRegister(final Integer idBeritaku, final String idUserku, final String isi,
                                 final Integer rate){
        pDialog.setMessage("Please Wait ...");
        showDialog();
        mApiService.saveRating(idBeritaku, idUserku, isi, rate, Utils.getDeviceName()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("value").equals("false")){
                            if (jsonRESULTS.getString("value").equals("false")){
                                rating = jsonRESULTS.getJSONObject("rate").getInt("total_nilai");
                                countUser = jsonRESULTS.getJSONObject("rate").getInt("user_count");
                                reloadDataRating();
                                Toast.makeText(InfoData.this, "Thank You for your Rate....", Toast.LENGTH_LONG).show();
                            } else {
                                String error_message = jsonRESULTS.getString("message");
                                Toast.makeText(InfoData.this, error_message, Toast.LENGTH_LONG).show();
                            }
                            hideDialog();
                        }
                    }catch (JSONException e) {
                        hideDialog();
                        Toast.makeText(InfoData.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }catch (IOException e) {
                        hideDialog();
                        Toast.makeText(InfoData.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }else{
                    hideDialog();
                    Toast.makeText(InfoData.this, "GAGAL", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideDialog();
                Toast.makeText(InfoData.this, "GAGAL", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 21 && resultCode == this.RESULT_OK && null != data) {//gmbar foto2
            sImage	= getNameImage().toString()+".jpg";
            uImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = this.getContentResolver().query(uImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            String fileNameSegments[] = picturePath.split("/");
            Bitmap myImg = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            myImg.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            encodedString = Base64.encodeToString(byte_arr, Base64.DEFAULT);
            uploadImage(idBerita, sImage, idUser, uImage);
        }
    }

    private void uploadImage(final Integer idBeritaku, final String pathGambarKu, final String idUserku,
                             final Uri uri) {
        pDialog.setMessage("Uploading Image to Server...");
        showDialog();
        File file = new File(getRealPathFromURI(uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), pathGambarKu);
        mUploadService.uploadImageData(requestFile, descBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("value").equals("false")){
                            proses_simpan(idBeritaku, pathGambarKu, idUserku);
                        }else{
                            hideDialog();
                            Toast.makeText(InfoData.this, jsonRESULTS.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    }catch (JSONException e) {
                        hideDialog();
                        e.printStackTrace();
                    } catch (IOException e) {
                        hideDialog();
                        e.printStackTrace();
                    }
                }else{
                    hideDialog();
                    Toast.makeText(InfoData.this, "Some error occurred...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideDialog();
                Toast.makeText(InfoData.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void proses_simpan(final Integer idBeritaku, final String pathGambarku, final String idUserku){
        pDialog.setMessage("Proses Simpan ...");
        showDialog();
        String device = Utils.getDeviceName();
        mApiService.saveBeritaDetail(idBeritaku,pathGambarku, idUserku, device).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("value").equals("false")){
                            Toast.makeText(InfoData.this, jsonRESULTS.getString("message"), Toast.LENGTH_LONG).show();
                            treeMap = new TreeMap<Integer,String>();
                            sliderLayout.removeAllSliders();
                            a=0;
                            getDataUpload(Link.BASE_URL_API+"getListGambar.php?idBerita="+String.valueOf(idBerita));
                        }else{
                            String error_message = jsonRESULTS.getString("message");
                            Toast.makeText(InfoData.this, error_message, Toast.LENGTH_LONG).show();
                            hideDialog();
                        }
                    }catch (JSONException e) {
                        hideDialog();
                        Toast.makeText(InfoData.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }catch (IOException e) {
                        hideDialog();
                        Toast.makeText(InfoData.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(InfoData.this, "Gagal Simpan Data", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(InfoData.this, "Gagal Simpan Data", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*treeMap = new TreeMap<Integer,String>();
        sliderLayout.stopAutoCycle();
        a=0;
        getDataUpload(Link.BASE_URL_API+"getListGambar.php?idBerita="+String.valueOf(idBerita));*/
    }

    private String getNameImage() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd-HHmmss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }
}