package sample;

import org.postgresql.Driver;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCDataBase {
    private static final String DATABASE_URL="jdbs:postgresql://localhost:5432";
    private static final String DATABASE_USERNAME="FirstDB";
    private static final String DATABASE_PASSWORD="1234";
    private static final String SELECT_QUERY = "SELECT * FROM askers WHERE username = ? AND password = ?";
    private static Connection connection;



    public static Connection getConnect (){
        try {
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
        } catch (SQLException ex) {
            //Logger.getLogger(JDBCDataBase.class.getName()).log(Level.SEVERE, null, ex);
        }

        return  connection;
    }

    public boolean validate(String username, String password){
        // 1: establishing the connection
       try {
           Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
           // 2: creating statement using connection object
           PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
           preparedStatement.setString(1, username);
           preparedStatement.setString(2, password);

           System.out.println(preparedStatement);

           ResultSet resultSet = preparedStatement.executeQuery();
           if(resultSet.next()){
               return true;
           }

       }catch (SQLException e){
           // print SQLExeption
           printSQLExeption(e);
       }
       return false;
    }
    public static void printSQLExeption(SQLException ex){
        for(Throwable e: ex){
            if(ex instanceof SQLException){
                e.printStackTrace(System.err);
                System.err.println("SQLState:" + ((SQLException)e).getSQLState());
                System.err.println("Error Code:" + ((SQLException)e).getErrorCode());
                System.err.println("Message:" + ex.getMessage());
                Throwable t = ex.getCause();
                while(t != null){
                    System.out.println("Cause: "+t);
                    t = t.getCause();
                }
            }
        }
    }
}
