package com.example.project.wisataapp.utilities;

import com.example.project.wisataapp.model.ListKategoriModel;
import com.example.project.wisataapp.model.ListSubKategoriModel;

/**
 * Created by Chris on 25/03/2018.
 */

public class JSONResponse {
    private ListKategoriModel[] kategori;
    public ListKategoriModel[] getKategori() {
        return kategori;
    }

    private ListSubKategoriModel[] subKategori;
    public ListSubKategoriModel[] getSubKategori() {
        return subKategori;
    }
}