/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/**
 *
 * @author yimmy
 */
public class ColaStringLES {
    private LinkedList<String> elementos;

    public ColaStringLES() {
        elementos = new LinkedList<>();
    }

    public void enqueue(String e) {
        elementos.addLast(e);
    }

    public String dequeue() {
        if (elementos.size() > 0) {
            String e = elementos.getFirst();
            elementos.remove(0);
            return e;
        } else {
            System.out.println("La cola esta vacia");
            return null;
        }
    }

    public String peek() {
        if (elementos.size() > 0) {
            return elementos.getFirst();
        } else {
            return null;
        }
    }

    public void clear() {
        elementos.clear();
    }

    public boolean isEmpty() {
        return elementos.size() == 0;
    }

    public int size() {
        return elementos.size();
    }

    
    public List<String> getElementos() {
        return new ArrayList<>(elementos);
    }
}
