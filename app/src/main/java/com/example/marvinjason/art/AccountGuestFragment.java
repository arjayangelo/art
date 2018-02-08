package com.example.marvinjason.art;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class AccountGuestFragment extends Fragment {

    private Button btnLoginFragmentAccountGuest;
    private Button btnSignupFragmentAccountGuest;

    public AccountGuestFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account_guest, container, false);

        btnLoginFragmentAccountGuest = (Button) view.findViewById(R.id.btnLogin_fragmentAccountGuest);
        btnSignupFragmentAccountGuest = (Button) view.findViewById(R.id.btnSignup_fragmentAccountGuest);

        btnLoginFragmentAccountGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), LoginActivity.class), 1);
            }
        });

        btnSignupFragmentAccountGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), SignupActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            boolean result = data.getBooleanExtra("result", false);

            if (result) {
                Toast.makeText(getActivity().getApplicationContext(), "You have successfully logged in!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
