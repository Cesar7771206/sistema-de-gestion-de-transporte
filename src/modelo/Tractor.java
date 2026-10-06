/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.Year;
import java.util.Comparator;

/**
 *
 * @author cesar
 */
public class Tractor {
    private final String placa;
    private int ejes;
    private String marca;
    private String modelo;
    private String carroceria;
    private int anio;
    private String MTC;
    
    private static final int ANIO_MINIMO = 2000;
    private static final int ANIO_ACTUAL = Year.now().getValue();
    
    // criterios de ordenación
    public static final Comparator<Tractor> POR_PLACA = Comparator.comparing(Tractor :: getPlaca);
    public static final Comparator<Tractor> POR_EJES = Comparator.comparing(Tractor :: getEjes).reversed();

    public Tractor(String placa, int ejes, String marca, String modelo, String carroceria, int anio, String MTC) {
        this.placa = validarTexto(marca, "La marca").toUpperCase();
        setEjes(ejes);
        setMarca(marca);
        setModelo(modelo);
        setCarroceria(carroceria);
        setAnio(anio);
        setMTC(MTC);
    }
    
    private static String validarTexto(String valor, String campo){
        if (valor == null || valor.isBlank()){
            throw new IllegalArgumentException(campo + "No puede estar vacío");
        }
        return valor.trim();
    }
    
    public void ActualizarTodosAtributos(int ejes, String marca, String modelo, String carroceria, int anio, String MTC){
        setEjes(ejes);
        setMarca(marca);
        setModelo(modelo);
        setCarroceria(carroceria);
        setAnio(anio);
        setMTC(MTC);
    }
    
    public void setEjes(int ejes) {
        if (ejes <3 || ejes>6){
            throw new IllegalArgumentException("Cantidad de ejes invalida: "+ejes);
        }
        this.ejes = ejes;
    }
    
    public void setAnio(int anio) {
        if (anio < ANIO_MINIMO || anio > ANIO_ACTUAL){
            throw new IllegalArgumentException("Año fuera del rango: "+anio);
        }
        this.anio = anio;
    }

    public void setMarca(String marca) {
        this.marca = validarTexto(marca, "La marca");
    }

    public void setModelo(String modelo) {
        this.modelo = validarTexto(modelo, "El modelo");
    }

    public void setCarroceria(String carroceria) {
        this.carroceria = validarTexto(carroceria, "La carrocería");
    }

    public void setMTC(String MTC) {
        this.MTC = validarTexto(MTC, "El MTC");
    }
    

    public String getPlaca() {
        return placa;
    }

    public int getEjes() {
        return ejes;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getCarroceria() {
        return carroceria;
    }

    public int getAnio() {
        return anio;
    }

    public String getMTC() {
        return MTC;
    }
}
