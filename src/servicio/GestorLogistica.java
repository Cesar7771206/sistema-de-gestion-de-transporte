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

    
    public static void agregarPlanta(Planta planta) {
        plantas.add(planta);
    }

    public static ArrayList<Planta> getPlantas() {
        return plantas;
    }

    
    public static void agregarTerminal(Terminal terminal) {
        terminales.add(terminal);
    }

    public static ArrayList<Terminal> getTerminales() {
        return terminales;
    }

    
    public static void agregarRuta(Ruta ruta) {
        rutas.add(ruta);
    }

    public static ArrayList<Ruta> getRutas() {
        return rutas;
    }
}
