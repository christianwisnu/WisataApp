package com.example.project.wisataapp.utilities;

import com.example.project.wisataapp.servise.BaseApiService;

/**
 * Created by christian on 14/02/18.
 */

public class Link {

    public static final String BASE_URL_API = "http://softchrist.com/wisataapp/php/";
    public static final String BASE_URL_IMAGE_PROFIL = "http://softchrist.com/wisataapp/image/profile/";
    public static final String BASE_URL_IMAGE_BERITA = "http://softchrist.com/wisataapp/image/img/";

    // Mendeklarasikan Interface BaseApiService
    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }

    public static BaseApiService getImageService(){
        return RetrofitClient.getClient(BASE_URL_IMAGE_PROFIL).create(BaseApiService.class);
    }

    public static BaseApiService getImageServiceBerita(){
        return RetrofitClient.getClient(BASE_URL_IMAGE_BERITA).create(BaseApiService.class);
    }
}
