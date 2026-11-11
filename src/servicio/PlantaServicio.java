/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import estructuras.PilaArreglo;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;
import modelo.Planta;

/**
 * Servicio de gestión de Plantas.
 * Evidencia el uso de:
 * - Pila con arreglo (PilaArreglo) para el historial de eliminadas y operación deshacer (LIFO).
 * - Pila estándar (Stack<Planta>) de Java Collections.
 * - Cola con prioridad (PriorityQueue con Comparator POR_CARGA) para atención por carga de trabajo.
 *
 * @author cesar, yimmy
 */
public class PlantaServicio {
    private List<Planta> plantas;

    private static final int CAPACIDAD_DESHACER = 50;
    private final PilaArreglo eliminadas = new PilaArreglo(CAPACIDAD_DESHACER);
    private final Stack<Planta> pilaEliminadasStack = new Stack<>();

    // Lista donde se guardaran todas las plantas
    public PlantaServicio(List<Planta> plantas) {
        this.plantas = plantas;
    }

    public List<Planta> getPlantas() {
        return plantas;
    }

    public void guardarPlanta(String ruc, String nombre, String direccion) {
        Planta planta = new Planta(ruc, nombre, direccion);
        if (existePlanta(planta.getRuc())) {
            throw new IllegalArgumentException("Ya existe una planta con ese RUC");
        }
        plantas.add(planta);
    }

    public void editarPlanta(String ruc, String nombre, String direccion) {
        Planta planta = this.obtenerPlantaPorRuc(ruc);

        if (planta == null) {
            throw new IllegalArgumentException("La planta no existe");
        }
        new Planta(planta.getRuc(), nombre, direccion);

        planta.setNombre(nombre);
        planta.setDireccion(direccion);
    }

    public void eliminarPlanta(String ruc) {
        Planta planta = this.obtenerPlantaPorRuc(ruc);
        if (planta == null) {
            throw new IllegalArgumentException("La planta no existe");
        }
        plantas.remove(planta);
        if (!eliminadas.isFull()) {
            eliminadas.push(planta);
        }
        pilaEliminadasStack.push(planta);
    }

    public Planta deshacerEliminacion() {
        if (!pilaEliminadasStack.isEmpty()) {
            pilaEliminadasStack.pop();
        }
        if (eliminadas.isEmpty()) {
            return null;
        }
        Planta planta = eliminadas.pop();
        if (existePlanta(planta.getRuc())) {
            return null;
        }
        plantas.add(planta);
        return planta;
    }

    public Stack<Planta> getPilaEliminadasStack() {
        return pilaEliminadasStack;
    }

    public boolean hayEliminadas() {
        return !eliminadas.isEmpty();
    }

    public void programarCarga(String ruc, int dia, int turno, int cantidad) {
        Planta planta = obtenerPlantaPorRuc(ruc);
        if (planta == null) {
            throw new IllegalArgumentException("La planta no existe");
        }
        planta.setCarga(dia, turno, cantidad);
    }

    public PriorityQueue<Planta> colaPorCarga() {
        PriorityQueue<Planta> cola = new PriorityQueue<>(Planta.POR_CARGA);
        for (Planta p : plantas) {
            cola.offer(p);
        }
        return cola;
    }

    public List<Planta> plantasPorCarga() {
        PriorityQueue<Planta> cola = colaPorCarga();
        List<Planta> ordenadas = new ArrayList<>();
        while (!cola.isEmpty()) {
            ordenadas.add(cola.poll());
        }
        return ordenadas;
    }

    public Planta obtenerPlantaPorRuc(String ruc) {
        if (ruc == null) {
            throw new IllegalArgumentException("El RUC no puede ser nulo");
        }
        String rucBuscado = ruc.trim();
        for (Planta p : plantas) {
            if (p.getRuc().equals(rucBuscado)) {
                return p;
            }
        }
        return null;
    }

    public boolean existePlanta(String ruc) {
        return obtenerPlantaPorRuc(ruc) != null;
    }

    public List<Planta> ordenarPlantaPorRuc() {
        return plantas.stream().sorted(Planta.POR_RUC).toList();
    }

    public List<Planta> ordenarPlantaPorNombre() {
        return plantas.stream().sorted(Planta.POR_NOMBRE).toList();
    }
}
