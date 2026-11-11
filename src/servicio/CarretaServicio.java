/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import modelo.Carreta;

/**
 * Servicio de gestión de Carretas.
 * Evidencia el uso de:
 * - Arreglo estático ordenado (Carreta[]) con búsqueda binaria e inserción ordenada.
 * - Pila estática (papelera con arreglo y puntero tope) para deshacer eliminaciones (LIFO).
 * - Ordenamiento por algoritmo de la burbuja (sobre copias de arreglos estáticos).
 * - Respaldo, clonación de arreglos y comparación de estados de flota.
 *
 * @author NILENY, cesar
 */
public class CarretaServicio {
    
    public static final int CAPACIDAD_FLOTA = 50;
    public static final int CAPACIDAD_PAPELERA = 5; 
    
    private final Carreta[] carretas = new Carreta[CAPACIDAD_FLOTA];
    private int cantidad = 0;
    
    private final Carreta[] papelera = new Carreta[CAPACIDAD_PAPELERA];
    private int tope = -1;
    
    private Carreta[] respaldo;      
    private int cantidadRespaldo;

    public CarretaServicio() {
    }

    public CarretaServicio(List<Carreta> listaInicial) {
        if (listaInicial != null) {
            for (Carreta c : listaInicial) {
                if (c != null && !existeCarreta(c.getPlaca())) {
                    guardarCarreta(c.getPlaca(), c.getEjes(), c.getMarca(),
                            c.getModelo(), c.getCarroceria(), c.getAnio(), c.getMTC());
                }
            }
        }
    }
    
    // Consultar
    public int cantidad() {
        return cantidad;
    }

    public int getCantidad() {
        return cantidad;
    }
    
    public Carreta[] getCarretasArreglo() {
        return copiar(carretas, cantidad);
    }

