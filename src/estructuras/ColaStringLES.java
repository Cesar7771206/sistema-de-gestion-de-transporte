/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import java.util.ArrayList;
import java.util.List;

/**
 * Cola implementada con Lista Enlazada Simple (LES) para la gestión
 * de códigos de contenedores en espera (FIFO).
 * Todo autocontenido en esta única clase con un nodo estático interno.
 *
 * @author yimmy, cesar
 */
public class ColaStringLES {

    private static class NodoString {
        private final String dato;
        private NodoString siguiente;

        public NodoString(String dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }

    private NodoString frente;
    private NodoString fin;
    private int tamanio;

    public ColaStringLES() {
        this.frente = null;
        this.fin = null;
        this.tamanio = 0;
    }

    public void enqueue(String e) {
        if (e == null) {
            return;
        }
        NodoString nuevo = new NodoString(e);
        if (fin == null) {
            frente = nuevo;
            fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
        tamanio++;
    }

    public String dequeue() {
        if (frente == null) {
            System.out.println("La cola esta vacia");
            return null;
        }
        String e = frente.dato;
        frente = frente.siguiente;
        if (frente == null) {
            fin = null;
        }
        tamanio--;
        return e;
    }

    public String peek() {
        return (frente != null) ? frente.dato : null;
    }

    public void clear() {
        frente = null;
        fin = null;
        tamanio = 0;
    }

    public boolean isEmpty() {
        return tamanio == 0;
    }

    public int size() {
        return tamanio;
    }

    public List<String> getElementos() {
        List<String> lista = new ArrayList<>();
        NodoString actual = frente;
        while (actual != null) {
            lista.add(actual.dato);
            actual = actual.siguiente;
        }
        return lista;
    }
}
