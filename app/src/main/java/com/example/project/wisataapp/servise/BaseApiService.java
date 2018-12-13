package com.example.project.wisataapp.servise;

import com.example.project.wisataapp.utilities.JSONResponse;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


/**
 * Created by christian on 14/02/18.
 */

public interface BaseApiService {

    @FormUrlEncoded
    @POST("login.php")
    Call<ResponseBody> loginRequest(@Field("id") String id,
                                    @Field("pasw") String pasw);

    @FormUrlEncoded
    @POST("register.php")
    Call<ResponseBody> registerRequest(@Field("nama") String nama,
                                       @Field("id") String id,
                                       @Field("foto") String foto,
                                       @Field("device") String deviceName,
                                       @Field("pasw") String pasw,
                                       @Field("phone") String phone,
                                       @Field("gender") String gender);

    @FormUrlEncoded
    @POST("addInfo2.php")
    Call<ResponseBody> saveInfo2(@Field("idKategori") Integer kategori,
                                @Field("judul") String judul,
                                @Field("isi") String isi,
                                @Field("gambar1") String gambar1,
                                @Field("gambar2") String gambar2,
                                @Field("gambar3") String gambar3,
                                @Field("gambar4") String gambar4,
                                @Field("alamat") String alamat,
                                @Field("latt") double latt,
                                @Field("longt") double longt,
                                @Field("user") String user,
                                @Field("device") String device,
                                @Field("status") String status,// S=SAVE, U=UPDATE
                                @Field("aktif") String aktif,// A/D(c_stat)
                                @Field("tglNow") String tglNow,// khusus utk update
                                @Field("idBerita") Integer idBerita,//khusus utk update
                                @Field("namaTempat") String namaTempat,
                                @Field("jam_buka") String jamBuka,
                                @Field("jam_tutup") String jamTutup,
                                @Field("harga") double harga,
                                @Field("tglEvent") String tglEvent,
                                @Field("idSubKategori") Integer subKategori
    );

    @FormUrlEncoded
    @POST("addDetail.php")
    Call<ResponseBody> saveBeritaDetail(@Field("idBerita") Integer idBerita,
                                        @Field("gambar") String gambar,
                                        @Field("idUser") String userId,
                                        @Field("device") String device
    );

    @FormUrlEncoded
    @POST("dialogRate.php")
    Call<ResponseBody> saveRating(@Field("idBerita") Integer idBerita,
                                @Field("idUser") String idUser,
                                @Field("isi") String isi,
                                @Field("rate") double rate,
                                @Field("device") String device
    );

    @FormUrlEncoded
    @POST("countUserRate.php")
    Call<ResponseBody> countUser(@Field("idBerita") Integer idBerita,
                                  @Field("idUser") String idUser
    );

    @Multipart
    @POST("upload.php?apicall=upload")
    Call<ResponseBody> uploadImage(@Part("image\"; filename=\"myfile.jpg\" ") RequestBody file,
                                   @Part("desc") RequestBody desc);

    @Multipart
    @POST("uploadImg.php?apicall=upload")
    Call<ResponseBody> uploadImageData(@Part("image\"; filename=\"myfile.jpg\" ") RequestBody file,
                                   @Part("desc") RequestBody desc);

    @GET("listKategori.php")
    Call<JSONResponse> getKategori();

    @FormUrlEncoded
    @POST("listSubKategori.php")
    Call<JSONResponse> getSubKategori(@Field("kodeKategori") String kodeKategori);
}