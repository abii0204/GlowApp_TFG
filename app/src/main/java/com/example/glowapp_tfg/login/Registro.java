package com.example.glowapp_tfg.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.glowapp_tfg.R;
import com.example.glowapp_tfg.dao.UsuarioDAO;
import com.example.glowapp_tfg.modelos.UsuarioModel;

import java.sql.SQLException;

public class Registro extends AppCompatActivity {

    Spinner tipoPiel;
    EditText nombre;
    EditText correo;
    EditText contrasena;
    EditText contrasenaConfirm;

    UsuarioDAO usuarioDAO;

    String tipoSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usuarioDAO = new UsuarioDAO();

        id();
        elegirTipoPiel();

    }

    public void id(){
        nombre = findViewById(R.id.nombreR);
        correo = findViewById(R.id.correoR);
        contrasena = findViewById(R.id.contrasenaR);
        contrasenaConfirm = findViewById(R.id.confirmarCR);
    }

    public void elegirTipoPiel(){
        tipoPiel = findViewById(R.id.tiposDePielR);

        String[] tiposDePiel = {"Elige tu tipo de piel:", "Normal", "Grasa", "Seca", "Mixta", "Sensible"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tiposDePiel);
        tipoPiel.setAdapter(adapter);

        tipoPiel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                tipoSeleccionado = (String) parentView.getItemAtPosition(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    public void registrarUsuario(View view) {

        String nb = String.valueOf(this.nombre.getText());
        String ml = String.valueOf(correo.getText());
        String cnt = String.valueOf(contrasena.getText());
        String cntC = String.valueOf(contrasenaConfirm.getText());

        if (tipoSeleccionado == null || tipoSeleccionado.equals("Elige tu tipo de piel:")) {
            Toast.makeText(this, "Por favor, selecciona un tipo de piel", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!cnt.equals(cntC)) {
            Toast.makeText(this, "Las contraseñas no coincicen", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (usuarioDAO.existeEmail(ml)) {
//            Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
//            return;
//        }

        UsuarioModel nuevoUsuario = new UsuarioModel(nb, ml, cnt, tipoSeleccionado);
        usuarioDAO.insertarUsuario(nuevoUsuario);

        if (usuarioDAO.insertarUsuario(nuevoUsuario)) {
            Intent intent = new Intent(this, VerificarCodigo.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
        }
    }
}