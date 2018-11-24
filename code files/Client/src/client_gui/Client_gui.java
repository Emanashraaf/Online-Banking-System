package client_gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client_gui extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
    static public DataOutputStream dos = null;
    static public DataInputStream dis = null;
    static public Socket c;
    public static void main(String[] args) 
    {  
        ResourceBundle rb = ResourceBundle.getBundle("client_config");
        String ServerIP = rb.getString("ServerIP");
        String PortNumber = rb.getString("PortNumber");
       
         try 
        {
            // Connect to server (create socket)
            c = new Socket(ServerIP,Integer.parseInt(PortNumber));
            dos = new DataOutputStream(c.getOutputStream());
            dis = new DataInputStream(c.getInputStream());
            
            // Client invokes server
            dos.writeUTF("Hello");            
            // Login or Create account
            if (dis.readUTF().equalsIgnoreCase("Hello"));
                launch(args);
            
        }
         
         catch (IOException ex) 
        {
            System.out.println(ex);
        }
        
                 
    }
    
}
