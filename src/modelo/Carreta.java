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
public class Carreta {
    
    private static final int ANIO_MINIMO = 2000;
    private static final int ANIO_ACTUAL = Year.now().getValue();

    private String placa;
    private int ejes;
    private String marca;
    private String modelo;
    private String carroceria;
    private int anio;
    private String MTC;
    
    // Comparadores para los botones "Ordenar por"
    public static final Comparator<Carreta> POR_PLACA = Comparator.comparing(Carreta::getPlaca);
    public static final Comparator<Carreta> POR_MARCA = Comparator.comparing(Carreta::getMarca);
    public static final Comparator<Carreta> POR_ANIO =  Comparator.comparingInt(Carreta::getAnio);
    
    //Constructor que usa setters para validacion
    public Carreta(String placa, int ejes, String marca, String modelo, String carroceria, int anio, String MTC) {
        setPlaca(placa);
        setEjes(ejes);
        setMarca(marca);
        setModelo(modelo);
        setCarroceria(carroceria);
        setAnio(anio);
        setMTC(MTC);
    }
    
    //Getters y Setters
    public String getPlaca() {
        return placa;
    } 
    
    // 
    public void setPlaca(String placa) {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa no puede ser nula ni vacía.");
        }    
        String placaFinal = placa.trim().toUpperCase();
        if (!placaFinal.matches("[A-Z0-9]{6}")) {
            throw new IllegalArgumentException("La placa debe tener 6 letras o números");
        }
        this.placa = placaFinal;
    
    }   

    public int getEjes() {
        return ejes;
    }

    //
    public void setEjes(int ejes) {
        if (ejes < 1 || ejes > 6) {
            throw new IllegalArgumentException("Los ejes deben estar entre 1 y 6");
        }
        this.ejes = ejes;
        
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        if (marca != null){
        String marcaLimpia = marca.trim().toUpperCase();
        if (!marcaLimpia.isEmpty()){
                this.marca = marcaLimpia;
            }else {
                    throw new IllegalArgumentException("La marca no puede estar vacía.");
            }
        } else {
                throw new IllegalArgumentException("La marca no puede ser nula.");
               }
    }
    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        if (modelo != null){
            String modeloLimpio = modelo.trim();
            if(!modeloLimpio.isEmpty()){
                this.modelo = modeloLimpio;
            }else {
                    throw new IllegalArgumentException("El modelo no puede estar vacío.");
            }
        } else {
                throw new IllegalArgumentException("El modelo no puede ser nulo.");
               }
    }
    public String getCarroceria() {
        return carroceria;
    }

    public void setCarroceria(String carroceria) {
        if (carroceria != null){
            String carroceriaLimpio = carroceria.trim();
            if (!carroceriaLimpio.isEmpty()){
                this.carroceria = carroceriaLimpio;
            }else {
                    throw new IllegalArgumentException("La carrocería no puede estar vacía.");
            }
        } else {
                throw new IllegalArgumentException("La carrocería no puede ser nula.");
               }
    }
    
    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        if (anio < ANIO_MINIMO || anio > ANIO_ACTUAL) {
            throw new IllegalArgumentException(
                    "El año debe estar entre " + ANIO_MINIMO + " y " + ANIO_ACTUAL);
        }
        this.anio = anio;
    }

    public String getMTC() {
        return MTC;
    }

    public void setMTC(String MTC) {
         if (MTC != null){
            String MTCLimpio = MTC.trim();
            if (!MTCLimpio.isEmpty()){
                this.MTC = MTCLimpio;
                }else {
                    throw new IllegalArgumentException("El MTC no puede estar vacío.");
            }
        } else {
                throw new IllegalArgumentException("El MTC no puede ser nulo.");
               }
    
    }

    @Override
    public String toString() {
        return "Carreta{" + "placa=" + placa + ", ejes=" + ejes + ", marca=" + marca + ", modelo=" + modelo + ", carroceria=" + carroceria + ", anio=" + anio + ", MTC=" + MTC + '}';
    }
    
    
    
}
