/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import java.util.ArrayList;
import java.util.List;
import modelo.Cliente;

/**
 *
 * @author NILENY
 */
public class ClienteServicio {
    
    public static final int CAPACIDAD_COLA=10;
    
    //lista donde se guardan los clientes
    private final ArrayList<Cliente> clientes = new ArrayList<>();

    // Cola circular con arreglo: turnos de atención
    //   frente = posición del que se atiende primero
    //   fin    = posición del último que llegó (-1 si nunca entró nadie)
    //   enCola = cuántos clientes están esperando    
    
    private final Cliente[] cola = new Cliente[CAPACIDAD_COLA];
    private int frente = 0;
    private int fin = -1;
    private int enCola = 0;
    
    
    //Devuelve todos los clientes registrados
    public List<Cliente> getCliente() {
       return clientes;
    }
    
    // Registra un cliente nuevo 
    public void guardarCliente(String ruc, String nombre, String direccion) {
    Cliente c = new Cliente (ruc, nombre, direccion);
    if (existeCliente(c.getRuc())) {
        throw new IllegalArgumentException("Ya existe un Cliente con ese RUC" + c.getRuc());
    }
    clientes.add(c);
}

    // Modifica nombre y dirección de un cliente existente
    public void editarCliente(String ruc, String nombre, String direccion) {
    Cliente cliente = obtenerClientePorRuc(ruc);
    if (cliente == null) {
        throw new IllegalArgumentException("El cliente no existe");
    }
    new Cliente(cliente.getRuc(), nombre, direccion);

    cliente.setNombre(nombre);
    cliente.setDireccion(direccion);
    }

    // Elimina el cliente con el ruc buscado 
    public void eliminarCliente(String ruc) {
        Cliente cliente = this.obtenerClientePorRuc(ruc);
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no existe");
        }
        if (estaEnCola(cliente)){
            throw new IllegalArgumentException("No se puede eliminar el cliente esta esperando en la cola de atención");
        }
        clientes.remove(cliente);
    }

    //Buscar un cliente por Ruc
    public Cliente obtenerClientePorRuc(String ruc) {
        if (ruc == null) {
            throw new IllegalArgumentException("El RUC no puede ser nulo");
        }
        String rucBuscado = ruc.trim();
        for (int i = 0; i < clientes.size();i ++) {
            if (clientes.get(i).getRuc().equals(rucBuscado)) {
                return clientes.get(i);
            }
        }
        return null;
    }

    //Responde Si/No a ¿Existe un cliente con ese RUC?
    
    public boolean existeCliente(String ruc) {
    return obtenerClientePorRuc(ruc) != null;
    }
    
    // Método ordenar los clientes por RUC y Nombre
    public List<Cliente> ordenarClientePorRuc() {
        return insercion(new ArrayList<>(clientes), "ruc");
    }

    public List<Cliente> ordenarClientePorNombre() {
        return insercion(new ArrayList<>(clientes), "nombre");
    }
    
    private List<Cliente> insercion(ArrayList<Cliente> lista, String criterio) {
        for (int i = 1; i < lista.size(); i++) {
            Cliente actual = lista.get(i);
            int j = i - 1;
            while (j >= 0 && clave(lista.get(j), criterio).compareTo(clave(actual, criterio)) > 0) {
                lista.set(j + 1, lista.get(j)); // desplaza a la derecha
                j--;
            }
            lista.set(j + 1, actual);           // coloca en su lugar
        }
        return lista;
    }
    
     private String clave(Cliente c, String criterio) {
        if (criterio.equals("ruc")) {
            return c.getRuc();
        }
        return c.getNombre();
    }
     
    public boolean colaVacia() {
        return enCola == 0;
    }
 
    public boolean colaLlena() {
        return enCola == cola.length;
    }
 
    public int cantidadEnCola() {
        return enCola;
    }
    
    //peek
    
     public Cliente siguienteEnCola() {
        return colaVacia() ? null : cola[frente];
    }
    
    // encolar
    public void pedirAtencion(String ruc) {
        Cliente c = obtenerClientePorRuc(ruc);
        if (c == null) {
            throw new IllegalArgumentException("El cliente no existe");
        }
        if (estaEnCola(c)) {
            throw new IllegalArgumentException(c.getNombre() + " ya está en la cola");
        }
        if (colaLlena()) {
            throw new IllegalArgumentException("La cola está llena (" + CAPACIDAD_COLA + " clientes)");
        }
        fin = (fin + 1) % cola.length;   // avanza en círculo: después de 9 vuelve a 0
        cola[fin] = c;
        enCola++;
    }
     
    //Desencolar
    public Cliente atenderSiguiente() {
        if (colaVacia()) {
            throw new IllegalStateException("No hay clientes en la cola");
        }
        Cliente atendido = cola[frente];
        cola[frente] = null;
        frente = (frente + 1) % cola.length;  // avanza en círculo
        enCola--;
        return atendido;
    }
    
     public Cliente[] getCola() {
        Cliente[] resultado = new Cliente[enCola];
        for (int k = 0; k < enCola; k++) {
            resultado[k] = cola[(frente + k) % cola.length];
        }
        return resultado;
    }
     
    //¿Clienet está en la cola?
     
     public boolean estaEnCola(Cliente c) {
        for (int k = 0; k < enCola; k++) {
            if (cola[(frente + k) % cola.length] == c) {
                return true;
            }
        }
        return false;
    }
}
