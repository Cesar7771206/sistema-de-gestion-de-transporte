/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import java.util.ArrayList;
import java.util.List;
import modelo.Cliente;

/**
 * Servicio de gestión de Clientes.
 * Evidencia el uso de:
 * - ArrayLists para la colección dinámica de clientes y búsquedas secuenciales.
 * - Cola circular implementada sobre un arreglo estático (Cliente[]) para los turnos de atención al cliente (FIFO).
 *
 * @author NILENY, cesar
 */
public class ClienteServicio {
    
    public static final int CAPACIDAD_COLA = 10;
    
    // Lista dinámica donde se guardan los clientes (ArrayList)
    private final ArrayList<Cliente> clientes;

    // Cola circular con arreglo: turnos de atención
    //   frente = posición del que se atiende primero
    //   fin    = posición del último que llegó
    //   enCola = cuántos clientes están esperando actualmente
    private final Cliente[] cola = new Cliente[CAPACIDAD_COLA];
    private int frente = 0;
    private int fin = -1;
    private int enCola = 0;

    public ClienteServicio() {
        this.clientes = new ArrayList<>();
    }

    public ClienteServicio(List<Cliente> inicial) {
        this.clientes = new ArrayList<>();
        if (inicial != null) {
            this.clientes.addAll(inicial);
        }
    }
    
    // Devuelve todos los clientes registrados
    public List<Cliente> getCliente() {
        return clientes;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }
    
    // Registra un cliente nuevo 
    public void guardarCliente(String ruc, String nombre, String direccion) {
        Cliente c = new Cliente(ruc, nombre, direccion);
        if (existeCliente(c.getRuc())) {
            throw new IllegalArgumentException("Ya existe un Cliente con ese RUC " + c.getRuc());
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
        if (estaEnCola(cliente)) {
            throw new IllegalArgumentException("No se puede eliminar el cliente: está esperando en la cola de atención");
        }
        clientes.remove(cliente);
    }

    // Buscar un cliente por Ruc (búsqueda secuencial)
    public Cliente obtenerClientePorRuc(String ruc) {
        if (ruc == null) {
            throw new IllegalArgumentException("El RUC no puede ser nulo");
        }
        String rucBuscado = ruc.trim();
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getRuc().equals(rucBuscado)) {
                return clientes.get(i);
            }
        }
        return null;
    }

    public boolean existeCliente(String ruc) {
        return obtenerClientePorRuc(ruc) != null;
    }
    
    // Método ordenar las plantas por RUC
    public List<Cliente> ordenarClientePorRuc() {
        return clientes.stream().sorted(Cliente.POR_RUC).toList();
    }
    
    // Método ordenar las plantas por Nombre
    public List<Cliente> ordenarClientePorNombre() {
        return clientes.stream().sorted(Cliente.POR_NOMBRE).toList();
    }

    // ==========================================
    // OPERACIONES DE LA COLA CIRCULAR DE ATENCIÓN
    // ==========================================

    public boolean colaVacia() {
        return enCola == 0;
    }

    public boolean colaLlena() {
        return enCola == CAPACIDAD_COLA;
    }

    public int cantidadEnCola() {
        return enCola;
    }

    public void pedirAtencion(String ruc) {
        Cliente c = obtenerClientePorRuc(ruc);
        if (c == null) {
            throw new IllegalArgumentException("No se encontró un cliente con RUC " + ruc);
        }
        if (colaLlena()) {
            throw new IllegalStateException("La cola de atención está llena (máx " + CAPACIDAD_COLA + ")");
        }
        if (estaEnCola(c)) {
            throw new IllegalArgumentException("El cliente ya se encuentra en la cola de atención");
        }

        fin = (fin + 1) % CAPACIDAD_COLA;
        cola[fin] = c;
        enCola++;
    }

    public Cliente atenderSiguiente() {
        if (colaVacia()) {
            throw new IllegalStateException("No hay clientes en la cola de atención");
        }
        Cliente atendido = cola[frente];
        cola[frente] = null;
        frente = (frente + 1) % CAPACIDAD_COLA;
        enCola--;
        return atendido;
    }

    public Cliente proximoEnCola() {
        return colaVacia() ? null : cola[frente];
    }

    public boolean estaEnCola(Cliente c) {
        if (colaVacia() || c == null) {
            return false;
        }
        for (int i = 0; i < enCola; i++) {
            int indice = (frente + i) % CAPACIDAD_COLA;
            if (cola[indice].getRuc().equals(c.getRuc())) {
                return true;
            }
        }
        return false;
    }

    public List<Cliente> obtenerCola() {
        List<Cliente> lista = new ArrayList<>();
        for (int i = 0; i < enCola; i++) {
            int indice = (frente + i) % CAPACIDAD_COLA;
            lista.add(cola[indice]);
        }
        return lista;
    }
}
