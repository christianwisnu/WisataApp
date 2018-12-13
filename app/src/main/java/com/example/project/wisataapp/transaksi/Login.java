package com.example.project.wisataapp.transaksi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.wisataapp.R;
import com.example.project.wisataapp.servise.BaseApiService;
import com.example.project.wisataapp.utilities.Link;
import com.example.project.wisataapp.utilities.PrefUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private PrefUtil prefUtil;
    private BaseApiService mApiService;
    private ProgressDialog pDialog;

    @BindView(R.id.bLoginLogin)
    Button btnLogin;
    @BindView(R.id.btnLoginClearId) Button btnClearId;
    @BindView(R.id.input_layout_login_id)
    TextInputLayout inputLayoutId;
    @BindView(R.id.input_layout_login_sandi) TextInputLayout inputLayoutPasw;
    @BindView(R.id.eLoginId)
    EditText eUserId;
    @BindView(R.id.eLoginSandi) EditText ePassword;
    @BindView(R.id.txtLoginSignUp)
    TextView txtSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        mApiService = Link.getAPIService();
        prefUtil = new PrefUtil(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
    }

    @OnTextChanged(value = R.id.eLoginId, callback = OnTextChanged.Callback.TEXT_CHANGED)
    protected void txtChangeId(){
        btnClearId.setVisibility(View.VISIBLE);
    }

    @OnTextChanged(value = R.id.eLoginSandi, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    protected void afterTxtChangePass(Editable editable){
        validatePasw(editable.length());
    }

    @OnClick(R.id.txtLoginSignUp)
    protected void signUp(){
        startActivityForResult(new Intent(Login.this, Register.class),3);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    @OnClick(R.id.btnLoginClearId)
    protected void clearId(){
        eUserId.setText("");
        btnClearId.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.bLoginLogin)
    protected void loginOther(){
        if(validateUserId(eUserId) && validatePasw(ePassword.length())){
            pDialog.setMessage("Login ...");
            showDialog();
            mApiService.loginRequest(eUserId.getText().toString(), ePassword.getText().toString()).
                    enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()){
                                try{
                                    JSONObject jsonRESULTS = new JSONObject(response.body().string());
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
                                        Toast.makeText(Login.this, "BERHASIL LOGIN", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(Login.this, MainActivity.class));
                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                        finish();
                                    }else{
                                        String error_message = jsonRESULTS.getString("message");
                                        Toast.makeText(Login.this, error_message, Toast.LENGTH_LONG).show();
                                        hideDialog();
                                        prefUtil.clear();
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
                                Toast.makeText(Login.this, "GAGAL LOGIN", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            hideDialog();
                            Toast.makeText(Login.this, "GAGAL LOGIN", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private boolean validatePasw(int length) {
        boolean value = true;
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

    private boolean validateUserId(EditText edittext) {
        boolean value;
        if (eUserId.getText().toString().isEmpty()){
            value=false;
            requestFocus(eUserId);
            inputLayoutId.setError(getString(R.string.err_msg_id));
        } else if (edittext.length() > inputLayoutId.getCounterMaxLength()) {
            value = false;
            inputLayoutId.setError("Max character User Id length is " + inputLayoutId.getCounterMaxLength());
        }else{
            value=true;
            inputLayoutId.setError(null);
        }
        return value;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void navigateUp() {
        final Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (NavUtils.shouldUpRecreateTask(this, upIntent) || isTaskRoot()) {
            TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
        } else {
            NavUtils.navigateUpTo(this, upIntent);
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        navigateUp();
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
