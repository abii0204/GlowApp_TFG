package com.example.glowapp_tfg.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.glowapp_tfg.R;
import com.example.glowapp_tfg.dao.UsuarioDAO;
import com.example.glowapp_tfg.menus.MenuPrincipal;
import com.example.glowapp_tfg.modelos.UsuarioModel;

import java.sql.SQLException;

public class Login extends AppCompatActivity {

    TextView correo, contrasena;
    UsuarioDAO usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        usuarioDao = new UsuarioDAO();
        id();
    }

    public void id(){
        correo = findViewById(R.id.correoLogin);
        contrasena = findViewById(R.id.contrasenaLogin);
    }

    public void IrARegistroL(View view) throws SQLException {
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }

    public void iniciarSesion(View view) {

        String email = correo.getText().toString().trim();
        String contrasenaComprobar = contrasena.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Por favor, introduce tu correo", Toast.LENGTH_SHORT).show();
            correo.requestFocus();
            return;
        }

        if (contrasenaComprobar.isEmpty()) {
            Toast.makeText(this, "Por favor, introduce tu contraseña", Toast.LENGTH_SHORT).show();
            contrasena.requestFocus();
            return;
        }

        try {
            UsuarioModel usuario = usuarioDao.comprobarUsuario(email, contrasenaComprobar);

            if (usuario == null) {
                Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login.this, MenuPrincipal.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("nombreUsuario", usuario.getNombre());
            intent.putExtra("idUsuario", usuario.getId());
            SharedPreferences sharedPreferences = getSharedPreferences("datosGuardados", Context.MODE_PRIVATE);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            sharedPreferencesEditor.putInt("idUsuario", usuario.getId());
            sharedPreferencesEditor.putString("nombreUsuario", usuario.getNombre());
            sharedPreferencesEditor.apply();
            startActivity(intent);
            finish();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
        }
    }

}