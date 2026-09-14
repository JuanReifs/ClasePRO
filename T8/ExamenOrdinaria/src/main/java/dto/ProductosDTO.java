package dto;
import com.mysql.cj.jdbc.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;

import database.ConexionDB;
import model.Productos;


public class ProductosDTO {

    private Productos productos;
    public ProductosDTO() {
        Productos productos = new Productos();
    }

    private Connection connection;

    public void realizarInsercionPrepare(){
        try {
            if (!connection.isClosed()){
                String query = "INSERT INTO productos (nombre,precio,cantidad) " +
                        "VALUES (?,?,?)";
                PreparedStatement pStatement = connection.prepareStatement(query);

                pStatement.setString(1,productos.getNombre());
                pStatement.setDouble(2,productos.getPrecio());
                pStatement.setInt(3,productos.getCantidad());
                pStatement.execute();

                pStatement.close();
                pStatement.getConnection().close();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void realizarBorradoPrepare(){
        try {
            if (!connection.isClosed()){
                String query = "DELETE FROM productos" +
                        " WHERE id = ?";
                PreparedStatement pStatement = connection.prepareStatement(query);
                pStatement.setLong(1,productos.getId());
                int filasAfectadas = pStatement.executeUpdate();
                System.out.println("Las filas que se han borrado han sido "+filasAfectadas);
                pStatement.close();
                pStatement.getConnection().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mostrarProductos(){
        try {
            if (!connection.isClosed()){
                String query = "SELECT * FROM productos" +
                        " WHERE id = ?";
                PreparedStatement pStatement = connection.prepareStatement(query);
                pStatement.setLong(1,productos.getId());
                int filasAfectadas = pStatement.executeUpdate();
                System.out.println(filasAfectadas);
                pStatement.close();
                pStatement.getConnection().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void mostrarALLProductos(){
        try {
            if (!connection.isClosed()){
                String query = "SELECT * FROM productos";
                PreparedStatement pStatement = connection.prepareStatement(query);
                int filasAfectadas = pStatement.executeUpdate();
                System.out.println(filasAfectadas);
                pStatement.close();
                pStatement.getConnection().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
/*
    public void realizarActualizacion() {
        try {
            if (!connection.isClosed()){
                Statement statement = connection.createStatement();

                String query = "UPDATE productos SET nombre = '%s'" +
                        " WHERE nombre = '%s'";
                int filasAfectadas = statement.executeUpdate(String.format(query));
                System.out.println("Las filas que se han actualizado han sido "+filasAfectadas);
                statement.close();
                statement.getConnection().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
*/
}


