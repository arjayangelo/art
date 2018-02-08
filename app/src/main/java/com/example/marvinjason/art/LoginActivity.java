package com.example.marvinjason.art;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    private ViewPager mViewPager;

    public static boolean isFacebook = false;
    public static boolean isGmail = false;
    public static boolean isEmail = false;
    public static boolean isLoggedIn = false;
    public static String firstName = "";
    public static String imageUrl = "";
    public static String userId = "";
    public static String kek = "";

    private EditText editText;
    private EditText editPass;
    private Button btnLogin;

    private TextView passSignUp;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Log In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText = (EditText) findViewById(R.id.et_activityLoginEmail);
        editPass = (EditText) findViewById(R.id.et_activityLoginPass);

        btnLogin = (Button) findViewById(R.id.btnLogin_activityLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = editText.getText().toString();
                password = editPass.getText().toString();
                sendData();
            }
        });

    }

    public void sendData() {
        new AsyncTask() {
            JSONObject jsonObject;
            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] objects){
                RestApi rest = new RestApi(LoginActivity.this);
                jsonObject = rest.authenticate("https://art-augmented-retail.herokuapp.com/api/v1/sessions", email, password);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                Context context = LoginActivity.this;
                try {
                    String token = jsonObject.getString("access_token");
                    Log.d("TOKEN", token);
                    context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().putString("access_token",token).commit();
                    MainActivity.status = true;
                } catch (Exception e) {
                    context.getSharedPreferences("data", Context.MODE_PRIVATE).edit().putString("access_token","error").commit();
                }

                String token = context.getSharedPreferences("data", Context.MODE_PRIVATE).getString("access_token","error");
                if (!token.equals("error")) {
                    isLoggedIn = true;
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class );
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid email/password!" + kek, Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }
}