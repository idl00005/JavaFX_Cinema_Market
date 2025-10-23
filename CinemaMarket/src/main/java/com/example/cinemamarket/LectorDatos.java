package com.example.cinemamarket;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

public class LectorDatos {
    private ArrayList<Pelicula> ArrPeliculas;
    private ArrayList<Actor> ArrActores;
    private ArrayList<Director> ArrDirectores;
    private ArrayList<Genero> ArrGeneros;
    private String[][] frases;

    public LectorDatos(ArrayList<Pelicula> peliculas, ArrayList<Actor> actores, ArrayList<Director> directores, ArrayList<Genero> generos, String[][] frases) {
        this.ArrPeliculas = peliculas;
        this.ArrActores = actores;
        this.ArrDirectores = directores;
        this.ArrGeneros = generos;
        this.frases = frases;
    }

    public void leerDatos(String url) throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream(url);
        XSSFWorkbook libro = new XSSFWorkbook(input);

        XSSFSheet hoja = libro.getSheetAt(0);
        Iterator<Row> fila = hoja.rowIterator();
        Iterator<Cell> columnas = null;

        if(fila.hasNext()) fila.next();
        do{
            columnas = fila.next().iterator();
            ArrayList<String> nombre = new ArrayList<>();
            ArrayList<String> descripcion = new ArrayList<>();
            nombre.add(columnas.next().getStringCellValue());
            descripcion.add(columnas.next().getStringCellValue());
            String director = columnas.next().getStringCellValue();
            String genero = columnas.next().getStringCellValue();
            String actor = columnas.next().getStringCellValue();
            int duracion = (int) columnas.next().getNumericCellValue();
            String rutaCartel = columnas.next().getStringCellValue();
            String rutaTrailer = columnas.next().getStringCellValue();
            int numVentas = (int) columnas.next().getNumericCellValue();
            int precio = (int) columnas.next().getNumericCellValue();
            while(columnas.hasNext()){
                nombre.add(columnas.next().getStringCellValue());
                descripcion.add(columnas.next().getStringCellValue());
            }
            String[] directores = director.split(", ");
            String[] generos = genero.split(", ");
            String[] actores = actor.split(", ");
            Pelicula peli = new Pelicula();
            for(String dirNombre : directores){
                Director obDir = new Director(dirNombre);
                Director busqueda = yaEstaDirector(obDir);
                if(busqueda!=null){
                    busqueda.anadirPelicula(peli);
                } else {
                    obDir.anadirPelicula(peli);
                    peli.anadirDirector(obDir);
                    ArrDirectores.add(obDir);
                }
            }
            for(String actNombre : actores){
                Actor obAct = new Actor(actNombre);
                Actor busqueda = yaEstaActor(obAct);
                if(busqueda!=null){
                    busqueda.anadirPelicula(peli);
                } else {
                    obAct.anadirPelicula(peli);
                    peli.anadirActor(obAct);
                    ArrActores.add(obAct);
                }
            }
            for(String genNombre : generos){
                Genero obGen = new Genero(genNombre);
                Genero busqueda = yaEstaGenero(obGen);
                if(busqueda!=null){
                    busqueda.anadirPelicula(peli);
                } else {
                    obGen.anadirPelicula(peli);
                    peli.anadirGenero(obGen);
                    ArrGeneros.add(obGen);
                }
            }
            peli.setTitulos(nombre);
            peli.setDescripciones(descripcion);
            peli.setDuracion(duracion);
            peli.setPrecio(precio);
            peli.setVentas(numVentas);
            peli.setRutaCartel(rutaCartel);
            peli.setRutaTrailer(rutaTrailer);
            ArrPeliculas.add(peli);
        } while (fila.hasNext());

        XSSFSheet hoja2 = libro.getSheetAt(1);
        fila = hoja2.rowIterator();
        columnas = null;

        columnas = fila.next().iterator();
        int numIdiomas = (int) columnas.next().getNumericCellValue();
        int j=0;
        while (fila.hasNext()){
            columnas = fila.next().iterator();
            for(int i=0; i<numIdiomas; i++){
                frases[j][i] = columnas.next().getStringCellValue();
            }
            j++;
        }


        input.close();
        libro.close();
    }

    Genero yaEstaGenero( Genero gen){
        for(Genero it : ArrGeneros ){
            if(it.getNombre().equals(gen.getNombre())){
                return it;
            }
        }
        return null;
    }
    Actor yaEstaActor( Actor act){
        for(Actor it : ArrActores ){
            if(it.getNombre().equals(act.getNombre())){
                return it;
            }
        }
        return null;
    }
    Director yaEstaDirector( Director dir){
        for(Director it : ArrDirectores ){
            if(it.getNombre().equals(dir.getNombre())){
                return it;
            }
        }
        return null;
    }
}

