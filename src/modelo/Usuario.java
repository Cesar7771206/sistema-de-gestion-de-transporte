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
public class Usuario {
    private String dni;
    private String nombre;
    private Direccion direccionCochera;
    private String correo;
    private String contrasenia;

    public static final Comparator<Usuario> POR_NOMBRE = Comparator.comparing(Usuario::getNombre);
    public static final Comparator<Usuario> POR_DNI = Comparator.comparing(Usuario::getDni);

    public Usuario(String dni, String nombre, Direccion direccionCochera,
                   String correo, String contrasenia) {
        setDni(dni);
        setNombre(nombre);
        setDireccionCochera(direccionCochera);
        setCorreo(correo);
        setContrasenia(contrasenia);
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        if (dni != null) {
            String dniLimpio = dni.trim();
            if (!dniLimpio.isEmpty()) {
                this.dni = dniLimpio;
            }
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre != null) {
            String nombreLimpio = nombre.trim();
            if (!nombreLimpio.isEmpty()) {
                this.nombre = nombreLimpio;
            }
        }
    }

    public Direccion getDireccionCochera() {
        return direccionCochera;
    }

    public void setDireccionCochera(Direccion direccionCochera) {
        if (direccionCochera != null) {
            this.direccionCochera = direccionCochera;
        }
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        if (correo != null) {
            String correoLimpio = correo.trim();
            if (!correoLimpio.isEmpty()) {
                this.correo = correoLimpio;
            }
        }
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        if (contrasenia != null && !contrasenia.isEmpty()) {
            this.contrasenia = contrasenia;
        }
    }
}