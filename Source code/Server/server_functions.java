package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import static java.time.Instant.now;
import static server.Server.db;

public class server_functions 
{  
    // Login to account
    public static boolean Login(int AccountID , String password)
    {
        ResultSet rs;
        try 
        {   
            // Encrypt password            
            Crypto AES = new Crypto("HAVANAOHNANANADA");
            String encdata =AES.encrypt(password);
            password = encdata;
            		       
            //check account ID and password 
            rs = db.stmt.executeQuery(" SELECT ID FROM client WHERE ID = '"+AccountID+"' AND password = '"+password+"'");
            if(rs.next())
            {
                Server.Session_ID = rs.getInt(1);
                return true;
            }           
        } 
        catch (Exception ex) 
        {
            System.out.println("Something went wrong");
        }             
        return false;
    }
    
    // Create a new account
    static int CreateAccount(String Name , String password , int initial_amount)
    {
        ResultSet rs;
        int ID = 0;
        try 
        {
            // Encrypt password
            Crypto AES = new Crypto("HAVANAOHNANANADA");
            String encdata =AES.encrypt(password);
            password = encdata;

            // Save user info to database
            boolean error = db.stmt.execute("INSERT INTO client (full_name, password,balance) VALUES ('"+Name+"','"+password+"','"+initial_amount+"')");
            if(!error)
            {
                rs = db.stmt.executeQuery("SELECT MAX(ID) FROM client");
                rs.next();
                ID = rs.getInt(1);
                Server.Session_ID = ID;
            }            
            else
            {
                return -1;
            }
            String s = "Create account with initial amount " + initial_amount + " LE, at " + now();
            db.stmt.execute("INSERT INTO history (client_id,transaction) VALUES ('"+ID+"' ,'"+s+"')");
        } 
        catch (Exception e) 
        {
            System.out.println("Something went wrong");
        }   
        
        return ID;
    }
    
    // Account Info
    static int ShowAccountInfo()
    {
        int balance = 0;
        ResultSet rs = null;
        try 
        {
            //search in database by ID and return balance
            rs = db.stmt.executeQuery("SELECT balance FROM client WHERE ID = '"+Server.Session_ID+"'");
            rs.next();
            balance = rs.getInt(1);
        } 
        catch (SQLException ex)
        {
            System.out.println("Something went wrong");
        }
        return balance;
    }
    
    // Deposit to account
    static int Deposit(int amount)
    {       
        int balance = 0;
        try 
        {
            //update the database with balance+=amount;
            boolean error = db.stmt.execute("UPDATE client SET balance = balance+ '"+amount+"' WHERE ID = '"+Server.Session_ID+"' ");
            if(!error)
            {
                balance = ShowAccountInfo();
                String s = "Deposit " + amount + " LE, at " + now();
                db.stmt.execute("INSERT INTO history (client_id,transaction) VALUES ('"+Server.Session_ID+"' ,'"+s+"')");
            }
        }        
        catch (SQLException e) 
        {
            System.out.println("Something went wrong");
        }        
        return balance ;
    }
    
    // Withdraw from account
    static int Withdraw(int amount)
    {
        ResultSet rs = null;
        int balance = 0;
        try 
        {            
            rs = db.stmt.executeQuery("SELECT balance FROM client WHERE ID = '"+Server.Session_ID+"'");
            rs.next();
            balance = rs.getInt(1);
            
            if(balance < amount)
            {
                return -1; //withdraw cannot be done
            }
            else
            {
                //update the database with balance-=amount;
                boolean error = db.stmt.execute("UPDATE client SET balance = balance - '"+amount+"' WHERE ID = '"+Server.Session_ID+"' ");
                
                if(!error)
                {
                    balance -= amount;
                    String s = "Withdraw " + amount + " LE, at " + now();
                    db.stmt.execute("INSERT INTO history (client_id,transaction) VALUES ('"+Server.Session_ID+"' ,'"+s+"')");
                }
                
            }
        } 
        catch (Exception e)
        {
            System.out.println("Something went wrong");
        }
        return balance;
    }
    
