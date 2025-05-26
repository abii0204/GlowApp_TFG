package com.example.glowapp_tfg.dao;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.glowapp_tfg.conexion.ConexionBBDD;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SeguimientoDAO extends ConexionBBDD {

    public void guardarSelfieEnSeguimiento(int idSeguimiento, byte[] imagenBytes) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = conectarBD();

            String sql = "UPDATE seguimiento SET selfie = ? WHERE id = ?";
            statement = connection.prepareStatement(sql);
            statement.setBytes(1, imagenBytes);
            statement.setInt(2, idSeguimiento);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al guardar la selfie: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    public Bitmap obtenerSelfiePorFecha(int idUsuario, String fecha) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = conectarBD();
            String sql = "SELECT selfie FROM seguimiento WHERE usuario_id = ? AND fecha = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, idUsuario);
            statement.setDate(2, Date.valueOf(fecha));
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                byte[] imagenBytes = resultSet.getBytes("selfie");
                if (imagenBytes != null) {
                    return BitmapFactory.decodeByteArray(imagenBytes, 0, imagenBytes.length);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al recuperar la selfie: " + e.getMessage());
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

        return null;
    }



    public void insertarSentimientoEnSeguimiento(int idSeguimiento, int idUsuario, String nombreSentimiento) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = conectarBD();

            String queryId = "SELECT id FROM sentimientos WHERE nombre = ?";
            preparedStatement = connection.prepareStatement(queryId);
            preparedStatement.setString(1, nombreSentimiento);
            resultSet = preparedStatement.executeQuery();

            int idSentimiento = -1;
            if (resultSet.next()) {
                idSentimiento = resultSet.getInt("id");
            } else {
                System.err.println("Sentimiento no encontrado en la base de datos.");
                return;
            }

            resultSet.close();
            preparedStatement.close();


            String insertQuery = "INSERT INTO sentimientos_seguimiento (id_seguimiento, id_sentimiento) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setInt(1, idSeguimiento);
            preparedStatement.setInt(2, idSentimiento);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar el sentimiento en el seguimiento: " + e.getMessage());
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
    }


    public int obtenerOInsertarSeguimiento(int idUsuario, String fecha) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = conectarBD();

            String consulta = "SELECT id FROM seguimiento WHERE usuario_id = ? AND fecha = ?";
            statement = connection.prepareStatement(consulta);
            statement.setInt(1, idUsuario);
            statement.setDate(2, Date.valueOf(fecha));
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }

            String insertar = "INSERT INTO seguimiento (usuario_id, fecha) VALUES (?, ?) RETURNING id";
            statement.close();
            resultSet.close();
            statement = connection.prepareStatement(insertar);
            statement.setInt(1, idUsuario);
            statement.setDate(2, java.sql.Date.valueOf(fecha));
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
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

        return -1;
    }

    public void actualizarAguaConsumida(int idSeguimiento, int cantidad) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = conectarBD();

            String query = "UPDATE agua_consumida SET cantidad_ml = ? WHERE seguimiento_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, cantidad);
            preparedStatement.setInt(2, idSeguimiento);

            int filasAfectadas = preparedStatement.executeUpdate();

            // Si no existe el registro aún, lo insertamos
            if (filasAfectadas == 0) {
                preparedStatement.close();
                query = "INSERT INTO agua_consumida (seguimiento_id, cantidad_ml) VALUES (?, ?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, idSeguimiento);
                preparedStatement.setInt(2, cantidad);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println("Error al actualizar/insertar agua consumida: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void insertarAlimentoEnSeguimiento(int idSeguimiento, String alimento) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = conectarBD();

            String sql = "INSERT INTO alimentacion (seguimiento_id, alimento) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idSeguimiento);
            preparedStatement.setString(2, alimento);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar alimento en el seguimiento: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void insertarProductoUsado(int idSeguimiento, String producto)  {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = conectarBD();
            String sql = "INSERT INTO productos_usados (seguimiento_id, producto) VALUES (?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, idSeguimiento);
            statement.setString(2, producto);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            if (statement != null) {
                try {
                    statement.close();
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    public class DatosSeguimiento {
        public ArrayList<String> sentimientos = new ArrayList<>();
        public ArrayList<String> alimentos = new ArrayList<>();
        public ArrayList<String> productos = new ArrayList<>();
        public String selfie;
        public int agua = 0;
    }

    public DatosSeguimiento obtenerDatosDeSeguimiento(int idUsuario, String fecha) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        DatosSeguimiento datos = new DatosSeguimiento();

        try {
            connection = conectarBD();

            // Obtener el id del seguimiento
            String consultaId = "SELECT id FROM seguimiento WHERE usuario_id = ? AND fecha = ?";
            statement = connection.prepareStatement(consultaId);
            statement.setInt(1, idUsuario);
            statement.setDate(2, Date.valueOf(fecha));
            resultSet = statement.executeQuery();

            int idSeguimiento = -1;
            if (resultSet.next()) {
                idSeguimiento = resultSet.getInt("id");
            } else {
                return datos; // No hay seguimiento ese día
            }

            resultSet.close();
            statement.close();

            // Sentimientos
            String sqlSentimientos = "SELECT s.nombre FROM sentimientos_seguimiento ss JOIN sentimientos s ON ss.id_sentimiento = s.id WHERE ss.id_seguimiento = ?";
            statement = connection.prepareStatement(sqlSentimientos);
            statement.setInt(1, idSeguimiento);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                datos.sentimientos.add(resultSet.getString("nombre"));
            }
            resultSet.close();
            statement.close();

            // Alimentos
            String sqlAlimentos = "SELECT alimento FROM alimentacion WHERE seguimiento_id = ?";
            statement = connection.prepareStatement(sqlAlimentos);
            statement.setInt(1, idSeguimiento);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                datos.alimentos.add(resultSet.getString("alimento"));
            }
            resultSet.close();
            statement.close();

            // Productos
            String sqlProductos = "SELECT producto FROM productos_usados WHERE seguimiento_id = ?";
            statement = connection.prepareStatement(sqlProductos);
            statement.setInt(1, idSeguimiento);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                datos.productos.add(resultSet.getString("producto"));
            }
            resultSet.close();
            statement.close();

            // Agua
            String sqlAgua = "SELECT SUM(cantidad_ml) AS total FROM agua_consumida WHERE seguimiento_id = ?";
            statement = connection.prepareStatement(sqlAgua);
            statement.setInt(1, idSeguimiento);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                datos.agua = resultSet.getInt("total");
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

        return datos;
    }

    public void borrarSelfie(int idSeguimiento) {
        Connection connection = conectarBD();
        if (connection != null) {
            try {
                PreparedStatement stmt = connection.prepareStatement(
                        "UPDATE seguimiento SET selfie = NULL WHERE id = ?");
                stmt.setInt(1, idSeguimiento);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void borrarSentimientos(int idSeguimiento) {
        Connection connection = conectarBD();
        if (connection != null) {
            try {
                PreparedStatement stmt = connection.prepareStatement(
                        "DELETE FROM sentimientos_seguimiento WHERE id_seguimiento = ?");
                stmt.setInt(1, idSeguimiento);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void borrarProductos(int idSeguimiento) {
        Connection connection = conectarBD();
        if (connection != null) {
            try {
                PreparedStatement stmt = connection.prepareStatement(
                        "DELETE FROM seguimiento_productos WHERE seguimiento_id = ?");
                stmt.setInt(1, idSeguimiento);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void borrarAlimentos(int idSeguimiento) {
        Connection connection = conectarBD();
        if (connection != null) {
            try {
                PreparedStatement stmt = connection.prepareStatement(
                        "DELETE FROM seguimiento_alimentos WHERE seguimiento_id = ?");
                stmt.setInt(1, idSeguimiento);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



}
