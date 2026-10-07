/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import java.util.List;
import modelo.Cliente;

/**
 *
 * @author NILENY
 */
public class ClienteServicio {
    
    private List<Cliente> clientes;

    //Lista donde se guardaran todas los clientes
    public ClienteServicio(List<Cliente> cliente) {
        this.clientes = cliente;
    }
    
    //Devuelve todos los clientes registrados
    public List<Cliente> getCliente() {
       return clientes;
    }
    
    // Registra un cliente nuevo 
    public void guardarCliente(String ruc, String nombre, String direccion) {
    Cliente c = new Cliente (ruc, nombre, direccion);
    if (existeCliente(c.getRuc())) {
        throw new IllegalArgumentException("Ya existe un Cliente con ese RUC");
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
        clientes.remove(cliente);
    }

    //Buscar un cliente por Ruc
    public Cliente obtenerClientePorRuc(String ruc) {
        if (ruc == null) {
            throw new IllegalArgumentException("El RUC no puede ser nulo");
        }
        String rucBuscado = ruc.trim();
        for (Cliente c : clientes) {
            if (c.getRuc().equals(rucBuscado)) {
                return c;
            }
        }
        return null;
    }

    //Responde Si/No a ¿Existe una planta con ese RUC?
    
    public boolean existeCliente(String ruc) {
    return obtenerClientePorRuc(ruc) != null;
    }
    
    // Método ordenar las plantas por RUC
    public List<Cliente> ordenarClientePorRuc() {
        return clientes.stream().sorted(Cliente.POR_RUC).toList();
    }

    
    //Método ordenar las plantas por Nombre
    public List<Cliente> ordenarClientePorNombre() {
        return clientes.stream().sorted(Cliente.POR_NOMBRE).toList();
    
    }
    
}
