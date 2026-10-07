/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author cesar
 */
public class Ruta {

    private int id;
    private Ubicacion cochera;
    private Terminal terminalRetiro;
    private Planta plantaCarga;
    private Terminal terminalFinal;
    private double tarifa;

    public Ruta(int id, Ubicacion cochera, Terminal terminalRetiro,
                Planta plantaCarga, Terminal terminalFinal, double tarifa) {

        this.id = id;
        this.cochera = cochera;
        this.terminalRetiro = terminalRetiro;
        this.plantaCarga = plantaCarga;
        this.terminalFinal = terminalFinal;
        this.tarifa = tarifa;
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
    }

    public Terminal getTerminalRetiro() {
        return terminalRetiro;
    }

    public void setTerminalRetiro(Terminal terminalRetiro) {
        this.terminalRetiro = terminalRetiro;
    }

    public Planta getPlantaCarga() {
        return plantaCarga;
    }

    public void setPlantaCarga(Planta plantaCarga) {
        this.plantaCarga = plantaCarga;
    }

    public Terminal getTerminalFinal() {
        return terminalFinal;
    }

    public void setTerminalFinal(Terminal terminalFinal) {
        this.terminalFinal = terminalFinal;
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