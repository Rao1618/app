package application;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.awt.*;

/**
 * Created by Rao on 28-06-2018.
 */
public class DeleteController {
    @FXML
    private TextField itemName;

    @FXML
    private Label invalid,success;

    @FXML
    public void delete(){
        try{
            invalid.setVisible(false);
            success.setVisible(true);
            String name=itemName.getText().trim();
            boolean suc=Datasource.getInstance().DELETE_ELEMENT_FROM_LIST(name);

            success.setVisible(true);
        }catch(Exception e){
            invalid.setVisible(true);
        }
    }
}
