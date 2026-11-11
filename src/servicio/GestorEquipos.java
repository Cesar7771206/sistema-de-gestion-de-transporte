package servicio;

import java.util.ArrayList;
import modelo.Carreta;
import modelo.Contenedor;
import modelo.Tractor;
import modelo.UnidadTransporte;

public class GestorEquipos {

    public static final int LIMITE_UNIDADES = 7;

    private static ArrayList<Tractor> tractores = new ArrayList<>();
    private static ArrayList<Carreta> carretas = new ArrayList<>();
    private static ArrayList<Contenedor> contenedores = new ArrayList<>();
    private static ArrayList<UnidadTransporte> unidades = new ArrayList<>();

    public static void agregarTractor(Tractor tractor) {
        if (tractor != null && !tractores.contains(tractor)) {
            tractores.add(tractor);
        }
    }

    public static ArrayList<Tractor> getTractores() {
        return tractores;
    }

    public static void agregarCarreta(Carreta carreta) {
        if (carreta != null && !carretas.contains(carreta)) {
            carretas.add(carreta);
        }
    }

    public static ArrayList<Carreta> getCarretas() {
        return carretas;
    }

    public static void agregarContenedor(Contenedor contenedor) {
        if (contenedor != null && !contenedores.contains(contenedor)) {
            contenedores.add(contenedor);
        }
    }

    public static ArrayList<Contenedor> getContenedores() {
        return contenedores;
    }

    public static void agregarUnidad(UnidadTransporte unidad) {
        if (unidad == null) {
            throw new IllegalArgumentException("La unidad no puede ser nula");
        }
        if (unidades.size() >= LIMITE_UNIDADES) {
            throw new IllegalStateException("Límite máximo alcanzado. No se pueden registrar más de 7 unidades operativas.");
        }
        if (buscarUnidadPorTractor(unidad.getPlaca()) != null) {
            throw new IllegalStateException("El tractor " + unidad.getPlaca() + " ya está asignado a otra unidad operativa.");
        }
        if (unidad.getCarreta() != null && buscarUnidadPorCarreta(unidad.getCarreta().getPlaca()) != null) {
            throw new IllegalStateException("La carreta " + unidad.getCarreta().getPlaca() + " ya está asignada a otra unidad operativa.");
        }
        unidades.add(unidad);
    }

    public static boolean eliminarUnidad(UnidadTransporte unidad) {
        return unidades.remove(unidad);
    }

    public static ArrayList<UnidadTransporte> getUnidades() {
        return unidades;
    }

    public static UnidadTransporte buscarUnidadPorTractor(String placaTractor) {
        if (placaTractor == null || placaTractor.isBlank()) return null;
        for (UnidadTransporte u : unidades) {
            if (u.getTractor() != null && u.getTractor().getPlaca().equalsIgnoreCase(placaTractor.trim())) {
                return u;
            }
        }
        return null;
    }

    public static UnidadTransporte buscarUnidadPorCarreta(String placaCarreta) {
        if (placaCarreta == null || placaCarreta.isBlank()) return null;
        for (UnidadTransporte u : unidades) {
            if (u.getCarreta() != null && u.getCarreta().getPlaca().equalsIgnoreCase(placaCarreta.trim())) {
                return u;
            }
        }
        return null;
    }

    public static Tractor buscarTractorPorPlaca(String placa) {
        if (placa == null || placa.isBlank()) return null;
        for (Tractor t : tractores) {
            if (t.getPlaca().equalsIgnoreCase(placa.trim())) {
                return t;
            }
        }
        return null;
    }

    public static Carreta buscarCarretaPorPlaca(String placa) {
        if (placa == null || placa.isBlank()) return null;
        for (Carreta c : carretas) {
            if (c.getPlaca().equalsIgnoreCase(placa.trim())) {
                return c;
            }
        }
        return null;
    }
}
