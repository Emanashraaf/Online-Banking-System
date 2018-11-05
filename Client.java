package banking.system_ds;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    public static void main(String[] args) {
        // TODO code application logic here
        try
        {
            int client_resp = 0;
            String server_resp = "";
            Scanner sc = new Scanner(System.in);
            //1.connect
            //2. create socket
            Socket c = new Socket("127.0.0.1", 1234);
            DataOutputStream dos = new DataOutputStream(c.getOutputStream());
            DataInputStream dis = new DataInputStream(c.getInputStream());

            //3. perform IO
            dos.writeUTF("Hello");
            //login or create account
            System.out.println(dis.readUTF());
            client_resp = sc.nextInt(); 
            sc.nextLine();
            dos.writeInt(client_resp);
            switch(client_resp){
                        case 1://Login
                            //enter your ID
                            System.out.println(dis.readUTF());
                            dos.writeUTF(sc.nextLine());
                            //enter password
                            System.out.println(dis.readUTF());
                            dos.writeUTF(sc.nextLine());
                            //options from bank
                            System.out.println(dis.readUTF());
                            client_resp = sc.nextInt();
                            dos.writeInt(client_resp);
                            sc.nextLine();
                            switch (client_resp){
                                case 1://show balance
                                    //print balance
                                    System.out.println(dis.readUTF());
                                    break;
                                case 2://Deposit
                                    //enter amount of money
                                    System.out.println(dis.readUTF());
                                    dos.writeInt(sc.nextInt());
                                    sc.nextLine();
                                    //print new balance
                                    System.out.println(dis.readUTF());
                                    break;
                                case 3://Withdraw
                                    //enter amount of money
                                    System.out.println(dis.readUTF());
                                    dos.writeInt(sc.nextInt());
                                    sc.nextLine();
                                    //print new balance
                                    System.out.println(dis.readUTF());
                                    break;
                                case 4://transfer to another account in the same bank
                                    //enter target ID
                                    System.out.println(dis.readUTF());
                                    dos.writeUTF(sc.nextLine());
                                    //enter amount of money
                                    System.out.println(dis.readUTF());
                                    dos.writeInt(sc.nextInt());
                                    sc.nextLine();
                                    //print new balance
                                    System.out.println(dis.readUTF());
                                    break;
                                case 5://tranfer to another bank
                                    //enter bank IP
                                    System.out.println(dis.readUTF());
                                    dos.writeUTF(sc.nextLine());
                                    //enter target ID
                                    System.out.println(dis.readUTF());
                                    dos.writeUTF(sc.nextLine());
                                    //enter amount of money
                                    System.out.println(dis.readUTF());
                                    dos.writeInt(sc.nextInt());
                                    sc.nextLine();
                                    //print new balance
                                    System.out.println(dis.readUTF());
                                    break;
                                case 6:// show transaction history
                                    //print transaction history
                                    System.out.println(dis.readUTF());
                                    break;
                            }
                            break;
                        case 2://create account
                            //enter your full name
                            System.out.println(dis.readUTF());
                            dos.writeUTF(sc.nextLine());
                            //enter password
                            System.out.println(dis.readUTF());
                            dos.writeUTF(sc.nextLine());
                            //reenter password
                            System.out.println(dis.readUTF());
                            dos.writeUTF(sc.nextLine());
                            //enter amount of money
                            System.out.println(dis.readUTF());
                            dos.writeInt(sc.nextInt());
                            //ID
                            System.out.println(dis.readUTF());
                            break;
                        default:
                            break;
                    }
            
            
            //4.close comm
            dos.close();
            dis.close();
            c.close();
        } catch (IOException ex)
        {
            System.out.println("something went wrong");
        }
    }
    
}