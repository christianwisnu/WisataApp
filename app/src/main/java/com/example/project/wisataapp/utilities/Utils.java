package com.example.project.wisataapp.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.wisataapp.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by christian on 09/02/18.
 */

public class Utils {

    private static final ImageLoader imgloader = ImageLoader.getInstance();

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    public static void getImage(String url, ImageView img, Context context){
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.img_person)
                .showImageForEmptyUri(R.drawable.img_person)
                .showImageOnFail(R.drawable.img_person)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .build();
        imgloader.init(ImageLoaderConfiguration.createDefault(context));
        imgloader.displayImage(url, img, options);
        return;
    }

    public static void GetCycleImage(String url,ImageView img,Context context){
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer((int) 50.5f))
                .showImageOnLoading(R.mipmap.ic_action_picture)
                .showImageForEmptyUri(R.mipmap.ic_action_picture)
                .showImageOnFail(R.mipmap.ic_action_picture)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .build();
        imgloader.init(ImageLoaderConfiguration.createDefault(context));
        imgloader.displayImage(url, img, options);
        return;
    }

    public static byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
        int buffSize = 1024;
        byte[] buff = new byte[buffSize];
        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }
        return byteBuff.toByteArray();
    }

    public static void CurrentTime(String date,TextView txtTime){
        long now 			= System.currentTimeMillis();
        long longTimeAgo	= timeStringtoMilis(date);
        PrettyTime prettyTime = new PrettyTime();
        txtTime.setText(prettyTime.format(new Date(longTimeAgo)));
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String getHari(String tanggal){
        String[] a = tanggal.split(" ");
        String[] b = a[0].split("-");
        String year = b[0];
        String month = b[1];
        String date = b[2];
        SimpleDateFormat sdf= new SimpleDateFormat("EEEE");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.valueOf(year));
        cal.set(Calendar.MONTH, Integer.valueOf(month)-1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date));
        String hari="";
        if(sdf.format(cal.getTime()).equals("Sunday")){
            hari="Minggu";
        }else if(sdf.format(cal.getTime()).equals("Monday")){
            hari="Senin";
        }else if(sdf.format(cal.getTime()).equals("Tuesday")){
            hari="Selasa";
        }else if(sdf.format(cal.getTime()).equals("Wednesday")){
            hari="Rabu";
        }else if(sdf.format(cal.getTime()).equals("Thursday")){
            hari="Kamis";
        }else if(sdf.format(cal.getTime()).equals("Friday")){
            hari="Jumat";
        }else if(sdf.format(cal.getTime()).equals("Saturday")){
            hari="Sabtu";
        }else{
            hari=sdf.format(cal.getTime());
        }
        return hari;
    }
//======================================================================
    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    private static long timeStringtoMilis(String time) {
        long milis = 0;
        try {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date 	= sd.parse(time);
            milis 		= date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return milis;
    }
}

