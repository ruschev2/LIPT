/**
 * Luis Hernandez, Guillermo Zendejas
 * April 1, 2024
 * trainerRecordActivity.java, this describes the trainer record activity for our application
 */

package com.example.lipt;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.lipt.databinding.ActivityTrainerRecordBinding;

public class trainerRecordActivity extends AppCompatActivity {

    private ActivityTrainerRecordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrainerRecordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //instantiating an interface of onClickListener for return home button
        binding.trainerRecordExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.mainToRegistrationFactory(getApplicationContext());
                startActivity(intent);
            }
        });

    }


    //intent factory
    public static Intent trainerRecordFactory(Context context) {
        return new Intent(context, trainerRecordActivity.class);
    }

}