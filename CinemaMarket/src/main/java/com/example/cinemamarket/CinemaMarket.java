package com.example.cinemamarket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URISyntaxException;
import java.io.IOException;
import java.util.ArrayList;

public class CinemaMarket extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("paginaPrincipal.fxml"));
        BorderPane root = loader.load();

        ArrayList<Pelicula> peliculas = new ArrayList<>();
        ArrayList<Actor> actores = new ArrayList<>();
        ArrayList<Director> directores = new ArrayList<>();
        ArrayList<Genero> generos = new ArrayList<>();
        String[][] frases = new String[20][5];
        LectorDatos lector = new LectorDatos(peliculas,actores,directores,generos,frases);
        lector.leerDatos("datos.xlsx");

        PaginaPrincipal controlador = loader.getController();
        controlador.pasarParametros(peliculas,actores,generos,directores,frases);
        controlador.calcularTop6();
        controlador.actualizarImagenes();
        controlador.cargarIdiomas();

        Scene scene = new Scene(root);
        stage.setTitle("CinemaMarket");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}