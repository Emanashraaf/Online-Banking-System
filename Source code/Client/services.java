package client_gui;
import static client_gui.Client_gui.dis;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class services implements Initializable 
{
    String HistroyString , Bank_IP;
    int amount ,balance , AccountID , Bank_PN;
    Stage window;
    Button button1;
    @FXML
    Label Balance , BalanceAmount;
    @FXML
    TextArea HISTORY= new TextArea();
    @FXML
    TextField DepositText,WithdrewText ,TransferDepositTF,TransferIDTF,TAB_BIP,TAB_BPN,TAB_AID,TAB_amount;
    @FXML
    AnchorPane BalanceAP , withdrewAP , TTACAP , TTABAP , HistoryAP;
    @FXML
    Button DepositSubmit,WithdrawSubmit,TransferDepositBT,TransferBankBT ,QuitBTN;
    @FXML
    Label ID_label,Deposit_Error,withdraw_Error,TTACID_Error,TTACAMT_Error,TTABID_Error,TTABAMT_Error;
    
    @Override
    public void initialize(URL url ,ResourceBundle rb)
    {
        ID_label.setText("Account ID : " +Controller.AccountID);
    }
    
    @FXML
    private void AccountInfo (ActionEvent event) throws IOException
    {
        HistoryAP.setVisible(false);
        BalanceAP.setVisible(false);
        withdrewAP.setVisible(false);
        TTACAP.setVisible(false);
        TTABAP.setVisible(false);
        Balance.setVisible(true);
        BalanceAmount.setVisible(true);
        Client_gui.dos.writeInt(1);
        if(dis.readInt() == 1)
            BalanceAmount.setText(Integer.toString(dis.readInt()));       
    }
    
    
    @FXML
    private void DepositButton (ActionEvent event) throws IOException 
    {
        HistoryAP.setVisible(false);
        withdrewAP.setVisible(false);
        TTACAP.setVisible(false);
        TTABAP.setVisible(false);
        BalanceAP.setVisible(true);
        Balance.setVisible(false);
        BalanceAmount.setVisible(false);
    }
    
    @FXML
    private void DepositSubmit (ActionEvent event) throws IOException 
    {
        boolean error = false;
        Deposit_Error.setVisible(false);
        
        if (DepositText.getText() == null || DepositText.getText().trim().isEmpty())
        {
            Deposit_Error.setText("Enter the amount of money!");
            Deposit_Error.setVisible(true);
            error = true;           
        }
        else
        {
            amount = Integer.parseInt(DepositText.getText());
            if (amount <0)
            {
                Deposit_Error.setText("Invalid amount!");
                Deposit_Error.setVisible(true);
                error = true;  
            }
        }
        
        if (!error)
        {
            Client_gui.dos.writeInt(2);
            if (dis.readInt() == 2)
            {
                Client_gui.dos.writeInt(amount);
                BalanceAmount.setText(Integer.toString(dis.readInt()));
                Balance.setVisible(true);
                BalanceAmount.setVisible(true);
            }
        }
    }

    
    @FXML
    private void withdrawButton(ActionEvent event) throws IOException 
    {
        HistoryAP.setVisible(false);
        BalanceAP.setVisible(false);
        TTACAP.setVisible(false);
        TTABAP.setVisible(false);
        withdrewAP.setVisible(true);
        Balance.setVisible(false);
        BalanceAmount.setVisible(false);
    }
    
    @FXML
    private void WithdrawSubmit(ActionEvent event) throws IOException  
    {
        boolean error = false;
        withdraw_Error.setVisible(false);
        
        if (WithdrewText.getText() == null || WithdrewText.getText().trim().isEmpty())
        {
            withdraw_Error.setText("Enter the amount of money!");
            withdraw_Error.setVisible(true);
            error = true;           
        }
        else
        {
            amount = Integer.parseInt(WithdrewText.getText());
            if (amount <0)
            {
                withdraw_Error.setText("Invalid amount!");
                withdraw_Error.setVisible(true);
                error = true;  
            }
        }
      
        if (!error)
        {
            Client_gui.dos.writeInt(3);
        
            if (dis.readInt() == 3)
            {
                Client_gui.dos.writeInt(amount);
                balance = dis.readInt();
                if (balance == -1)
                {
                    withdraw_Error.setText("Invalid amount!");               
                    withdraw_Error.setVisible(true);
                    Balance.setVisible(false);
                    BalanceAmount.setVisible(false);
                }
                else
                {
                    BalanceAmount.setText(Integer.toString(balance));
                    Balance.setVisible(true);
                    BalanceAmount.setVisible(true);
                }    
                
            }
        } 
      
    }
    
    
    @FXML
    private void TTACButton(ActionEvent event) throws IOException 
    {
        HistoryAP.setVisible(false);
        BalanceAP.setVisible(false);
        withdrewAP.setVisible(false);
        TTABAP.setVisible(false);
        TTACAP.setVisible(true);
        Balance.setVisible(false);
        BalanceAmount.setVisible(false);
    }
    
    @FXML
    private void TransferDepositBT(ActionEvent event) throws IOException 
    {
        boolean error = false;
        TTACID_Error.setVisible(false);
        TTACAMT_Error.setVisible(false);
        if (TransferDepositTF.getText() == null || TransferDepositTF.getText().trim().isEmpty())
        {
            TTACAMT_Error.setText("Enter the amount of money!");
            TTACAMT_Error.setVisible(true);
            error = true;           
        }
        else
        {
            amount = Integer.parseInt(TransferDepositTF.getText());
            if (amount <0)
            {
                TTACAMT_Error.setText("Invalid amount!");
                TTACAMT_Error.setVisible(true);
                error = true;  
            }
        }
        
        AccountID = Integer.parseInt(TransferIDTF.getText());
        if (!error)
        {
            Client_gui.dos.writeInt(4);
        
            if (dis.readInt() == 4)
            {
                Client_gui.dos.writeInt(AccountID);
                Client_gui.dos.writeInt(amount);
                balance = dis.readInt();
                switch (balance) 
                {
                    case -1:
                        TTACAMT_Error.setText("Invalid amount!");
                        TTACAMT_Error.setVisible(true);
                        Balance.setVisible(false);
                        BalanceAmount.setVisible(false);
                        break;
                    case -2:
                        TTACID_Error.setText("The Target ID isn't found!");
                        TTACID_Error.setVisible(true);
                        Balance.setVisible(false);
                        BalanceAmount.setVisible(false);
                        break;
                    default:
                        BalanceAmount.setText(Integer.toString(balance));
                        Balance.setVisible(true);
                        BalanceAmount.setVisible(true);
                        break;
                }
            }
        }        
        else
        {
            // label error
        }
    }
    
    @FXML
    private void TTABButton(ActionEvent event) throws IOException 
    {
        HistoryAP.setVisible(false);
        TTABAP.setVisible(true);
        BalanceAP.setVisible(false);
        withdrewAP.setVisible(false);
        TTACAP.setVisible(false);
        Balance.setVisible(false);
        BalanceAmount.setVisible(false);
    }

    @FXML
    private void TTABSubmit(ActionEvent event) throws IOException 
    {
        boolean error = false;
        TTABID_Error.setVisible(false);
        TTABAMT_Error.setVisible(false);
        if (TAB_amount.getText() == null || TAB_amount.getText().trim().isEmpty())
        {
            TTABAMT_Error.setText("Enter the amount of money!");
            TTABAMT_Error.setVisible(true);
            error = true;           
        }
        else
        {
            amount = Integer.parseInt(TAB_amount.getText());
            if (amount <0)
            {
                TTABAMT_Error.setText("Invalid amount!");
                TTABAMT_Error.setVisible(true);
                error = true;  
            }
        }
        AccountID = Integer.parseInt(TAB_AID.getText());
        Bank_IP = TAB_BIP.getText(); 
        Bank_PN = Integer.parseInt(TAB_BPN.getText());
        
        if (!error)
        {
            Client_gui.dos.writeInt(5);
        
            if (dis.readInt() == 5)
            {
                Client_gui.dos.writeUTF(Bank_IP);
                Client_gui.dos.writeInt(Bank_PN);
                Client_gui.dos.writeInt(AccountID);
                Client_gui.dos.writeInt(amount);
                balance = dis.readInt();
                System.out.println(balance);
                switch (balance) 
                {
                    case -1:
                        TTABAMT_Error.setText("Invalid amount!");
                        TTABAMT_Error.setVisible(true);
                        Balance.setVisible(false);
                        BalanceAmount.setVisible(false);
                        break;
                    case -2:
                        TTABID_Error.setText("The Target ID isn't found!");
                        TTABID_Error.setVisible(true);
                        Balance.setVisible(false);
                        BalanceAmount.setVisible(false);
                        break;
                    default:
                        BalanceAmount.setText(Integer.toString(balance));
                        Balance.setVisible(true);
                        BalanceAmount.setVisible(true);
                        break;
                }
            }
        }        
        else
        {
            // label error
        }
    }
    
    @FXML
    private void showHistoryButton(ActionEvent event) throws IOException 
    {

        TTABAP.setVisible(false);
        BalanceAP.setVisible(false);
        withdrewAP.setVisible(false);
        TTACAP.setVisible(false);
        BalanceAmount.setVisible(false);
        HistoryAP.setVisible(true);
        Balance.setVisible(false);
        
        Client_gui.dos.writeInt(6);
        
        if (dis.readInt() == 6)
        {
            HistroyString = dis.readUTF();
            HISTORY.setText(HistroyString);
        }
    }
    
    @FXML
    private void Quit() throws IOException
    {
        Client_gui.dos.writeInt(0);
        
        window = (Stage) QuitBTN.getScene().getWindow();
        window.close();
    }
    
    
    
    


}
