/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Entidad Planta.
 * Incluye matriz bidimensional de programación semanal de carga (7 días x 3 turnos),
 * y comparadores para ordenamiento y cola con prioridad.
 *
 * @author cesar, yimmy
 */
public class Planta {
    
    private String ruc;
    private String nombre;
    private String direccion;
    
    public static final Comparator<Planta> POR_RUC = Comparator.comparing(Planta::getRuc);
    public static final Comparator<Planta> POR_NOMBRE = Comparator.comparing(Planta::getNombre);

    public static final int DIAS = 7;
    public static final int TURNOS = 3;
    private final int[][] programacion = new int[DIAS][TURNOS];

    private static final String[] NOMBRES_DIAS =
        {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

    public static final Comparator<Planta> POR_CARGA_TOTAL =
            Comparator.comparingInt(Planta::getCargaTotal);

    public static final Comparator<Planta> POR_CARGA =
            POR_CARGA_TOTAL.reversed().thenComparing(POR_NOMBRE).thenComparing(POR_RUC);

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
        String r = ruc.trim();
        if (r.matches("\\d{11}")){
            this.ruc = r;
        } else {
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

    public void setCarga(int dia, int turno, int cantidad) {
        if (dia < 0 || dia >= DIAS) {
            throw new IllegalArgumentException("El día debe estar entre 0 y " + (DIAS - 1));
        }
        if (turno < 0 || turno >= TURNOS) {
            throw new IllegalArgumentException("El turno debe estar entre 0 y " + (TURNOS - 1));
        }
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        programacion[dia][turno] = cantidad;
    }

    public int getCarga(int dia, int turno) {
        return programacion[dia][turno];
    }

    public int[][] getProgramacion() {
        int[][] copia = new int[DIAS][TURNOS];
        for (int d = 0; d < DIAS; d++) {
            System.arraycopy(programacion[d], 0, copia[d], 0, TURNOS);
        }
        return copia;
    }

    public void setProgramacion(int[][] nueva) {
        if (nueva == null || nueva.length != DIAS) {
            throw new IllegalArgumentException("La programación debe tener " + DIAS + " días");
        }
        for (int[] fila : nueva) {
            if (fila == null || fila.length != TURNOS) {
                throw new IllegalArgumentException("Cada día debe tener " + TURNOS + " turnos");
            }
            for (int cantidad : fila) {
                if (cantidad < 0) {
                    throw new IllegalArgumentException("Las cantidades no pueden ser negativas");
                }
            }
        }
        for (int d = 0; d < DIAS; d++) {
            System.arraycopy(nueva[d], 0, programacion[d], 0, TURNOS);
        }
    }

    public int getCargaTotal() {
        int total = 0;
        for (int[] fila : programacion) {
            for (int cantidad : fila) {
                total += cantidad;
            }
        }
        return total;
    }

    public int[] getTotalPorDia() {
        int[] totales = new int[DIAS];
        for (int d = 0; d < DIAS; d++) {
            int suma = 0;
            for (int t = 0; t < TURNOS; t++) {
                suma += programacion[d][t];
            }
            totales[d] = suma;
        }
        return totales;
    }

    public int[] getTotalPorTurno() {
        int[] totales = new int[TURNOS];
        for (int t = 0; t < TURNOS; t++) {
            int suma = 0;
            for (int d = 0; d < DIAS; d++) {
                suma += programacion[d][t];
            }
            totales[t] = suma;
        }
        return totales;
    }

    public String generarReporte() {
        StringBuilder reporte = new StringBuilder();
        reporte.append(String.format("Reporte de programación - %s (RUC: %s)%n", nombre, ruc));
        reporte.append(String.format("%-10s %7s %7s %7s %7s%n",
                "Día", "Turno 1", "Turno 2", "Turno 3", "Total"));
        reporte.append("-".repeat(45)).append(System.lineSeparator());

        int[] totalDia = getTotalPorDia();
        for (int d = 0; d < DIAS; d++) {
            reporte.append(String.format("%-10s", NOMBRES_DIAS[d]));
            for (int t = 0; t < TURNOS; t++) {
                reporte.append(String.format(" %7d", programacion[d][t]));
            }
            reporte.append(String.format(" %7d%n", totalDia[d]));
        }
        reporte.append(String.format("%-10s", "Total"));
        for (int total : getTotalPorTurno()) {
            reporte.append(String.format(" %7d", total));
        }
        reporte.append(String.format(" %7d%n", getCargaTotal()));
        return reporte.toString();
    }

    @Override
    public String toString() {
        return "Planta{" + "ruc=" + ruc + ", nombre=" + nombre + ", direccion=" + direccion + '}';
    }
}
