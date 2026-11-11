/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import estructuras.ColaStringLES;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Entidad Terminal Portuaria / Extraportuaria.
 * Incluye cola enlazada simple (ColaStringLES) para los contenedores en espera de despacho.
 *
 * @author cesar, yimmy
 */
public class Terminal {
    private String ruc; 
    private String nombre;
    private String direccion;

    private final ColaStringLES contenedoresEnEspera = new ColaStringLES();
    private final Queue<String> colaContenedoresQueue = new LinkedList<>();

    public Terminal(String ruc, String nombre, String direccion) {
        setRuc(ruc);
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        if (ruc == null) {
            throw new IllegalArgumentException("El Ruc no puede ser nulo");
        }
        String r = ruc.trim();
        if (r.matches("\\d{11}")) {
            this.ruc = r;
        } else {
            throw new IllegalArgumentException("El ruc debe tener solo 11 digitos");
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void encolarContenedor(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException("El código del contenedor no puede estar vacío");
        }
        String cod = codigo.trim();
        contenedoresEnEspera.enqueue(cod);
        colaContenedoresQueue.offer(cod);
    }

    public String atenderContenedor() {
        if (contenedoresEnEspera.isEmpty()) {
            return null;
        }
        colaContenedoresQueue.poll();
        return contenedoresEnEspera.dequeue();
    }

    public String getProximoContenedor() {
        return contenedoresEnEspera.peek();
    }

    public int getCantidadContenedoresEnEspera() {
        return contenedoresEnEspera.size();
    }

    public List<String> getContenedoresEnEspera() {
        return contenedoresEnEspera.getElementos();
    }

    public Queue<String> getColaContenedoresQueue() {
        return colaContenedoresQueue;
    }

    @Override
    public String toString() {
        return nombre != null ? nombre : ruc;
    }
}
