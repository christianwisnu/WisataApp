package com.example.project.wisataapp.transaksi;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.project.wisataapp.R;
import com.example.project.wisataapp.list.ListKategoriView;
import com.example.project.wisataapp.list.ListSubKategoriView;
import com.example.project.wisataapp.servise.BaseApiService;
import com.example.project.wisataapp.utilities.CircleTransform;
import com.example.project.wisataapp.utilities.Link;
import com.example.project.wisataapp.utilities.PrefUtil;
import com.example.project.wisataapp.utilities.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahInfo extends AppCompatActivity {

    private LocationManager locationManager ;
    private boolean GpsStatus, alamatManual=false ;
    private double latitude, longtitude;
    private PrefUtil prefUtil;
    private ProgressDialog pDialog;
    private BaseApiService mApiService, mUploadService;
    private int RESULT_MAP = 5;
    private int RESULT_KATEGORI = 7;
    private int RESULT_SUB_KATEGORI = 8;
    private String alamat, sImage1=null, sImage2=null, sImage3=null, sImage4=null, encodedString, kodeKategori, namaKategori,
            idUser, infoStatus, judulHeader, ganti1="N", ganti2="N", ganti3="N", ganti4="N", hasilTglEvent, kodeSubKategori,
            namaSubKategori;
    private SharedPreferences shared;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Uri uImage1, uImage2, uImage3, uImage4;
    private Integer idBerita;
    private String id;
    private boolean alamatMaps=false;
    private Calendar dateAndTime = Calendar.getInstance();
    private SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
    private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    private Date tglEvent;
    private DecimalFormat decim = new DecimalFormat("####");

    @BindView(R.id.img_info_back)ImageView imgBack;
    @BindView(R.id.input_layout_info_namatempat)TextInputLayout tilNamaTempat;
    @BindView(R.id.eInfoNamatempat)EditText edNamaTempat;
    @BindView(R.id.input_layout_info_judul)TextInputLayout tilJudul;
    @BindView(R.id.eInfoJudul)EditText edJudul;
    @BindView(R.id.input_layout_info_isi)TextInputLayout tilIsi;
    @BindView(R.id.eInfoIsi)EditText edIsi;
    @BindView(R.id.input_layout_info_kategori)TextInputLayout tilKategori;
    @BindView(R.id.eInfoKategori)EditText edKategori;
    @BindView(R.id.txtInfoPilihKategori)TextView txtPilihKategori;
    @BindView(R.id.input_layout_info_sub_kategori)TextInputLayout tilSubKategori;
    @BindView(R.id.eInfoSubKategori)EditText edSubKategori;
    @BindView(R.id.txtInfoPilihSubKategori)TextView txtPilihSubKategori;
    @BindView(R.id.imgInfoGbr1)ImageView imgGambar1;
    @BindView(R.id.imgInfoGbr2)ImageView imgGambar2;
    @BindView(R.id.imgInfoGbr3)ImageView imgGambar3;
    @BindView(R.id.imgInfoGbr4)ImageView imgGambar4;
    @BindView(R.id.input_layout_info_alamat)TextInputLayout tilAlamat;
    @BindView(R.id.eInfoAlamat)EditText edAlamat;
    @BindView(R.id.bInfoAlamatMap)Button btnMap;
    @BindView(R.id.bInfoSave)Button btnSave;
    @BindView(R.id.txtJudulInfo)TextView txtJudulHeader;
    @BindView(R.id.ckAlamatManual)CheckBox ckAlamatManual;
    @BindView(R.id.linearGambar)LinearLayout linGambar;
    @BindView(R.id.linHarga)LinearLayout linHarga;
    @BindView(R.id.linJamBuka)LinearLayout linJamBuka;
    @BindView(R.id.linJamTutup)LinearLayout linJamTutup;
    @BindView(R.id.linTglEvent)RelativeLayout linTglEvent;
    @BindView(R.id.imgInfoTglEvent)ImageView imgKalendar;
    @BindView(R.id.input_layout_info_tglevent)TextInputLayout tilTglEvent;
    @BindView(R.id.eInfoTglEvent)EditText edTglEvent;
    @BindView(R.id.input_layout_info_jambuka)TextInputLayout tilJamBuka;
    @BindView(R.id.eInfoJamBuka)EditText edJamBuka;
    @BindView(R.id.input_layout_info_jamtutup)TextInputLayout tilJamTutup;
    @BindView(R.id.eInfoJamTutup)EditText edJamTutup;
    @BindView(R.id.input_layout_info_harga)TextInputLayout tilHarga;
    @BindView(R.id.eInfoHarga)EditText edHarga;
    @BindView(R.id.imgInfoJamBuka)ImageView imgJamBuka;
    @BindView(R.id.imgInfoJamTutup)ImageView imgJamTutup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);
        ButterKnife.bind(this);
        Intent i = getIntent();
        Calendar cal = Calendar.getInstance();
        infoStatus = i.getStringExtra("status");
        idBerita = Integer.valueOf(i.getStringExtra("idBerita"));

        mApiService         = Link.getAPIService();
        mUploadService      = Link.getImageServiceBerita();
        prefUtil            = new PrefUtil(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        try{
            shared  = prefUtil.getUserInfo();
            idUser      = shared.getString(PrefUtil.ID, null);
        }catch (Exception e){e.getMessage();}

        txtJudulHeader.setText("Tambah Data");
        edHarga.setText("0");
        if(!infoStatus.equals("ADD")){
            linGambar.setVisibility(View.GONE);
            if(i.getStringExtra("idKategori").equals("1")){
                judulHeader="Info Wisata";
                linHarga.setVisibility(View.VISIBLE);
                linTglEvent.setVisibility(View.GONE);
                edHarga.setText(decim.format(i.getDoubleExtra("harga",0)));
                hasilTglEvent=i.getStringExtra("tglEvent");
            }else if(i.getStringExtra("idKategori").equals("2")){
                judulHeader="Info Penginapan";
                linHarga.setVisibility(View.VISIBLE);
                linTglEvent.setVisibility(View.GONE);
                edHarga.setText(decim.format(i.getDoubleExtra("harga",0)));
                hasilTglEvent=i.getStringExtra("tglEvent");
            }else if(i.getStringExtra("idKategori").equals("3")){
                judulHeader="Info Kuliner";
                linHarga.setVisibility(View.GONE);
                linTglEvent.setVisibility(View.GONE);
                edHarga.setText("0");
                hasilTglEvent=i.getStringExtra("tglEvent");
            }else if(i.getStringExtra("idKategori").equals("4")){
                judulHeader="Info Souvenir";
                linHarga.setVisibility(View.GONE);
                linTglEvent.setVisibility(View.GONE);
                edHarga.setText("0");
                hasilTglEvent=i.getStringExtra("tglEvent");
            }else if(i.getStringExtra("idKategori").equals("5")){
                judulHeader="Info Pelayanan";
                linHarga.setVisibility(View.GONE);
                linTglEvent.setVisibility(View.GONE);
                edHarga.setText("0");
                hasilTglEvent=i.getStringExtra("tglEvent");
            }else if(i.getStringExtra("idKategori").equals("6")){
                judulHeader="Info Agenda Kota";
                linHarga.setVisibility(View.GONE);
                linTglEvent.setVisibility(View.VISIBLE);
                edHarga.setText("0");
                hasilTglEvent=i.getStringExtra("tglEvent");
            }
            txtJudulHeader.setText(judulHeader);
            idBerita=Integer.valueOf(i.getStringExtra("idBerita"));
            edNamaTempat.setText(i.getStringExtra("namaTempat"));
            edJudul.setText(i.getStringExtra("judul"));
            edIsi.setText(i.getStringExtra("isi"));
            kodeKategori=i.getStringExtra("idKategori");
            edKategori.setText(i.getStringExtra("kategori"));
            kodeSubKategori=i.getStringExtra("idSubKategori");
            edSubKategori.setText(i.getStringExtra("subKategori"));
            edAlamat.setText(i.getStringExtra("alamat"));
            latitude=i.getDoubleExtra("latt",0);
            longtitude=i.getDoubleExtra("longt",0);
            sImage1=i.getStringExtra("gambar");
            edJamBuka.setText(i.getStringExtra("jamBuka"));
            edJamTutup.setText(i.getStringExtra("jamTutup"));
            edTglEvent.setText(i.getStringExtra("tglEvent"));
            if(infoStatus.equals("EDIT")){
                btnSave.setText("SIMPAN");
                headerState(true);
                alamatMaps=true;
            }else{
                btnSave.setText("DELETE");
                headerState(false);
                alamatMaps=true;
            }
        }else{
            hasilTglEvent=sdf2.format(cal.getTime());
            linGambar.setVisibility(View.VISIBLE);
            headerState(true);
            btnSave.setText("SIMPAN");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_MAP) {//map
            if(resultCode == RESULT_OK) {//map
                alamat = data.getStringExtra("alamat");
                latitude = data.getDoubleExtra("latitude",0);
                longtitude = data.getDoubleExtra("longtitude",0);
                edAlamat.setText(alamat);
                alamatMaps=true;
            }else{
                alamatMaps=false;
            }
        }else if(requestCode == RESULT_KATEGORI){
            if(resultCode == RESULT_OK) {
                kodeKategori = data.getStringExtra("kodeKategori");
                namaKategori = data.getStringExtra("namaKategori");
                edKategori.setText(namaKategori);
                txtPilihSubKategori.setVisibility(View.VISIBLE);
                edSubKategori.setText("");
                if(kodeKategori.equals("1") || kodeKategori.equals("2")){
                    linHarga.setVisibility(View.VISIBLE);
                }else{
                    linHarga.setVisibility(View.GONE);
                    edHarga.setText("0");
                }
                if(kodeKategori.equals("6")){
                    linTglEvent.setVisibility(View.VISIBLE);
                }else{
                    linTglEvent.setVisibility(View.GONE);
                }
            }
        }else if(requestCode == RESULT_SUB_KATEGORI){
            if(resultCode == RESULT_OK) {
                kodeSubKategori = data.getStringExtra("kodeSubKategori");
                namaSubKategori = data.getStringExtra("namaSubKategori");
                edSubKategori.setText(namaSubKategori);
            }
        }else if (requestCode == 21 && resultCode == this.RESULT_OK && null != data) {//gmbar foto2
            sImage1	= getNameImage().toString()+".jpg";
            uImage1 = data.getData();
            ganti1="Y";
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = this.getContentResolver().query(uImage1, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Utils.GetCycleImage("file:///"+picturePath, imgGambar1, this);
            String fileNameSegments[] = picturePath.split("/");
            Bitmap myImg = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            myImg.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            encodedString = Base64.encodeToString(byte_arr, Base64.DEFAULT);
            //uploadingImage(Link.FileUpload, simage2);
        }else if (requestCode == 22 && resultCode == this.RESULT_OK && null != data) {//gmbar foto3
            sImage2	= getNameImage().toString()+".jpg";
            uImage2 = data.getData();
            ganti2="Y";
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = this.getContentResolver().query(uImage2, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Utils.GetCycleImage("file:///"+picturePath, imgGambar2, this);
            String fileNameSegments[] = picturePath.split("/");
            Bitmap myImg = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            myImg.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            encodedString = Base64.encodeToString(byte_arr, Base64.DEFAULT);
            //uploadingImage(Link.FileUpload, simage3);
        }else if (requestCode == 23 && resultCode == this.RESULT_OK && null != data) {//gmbar foto4
            sImage3	= getNameImage().toString()+".jpg";
            uImage3 = data.getData();
            ganti3="Y";
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = this.getContentResolver().query(uImage3, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Utils.GetCycleImage("file:///"+picturePath, imgGambar3, this);
            String fileNameSegments[] = picturePath.split("/");
            Bitmap myImg = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            myImg.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            encodedString = Base64.encodeToString(byte_arr, Base64.DEFAULT);
            //uploadingImage(Link.FileUpload, simage4);
        }else if (requestCode == 24 && resultCode == this.RESULT_OK && null != data) {////gmbar foto5
            sImage4	= getNameImage().toString()+".jpg";
            uImage4 = data.getData();
            ganti4="Y";
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = this.getContentResolver().query(uImage4, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Utils.GetCycleImage("file:///"+picturePath, imgGambar4, this);
            String fileNameSegments[] = picturePath.split("/");
            Bitmap myImg = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            myImg.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            encodedString = Base64.encodeToString(byte_arr, Base64.DEFAULT);
            //uploadingImage(Link.FileUpload, simage5);
        }
    }

    private void headerState(boolean stat) {
        edNamaTempat.setEnabled(stat);
        edJudul.setEnabled(stat);
        edIsi.setEnabled(stat);
        ckAlamatManual.setEnabled(stat);
        if (stat){
            txtPilihKategori.setVisibility(View.VISIBLE);
            txtPilihSubKategori.setVisibility(View.VISIBLE);
            btnMap.setVisibility(View.VISIBLE);
        }else {
            txtPilihKategori.setVisibility(View.GONE);
            txtPilihSubKategori.setVisibility(View.GONE);
            btnMap.setVisibility(View.GONE);
        }
    }

    private void loadGambar(String namaImg, ImageView imgSrc){
        Glide.with(this).load(Link.BASE_URL_IMAGE_BERITA+namaImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgSrc);
    }

    private String getNameImage() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd-HHmmss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    @OnClick(R.id.imgInfoJamBuka)
    protected void jamBuka() {
        pilihJam(edJamBuka);
    }

    @OnClick(R.id.imgInfoJamTutup)
    protected void jamTutup() {
        pilihJam(edJamTutup);
    }

    @OnClick(R.id.imgInfoTglEvent)
    protected void pilihTglEvent() {
        new DatePickerDialog(TambahInfo.this, dTo, dateAndTime.get(Calendar.YEAR),dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener dTo =new DatePickerDialog.OnDateSetListener(){

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, month);
            dateAndTime.set(Calendar.DAY_OF_MONTH, day);
            updatelabel();
        }
    };

    private void updatelabel(){
        edTglEvent.setText(sdf1.format(dateAndTime.getTime()));
        tglEvent=dateAndTime.getTime();
        hasilTglEvent=sdf2.format(dateAndTime.getTime());
    }

    private void pilihJam(final EditText edittext) {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(TambahInfo.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String sHrs, sMins;
                if (selectedMinute < 10) {
                    sMins = "0" + selectedMinute;
                } else {
                    sMins = String.valueOf(selectedMinute);
                }
                if (selectedHour < 10) {
                    sHrs = "0" + selectedHour;
                } else {
                    sHrs = String.valueOf(selectedHour);
                }
                setSelectedTime(sHrs, sMins, edittext);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Pilih Jam");
        mTimePicker.show();
    }

    public void setSelectedTime(String hourOfDay, String minute, EditText edText) {
        edText.setText(hourOfDay + ":" + minute);
    }

    @OnClick(R.id.imgInfoGbr1)
    protected void pilihFoto1() {
        if(!infoStatus.equals("DEL")){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, 21);
        }
    }

    @OnClick(R.id.imgInfoGbr2)
    protected void pilihFoto2() {
        if(!infoStatus.equals("DEL")){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, 22);
        }
    }

    @OnClick(R.id.imgInfoGbr3)
    protected void pilihFoto3() {
        if(!infoStatus.equals("DEL")){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, 23);
        }
    }

    @OnClick(R.id.imgInfoGbr4)
    protected void pilihFoto4() {
        if(!infoStatus.equals("DEL")){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, 24);
        }
    }

    @OnClick(R.id.ckAlamatManual)
    protected void alamatManual() {
        alamatManual=!alamatManual;
        edAlamat.setEnabled(alamatManual);
    }

    @OnClick(R.id.txtInfoPilihKategori)
    protected void pilihKategori() {
        Intent i = new Intent(TambahInfo.this, ListKategoriView.class);
        startActivityForResult(i, RESULT_KATEGORI);
    }

    @OnClick(R.id.txtInfoPilihSubKategori)
    protected void pilihSubKategori() {
        Intent i = new Intent(TambahInfo.this, ListSubKategoriView.class);
        i.putExtra("kodeKategori",kodeKategori);
        startActivityForResult(i, RESULT_SUB_KATEGORI);
    }

    @OnClick(R.id.bInfoAlamatMap)
    protected void pilihMap() {
        CheckGpsStatus() ;
        if(GpsStatus == true){
            Intent i = new Intent(getApplication(), MapsActivity.class);
            startActivityForResult(i, RESULT_MAP);
        }else {
            new AlertDialog.Builder(this)
                    .setTitle("Perhatian")
                    .setMessage("GPS belum diaktifkan\nAktifkan dahulu!")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .create().show();
        }
    }

    @OnClick(R.id.bInfoSave)
    protected void simpan() {
        if(validateNamaTmpt() && validateIsi() && validateKategori() && validateSubKategori() &&
                validateAlamat() && validateJamBuka() && validateJamTutup() && validateMaps() ){
            if(kodeKategori.equals("1") || kodeKategori.equals("2")){
                if(validateHarga()){
                    String status;
                    String aktif;
                    if(infoStatus.equals("ADD")){
                        status="S";
                        aktif="A";
                    }else{
                        status="U";
                        if(infoStatus.equals("DEL")){
                            aktif="D";
                        }else{
                            aktif="A";
                        }
                    }
                    if(infoStatus.equals("ADD")){
                        if(ganti1.equals("Y")){
                            uploadImage1(status, aktif, idBerita, infoStatus);
                        }else{
                            Toast.makeText(TambahInfo.this, "Pilih Gambar Minimum 1 gambar!", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        if(!infoStatus.equals("ADD")){
                            proses_simpan("", edIsi.getText().toString(), Integer.valueOf(kodeKategori), sImage1, sImage2, sImage3,
                                    sImage4, latitude, longtitude, edAlamat.getText().toString(), status, aktif, idBerita,
                                    edNamaTempat.getText().toString(), edJamBuka.getText().toString(), edJamTutup.getText().toString(),
                                    Double.parseDouble(edHarga.getText().toString()), hasilTglEvent, Integer.valueOf(kodeSubKategori));
                        }
                    }
                }
            }else{
                String status;
                String aktif;
                if(infoStatus.equals("ADD")){
                    status="S";
                    aktif="A";
                }else{
                    status="U";
                    if(infoStatus.equals("DEL")){
                        aktif="D";
                    }else{
                        aktif="A";
                    }
                }
                if(infoStatus.equals("ADD")){
                    if(ganti1.equals("Y")){
                        uploadImage1(status, aktif, idBerita, infoStatus);
                    }else{
                        Toast.makeText(TambahInfo.this, "Pilih Gambar Minimum 1 gambar!", Toast.LENGTH_LONG).show();
                    }
                }else{
                    if(!infoStatus.equals("ADD")){
                        proses_simpan("", edIsi.getText().toString(), Integer.valueOf(kodeKategori), sImage1, sImage2, sImage3,
                                sImage4, latitude, longtitude, edAlamat.getText().toString(), status, aktif, idBerita,
                                edNamaTempat.getText().toString(), edJamBuka.getText().toString(), edJamTutup.getText().toString(),
                                Double.parseDouble(edHarga.getText().toString()), hasilTglEvent, Integer.valueOf(kodeSubKategori));
                    }
                }
            }
        }
    }

    @OnClick(R.id.img_info_back)
    protected void backk() {
        finish();
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

    private void uploadImage1(final String status, final String aktif, final Integer idBerita, final String infoStat) {
        pDialog.setMessage("Uploading Image 1 to Server...");
        showDialog();
        File file = new File(getRealPathFromURI(uImage1));
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(uImage1)), file);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), sImage1);
        mUploadService.uploadImageData(requestFile, descBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("value").equals("false")){
                            if(ganti2.equals("Y") ){
                                uploadImage2(status, aktif, idBerita, infoStat);
                            }else if(ganti3.equals("Y") ){
                                uploadImage3(status, aktif, idBerita, infoStat);
                            }else if(ganti4.equals("Y") ){
                                uploadImage4(status, aktif, idBerita, infoStat);
                            }else{
                                proses_simpan("", edIsi.getText().toString(), Integer.valueOf(kodeKategori), sImage1, sImage2, sImage3,
                                        sImage4, latitude, longtitude, edAlamat.getText().toString(), status, aktif, idBerita,
                                        edNamaTempat.getText().toString(), edJamBuka.getText().toString(), edJamTutup.getText().toString(),
                                        Double.parseDouble(edHarga.getText().toString()), hasilTglEvent, Integer.valueOf(kodeSubKategori));
                            }
                        }else{
                            hideDialog();
                            Toast.makeText(TambahInfo.this, jsonRESULTS.getString("message"), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(TambahInfo.this, "Some error occurred...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideDialog();
                Toast.makeText(TambahInfo.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadImage2(final String status, final String aktif, final Integer idBerita, final String infoStat) {
        pDialog.setMessage("Uploading Image 2 to Server...");
        showDialog();
        File file = new File(getRealPathFromURI(uImage2));
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(uImage2)), file);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), sImage2);
        mUploadService.uploadImageData(requestFile, descBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("value").equals("false")){
                            if(ganti3.equals("Y")){
                                uploadImage3(status, aktif, idBerita, infoStat);
                            }else if(ganti4.equals("Y") ){
                                uploadImage4(status, aktif, idBerita, infoStat);
                            }else{
                                proses_simpan("", edIsi.getText().toString(), Integer.valueOf(kodeKategori), sImage1, sImage2, sImage3,
                                        sImage4, latitude, longtitude, edAlamat.getText().toString(), status, aktif, idBerita,
                                        edNamaTempat.getText().toString(), edJamBuka.getText().toString(), edJamTutup.getText().toString(),
                                        Double.parseDouble(edHarga.getText().toString()), hasilTglEvent, Integer.valueOf(kodeSubKategori));
                            }
                        }else{
                            hideDialog();
                            Toast.makeText(TambahInfo.this, jsonRESULTS.getString("message"), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(TambahInfo.this, "Some error occurred...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideDialog();
                Toast.makeText(TambahInfo.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadImage3(final String status, final String aktif, final Integer idBerita, final String infoStat) {
        pDialog.setMessage("Uploading Image 3 to Server...");
        showDialog();
        File file = new File(getRealPathFromURI(uImage3));
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(uImage3)), file);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), sImage3);
        mUploadService.uploadImageData(requestFile, descBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("value").equals("false")){
                            if(ganti4.equals("Y") ){
                                uploadImage4(status, aktif, idBerita, infoStat);
                            }else{
                                proses_simpan("", edIsi.getText().toString(), Integer.valueOf(kodeKategori), sImage1, sImage2, sImage3,
                                        sImage4, latitude, longtitude, edAlamat.getText().toString(), status, aktif, idBerita,
                                        edNamaTempat.getText().toString(), edJamBuka.getText().toString(), edJamTutup.getText().toString(),
                                        Double.parseDouble(edHarga.getText().toString()), hasilTglEvent, Integer.valueOf(kodeSubKategori));
                            }
                        }else{
                            hideDialog();
                            Toast.makeText(TambahInfo.this, jsonRESULTS.getString("message"), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(TambahInfo.this, "Some error occurred...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideDialog();
                Toast.makeText(TambahInfo.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadImage4(final String status, final String aktif, final Integer idBerita, final String infoStat) {
        pDialog.setMessage("Uploading Image 4 to Server...");
        showDialog();
        File file = new File(getRealPathFromURI(uImage4));
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(uImage4)), file);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), sImage4);
        mUploadService.uploadImageData(requestFile, descBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("value").equals("false")){
                            proses_simpan("", edIsi.getText().toString(), Integer.valueOf(kodeKategori), sImage1, sImage2, sImage3,
                                    sImage4, latitude, longtitude, edAlamat.getText().toString(), status, aktif, idBerita,
                                    edNamaTempat.getText().toString(), edJamBuka.getText().toString(), edJamTutup.getText().toString(),
                                    Double.parseDouble(edHarga.getText().toString()), hasilTglEvent, Integer.valueOf(kodeSubKategori));
                        }else{
                            hideDialog();
                            Toast.makeText(TambahInfo.this, jsonRESULTS.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    }catch (JSONException e) {
                        hideDialog();
                        Toast.makeText(TambahInfo.this, e.getMessage()+" 4", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    } catch (IOException e) {
                        hideDialog();
                        Toast.makeText(TambahInfo.this, e.getMessage()+" 4", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }else{
                    hideDialog();
                    Toast.makeText(TambahInfo.this, "Some error occurred 4 ...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideDialog();
                Toast.makeText(TambahInfo.this, t.getMessage()+" 4", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void proses_simpan(final String judul, final String isi, final Integer kodeKategori, final String pathGbr1,
                                 final String pathGbr2, final String pathGbr3, final String pathGbr4, final double latt,
                                 final double longt, final String alamat, final String status, final String aktif,
                               final Integer idBerita, final String nmTempat, final String jamBukaku, final String jamTutupku,
                               final double hargaku, final String tglEventku, final Integer kodeSubKategori){
        pDialog.setMessage("Proses Simpan ...");
        showDialog();
        String device = Utils.getDeviceName();
        Date today = Calendar.getInstance().getTime();
        String tanggalNow =df.format(today);
        mApiService.saveInfo2(kodeKategori, judul, isi, pathGbr1, pathGbr2, pathGbr3, pathGbr4, alamat, latt, longt, idUser,
                device, status, aktif, tanggalNow,  idBerita, nmTempat, jamBukaku, jamTutupku, hargaku, tglEventku, kodeSubKategori)
                .enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("value").equals("false")){
                            if(infoStatus.equals("DEL")){
                                Toast.makeText(TambahInfo.this, "DATA SUDAH DIHAPUS", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(TambahInfo.this, jsonRESULTS.getString("message"), Toast.LENGTH_LONG).show();
                            }
                            finish();
                        }else{
                            String error_message = jsonRESULTS.getString("message");
                            Toast.makeText(TambahInfo.this, error_message, Toast.LENGTH_LONG).show();
                            hideDialog();
                        }
                    }catch (JSONException e) {
                        hideDialog();
                        Toast.makeText(TambahInfo.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }catch (IOException e) {
                        hideDialog();
                        Toast.makeText(TambahInfo.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(TambahInfo.this, "Gagal Simpan Data", Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(TambahInfo.this, "Gagal Simpan Data", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });
    }

    public void CheckGpsStatus(){
        locationManager = (LocationManager)TambahInfo.this.getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private boolean validateNamaTmpt() {
        boolean value;
        if (edNamaTempat.getText().toString().isEmpty()){
            value=false;
            requestFocus(edNamaTempat);
            tilNamaTempat.setError(getString(R.string.err_msg_namatempat));
        } else {
            value=true;
            tilNamaTempat.setError(null);
        }
        return value;
    }

    /*private boolean validateJudul() {
        boolean value;
        if (edJudul.getText().toString().isEmpty()){
            value=false;
            requestFocus(edJudul);
            tilJudul.setError(getString(R.string.err_msg_judul));
        } else {
            value=true;
            tilJudul.setError(null);
        }
        return value;
    }*/

    private boolean validateIsi() {
        boolean value;
        if (edIsi.getText().toString().isEmpty()){
            value=false;
            requestFocus(edIsi);
            tilIsi.setError(getString(R.string.err_msg_isi));
        } else {
            value=true;
            tilIsi.setError(null);
        }
        return value;
    }

    private boolean validateKategori() {
        boolean value;
        if (edKategori.getText().toString().isEmpty()){
            value=false;
            requestFocus(edKategori);
            tilKategori.setError(getString(R.string.err_msg_kategori));
        } else {
            value=true;
            tilKategori.setError(null);
        }
        return value;
    }

    private boolean validateSubKategori() {
        boolean value;
        if (edSubKategori.getText().toString().isEmpty()){
            value=false;
            requestFocus(edKategori);
            tilSubKategori.setError(getString(R.string.err_msg_sub_kategori));
        } else {
            value=true;
            tilSubKategori.setError(null);
        }
        return value;
    }

    private boolean validateAlamat() {
        boolean value;
        if (edAlamat.getText().toString().isEmpty()){
            value=false;
            requestFocus(edAlamat);
            tilAlamat.setError(getString(R.string.err_msg_alamat));
        } else {
            value=true;
            tilAlamat.setError(null);
        }
        return value;
    }

    private boolean validateJamBuka() {
        boolean value;
        if (edJamBuka.getText().toString().isEmpty()){
            value=false;
            requestFocus(edJamBuka);
            tilJamBuka.setError(getString(R.string.err_msg_jambuka));
        } else {
            value=true;
            tilJamBuka.setError(null);
        }
        return value;
    }

    private boolean validateJamTutup() {
        boolean value;
        if (edJamTutup.getText().toString().isEmpty()){
            value=false;
            requestFocus(edJamTutup);
            tilJamTutup.setError(getString(R.string.err_msg_jambuka));
        } else {
            value=true;
            tilJamTutup.setError(null);
        }
        return value;
    }

    private boolean validateHarga() {
        boolean value;
        if (edHarga.getText().toString().isEmpty()){
            value=false;
            requestFocus(edHarga);
            tilHarga.setError(getString(R.string.err_msg_harga));
        } else {
            value=true;
            tilHarga.setError(null);
        }
        return value;
    }//

    private boolean validateMaps() {
        boolean value;
        if (alamatMaps == false){
            value=false;
            Toast.makeText(TambahInfo.this,
                    "Koordinat lokasi map, belum ditentukan", Toast.LENGTH_LONG)
                    .show();
        } else {
            value=true;
        }
        return value;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
