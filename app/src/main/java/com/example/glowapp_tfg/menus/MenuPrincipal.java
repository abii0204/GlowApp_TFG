package com.example.glowapp_tfg.menus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.glowapp_tfg.R;
import com.example.glowapp_tfg.dao.SeguimientoDAO;
import com.example.glowapp_tfg.dao.UsuarioDAO;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MenuPrincipal extends AppCompatActivity {

    TextView bienvenido, txSentimientosAñadidos, txProductosUtilizadosAñadidos, txAlimentosAñadidos, txAguaAñadida;
    CalendarView calendario;
    ImageView imagenSelfie;
    Spinner opcionSentimientoPiel, opcionAguaConsumida;
    EditText etProductosUtilizados, etAlimentosConsumidos;
    ImageButton btSelfie, btSentimientos, btProductos, btAlimentos, btAgua;

    UsuarioDAO usuarioDAO;


    private String fechaSeleccionada;
    private int idUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_principal);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        id();
        cargarDatosDelDia();

        // Inicializar fechaSeleccionada con la fecha actual del calendario
        long fechaHoy = calendario.getDate();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(fechaHoy);
        int año = calendar.get(java.util.Calendar.YEAR);
        int mes = calendar.get(java.util.Calendar.MONTH) + 1; // +1 porque enero = 0
        int día = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        fechaSeleccionada = String.format("%04d-%02d-%02d", año, mes, día);

        // Listener por si el usuario cambia la fecha manualmente
        calendario.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            month += 1; // Ajuste del mes
            fechaSeleccionada = String.format("%04d-%02d-%02d", year, month, dayOfMonth);
            cargarDatosDelDia();
        });

        String nombreUsuario = getIntent().getStringExtra("nombreUsuario");
        if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
            bienvenido.setText("Bienvenido/a " + nombreUsuario);
        }

        idUsuario = getIntent().getIntExtra("idUsuario", -1);

        Intent intentUsuario = new Intent(MenuPrincipal.this, Resenas.class);
        intentUsuario.putExtra("usuario_id", idUsuario);
        startActivity(intentUsuario);


        cargarSpinnerSentimientos();
        cargarSpinnerAgua();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_principal) {
                return true;
            } else if (id == R.id.menu_resenas) {
                Intent intent = new Intent(MenuPrincipal.this, Resenas.class);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.menu_info) {
                Intent intent = new Intent(MenuPrincipal.this, InformacionGeneral.class);
                startActivity(intent);
                finish();
                return true;
            } else {
                return false;
            }
        });

    }

    public void id() {
        bienvenido = findViewById(R.id.txtBienvenidoM);
        txSentimientosAñadidos = findViewById(R.id.lbSentimientosAñadidos);
        txProductosUtilizadosAñadidos = findViewById(R.id.lbProductosAñadidos);
        txAlimentosAñadidos = findViewById(R.id.lbAlimentosAñadidos);
        txAguaAñadida = findViewById(R.id.lbCantidadAguaConsumida);
        calendario = findViewById(R.id.idCalendario);
        imagenSelfie = findViewById(R.id.idImagenSelfie);
        opcionSentimientoPiel = findViewById(R.id.spSentimientosPiel);
        opcionAguaConsumida = findViewById(R.id.spAguaConsumida);
        etProductosUtilizados = findViewById(R.id.etProductosUsados);
        etAlimentosConsumidos = findViewById(R.id.etAñadirAlimentos);
        btSelfie = findViewById(R.id.btSelfie);
        btSentimientos = findViewById(R.id.btAnadirSentimiento);
        btProductos = findViewById(R.id.btMeterProducto);
        btAlimentos = findViewById(R.id.btAnadirAlimentos);
        btAgua = findViewById(R.id.btAnadirAgua);
    }

    public void cargarSpinnerSentimientos() {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        ArrayList<String> sentimientos = usuarioDAO.obtenerSentimientos();

        sentimientos.add(0, "Elige un sentimiento");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sentimientos);
        opcionSentimientoPiel.setAdapter(adapter);

        opcionSentimientoPiel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {}

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    public void introducirSentimiento(View view) {
        String sentimientoSeleccionado = opcionSentimientoPiel.getSelectedItem().toString();

        if (!sentimientoSeleccionado.equals("Añade un sentimiento")) {
            String actuales = txSentimientosAñadidos.getText().toString();

            if (!actuales.contains(sentimientoSeleccionado)) {
                if (!actuales.isEmpty()) {
                    actuales += ", ";
                }
                actuales += sentimientoSeleccionado;
                txSentimientosAñadidos.setText(actuales);

                SeguimientoDAO seguimientoDAO = new SeguimientoDAO();
                int idSeguimientoHoy = seguimientoDAO.obtenerOInsertarSeguimiento(idUsuario, fechaSeleccionada);

                if (idSeguimientoHoy != -1) {
                    seguimientoDAO.insertarSentimientoEnSeguimiento(idSeguimientoHoy, idUsuario, sentimientoSeleccionado);
                } else {
                    System.err.println("Error al obtener o insertar el seguimiento.");
                }
            }

            opcionSentimientoPiel.setSelection(0);
        }
    }

    public void cargarSpinnerAgua() {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        ArrayList<String> opcionesAgua = usuarioDAO.obtenerOpcionesAgua();

        opcionesAgua.add("Elige cantidad de agua");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opcionesAgua);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opcionAguaConsumida.setAdapter(adapter);

        opcionAguaConsumida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void introducirAgua(View view) {
        String aguaSeleccionada = opcionAguaConsumida.getSelectedItem().toString();

        if (!aguaSeleccionada.equals("Elige cantidad de agua")) {
            String actuales = txAguaAñadida.getText().toString();

            if (!actuales.contains(aguaSeleccionada)) {
                if (!actuales.isEmpty()) {
                    actuales += ", ";
                }
                actuales += aguaSeleccionada;
                txAguaAñadida.setText(actuales);

                SeguimientoDAO seguimientoDAO = new SeguimientoDAO();
                int idSeguimientoHoy = seguimientoDAO.obtenerOInsertarSeguimiento(idUsuario, fechaSeleccionada);

                if (idSeguimientoHoy != -1) {
                    int cantidad = Integer.parseInt(aguaSeleccionada.replace(" ml", ""));
                    seguimientoDAO.insertarAguaConsumida(idSeguimientoHoy, cantidad);
                } else {
                    System.err.println("Error al obtener o insertar el seguimiento.");
                }
            }

            opcionAguaConsumida.setSelection(0);
        }
    }

    public void introducirAlimentos(View view) {
        String alimento = etAlimentosConsumidos.getText().toString().trim();
        if (alimento.isEmpty()) {
            return;
        }

        String actuales = txAlimentosAñadidos.getText().toString();
        if (!actuales.contains(alimento)) {
            if (!actuales.isEmpty()) {
                actuales += ", ";
            }
            actuales += alimento;
            txAlimentosAñadidos.setText(actuales);

            SeguimientoDAO seguimientoDAO = new SeguimientoDAO();
            int idSeguimientoHoy = seguimientoDAO.obtenerOInsertarSeguimiento(idUsuario, fechaSeleccionada);
            if (idSeguimientoHoy != -1) {
                seguimientoDAO.insertarAlimentoEnSeguimiento(idSeguimientoHoy, alimento);
            } else {
                System.err.println("Error al obtener o insertar el seguimiento para alimentos.");
            }
        }

        etAlimentosConsumidos.setText("");
    }

    public void introducirProducto(View view) {
        String producto = etProductosUtilizados.getText().toString().trim();
        if (producto.isEmpty()) {
            return;
        }

        String actuales = txProductosUtilizadosAñadidos.getText().toString();
        if (!actuales.contains(producto)) {
            if (!actuales.isEmpty()) {
                actuales += ", ";
            }
            actuales += producto;
            txProductosUtilizadosAñadidos.setText(actuales);

            SeguimientoDAO seguimientoDAO = new SeguimientoDAO();
            int idSeguimientoHoy = seguimientoDAO.obtenerOInsertarSeguimiento(idUsuario, fechaSeleccionada);
            if (idSeguimientoHoy != -1) {
                seguimientoDAO.insertarProductoUsado(idSeguimientoHoy, producto);
            } else {
                System.err.println("Error al obtener o insertar el seguimiento para productos.");
            }
        }

        etProductosUtilizados.setText("");
    }

    private void cargarDatosDelDia() {
        new Thread(() -> {
            SeguimientoDAO seguimientoDAO = new SeguimientoDAO();
            SeguimientoDAO.DatosSeguimiento datos = seguimientoDAO.obtenerDatosDeSeguimiento(idUsuario, fechaSeleccionada);

            runOnUiThread(() -> {
                txSentimientosAñadidos.setText(String.join(", ", datos.sentimientos));
                txAlimentosAñadidos.setText(String.join(", ", datos.alimentos));
                txProductosUtilizadosAñadidos.setText(String.join(", ", datos.productos));
                txAguaAñadida.setText(datos.agua > 0 ? datos.agua + " ml" : "");
            });
        }).start();
    }


}
