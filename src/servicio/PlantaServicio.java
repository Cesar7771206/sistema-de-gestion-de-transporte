/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import java.util.List;
import modelo.Planta;

/**
 *
 * @author NILENY
 */
public class PlantaServicio {
    private List<Planta> plantas;
    
    //Lista donde se guardaran todas las plantas
    public PlantaServicio(List plantas) {
        this.plantas = plantas;
    }
    
    // Devuelve todas las plantas registradas
    public List<Planta> getPlantas() {
        return plantas;
    }
    
    // Registra una planta nueva 
    public void guardarPlanta(String ruc, String nombre, String direccion) {
        Planta planta = new Planta(ruc, nombre, direccion);
        if (existePlanta(planta.getRuc())) {
            throw new IllegalArgumentException("Ya existe una planta con ese RUC");
        }
        plantas.add(planta);
    }
    
    // modifica nombre y direccion de una planta existente 
    public void editarPlanta(String ruc, String nombre, String direccion) {
        Planta planta = this.obtenerPlantaPorRuc(ruc);
        
        if (planta == null) {
            throw new IllegalArgumentException("La planta no existe");
        }
        
        new Planta(planta.getRuc(), nombre, direccion);
        planta.setNombre(nombre);
        planta.setDireccion(direccion);
    }

    // Elimina la planta con el ruc buscado 
    public void eliminarPlanta(String ruc) {
        Planta planta = this.obtenerPlantaPorRuc(ruc);
        if (planta == null) {
            throw new IllegalArgumentException("La planta no existe");
        }
        plantas.remove(planta);
    }

    //metodo para buscar una planta
    public Planta obtenerPlantaPorRuc(String ruc) {
        if (ruc == null) {
            throw new IllegalArgumentException("El RUC no puede ser nulo");
        }
        String rucBuscado = ruc.trim();
        for (Planta p : plantas) {
            if (p.getRuc().equals(rucBuscado)) {
                return p;
            }
        }
        return null;
    }

    //Responde Si/No a ¿Existe una planta con ese RUC?
    
    public boolean existePlanta(String ruc) {
    return obtenerPlantaPorRuc(ruc) != null;
    }
    
    // Método ordenar las plantas por RUC
    public List<Planta> ordenarPlantaPorRuc() {
        return plantas.stream().sorted(Planta.POR_RUC).toList();
    }

    
    //Método ordenar las plantas por Nombre
    public List<Planta> ordenarPlantaPorNombre() {
        return plantas.stream().sorted(Planta.POR_NOMBRE).toList();
    }
}
    
    

