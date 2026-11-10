package servicio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import modelo.UnidadTransporte;
import modelo.Viaje;

/**
 * Servicio encargado de la gestión del patio / cochera:
 * Asignación de espacios según prioridades (1, 2, 3),
 * cálculo de prioridad por proximidad del próximo viaje en cola,
 * despacho (salida a viaje) y recepción (llegada y retorno de viaje).
 */
public class CocheraServicio {

    public static final int CAPACIDAD_TOTAL = 7;

    private final List<UnidadTransporte> unidades;
    private final int[][] coordenadasCochera;
    private final int[][][] coordenadasPrioridad1;
    private final int[][][] coordenadasPrioridad2;
    private final int[][][] coordenadasPrioridad3;

    public CocheraServicio() {
        this.unidades = new ArrayList<>(CAPACIDAD_TOTAL);
        this.coordenadasCochera = new int[10][10];

        // Prioridad 1: Espacios con salida inmediata (cercanos a salida/maniobra rápida)
        this.coordenadasPrioridad1 = new int[][][]{
            {{6, 1}, {7, 1}, {8, 1}},
            {{6, 6}, {6, 7}, {6, 8}}
        };

        // Prioridad 2: Espacios intermedios
        this.coordenadasPrioridad2 = new int[][][]{
            {{2, 1}, {3, 1}, {4, 1}},
            {{2, 5}, {3, 5}, {4, 5}},
            {{6, 3}, {7, 3}, {8, 3}},
            {{8, 6}, {8, 7}, {8, 8}}
        };

        // Prioridad 3: Espacios profundos (estancia prolongada / sin viaje próximo)
        this.coordenadasPrioridad3 = new int[][][]{
            {{2, 3}, {3, 3}, {4, 3}}
        };
    }

    /**
     * Calcula la prioridad en cochera (1, 2 o 3) según la proximidad del próximo viaje.
     * Si la unidad tiene un viaje urgente o inmediato (primeros turnos en cola) -> Prioridad 1.
     * Si el viaje es intermedio -> Prioridad 2.
     * Si el viaje es lejano o no tiene viaje asignado -> Prioridad 3.
     */
    public int calcularPrioridadPorProximidad(UnidadTransporte unidad) {
        if (unidad == null || unidad.getTractor() == null) {
            return 3;
        }

        String placa = unidad.getPlaca();
        List<Viaje> listaViajes = GestorViajes.getViajes();

        int posicionEnCola = -1;
        for (int i = 0; i < listaViajes.size(); i++) {
            Viaje viaje = listaViajes.get(i);
            if (viaje.getTractor() != null && viaje.getTractor().getPlaca().equalsIgnoreCase(placa)) {
                posicionEnCola = i;
                break;
            }
        }

        if (posicionEnCola == -1) {
            return 3; // Sin viaje programado -> Prioridad 3
        } else if (posicionEnCola <= 1) {
            return 1; // Primeros 2 turnos (incluye viajes urgentes en cabecera) -> Prioridad 1
        } else if (posicionEnCola <= 4) {
            return 2; // Turnos intermedios -> Prioridad 2
        } else {
            return 3; // Turnos lejanos -> Prioridad 3
        }
    }

    /**
     * Registra la llegada de una unidad a la cochera (por ejemplo, al finalizar un viaje previo).
     * Determina su prioridad según la proximidad de su próximo viaje y le asigna un espacio.
     */
    public boolean registrarLlegada(UnidadTransporte unidad) {
        if (unidad == null) {
            throw new IllegalArgumentException("La unidad no puede ser nula");
        }
        if (unidad.estaEnCochera()) {
            return false; // Ya se encuentra estacionada en cochera
        }

        int prioridadCalculada = calcularPrioridadPorProximidad(unidad);
        unidad.setPrioridadCochera(prioridadCalculada);
        asignarEspacioEnCochera(unidad);
        unidad.setEstado(UnidadTransporte.EstadoUnidad.EN_COCHERA);
        return true;
    }

    /**
     * Despacha una unidad para iniciar su viaje programado, liberando el espacio en cochera.
     */
    public boolean registrarSalida(UnidadTransporte unidad) {
        if (unidad == null || !unidad.estaEnCochera()) {
            return false;
        }
        boolean liberado = liberarEspacio(unidad);
        if (liberado) {
            unidad.setEstado(UnidadTransporte.EstadoUnidad.EN_VIAJE);
        }
        return liberado;
    }

    /**
     * Asigna un espacio físico de 3 celdas dentro de la cuadrícula de cochera según su prioridad,
     * implementando fallback si la zona de su prioridad preferida se encuentra saturada.
     */
    public void asignarEspacioEnCochera(UnidadTransporte unidad) {
        if (unidad == null) {
            throw new IllegalArgumentException("La unidad no puede ser nula");
        }
        if (unidad.estaEnCochera()) {
            return;
        }
        if (unidades.size() >= CAPACIDAD_TOTAL) {
            throw new IllegalStateException("La cochera se encuentra a máxima capacidad (7 unidades).");
        }

        int prioridad = unidad.getPrioridadCochera();

        // Intento 1: Asignar en su prioridad designada
        boolean ubicado = switch (prioridad) {
            case 1 -> ubicar(unidad, coordenadasPrioridad1);
            case 2 -> ubicar(unidad, coordenadasPrioridad2);
            case 3 -> ubicar(unidad, coordenadasPrioridad3);
            default -> throw new IllegalArgumentException("Prioridad inválida: " + prioridad);
        };

        // Fallback inteligente si los espacios de su prioridad están ocupados
        if (!ubicado) {
            if (prioridad == 1) {
                ubicado = ubicar(unidad, coordenadasPrioridad2) || ubicar(unidad, coordenadasPrioridad3);
            } else if (prioridad == 2) {
                ubicado = ubicar(unidad, coordenadasPrioridad3) || ubicar(unidad, coordenadasPrioridad1);
            } else {
                ubicado = ubicar(unidad, coordenadasPrioridad2) || ubicar(unidad, coordenadasPrioridad1);
            }
        }

        if (!ubicado) {
            throw new IllegalStateException("No hay espacios disponibles en cochera para la unidad " + unidad.getPlaca());
        }

        if (!unidades.contains(unidad)) {
            unidades.add(unidad);
        }
        unidad.setEstado(UnidadTransporte.EstadoUnidad.EN_COCHERA);
    }

