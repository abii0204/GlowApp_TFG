package com.example.glowapp_tfg.dao;

import com.example.glowapp_tfg.conexion.ConexionBBDD;
import com.example.glowapp_tfg.modelos.UsuarioModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO extends ConexionBBDD {

    public boolean insertarUsuario(UsuarioModel usuario) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = conectarBD();
            if (connection == null) {
                System.err.println("Error al conectar con la base de datos");
                return false;
            }

            String query = "INSERT INTO usuarios (nombre, email, contraseña_hash, tipo_piel) VALUES (?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, usuario.getNombre());
            preparedStatement.setString(2, usuario.getCorreo());
            preparedStatement.setString(3, usuario.getContrasena());
            preparedStatement.setString(4, usuario.getTipo_piel());

            int filasInsertadas = preparedStatement.executeUpdate();
            if (filasInsertadas > 0) {
                System.out.println("Usuario insertado correctamente.");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar el usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
        return false;
    }
    public boolean existeEmail(String email) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean existe = false;

        try {
            connection = conectarBD();
            if (connection == null) {
                System.err.println("Error al conectar con la base de datos");
                return false;
            }

            String query = "SELECT 1 FROM usuarios WHERE email = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            resultSet = preparedStatement.executeQuery();
            existe = resultSet.next();

        } catch (SQLException e) {
            System.err.println("Error al verificar el email: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }

        return existe;
    }

}