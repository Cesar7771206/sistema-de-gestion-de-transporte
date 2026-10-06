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
    
    public boolean buscarTerminalPorRuc(String ruc){
        if (ruc == null){
            throw new IllegalArgumentException("La ruc no puede ser null");
        }
        
        for (Terminal t : terminales){
            if (t.getRuc().equals(ruc)){
                return true;
            }
        }
        
        return false;
    }
    
    public boolean guardarTerminal(String ruc, String nombre, String direccion){
        
        if (buscarTerminalPorRuc(ruc)){
            return false;
        }
        
        Terminal t = new Terminal(ruc, nombre, direccion);
        terminales.add(t);
        return true;
    }
}
