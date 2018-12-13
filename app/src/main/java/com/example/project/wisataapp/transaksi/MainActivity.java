package com.example.project.wisataapp.transaksi;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appeaser.sublimenavigationviewlibrary.OnNavigationMenuEventListener;
import com.appeaser.sublimenavigationviewlibrary.SublimeBaseMenuItem;
import com.appeaser.sublimenavigationviewlibrary.SublimeMenu;
import com.appeaser.sublimenavigationviewlibrary.SublimeNavigationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.project.wisataapp.R;
import com.example.project.wisataapp.fragment.FrgData;
import com.example.project.wisataapp.fragment.FrgHome;
import com.example.project.wisataapp.fragment.FrgNearby;
import com.example.project.wisataapp.list.ListSubKategoriView;
import com.example.project.wisataapp.utilities.Anim;
import com.example.project.wisataapp.utilities.CircleTransform;
import com.example.project.wisataapp.utilities.ExceptionHandler;
import com.example.project.wisataapp.utilities.PrefUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.view.Gravity.START;

public class MainActivity extends AppCompatActivity implements com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    private PrefUtil pref;
    private SublimeNavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;

    private Toolbar toolbar;
    private SharedPreferences shared;
    private String id, nama, login, gender, profile, telp, kodeKategori, kodeSubKategori, namaSubKategori;
    private ImageView imgProfile, imgBg;
    private TextView txtNama;
    private boolean shouldLoadHomeFragOnBackPress = true;
    private static int navItemIndex = 0;
    private LinearLayout linFilter, lin1, lin2;
    private ImageView imgColExpKontak;
    private TextInputLayout tilSearch, tilSubKategori;
    private EditText edSearch, edSubKategori, edKategori;
    private TextView txtPilihSubKategori;
    private Button btnSearch;
    private LocationManager locationManager ;
    private boolean GpsStatus;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private double latitude, longtitude;

    private static final String TAG_HOME = "Home";
    private static final String TAG_BERANDA = "Wisata";//WISATA
    private static final String TAG_PENGINAPAN = "Penginapan";
    private static final String TAG_KULINER = "Kuliner";
    private static final String TAG_SOUVENIR = "Souvenir";
    private static final String TAG_PELAYANAN_UMUM = "Pelayanan Umum";
    private static final String TAG_AGENDA = "Agenda Kota";
    private static final String TAG_NEARBY = "Nearby";
    private static final String TAG_ABOUTUS = "About Us";
    private static final String TAG_SEJARAH = "Sejarah";
    private static final String TAG_INFO = "Tambah Info Baru";
    public static String CURRENT_TAG = TAG_BERANDA;
    private int RESULT_SUB_KATEGORI = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        pref = new PrefUtil(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        linFilter = (LinearLayout) findViewById(R.id.linSearchMain);
        lin1 = (LinearLayout) findViewById(R.id.linMainPertama);
        lin2 = (LinearLayout) findViewById(R.id.linMainKedua);
        txtPilihSubKategori = (TextView)findViewById(R.id.txtMainPilihSubKategori);
        edSubKategori = (EditText)findViewById(R.id.eMainSubKategori);
        edKategori = (EditText)findViewById(R.id.eMainKategori);
        edSearch = (EditText)findViewById(R.id.eMainSearch);
        imgColExpKontak = (ImageView) findViewById(R.id.imgExpandFilter);
        btnSearch = (Button)findViewById(R.id.btnMainSearch);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (SublimeNavigationView) findViewById(R.id.navigation_view);
        navHeader = navigationView.getHeaderView();
        imgProfile  = (ImageView) navHeader.findViewById(R.id.img_profile);
        imgBg       = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        txtNama     = (TextView) navHeader.findViewById(R.id.name);
        try{
            shared  = pref.getUserInfo();
            id      = shared.getString(PrefUtil.ID, null);
            nama    = shared.getString(PrefUtil.NAME, null);
            login   = shared.getString(PrefUtil.LOGIN, null);
            gender  = shared.getString(PrefUtil.GENDER, null);
            profile = shared.getString(PrefUtil.PROFILE, null);
            telp    = shared.getString(PrefUtil.TELP, null);
        }catch (Exception e){e.getMessage();}
        if(login==null){
            loadNavHeader(false);
            setUpNavigationView();
            menu();
        }else{
            loadNavHeader(true);
            setUpNavigationView();
            menuLogin(login);
        }
        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_BERANDA;
            loadHomeFragment();
        }
        hideFilter();
        kodeKategori="0";
        changeFragmentListUploadKriteria(new FrgHome(), kodeKategori, false);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragmentSearch(new FrgData(), kodeKategori, kodeSubKategori, edSearch.getText().toString());
            }
        });

        txtPilihSubKategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ListSubKategoriView.class);
                i.putExtra("kodeKategori",kodeKategori);
                startActivityForResult(i, RESULT_SUB_KATEGORI);
            }
        });
        buildGoogleApiClient();
    }

    @Override
    public void onConnected( Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed( ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        latitude = location.getLatitude();
        longtitude = location.getLongitude();

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void hideFilter(){
        linFilter.setVisibility(View.GONE);
        lin1.setVisibility(View.GONE);
        lin2.setVisibility(View.GONE);
    }

    private void showFilter(){
        linFilter.setVisibility(View.VISIBLE);
        lin1.setVisibility(View.VISIBLE);
        lin2.setVisibility(View.VISIBLE);
    }

    public void expcol_filter(View v) {
        if (linFilter.isShown()) {
            Anim.slide_up(this, linFilter);
            linFilter.setVisibility(View.GONE);
            imgColExpKontak.setBackgroundResource(R.drawable.snv_expand);
        } else {
            Anim.slide_down(this, linFilter);
            linFilter.setVisibility(View.VISIBLE);
            imgColExpKontak.setBackgroundResource(R.drawable.snv_collapse);
        }
    }

    private void loadNavHeader(boolean status) {
        if(status){
            txtNama.setText(nama);
            Glide.with(this).load(profile)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(this))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfile);
        }
        Glide.with(this).load(R.drawable.header)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgBg);
    }

    private void setUpNavigationView() {
        navigationView.setNavigationMenuEventListener(new OnNavigationMenuEventListener() {
            @Override
            public boolean onNavigationMenuEvent(Event event, SublimeBaseMenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        getSupportActionBar().setTitle(CURRENT_TAG);
                        drawer.closeDrawers();
                        kodeKategori="0";
                        edKategori.setText("");
                        changeFragmentListUploadKriteria(new FrgHome(), kodeKategori, false);
                        break;
                    case R.id.wisata:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_BERANDA;
                        getSupportActionBar().setTitle(CURRENT_TAG);
                        drawer.closeDrawers();
                        kodeKategori="1";
                        edKategori.setText(TAG_BERANDA);
                        changeFragmentListUploadKriteria(new FrgData(), kodeKategori, true);
                        break;
                    case R.id.penginapan:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PENGINAPAN;
                        getSupportActionBar().setTitle(CURRENT_TAG);
                        drawer.closeDrawers();
                        kodeKategori="2";
                        edKategori.setText(TAG_PENGINAPAN);
                        changeFragmentListUploadKriteria(new FrgData(), kodeKategori, true);
                        break;
                    case R.id.kuliner:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_KULINER;
                        getSupportActionBar().setTitle(CURRENT_TAG);
                        drawer.closeDrawers();
                        kodeKategori="3";
                        edKategori.setText(TAG_KULINER);
                        changeFragmentListUploadKriteria(new FrgData(), kodeKategori, true);
                        break;
                    case R.id.suvenir:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_SOUVENIR;
                        getSupportActionBar().setTitle(CURRENT_TAG);
                        drawer.closeDrawers();
                        kodeKategori="4";
                        edKategori.setText(TAG_SOUVENIR);
                        changeFragmentListUploadKriteria(new FrgData(), kodeKategori, true);
                        break;
                    case R.id.pelayanan:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_PELAYANAN_UMUM;
                        getSupportActionBar().setTitle(CURRENT_TAG);
                        drawer.closeDrawers();
                        kodeKategori="5";
                        edKategori.setText(TAG_PELAYANAN_UMUM);
                        changeFragmentListUploadKriteria(new FrgData(), kodeKategori, true);
                        break;
                    case R.id.agenda:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_AGENDA;
                        getSupportActionBar().setTitle(CURRENT_TAG);
                        drawer.closeDrawers();
                        kodeKategori="6";
                        edKategori.setText(TAG_AGENDA);
                        changeFragmentListUploadKriteria(new FrgData(), kodeKategori, true);
                        break;
                    case R.id.nearby:
                        CheckGpsStatus() ;
                        if(GpsStatus == true){
                            Double nol = new Double("0.0");
                            navItemIndex = 5;
                            CURRENT_TAG = TAG_NEARBY;
                            getSupportActionBar().setTitle(CURRENT_TAG);
                            drawer.closeDrawers();
                            edKategori.setText("");
                            if(Double.valueOf(latitude).compareTo(nol)==0 ||
                                    Double.valueOf(longtitude).compareTo(nol)==0){
                                Toast.makeText(MainActivity.this, "Latitude dan Longtitude lokasi anda belum diketahui!", Toast.LENGTH_SHORT).show();
                            }else{
                                changeFragmentNearby(new FrgNearby(), latitude, longtitude);
                            }
                        }else {
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Perhatian")
                                    .setMessage("GPS belum diaktifkan\nAktifkan dahulu!")
                                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .create().show();
                        }
                        break;
                    case R.id.tambahinfo:
                        Intent i = new Intent(getApplicationContext(), TambahInfo.class);
                        i.putExtra("status","ADD");
                        i.putExtra("idBerita","0");
                        startActivity(i);
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case R.id.daftar:
                        startActivity(new Intent(getApplicationContext(), Register.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                        return true;
                    case R.id.signin:
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                        return true;
                    case R.id.signout:
                        Toast.makeText(MainActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
                        pref.clear();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        finish();
                        return true;
                    case R.id.about_us://VISI MISI
                        startActivity(new Intent(getApplicationContext(), Visi.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        return true;
                    case R.id.sejarah:
                        startActivity(new Intent(getApplicationContext(), Sejarah.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    default:
                        navItemIndex = 0;
                }
                if (menuItem.isChecked()) {
                    menuItem.setChecked(true);
                } else {
                    menuItem.setChecked(false);
                }
                loadHomeFragment();
                return true;
            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    public void CheckGpsStatus(){
        locationManager = (LocationManager)MainActivity.this.getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void loadHomeFragment() {
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }
        /*Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }*/
        invalidateOptionsMenu();
    }

    private void menuLogin(String status){
        navigationView = (SublimeNavigationView) findViewById(R.id.navigation_view);
        SublimeMenu nav_Menu = navigationView.getMenu();
        nav_Menu.getMenuItem(R.id.home).setVisible(true);
        nav_Menu.getGroup(R.id.group_11).setVisible(true);
        nav_Menu.getMenuItem(R.id.wisata).setVisible(true);
        nav_Menu.getGroup(R.id.group_12).setVisible(true);
        nav_Menu.getMenuItem(R.id.penginapan).setVisible(true);
        nav_Menu.getGroup(R.id.group_13).setVisible(true);
        nav_Menu.getMenuItem(R.id.kuliner).setVisible(true);
        nav_Menu.getGroup(R.id.group_14).setVisible(true);
        nav_Menu.getMenuItem(R.id.suvenir).setVisible(true);
        nav_Menu.getGroup(R.id.group_15).setVisible(true);
        nav_Menu.getMenuItem(R.id.pelayanan).setVisible(true);
        nav_Menu.getGroup(R.id.group_16).setVisible(true);
        nav_Menu.getMenuItem(R.id.agenda).setVisible(true);
        if(status.equals("U")){
            nav_Menu.getMenuItem(R.id.tambahinfo).setVisible(false);
        }else{
            nav_Menu.getMenuItem(R.id.tambahinfo).setVisible(true);
            /*nav_Menu.getMenuItem(R.id.edit_agenda).setVisible(true);
            nav_Menu.getMenuItem(R.id.edit_wisata).setVisible(true);
            nav_Menu.getMenuItem(R.id.edit_pelayanan).setVisible(true);
            nav_Menu.getMenuItem(R.id.edit_penginapan).setVisible(true);
            nav_Menu.getMenuItem(R.id.edit_suvenir).setVisible(true);
            nav_Menu.getMenuItem(R.id.edit_kuliner).setVisible(true);*/
        }
        nav_Menu.getMenuItem(R.id.signout).setVisible(true);

        nav_Menu.getMenuItem(R.id.signin).setVisible(false);
        nav_Menu.getMenuItem(R.id.daftar).setVisible(false);

        nav_Menu.getMenuItem(R.id.separator_item_21).setVisible(true);

        nav_Menu.getGroup(R.id.group_21).setVisible(true);
        nav_Menu.getMenuItem(R.id.about_us).setVisible(true);
        nav_Menu.getMenuItem(R.id.sejarah).setVisible(true);
    }

    private void menu(){
        navigationView = (SublimeNavigationView) findViewById(R.id.navigation_view);
        SublimeMenu nav_Menu = navigationView.getMenu();
        nav_Menu.getMenuItem(R.id.home).setVisible(true);
        nav_Menu.getGroup(R.id.group_11).setVisible(true);
        nav_Menu.getMenuItem(R.id.wisata).setVisible(true);
        nav_Menu.getGroup(R.id.group_12).setVisible(true);
        nav_Menu.getMenuItem(R.id.penginapan).setVisible(true);
        nav_Menu.getGroup(R.id.group_13).setVisible(true);
        nav_Menu.getMenuItem(R.id.kuliner).setVisible(true);
        nav_Menu.getGroup(R.id.group_14).setVisible(true);
        nav_Menu.getMenuItem(R.id.suvenir).setVisible(true);
        nav_Menu.getGroup(R.id.group_15).setVisible(true);
        nav_Menu.getMenuItem(R.id.pelayanan).setVisible(true);
        nav_Menu.getGroup(R.id.group_16).setVisible(true);
        nav_Menu.getMenuItem(R.id.agenda).setVisible(true);
        nav_Menu.getMenuItem(R.id.tambahinfo).setVisible(false);

        nav_Menu.getMenuItem(R.id.signout).setVisible(false);

        nav_Menu.getMenuItem(R.id.signin).setVisible(true);
        nav_Menu.getMenuItem(R.id.daftar).setVisible(true);

        nav_Menu.getMenuItem(R.id.separator_item_21).setVisible(true);

        nav_Menu.getGroup(R.id.group_21).setVisible(true);
        nav_Menu.getMenuItem(R.id.about_us).setVisible(true);
        nav_Menu.getMenuItem(R.id.sejarah).setVisible(true);
    }

    private void changeFragmentListUploadKriteria(Fragment targetFragment, String kdKategori, boolean show){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FrmMainMenu, targetFragment)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setCustomAnimations(R.anim.blink, R.anim.fade_in)
                .commit();
        Bundle extras = new Bundle();
        extras.putString("data", kdKategori);
        extras.putString("subKategori", "0");
        extras.putString("namaSearch", "0");
        extras.putString("search", "N");//NO
        extras.putString("login", login);
        extras.putString("idUser", id);
        extras.putString("profile", profile);
        targetFragment.setArguments(extras);
        if(show == true){
            showFilter();
        }else{
            hideFilter();
        }
        drawer.closeDrawer(START);
    }

    private void changeFragmentNearby(Fragment targetFragment, double latt, double longt){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FrmMainMenu, targetFragment)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setCustomAnimations(R.anim.blink, R.anim.fade_in)
                .commit();
        Bundle extras = new Bundle();
        extras.putDouble("latt", latt);
        extras.putDouble("longt", longt);
        extras.putString("login", login);
        extras.putString("idUser", id);
        extras.putString("profile", profile);
        targetFragment.setArguments(extras);
        hideFilter();
        drawer.closeDrawer(START);
    }

    private void changeFragmentSearch(Fragment targetFragment, String kdKategori, String kdSubKategori,
                                      String namaSearch){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FrmMainMenu, targetFragment)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setCustomAnimations(R.anim.blink, R.anim.fade_in)
                .commit();
        Bundle extras = new Bundle();
        extras.putString("data", kdKategori);
        extras.putString("subKategori", kdSubKategori);
        extras.putString("namaSearch", namaSearch.trim().equals("") || namaSearch == null ? "%" :
                namaSearch.trim());
        extras.putString("search", "Y");//YES
        extras.putString("login", login);
        extras.putString("idUser", id);
        extras.putString("profile", profile);
        targetFragment.setArguments(extras);
        drawer.closeDrawer(START);
    }

    private void changeFragment(Fragment targetFragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FrmMainMenu, targetFragment)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .setCustomAnimations(R.anim.blink, R.anim.fade_in)
                .commit();
        drawer.closeDrawer(START);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    }).create().show();

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        if (shouldLoadHomeFragOnBackPress) {
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_BERANDA;
                loadHomeFragment();
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(login!=null){
            getMenuInflater().inflate(R.menu.keluar, menu);
        }else{
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            Toast.makeText(MainActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
            pref.clear();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
            return true;
        }
        if (id == R.id.action_login) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_SUB_KATEGORI){
            if(resultCode == RESULT_OK) {
                kodeSubKategori = data.getStringExtra("kodeSubKategori");
                namaSubKategori = data.getStringExtra("namaSubKategori");
                edSubKategori.setText(namaSubKategori);
            }
        }
    }
}
