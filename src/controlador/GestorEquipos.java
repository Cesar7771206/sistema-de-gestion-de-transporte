package controlador;

import java.util.ArrayList;
import modelo.Tractor;
import modelo.Carreta;
import modelo.Contenedor;

public class GestorEquipos {

    private static ArrayList<Tractor> tractores = new ArrayList<>();
    private static ArrayList<Carreta> carretas = new ArrayList<>();
    private static ArrayList<Contenedor> contenedores = new ArrayList<>();

  
    public static void agregarTractor(Tractor tractor) {
        tractores.add(tractor);
    }

    public static ArrayList<Tractor> getTractores() {
        return tractores;
    }


    public static void agregarCarreta(Carreta carreta) {
        carretas.add(carreta);
    }

    public static ArrayList<Carreta> getCarretas() {
        return carretas;
    }

    public static void agregarContenedor(Contenedor contenedor) {
        contenedores.add(contenedor);
    }

    public static ArrayList<Contenedor> getContenedores() {
        return contenedores;
    }
}