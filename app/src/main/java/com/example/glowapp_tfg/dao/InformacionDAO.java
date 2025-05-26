package com.example.glowapp_tfg.dao;

import android.util.Log;

import com.example.glowapp_tfg.conexion.ConexionBBDD;
import com.example.glowapp_tfg.modelos.InformacionModel;
import com.example.glowapp_tfg.modelos.SubseccionModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class InformacionDAO extends ConexionBBDD {

    public ArrayList<InformacionModel> obtenerInformacionPorCategoria(String categoria) {
        ArrayList<InformacionModel> lista = new ArrayList<>();
        String sql = "SELECT titulo, subtitulo, contenido FROM informacion WHERE categoria = ? ORDER BY id";

        try (Connection conn = conectarBD();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, categoria);
            ResultSet rs = ps.executeQuery();

            HashMap<String, InformacionModel> mapa = new HashMap<>();

            while (rs.next()) {
                String titulo = rs.getString("titulo");
                String subtitulo = rs.getString("subtitulo");
                String contenido = rs.getString("contenido");

                SubseccionModel sub = new SubseccionModel();
                sub.setSubtitulo(subtitulo);
                sub.setContenido(contenido);

                if (!mapa.containsKey(titulo)) {
                    InformacionModel info = new InformacionModel();
                    info.setTitulo(titulo);
                    info.setSubsecciones(new ArrayList<>());
                    mapa.put(titulo, info);
                }

                mapa.get(titulo).getSubsecciones().add(sub);
            }

            lista.addAll(mapa.values());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    public String obtenerTipoPielUsuario(int idUsuario) {
        String tipoPiel = "";
        String sql = "SELECT tipo_piel FROM usuarios WHERE id = ?";

        try (Connection conn = conectarBD();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tipoPiel = rs.getString("tipo_piel");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tipoPiel;
    }
}
