package controlador;

import java.util.ArrayList;
import modelo.Viaje;

public class GestorViajes {

    private static ArrayList<Viaje> viajes = new ArrayList<>();

 
    public static void agregarViaje(Viaje viaje) {
        viajes.add(viaje);
    }

 
    public static ArrayList<Viaje> getViajes() {
        return viajes;
    }

   
    public static int generarId() {
        return viajes.size() + 1;
    }

    
    public static Viaje buscarPorContenedor(String codigo) {

        for (Viaje viaje : viajes) {

            if (viaje.getContenedor()
                    .getCodigo()
                    .equalsIgnoreCase(codigo)) {

                return viaje;
            }
        }

        return null;
    }
}