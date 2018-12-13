package com.example.project.wisataapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.project.wisataapp.R;
import com.example.project.wisataapp.model.KomentarModel;
import com.example.project.wisataapp.utilities.AppController;
import com.example.project.wisataapp.utilities.CircleTransform;
import com.example.project.wisataapp.utilities.Link;
import com.example.project.wisataapp.utilities.Utils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdpKomentar extends ArrayAdapter<KomentarModel> {

    private List<KomentarModel> columnslist;
    private LayoutInflater vi;
    private int Resource;
    private ViewHolder holder;
    private Context context;
    private String statusku;
    private AlertDialog alert;
    private String FileDeteleted = "delete_komentar.php?id=";

    public AdpKomentar(Context context, int resource, List<KomentarModel> objects, String status) {
        super(context, resource,  objects);
        this.context = context;
        vi	=	(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource		= resource;
        columnslist		= objects;
        statusku = status;
    }

    @Override
    public View getView(final int position,  View convertView,  ViewGroup parent) {
        View v	=	convertView;
        if (v == null){
            holder	=	new ViewHolder();
            v= vi.inflate(Resource, null);
            holder.ImgUser				= 	 (ImageView)v.findViewById(R.id.ImgColCommentUser);
            holder.ImgDel				= 	 (ImageView)v.findViewById(R.id.ImgColKomentarDelete);
            holder.TvDate				=	 (TextView)v.findViewById(R.id.TvColCommentTime);
            holder.TvDescription		=	 (TextView)v.findViewById(R.id.TvColCommentMessage);
            v.setTag(holder);
        }else{
            holder 	= (ViewHolder)v.getTag();
        }
        String url 		= columnslist.get(position).getFoto();
        String userName = "<b>"+columnslist.get(position).getUserName()+"</b>";
        Utils.CurrentTime(columnslist.get(position).getTglKomentar(), holder.TvDate);
        if(columnslist.get(position).getCstat().equals("A")){
            holder.TvDescription.setText(Html.fromHtml(userName+" "+columnslist.get(position).getIsiComent()));
            if(statusku == null){
                holder.ImgDel.setVisibility(View.GONE);
            }else if(statusku.equals("U")){
                holder.ImgDel.setVisibility(View.GONE);
            }else {
                holder.ImgDel.setVisibility(View.VISIBLE);
            }
        }

        if (holder.ImgUser.getTag()==null||!holder.ImgUser.getTag().equals(url)){
            //Utils.GetCycleImage(url, holder.ImgUser, getContext());
            Glide.with(getContext()).load(url)
                    .crossFade()
                    .thumbnail(0.5f)
                    .bitmapTransform(new CircleTransform(getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ImgUser);
        }

        holder.ImgDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder msMaintance = new AlertDialog.Builder(getContext());
                msMaintance.setCancelable(false);
                msMaintance.setMessage("Yakin akan dihapus? ");
                msMaintance.setNegativeButton("Ya", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sidupload = String.valueOf(columnslist.get(position).getIdComent());
                        deletedData(Link.BASE_URL_API + FileDeteleted + sidupload, position);
                    }
                });

                msMaintance.setPositiveButton("Tidak", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alert.dismiss();
                    }
                });
                alert	=msMaintance.create();
                alert.show();
            }

        });

        return v;
    }

    private void deletedData(String save, final int position){
        StringRequest register = new StringRequest(Request.Method.POST, save,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        VolleyLog.d("Respone", response.toString());
                        try {
                            JSONObject jsonrespon = new JSONObject(response);
                            int Sucsess = jsonrespon.getInt("success");
                            Log.i("success", String.valueOf(Sucsess));
                            if (Sucsess ==1 ){
                                columnslist.remove(position);
                                notifyDataSetChanged();
                            }else{
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(register);
    }

    static class ViewHolder{
        private ImageView ImgUser;
        private ImageView ImgDel;
        private TextView TvDescription;
        private TextView TvDate;
    }
}