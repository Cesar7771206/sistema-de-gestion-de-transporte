/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import modelo.Planta;

/**
 *
 * @author yimmy
 */
public class PilaArreglo {
    private Planta[] elementos;
    private int top;

    public PilaArreglo(int capacidad) {
        if (capacidad < 1) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a 0");
        }
        this.elementos = new Planta[capacidad];
        this.top = -1;
    }

    public void push(Planta e) {
        if (top == elementos.length - 1) {
            System.out.println("La pila está llena");
        } else {
            top++;
            elementos[top] = e;
        }
    }

    public Planta pop() {
        if (top == -1) {
            return null;
        } else {
            Planta e = elementos[top];
            elementos[top] = null;
            top--;
            return e;
        }
    }

    public Planta peek() {
        if (top == -1) {
            return null;
        } else {
            return elementos[top];
        }
    }

    public int size() {
        return top + 1;
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isFull() {
        return top == elementos.length - 1;
    }

    public void clear() {
        for (int i = 0; i < elementos.length; i++) {
            elementos[i] = null;
        }
        top = -1;
    }

    public int getCapacidad() {
        return elementos.length;
    }
}