    public List<Carreta> getCarretas() {
        List<Carreta> lista = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            lista.add(carretas[i]);
        }
        return lista;
    }
    
    // ==========================================
    // OPERACIONES DE LA PILA ESTÁTICA (PAPELERA)
    // ==========================================

    public boolean papeleraVacia() {
        return tope == -1;
    }

    public boolean papeleraLlena() {
        return tope == papelera.length - 1;
    }

    public int cantidadPapelera() {
        return tope + 1;
    }
    
    public Carreta cimaPapelera() {
        return papeleraVacia() ? null : papelera[tope];
    }
     
    private void apilar(Carreta c) {
        if (papeleraLlena()) {
            for (int i = 0; i < tope; i++) {
                papelera[i] = papelera[i + 1];
            }
            papelera[tope] = null;
            tope--;
        }
        tope++;
        papelera[tope] = c;
    } 
    
    private Carreta desapilar() {
        if (papeleraVacia()) {
            throw new IllegalStateException("La papelera está vacía");
        }
        Carreta c = papelera[tope];
        papelera[tope] = null;
        tope--;
        return c;
    }

    public Carreta deshacerEliminacion() {
        if (papeleraVacia()) {
            throw new IllegalStateException("No hay eliminaciones para deshacer");
        }
        Carreta ultima = cimaPapelera(); 
        if (existeCarreta(ultima.getPlaca())) {
            throw new IllegalStateException("Ya se registró otra carreta con la placa " + ultima.getPlaca());
        }
        insertarOrdenado(ultima);
        desapilar(); 
        return ultima;
    }

    // ==========================================
    // BÚSQUEDA BINARIA EN ARREGLO ORDENADO
    // ==========================================

    private int busquedaBinaria(String placa) {
        int izquierda = 0;
        int derecha = cantidad - 1;
        while (izquierda <= derecha) {
            int medio = (izquierda + derecha) / 2;
            int comparacion = carretas[medio].getPlaca().compareTo(placa);
            if (comparacion == 0) {
                return medio;
            } else if (comparacion < 0) {
                izquierda = medio + 1;  
            } else {
                derecha = medio - 1;       
            }
        }
        return -1;
    } 
     
    public Carreta obtenerCarretaPorPlaca(String placa) {
        if (placa == null) {
            return null;
        }
        int pos = busquedaBinaria(placa.trim().toUpperCase());
        return pos == -1 ? null : carretas[pos];
    }
     
    public boolean existeCarreta(String placa) {
        return obtenerCarretaPorPlaca(placa) != null;
    } 
    
    // Registra una carreta nueva
    public void guardarCarreta(String placa, int ejes, String marca, String modelo, String carroceria, int anio, String MTC) {
        Carreta nuevaCarreta = new Carreta(placa, ejes, marca, modelo, carroceria, anio, MTC);
        if (existeCarreta(nuevaCarreta.getPlaca())) {
            throw new IllegalArgumentException("Ya existe una carreta con esa placa " + nuevaCarreta.getPlaca());
        }
        insertarOrdenado(nuevaCarreta);
    }

    private void insertarOrdenado(Carreta nueva) {
        if (cantidad == carretas.length) {
            throw new IllegalArgumentException("La flota está completa (" + CAPACIDAD_FLOTA + " carretas)");
        }
        int pos = 0;
        while (pos < cantidad && carretas[pos].getPlaca().compareTo(nueva.getPlaca()) < 0) {
            pos++;
        }
        for (int i = cantidad; i > pos; i--) {
            carretas[i] = carretas[i - 1];
        }
        carretas[pos] = nueva;
        cantidad++;
    }

    // Modifica los atributos de una carreta existente, menos la placa
    public void editarCarreta(String placa, int ejes, String marca, String modelo, String carroceria, int anio, String MTC) {
        Carreta carreta = obtenerCarretaPorPlaca(placa);
        if (carreta == null) {
            throw new IllegalArgumentException("La carreta no existe");
        }
        new Carreta(carreta.getPlaca(), ejes, marca, modelo, carroceria, anio, MTC);

        carreta.setEjes(ejes);
        carreta.setMarca(marca);
        carreta.setModelo(modelo);
        carreta.setCarroceria(carroceria);
        carreta.setAnio(anio);
        carreta.setMTC(MTC);
    }

    // Elimina la carreta de la placa ingresada y la apila en la papelera
    public void eliminarCarreta(String placa) {
        int pos = busquedaBinaria(placa == null ? "" : placa.trim().toUpperCase());
        if (pos == -1) {
            throw new IllegalArgumentException("La carreta no existe");
        }
        Carreta eliminada = carretas[pos];
       
        for (int i = pos; i < cantidad - 1; i++) {
            carretas[i] = carretas[i + 1];
        }
        carretas[cantidad - 1] = null;
        cantidad--;
        apilar(eliminada); 
    }
    
    // ==========================================
    // ORDENAMIENTO POR BURBUJA (ARREGLO ESTÁTICO)
    // ==========================================

    public List<Carreta> ordenarCarretaPorPlaca() {
        return Arrays.asList(copiar(carretas, cantidad));
    }

    public List<Carreta> ordenarCarretaPorMarca() {
        Carreta[] ordenado = burbuja(copiar(carretas, cantidad), "marca");
        return Arrays.asList(ordenado);
    }

    public List<Carreta> ordenarCarretaPorAnio() {
        Carreta[] ordenado = burbuja(copiar(carretas, cantidad), "anio");
        return Arrays.asList(ordenado);
    }
    
    private Carreta[] burbuja(Carreta[] a, String criterio) {
        for (int pasada = 0; pasada < a.length - 1; pasada++) {
            boolean huboCambio = false;
            for (int j = 0; j < a.length - 1 - pasada; j++) {
                if (vaDespues(a[j], a[j + 1], criterio)) {
                    Carreta temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                    huboCambio = true;
                }
            }
            if (!huboCambio) {
                break; 
            }
        }
        return a;
    }
    
    private boolean vaDespues(Carreta x, Carreta y, String criterio) {
        if (criterio.equals("marca")) {
            return x.getMarca().compareTo(y.getMarca()) > 0;
        }
        return x.getAnio() < y.getAnio();
    }
    
    // ==========================================
    // COPIA, CLONACIÓN Y COMPARACIÓN DE ARREGLOS
    // ==========================================

    private static Carreta[] copiar(Carreta[] origen, int n) {
        Carreta[] destino = new Carreta[n];
        for (int i = 0; i < n; i++) {
            destino[i] = origen[i];
        }
        return destino;
    }
    
    private static Carreta[] clonar(Carreta[] origen) {
        return origen.clone();
    }

    public void crearRespaldo() {
        respaldo = clonar(carretas);
        cantidadRespaldo = cantidad;
    }

    public boolean tieneRespaldo() {
        return respaldo != null;
    }

    public Carreta[] getRespaldo() {
        return respaldo == null ? new Carreta[0] : copiar(respaldo, cantidadRespaldo);
    }
    
    public boolean flotaIgualAlRespaldo() {
        if (respaldo == null) {
            throw new IllegalStateException("Primero cree un respaldo");
        }
        if (cantidad != cantidadRespaldo) {
            return false;
        }
        for (int i = 0; i < cantidad; i++) {
            if (carretas[i] != respaldo[i]) {
                return false;
            }
        }
        return true;
    }
}
