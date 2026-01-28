package com.example.counterapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.content.Context;

import com.google.android.material.progressindicator.CircularProgressIndicator;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout main;
    TextView counterVar,mala_count;
    CircularProgressIndicator progress;
    private int count=0;
    int mala=1;
    Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
         progress = findViewById(R.id.progressbar);
        main= findViewById(R.id.main);
        counterVar= findViewById(R.id.counterNumbertxt);
        reset = findViewById(R.id.resetbtn);
        mala_count=findViewById(R.id.mala_count);
        main.setOnClickListener(v -> {
            count++;
            if(count==108){
                mala++;
                mala_count.setText(String.valueOf(mala));
                reset();
            }
            counterVar.setText(String.valueOf(count));
            progress.setProgressCompat(count, true);
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(100);
            }
        });
        reset.setOnClickListener(v -> {
            reset();
        });

    }
    public void reset(){
        count=0;
        progress.setProgressCompat(count, true);
        counterVar.setText(String.valueOf(count));
    }


}