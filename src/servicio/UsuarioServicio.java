/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import estructuras.ColaArreglo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import modelo.Direccion;
import modelo.Usuario;

/**
 * Servicio de gestión de Usuarios y Autenticación.
 * Evidencia el uso de:
 * - Cola circular sobre arreglo estático (ColaArreglo) para almacenar los últimos accesos exitosos.
 * - Lista enlazada (LinkedList) como bitácora cronológica de eventos (accesos, fallos y registros).
 *
 * @author yimmy, cesar
 */
public class UsuarioServicio {

    private static final Pattern PATRON_CORREO =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private static final Pattern PATRON_DNI =
            Pattern.compile("^\\d{8}$");
    private static final int LARGO_MIN_CONTRASENIA = 4;

    private static final int MAX_ULTIMOS_ACCESOS = 5;

    private List<Usuario> usuarios;

    private final LinkedList<String> bitacora = new LinkedList<>();

    private final ColaArreglo<String> ultimosAccesos = new ColaArreglo<>(MAX_ULTIMOS_ACCESOS);

    public UsuarioServicio() {
        usuarios = new ArrayList<>();
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void guardarUsuario(String dni, String nombre, Direccion direccionCochera,
                               String correo, String contrasenia) {
        if (dni == null || dni.isBlank() || nombre == null || nombre.isBlank()
                || correo == null || correo.isBlank()
                || contrasenia == null || contrasenia.isEmpty()
                || direccionCochera == null) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
        if (!esDniValido(dni)) {
            throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos numéricos");
        }
        if (!esCorreoValido(correo)) {
            throw new IllegalArgumentException("El formato del correo no es válido");
        }
        if (contrasenia.length() < LARGO_MIN_CONTRASENIA) {
            throw new IllegalArgumentException("La contraseña debe tener al menos "
                    + LARGO_MIN_CONTRASENIA + " caracteres");
        }
        if (existeDni(dni)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese DNI");
        }
        if (existeCorreo(correo)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo");
        }
        usuarios.add(new Usuario(dni, nombre, direccionCochera, correo, contrasenia));
        bitacora.addLast("REGISTRO: " + correo.trim());
    }

    public boolean esDniValido(String dni) {
        return dni != null && PATRON_DNI.matcher(dni.trim()).matches();
    }

    public boolean esCorreoValido(String correo) {
        return correo != null && PATRON_CORREO.matcher(correo.trim()).matches();
    }

    public boolean existeDni(String dni) {
        return obtenerUsuarioPorDni(dni) != null;
    }

    public boolean existeCorreo(String correo) {
        if (correo == null) {
            return false;
        }
        for (Usuario u : usuarios) {
            if (u.getCorreo().equalsIgnoreCase(correo.trim())) {
                return true;
            }
        }
        return false;
    }

    public Usuario autenticar(String correo, String contrasenia) {
        if (correo == null || contrasenia == null) {
            return null;
        }
        for (Usuario u : usuarios) {
            if (u.getCorreo().equalsIgnoreCase(correo.trim())
                    && u.getContrasenia().equals(contrasenia)) {
                registrarAcceso(u.getCorreo());
                return u;
            }
        }
        bitacora.addFirst("ACCESO FALLIDO: " + correo.trim());
        return null;
    }

    private void registrarAcceso(String correo) {
        if (ultimosAccesos.isFull()) {
            ultimosAccesos.dequeue();
        }
        ultimosAccesos.enqueue(correo);
        bitacora.addLast("ACCESO: " + correo);
    }

    public List<String> getUltimosAccesos() {
        return ultimosAccesos.getElementos();
    }

    public List<String> getBitacora() {
        return new ArrayList<>(bitacora);
    }

    public void registrarEventoEn(int indice, String evento) {
        if (indice < 0 || indice > bitacora.size()) {
            throw new IllegalArgumentException("La posición debe estar entre 0 y " + bitacora.size());
        }
        bitacora.add(indice, evento);
    }

    public void registrarEventos(Collection<String> eventos) {
        bitacora.addAll(eventos);
    }

    public int contarEventos(String inicio) {
        int total = 0;
        for (String evento : bitacora) {
            if (evento.startsWith(inicio)) {
                total++;
            }
        }
        return total;
    }

    public Usuario obtenerUsuarioPorDni(String dni) {
        if (dni == null) {
            return null;
        }
        for (Usuario u : usuarios) {
            if (u.getDni().equalsIgnoreCase(dni.trim())) {
                return u;
            }
        }
        return null;
    }

    public List<Usuario> ordenarUsuarioPorNombre() {
        return usuarios.stream().sorted(Usuario.POR_NOMBRE).toList();
    }

    public List<Usuario> ordenarUsuarioPorDni() {
        return usuarios.stream().sorted(Usuario.POR_DNI).toList();
    }
}
