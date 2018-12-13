package com.example.project.wisataapp.transaksi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.project.wisataapp.R;
import com.example.project.wisataapp.servise.BaseApiService;
import com.example.project.wisataapp.utilities.CircleTransform;
import com.example.project.wisataapp.utilities.Link;
import com.example.project.wisataapp.utilities.PrefUtil;
import com.example.project.wisataapp.utilities.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity {

    private PrefUtil prefUtil;
    private ProgressDialog pDialog;
    private BaseApiService mApiService, mUploadService;
    private RadioButton radioSexButton;
    private String[] items = {"Camera", "Gallery"};
    private static final int REQUEST_CODE_CAMERA = 0012;
    private static final int REQUEST_CODE_GALLERY = 0013;
    private String hasilFoto=null, hasilImage=null;
    private Uri selectedImage;

    @BindView(R.id.bRegisterDaftar)Button btnRegister;
    @BindView(R.id.btnRegisterClearId)Button btnClearId;
    @BindView(R.id.input_layout_register_id)TextInputLayout inputLayoutId;
    @BindView(R.id.input_layout_register_sandi)TextInputLayout inputLayoutPasw;
    @BindView(R.id.input_layout_register_sandi2)TextInputLayout inputLayoutPasw2;
    @BindView(R.id.input_layout_register_nama)TextInputLayout inputLayoutNama;
    @BindView(R.id.input_layout_register_telp)TextInputLayout inputLayoutTelp;//
    @BindView(R.id.imgRegisterFoto)ImageView imgFoto;
    @BindView(R.id.radioSex)RadioGroup rgSex;
    @BindView(R.id.radioMale)RadioButton rbMale;
    @BindView(R.id.radioFemale)RadioButton rbFemale;
    @BindView(R.id.eRegisterId)EditText eId;
    @BindView(R.id.eRegisterSandi)EditText ePassword;
    @BindView(R.id.eRegisterSandi2)EditText ePassword2;
    @BindView(R.id.eRegisterNama)EditText eNama;
    @BindView(R.id.eRegisterTelp)EditText eTelp;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ButterKnife.bind(this);
        mApiService         = Link.getAPIService();
        mUploadService = Link.getImageService();
        prefUtil            = new PrefUtil(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
    }

    @OnClick(R.id.btnRegisterClearId)
    protected void regClearEmail(){
        eId.setText("");
        btnClearId.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.bRegisterDaftar)
    protected void register(){
        if(validateId(eId) && validateNama(eNama.length()) &&
                validatePasw(ePassword.length()) && validatePasw2(ePassword2.length())){
            int selectedId = rgSex.getCheckedRadioButtonId();
            radioSexButton = (RadioButton) findViewById(selectedId);
            if(hasilFoto!=null){
                uploadImage(selectedImage, hasilImage, eId.getText().toString(), eNama.getText().toString(), eTelp.getText().toString(),
                        hasilFoto.equals("Y")?Link.BASE_URL_IMAGE_PROFIL+hasilImage:"",
                        radioSexButton.getText().equals("Laki-laki")?"L":"P", ePassword.getText().toString());
            }else{
                requestRegister(eNama.getText().toString(), ePassword.getText().toString(), eId.getText().toString(),
                        "", radioSexButton.getText().equals("Laki-laki")?"L":"P",
                        eTelp.getText().toString());
            }
        }
    }

    @OnClick(R.id.imgRegisterFoto)
    protected void pilihFoto() {
        openImage();
    }

    @OnTextChanged(value = R.id.eRegisterId, callback = OnTextChanged.Callback.TEXT_CHANGED)
    protected void txtChangePass(){
        btnClearId.setVisibility(View.VISIBLE);
    }

    @OnTextChanged(value = R.id.eRegisterId, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void afterTxtChangeEmail(Editable editable){
        validateId(eId);
    }

    @OnTextChanged(value = R.id.eRegisterSandi, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void afterTxtChangeSandi(Editable editable){
        validatePasw(editable.length());
    }

    @OnTextChanged(value = R.id.eRegisterSandi2, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void afterTxtChangeSandi2(Editable editable){
        validatePasw2(editable.length());
    }

    @OnTextChanged(value = R.id.eRegisterNama, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void afterTxtChangeNama(Editable editable){
        validateNama(editable.length());
    }

    private void navigateUp() {
        final Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent) || isTaskRoot()) {
            TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
        } else {
            NavUtils.navigateUpTo(this, upIntent);
        }
    }

    @Override
    public void onBackPressed() {
        navigateUp();
        finish();
        super.onBackPressed();
    }

    private boolean validateId(EditText edittext) {
        boolean value;
        if (eId.getText().toString().isEmpty()){
            value=false;
            requestFocus(eId);
            inputLayoutId.setError(getString(R.string.err_msg_id));
        } else if (edittext.length() > inputLayoutId.getCounterMaxLength()) {
            value=false;
            inputLayoutId.setError("Max character User ID length is " + inputLayoutId.getCounterMaxLength());
        }else {
            value=true;
            inputLayoutId.setError(null);
        }
        return value;
    }

    private boolean validatePasw(int length) {
        boolean value=true;
        int minValue = 6;
        if (ePassword.getText().toString().trim().isEmpty()) {
            value=false;
            requestFocus(ePassword);
            inputLayoutPasw.setError(getString(R.string.err_msg_sandi));
        } else if (length > inputLayoutPasw.getCounterMaxLength()) {
            value=false;
            inputLayoutPasw.setError("Max character password length is " + inputLayoutPasw.getCounterMaxLength());
        } else if (length < minValue) {
            value=false;
            inputLayoutPasw.setError("Min character password length is 6" );
        } else{
            value=true;
            inputLayoutPasw.setError(null);}
        return value;
    }

    private boolean validatePasw2(int length) {
        boolean value=true;
        int minValue = 6;
        if (ePassword2.getText().toString().trim().isEmpty()) {
            value=false;
            requestFocus(ePassword2);
            inputLayoutPasw2.setError(getString(R.string.err_msg_sandi));
        } else if (length > inputLayoutPasw2.getCounterMaxLength()) {
            value=false;
            inputLayoutPasw2.setError("Max character password length is " + inputLayoutPasw2.getCounterMaxLength());
        } else if (length < minValue) {
            value=false;
            inputLayoutPasw2.setError("Min character password length is 6" );
        } else if(!ePassword2.getText().toString().equals(ePassword.getText().toString())){
            value=false;
            inputLayoutPasw2.setError("Confirm Password is wrong");
        }else{
            value=true;
            inputLayoutPasw2.setError(null);
        }
        return value;
    }

    private boolean validateNama(int length) {
        boolean value=true;
        if (eNama.getText().toString().trim().isEmpty()) {
            value=false;
            requestFocus(eNama);
            inputLayoutNama.setError(getString(R.string.err_msg_name));
        } else if (length > inputLayoutNama.getCounterMaxLength()) {
            value=false;
            inputLayoutNama.setError("Max character name length is " + inputLayoutNama.getCounterMaxLength());
        }else {
            value=true;
            inputLayoutNama.setError(null);
        }
        return value;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void openImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {
                    //dialog.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CODE_CAMERA);
                } else if (items[i].equals("Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, REQUEST_CODE_GALLERY);
                }
            }
        });
        builder.show();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private String getNameImage() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd-HHmmss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private void uploadImage(Uri selectedImage, String desc, final String id, final String nama, final String telp,
                             final String foto, final String gender, final String pasw) {
        pDialog.setMessage("Uploading Image to Server...");
        showDialog();
        File file = new File(getRealPathFromURI(selectedImage));
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(selectedImage)), file);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);
        mUploadService.uploadImage(requestFile, descBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("value").equals("false")){
                            requestRegister(nama, pasw, id, foto, gender, telp);
                        }else{
                            hideDialog();
                            Toast.makeText(Register.this, jsonRESULTS.getString("message"), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(Register.this, "Some error occurred...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideDialog();
                Toast.makeText(Register.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void requestRegister(String nama, String pasw, String id, String foto,
                                 final String gender, String phone){
        pDialog.setMessage("Registering ...");
        showDialog();
        String device = Utils.getDeviceName();
        mApiService.registerRequest(nama, id, foto, device, pasw, phone, gender).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("value").equals("false")){
                            if (jsonRESULTS.getString("value").equals("false")){
                                String uId = jsonRESULTS.getJSONObject("user").getString("c_userid")==null?"":
                                        jsonRESULTS.getJSONObject("user").getString("c_userid");
                                String nama = jsonRESULTS.getJSONObject("user").getString("vc_username")==null?"":
                                        jsonRESULTS.getJSONObject("user").getString("vc_username");
                                String telp = jsonRESULTS.getJSONObject("user").getString("c_telp")==null?"":
                                        jsonRESULTS.getJSONObject("user").getString("c_telp");
                                String gender = jsonRESULTS.getJSONObject("user").getString("c_gender")==null?"":
                                        jsonRESULTS.getJSONObject("user").getString("c_gender");
                                String foto = jsonRESULTS.getJSONObject("user").getString("vc_pathfoto")==null?"":
                                        jsonRESULTS.getJSONObject("user").getString("vc_pathfoto");
                                String jenisAdmin = jsonRESULTS.getJSONObject("user").getString("c_jenis")==null?"":
                                        jsonRESULTS.getJSONObject("user").getString("c_jenis");
                                prefUtil.saveUserInfo(nama, uId, gender, foto, jenisAdmin, telp);
                                Toast.makeText(Register.this, "BERHASIL REGISTRASI", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Register.this, MainActivity.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            } else {
                                String error_message = jsonRESULTS.getString("message");
                                Toast.makeText(Register.this, error_message, Toast.LENGTH_LONG).show();
                                hideDialog();
                                prefUtil.clear();
                            }
                        }
                    }catch (JSONException e) {
                        hideDialog();
                        e.printStackTrace();
                    }catch (IOException e) {
                        hideDialog();
                        e.printStackTrace();
                    }
                }else{
                    hideDialog();
                    Toast.makeText(Register.this, "GAGAL REGISTRASI", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideDialog();
                Toast.makeText(Register.this, "GAGAL REGISTRASI", Toast.LENGTH_LONG).show();
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            try {
                hasilFoto = "Y";
                hasilImage = getNameImage().toString() + ".jpg";
                selectedImage = data.getData();

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                File destination = new File(Environment.getExternalStorageDirectory(),
                        hasilImage);
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Glide.with(this).load(selectedImage)
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(this))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgFoto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            try {
                hasilFoto = "Y";
                hasilImage = getNameImage().toString() + ".jpg";
                selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor == null) return;
                cursor.moveToFirst();
                cursor.close();
                Glide.with(this).load(selectedImage)
                        .crossFade()
                        .thumbnail(0.5f)
                        .bitmapTransform(new CircleTransform(this))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgFoto);
            } catch (Exception e) {
                hideDialog();
                e.printStackTrace();
            }
        }
    }
}

