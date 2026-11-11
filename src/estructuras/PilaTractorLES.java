/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import java.util.ArrayList;
import java.util.List;
import modelo.Tractor;

/**
 * Pila implementada mediante una Lista Enlazada Simple (LES) dedicada
 * exclusivamente a la entidad Tractor.
 *
 * Sigue el principio LIFO (Last In, First Out) para gestionar el historial
 * de tractores eliminados y permitir su restauración.
 * Todo autocontenido en esta única clase mediante un nodo estático interno.
 *
 * @author cesar
 */
public class PilaTractorLES {

    /**
     * Nodo interno de la Lista Enlazada Simple para Tractores.
     */
    private static class NodoTractor {
        private final Tractor dato;
        private NodoTractor siguiente;

        public NodoTractor(Tractor dato, NodoTractor siguiente) {
            this.dato = dato;
            this.siguiente = siguiente;
        }
    }

    private NodoTractor cima;
    private int tamanio;

    public PilaTractorLES() {
        this.cima = null;
        this.tamanio = 0;
    }

    /**
     * Apila un tractor en la cima (LIFO).
     *
     * @param tractor Tractor a apilar
     */
    public void push(Tractor tractor) {
        if (tractor == null) {
            throw new IllegalArgumentException("No se puede apilar un tractor nulo");
        }
        cima = new NodoTractor(tractor, cima);
        tamanio++;
    }

    /**
     * Desapila y retorna el tractor en la cima.
     *
     * @return Tractor desapilado o null si la pila está vacía
     */
    public Tractor pop() {
        if (isEmpty()) {
            return null;
        }
        Tractor desapilado = cima.dato;
        cima = cima.siguiente;
        tamanio--;
        return desapilado;
    }

    /**
     * Consulta el tractor en la cima sin removerlo.
     *
     * @return Tractor en la cima o null si está vacía
     */
    public Tractor peek() {
        return isEmpty() ? null : cima.dato;
    }

    /**
     * Determina si la pila está vacía.
     */
    public boolean isEmpty() {
        return cima == null;
    }

    /**
     * Retorna la cantidad de elementos apilados.
     */
    public int size() {
        return tamanio;
    }

    /**
     * Limpia la pila por completo.
     */
    public void clear() {
        cima = null;
        tamanio = 0;
    }

    /**
     * Retorna una lista con los elementos desde la cima hacia la base.
     */
    public List<Tractor> getElementos() {
        List<Tractor> lista = new ArrayList<>();
        NodoTractor actual = cima;
        while (actual != null) {
            lista.add(actual.dato);
            actual = actual.siguiente;
        }
        return lista;
    }
}
