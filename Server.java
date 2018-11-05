package banking.system_ds;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Server 
{  
    static int CreateAccount(Socket c)
    {
        String fullName = "" , password = "";
        int amount, ID = 0;
        try 
        {
            DataOutputStream dos = new DataOutputStream(c.getOutputStream());
            DataInputStream dis = new DataInputStream(c.getInputStream());        
            //get user info
            dos.writeUTF("Enter your full name:");
            fullName = dis.readUTF();
            dos.writeUTF("Enter your password:");
            password = dis.readUTF();                    
            dos.writeUTF("Reenter your password:");
            if(!dis.readUTF().equals(password))
            {
                //do something about it
            }
            dos.writeUTF("Enter initial amount of money:");
            amount = dis.readInt();
            if(amount<0)
            {
                //do something about it
            }           
            //Encrypt password
            
            //save to data base
            boolean error = stmt.execute("INSERT INTO client (full_name, password,balance) VALUES ('"+fullName+"','"+password+"','"+amount+"')");
            if(!error)
            {
                rs = stmt.executeQuery("SELECT MAX(ID) FROM client");
                rs.next();
                ID = rs.getInt(1);
            }            
            else
            {
                //do something about it
            }
        } 
        catch (Exception e) {}       
        return ID;
    }
    
    static int Session_ID = 0;
    static void Login(Socket c)
    {       
        try {
            DataOutputStream dos = new DataOutputStream(c.getOutputStream());
            DataInputStream dis = new DataInputStream(c.getInputStream());
            dos.writeUTF("Enter your ID");
            String IDReadFromUser = dis.readUTF();
            dos.writeUTF("Enter your password:");
            String password = dis.readUTF();
            
            //check database for login
            rs = stmt.executeQuery(" SELECT ID FROM client WHERE ID = '"+IDReadFromUser+"' AND password = '"+password+"'");
            rs.next();
            Session_ID = rs.getInt(1);
            System.out.println(Session_ID);
                                  
            //return 0 if not found
            //return ID if found                                 
        } 
        catch (Exception e) {}              
    }   
    
    static int ShowAccountInfo(Socket c)
    {
        int balance = 0;
        try 
        {
            //search in database by ID and return balance
            rs = stmt.executeQuery(" SELECT balance FROM client WHERE ID = '"+Session_ID+"'");
            rs.next();
            balance = rs.getInt(1);
        } 
        catch (SQLException ex){}
        return balance;
    }
    
    static int Deposit(Socket c)
    {
        int balance = 0;
        try {
            DataOutputStream dos = new DataOutputStream(c.getOutputStream());
            DataInputStream dis = new DataInputStream(c.getInputStream());
            dos.writeUTF("Enter the amount of money to Deposit:");
            int amount = dis.readInt();
            if(amount<0)
            {
                //do something
            }
            else
            {
                //update the database with balance+=amount;
                boolean error = stmt.execute("UPDATE client SET balance = balance+ '"+amount+"' WHERE ID = '"+Session_ID+"' ");
                if(!error)
                {
                    String transaction = "Deposit '"+amount+"' LE";  
                    stmt.execute("INSERT INTO history (client_id,transaction) VALUES ('"+Session_ID+"','"+transaction+"')");
                    rs = stmt.executeQuery("SELECT balance FROM client WHERE ID = '"+Session_ID+"'");
                    rs.next();
                    balance = rs.getInt(1);                                    
                }
            }
        } 
        catch (Exception e) {}        
        return balance;
    }
    
    static int Withdraw(Socket c){
        int balance = 0;
        try {
            DataOutputStream dos = new DataOutputStream(c.getOutputStream());
            DataInputStream dis = new DataInputStream(c.getInputStream());
            dos.writeUTF("Enter the amount of money to Withdraw:");
            int amount = dis.readInt();
            if(amount<0)
            {
                //add condition to check if the amount is more than the initial balance
                //do something
            }
            else
            {
                //update the database with balance-=amount;
                boolean error = stmt.execute("UPDATE client SET balance = balance - '"+amount+"' WHERE ID = '"+Session_ID+"' ");
                if(!error)
                {
                    rs = stmt.executeQuery("SELECT balance FROM client WHERE ID = '"+Session_ID+"'");
                    rs.next();
                    balance = rs.getInt(1);
                }
                
            }
        } 
        catch (Exception e) {}
        return balance;
    }
    
    static int TransferToAnotherAccount(Socket c)
    {
        int balance = 0;
        boolean error;
        try {
            DataOutputStream dos = new DataOutputStream(c.getOutputStream());
            DataInputStream dis = new DataInputStream(c.getInputStream());
            dos.writeUTF("Enter the account ID you want to Transfer to:");
            String IDForTheTargetAccount = dis.readUTF();
            //check the data base for the target ID
            
            rs = stmt.executeQuery(" SELECT ID FROM client WHERE ID = '"+IDForTheTargetAccount+"'");
            
            if(rs.next())
            {
                dos.writeUTF("Enter the amount of money to Transfer to " + IDForTheTargetAccount + ":");
                int amount = dis.readInt();
                if(amount<0)
                {
                    //do something
                }
                else
                {
                    //check if the amount is more than the balance
                    rs = stmt.executeQuery("SELECT balance FROM client WHERE ID = '"+Session_ID+"'");
                    rs.next();
                    balance = rs.getInt(1);
                    
                    if (balance >= amount)
                    {
                        //update target data base with target balance+=amount;
                        error = stmt.execute("UPDATE client SET balance = balance+ '"+amount+"' WHERE ID = '"+IDForTheTargetAccount+"' ");
                        //update self data base with balance-=amount;
                        if(!error)
                            error = stmt.execute("UPDATE client SET balance = balance - '"+amount+"' WHERE ID = '"+Session_ID+"' ");

                        if(!error)
                        {
                            rs = stmt.executeQuery("SELECT balance FROM client WHERE ID = '"+Session_ID+"'");
                            rs.next();
                            balance = rs.getInt(1);
                        }
                    }
                    
                    else
                    {
                        // do something
                    }
                }
            }
            else
            {
                //do something
            }    
        } 
        catch (Exception e) {}
        return balance;
    } 
    
    static int TransferToAnotherAccountInAnotherBank(Socket c, int ID){
        int balance = 0;
        try {
            DataOutputStream dos = new DataOutputStream(c.getOutputStream());
            DataInputStream dis = new DataInputStream(c.getInputStream());
            dos.writeUTF("Enter the bank ID you want to Transfer to:");
            String IP = dis.readUTF();
            //establish connection with another server with the
            //IP address mentioned above
            Socket peepToPeerSocket = new Socket("127.0.0.1", Integer.valueOf(IP));
            //System.out.println("server.Server.TransferToAnotherAccountInAnotherBank()" + IP);
            DataOutputStream dosPTP = new DataOutputStream(peepToPeerSocket.getOutputStream());
            DataInputStream disPTP = new DataInputStream(peepToPeerSocket.getInputStream());
            dosPTP.writeUTF("I'm another server");
            //connection established *Dap meme*
            //list of options
            System.out.println(disPTP.readUTF());
            //choosing option 1, transfer
            dosPTP.writeChar('1');
            
            
            if(/*target bank exists*/ true){
                dos.writeUTF("Enter the account ID you want to Transfer to:");
                String IDForTheTargetAccount = dis.readUTF();
                //sending ID of target to target bank(server)
                dosPTP.writeUTF(IDForTheTargetAccount);
                //reading boolean if found or not
                boolean targetIDFound = disPTP.readBoolean();
                
                if(targetIDFound){
                    dos.writeUTF("Enter the amount of money to Transfer to:" + IDForTheTargetAccount);
                    int amount = dis.readInt();
                    if(amount<0){//add condition to check if the amount is more than the initial balance
                        //do something
                    }
                    else{
                        //send the amount of money to the other bank
                        dosPTP.writeInt(amount);
                        //update self data base with balance-=amount;
                    }
                    System.out.println(amount);
                }else{
                    //do something
                }    
            }
            
        } 
        catch (Exception e) {}
        return balance;
    }
    
    static String ShowHistory(Socket c, int ID){
        String History = "This is the transaction history";
        //search the data base by ID and return the history formatted for 
        //display with no further formatting needed
        return History;
    }
    
    static String username = "root";
    static String password = "";
    static String conn_string = "jdbc:mysql://localhost/banking_system";
    static Connection conn;
    static Statement stmt;
    static ResultSet rs;
    
    public static void main(String[] args) throws SQLException 
    {
        // Create connection to database
        conn = DriverManager.getConnection(conn_string, username, password);
        stmt = conn.createStatement();

        // TODO code application logic here
        try
        {                            
            //1.Listen
            //2.accept
            //3.create socket (I/O) with client
            Scanner sc = new Scanner(System.in);
            int socketNumber = sc.nextInt();
            sc.nextLine();
            ServerSocket s = new ServerSocket(socketNumber);
            while (true)
            {
                int client_resp, balance;
                Socket c = s.accept();
                System.out.println("Client Arrived");
                DataOutputStream dos = new DataOutputStream(c.getOutputStream());
                DataInputStream dis = new DataInputStream(c.getInputStream());

                //4.perform IO with client
                //here we know what is establishing connection with the server
                //"Hello" if it was a client
                //"I'm another server" if it was server
                String whoAmI = dis.readUTF();
                if(whoAmI.equalsIgnoreCase("Hello")){
                    dos.writeUTF("1.Login\n2.Create Account");
                    client_resp = dis.readInt();
                    switch(client_resp){
                        case 1://Login
                            Login(c);
                            if(Session_ID != 0){
                                dos.writeUTF("1.Show account info"
                                        + "\n2.Deposit"
                                        + "\n3.Withdraw"
                                        + "\n4.Transfer to another account"
                                        + "\n5.Transfer to another bank"
                                        + "\n6.Show history");
                                client_resp = dis.readInt();
                                switch (client_resp){
                                    case 1:
                                        balance = ShowAccountInfo(c);
                                        dos.writeUTF("Your balance is:" + balance);
                                        break;
                                    case 2:
                                        balance = Deposit(c);
                                        dos.writeUTF("Your new balance is:" + balance);
                                        break;
                                    case 3:
                                        balance = Withdraw(c);
                                        dos.writeUTF("Your new balance is:" + balance);
                                        break;
                                    case 4:
                                        balance = TransferToAnotherAccount(c);
                                        dos.writeUTF("Your new balance is:" + balance);
                                        break;
                                    /*case 5:
                                        balance = TransferToAnotherAccountInAnotherBank(c, ID_Session);
                                        dos.writeUTF("Your new balance is:" + balance);
                                        break;
                                    case 6:
                                        String History = ShowHistory(c, ID_Session);
                                        dos.writeUTF(History);
                                        break;*/
                                }
                            }
                            else {
                                //do something
                            }
                            break;
                        case 2://create account
                            dos.writeUTF("Your ID is: " + CreateAccount(c));
                            break;
                        default:
                            break;
                    }
                    
                }
                else if(whoAmI.equalsIgnoreCase("I'm another server")){
                    System.out.println("I hear you loud and clear");
                    dos.writeUTF("1.Transfer to account that I have");
                    char serverChoise = dis.readChar();
                    System.out.println(serverChoise);
                    switch(serverChoise){
                        case '1':
                            System.out.println("the server chose transfer");
                            //getting account ID
                            String IDOfTargetAccount = dis.readUTF();
                            System.out.println(IDOfTargetAccount);
                            //check data base for target ID
                            //return true or false to the server
                            boolean targetIDFound = true;//change the default to false
                            dos.writeBoolean(targetIDFound);
                            if(targetIDFound){
                                //read amount of money
                                int amountTransfered = dis.readInt();
                                System.out.println(amountTransfered);
                                //update data base with targetbalance += amount;
                                
                            }else{
                                //do something
                            }
                            break;
                            
                    }
                }
                

                //5. close comm with client
                dos.close();
                dis.close();
                c.close();
            }

            //s.close();
        } 
        catch (IOException ex)
        {
            //ex.printStackTrace();
            System.out.println("Something went wrong");
        }

    }
   
}
