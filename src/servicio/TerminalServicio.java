/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import estructuras.PilaTerminalLES;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import modelo.Terminal;

/**
 * Servicio de gestión de Terminales.
 * Evidencia el uso de:
 * - Lista enlazada doble (LinkedList de Terminales con puntero de navegación bidireccional: primera, anterior, siguiente, última).
 * - Pila enlazada (PilaTerminalLES) para almacenar el historial de terminales eliminadas y permitir deshacer (LIFO).
 * - Pila estándar de Java (Stack<Terminal>).
 *
 * @author cesar, yimmy
 */
public class TerminalServicio {

    private final LinkedList<Terminal> terminales;
    private int posicion = -1; 

    private final PilaTerminalLES eliminadas = new PilaTerminalLES();
    private final Stack<Terminal> pilaEliminadasStack = new Stack<>();

    public TerminalServicio() {
        this.terminales = new LinkedList<>();
    }

    public TerminalServicio(List<Terminal> listaInicial) {
        this.terminales = new LinkedList<>();
        if (listaInicial != null) {
            for (Terminal t : listaInicial) {
                guardarTerminal(t.getRuc(), t.getNombre(), t.getDireccion());
            }
        }
    }

    public List<Terminal> getTerminales() {
        return new ArrayList<>(terminales);
    }

    public LinkedList<Terminal> getTerminalesLinkedList() {
        return terminales;
    }

    public Terminal obtenerPorRuc(String ruc) {
        for (Terminal t : terminales) {
            if (t.getRuc().equals(ruc)) {
                return t;
            }
        }
        return null;
    }

    public boolean buscarTerminalPorRuc(String ruc) {
        return obtenerPorRuc(ruc) != null;
    }

    public boolean guardarTerminal(String ruc, String nombre, String direccion) {
        if (ruc == null || nombre == null || direccion == null) {
            return false;
        }
        if (buscarTerminalPorRuc(ruc.trim())) {
            return false;
        }
        Terminal nueva = new Terminal(ruc.trim(), nombre.trim(), direccion.trim());
        terminales.addLast(nueva);
        if (posicion == -1) {
            posicion = 0;
        }
        return true;
    }

    public boolean editarPorRuc(String ruc, String nombre, String direccion) {
        Terminal t = obtenerPorRuc(ruc);
        if (t == null) {
            return false;
        } 
        t.setNombre(nombre.trim());
        t.setDireccion(direccion.trim());
        return true; 
    }

    public boolean modificarTerminal(String ruc, String nombre, String direccion) {
        return editarPorRuc(ruc, nombre, direccion);
    }

    public boolean eliminarPorRuc(String ruc) {
        Terminal t = obtenerPorRuc(ruc);
        if (t == null) {
            return false;
        }
        int indice = terminales.indexOf(t);
        terminales.remove(indice);
        if (terminales.isEmpty()) {
            posicion = -1;
        } else if (indice < posicion || posicion >= terminales.size()) {
            posicion--;
        }
        eliminadas.push(t);
        pilaEliminadasStack.push(t);
        return true;
    }

    public Terminal deshacerEliminacion() {
        if (!pilaEliminadasStack.isEmpty()) {
            pilaEliminadasStack.pop();
        }
        if (eliminadas.isEmpty()) {
            return null;
        }
        Terminal t = eliminadas.pop();
        if (buscarTerminalPorRuc(t.getRuc())) {
            return null;
        }
        terminales.addLast(t);
        if (posicion == -1) {
            posicion = 0;
        }
        return t;
    }

    public Stack<Terminal> getPilaEliminadasStack() {
        return pilaEliminadasStack;
    }

    public boolean hayEliminadas() {
        return !eliminadas.isEmpty();
    }

    public int getCantidadEliminadas() {
        return eliminadas.size();
    }

    // Navegación interactiva de Lista Enlazada Doble (LED)
    public Terminal getTerminalActual() {
        return (posicion < 0 || posicion >= terminales.size()) ? null : terminales.get(posicion);
    }

    public int getPosicionActual() {
        return posicion;
    }

    public Terminal irPrimera() {
        posicion = terminales.isEmpty() ? -1 : 0;
        return getTerminalActual();
    }

    public Terminal irUltima() {
        posicion = terminales.size() - 1;
        return getTerminalActual();
    }

    public Terminal irSiguiente() {
        if (posicion != -1 && posicion < terminales.size() - 1) {
            posicion++;
        }
        return getTerminalActual();
    }

    public Terminal irAnterior() {
        if (posicion > 0) {
            posicion--;
        }
        return getTerminalActual();
    }

    public List<Terminal> buscarPorNombre(String texto) {
        List<Terminal> resultado = new ArrayList<>();
        if (texto == null || texto.isBlank()) {
            return resultado;
        }
        String buscado = texto.toLowerCase().trim();
        for (Terminal t : terminales) {
            if (t.getNombre().toLowerCase().contains(buscado)) {
                resultado.add(t);
            }
        }
        return resultado;
    }
}
