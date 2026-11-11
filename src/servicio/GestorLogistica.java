package servicio;

import java.util.ArrayList;
import modelo.Cliente;
import modelo.Planta;
import modelo.Terminal;
import modelo.Ruta;

public class GestorLogistica {

    private static ArrayList<Cliente> clientes = new ArrayList<>();
    private static ArrayList<Planta> plantas = new ArrayList<>();
    private static ArrayList<Terminal> terminales = new ArrayList<>();
    private static ArrayList<Ruta> rutas = new ArrayList<>();

    public static void agregarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    public static ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public static Cliente buscarClientePorRuc(String ruc) {
        if (ruc == null) return null;
        for (Cliente c : clientes) {
            if (c.getRuc().equalsIgnoreCase(ruc.trim())) {
                return c;
            }
        }
        return null;
    }

    public static boolean eliminarCliente(String ruc) {
        if (ruc == null) return false;
        return clientes.removeIf(c -> c.getRuc().equalsIgnoreCase(ruc.trim()));
    }
    
    public static void agregarPlanta(Planta planta) {
        plantas.add(planta);
    }

    public static ArrayList<Planta> getPlantas() {
        return plantas;
    }

    public static Planta buscarPlantaPorRuc(String ruc) {
        if (ruc == null) return null;
        for (Planta p : plantas) {
            if (p.getRuc().equalsIgnoreCase(ruc.trim())) {
                return p;
            }
        }
        return null;
    }

    public static boolean eliminarPlanta(String ruc) {
        if (ruc == null) return false;
        return plantas.removeIf(p -> p.getRuc().equalsIgnoreCase(ruc.trim()));
    }
    
    public static void agregarTerminal(Terminal terminal) {
        terminales.add(terminal);
    }

    public static ArrayList<Terminal> getTerminales() {
        return terminales;
    }

    public static Terminal buscarTerminalPorRuc(String ruc) {
        if (ruc == null) return null;
        for (Terminal t : terminales) {
            if (t.getRuc().equalsIgnoreCase(ruc.trim())) {
                return t;
            }
        }
        return null;
    }

    public static boolean eliminarTerminal(String ruc) {
        if (ruc == null) return false;
        return terminales.removeIf(t -> t.getRuc().equalsIgnoreCase(ruc.trim()));
    }

    
    public static void agregarRuta(Ruta ruta) {
        rutas.add(ruta);
    }

    public static boolean eliminarRuta(int id) {
        return rutas.removeIf(r -> r.getId() == id);
    }

    public static Ruta buscarRutaPorId(int id) {
        for (Ruta r : rutas) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    public static ArrayList<Ruta> getRutas() {
        return rutas;
    }
}
