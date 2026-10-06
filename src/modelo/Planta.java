/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.Comparator;

/**
 *
 * @author cesar
 */
public class Planta {
    
    private String ruc;
    private String nombre;
    private String direccion;
    
    public static final Comparator<Planta> POR_RUC = Comparator.comparing(Planta::getRuc);
    public static final Comparator<Planta> POR_NOMBRE = Comparator.comparing(Planta::getNombre);

    public Planta(String ruc, String nombre, String direccion) {
        setRuc(ruc);
        setNombre(nombre);
        setDireccion(direccion);
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        if (ruc == null){
        throw new IllegalArgumentException("El RUC no puede ser nulo");
            }
        String r =ruc.trim();
        if(ruc.matches("\\d{11}")){
            this.ruc=ruc;
            }else {
                     throw new IllegalArgumentException("El ruc debe tener solo 11 digitos");
                  }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre != null) {
            String n = nombre.trim().toUpperCase();
                if (n.isEmpty()) {
                    this.nombre = n;
                    } else {
                       throw new IllegalArgumentException("El nombre no puede estar vacio") ;
            }
       }
    }
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        if (direccion == null) {
            throw new IllegalArgumentException("La dirección no puede ser nula");
        }
        String d = direccion.trim().toUpperCase();
        if (d.isEmpty()) {
            throw new IllegalArgumentException("La dirección no puede estar vacía");
        }
        this.direccion = d;
    }

    @Override
    public String toString() {
        return "Planta{" + "ruc=" + ruc + ", nombre=" + nombre + ", direccion=" + direccion + '}';
    }
    
    
    
}
