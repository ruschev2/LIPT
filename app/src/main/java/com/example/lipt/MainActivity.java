package com.example.lipt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.lipt.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //instantiating an interface of onClickListener for registration1 button
        binding.registrationButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = registrationActivity.registrationToMainFactory(getApplicationContext());
                startActivity(intent);
            }
        });
    }

    //Main to registration activity factory
    public static Intent mainToRegistrationFactory(Context context) {
        return new Intent(context, MainActivity.class);
    }


}