package com.example.glowapp_tfg.menus;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.Manifest;
import android.widget.Toast;

import com.example.glowapp_tfg.R;
import com.example.glowapp_tfg.dao.SeguimientoDAO;
import com.example.glowapp_tfg.dao.UsuarioDAO;
import com.example.glowapp_tfg.login.Login;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MenuPrincipal extends AppCompatActivity {

    TextView bienvenido, txSentimientosAñadidos, txProductosUtilizadosAñadidos, txAlimentosAñadidos, txAguaAñadida;
    CalendarView calendario;
    ImageView imagenSelfie, imVasoAgua;
    Spinner opcionSentimientoPiel;
    EditText etProductosUtilizados, etAlimentosConsumidos;
    ImageButton btSelfie;

    SharedPreferences sharedPreferences;


    private String fechaSeleccionada;
    private static final int PERMISOS_CAMARA = 0;
    private static final int ALMACENAR_CODIGO_CAMARA = 1;
    Bitmap imageBitmap;
    private int idUsuario;

    private String baseSentimientos;
    private String baseProductos;
    private String baseAlimentos;

    private int[] imagenesVasos = {
            R.drawable.primervaso, // 250 ml
            R.drawable.segundovaso, // 500 ml
            R.drawable.tercervaso,  // 1L
            R.drawable.cuartovaso,  // 1.5L
            R.drawable.quintovaso   // 2L
    };

    private int[] cantidades = {250, 500, 1000, 1500, 2000};

    private int indiceVasoActual = 0;
    private int cantidadSeleccionada = cantidades[0];


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

        sharedPreferences = getSharedPreferences("datosGuardados", Context.MODE_PRIVATE);
        idUsuario = sharedPreferences.getInt("idUsuario", 0);


        String nombreUsuario = sharedPreferences.getString("nombreUsuario", "Error");
        if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
            bienvenido.setText("Bienvenido/a " + nombreUsuario);
        }

        Log.d("ID USUARIO", String.valueOf(idUsuario));

        // Inicializar fechaSeleccionada con la fecha actual del calendario
        long fechaHoy = calendario.getDate();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(fechaHoy);
        int año = calendar.get(java.util.Calendar.YEAR);
        int mes = calendar.get(java.util.Calendar.MONTH) + 1; // +1 porque enero = 0
        int día = calendar.get(java.util.Calendar.DAY_OF_MONTH);
        fechaSeleccionada = String.format("%04d-%02d-%02d", año, mes, día);
        Log.d("FECHA SELECCIONADA1", fechaSeleccionada);

        cargarDatosDelDia();


        // Listener por si el usuario cambia la fecha manualmente
        calendario.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            month += 1; // Ajuste del mes
            fechaSeleccionada = String.format("%04d-%02d-%02d", year, month, dayOfMonth);
            cargarDatosDelDia();
            Log.d("FECHA SELECCIONADA2", fechaSeleccionada);
        });


        cargarSpinnerSentimientos();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.menu_principal);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_principal) {
                return true;
            } else if (id == R.id.menu_resenas) {
                Intent intent = new Intent(MenuPrincipal.this, Resenas.class);
                intent.putExtra("usuario_id", idUsuario);
                startActivity(intent);
                finish();
                return true;
            } else if (id == R.id.menu_info) {
                startActivity(new Intent(MenuPrincipal.this, InformacionGeneral.class));
                finish();
                return true;
            } else if (id == R.id.menu_cerrarSesion) {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Confirmación")
                        .setMessage("¿Estás seguro de que deseas continuar?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                sharedPreferences = getSharedPreferences("datosGuardados", Context.MODE_PRIVATE);
                                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                                sharedPreferencesEditor.clear();
                                sharedPreferencesEditor.apply();
                                startActivity(new Intent(MenuPrincipal.this, Login.class));
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .create();
                dialog.setCancelable(false);
                dialog.show();

                return  true;

            } else {
                return false;
            }
        });

    }

    public void id() {
        baseSentimientos = getString(R.string.pielAñadir);
        baseProductos = getString(R.string.productosAñadir);
        baseAlimentos = getString(R.string.comidaAñadir);

        bienvenido = findViewById(R.id.txtBienvenidoM);

        txSentimientosAñadidos = findViewById(R.id.lbSentimientosAñadidos);
        txProductosUtilizadosAñadidos = findViewById(R.id.lbProductosAñadidos);
        txAlimentosAñadidos = findViewById(R.id.lbAlimentosAñadidos);

        calendario = findViewById(R.id.idCalendario);
        imagenSelfie = findViewById(R.id.idImagenSelfie);
        imVasoAgua = findViewById(R.id.imgVasoAgua);

        opcionSentimientoPiel = findViewById(R.id.spSentimientosPiel);

        etProductosUtilizados = findViewById(R.id.etProductosUsados);
        etAlimentosConsumidos = findViewById(R.id.etAñadirAlimentos);

        btSelfie = findViewById(R.id.btSelfie);

    }

    public void pedirPermisosCamara(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // El permiso no está concedido, se solicita
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISOS_CAMARA);
        } else {
            // Ya tiene el permiso
            abrirCamara();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISOS_CAMARA) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) { //comprobar si usuario a aceptado los permisos
                abrirCamara();
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void abrirCamara() {
        Toast.makeText(this, "Permiso de cámara aceptado", Toast.LENGTH_SHORT).show();
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, ALMACENAR_CODIGO_CAMARA);
        } catch (ActivityNotFoundException e) {
            // Manejar el caso en que no hay aplicación de cámara disponible
            Toast.makeText(this, "No se encontró una aplicación de cámara", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == ALMACENAR_CODIGO_CAMARA && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            if (imageBitmap != null) {
                byte[] imagenBytes = convertirImagenEnByte(imageBitmap);

                SeguimientoDAO seguimientoDAO = new SeguimientoDAO();
                int idSeguimiento = seguimientoDAO.obtenerOInsertarSeguimiento(idUsuario, fechaSeleccionada);

                seguimientoDAO.guardarSelfieEnSeguimiento(idSeguimiento, imagenBytes);

                Bitmap bitmap = seguimientoDAO.obtenerSelfiePorFecha(idUsuario, fechaSeleccionada);

                imagenSelfie.setImageBitmap(bitmap);

            }
        }
    }


    public byte[] convertirImagenEnByte(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
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

        if (!sentimientoSeleccionado.equals("Elige un sentimiento")) {
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
        SeguimientoDAO seguimientoDAO = new SeguimientoDAO();
        SeguimientoDAO.DatosSeguimiento datos = seguimientoDAO.obtenerDatosDeSeguimiento(idUsuario, fechaSeleccionada);

        txSentimientosAñadidos.setText(baseSentimientos + String.join(", ", datos.sentimientos));
        txAlimentosAñadidos.setText(baseAlimentos + String.join(", ", datos.alimentos));
        txProductosUtilizadosAñadidos.setText(baseProductos + String.join(", ", datos.productos));

        if (datos.agua > 0) {
            for (int i = 0; i < cantidades.length; i++) {
                if (datos.agua == cantidades[i]) {
                    indiceVasoActual = i;
                    break;
                }
            }
        } else {
            indiceVasoActual = 0;
        }

        imVasoAgua.setImageResource(imagenesVasos[indiceVasoActual]);
        cantidadSeleccionada = cantidades[indiceVasoActual];


        Bitmap bitmap = seguimientoDAO.obtenerSelfiePorFecha(idUsuario, fechaSeleccionada);

        if(bitmap != null){
            imagenSelfie.setImageBitmap(bitmap);
        }
        else{
            imagenSelfie.setImageResource(R.drawable.selfiegato);
        }

    }

    public void sumarAgua(View view) {
        if (indiceVasoActual < imagenesVasos.length - 1) {
            indiceVasoActual++;
            actualizarImagenYCantidad();
        }
    }

    public void restarAgua(View view) {
        if (indiceVasoActual > 0) {
            indiceVasoActual--;
            actualizarImagenYCantidad();
        }
    }

    private void actualizarImagenYCantidad() {
        imVasoAgua.setImageResource(imagenesVasos[indiceVasoActual]);
        cantidadSeleccionada = cantidades[indiceVasoActual];

        SeguimientoDAO seguimientoDAO = new SeguimientoDAO();
        int idSeguimientoHoy = seguimientoDAO.obtenerOInsertarSeguimiento(idUsuario, fechaSeleccionada);

        if (idSeguimientoHoy != -1) {
            seguimientoDAO.actualizarAguaConsumida(idSeguimientoHoy, cantidadSeleccionada);
        } else {
            System.err.println("Error al obtener o insertar el seguimiento.");
        }
    }

    public void borrarSelfie(View view) {
        SeguimientoDAO seguimientoDAO = new SeguimientoDAO();
        int idSeguimiento = seguimientoDAO.obtenerOInsertarSeguimiento(idUsuario, fechaSeleccionada);

        seguimientoDAO.borrarSelfie(idSeguimiento);
        imagenSelfie.setImageResource(R.drawable.selfiegato);
        Toast.makeText(this, "Selfie eliminada", Toast.LENGTH_SHORT).show();
    }

    public void borrarSentimientos(View view) {
        txSentimientosAñadidos.setText(baseSentimientos);

        SeguimientoDAO seguimientoDAO = new SeguimientoDAO();
        int idSeguimiento = seguimientoDAO.obtenerOInsertarSeguimiento(idUsuario, fechaSeleccionada);
        seguimientoDAO.borrarSentimientos(idSeguimiento);

        Toast.makeText(this, "Sentimientos eliminados", Toast.LENGTH_SHORT).show();
    }

    public void borrarProductos(View view) {
        txProductosUtilizadosAñadidos.setText(baseProductos);

        SeguimientoDAO seguimientoDAO = new SeguimientoDAO();
        int idSeguimiento = seguimientoDAO.obtenerOInsertarSeguimiento(idUsuario, fechaSeleccionada);
        seguimientoDAO.borrarProductos(idSeguimiento);

        Toast.makeText(this, "Productos eliminados", Toast.LENGTH_SHORT).show();
    }

    public void borrarAlimentos(View view) {
        txAlimentosAñadidos.setText(baseAlimentos);

        SeguimientoDAO seguimientoDAO = new SeguimientoDAO();
        int idSeguimiento = seguimientoDAO.obtenerOInsertarSeguimiento(idUsuario, fechaSeleccionada);
        seguimientoDAO.borrarAlimentos(idSeguimiento);

        Toast.makeText(this, "Alimentos eliminados", Toast.LENGTH_SHORT).show();
    }



}
