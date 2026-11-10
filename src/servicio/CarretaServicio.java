/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;


import modelo.Carreta;

/**
 *
 * @author NILENY
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

    
    //Consultar
    public int cantidad(){
        return cantidad;
    }
    
    public Carreta[] getCarretas(){
        return copiar(carretas, cantidad);
    }
    
    // pila en papelera
    public boolean papeleraVacia() {
        return tope == -1;
    }

    public boolean papeleraLlena() {
        return tope == papelera.length - 1;
    }

    public int cantidadPapelera() {
        return tope + 1;
    }
    
    //peek
     public Carreta cimaPapelera() {
        return papeleraVacia() ? null : papelera[tope];
    }
     
    //Push
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
    
    //Pop
    private Carreta desapilar() {
        if (papeleraVacia()) {
            throw new IllegalStateException("La papelera está vacía");
        }
        Carreta c = papelera[tope];
        papelera[tope] = null;
        tope--;
        return c;
    }
    
    
    //Buscar 
   
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
            throw new IllegalArgumentException("Ya existe una carreta con esa placa" + nuevaCarreta.getPlaca());
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

    // Elimina la carreta de la placa ingresada; falla si no existe
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
    
    //ordenar por 
    
     public Carreta[] ordenarCarretaPorPlaca() {
        return copiar(carretas, cantidad);
    }

    // Se ordena una COPIA para que el original siga ordenado por placa
    public Carreta[] ordenarCarretaPorMarca() {
        return burbuja(copiar(carretas, cantidad), "marca");
    }

    public Carreta[] ordenarCarretaPorAnio() {
        return burbuja(copiar(carretas, cantidad), "anio");
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
    
    
    //copiar
    
    private static Carreta[] copiar(Carreta[] origen, int n) {
        Carreta[] destino = new Carreta[n];
        for (int i = 0; i < n; i++) {
            destino[i] = origen[i];
        }
        return destino;
    }
    
    //clonacion
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
    
    //comparación
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

   