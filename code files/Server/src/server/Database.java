package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Database 
{
    public Statement stmt;
    Database(int portNumber)
    {      
        String conn_string = "jdbc:mysql://localhost/banking_system_" + String.valueOf(portNumber);
        String username = "root";
        String password = "";
        
        try 
        {
            Connection conn = DriverManager.getConnection(conn_string, username, password);
            stmt = conn.createStatement();
        }
        
        catch (SQLException ex) 
        {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}
