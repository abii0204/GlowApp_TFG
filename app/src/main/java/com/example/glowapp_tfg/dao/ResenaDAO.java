package com.example.glowapp_tfg.dao;

import com.example.glowapp_tfg.conexion.ConexionBBDD;
import com.example.glowapp_tfg.modelos.ResenaModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ResenaDAO extends ConexionBBDD {

    public boolean insertarResena(ResenaModel resena) {
        Connection con = null;
        PreparedStatement ps = null;

        String sql = "INSERT INTO reseñas (usuario_id, producto, marca, tipo_piel, comentario, puntuacion) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            con = conectarBD();
            ps = con.prepareStatement(sql);
            ps.setInt(1, resena.getIdUsuario());
            ps.setString(2, resena.getProducto());
            ps.setString(3, resena.getMarca());
            ps.setString(4, resena.getTipoPiel());
            ps.setString(5, resena.getComentario());
            ps.setInt(6, resena.getPuntuacion());

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<ResenaModel> obtenerTodasLasResenas() {
        ArrayList<ResenaModel> lista = new ArrayList<>();

        try (Connection conn = conectarBD();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM reseñas ORDER BY fecha DESC")) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ResenaModel resena = new ResenaModel(
                        rs.getInt("usuario_id"),
                        rs.getString("producto"),
                        rs.getString("marca"),
                        rs.getString("tipo_piel"),
                        rs.getString("comentario"),
                        rs.getInt("puntuacion")
                );
                lista.add(resena);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}
