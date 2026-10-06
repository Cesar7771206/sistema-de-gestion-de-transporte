/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import modelo.Tractor;

/**
 *
 * @author cesar
 */
public class TractorServicio {
    private List<Tractor> tractors;

    public TractorServicio() {
        this.tractors = new ArrayList<>();
    }  

    public List<Tractor> getTractors() {
        return Collections.unmodifiableList(tractors);
    }
    
    public Tractor getTractorByPlaca(String placa){
        if (placa == null || placa.isBlank()){
            throw new IllegalArgumentException("La placa ingresada no puede estar vacía");
        }
        for (Tractor t : tractors){
            if (t.getPlaca().trim().equalsIgnoreCase(placa)){
                return t;
            }
        }
        return null;
    }
    
    public void registrarTractor(String placa, int ejes, String marca, String modelo, String carroceria, int anio, String MTC){
        if (getTractorByPlaca(placa) != null){
            throw new IllegalStateException("Ya existe un tractor con la placa "+placa);
        }
        Tractor nuevo = new Tractor(placa, ejes, marca, modelo, carroceria, anio, MTC);
        tractors.add(nuevo);
    }
    
    public void actualizarTractor(String placa, int ejes, String marca, String modelo, String carroceria, int anio, String MTC){
       if (getTractorByPlaca(placa) == null){
           throw new IllegalStateException("No existe un tractor con la placa "+placa);
       }
       Tractor t = getTractorByPlaca(placa);
       t.ActualizarTodosAtributos(ejes, marca, modelo, carroceria, anio, MTC);
    }
    
    public void eliminarTractor(String placa){
        if (getTractorByPlaca(placa) == null){
           throw new IllegalStateException("No existe un tractor con la placa "+placa);
       }
       tractors.removeIf(t -> t.getPlaca().equalsIgnoreCase(placa.trim()));
    }

    public List<Tractor> ordenarTractorsPorPlaca(){
        List<Tractor> tractorsPorPlaca = tractors.stream()
                .sorted(Tractor.POR_PLACA).toList();
        return tractorsPorPlaca;
    }
    
    public List<Tractor> ordenarTractorsPorEjes(){
        List<Tractor> tractorsPorEjes = tractors.stream()
                .sorted(Tractor.POR_EJES).toList();
        return tractorsPorEjes;
    }
}
