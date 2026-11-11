/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import java.util.ArrayList;
import java.util.List;
import modelo.Terminal;

/**
 * Pila implementada con Lista Enlazada Simple (LES) para gestionar el historial
 * de terminales eliminadas (LIFO).
 * Todo autocontenido en esta única clase con un nodo estático interno.
 *
 * @author yimmy, cesar
 */
public class PilaTerminalLES {

    private static class NodoTerminal {
        private final Terminal dato;
        private NodoTerminal siguiente;

        public NodoTerminal(Terminal dato, NodoTerminal siguiente) {
            this.dato = dato;
            this.siguiente = siguiente;
        }
    }

    private NodoTerminal cima;
    private int tamanio;

    public PilaTerminalLES() {
        this.cima = null;
        this.tamanio = 0;
    }

    public void push(Terminal e) {
        if (e != null) {
            cima = new NodoTerminal(e, cima);
            tamanio++;
        }
    }

    public Terminal pop() {
        if (cima == null) {
            System.out.println("La pila esta vacia");
            return null;
        }
        Terminal e = cima.dato;
        cima = cima.siguiente;
        tamanio--;
        return e;
    }

    public Terminal peek() {
        if (cima != null) {
            return cima.dato;
        } else {
            return null;
        }
    }

    public void clear() {
        cima = null;
        tamanio = 0;
    }

    public boolean isEmpty() {
        return cima == null;
    }

    public int size() {
        return tamanio;
    }

    public List<Terminal> getElementos() {
        List<Terminal> lista = new ArrayList<>();
        NodoTerminal actual = cima;
        while (actual != null) {
            lista.add(actual.dato);
            actual = actual.siguiente;
        }
        return lista;
    }
}
