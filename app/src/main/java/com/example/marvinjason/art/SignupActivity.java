package com.example.marvinjason.art;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;


public class SignupActivity extends AppCompatActivity {
    private Button regBtn;
    private EditText email, password, confirmPass;
    private String mail, pass, con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmPass = (EditText) findViewById(R.id.confirmPassword);

        sendData();
    }

    public void sendData() {
        new AsyncTask() {

            @Override
            protected void onPreExecute(){
                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] objects){

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {

            }
        }.execute();
    }
}

