package servicio;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import modelo.Viaje;

/**
 * Gestor global de Viajes de transporte.
 * Evidencia el uso de:
 * - Lista Enlazada (LinkedList<Viaje>) para el almacenamiento y recorrido secuencial de todos los viajes.
 * - Pila (Stack<Viaje>) para almacenar los viajes eliminados y permitir su recuperación (LIFO).
 * - Cola (Queue<Viaje> como LinkedList) para despachar y atender los viajes en orden de llegada (FIFO).
 *
 * @author Diego, cesar
 */
public class GestorViajes {

    // LISTA ENLAZADA: almacena todos los viajes
    private static LinkedList<Viaje> viajes = new LinkedList<>();

    // PILA: almacena los viajes eliminados para recuperarlos (LIFO)
    private static Stack<Viaje> pilaEliminados = new Stack<>();

    // COLA: almacena los viajes pendientes por atender (FIFO)
    private static Queue<Viaje> colaPendientes = new LinkedList<>();

    // AGREGAR VIAJE NORMAL AL FINAL DE LA LISTA Y ENCOLARLO
    public static void agregarViaje(Viaje viaje) {
        viajes.addLast(viaje);
        colaPendientes.offer(viaje);
    }

    // AGREGAR VIAJE URGENTE AL INICIO DE LA LISTA Y ENCOLARLO CON PRIORIDAD
    public static void agregarViajeUrgente(Viaje viaje) {
        viajes.addFirst(viaje);
        colaPendientes.offer(viaje);
    }

    // OBTENER TODOS LOS VIAJES (LISTA ENLAZADA)
    public static LinkedList<Viaje> getViajes() {
        return viajes;
    }

    // ELIMINAR VIAJE Y GUARDARLO EN LA PILA DE DESHACER
    public static boolean eliminarViaje(Viaje viaje) {
        if (viajes.remove(viaje)) {
            pilaEliminados.push(viaje);
            colaPendientes.remove(viaje);
            return true;
        }
        return false;
    }

    // RECUPERAR EL ÚLTIMO VIAJE ELIMINADO DESDE LA PILA (POP)
    public static Viaje recuperarViaje() {
        if (!pilaEliminados.empty()) {
            Viaje viaje = pilaEliminados.pop();
            viajes.addLast(viaje);
            colaPendientes.offer(viaje);
            return viaje;
        }
        return null;
    }

    public static boolean hayViajesEliminados() {
        return !pilaEliminados.isEmpty();
    }

    public static int getCantidadEliminados() {
        return pilaEliminados.size();
    }

    // ATENDER EL SIGUIENTE VIAJE DE LA COLA (POLL)
    public static Viaje atenderSiguienteViaje() {
        return colaPendientes.poll();
    }

    // CONSULTAR SIGUIENTE VIAJE PENDIENTE (PEEK)
    public static Viaje getSiguientePendiente() {
        return colaPendientes.peek();
    }

    // CONSULTAR CANTIDAD DE VIAJES PENDIENTES
    public static int getCantidadPendientes() {
        return colaPendientes.size();
    }

    public static LinkedList<Viaje> getColaPendientes() {
        return new LinkedList<>(colaPendientes);
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

    public static Viaje buscarPorContenedor(String codigo) {
        for (Viaje viaje : viajes) {
            if (viaje.getContenedor() != null
                    && viaje.getContenedor().getCodigo().equalsIgnoreCase(codigo)) {
                return viaje;
            }
        }
        return null;
    }

    public static Viaje buscarPorId(int id) {
        for (Viaje viaje : viajes) {
            if (viaje.getId() == id) {
                return viaje;
            }
        }
        return null;
    }
}
