package client_gui;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    static File config = new File("Client_config.txt");
    static Scanner sc = null;
    static public DataOutputStream dos = null;
    static public DataInputStream dis = null;
    static public Socket c;
    public static void main(String[] args) 
    {  
        try 
        {
            sc = new Scanner(config);
        } catch (FileNotFoundException ex) 
        {
            Logger.getLogger(Client_gui.class.getName()).log(Level.SEVERE, null, ex);
        }

        String ServerIP = sc.nextLine();
        String PortNumber = sc.nextLine();
       
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
            System.out.println("something went wrong");
        }
        
                 
    }
    
}
