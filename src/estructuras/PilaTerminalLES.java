/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

import java.util.LinkedList;
import modelo.Terminal;
/**
 *
 * @author yimmy
 */
public class PilaTerminalLES {
    private LinkedList<Terminal> elementos;

    public PilaTerminalLES() {
        elementos = new LinkedList<>();
    }

    public void push(Terminal e) {
        elementos.addLast(e);
    }

    public Terminal pop() {
        if (elementos.size() > 0) {
            Terminal e = elementos.getLast();
            elementos.remove(elementos.size() - 1);
            return e;
        } else {
            System.out.println("La pila esta vacia");
            return null;
        }
    }

    public Terminal peek() {
        if (elementos.size() > 0) {
            return elementos.getLast();
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
}
