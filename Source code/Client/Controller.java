package client_gui;

import static client_gui.Client_gui.dis;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Controller implements Initializable 
{
    String LoginPasswordVar , LoginIDVar;
    Stage window;
    
    @FXML
    PasswordField LoginPassword;
    @FXML
    TextField LoginID;
    @FXML
    AnchorPane IDPane;
    @FXML
    Label Error_label;
    
    static int AccountID;
   
    @FXML
    private void SignIn (ActionEvent event) 
    {
        LoginPasswordVar = LoginPassword.getText();
        LoginIDVar = LoginID.getText();
        try 
        {
            Client_gui.dos.writeInt(1);
            if (dis.readInt() == 1)
            {
                // Send account info to server
                Client_gui.dos.writeInt(Integer.parseInt(LoginIDVar));
                Client_gui.dos.writeUTF(LoginPasswordVar);
            }  
            
            boolean logged_in = dis.readBoolean();
            if (logged_in)
            {
                Controller.AccountID = Integer.parseInt(LoginIDVar);
                Parent tableViewParent = FXMLLoader.load(getClass().getResource("services.fxml"));
                Scene tableViewScene = new Scene(tableViewParent);

                //This line gets the Stage information
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

                window.setScene(tableViewScene);
                window.show();
            }
            
            else
            {
                Error_label.setText("Invalid ID or Password!");
            }
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    @FXML
    public void signUp(ActionEvent event) throws IOException
    {
        Parent SignInView = FXMLLoader.load(getClass().getResource("create_account.fxml"));
        Scene SignInViewScene = new Scene(SignInView);

        //This line gets the Stage information
        Stage SignInViewwindow = (Stage)((Node)event.getSource()).getScene().getWindow();

        SignInViewwindow.setScene(SignInViewScene);
        SignInViewwindow.show();
    }
    @Override
    public void initialize(URL url ,ResourceBundle rb){

    }


}
