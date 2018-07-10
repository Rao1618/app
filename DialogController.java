package application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Created by Rao on 28-06-2018.
 */
public class DialogController {
    @FXML
    private TextField name;

    @FXML
    private TextField price;

    @FXML
    private TextField quantity;

    @FXML
    private Label invalid,success;

    @FXML
    public void addNewMed() {
        System.out.println("heyadd");
        try {
            success.setVisible(false);
            String namedb = name.getText().trim();
            int pricedb = Integer.parseInt(price.getText().trim());
            int quantitydb = Integer.parseInt(quantity.getText().trim());
            invalid.setVisible(false);
            int suc = Datasource.getInstance().ADD_ELEMENT_TO_THE_LIST(namedb, pricedb, quantitydb);
            if(suc==1)
                success.setText("Successfully added!");
            else if(suc==0)
                success.setText("Item already exists!");
            else
                success.setText("Failed!");
                success.setVisible(true);

        }catch (Exception e) {
            invalid.setVisible(true);
        }
    }

    @FXML
    public void updateMed() {
        System.out.println("heyupdate");
        try{
            success.setVisible(false);
            String namedb = name.getText().trim();
            int pricedb = Integer.parseInt(price.getText().trim());
            int quantitydb = Integer.parseInt(quantity.getText().trim());
            invalid.setVisible(false);
            Datasource.getInstance().UPDATE_MEDICINE_IN_THE_LIST(namedb, pricedb, quantitydb);
            success.setText("Successfully updated");
            success.setVisible(true);
        }catch (Exception e){
            invalid.setVisible(true);
        }
    }

    @FXML
    public void clear() {
        name.clear();
        price.clear();
        quantity.clear();
    }
}