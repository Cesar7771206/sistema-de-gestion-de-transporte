/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author yimmy
 */
public class Direccion {
    private int id;
    private String distrito;
    private String provincia;
    private String departamento;
    private String direccion;

    public Direccion() {
    }

    public Direccion(int id, String distrito, String provincia,
                     String departamento, String direccion) {
        this.id = id;
        this.distrito = distrito;
        this.provincia = provincia;
        this.departamento = departamento;
        this.direccion = direccion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDistrito() { return distrito; }
    public void setDistrito(String distrito) { this.distrito = distrito; }

    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    @Override
    public String toString() {
        return direccion + ", " + distrito + ", " + provincia + ", " + departamento;
    }
}