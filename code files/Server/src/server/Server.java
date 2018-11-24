package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;
//import java.util.Scanner;

public class Server 
{
    static ResourceBundle rb = ResourceBundle.getBundle("server_config"); 
    static public Database db;
    static public int Session_ID = 0;
    
    public static void main(String[] args) 
    {      
        String portNumber = rb.getString("PortNumber");
        int PN = Integer.parseInt(portNumber);
       /* Scanner sc = new Scanner(System.in);
        int pn = sc.nextInt();*/
        
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
