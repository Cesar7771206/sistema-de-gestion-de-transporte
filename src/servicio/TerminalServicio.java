/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;
import java.util.ArrayList;
import java.util.List;
import modelo.Terminal;

/**
 *
 * @author cesar
 */
public class TerminalServicio {

    private List<Terminal> terminales;

    public TerminalServicio() {
        terminales = new ArrayList<>();
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
        terminales.add(new Terminal(ruc.trim(), nombre.trim(), direccion.trim()));
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
        return t != null && terminales.remove(t);
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
