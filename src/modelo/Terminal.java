/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;
import java.util.ArrayList;

/**
 *
 * @author cesar
 */
public class Terminal {
  
    private String ruc;
    private String nombre;
    private ArrayList<Ubicacion> ubicaciones;

    public Terminal(String ruc, String nombre) {
        this.ruc = ruc;
        this.nombre = nombre;
        this.ubicaciones = new ArrayList<>();
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Ubicacion> getUbicaciones() {
        return ubicaciones;
    }

    public void setUbicaciones(ArrayList<Ubicacion> ubicaciones) {
        this.ubicaciones = ubicaciones;
    }

    public void agregarUbicacion(Ubicacion ubicacion) {
        ubicaciones.add(ubicacion);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
