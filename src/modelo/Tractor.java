/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.Year;

/**
 *
 * @author cesar
 */
public class Tractor {
    private String placa;
    private int ejes;
    private String marca;
    private String modelo;
    private String carroceria;
    private int anio;
    private String MTC;
    
    private final int ANIO_MINIMO = 2000;
    private final int ANIO_ACTUAL = Year.now().getValue();

    public Tractor(String placa, int ejes, String marca, String modelo, String carroceria, int anio, String MTC) {
        setPlaca(placa);
        setEjes(ejes);
        setMarca(marca);
        setModelo(modelo);
        setCarroceria(carroceria);
        setAnio(anio);
        setMTC(MTC);
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        if (placa != null){
            String placaLimpia = placa.trim();
            if (!placaLimpia.isEmpty()){
                this.placa = placaLimpia;
            }
        }
    }

    public int getEjes() {
        return ejes;
    }

    public void setEjes(int ejes) {
        if (ejes>=0){
            this.ejes = ejes;
        } else {
            this.ejes = 0;
        }
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        if (marca != null){
            String marcaLimpia = marca.trim();
            if (!marcaLimpia.isEmpty()){
                this.marca = marcaLimpia;
            }
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
            }
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
            }
        }
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        if (anio>=ANIO_MINIMO && anio<=ANIO_ACTUAL){
            this.anio = anio;
        } else {
            this.anio = ANIO_MINIMO;
        }
    }

    public String getMTC() {
        return MTC;
    }

    public void setMTC(String MTC) {
        if (MTC != null){
            String MTCLimpio = MTC.trim();
            if (!MTCLimpio.isEmpty()){
                this.MTC = MTCLimpio;
            }
        }
    }
}
