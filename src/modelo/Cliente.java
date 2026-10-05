/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author cesar
 */
public class Cliente {

    private String ruc;
    private String nombre;
    private Ubicacion direccionFiscal;

    public Cliente(String ruc, String nombre, Ubicacion direccionFiscal) {
        this.ruc = ruc;
        this.nombre = nombre;
        this.direccionFiscal = direccionFiscal;
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

    public Ubicacion getDireccionFiscal() {
        return direccionFiscal;
    }

    public void setDireccionFiscal(Ubicacion direccionFiscal) {
        this.direccionFiscal = direccionFiscal;
    }

    @Override
    public String toString() {
        return nombre;
    }
}