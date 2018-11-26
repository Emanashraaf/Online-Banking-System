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
import javafx.scene.control.Label;
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
    Label Name_Error,Password_Error,Amount_Error;


    @FXML
    private void SignUpButtonAct(ActionEvent event)
    {
        boolean error = false;       
        String RegExp = "^[a-zA-Z\\s]*$";
        int amount = 0;
        
        Name_Error.setVisible(false);
        Password_Error.setVisible(false); 
        Amount_Error.setVisible(false);
          
        if (SignUpNameField.getText() == null || SignUpNameField.getText().trim().isEmpty())
        {
            Name_Error.setText("You must enter a name!");
            Name_Error.setVisible(true);
            error = true;
        }
        
        else
        {
            SignUpUserName = SignUpNameField.getText();
            if (!SignUpUserName.matches(RegExp))
            {
                Name_Error.setText("Invalid Name!");
                Name_Error.setVisible(true);              
                error = true;
            }
        }
        
        if (SignUpInitPass.getText() == null || SignUpInitPass.getText().trim().isEmpty()
              || SignUpRepeatPass.getText() == null || SignUpRepeatPass.getText().trim().isEmpty()  )
        {
            Password_Error.setText("You must enter a password!");
            Password_Error.setVisible(true); 
            error = true;
        }
        
        else
        {
            SignUpPassword = SignUpInitPass.getText();
            SignUpRepeatPassword = SignUpRepeatPass.getText();
            if (!SignUpPassword.equals(SignUpRepeatPassword))
            {
                Password_Error.setText("Passwords don't match!");
                Password_Error.setVisible(true); 
                error = true;
            }
        }
        
        if (SignUpInitDeposit.getText() == null || SignUpInitDeposit.getText().trim().isEmpty())
        {
            Amount_Error.setText("You must enter an initial amount!");
            Amount_Error.setVisible(true); 
            error = true;
        }
        
        else
        {
            SignUpinitDeposit = SignUpInitDeposit.getText();
            amount = Integer.parseInt(SignUpinitDeposit);
            if(amount<=0)
            {
                Amount_Error.setText("Invalid amount!");
                Amount_Error.setVisible(true); 
                error = true;               
            }            
        }
            
        if(!error)
        {    
            try 
            {
                Client_gui.dos.writeInt(2);

                // Send user info to server
                if (dis.readInt() == 2)
                {
                    Client_gui.dos.writeUTF(SignUpUserName);
                    Client_gui.dos.writeUTF(SignUpPassword);
                    Client_gui.dos.writeInt(amount);               
                }

                Controller.AccountID = dis.readInt();
                if (Controller.AccountID != -1)
                {
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
    }
    @Override
    public void initialize(URL url ,ResourceBundle rb){
    }

}
