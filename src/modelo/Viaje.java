package modelo;

public class Viaje {
    
    private int id;
    private Contenedor contenedor;
    private UnidadTransporte unidad;
    private Planta planta;
    private Terminal terminal;
    private Cliente cliente;
    private Ruta ruta;

    public Viaje(int id, Contenedor contenedor, UnidadTransporte unidad,
                 Planta planta, Terminal terminal,
                 Cliente cliente, Ruta ruta) {

        this.id = id;
        this.contenedor = contenedor;
        this.unidad = unidad;
        this.planta = planta;
        this.terminal = terminal;
        this.cliente = cliente;
        this.ruta = ruta;
    }

    public Viaje(int id, Contenedor contenedor, Tractor tractor,
                 Carreta carreta, Planta planta, Terminal terminal,
                 Cliente cliente, Ruta ruta) {

        this(id, contenedor, new UnidadTransporte(tractor, carreta), planta, terminal, cliente, ruta);
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

    public UnidadTransporte getUnidad() {
        return unidad;
    }

    public void setUnidad(UnidadTransporte unidad) {
        this.unidad = unidad;
    }

    public Tractor getTractor() {
        return unidad != null ? unidad.getTractor() : null;
    }

    public void setTractor(Tractor tractor) {
        if (this.unidad == null) {
            this.unidad = new UnidadTransporte(tractor);
        } else {
            this.unidad.setTractor(tractor);
        }
    }

    public Carreta getCarreta() {
        return unidad != null ? unidad.getCarreta() : null;
    }

    public void setCarreta(Carreta carreta) {
        if (this.unidad != null) {
            this.unidad.setCarreta(carreta);
        }
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
