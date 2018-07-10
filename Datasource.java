package application;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rao on 25-06-2018.
 */
public class Datasource {
    public static final String DATABASE_NAME = "data.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\shash\\Desktop\\Project\\" + DATABASE_NAME;
    public static final String COLUMN_PRICE = "Price";
    public static final String COLUMN_QUANTITY = "Quantity";
    public static final String COLUMN_ITEM_NAME = "\"Item Name\"";

    public static final String LIST_NAME = "MedList";
    public static final String ADD_ELEMENTS_QUERY = "INSERT INTO " + LIST_NAME + " VALUES(";
    private Connection conn;

    private static Datasource instance = new Datasource();

    private Datasource() {
    }

    public static Datasource getInstance(){
        return instance;
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
        } catch (SQLException e) {
            System.out.println("ERROR WHILE OPENING DATABASE");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void CREATE_TABLE() {
        try {
            Statement statement = conn.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS " + LIST_NAME + "(" + COLUMN_ITEM_NAME + " TEXT," + COLUMN_PRICE + " INTEGER,"
                    + COLUMN_QUANTITY + " INTEGER )");
        } catch (SQLException e) {
            System.out.println("Create table exception.(EXCEPTION)");
        }
    }

    public int ADD_ELEMENT_TO_THE_LIST(String name, int price, int quantity) {
        try {
            Statement statement = conn.createStatement();
            statement.execute("SELECT * FROM " + LIST_NAME + " WHERE " + COLUMN_ITEM_NAME + " =" + "\"" + name + "\"");
            ResultSet result = statement.getResultSet();
            if (result.next()) {
                System.out.println("Already in the list. Cant use NEW button.");
                return 0;
            } else {
                statement.execute(ADD_ELEMENTS_QUERY + "'" + name + "'," + price + "," + quantity + ")");
            }
            statement.execute("SELECT * FROM "+LIST_NAME+" ORDER BY "+COLUMN_ITEM_NAME);
            return 1;
        } catch (SQLException e) {
            System.out.println("Insert element failed.(EXCEPTION)");
        }
        return -1;
    }

    public void UPDATE_MEDICINE_IN_THE_LIST(String name, int price, int quantity) {
        try {
            Statement statement = conn.createStatement();
            statement.execute("SELECT " + COLUMN_QUANTITY + " FROM " + LIST_NAME + " WHERE " + COLUMN_ITEM_NAME + "=" + "\"" + name + "\"");
            ResultSet result = statement.getResultSet();
            int q = result.getInt(1);
            q += quantity;
            statement.execute("UPDATE " + LIST_NAME + " SET " + COLUMN_PRICE + "= " + price + "," + COLUMN_QUANTITY +
                    "=" + q + " WHERE " + COLUMN_ITEM_NAME + "=" + "\"" + name + "\"");
        } catch (SQLException e) {
            System.out.println("Couldn't update the list.(EXCEPTION)");
        }
    }
    public boolean DELETE_ELEMENT_FROM_LIST(String name) {
        try {
            Statement statement = conn.createStatement();
            statement.execute("DELETE FROM " + LIST_NAME + " WHERE " + COLUMN_ITEM_NAME + "=" + "\"" + name + "\"");
            return true;
        } catch (SQLException e) {
            System.out.println("Element wasn't deleted.(EXCEPTION)");
            return false;
        }
    }
    public List<Medicine> QUERY_MED_FROM_LIST(){
        List<Medicine> list = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            ResultSet result1 = statement.executeQuery("SELECT * FROM "+LIST_NAME+" ORDER BY "+COLUMN_ITEM_NAME+" COLLATE NOCASE");
            while(result1.next()){
                Medicine med= new Medicine();
                med.setItem_name(result1.getString(1));
                med.setPrice(result1.getInt(2));
                med.setQuantity(result1.getString(3));
                list.add(med);
            }
        }catch(SQLException e){
            System.out.println("Couldn't query from list.(EXCEPTION)");
            e.printStackTrace();
        }
        return list;
    }
    public List<Medicine> SEARCH_MED_FROM_LIST(String medicine){
        List<Medicine> list = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            ResultSet result1 = statement.executeQuery("SELECT * FROM "+LIST_NAME+" WHERE "+COLUMN_ITEM_NAME+" LIKE "+ "'" + medicine + "%'"
                    +" ORDER BY "+COLUMN_ITEM_NAME+" COLLATE NOCASE");
            while(result1.next()){
                Medicine med= new Medicine();
                med.setItem_name(result1.getString(1));
                med.setPrice(result1.getInt(2));
                med.setQuantity(result1.getString(3));
                list.add(med);
            }
        }catch(SQLException e){
            System.out.println("Couldn't query from list.(EXCEPTION)");
            e.printStackTrace();
        }
        return list;
    }
}


