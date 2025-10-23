package com.example.cinemamarket;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Busqueda implements Initializable {
    private String categoria;
    private String busqueda;
    private ArrayList<Pelicula> peliculas;
    ObservableList<String> listaIdiomas = FXCollections.observableArrayList();
    ObservableList<Image> listaIconos = FXCollections.observableArrayList(
            new Image(getClass().getResource("/img/GENERO/buscarNombre.png").toExternalForm()),
            new Image(getClass().getResource("/img/GENERO/filtroActor.png").toExternalForm()),
            new Image(getClass().getResource("/img/GENERO/filtroDirector.png").toExternalForm()),
            new Image(getClass().getResource("/img/GENERO/filtroGenero.png").toExternalForm())
    );
    int idioma = 0;
    ArrayList<Label> labelGenerados = new ArrayList<>();
    ArrayList<Pelicula> resultado = new ArrayList<>();
    int indicePelicula = 0;
    String[][] frasesMatriz;
    @FXML
    private TextField buscador;

    @FXML
    private Button buscar;

    @FXML
    private Label contacto;

    @FXML
    private HBox hBoxPadre;

    @FXML
    private ComboBox<Image> iconosComboBox;

    @FXML
    private ChoiceBox<String> idiomasSelector;

    @FXML
    private ImageView imagenSeleccionada;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label siguenos;

    @FXML
    private Label tituloBuscador;

    @FXML
    private Label labelResultado;

    @FXML
    private VBox vBoxPadre;
    @FXML
    private ImageView home;
    @FXML
    private ImageView icInstagram;
    @FXML
    private ImageView icFacebook;
    @FXML
    private ImageView icX;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        home.setImage(new Image(getClass().getResource("/img/iconoCasa.png").toExternalForm()));
        scrollPane.setFitToWidth(true);
        anadirEnlaceImagen(icInstagram);
        anadirEnlaceImagen(icFacebook);
        anadirEnlaceImagen(icX);


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
        ventanaEmergente.setTitle(frasesMatriz[17][idioma]);

        StackPane layout = new StackPane();
        layout.getChildren().add(new Label(frasesMatriz[18][idioma]));

        Scene scene = new Scene(layout, 350, 200);
        ventanaEmergente.setScene(scene);

        ventanaEmergente.showAndWait();
    }
    @FXML
    void volverPrincipal(ActionEvent event) throws IOException, URISyntaxException {
        Scene escena = imagenSeleccionada.getScene();
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
        controlador.actualizarImagenes();
        controlador.cargarIdiomas();
        controlador.labelsPrincipalesActualizar();

        escena.setRoot(root);
    }
    private void anadirEnlaceImagen(ImageView imageView){

    }
    public void actualizarLabels(){
        for(Label l : labelGenerados){
            String titulo = l.getText();
            for(Pelicula p :peliculas){
                if(titulo==p.getTitulos().get(0) || titulo ==p.getTitulos().get(1)){
                    l.setText(p.getTitulos().get(idioma));
                }
            }
        }
    }
    public void pasarParametros(String busqueda, int idioma, String categoria, ArrayList<Pelicula> peliculas, String[][] frases){
        frasesMatriz=frases;
        this.busqueda = busqueda;
        this.idioma = idioma;
        this.categoria = categoria;
        this.peliculas = peliculas;
    }
    public void realizarBusqueda(){
        if(categoria.equals("buscarNombre")){
            for(Pelicula p : peliculas){
                if(p.getTitulos().get(0).toLowerCase().contains(busqueda.toLowerCase()) ||
                        p.getTitulos().get(1).toLowerCase().contains(busqueda.toLowerCase())){
                    resultado.add(p);
                }
            }
        } else if(categoria.equals("filtroActor")){
            for(Pelicula p : peliculas){
                for(Actor act : p.getActores()){
                    if(act.getNombre().toLowerCase().contains(busqueda.toLowerCase())){
                        resultado.add(p);
                    }
                }
            }
        } else if(categoria.equals("filtroDirector")){
            for(Pelicula p : peliculas){
                for(Director dir : p.getDirectores()){
                    if(dir.getNombre().toLowerCase().contains(busqueda.toLowerCase())){
                        resultado.add(p);
                    }
                }
            }
        } else if(categoria.equals("filtroGenero")){
            for(Pelicula p : peliculas){
                for(Genero gen : p.getGeneros()){
                    if(gen.getNombre().toLowerCase().contains(busqueda.toLowerCase())){
                        resultado.add(p);
                    }
                }
            }
        }
    }

    void cargarBusqueda() {
        while (indicePelicula<resultado.size()){
            vBoxPadre.setPrefHeight(vBoxPadre.getPrefHeight()+250);
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
                if(resultado.size() > indicePelicula){
                    img.add(new ImageView(new Image(getClass().getResource(resultado.get(indicePelicula).getRutaCartel()).toExternalForm())));
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
            vBoxPadre.getChildren().add(vBoxPadre.getChildren().indexOf(hBoxPadre),hbox1);

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
                titulo1 = crearLabel(resultado.get(indicePelicula-3).getTitulos().get(idioma));
                titulo2 = crearLabel(resultado.get(indicePelicula-2).getTitulos().get(idioma));
                titulo3 = crearLabel(resultado.get(indicePelicula-1).getTitulos().get(idioma));
            } else if(hayTitulo[1]){
                titulo1 = crearLabel(resultado.get(indicePelicula-2).getTitulos().get(idioma));
                titulo2 = crearLabel(resultado.get(indicePelicula-1).getTitulos().get(idioma));
                titulo3 = crearLabel(resultado.get(indicePelicula-1).getTitulos().get(idioma));
                titulo3.setVisible(false);
            } else {
                titulo1 = crearLabel(resultado.get(indicePelicula-1).getTitulos().get(idioma));
                titulo2 = crearLabel(resultado.get(indicePelicula-1).getTitulos().get(idioma));
                titulo3 = crearLabel(resultado.get(indicePelicula-1).getTitulos().get(idioma));
                titulo2.setVisible(false);
                titulo3.setVisible(false);
            }
            // Añadir Labels al HBox con márgenes
            hbox2.getChildren().addAll(titulo1, titulo2, titulo3);
            HBox.setMargin(titulo1, new Insets(0, 40, 0, 40));
            HBox.setMargin(titulo2, new Insets(0, 40, 0, 40));
            HBox.setMargin(titulo3, new Insets(0, 40, 0, 40));

            vBoxPadre.getChildren().add(vBoxPadre.getChildren().indexOf(hBoxPadre),hbox2);
            actualizarLabels();
        }
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
    public void cargarIdiomas(){
        idiomasSelector.setValue(frasesMatriz[0][1]);
        for(String id : frasesMatriz[1]){
            if(id!=null){
                listaIdiomas.add(id);
            }
        }
        idiomasSelector.setItems(listaIdiomas);
    }
    private void cambiarEscenaImagen( Image img) throws IOException {
        Scene escena = imagenSeleccionada.getScene();
        FXMLLoader loader = new FXMLLoader(VistaPelicula.class.getResource("vistaPelicula.fxml"));
        BorderPane root = loader.load();
        VistaPelicula controlador = loader.getController();
        String[] url = img.getUrl().split("/");
        String nombreImagen = url[url.length-1];
        for(Pelicula p : peliculas){
            String[] urlp = p.getRutaCartel().split("/");
            String nombrep = urlp[urlp.length-1];
            if(nombrep.equals(nombreImagen)){
                controlador.pasarParametros(p,idioma,frasesMatriz);
            }
        }
        controlador.cargarIdiomas();
        controlador.cargarPelicula();
        controlador.labelsPrincipalesActualizar();
        escena.setRoot(root);
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

    private void cambiarEscenaLabel(String titulo, Scene escena) throws IOException {
        FXMLLoader loader = new FXMLLoader(VistaPelicula.class.getResource("vistaPelicula.fxml"));
        BorderPane root = loader.load();
        VistaPelicula controlador = loader.getController();
        for(Pelicula p : peliculas){
            if(p.getTitulos().get(0).equals(titulo) || p.getTitulos().get(1).equals(titulo)){
                controlador.pasarParametros(p,idioma,frasesMatriz);
            }
        }
        controlador.cargarIdiomas();
        controlador.cargarPelicula();
        controlador.labelsPrincipalesActualizar();
        escena.setRoot(root);
    }

    private void cambiarEscenaBusqueda() throws IOException {
        Scene escena = imagenSeleccionada.getScene();
        FXMLLoader loader = new FXMLLoader(Busqueda.class.getResource("busqueda.fxml"));
        BorderPane root = loader.load();
        Busqueda controlador = loader.getController();
        String busqueda = buscador.getText();
        String[] vcategoria = iconosComboBox.getSelectionModel().getSelectedItem().getUrl().split("/");
        String[] categoriaExtension = vcategoria[vcategoria.length-1].split("\\.");
        String categoria = categoriaExtension[0];
        controlador.pasarParametros(busqueda,idioma,categoria,peliculas,frasesMatriz);
        controlador.cargarIdiomas();
        controlador.labelsPrincipalesActualizar();
        controlador.realizarBusqueda();
        controlador.cargarBusqueda();
        escena.setRoot(root);
    }

    public void labelsPrincipalesActualizar() {
        idiomasSelector.setValue(frasesMatriz[0][idioma]);
        labelResultado.setText(frasesMatriz[8][idioma]);
        tituloBuscador.setText(frasesMatriz[2][idioma]);
        contacto.setText(frasesMatriz[6][idioma]);
        siguenos.setText(frasesMatriz[7][idioma]);
        buscar.setText(frasesMatriz[15][idioma]);
        buscador.setPromptText(frasesMatriz[16][idioma]);
    }

}
