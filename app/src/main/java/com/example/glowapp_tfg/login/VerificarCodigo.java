package com.example.glowapp_tfg.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.glowapp_tfg.R;
import com.example.glowapp_tfg.dao.UsuarioDAO;
import com.example.glowapp_tfg.menus.InformacionGeneral;
import com.example.glowapp_tfg.menus.MenuPrincipal;

public class VerificarCodigo extends AppCompatActivity {

    EditText codigoEditText;
    String email;
    UsuarioDAO usuarioDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verificar_codigo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        codigoEditText = findViewById(R.id.editTextCodigo);
        usuarioDAO = new UsuarioDAO();
        email = getIntent().getStringExtra("email");

    }
    public void verificarCodigo(View view) {
        String codigoIngresado = codigoEditText.getText().toString().trim();

        if (codigoIngresado.isEmpty()) {
            Toast.makeText(this, "Introduce el código", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean verificado = usuarioDAO.verificarCodigoUsuario(email, codigoIngresado);

        if (verificado) {
            Toast.makeText(this, "Cuenta verificada con éxito", Toast.LENGTH_SHORT).show();
            finish(); // o redirigir al login
        } else {
            Toast.makeText(this, "Código incorrecto", Toast.LENGTH_SHORT).show();
        }
    }

    public void cancelarRegistro(View view) {
        boolean eliminado = usuarioDAO.eliminarUsuarioNoVerificado(email);

        if (eliminado) {
            Toast.makeText(this, "Registro cancelado. Usuario eliminado.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se pudo eliminar el usuario.", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(VerificarCodigo.this, Login.class);
        startActivity(intent);
        finish();
    }


}