package modelo;

import java.util.Objects;

/**
 * Representa una unidad de transporte operativa compuesta por un Tractor
 * y opcionalmente una Carreta, junto con su estado y ubicación dentro de la cochera.
 */
public class UnidadTransporte {

    public enum EstadoUnidad {
        EN_COCHERA,
        EN_VIAJE,
        DISPONIBLE
    }

    private Tractor tractor;
    private Carreta carreta;
    private int[][] coordenadasCochera;
    private int prioridadCochera;
    private EstadoUnidad estado;

    public UnidadTransporte(Tractor tractor) {
        this(tractor, null, 3);
    }

    public UnidadTransporte(Tractor tractor, Carreta carreta) {
        this(tractor, carreta, 3);
    }

    public UnidadTransporte(Tractor tractor, Carreta carreta, int prioridadCochera) {
        setTractor(tractor);
        this.carreta = carreta;
        setPrioridadCochera(prioridadCochera);
        this.coordenadasCochera = null;
        this.estado = EstadoUnidad.DISPONIBLE;
    }

    public Tractor getTractor() {
        return tractor;
    }

    public void setTractor(Tractor tractor) {
        if (tractor == null) {
            throw new IllegalArgumentException("Tiene que asignar de forma obligatoria un tractor a la unidad de transporte");
        }
        this.tractor = tractor;
    }

    public Carreta getCarreta() {
        return carreta;
    }

    public void setCarreta(Carreta carreta) {
        this.carreta = carreta;
    }

    public int[][] getCoordenadasCochera() {
        return coordenadasCochera;
    }

    public void setCoordenadasCochera(int[][] coordenadasCochera) {
        this.coordenadasCochera = coordenadasCochera;
        if (coordenadasCochera != null && coordenadasCochera.length > 0) {
            this.estado = EstadoUnidad.EN_COCHERA;
        }
    }

    public int getPrioridadCochera() {
        return prioridadCochera;
    }

    public void setPrioridadCochera(int prioridadCochera) {
        if (prioridadCochera < 1 || prioridadCochera > 3) {
            throw new IllegalArgumentException("Valor inválido, la prioridad debe ser 1, 2 o 3");
        }
        this.prioridadCochera = prioridadCochera;
    }

    public EstadoUnidad getEstado() {
        return estado;
    }

    public void setEstado(EstadoUnidad estado) {
        this.estado = estado;
    }

    public String getPlaca() {
        return tractor != null ? tractor.getPlaca() : "";
    }

    public boolean estaEnCochera() {
        return coordenadasCochera != null && coordenadasCochera.length > 0;
    }

    public void liberarCochera() {
        this.coordenadasCochera = null;
        if (this.estado == EstadoUnidad.EN_COCHERA) {
            this.estado = EstadoUnidad.DISPONIBLE;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnidadTransporte that = (UnidadTransporte) o;
        return Objects.equals(getPlaca(), that.getPlaca());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlaca());
    }

    @Override
    public String toString() {
        String infoCarreta = (carreta != null) ? carreta.getPlaca() : "Sin Carreta";
        return "Unidad [" + getPlaca() + " / " + infoCarreta + "] - P" + prioridadCochera + " (" + estado + ")";
    }
}
