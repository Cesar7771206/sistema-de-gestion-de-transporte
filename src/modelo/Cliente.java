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
public class Cliente {
    private String ruc;
    private String nombre;
    private String direccion;
    
    
     // Comparadores para los botones "Ordenar por"
    public static final Comparator<Cliente> POR_RUC = Comparator.comparing(Cliente::getRuc);
    public static final Comparator<Cliente> POR_NOMBRE = Comparator.comparing(Cliente::getNombre);

    // Constructor 
    public Cliente(String ruc, String nombre, String Direccion) {
        setRuc(ruc);
        setNombre(nombre);
        setDireccion(direccion);
        
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        if (nombre == null){
            throw new IllegalArgumentException("El Nombre no puede ser nulo");
        }
        
        String n = ruc.trim().toUpperCase();
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
        if (nombre == null) {
            throw new IllegalArgumentException("El nombre no puede ser nulo");
        }
        String n = nombre.trim().toUpperCase();
        if (n.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio");
        }
        this.nombre = n;
    
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String Direccion) {
        
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
        return "Cliente{" + "ruc=" + ruc + ", nombre=" + nombre + ", direccion=" + direccion + '}';
    }

    

    
    
    
}