    private boolean ubicar(UnidadTransporte unidad, int[][][] espaciosPrioridad) {
        for (int[][] espacio : espaciosPrioridad) {
            if (espacioLibre(espacio)) {
                unidad.setCoordenadasCochera(espacio);
                for (int[] celda : espacio) {
                    coordenadasCochera[celda[0]][celda[1]] = unidad.getPrioridadCochera();
                }
                return true;
            }
        }
        return false;
    }

    private boolean espacioLibre(int[][] espacio) {
        for (int[] celda : espacio) {
            if (coordenadasCochera[celda[0]][celda[1]] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Libera el espacio ocupado por una unidad en cochera.
     */
    public boolean liberarEspacio(UnidadTransporte unidad) {
        if (unidad == null || !unidad.estaEnCochera()) {
            return false;
        }

        int[][] espacio = unidad.getCoordenadasCochera();
        if (espacio != null) {
            for (int[] celda : espacio) {
                coordenadasCochera[celda[0]][celda[1]] = 0;
            }
        }

        unidad.liberarCochera();
        unidades.remove(unidad);
        return true;
    }

    /**
     * Libera el espacio que ocupa la celda (fila, col) si está ocupada.
     */
    public boolean liberarEspacio(int fila, int col) {
        UnidadTransporte u = getUnidadEn(fila, col);
        if (u != null) {
            return liberarEspacio(u);
        }
        return false;
    }

    /**
     * Reorganiza las unidades estacionadas en cochera reevaluando su prioridad
     * a partir del estado actual y orden de la cola de viajes en GestorViajes.
     */
    public void reorganizarSegunProximidadViajes() {
        List<UnidadTransporte> estacionadas = new ArrayList<>(unidades);

        // Liberar todos los espacios actuales
        for (UnidadTransporte u : estacionadas) {
            liberarEspacio(u);
        }

        // Recalcular prioridades según nueva proximidad
        for (UnidadTransporte u : estacionadas) {
            u.setPrioridadCochera(calcularPrioridadPorProximidad(u));
        }

        // Reubicar ordenando primero las de Prioridad 1, luego 2, luego 3
        estacionadas.sort(Comparator.comparingInt(UnidadTransporte::getPrioridadCochera));

        for (UnidadTransporte u : estacionadas) {
            try {
                asignarEspacioEnCochera(u);
            } catch (IllegalStateException ignored) {
            }
        }
    }

    public UnidadTransporte getUnidadEn(int fila, int col) {
        for (UnidadTransporte u : unidades) {
            if (u.getCoordenadasCochera() != null) {
                for (int[] celda : u.getCoordenadasCochera()) {
                    if (celda[0] == fila && celda[1] == col) {
                        return u;
                    }
                }
            }
        }
        return null;
    }

    public UnidadTransporte getUnidadPorPlaca(String placa) {
        if (placa == null || placa.isBlank()) return null;
        for (UnidadTransporte u : unidades) {
            if (u.getPlaca().equalsIgnoreCase(placa.trim())) {
                return u;
            }
        }
        return null;
    }

    public int getPrioridadDeEspacio(int fila, int col) {
        for (int[][] esp : coordenadasPrioridad1) {
            for (int[] celda : esp) {
                if (celda[0] == fila && celda[1] == col) return 1;
            }
        }
        for (int[][] esp : coordenadasPrioridad2) {
            for (int[] celda : esp) {
                if (celda[0] == fila && celda[1] == col) return 2;
            }
        }
        for (int[][] esp : coordenadasPrioridad3) {
            for (int[] celda : esp) {
                if (celda[0] == fila && celda[1] == col) return 3;
            }
        }
        return 0; // Pasillo / Vía de circulación
    }

    public boolean esPasillo(int fila, int col) {
        return getPrioridadDeEspacio(fila, col) == 0;
    }

    public int[][][] getEspacios(int prioridad) {
        return switch (prioridad) {
            case 1 -> coordenadasPrioridad1;
            case 2 -> coordenadasPrioridad2;
            case 3 -> coordenadasPrioridad3;
            default -> throw new IllegalArgumentException("Prioridad inválida: " + prioridad);
        };
    }

    public List<UnidadTransporte> getUnidades() {
        return Collections.unmodifiableList(unidades);
    }

    public int getCantidadUnidades() {
        return unidades.size();
    }

    public int getCapacidadTotal() {
        return CAPACIDAD_TOTAL;
    }

    public int getEspaciosDisponibles() {
        return CAPACIDAD_TOTAL - unidades.size();
    }

    public boolean estaLlena() {
        return unidades.size() >= CAPACIDAD_TOTAL;
    }

    public int[][] getCoordenadasCochera() {
        return coordenadasCochera;
    }

    public void imprimirCochera() {
        for (int[] fila : coordenadasCochera) {
            System.out.println(java.util.Arrays.toString(fila));
        }
    }
}
