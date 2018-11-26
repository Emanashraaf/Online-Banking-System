package server;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server 
{
    static public Database db;
    static public int Session_ID = 0;  
    static File config = new File("Server_config.txt");
    static Scanner sc = null;
   
    public static void main(String[] args) 
    {
        try 
        {  
            sc = new Scanner(config);
        } 
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        String portNumber = sc.nextLine();
        int PN = Integer.parseInt(portNumber);
        /* Scanner sc = new Scanner(System.in);
        int pn = sc.nextInt();*/
        System.out.println(PN);
        // Create Server Socket
        ServerSocket s = null ;
        try 
        {
            s = new ServerSocket(PN);
        }
        catch (IOException ex) 
        {
             System.out.println(ex);
        }        
        // Connect to database
        db = new Database (PN);
        
        while (true)
        {
            Socket c = null;
            try 
            {
                c = s.accept();
            } 
            catch (IOException ex) 
            {
                 System.out.println(ex);
            }
            System.out.println("Client Arrived");
            clientHandler ch = new clientHandler(c);
            Thread t = new Thread(ch);
            t.start();         
        }     
    }
    
}
