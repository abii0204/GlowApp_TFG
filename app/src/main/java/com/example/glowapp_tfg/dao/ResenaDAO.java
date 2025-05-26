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
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = conectarBD();
            String sql = "SELECT r.*, u.nombre AS nombre_usuario " +
                    "FROM reseñas r " +
                    "JOIN usuarios u ON r.usuario_id = u.id " +
                    "ORDER BY r.fecha DESC";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                int usuarioId = rs.getInt("usuario_id");
                String nombreUsuario = rs.getString("nombre_usuario");
                String producto = rs.getString("producto");
                String marca = rs.getString("marca");
                String tipoPiel = rs.getString("tipo_piel");
                String comentario = rs.getString("comentario");
                int puntuacion = rs.getInt("puntuacion");
                String fecha = rs.getString("fecha"); // Asegúrate de que la columna existe

                ResenaModel resena = new ResenaModel(usuarioId, nombreUsuario, producto, marca, tipoPiel, comentario, puntuacion, fecha);
                lista.add(resena);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return lista;
    }

    public ArrayList<ResenaModel> buscarPorTipoPiel(String tipoPiel) {
        ArrayList<ResenaModel> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT r.*, u.nombre AS nombre_usuario FROM reseñas r " +
                "JOIN usuarios u ON r.usuario_id = u.id " +
                "WHERE r.tipo_piel ILIKE ?";

        try {
            conn = conectarBD();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + tipoPiel + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                ResenaModel resena = new ResenaModel();
                resena.setIdUsuario(rs.getInt("usuario_id"));
                resena.setNombreUsuario(rs.getString("nombre_usuario"));
                resena.setProducto(rs.getString("producto"));
                resena.setMarca(rs.getString("marca"));
                resena.setTipoPiel(rs.getString("tipo_piel"));
                resena.setComentario(rs.getString("comentario"));
                resena.setPuntuacion(rs.getInt("puntuacion"));
                resena.setFecha(rs.getString("fecha"));
                lista.add(resena);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lista;
    }


    public ArrayList<ResenaModel> buscarPorProducto(String producto) {
        ArrayList<ResenaModel> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT r.*, u.nombre AS nombre_usuario FROM reseñas r " +
                "JOIN usuarios u ON r.usuario_id = u.id " +
                "WHERE r.producto ILIKE ?";

        try {
            conn = conectarBD();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + producto + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                ResenaModel resena = new ResenaModel();
                resena.setIdUsuario(rs.getInt("usuario_id"));
                resena.setNombreUsuario(rs.getString("nombre_usuario"));
                resena.setProducto(rs.getString("producto"));
                resena.setMarca(rs.getString("marca"));
                resena.setTipoPiel(rs.getString("tipo_piel"));
                resena.setComentario(rs.getString("comentario"));
                resena.setPuntuacion(rs.getInt("puntuacion"));
                resena.setFecha(rs.getString("fecha"));
                lista.add(resena);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lista;
    }


    public ArrayList<ResenaModel> buscarPorMarca(String marca) {
        ArrayList<ResenaModel> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT r.*, u.nombre AS nombre_usuario FROM reseñas r " +
                "JOIN usuarios u ON r.usuario_id = u.id " +
                "WHERE r.marca ILIKE ?";

        try {
            conn = conectarBD();
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + marca + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                ResenaModel resena = new ResenaModel();
                resena.setIdUsuario(rs.getInt("usuario_id"));
                resena.setNombreUsuario(rs.getString("nombre_usuario"));
                resena.setProducto(rs.getString("producto"));
                resena.setMarca(rs.getString("marca"));
                resena.setTipoPiel(rs.getString("tipo_piel"));
                resena.setComentario(rs.getString("comentario"));
                resena.setPuntuacion(rs.getInt("puntuacion"));
                resena.setFecha(rs.getString("fecha"));
                lista.add(resena);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return lista;
    }




}
