package com.example.cinemamarket;

import java.util.ArrayList;

public class Director {
    private String nombre;
    private ArrayList<Pelicula> peliculas = new ArrayList<>();

    public Director(String nombre) {
        this.nombre = nombre;
    }

    public void anadirPelicula(Pelicula pelicula){
        peliculas.add(pelicula);
    }
    public String getNombre() {
        return nombre;
    }

    public ArrayList<Pelicula> getPeliculas() {
        return peliculas;
    }
}
