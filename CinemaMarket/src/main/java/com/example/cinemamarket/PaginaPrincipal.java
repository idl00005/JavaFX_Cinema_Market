package com.example.cinemamarket;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

public class PaginaPrincipal implements Initializable{
    static ArrayList<Pelicula> peliculas;
    ArrayList<Actor> actores;
    ArrayList<Genero> generos;
    ArrayList<Director> directores;
    ObservableList<String> listaIdiomas = FXCollections.observableArrayList();
    ObservableList<Image> listaIconos = FXCollections.observableArrayList(
            new Image(getClass().getResource("/img/GENERO/buscarNombre.png").toExternalForm()),
            new Image(getClass().getResource("/img/GENERO/filtroActor.png").toExternalForm()),
            new Image(getClass().getResource("/img/GENERO/filtroDirector.png").toExternalForm()),
            new Image(getClass().getResource("/img/GENERO/filtroGenero.png").toExternalForm())
    );
    ObservableList<Image> top6peliculas = FXCollections.observableArrayList();
    int posIm1 = 0;
    int posIm2 = 1;
    int posIm3 = 2;
    int indicePelicula = 3;
    int idioma = 0;
    String [][]frases;
    ArrayList<Label> labelGenerados = new ArrayList<>();
    @FXML
    private ComboBox<Image> iconosComboBox = new ComboBox<>();
    @FXML
    private ImageView imagenSeleccionada = new ImageView();
    @FXML
    private ChoiceBox<String> idiomasSelector = new ChoiceBox();
    @FXML
    private Label tituloBuscador = new Label();
    @FXML
    private Label topVentas = new Label();
    @FXML
    private Label todasPeliculas = new Label();
    @FXML
    private Label contacto = new Label();
    @FXML
    private Label siguenos = new Label();
    @FXML
    private Button buscar = new Button();
    @FXML
    private Button cargarMas = new Button();
    @FXML
    private TextField buscador = new TextField();
    @FXML
    private VBox panelStack = new VBox();
    @FXML
    private ScrollPane scrollPane = new ScrollPane();
    @FXML
    private ImageView imagen1 = new ImageView();
    @FXML
    private ImageView imagen2 = new ImageView();
    @FXML
    private ImageView imagen3 = new ImageView();
    @FXML
    private Button botonIzq = new Button();
    @FXML
    private Button botodDer = new Button();
    @FXML
    private StackPane stackPane;
    @FXML
    private HBox hBoxBotton;
    @FXML
    private VBox vBoxPadre;
    @FXML
    private ImageView imagen4;
    @FXML
    private ImageView imagen5;
    @FXML
    private ImageView imagen6;
    @FXML
    private Label titulo1;
    @FXML
    private Label titulo2;
    @FXML
    private Label titulo3;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        scrollPane.setFitToWidth(true); // Ajustar el ancho del ScrollPane al contenido

        panelStack.setVgrow(panelStack, javafx.scene.layout.Priority.ALWAYS);

