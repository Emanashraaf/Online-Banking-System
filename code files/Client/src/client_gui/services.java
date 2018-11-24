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


public class services implements Initializable 
{

    String HistroyString;
    int amount , AccountID;

    @FXML
    Label Balance , BalanceAmount;
    @FXML
    TextArea HISTORY= new TextArea();
    @FXML
    TextField DepositText,WithdrewText ,TransferDepositTF,TransferIDTF,TransferBankDepositTF,TransferBankIDTF,TTABAPID;
    @FXML
    AnchorPane BalanceAP , withdrewAP , TTACAP , TTABAP , HistoryAP;
    @FXML
    Button DepositSubmit,WithdrawSubmit,TransferDepositBT,TransferBankBT;


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
        if (amount > 0)
        {
            amount = Integer.parseInt(DepositText.getText());
            Client_gui.dos.writeInt(2);
            if (dis.readInt() == 2)
            {
                Client_gui.dos.writeInt(amount);
                BalanceAmount.setText(Integer.toString(dis.readInt()));
                Balance.setVisible(true);
                BalanceAmount.setVisible(true);
            }
        }
        
        else
        {
            // label error
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
        amount = Integer.parseInt(WithdrewText.getText());
        if (amount > 0)
        {
            Client_gui.dos.writeInt(3);
        
            if (dis.readInt() == 3)
            {
                Client_gui.dos.writeInt(amount);
                BalanceAmount.setText(Integer.toString(dis.readInt()));
                Balance.setVisible(true);
                BalanceAmount.setVisible(true);
            }
        }
        
        else
        {
            // label error
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
        amount = Integer.parseInt(TransferDepositTF.getText());
        AccountID = Integer.parseInt(TransferIDTF.getText());
        if (amount > 0)
        {
            Client_gui.dos.writeInt(4);
        
            if (dis.readInt() == 4)
            {
                Client_gui.dos.writeInt(AccountID);
                Client_gui.dos.writeInt(amount);
                BalanceAmount.setText(Integer.toString(dis.readInt()));
                Balance.setVisible(true);
                BalanceAmount.setVisible(true);
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
        amount = Integer.parseInt(TransferBankDepositTF.getText());
        AccountID = Integer.parseInt(TTABAPID.getText());
        if (amount > 0)
        {
            Client_gui.dos.writeInt(5);
        
            if (dis.readInt() == 5)
            {
                Client_gui.dos.writeInt(AccountID);
                Client_gui.dos.writeInt(amount);
                BalanceAmount.setText(Integer.toString(dis.readInt()));
                Balance.setVisible(true);
                BalanceAmount.setVisible(true);
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

    @Override
    public void initialize(URL url ,ResourceBundle rb){

    }

}
