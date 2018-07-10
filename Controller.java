package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    private TableView<Medicine> Med_list;
    @FXML
    private Label total;

    @FXML
    private TableView<Medicine> side_list;

    @FXML
    private TextField search;

    private List<Medicine> list = new ArrayList<>();

    @FXML
    private TableColumn<Medicine, String> Item_column, Quantity_column;
    @FXML
    private TableColumn<Medicine, Integer> Price_column;

    @FXML
    private Label grand_total;

    @FXML
    public void addToInvoiceMethod() {
        try {
            Medicine medInv = side_list.getSelectionModel().getSelectedItem();
            Task<ObservableList<Medicine>> task = new AddToInvoice(medInv, list);
            Med_list.itemsProperty().bind(task.valueProperty());
            new Thread(task).start();
            Thread.sleep(10);
            setTotal(list);
        } catch (Exception e) {
        }
    }

    public void list_medicine() {
        Task<ObservableList<Medicine>> task = new GetMedicine();
        side_list.itemsProperty().bind(task.valueProperty());
        new Thread(task).start();
    }


    public void invoiceUpdateMethod() {
        try{
            Medicine obj = list.get(0);
            Task<ObservableList<Medicine>> task = new invoiceUpdate(list );
            Med_list.itemsProperty().bind(task.valueProperty());
            new Thread(task).start();
            Thread.sleep(10);
            setTotal(list);
        }catch(Exception e){
        }
    }


    public void search_result() {
        try {
            String medicine = search.getText();
            Task<ObservableList<Medicine>> task = new SearchMedicine(medicine);
            side_list.itemsProperty().bind(task.valueProperty());
            new Thread(task).start();
        } catch (Exception e) {

        }
    }

    @FXML
    private BorderPane mainWindow;

    @FXML
    public void showDeleteDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindow.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("DeleteItem.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (Exception e) {
            System.out.println("Error in opening dialog pane.(Exception)");
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.FINISH);
        dialog.showAndWait();
        list_medicine();
    }


    @FXML
    public void showDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainWindow.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Dialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (Exception e) {
            System.out.println("Error in opening dialog pane.(Exception)");
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.FINISH);
        dialog.showAndWait();
        list_medicine();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Med_list.setEditable(true);
        Quantity_column.setCellFactory(TextFieldTableCell.forTableColumn());
        Med_list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public void changeQuantity(TableColumn.CellEditEvent editcell){

        String name = Med_list.getSelectionModel().getSelectedItem().getItem_name();
        List<Medicine> list = Datasource.getInstance().SEARCH_MED_FROM_LIST(name);
        int qtystock = Integer.parseInt(list.get(0).getQuantity());
        int pr = list.get(0).getPrice();
        int qty = Integer.parseInt(editcell.getNewValue().toString());
        if(qty>qtystock){
            Med_list.getSelectionModel().getSelectedItem().setQuantity(Integer.toString(qtystock));
            pr=pr*qtystock;
            Med_list.getSelectionModel().getSelectedItem().setPrice(pr);
        }
        else {
            Med_list.getSelectionModel().getSelectedItem().setQuantity(editcell.getNewValue().toString());
            pr = pr*qty;
            Med_list.getSelectionModel().getSelectedItem().setPrice(pr);
            //System.out.println(pr);
        }
        try{
            Thread.sleep(200);
        }catch(Exception e){
        }
        
        invoiceUpdateMethod();

    }

    public void removeItemFromInvoice(){
        Medicine med=Med_list.getSelectionModel().getSelectedItem();
        Med_list.getItems().removeAll(med);
        list.remove(med);
        invoiceUpdateMethod();
    }
    public void deleteInvoice(){
        Med_list.getItems().clear();
        list.clear();
        invoiceUpdateMethod();
    }

    public void setTotal(List<Medicine> list){
        String view;
        int p=0;
        System.out.println(list.size());
        for(Medicine i : list){
            p+= i.getPrice();
        }
        view = Integer.toString(p);
        total.setText(view);
        setGrand_total(p);
    }
    public void setGrand_total(int tot){
        String view1;
        double gtot;
        gtot = (0.18)*tot + tot;
        view1 = Double.toString(gtot);
        grand_total.setText(view1);
    }
}

class GetMedicine extends Task {
    @Override
    protected Object call() throws Exception {
        return FXCollections.observableArrayList(Datasource.getInstance().QUERY_MED_FROM_LIST());
    }
}


class invoiceUpdate extends Task {
    private int price;
    private List<Medicine> list;
    public invoiceUpdate(List<Medicine> list){
        this.list=list;
    }
    @Override
    protected Object call() throws Exception {
        for(Medicine i:list){
            i.setSerialNo(list.indexOf(i)+1);
        }
        return FXCollections.observableArrayList(list);
    }
}

class SearchMedicine extends Task {
    private String medicine;

    public SearchMedicine(String medicine) {
        this.medicine = medicine;
    }

    @Override
    protected Object call() throws Exception {
        return FXCollections.observableArrayList(Datasource.getInstance().SEARCH_MED_FROM_LIST(medicine));
    }
}

class AddToInvoice extends Task {
    private Medicine obj = new Medicine();
    List<Medicine> list;

    public AddToInvoice(Medicine object, List list) {
        obj.setItem_name(object.getItem_name());
        obj.setPrice(object.getPrice());
        if (Integer.parseInt(object.getQuantity()) > 1)
            obj.setQuantity("1");
        this.list = list;
        obj.setSerialNo(0);
    }

    @Override
    protected Object call() throws Exception {
        boolean flag=false;
        for(Medicine i:list){
            flag=this.obj.compare(i);
            if(flag)
                break;
        }
        if(flag==false){
            list.add(this.obj);
            obj.setSerialNo(list.indexOf(obj) + 1);
        }
        System.out.println("///////////////////");
        return FXCollections.observableArrayList(list);
    }
}