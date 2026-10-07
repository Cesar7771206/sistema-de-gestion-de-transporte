/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author cesar
 */
public class Terminal {
    private String ruc;
    private String nombre;
    private String direccion;

    public Terminal(String ruc, String nombre, String direccion) {
        setRuc(ruc);
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        if (ruc != null && ruc.matches("\\d+")) {
            this.ruc = ruc;
        } else {
            throw new IllegalArgumentException("El RUC solo puede estar conformado por números");
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
