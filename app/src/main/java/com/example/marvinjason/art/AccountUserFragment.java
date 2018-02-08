package com.example.marvinjason.art;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Calendar;
import java.util.Locale;

public class AccountUserFragment extends Fragment {
    private Button viewCart;
    private Button checkoutBtn;
    private Button logoutBtn;
    public AccountUserFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_user, container, false);
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        
//        if (LoginActivity.isLoggedIn && LoginActivity.isFacebook) {
//            Picasso.with(getContext()).load(LoginActivity.imageUrl).resize(200, 200).into(profilePicture);
//            nameTextView.setText(LoginActivity.firstName);
//
//            Fragment newFragment = new AccountUserFragment();
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//            transaction.replace(R.id.rl_fragmentaccountguest, newFragment);
//            transaction.addToBackStack(null);
//
//            transaction.commit();
//        }
        viewCart = (Button) view.findViewById(R.id.cart_btn);
        viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cartIntent = new Intent(getActivity(),CartActivity.class);
                startActivity(cartIntent);
            }
        });

        checkoutBtn = (Button) view.findViewById(R.id.checkout_btn);
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity().getApplicationContext(), PurchaseActivity.class));
            }
        });

        logoutBtn = (Button) view.findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getApplicationContext().getSharedPreferences("data", Context.MODE_PRIVATE).edit().remove("access_token").commit();
                MainActivity.status = false;
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
