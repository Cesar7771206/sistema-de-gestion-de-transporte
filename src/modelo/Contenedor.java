/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.Comparator;

/**
 *
 * @author cesar
 */
public class Contenedor {
    private String codigo;
    private double payload;
    private double tara;
    
    public static final Comparator<Contenedor> POR_CODIGO = Comparator.comparing(Contenedor :: getCodigo);
    public static final Comparator<Contenedor> POR_PAYLOAD = Comparator.comparing(Contenedor :: getPayload);
    public static final Comparator<Contenedor> POR_TARA = Comparator.comparing(Contenedor :: getTara);
    
    public Contenedor(String codigo, double payload, double tara){
        setCodigo(codigo);
        setPayload(payload);
        setTara(tara);
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        if (codigo!=null){
            String codigoLimpio = codigo.trim();
            if (!codigoLimpio.isEmpty()){
                this.codigo = codigoLimpio;
            }
        }
    }

    public double getPayload() {
        return payload;
    }

    public void setPayload(double payload) {
        if (payload>=0){
            this.payload = payload;
        } else {
            this.payload = 0;
        }
    }

    public double getTara() {
        return tara;
    }

    public void setTara(double tara) {
        if (tara>=0){
            this.tara = tara;
        } else {
            this.tara = 0;
        }
    }
}
