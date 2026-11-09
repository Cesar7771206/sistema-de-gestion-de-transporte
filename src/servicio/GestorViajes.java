
package servicio;

import java.util.LinkedList;
import java.util.Stack;
import java.util.Queue;
import modelo.Viaje;

public class GestorViajes {

    // LISTA ENLAZADA: almacena todos los viajes
    private static LinkedList<Viaje> viajes = new LinkedList<>();

    // PILA: almacena los viajes eliminados
    private static Stack<Viaje> pilaEliminados = new Stack<>();

    // COLA: almacena los viajes pendientes
    private static Queue<Viaje> colaPendientes = new LinkedList<>();

    // AGREGAR VIAJE NORMAL
    public static void agregarViaje(Viaje viaje) {
        viajes.addLast(viaje);
        colaPendientes.offer(viaje);
    }

    // AGREGAR VIAJE URGENTE
    public static void agregarViajeUrgente(Viaje viaje) {
        viajes.addFirst(viaje);
        colaPendientes.offer(viaje);
    }

    // OBTENER TODOS LOS VIAJES
    public static LinkedList<Viaje> getViajes() {
        return viajes;
    }

    // ELIMINAR VIAJE Y GUARDARLO EN LA PILA
    public static boolean eliminarViaje(Viaje viaje) {

        if (viajes.remove(viaje)) {
            pilaEliminados.push(viaje);
            colaPendientes.remove(viaje);
            return true;
        }

        return false;
    }

    // RECUPERAR EL ÚLTIMO VIAJE ELIMINADO
    public static Viaje recuperarViaje() {

        if (!pilaEliminados.empty()) {

            Viaje viaje = pilaEliminados.pop();

            viajes.addLast(viaje);
            colaPendientes.offer(viaje);

            return viaje;
        }

        return null;
    }

    // ATENDER EL SIGUIENTE VIAJE
    public static Viaje atenderSiguienteViaje() {
        return colaPendientes.poll();
    }

    // CONSULTAR SIGUIENTE VIAJE PENDIENTE
    public static Viaje getSiguientePendiente() {
        return colaPendientes.peek();
    }

    // CONSULTAR CANTIDAD DE VIAJES PENDIENTES
    public static int getCantidadPendientes() {
        return colaPendientes.size();
    }
    // GENERAR ID AUTOMÁTICO PARA LOS VIAJES
public static int generarId() {

    int mayor = 0;

    for (Viaje viaje : viajes) {
        if (viaje.getId() > mayor) {
            mayor = viaje.getId();
        }
    }

    return mayor + 1;
}

public static LinkedList<Viaje> getColaPendientes() {
    return new LinkedList<>(colaPendientes);
}
}
