
package modelo;

public class Viaje {
    //Hacer modelo del viaje( atributos, buscar viaje, elminar, editar viaje
    
    private int id;
    private Contenedor contenedor;
    private Tractor tractor;
    private Carreta carreta;
    private Planta planta;
    private Terminal terminal;
    private Cliente cliente;
    private Ruta ruta;

    public Viaje(int id, Contenedor contenedor, Tractor tractor,
                 Carreta carreta, Planta planta, Terminal terminal,
                 Cliente cliente, Ruta ruta) {

        this.id = id;
        this.contenedor = contenedor;
        this.tractor = tractor;
        this.carreta = carreta;
        this.planta = planta;
        this.terminal = terminal;
        this.cliente = cliente;
        this.ruta = ruta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Contenedor getContenedor() {
        return contenedor;
    }

    public void setContenedor(Contenedor contenedor) {
        this.contenedor = contenedor;
    }

    public Tractor getTractor() {
        return tractor;
    }

    public void setTractor(Tractor tractor) {
        this.tractor = tractor;
    }

    public Carreta getCarreta() {
        return carreta;
    }

    public void setCarreta(Carreta carreta) {
        this.carreta = carreta;
    }

    public Planta getPlanta() {
        return planta;
    }

    public void setPlanta(Planta planta) {
        this.planta = planta;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }
}
