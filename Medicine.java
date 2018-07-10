package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Rao on 27-06-2018.
 */
public class Medicine{
    private SimpleStringProperty item_name,quantity;
    private SimpleIntegerProperty  price,serialNo;

    public Medicine() {
        this.item_name = new SimpleStringProperty();
        this.quantity = new SimpleStringProperty();
        this.price = new SimpleIntegerProperty();
        this.serialNo = new SimpleIntegerProperty();
        serialNo.set(0);
    }

    public String getItem_name() {
        return item_name.get();
    }

    public void setItem_name(String item_name) {
        this.item_name.set(item_name);
    }

    public String getQuantity() {
        return quantity.get();
    }

    public void setQuantity(String quantity) {
        this.quantity.set(quantity);
    }

    public int getPrice() {
        return price.get();
    }

    public int getSerialNo() {
        return serialNo.get();
    }


    public void setSerialNo(int serialNo) {
        this.serialNo.set(serialNo);
    }

    public void setPrice(int price) {
        this.price.set(price);
    }

    public boolean compare(Medicine object){
        if(this.item_name.get().equals(object.getItem_name()))
            return true;
        return false;
    }

    @Override
    public String toString() {

        return  item_name.get();
    }
}
