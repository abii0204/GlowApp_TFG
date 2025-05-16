package com.example.glowapp_tfg.dao;

import com.example.glowapp_tfg.conexion.ConexionBBDD;
import com.example.glowapp_tfg.modelos.UsuarioModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

            return resultSet.next();

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
        return false;
    }

    public UsuarioModel comprobarUsuario(String email, String contrasena) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = conectarBD();
            if (connection == null) {
                System.err.println("Error al conectar con la base de datos");
                return null;
            }

            String query = "SELECT * FROM usuarios WHERE email = ? AND contraseña_hash = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, contrasena);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new UsuarioModel(
                        resultSet.getInt("id"),
                        resultSet.getString("nombre"),
                        resultSet.getString("email"),
                        resultSet.getString("contraseña_hash"),
                        resultSet.getString("tipo_piel")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al validar el usuario: " + e.getMessage());
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

        return null;
    }

    public ArrayList<String> obtenerSentimientos() {
        ArrayList<String> sentimientos = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = conectarBD();
            if (connection == null) return sentimientos;

            String query = "SELECT nombre FROM sentimientos ORDER BY nombre ASC";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                sentimientos.add(resultSet.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return sentimientos;
    }


    public ArrayList<String> obtenerOpcionesAgua() {
        ArrayList<String> opciones = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = conectarBD();
            if (connection == null) return opciones;

            String query = "SELECT cantidad_ml FROM cantidades_agua ORDER BY cantidad_ml ASC";

            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                opciones.add(resultSet.getInt("cantidad_ml") + " ml");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return opciones;
    }




}