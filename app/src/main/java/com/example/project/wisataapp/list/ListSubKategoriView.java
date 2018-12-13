package com.example.project.wisataapp.list;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.project.wisataapp.R;
import com.example.project.wisataapp.adapter.AdpSubKategori;
import com.example.project.wisataapp.model.ListSubKategoriModel;
import com.example.project.wisataapp.servise.BaseApiService;
import com.example.project.wisataapp.utilities.JSONResponse;
import com.example.project.wisataapp.utilities.Link;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListSubKategoriView extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<ListSubKategoriModel> mArrayList;
    private ArrayList<ListSubKategoriModel> mFilteredList;
    private AdpSubKategori mAdapter;
    private ProgressDialog progress;
    private BaseApiService mApiService;
    private String kodeKategori;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_sub_kategori);
        Bundle i = getIntent().getExtras();
        if (i != null){
            try {
                kodeKategori = i.getString("kodeKategori");
            } catch (Exception e) {}
        }
        mApiService         = Link.getAPIService();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarSubKategori);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List Sub Kategori");
        progress = new ProgressDialog(this);
        progress.setCancelable(false);
        progress.setMessage("Loading ...");
        progress.show();
        initViews();
        loadJSON();
    }

    private void initViews(){
        mRecyclerView = (RecyclerView)findViewById(R.id.card_recycler_view_sub_kategori);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    private void loadJSON(){
        mApiService.getSubKategori(kodeKategori).enqueue(new Callback<JSONResponse>() {
            @Override
            public void onResponse(Call<JSONResponse> call, Response<JSONResponse> response) {
                JSONResponse jsonResponse = response.body();
                mArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getSubKategori()));
                mAdapter = new AdpSubKategori(ListSubKategoriView
                        .this, mArrayList);
                //assert mRecyclerView != null;
                mRecyclerView.setAdapter(mAdapter);
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<JSONResponse> call, Throwable t) {
                t.printStackTrace();
                progress.dismiss();
                Toast.makeText(ListSubKategoriView.this, "Jaringan Error!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }
}
