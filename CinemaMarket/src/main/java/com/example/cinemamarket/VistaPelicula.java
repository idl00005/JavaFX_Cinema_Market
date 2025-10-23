package com.example.cinemamarket;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class VistaPelicula implements Initializable {
    private Media media;
    private MediaPlayer mediaPlayer;
    private boolean isPlayed = false;
    private ObservableList<String> listaIdiomas = FXCollections.observableArrayList();
    private String duracion = "Duración";
    private Pelicula pelicula;
    private int idioma = 0;
    String [][]frasesMatriz = new String[20][5];
    @FXML
    private ChoiceBox<String> idiomasSelector = new ChoiceBox();
    @FXML
    private ScrollPane scrollPane = new ScrollPane();
    @FXML
    private MediaView mediaView;
    @FXML
    private Label lbdDuration;
    @FXML
    private Button butPlay = new Button();
    @FXML
    private Slider slider = new Slider();
    @FXML
    private Label verTrailer;
    @FXML
    private Label contacto;
    @FXML
    private Label siguenos;
    @FXML
    private Label titulo;
    @FXML
    private Label descripcion;

    @FXML
    private Label director;

    @FXML
    private Label LBduracion;

    @FXML
    private Label genero;
    @FXML
    private ImageView cartel;
    @FXML
    private Label actores;
    @FXML
    private ImageView comprar;
    @FXML
    private ImageView home;

    @FXML
    private ImageView stopImage;
    @FXML
    private ImageView playImage;
    @FXML
    private Label precio;
    @FXML
    private void pulsarPlay(MouseEvent event){
        if(!isPlayed){
            playImage.setImage(new Image(getClass().getResource("/img/pause.png").toExternalForm()));
            mediaPlayer.play();
            isPlayed = true;
        } else {
            playImage.setImage(new Image(getClass().getResource("/img/play.png").toExternalForm()));
            mediaPlayer.pause();
            isPlayed=false;
        }
    }
    @FXML
    private void pulsarStop(MouseEvent event){
        mediaView.getMediaPlayer().stop();
        playImage.setImage(new Image(getClass().getResource("/img/play.png").toExternalForm()));
        isPlayed = false;
    }
    @FXML
    private void sliderPressed( MouseEvent event){
        mediaPlayer.seek(Duration.seconds(slider.getValue()));
    }
    @FXML
    void volverPrincipal(ActionEvent event) throws IOException, URISyntaxException {
        mediaPlayer.stop();
        Scene escena = home.getScene();
        FXMLLoader loader = new FXMLLoader(PaginaPrincipal.class.getResource("paginaPrincipal.fxml"));
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
        controlador.setIdioma(idioma);
        controlador.cargarIdiomas();
        controlador.actualizarImagenes();
        controlador.labelsPrincipalesActualizar();

        escena.setRoot(root);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stopImage.setImage(new Image(getClass().getResource("/img/stop.png").toExternalForm()));
        playImage.setImage(new Image(getClass().getResource("/img/play.png").toExternalForm()));
        home.setImage(new Image(getClass().getResource("/img/iconoCasa.png").toExternalForm()));
        comprar.setImage(new Image(getClass().getResource("/img/iconoCompra.png").toExternalForm()));

        scrollPane.setFitToWidth(true);

        idiomasSelector.setValue("Idioma: ES");
        idiomasSelector.setItems(listaIdiomas);

        // Añadir un listener para cambiar el texto según la selección
        idiomasSelector.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Actualizar el texto según la selección
                for(int i = 0; i<frasesMatriz[1].length; i++){
                    String id = frasesMatriz[1][i];
                    if(id!=null){
                        if(id.equals(newValue)){
                            idioma = i;
                        }
                    }
                }
                actualizarLabels();
                labelsPrincipalesActualizar();
            }
        });
    }
    public void cargarPelicula(){
        if(pelicula!=null){
            idiomasSelector.setValue(frasesMatriz[0][idioma]);
            duracion = frasesMatriz[14][idioma];
            actualizarLabels();
            labelsPrincipalesActualizar();
            media = new Media(getClass().getResource(pelicula.getRutaTrailer()).toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.currentTimeProperty().addListener(((observableValue, oldValue, newValue) -> {
                slider.setValue(newValue.toSeconds());
                if((int)slider.getValue()%60 < 10 && (int)media.getDuration().toSeconds()%60 < 10){
                    lbdDuration.setText(duracion+" "+(int)slider.getValue()/60+":0"+(int)slider.getValue()%60+" / "+
                            (int)media.getDuration().toSeconds()/60+":0"+ (int)media.getDuration().toSeconds()%60);
                } else if((int)slider.getValue()%60 < 10){
                    lbdDuration.setText(duracion+" "+(int)slider.getValue()/60+":0"+(int)slider.getValue()%60+" / "+
                            (int)media.getDuration().toSeconds()/60+":"+ (int)media.getDuration().toSeconds()%60);
                } else if((int)media.getDuration().toSeconds()%60 < 10){
                    lbdDuration.setText(duracion+" "+(int)slider.getValue()/60+":"+(int)slider.getValue()%60+" / "+
                            (int)media.getDuration().toSeconds()/60+":0"+ (int)media.getDuration().toSeconds()%60);
                } else {
                    lbdDuration.setText(duracion+" "+(int)slider.getValue()/60+":"+(int)slider.getValue()%60+" / "+
                            (int)media.getDuration().toSeconds()/60+":"+ (int)media.getDuration().toSeconds()%60);
                }
            }));

            mediaPlayer.setOnReady(() -> {
                Duration totalDuration = media.getDuration();
                slider.setValue(totalDuration.toSeconds());
                if(totalDuration.toSeconds()%60<10){
                    lbdDuration.setText(duracion+" 00:00 / "+(int)totalDuration.toSeconds()/60+":0"+(int)totalDuration.toSeconds()%60);
                } else {
                    lbdDuration.setText(duracion+" 00:00 / "+(int)totalDuration.toSeconds()/60+":"+(int)totalDuration.toSeconds()%60);
                }
            });
        }
    }
    public void cargarIdiomas(){
        idiomasSelector.setValue(frasesMatriz[0][1]);
        for(String id : frasesMatriz[1]){
            if(id!=null){
                listaIdiomas.add(id);
            }
        }
        idiomasSelector.setItems(listaIdiomas);
    }
    public void pasarParametros(Pelicula p, int idioma, String[][] frases){
        pelicula = p;
        this.idioma = idioma;
        frasesMatriz = frases;
    }
    public void labelsPrincipalesActualizar() {
        idiomasSelector.setValue(frasesMatriz[0][idioma]);
        contacto.setText(frasesMatriz[6][idioma]);
        siguenos.setText(frasesMatriz[7][idioma]);
        verTrailer.setText(frasesMatriz[13][idioma]);
        duracion = frasesMatriz[14][idioma];
        titulo.setText(pelicula.getTitulos().get(idioma));
        descripcion.setText(pelicula.getDescripciones().get(idioma));
        if(media!=null){
            lbdDuration.setText(duracion+" 00:00 / "+(int)media.getDuration().toSeconds()/60+":"+(int)media.getDuration().toSeconds()%60);
        }
    }

    private void actualizarLabels(){
        descripcion.setText(pelicula.getDescripciones().get(idioma));
        titulo.setText(pelicula.getTitulos().get(idioma));
        String sDir,sGen,sAct;
        sDir = frasesMatriz[11][idioma];
        sGen = frasesMatriz[9][idioma];
        sAct = frasesMatriz[10][idioma];
        precio.setText(frasesMatriz[12][idioma]+" 10€");
        LBduracion.setText(pelicula.getDuracion()+" min");
        cartel.setImage(new Image(getClass().getResource(pelicula.getRutaCartel()).toExternalForm()));
        boolean primero = true;
        for(Director d : pelicula.getDirectores()){
            if(primero){
                primero = false;
            } else {
                sDir+=", ";
            }
            sDir += d.getNombre();
        }
        director.setText(sDir);
        primero = true;
        for(Genero g : pelicula.getGeneros()){
            if(primero){
                primero=false;
            }else {
                sGen+=" ,";
            }
            sGen+=g.getNombre();
        }
        genero.setText(sGen);
        primero = true;
        for(Actor a : pelicula.getActores()){
            if(primero){
                primero=false;
            }else{
                sAct+=" ,";
            }
            sAct+=a.getNombre();
        }
        actores.setText(sAct);
    }
}
