/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Ruta de Transporte.
 * Evidencia el uso de:
 * - Lista Enlazada Circular Simple (LECS): El itinerario o circuito cerrado de paradas
 *   (Cochera -> Terminal Retiro -> Planta Carga -> Terminal Devolución -> vuelta a Cochera)
 *   donde el puntero 'siguiente' del último nodo apunta nuevamente al primer nodo.
 *
 * @author cesar
 */
public class Ruta {

    public static class ParadaCircuito {
        private String nombre;
        private String tipo;
        private ParadaCircuito siguiente;

        public ParadaCircuito(String nombre, String tipo) {
            this.nombre = nombre;
            this.tipo = tipo;
            this.siguiente = null;
        }

        public String getNombre() {
            return nombre;
        }

        public String getTipo() {
            return tipo;
        }

        public ParadaCircuito getSiguiente() {
            return siguiente;
        }

        public void setSiguiente(ParadaCircuito siguiente) {
            this.siguiente = siguiente;
        }

        @Override
        public String toString() {
            return "[" + tipo + "] " + nombre;
        }
    }

    private int id;
    private Ubicacion cochera;
    private Terminal terminalRetiro;
    private Planta plantaCarga;
    private Terminal terminalFinal;
    private double tarifa;

    // Punteros para la Lista Enlazada Circular Simple (LECS)
    private ParadaCircuito paradaInicio;
    private ParadaCircuito paradaActual;
    private int totalParadas;

    public Ruta(int id, Ubicacion cochera, Terminal terminalRetiro,
                Planta plantaCarga, Terminal terminalFinal, double tarifa) {

        this.id = id;
        this.cochera = cochera;
        this.terminalRetiro = terminalRetiro;
        this.plantaCarga = plantaCarga;
        this.terminalFinal = terminalFinal;
        this.tarifa = tarifa;
        construirCircuitoCircular();
    }

    public final void construirCircuitoCircular() {
        String nomCochera = (cochera != null) ? (cochera.getDireccion() + " (" + cochera.getDistrito() + ")") : "Cochera Principal";
        String nomRetiro = (terminalRetiro != null) ? terminalRetiro.getNombre() : "Terminal Retiro";
        String nomPlanta = (plantaCarga != null) ? plantaCarga.getNombre() : "Planta Carga";
        String nomDevolucion = (terminalFinal != null) ? terminalFinal.getNombre() : "Terminal Devolución";

        ParadaCircuito n1 = new ParadaCircuito(nomCochera, "COCHERA ORIGEN");
        ParadaCircuito n2 = new ParadaCircuito(nomRetiro, "TERMINAL RETIRO");
        ParadaCircuito n3 = new ParadaCircuito(nomPlanta, "PLANTA CARGA");
        ParadaCircuito n4 = new ParadaCircuito(nomDevolucion, "TERMINAL FINAL");

        // Enlaces de la lista circular simple (el último nodo apunta al primero)
        n1.setSiguiente(n2);
        n2.setSiguiente(n3);
        n3.setSiguiente(n4);
        n4.setSiguiente(n1);

        this.paradaInicio = n1;
        this.paradaActual = n1;
        this.totalParadas = 4;
    }

    /**
     * Avanza al siguiente hito del recorrido circularmente (LECS).
     */
    public ParadaCircuito avanzarParada() {
        if (paradaActual != null) {
            paradaActual = paradaActual.getSiguiente();
        }
        return paradaActual;
    }

    public ParadaCircuito getParadaActual() {
        return paradaActual;
    }

    public List<ParadaCircuito> getItinerarioCircular() {
        List<ParadaCircuito> lista = new ArrayList<>();
        if (paradaInicio != null) {
            ParadaCircuito cur = paradaInicio;
            for (int i = 0; i < totalParadas; i++) {
                lista.add(cur);
                cur = cur.getSiguiente();
            }
        }
        return lista;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Ubicacion getCochera() {
        return cochera;
    }

    public void setCochera(Ubicacion cochera) {
        this.cochera = cochera;
        construirCircuitoCircular();
    }

    public Terminal getTerminalRetiro() {
        return terminalRetiro;
    }

    public void setTerminalRetiro(Terminal terminalRetiro) {
        this.terminalRetiro = terminalRetiro;
        construirCircuitoCircular();
    }

    public Planta getPlantaCarga() {
        return plantaCarga;
    }

    public void setPlantaCarga(Planta plantaCarga) {
        this.plantaCarga = plantaCarga;
        construirCircuitoCircular();
    }

    public Terminal getTerminalFinal() {
        return terminalFinal;
    }

    public void setTerminalFinal(Terminal terminalFinal) {
        this.terminalFinal = terminalFinal;
        construirCircuitoCircular();
    }

    public double getTarifa() {
        return tarifa;
    }

    public void setTarifa(double tarifa) {
        this.tarifa = tarifa;
    }

    @Override
    public String toString() {
        return "Ruta " + id;
    }
}
