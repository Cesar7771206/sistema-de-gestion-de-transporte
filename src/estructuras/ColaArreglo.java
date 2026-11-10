/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import java.util.NoSuchElementException;

/**
 *
 * @author yimmy
 */
public class ColaArreglo <T>{

    private final T[] datos;
    private int frente;
    private int tamanio;

    @SuppressWarnings("unchecked")
    public ColaArreglo(int capacidad) {
        if (capacidad < 1) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a 0");
        }
        datos = (T[]) new Object[capacidad];
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    public boolean estaLlena() {
        return tamanio == datos.length;
    }

  
    public int getTamanio() {
        return tamanio;
    }

    public int getCapacidad() {
        return datos.length;
    }

    public void encolar(T dato) {
        if (estaLlena()) {
            throw new IllegalStateException("La cola está llena");
        }
        datos[(frente + tamanio) % datos.length] = dato;
        tamanio++;
    }

    public T desencolar() {
        if (estaVacia()) {
            throw new NoSuchElementException("La cola está vacía");
        }
        T dato = datos[frente];
        datos[frente] = null;
        frente = (frente + 1) % datos.length;
        tamanio--;
        return dato;
    }

    public T getFrente() {
        if (estaVacia()) {
            throw new NoSuchElementException("La cola está vacía");
        }
        return datos[frente];
    }

    
    public java.util.List<T> getElementos() {
        java.util.List<T> resultado = new java.util.ArrayList<>();
        for (int i = 0; i < tamanio; i++) {
            resultado.add(datos[(frente + i) % datos.length]);
        }
        return resultado;
    }

    public boolean isFull() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    public void dequeue() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    public void enqueue(String correo) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
