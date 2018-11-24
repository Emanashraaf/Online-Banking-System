package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import static java.time.Instant.now;
import java.util.ResourceBundle;
import static server.Server.db;


class clientHandler implements Runnable
{
    Socket c;
        
    public clientHandler (Socket c)
    {
        this.c = c;        
    }
    
    @Override
        public void run()
        {
            DataOutputStream dos = null;
            DataInputStream dis = null;           
            int client_resp , AccountID , amount , balance , P2PServer_PN;
            String whoAmI , P2PServer_IP , Name , password , History;
            
            try 
            {
                dos = new DataOutputStream(c.getOutputStream());
                dis = new DataInputStream(c.getInputStream());
                                
                //here we know what is establishing connection with the server
                //"Hello" if it was a client
                //"I'm another server" if it was server
                whoAmI = dis.readUTF();
                System.out.println (whoAmI);
                if(whoAmI.equalsIgnoreCase("Hello"))
                {
                    // Ask if the client wants to login or create account
                    dos.writeUTF("Hello");                   
                    
                    client_resp = dis.readInt();
                    dos.writeInt(client_resp);                    
                    switch(client_resp)
                    {                                      
                        //login
                        case 1:
                            AccountID = dis.readInt();
                            password = dis.readUTF();
                            // Logged in
                            if (server_functions.Login(AccountID , password))
                                dos.writeUTF("Ok");                       
                        break;
                        
                        //create account
                        case 2:
                            Name = dis.readUTF();
                            password = dis.readUTF();
                            amount = dis.readInt();  // initial amount   
                            AccountID = server_functions.CreateAccount(Name , password , amount);
                            if (AccountID != -1)
                                dos.writeInt(AccountID);                        
                    }
                    
                    // Make quit condition
                    while(true)
                    {
                        client_resp = dis.readInt();
                        dos.writeInt(client_resp);
                        switch(client_resp)
                        {                     
                            // Account Info
                            case 1:
                                balance = server_functions.ShowAccountInfo();
                                dos.writeInt(balance);
                                break;

                            // Deposit to account
                            case 2:
                                amount = dis.readInt();
                                balance = server_functions.Deposit(amount);
                                dos.writeInt(balance);
                                break;

                            // Withdraw from account 
                            case 3:
                                amount = dis.readInt();
                                balance = server_functions.Withdraw(amount);
                                dos.writeInt(balance);
                                break;

                            // Transfer money to another account in the same bank 
                            case 4:
                                AccountID = dis.readInt(); // ID of the target account
                                amount = dis.readInt();
                                balance = server_functions.TransferToAnotherAccount(AccountID , amount);
                                dos.writeInt(balance);
                                break;

                            // Transfer money to another account in another bank    
                            case 5:
                                AccountID = dis.readInt(); // ID of the target account
                                amount = dis.readInt();
                                ResourceBundle rb = ResourceBundle.getBundle("server_config");
                                P2PServer_IP = rb.getString("P2PServer_IP");
                                P2PServer_PN= Integer.parseInt(rb.getString("P2PServer_PN"));

                                balance = server_functions.TransferToAnotherAccountInAnotherBank(P2PServer_IP,P2PServer_PN,AccountID,amount);
                                dos.writeInt(balance);
                                break;

                            // Account History
                            case 6:
                                History = server_functions.ShowHistory();
                                dos.writeUTF(History);                   
                        } 
                    }
                }
                
                else if(whoAmI.equalsIgnoreCase("I'm another server"))
                {
                    dos.writeUTF("Hello"); 
                    
                    // accept transfer
                    if (dis.readInt()==1)
                    {
                        
                        dos.writeInt(1);
                    } 
                    
                    AccountID = dis.readInt(); // ID of the target account
                    amount = dis.readInt();
                    
                    
                    //check data base for target ID
                    boolean targetIDFound = db.stmt.execute(" SELECT ID FROM client WHERE ID = '"+AccountID+"'");
                    
                    if(targetIDFound)
                    {
                        //update data base with targetbalance += amount;
                        boolean error = db.stmt.execute("UPDATE client SET balance = balance+ '"+amount+"' WHERE ID = '"+AccountID+"' ");
                        String history_string = "Someone deposited " + amount + " LE, at " + now();
                        db.stmt.execute("INSERT INTO history (client_id,transaction) VALUES ('"+AccountID+"' ,'"+history_string+"')");
                        dos.writeBoolean(true);
                    }
                    
                    else
                        dos.writeBoolean(false);                         
                }
                //Close communication with client
                dos.close();
                dis.close();
                c.close();
            }
            catch (Exception ex) 
            {
                System.out.println(ex);
            }       
        }
    }   
