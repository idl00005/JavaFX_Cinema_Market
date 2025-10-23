package com.example.cinemamarket;

import java.util.ArrayList;

public class Pelicula {
    private ArrayList<String> titulos;
    private ArrayList<String> descripciones;
    private ArrayList<Director> directores = new ArrayList<>();
    private ArrayList<Genero> generos = new ArrayList<>();
    private ArrayList<Actor> actores = new ArrayList<>();
    private int duracion;
    private int ventas;
    private int precio;
    private String rutaCartel;
    private String rutaTrailer;

    public Pelicula() {
    }

    public void setTitulos(ArrayList<String> titulos) {
        this.titulos = titulos;
    }

    public void setDescripciones(ArrayList<String> descripciones) {
        this.descripciones = descripciones;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setVentas(int ventas) {
        this.ventas = ventas;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public void setRutaCartel(String rutaCartel) {
        this.rutaCartel = rutaCartel;
    }

    public void setRutaTrailer(String rutaTrailer) {
        this.rutaTrailer = rutaTrailer;
    }

    public ArrayList<Director> getDirectores() {
        return directores;
    }

    public ArrayList<Genero> getGeneros() {
        return generos;
    }

    public ArrayList<Actor> getActores() {
        return actores;
    }

    public int getDuracion() {
        return duracion;
    }

    public int getVentas() {
        return ventas;
    }

    public int getPrecio() {
        return precio;
    }

    public String getRutaCartel() {
        return rutaCartel;
    }

    public String getRutaTrailer() {
        return rutaTrailer;
    }

    public void anadirActor(Actor act){
        actores.add(act);
    }
    public void anadirGenero(Genero gen){
        generos.add(gen);
    }
    public void anadirDirector(Director dir){
        directores.add(dir);
    }

    public ArrayList<String> getTitulos() {
        return titulos;
    }

    public ArrayList<String> getDescripciones() {
        return descripciones;
    }
}
