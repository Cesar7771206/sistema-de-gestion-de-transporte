/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;
import estructuras.PilaTerminalLES;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import modelo.Terminal;

/**
 *
 * @author cesar
 */
public class TerminalServicio {

    private final LinkedList<Terminal> terminales = new LinkedList<>();
    private int posicion = -1; 

   
    private final PilaTerminalLES eliminadas = new PilaTerminalLES();

    public TerminalServicio() {
    }

    public List<Terminal> getTerminales() {
        return terminales;
    }

    
    public boolean esRucValido(String ruc) {
        return ruc != null && ruc.trim().matches("\\d{11}");
    }

    
    public boolean buscarTerminalPorRuc(String ruc) {
        if (ruc == null) {
            throw new IllegalArgumentException("La ruc no puede ser null");
        }
        for (Terminal t : terminales) {
            if (t.getRuc().equals(ruc)) {
                return true;
            }
        }
        return false;
    }

    public Terminal obtenerPorRuc(String ruc) {
        if (ruc == null) {
            return null;
        }
        for (Terminal t : terminales) {
            if (t.getRuc().equals(ruc.trim())) {
                return t;
            }
        }
        return null;
    }

    
    public boolean guardarTerminal(String ruc, String nombre, String direccion) {
        if (ruc == null || ruc.isBlank() || nombre == null || nombre.isBlank()
                || direccion == null || direccion.isBlank()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
        if (!esRucValido(ruc)) {
            throw new IllegalArgumentException("El RUC debe tener exactamente 11 dígitos");
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

    
    public boolean modificarTerminal(String ruc, String nombre, String direccion) {
        if (nombre == null || nombre.isBlank() || direccion == null || direccion.isBlank()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
        Terminal t = obtenerPorRuc(ruc);
        if (t == null) {
            return false;
        } 
        t.setNombre(nombre.trim());
        t.setDireccion(direccion.trim());
        return true; 
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
        return true;
    }

    
    public Terminal deshacerEliminacion() {
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

    public boolean hayEliminadas() {
        return !eliminadas.isEmpty();
    }

    public int getCantidadEliminadas() {
        return eliminadas.size();
    }

    
    public Terminal getTerminalActual() {
        return posicion == -1 ? null : terminales.get(posicion);
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

    
    public List<Terminal> filtrar(String texto) {
        if (texto == null || texto.isBlank()) {
            return new ArrayList<>(terminales);
        }
        String buscado = texto.trim().toLowerCase();
        List<Terminal> resultado = new ArrayList<>();
        for (Terminal x : terminales) {
            if (x.getRuc().contains(buscado) || x.getNombre().toLowerCase().contains(buscado)) {
                resultado.add(x);
            }
        }
        return resultado;
    }
}