package servicio;

import java.util.LinkedList;
import modelo.Viaje;

public class GestorViajes {

    private static LinkedList<Viaje> viajes = new LinkedList<>();

 
    public static void agregarViaje(Viaje viaje) {
        viajes.addLast(viaje);
    }

    public static void agregarViajeUrgente(Viaje viaje) {
        viajes.addFirst(viaje);
    }

 
    public static LinkedList<Viaje> getViajes() {
        return viajes;
    }

   
    public static int generarId() {

    int mayorId = 0;

    for (Viaje viaje : viajes) {
        if (viaje.getId() > mayorId) {
            mayorId = viaje.getId();
        }
    }

    return mayorId + 1;
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