package com.example.project.wisataapp.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.project.wisataapp.R;

public class FrgHome extends Fragment {

    private View vupload;
    private ImageView imgWisata, imgHotel, imgKuliner, imgSuvenir, imgPelayanan, imgAgenda;
    private String jenisData, login, idUser, profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle	 = this.getArguments();
        if (bundle!=null){
            jenisData	= bundle.getString("data");
            login = bundle.getString("login");
            idUser = bundle.getString("idUser");
            profile = bundle.getString("profile");
        }
        vupload     = inflater.inflate(R.layout.home,container,false);
        imgWisata = (ImageView)vupload.findViewById(R.id.imgHomeWisata);
        imgHotel = (ImageView)vupload.findViewById(R.id.imgHomePenginapan);
        imgKuliner = (ImageView)vupload.findViewById(R.id.imgHomeKuliner);
        imgSuvenir = (ImageView)vupload.findViewById(R.id.imgHomeSouvenir);
        imgPelayanan = (ImageView)vupload.findViewById(R.id.imgHomePelayanan);
        imgAgenda = (ImageView)vupload.findViewById(R.id.imgHomeAgenda);

        imgWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrgData fragment = new FrgData(); // replace your custom fragment class
                Bundle extras = new Bundle();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                extras.putString("data", "1");
                extras.putString("subKategori", "0");
                extras.putString("namaSearch", "0");
                extras.putString("search", "N");//NO
                extras.putString("login", login);
                extras.putString("idUser", idUser);
                extras.putString("profile", profile);
                fragment.setArguments(extras);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.FrmMainMenu,fragment);
                fragmentTransaction.commit();
            }
        });

        imgHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrgData fragment = new FrgData(); // replace your custom fragment class
                Bundle extras = new Bundle();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                extras.putString("data", "2");
                extras.putString("subKategori", "0");
                extras.putString("namaSearch", "0");
                extras.putString("search", "N");//NO
                extras.putString("login", login);
                extras.putString("idUser", idUser);
                extras.putString("profile", profile);
                fragment.setArguments(extras);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.FrmMainMenu,fragment);
                fragmentTransaction.commit();
            }
        });

        imgKuliner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrgData fragment = new FrgData(); // replace your custom fragment class
                Bundle extras = new Bundle();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                extras.putString("data", "3");
                extras.putString("subKategori", "0");
                extras.putString("namaSearch", "0");
                extras.putString("search", "N");//NO
                extras.putString("login", login);
                extras.putString("idUser", idUser);
                extras.putString("profile", profile);
                fragment.setArguments(extras);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.FrmMainMenu,fragment);
                fragmentTransaction.commit();
            }
        });

        imgSuvenir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrgData fragment = new FrgData(); // replace your custom fragment class
                Bundle extras = new Bundle();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                extras.putString("data", "4");
                extras.putString("subKategori", "0");
                extras.putString("namaSearch", "0");
                extras.putString("search", "N");//NO
                extras.putString("login", login);
                extras.putString("idUser", idUser);
                extras.putString("profile", profile);
                fragment.setArguments(extras);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.FrmMainMenu,fragment);
                fragmentTransaction.commit();
            }
        });

        imgPelayanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrgData fragment = new FrgData(); // replace your custom fragment class
                Bundle extras = new Bundle();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                extras.putString("data", "5");
                extras.putString("subKategori", "0");
                extras.putString("namaSearch", "0");
                extras.putString("search", "N");//NO
                extras.putString("login", login);
                extras.putString("idUser", idUser);
                extras.putString("profile", profile);
                fragment.setArguments(extras);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.FrmMainMenu,fragment);
                fragmentTransaction.commit();
            }
        });

        imgAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrgData fragment = new FrgData(); // replace your custom fragment class
                Bundle extras = new Bundle();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                extras.putString("data", "6");
                extras.putString("subKategori", "0");
                extras.putString("namaSearch", "0");
                extras.putString("search", "N");//NO
                extras.putString("login", login);
                extras.putString("idUser", idUser);
                extras.putString("profile", profile);
                fragment.setArguments(extras);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.FrmMainMenu,fragment);
                fragmentTransaction.commit();
            }
        });

        return vupload;
    }
}
