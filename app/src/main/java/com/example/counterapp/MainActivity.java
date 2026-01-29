package com.example.counterapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
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
import android.view.MotionEvent;

import com.google.android.material.progressindicator.CircularProgressIndicator;

public class MainActivity extends AppCompatActivity {

    ConstraintLayout main;
    TextView counterVar, mala_count;
    CircularProgressIndicator progress;
    private int count = 0;
    int mala = 1;
    Button reset;

    AudioAttributes audioAttributes;
    SoundPool soundPool;
    int hareKrishnaSound;

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

        progress = findViewById(R.id.circularIndiacator);
        main = findViewById(R.id.main);
        counterVar = findViewById(R.id.counterNumbertxt);
        reset = findViewById(R.id.resetbtn);
        mala_count = findViewById(R.id.mala_count);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        }

        hareKrishnaSound = soundPool.load(this, R.raw.harekrishna, 1);

        main.setOnTouchListener((v, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                count++;
                showFloatingText(event.getX(), event.getY());

                if (count == 108) {
                    mala++;
                    mala_count.setText(String.valueOf(mala));
                    reset();
                }

                counterVar.setText(String.valueOf(count));
                progress.setProgressCompat(count, true);

                soundPool.play(hareKrishnaSound, 1f, 1f, 1, 0, 1f);

                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                            VibrationEffect.createOneShot(
                                    100,
                                    VibrationEffect.DEFAULT_AMPLITUDE
                            )
                    );
                } else {
                    vibrator.vibrate(100);
                }
            }

            return true; // ✅ VERY IMPORTANT
        });

        reset.setOnClickListener(v -> reset());
    }

    public void reset() {
        count = 0;
        progress.setProgressCompat(count, true);
        counterVar.setText(String.valueOf(count));
    }

    private void showFloatingText(float x, float y) {
        TextView tv = new TextView(this);
        tv.setText("Hare Krishna");
        tv.setTextSize(26f); // 🔼 bigger text
        tv.setAlpha(1f);

        // random color
        int color = android.graphics.Color.rgb(
                (int) (Math.random() * 256),
                (int) (Math.random() * 256),
                (int) (Math.random() * 256)
        );
        tv.setTextColor(color);

        // start position
        tv.setX(x);
        tv.setY(y);

        main.addView(tv);

        // 🔀 random direction
        float randomX = (float) ((Math.random() - 0.5) * 400); // left/right
        float randomY = (float) ((Math.random() - 0.5) * 400); // up/down

        ObjectAnimator moveX =
                ObjectAnimator.ofFloat(tv, "translationX", x, x + randomX);

        ObjectAnimator moveY =
                ObjectAnimator.ofFloat(tv, "translationY", y, y + randomY);

        ObjectAnimator fadeOut =
                ObjectAnimator.ofFloat(tv, "alpha", 1f, 0f);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(moveX, moveY, fadeOut);
        set.setDuration(1200); // slightly slower
        set.start();

        set.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                main.removeView(tv);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
        }
    }
}
