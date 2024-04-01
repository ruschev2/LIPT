package com.example.lipt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lipt.databinding.ActivityMainBinding;
import com.example.lipt.databinding.ActivityRegistrationBinding;

public class registrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //instantiating an interface of onclick listener
        binding.registrationButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.mainToRegistrationFactory(getApplicationContext());
                startActivity(intent);
            }
        });

    }

    //intent factory
    public static Intent registrationToMainFactory(Context context) {
        return new Intent(context, registrationActivity.class);
    }




}