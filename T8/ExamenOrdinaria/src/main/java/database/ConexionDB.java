package database;


import java.sql.*;
import com.mysql.jdbc.*;
public class ConexionDB {

    private Connection connection;

    public ConexionDB() {
        realizarConexion();
    }

    private void realizarConexion(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String user="root";
            String pass="root";
            String host="192.168.64.2:3306";
            String dbName= "inicial";
            String url= "jdbc:mysql://"+host+"/"+dbName;
            connection = DriverManager.getConnection(url,user,pass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void llamarConexion(){
        System.out.println(connection.toString());
    };
}