        // Añadir un listener para cambiar el texto según la selección
        idiomasSelector.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Actualizar el texto según la selección
                for(int i = 0; i<frases[1].length; i++){
                    String id = frases[1][i];
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

        iconosComboBox.setCellFactory(param -> new ListCell<Image>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Image item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(item);
                    imageView.setFitWidth(35); // Ajusta el ancho deseado
                    imageView.setFitHeight(35); // Ajusta la altura deseada
                    setGraphic(imageView);
                }
            }
        });
        // Configura la celda de botón del ComboBox con el mismo Cell Factory
        iconosComboBox.setButtonCell(iconosComboBox.getCellFactory().call(null));

        // Establece la lista de imágenes en el ComboBox
        iconosComboBox.setValue(listaIconos.get(0));
        iconosComboBox.setItems(listaIconos);

        onClickCartel(imagen1);
        onClickCartel(imagen2);
        onClickCartel(imagen3);
        onClickCartel(imagen4);
        onClickCartel(imagen5);
        onClickCartel(imagen6);
    }
    public void cargarIdiomas(){
        idiomasSelector.setValue(frases[0][1]);
        for(String id : frases[1]){
            if(id!=null){
                listaIdiomas.add(id);
            }
        }
        idiomasSelector.setItems(listaIdiomas);
    }

    private void onClickCartel(ImageView imagen) {
        imagen.setOnMouseClicked(event -> {
            try {
                cambiarEscenaImagen(imagen.getImage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void pulsarCargarMas(ActionEvent event) {
        if(indicePelicula!=peliculas.size()){
            vBoxPadre.setPrefHeight(vBoxPadre.getPrefHeight()+547);
            HBox hbox1 = new HBox();

            // Configuración del HBox
            hbox1.setAlignment(Pos.CENTER);
            hbox1.setMaxHeight(Double.MAX_VALUE);
            hbox1.setMaxWidth(Double.MAX_VALUE);
            hbox1.setPrefHeight(100.0);
            hbox1.setPrefWidth(200.0);

            // Configuración de las imágenes
            ArrayList<ImageView> img = new ArrayList<>();
            boolean[] hayTitulo = new boolean[3];
            for(int i=0; i<3; i++){
                if(peliculas.size() > indicePelicula){
                    img.add(new ImageView(new Image(getClass().getResource(peliculas.get(indicePelicula).getRutaCartel()).toExternalForm())));
                    indicePelicula++;
                    hayTitulo[i]=true;
                    onClickCartel(img.get(i));
                } else {
                    img.add(new ImageView());
                    hayTitulo[i]=false;
                }
                img.get(i).setFitHeight(407.0);
                img.get(i).setFitWidth(324.0);
                img.get(i).setPickOnBounds(true);
                img.get(i).getStyleClass().add("Cartelera");
                HBox.setMargin(img.get(i), new Insets(20, 20, 20, 20));
                hbox1.getChildren().add(img.get(i));
            }
            vBoxPadre.getChildren().add(vBoxPadre.getChildren().indexOf(hBoxBotton),hbox1);

            HBox hbox2 = new HBox();

            // Configuración del HBox
            hbox2.setAlignment(Pos.CENTER);
            hbox2.setMaxHeight(Double.MAX_VALUE);
            hbox2.setMaxWidth(Double.MAX_VALUE);
            hbox2.setPrefHeight(100.0);
            hbox2.setPrefWidth(200.0);

            // Configuración de los Labels
            Label titulo1,titulo2, titulo3;
            if(hayTitulo[2]){
                titulo1 = crearLabel(peliculas.get(indicePelicula-3).getTitulos().get(idioma));
                titulo2 = crearLabel(peliculas.get(indicePelicula-2).getTitulos().get(idioma));
                titulo3 = crearLabel(peliculas.get(indicePelicula-1).getTitulos().get(idioma));
            } else if(hayTitulo[1]){
                titulo1 = crearLabel(peliculas.get(indicePelicula-2).getTitulos().get(idioma));
                titulo2 = crearLabel(peliculas.get(indicePelicula-1).getTitulos().get(idioma));
                titulo3 = crearLabel(peliculas.get(indicePelicula-1).getTitulos().get(idioma));
                titulo3.setVisible(false);
            } else {
                titulo1 = crearLabel(peliculas.get(indicePelicula-1).getTitulos().get(idioma));
                titulo2 = crearLabel(peliculas.get(indicePelicula-1).getTitulos().get(idioma));
                titulo3 = crearLabel(peliculas.get(indicePelicula-1).getTitulos().get(idioma));
                titulo2.setVisible(false);
                titulo3.setVisible(false);
            }
            // Añadir Labels al HBox con márgenes
            hbox2.getChildren().addAll(titulo1, titulo2, titulo3);
            HBox.setMargin(titulo1, new Insets(0, 40, 0, 40));
            HBox.setMargin(titulo2, new Insets(0, 40, 0, 40));
            HBox.setMargin(titulo3, new Insets(0, 40, 0, 40));

            vBoxPadre.getChildren().add(vBoxPadre.getChildren().indexOf(hBoxBotton),hbox2);
            actualizarLabels();
        }
    }
    private Label crearLabel(String text) {
        Label label = new Label(text);

        // Configuración del Label
        label.setWrapText(true);
        label.setAlignment(Pos.TOP_CENTER);
        label.setPrefHeight(81.0);
        label.setPrefWidth(286.0);
        label.getStyleClass().add("textoNegrita2");
        label.setTextFill(javafx.scene.paint.Color.WHITE);
        label.setFont(new javafx.scene.text.Font(26.0));

        establecerOnClickedLabel(label);
        labelGenerados.add(label);

        return label;
    }
    private void establecerOnClickedLabel(Label label){
        label.setOnMouseClicked(event -> {
            try {
                cambiarEscenaLabel(label.getText(),label.getScene());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    @FXML
    private void handleBotonDerecho(ActionEvent event) {
        posIm1 = (posIm1 + 1) % top6peliculas.size();
        posIm2 = (posIm2 + 1) % top6peliculas.size();
        posIm3 = (posIm3 + 1) % top6peliculas.size();
        actualizarImagenes();
    }
    @FXML
    void realizarBusqueda(ActionEvent event) throws IOException {
        if(!buscador.getText().equals("")){
            cambiarEscenaBusqueda();
        } else {
            mostrarVentanaEmergente();
        }
    }
    private void mostrarVentanaEmergente() {
        Stage ventanaEmergente = new Stage();
        ventanaEmergente.initModality(Modality.APPLICATION_MODAL);
        ventanaEmergente.setTitle(frases[17][idioma]);

        StackPane layout = new StackPane();
        layout.getChildren().add(new Label(frases[18][idioma]));

        Scene scene = new Scene(layout, 350, 200);
        ventanaEmergente.setScene(scene);

        ventanaEmergente.showAndWait();
    }
    @FXML
    private void handleBotonIzquierdo(ActionEvent event) {
        if (posIm1 == 0) {
            posIm1 = top6peliculas.size() - 1;
        } else {
            posIm1--;
        }
        if (posIm2 == 0) {
            posIm2 = top6peliculas.size() - 1;
        } else {
            posIm2--;
        }
        if (posIm3 == 0) {
            posIm3 = top6peliculas.size() - 1;
        } else {
            posIm3--;
        }
        actualizarImagenes();
    }

    public void pasarParametros(ArrayList<Pelicula> peliculas, ArrayList<Actor> actores, ArrayList<Genero> generos,
                                ArrayList<Director> directores, String[][] frases){
        this.peliculas = peliculas;
        this.actores = actores;
        this.generos = generos;
        this.directores = directores;
        this.frases = frases;
    }

    public void calcularTop6(){
        Collections.sort(peliculas, new Comparator<Pelicula>() {
            @Override
            public int compare(Pelicula pelicula1, Pelicula pelicula2) {
                // Ordenar de mayor a menor
                return Integer.compare(pelicula2.getVentas(), pelicula1.getVentas());
            }
        });

        // Imprimir la lista ordenada
        for (int i=0; i<6; i++) {
            top6peliculas.add(new Image(getClass().getResource(peliculas.get(i).getRutaCartel()).toExternalForm()));
        }
    }
    public void actualizarImagenes(){
        imagen1.setImage(top6peliculas.get(posIm1));
        imagen2.setImage(top6peliculas.get(posIm2));
        imagen3.setImage(top6peliculas.get(posIm3));
        imagen4.setImage(new Image(getClass().getResource(peliculas.get(0).getRutaCartel()).toExternalForm()));
        imagen5.setImage(new Image(getClass().getResource(peliculas.get(1).getRutaCartel()).toExternalForm()));
        imagen6.setImage(new Image(getClass().getResource(peliculas.get(2).getRutaCartel()).toExternalForm()));

        titulo1.setText(peliculas.get(0).getTitulos().get(idioma));
        titulo2.setText(peliculas.get(1).getTitulos().get(idioma));
        titulo3.setText(peliculas.get(2).getTitulos().get(idioma));

        for(Label l : labelGenerados){
            String titulo = l.getText();
            for(Pelicula p :peliculas){
                if(titulo==p.getTitulos().get(0) || titulo ==p.getTitulos().get(1)){
                    if(idioma == 1){
                        l.setText(p.getTitulos().get(0));
                    }
                }
            }
        }
        establecerOnClickedLabel(titulo1);
        establecerOnClickedLabel(titulo2);
        establecerOnClickedLabel(titulo3);
    }
    private void actualizarLabels(){
        titulo1.setText(peliculas.get(0).getTitulos().get(idioma));
        titulo2.setText(peliculas.get(1).getTitulos().get(idioma));
        titulo3.setText(peliculas.get(2).getTitulos().get(idioma));

        for(Label l : labelGenerados){
            String titulo = l.getText();
            for(Pelicula p :peliculas){
                if(titulo==p.getTitulos().get(0) || titulo ==p.getTitulos().get(1)){
                    l.setText(p.getTitulos().get(idioma));
                }
            }
        }
    }

    public void labelsPrincipalesActualizar() {
        idiomasSelector.setValue(frases[0][idioma]);
        tituloBuscador.setText(frases[2][idioma]);
        topVentas.setText(frases[3][idioma]);
        todasPeliculas.setText(frases[4][idioma]);
        contacto.setText(frases[6][idioma]);
        siguenos.setText(frases[7][idioma]);
        buscar.setText(frases[15][idioma]);
        cargarMas.setText(frases[5][idioma]);
        buscador.setPromptText(frases[16][idioma]);
    }

    private void cambiarEscenaImagen( Image img) throws IOException {
        Scene escena = imagen3.getScene();
        FXMLLoader loader = new FXMLLoader(VistaPelicula.class.getResource("vistaPelicula.fxml"));
        BorderPane root = loader.load();
        VistaPelicula controlador = loader.getController();
        String[] url = img.getUrl().split("/");
        String nombreImagen = url[url.length-1];
        for(Pelicula p : peliculas){
            String[] urlp = p.getRutaCartel().split("/");
            String nombrep = urlp[urlp.length-1];
            if(nombrep.equals(nombreImagen)){
                controlador.pasarParametros(p,idioma,frases);
            }
        }
        controlador.cargarPelicula();
        controlador.cargarIdiomas();
        controlador.labelsPrincipalesActualizar();
        escena.setRoot(root);
    }

    private void cambiarEscenaLabel(String titulo, Scene escena) throws IOException {
        FXMLLoader loader = new FXMLLoader(VistaPelicula.class.getResource("vistaPelicula.fxml"));
        BorderPane root = loader.load();
        VistaPelicula controlador = loader.getController();
        for(Pelicula p : peliculas){
            if(p.getTitulos().get(0).equals(titulo) || p.getTitulos().get(1).equals(titulo)){
                controlador.pasarParametros(p,idioma,frases);
            }
        }
        controlador.cargarPelicula();
        controlador.cargarIdiomas();
        controlador.labelsPrincipalesActualizar();
        escena.setRoot(root);
    }

    public void setIdioma(int idioma) {
        this.idioma = idioma;
    }

    private void cambiarEscenaBusqueda() throws IOException {
        Scene escena = imagen3.getScene();
        FXMLLoader loader = new FXMLLoader(Busqueda.class.getResource("busqueda.fxml"));
        BorderPane root = loader.load();
        Busqueda controlador = loader.getController();
        String busqueda = buscador.getText();
        String[] vcategoria = iconosComboBox.getSelectionModel().getSelectedItem().getUrl().split("/");
        String[] categoriaExtension = vcategoria[vcategoria.length-1].split("\\.");
        String categoria = categoriaExtension[0];
        controlador.pasarParametros(busqueda,idioma,categoria,peliculas,frases);
        controlador.cargarIdiomas();
        controlador.realizarBusqueda();
        controlador.cargarBusqueda();
        controlador.actualizarLabels();
        controlador.labelsPrincipalesActualizar();
        escena.setRoot(root);
    }

}