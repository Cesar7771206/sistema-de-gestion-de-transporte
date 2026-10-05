/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;
import java.util.ArrayList;
import java.util.List; 
import modelo.Contenedor; 

/**
 *
 * @author cesar
 */
public class ContenedorControlador {
    private List<Contenedor> contenedores;

    public ContenedorControlador() {
        contenedores = new ArrayList<>(); 
    }

    public List<Contenedor> getContenedores() {
        return contenedores;
    }
    
    public void guardarContenedor(String codigo, double payload, double tara){
        Contenedor c = new Contenedor(codigo, payload, tara);
        this.contenedores.add(c);
    }
    
    public void editarContenedor(String codigo, double payload, double tara){
        Contenedor contenedor = this.obtenerContenedorPörCodigo(codigo);
        if (contenedor == null){
            throw new IllegalArgumentException("El contenedor no existe");
        }
        contenedor.setPayload(payload);
        contenedor.setTara(tara);
    }
    
    public boolean eliminarContenedor(String codigo){
        boolean eliminado = contenedores.removeIf(c -> c.getCodigo().equals(codigo));
        if (!eliminado){
            return false;
        } else {
            return true;
        }
    }
    
    public Contenedor obtenerContenedorPörCodigo(String codigo){
        for (Contenedor c: contenedores){
            if (c.getCodigo().equals(codigo)){
                return c;
            }
        }
        return null;
    }
    
    public List<Contenedor> ordenarContenedorPorCodigo(){
        List<Contenedor> contenedoresPorCodigo = contenedores.stream()
                .sorted(Contenedor.POR_CODIGO).toList();
        return contenedoresPorCodigo;
    }
    
    public List<Contenedor> ordenarContenedorPorPayload(){
        List<Contenedor> contenedoresPorPayload = contenedores.stream()
                .sorted(Contenedor.POR_PAYLOAD).toList();
        return contenedoresPorPayload;
    }
    
    public List<Contenedor> ordenarContenedorPorTara(){
        List<Contenedor> contenedoresPorTara = contenedores.stream()
                .sorted(Contenedor.POR_TARA).toList();
        return contenedoresPorTara;
    } 
}
