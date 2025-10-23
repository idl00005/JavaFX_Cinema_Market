package com.example.cinemamarket;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Prueba implements Initializable {
    ObservableList<String> listaIdiomas = FXCollections.observableArrayList("Español","Inglés");
    @FXML
    private Label welcomeText;

    @FXML
    private TextField texto;

    @FXML
    private ChoiceBox<String> idiomasSelector = new ChoiceBox();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idiomasSelector.setValue("aaaaa");
        idiomasSelector.setItems(listaIdiomas);
        idiomasSelector.getItems().addAll("aaa","aaa");
    }
}
