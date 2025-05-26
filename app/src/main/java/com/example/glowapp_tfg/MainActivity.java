package com.example.glowapp_tfg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.glowapp_tfg.login.Login;
import com.example.glowapp_tfg.menus.MenuPrincipal;

import java.sql.SQLException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView frasesMain;
    private String[] frases;
    private Timer timer;

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
        frasesMain = findViewById(R.id.frasesMain);
        frases = getResources().getStringArray(R.array.frases_main);
        cambiarFrase();
        iniciarTimer();
    }

    private void cambiarFrase() {
        String fraseAleatoria = frases[new Random().nextInt(frases.length)];
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                ObjectAnimator fadeOut = ObjectAnimator.ofFloat(frasesMain, "alpha", 1f, 0f);
                fadeOut.setDuration(500);  //5 segundos hasta transición

                fadeOut.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        frasesMain.setText(fraseAleatoria);
                        //Clase que permite hacer animaciones
                        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(frasesMain, "alpha", 0f, 1f);
                        fadeIn.setDuration(500);
                        fadeIn.start();
                    }
                });

                fadeOut.start();
            }
        });
    }
    private void iniciarTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                cambiarFrase();
            }
        }, 5000, 5000);
    }

    public void IrALogin(View view) throws SQLException {
        SharedPreferences sharedPreferences = getSharedPreferences("datosGuardados", Context.MODE_PRIVATE);
        if(sharedPreferences.contains("idUsuario") && sharedPreferences.contains("nombreUsuario")){
            Intent intent = new Intent(this, MenuPrincipal.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }

    }

}