    // Transfer money to another account in the same bank
    static int TransferToAnotherAccount(int IDForTheTargetAccount,int amount)
    { 
        ResultSet rs = null;
        int balance = 0;
        boolean error;
        try 
        {
            //check if the amount is more than the balance
            rs = db.stmt.executeQuery("SELECT balance FROM client WHERE ID = '"+Server.Session_ID+"'");
            rs.next();
            balance = rs.getInt(1);
            
            if(balance < amount)
            {
                return -1; //Invalid amount transfer cannot be done
            }
            
            else
            {
                //check the data base for the target ID            
                rs = db.stmt.executeQuery(" SELECT ID FROM client WHERE ID = '"+IDForTheTargetAccount+"'");
                if(rs.next())
                {
                    //update target data base with target balance+=amount;
                    error = db.stmt.execute("UPDATE client SET balance = balance+ '"+amount+"' WHERE ID = '"+IDForTheTargetAccount+"' ");
                    
                    //update self data base with balance-=amount;
                    if(!error)
                    {
                        error = db.stmt.execute("UPDATE client SET balance = balance - '"+amount+"' WHERE ID = '"+Server.Session_ID+"' ");                    
                        balance -= amount;
                        String s = "Transfer " + amount + " LE to Account ID : " + IDForTheTargetAccount + ", at " + now();
                        db.stmt.execute("INSERT INTO history (client_id,transaction) VALUES ('"+Server.Session_ID+"' ,'"+s+"')");
                     }
                }
                
                else
                {
                    return -2; //Target ID isn't found, transfer cannot be done

                }
            }
        } 
        catch (Exception e) 
        {
            System.out.println("Something went wrong");
        }
        return balance;
    }
    
    // Transfer money to another account in another bank
    
    static int TransferToAnotherAccountInAnotherBank(String bank_IP , int bank_PN , int IDForTheTargetAccount , int amount)
    {
        int balance = 0;
        ResultSet rs = null;
        try 
        {
            rs = db.stmt.executeQuery("SELECT balance FROM client WHERE ID = '"+Server.Session_ID+"'");
            rs.next();
            balance = rs.getInt(1);
            Socket peepToPeerSocket = null;
            DataOutputStream dosPTP = null;
            DataInputStream disPTP = null;
            if (amount > balance)
            {
                return -1;
            }
            
            else
            {
                //establish connection with another server with the
                //IP address mentioned above
                peepToPeerSocket = new Socket(bank_IP, bank_PN);
                dosPTP = new DataOutputStream(peepToPeerSocket.getOutputStream());
                disPTP = new DataInputStream(peepToPeerSocket.getInputStream());
                dosPTP.writeUTF("I'm another server");

                //connection established *Dap meme*
                if("Hello".equals(disPTP.readUTF()))
                {//list of options
                //choosing option 1, transfer
                dosPTP.writeInt(1);

                /*target bank exists*/ 
                if(disPTP.readInt() == 1)
                {            
                    //sending ID of target to target bank(server)
                    dosPTP.writeInt(IDForTheTargetAccount);
                    //sending ampunt to target bank(server)
                    dosPTP.writeInt(amount);
                    //reading boolean if found or not
                    boolean targetIDFound = disPTP.readBoolean();
                    if(targetIDFound)
                    {    
                        boolean error = db.stmt.execute("UPDATE client SET balance = balance - '"+amount+"' WHERE ID = '"+Server.Session_ID+"' ");

                        if(!error)
                        {
                             balance -= amount;
                             String s = "Transfer " + amount + " LE, at " + now();
                             db.stmt.execute("INSERT INTO history (client_id,transaction) VALUES ('"+Server.Session_ID+"' ,'"+s+"')");
                        }
                    }               
                    else
                    {
                        return -2;
                    }    
                }
                }

            }
            
            //Close communication with P2P Server
            dosPTP.close();
            disPTP.close();
            peepToPeerSocket.close();
        }
        catch (Exception e) 
        {
             System.out.println("Something went wrong");
        }
        return balance ;
    }
    
    // Account History
    static String ShowHistory()
    {
        ResultSet rs = null;
        String History = "";
        try 
        {
            //search the data base by ID and return the history
            rs = db.stmt.executeQuery("SELECT transaction FROM history WHERE client_id = '"+Server.Session_ID+"'");
            
            while (rs.next())
                History += rs.getString(1) + "\n";
        }
        catch (Exception ex) {}
        
        return History;  
    }
}

   
