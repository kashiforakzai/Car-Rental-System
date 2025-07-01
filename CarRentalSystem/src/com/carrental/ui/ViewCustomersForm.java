package com.carrental.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.carrental.db.DBConnector;

public class ViewCustomersForm {

    public static class Customer {
        private String name;
        private String contact;
        private String address;

        public Customer(String name, String contact, String address) {
            this.name = name;
            this.contact = contact;
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public String getContact() {
            return contact;
        }

        public String getAddress() {
            return address;
        }
    }

    public void showViewCustomers(Stage stage) {
        TableView<Customer> table = new TableView<>();

        TableColumn<Customer, String> nameCol = new TableColumn<>("👤 Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Customer, String> contactCol = new TableColumn<>("📞 Contact");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));

        TableColumn<Customer, String> addressCol = new TableColumn<>("🏠 Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        nameCol.setPrefWidth(160);
        contactCol.setPrefWidth(160);
        addressCol.setPrefWidth(160);

        table.getColumns().addAll(nameCol, contactCol, addressCol);

        ObservableList<Customer> customers = FXCollections.observableArrayList();

        try {
            Connection conn = DBConnector.connect();
            String query = "SELECT * FROM customers";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String name = rs.getString("name");
                String contact = rs.getString("contact");
                String address = rs.getString("address");
                customers.add(new Customer(name, contact, address));
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setItems(customers);
        table.setStyle("-fx-font-size: 13px;");

        Button backButton = new Button("⬅ Back to Dashboard");
        backButton.setStyle(
            "-fx-background-color: #ff69b4;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 15;" +
            "-fx-padding: 8 18 8 18;" +
            "-fx-cursor: hand;"
        );

        backButton.setOnAction(e -> {
            Dashboard dashboard = new Dashboard();
            dashboard.showDashboard(stage);
        });

        VBox layout = new VBox(15, table, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle(
            "-fx-padding: 25;" +
            "-fx-background-color: #fff0f5;" +
            "-fx-border-radius: 15;" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );

        Scene scene = new Scene(layout, 540, 450);
        stage.setScene(scene);
        stage.setTitle("View Customers - Car Rental System");
        stage.show();
    }
}
