/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import java.util.ArrayList;
import java.util.List; 
import modelo.Contenedor; 

/**
 * Servicio de gestión de Contenedores.
 * Evidencia el uso de:
 * - Lista Enlazada Simple (LES): Almacena y enlaza los contenedores mediante nodos enlazados
 *   unidireccionalmente (cabeza -> siguiente -> ... -> null), con operaciones de inserción,
 *   búsqueda por puntero y eliminación enlazada.
 *
 * @author cesar
 */
public class ContenedorServicio {

    /**
     * Nodo de la Lista Enlazada Simple de Contenedores.
     */
    public static class NodoContenedor {
        private Contenedor contenedor;
        private NodoContenedor siguiente;

        public NodoContenedor(Contenedor contenedor, NodoContenedor siguiente) {
            this.contenedor = contenedor;
            this.siguiente = siguiente;
        }

        public Contenedor getContenedor() {
            return contenedor;
        }

        public void setContenedor(Contenedor contenedor) {
            this.contenedor = contenedor;
        }

        public NodoContenedor getSiguiente() {
            return siguiente;
        }

        public void setSiguiente(NodoContenedor siguiente) {
            this.siguiente = siguiente;
        }
    }

    private NodoContenedor cabeza;
    private int tamanio;

    public ContenedorServicio() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    public void insertarInicio(Contenedor c) {
        if (c == null) {
            throw new IllegalArgumentException("El contenedor no puede ser nulo");
        }
        if (obtenerContenedorPörCodigo(c.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un contenedor con el código " + c.getCodigo());
        }
        cabeza = new NodoContenedor(c, cabeza);
        tamanio++;
    }

    public void insertarFinal(Contenedor c) {
        if (c == null) {
            throw new IllegalArgumentException("El contenedor no puede ser nulo");
        }
        if (obtenerContenedorPörCodigo(c.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un contenedor con el código " + c.getCodigo());
        }
        NodoContenedor nuevo = new NodoContenedor(c, null);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            NodoContenedor actual = cabeza;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo);
        }
        tamanio++;
    }

    public List<Contenedor> getContenedores() {
        List<Contenedor> lista = new ArrayList<>();
        NodoContenedor actual = cabeza;
        while (actual != null) {
            lista.add(actual.getContenedor());
            actual = actual.getSiguiente();
        }
        return lista;
    }
    
    public void guardarContenedor(String codigo, double payload, double tara) {
        Contenedor c = new Contenedor(codigo, payload, tara);
        insertarFinal(c);
    }
    
    public void editarContenedor(String codigo, double payload, double tara) {
        Contenedor contenedor = this.obtenerContenedorPörCodigo(codigo);
        if (contenedor == null) {
            throw new IllegalArgumentException("El contenedor no existe");
        }
        contenedor.setPayload(payload);
        contenedor.setTara(tara);
    }
    
    public void eliminarContenedor(String codigo) {
        if (codigo == null || cabeza == null) {
            throw new IllegalArgumentException("El contenedor no existe");
        }
        String codigoBuscado = codigo.trim();

        if (cabeza.getContenedor().getCodigo().equalsIgnoreCase(codigoBuscado)) {
            cabeza = cabeza.getSiguiente();
            tamanio--;
            return;
        }

        NodoContenedor anterior = cabeza;
        NodoContenedor actual = cabeza.getSiguiente();

        while (actual != null) {
            if (actual.getContenedor().getCodigo().equalsIgnoreCase(codigoBuscado)) {
                anterior.setSiguiente(actual.getSiguiente());
                tamanio--;
                return;
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }

        throw new IllegalArgumentException("El contenedor no existe");
    }
    
    public Contenedor obtenerContenedorPörCodigo(String codigo) {
        if (codigo == null) {
            return null;
        }
        String codigoBuscado = codigo.trim();
        NodoContenedor actual = cabeza;
        while (actual != null) {
            if (actual.getContenedor().getCodigo().equalsIgnoreCase(codigoBuscado)) {
                return actual.getContenedor();
            }
            actual = actual.getSiguiente();
        }
        return null;
    }
    
    public List<Contenedor> ordenarContenedorPorCodigo() {
        return getContenedores().stream().sorted(Contenedor.POR_CODIGO).toList();
    }
    
    public List<Contenedor> ordenarContenedorPorPayload() {
        return getContenedores().stream().sorted(Contenedor.POR_PAYLOAD).toList();
    }
    
    public List<Contenedor> ordenarContenedorPorTara() {
        return getContenedores().stream().sorted(Contenedor.POR_TARA).toList();
    } 
}
