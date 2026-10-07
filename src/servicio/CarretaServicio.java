/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import java.util.List;
import modelo.Carreta;

/**
 *
 * @author NILENY
 */
public class CarretaServicio {
    
    private List<Carreta> carretas;

    // Lista donde se guardaran las carretas
    public CarretaServicio(List<Carreta> carretas) {
        this.carretas = carretas;
    }
    
    // Devuelve todas las carretas registradas
    public List<Carreta> getCarretas() {
        return carretas;
    }
    
    // Registra una carreta nueva; falla si los datos son inválidos o la placa ya existe
    public void guardarCarreta(String placa, int ejes, String marca, String modelo, String carroceria, int anio, String MTC) {
        Carreta carreta = new Carreta(placa, ejes, marca, modelo, carroceria, anio, MTC);
        if (existeCarreta(carreta.getPlaca())) {
            throw new IllegalArgumentException("Ya existe una carreta con esa placa");
        }
        carretas.add(carreta);
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
        Carreta carreta = obtenerCarretaPorPlaca(placa);
        if (carreta == null) {
            throw new IllegalArgumentException("La carreta no existe");
        }
        carretas.remove(carreta);
    }

    // Búsqueda por placa: devuelve la carreta o null si no la encuentra
    public Carreta obtenerCarretaPorPlaca(String placa) {
        if (placa == null) {
            throw new IllegalArgumentException("La placa no puede ser nula");
        }
        String placaBuscada = placa.trim().toUpperCase();
        for (Carreta c : carretas) {
            if (c.getPlaca().equals(placaBuscada)) {
                return c;
            }
        }
        return null;
    }

    // Responde sí/no: ¿existe una carreta con esa placa?
    public boolean existeCarreta(String placa) {
        return obtenerCarretaPorPlaca(placa) != null;
    }

    // Devuelve la lista ordenada por placa
    public List<Carreta> ordenarCarretaPorPlaca() {
        return carretas.stream().sorted(Carreta.POR_PLACA).toList();
    }

    // Devuelve la lista ordenada por marca
    public List<Carreta> ordenarCarretaPorMarca() {
        return carretas.stream().sorted(Carreta.POR_MARCA).toList();
    }    
    
}
