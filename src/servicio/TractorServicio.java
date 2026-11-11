/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import estructuras.PilaTractorLES;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import modelo.Tractor;

/**
 * Servicio de gestión de Tractores.
 * Evidencia el uso de:
 * - ArrayList (ArrayList<Tractor>) para la colección dinámica de tractores y catálogo.
 * - Pila (Stack<Tractor>) de la biblioteca estándar de Java para deshacer eliminaciones (LIFO).
 * - PilaTractorLES para evidenciar una pila sobre Lista Enlazada Simple propia con nodo interno.
 * - Cola (Queue<Tractor> implementada con LinkedList) para el despacho de tractores en patio (FIFO).
 *
 * @author cesar
 */
public class TractorServicio {

    // COLECCIÓN DINÁMICA: ArrayList
    private final List<Tractor> tractors;

    // PILA ESTÁNDAR: Stack<Tractor> para deshacer (LIFO)
    private final Stack<Tractor> pilaEliminados = new Stack<>();

    // PILA PROPIA: PilaTractorLES implementada con lista enlazada simple
    private final PilaTractorLES pilaTractorLES = new PilaTractorLES();

    // COLA ESTÁNDAR: Queue<Tractor> con LinkedList para despacho en patio (FIFO)
    private final Queue<Tractor> colaPatio = new LinkedList<>();

    public TractorServicio() {
        this.tractors = new ArrayList<>();
    }

    public List<Tractor> getTractors() {
        return Collections.unmodifiableList(tractors);
    }

    public List<Tractor> getListaTractors() {
        return new ArrayList<>(tractors);
    }

    public Tractor getTractorByPlaca(String placa) {
        if (placa == null || placa.isBlank()) {
            throw new IllegalArgumentException("La placa ingresada no puede estar vacía");
        }
        for (Tractor t : tractors) {
            if (t.getPlaca().trim().equalsIgnoreCase(placa)) {
                return t;
            }
        }
        return null;
    }

    public void registrarTractor(String placa, int ejes, String marca, String modelo, String carroceria, int anio, String MTC) {
        if (getTractorByPlaca(placa) != null) {
            throw new IllegalStateException("Ya existe un tractor con la placa " + placa);
        }
        Tractor nuevo = new Tractor(placa, ejes, marca, modelo, carroceria, anio, MTC);
        tractors.add(nuevo);
    }

    public void actualizarTractor(String placa, int ejes, String marca, String modelo, String carroceria, int anio, String MTC) {
        Tractor t = getTractorByPlaca(placa);
        if (t == null) {
            throw new IllegalStateException("No existe un tractor con la placa " + placa);
        }
        t.ActualizarTodosAtributos(ejes, marca, modelo, carroceria, anio, MTC);
    }

    public Tractor eliminarTractor(String placa) {
        Tractor t = getTractorByPlaca(placa);
        if (t == null) {
            throw new IllegalStateException("No existe un tractor con la placa " + placa);
        }
        tractors.remove(t);
        pilaEliminados.push(t);
        pilaTractorLES.push(t);
        colaPatio.remove(t);
        return t;
    }

    // ==========================================
    // OPERACIONES DE PILA: DESHACER ELIMINACIÓN (LIFO)
    // ==========================================

    public Tractor deshacerEliminacion() {
        if (pilaEliminados.isEmpty()) {
            return null;
        }
        Tractor t = pilaEliminados.pop();
        if (!pilaTractorLES.isEmpty()) {
            pilaTractorLES.pop();
        }
        if (getTractorByPlaca(t.getPlaca()) == null) {
            tractors.add(t);
        }
        return t;
    }

    public boolean hayEliminados() {
        return !pilaEliminados.isEmpty();
    }

    public int getCantidadEliminados() {
        return pilaEliminados.size();
    }

    public Stack<Tractor> getPilaEliminados() {
        return pilaEliminados;
    }

    public PilaTractorLES getPilaTractorLES() {
        return pilaTractorLES;
    }

    // ==========================================
    // OPERACIONES DE COLA: PATIO DE DESPACHO (FIFO)
    // ==========================================

    public void encolarPatio(String placa) {
        Tractor t = getTractorByPlaca(placa);
        if (t == null) {
            throw new IllegalArgumentException("No existe un tractor con la placa " + placa);
        }
        if (colaPatio.contains(t)) {
            throw new IllegalStateException("El tractor con placa " + placa + " ya está esperando en el patio");
        }
        colaPatio.offer(t);
    }

    public Tractor despacharSiguientePatio() {
        return colaPatio.poll();
    }

    public Tractor getSiguientePatio() {
        return colaPatio.peek();
    }

    public int getCantidadEnPatio() {
        return colaPatio.size();
    }

    public boolean estaEnPatio(Tractor t) {
        return colaPatio.contains(t);
    }

    public Queue<Tractor> getColaPatio() {
        return colaPatio;
    }

    public List<Tractor> getListaPatio() {
        return new ArrayList<>(colaPatio);
    }

    // ==========================================
    // ORDENAMIENTOS
    // ==========================================

    public List<Tractor> ordenarTractorsPorPlaca() {
        return tractors.stream()
                .sorted(Tractor.POR_PLACA).toList();
    }

    public List<Tractor> ordenarTractorsPorEjes() {
        return tractors.stream()
                .sorted(Tractor.POR_EJES).toList();
    }
}
