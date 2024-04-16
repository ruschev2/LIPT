package com.example.lipt;

import android.os.Bundle;



import androidx.appcompat.app.AppCompatActivity;





import com.example.lipt.databinding.ActivityPlayerInfoBinding;

public class PlayerInfoActivity extends AppCompatActivity {

    private ActivityPlayerInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPlayerInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}