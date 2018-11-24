package client_gui;

import static client_gui.Client_gui.dis;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;


public class create_account implements Initializable
{
    String SignUpUserName , SignUpPassword , SignUpRepeatPassword , SignUpinitDeposit;
    @FXML
    TextField SignUpNameField , SignUpInitDeposit;
    @FXML
    PasswordField SignUpInitPass , SignUpRepeatPass ;
    @FXML
    Button SignUpButton;

    @FXML
    private void SignUpButtonAct(ActionEvent event)
    {
        SignUpUserName = SignUpNameField.getText();
        SignUpinitDeposit = SignUpInitDeposit.getText();
        SignUpPassword = SignUpInitPass.getText();
        SignUpRepeatPassword = SignUpRepeatPass.getText();
        
        try 
        {
            Client_gui.dos.writeInt(2);
            
            // Send user info to server
            if (dis.readInt() == 2)
            {
                Client_gui.dos.writeUTF(SignUpUserName);
                Client_gui.dos.writeUTF(SignUpPassword);
                Client_gui.dos.writeInt(Integer.parseInt(SignUpinitDeposit));               
            }
            
            int AccountID = dis.readInt();
            if (AccountID != -1)
            {
                // label y2ol el ID kam
                Parent tableViewParent = FXMLLoader.load(getClass().getResource("services.fxml"));
                Scene tableViewScene = new Scene(tableViewParent);

                //This line gets the Stage information
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

                window.setScene(tableViewScene);
                window.show();
            }
        } 
        
        catch (IOException ex) 
        {
            // do something
        }
        

    }
    @Override
    public void initialize(URL url ,ResourceBundle rb){
    }

}
