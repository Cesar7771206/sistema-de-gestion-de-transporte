package servicio;

import java.util.ArrayList;
import modelo.Carreta;
import modelo.Cliente;
import modelo.Contenedor;
import modelo.Planta;
import modelo.Ruta;
import modelo.Terminal;
import modelo.Tractor;
import modelo.Ubicacion;
import modelo.UnidadTransporte;
import modelo.Viaje;

public class GestorEquipos {

    private static ArrayList<Tractor> tractores = new ArrayList<>();
    private static ArrayList<Carreta> carretas = new ArrayList<>();
    private static ArrayList<Contenedor> contenedores = new ArrayList<>();
    private static ArrayList<UnidadTransporte> unidades = new ArrayList<>();

    public static void agregarTractor(Tractor tractor) {
        if (tractor != null && !tractores.contains(tractor)) {
            tractores.add(tractor);
            obtenerOCrearUnidad(tractor, null);
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
        if (unidad != null && !unidades.contains(unidad)) {
            unidades.add(unidad);
        }
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

    public static UnidadTransporte obtenerOCrearUnidad(Tractor tractor, Carreta carreta) {
        if (tractor == null) return null;
        UnidadTransporte u = buscarUnidadPorTractor(tractor.getPlaca());
        if (u == null) {
            u = new UnidadTransporte(tractor, carreta);
            unidades.add(u);
        } else if (carreta != null && u.getCarreta() == null) {
            u.setCarreta(carreta);
        }
        return u;
    }

    /**
     * Inicializa datos demostrativos completos para equipos, logística, viajes
     * y unidades de transporte si la lista de tractores se encuentra vacía.
     */
    public static void inicializarDatosDemo() {
        if (!tractores.isEmpty()) {
            return;
        }

        try {
            // Tractores
            Tractor t1 = new Tractor("ABC101", 3, "VOLVO", "FH16", "TRACTOCAMION", 2021, "MTC-001");
            Tractor t2 = new Tractor("DEF202", 4, "SCANIA", "R500", "TRACTOCAMION", 2022, "MTC-002");
            Tractor t3 = new Tractor("GHI303", 3, "MERCEDES", "ACTROS", "TRACTOCAMION", 2020, "MTC-003");
            Tractor t4 = new Tractor("JKL404", 4, "INTERNATIONAL", "PROSTAR", "TRACTOCAMION", 2023, "MTC-004");
            Tractor t5 = new Tractor("MNO505", 3, "FREIGHTLINER", "CASCADIA", "TRACTOCAMION", 2021, "MTC-005");

            agregarTractor(t1);
            agregarTractor(t2);
            agregarTractor(t3);
            agregarTractor(t4);
            agregarTractor(t5);

            // Carretas (placas de 6 caracteres alfanuméricos)
            Carreta c1 = new Carreta("CAR001", 3, "RANDON", "PLATAFORMA", "BARANDA", 2021, "MTC-C01");
            Carreta c2 = new Carreta("CAR002", 3, "FACCHINI", "PORTACONTENEDOR", "PLATAFORMA", 2022, "MTC-C02");
            Carreta c3 = new Carreta("CAR003", 3, "GUERRA", "PLATAFORMA", "PLATAFORMA", 2020, "MTC-C03");
            Carreta c4 = new Carreta("CAR004", 3, "MONTENEGRO", "FURGON", "CERRADA", 2023, "MTC-C04");
            Carreta c5 = new Carreta("CAR005", 3, "RANDON", "PORTACONTENEDOR", "PLATAFORMA", 2021, "MTC-C05");

            agregarCarreta(c1);
            agregarCarreta(c2);
            agregarCarreta(c3);
            agregarCarreta(c4);
            agregarCarreta(c5);

            // Contenedores
            Contenedor cnt1 = new Contenedor("MSKU1234567", 25000, 3800);
            Contenedor cnt2 = new Contenedor("CMAU7654321", 28000, 4100);
            Contenedor cnt3 = new Contenedor("HLCU9876543", 22000, 3600);

            agregarContenedor(cnt1);
            agregarContenedor(cnt2);
            agregarContenedor(cnt3);

            // Unidades operativas
            UnidadTransporte u1 = obtenerOCrearUnidad(t1, c1);
            UnidadTransporte u2 = obtenerOCrearUnidad(t2, c2);
            UnidadTransporte u3 = obtenerOCrearUnidad(t3, c3);
            UnidadTransporte u4 = obtenerOCrearUnidad(t4, c4);
            UnidadTransporte u5 = obtenerOCrearUnidad(t5, c5);

            // Logística
            Cliente cl1 = new Cliente("20100000001", "ALICORP S.A.A.", "AV. ARGENTINA 4793, CALLAO");
            Cliente cl2 = new Cliente("20200000002", "GLORIA S.A.", "AV. REPUBLICA DE PANAMA 2461, LIMA");
            Cliente cl3 = new Cliente("20300000003", "NESTLE PERU S.A.", "AV. LOS LIBERTADORES 850, LIMA");
            GestorLogistica.agregarCliente(cl1);
            GestorLogistica.agregarCliente(cl2);
            GestorLogistica.agregarCliente(cl3);

            Planta pl1 = new Planta("20500000001", "PLANTA HUACHIPA", "CARRETERA CENTRAL KM 9.5");
            Planta pl2 = new Planta("20600000002", "PLANTA CALLAO", "AV. NESTOR GAMBETTA 1200");
            GestorLogistica.agregarPlanta(pl1);
            GestorLogistica.agregarPlanta(pl2);

            Terminal tm1 = new Terminal("20400000001", "DP WORLD CALLAO", "AV. GUADALUPE 100");
            Terminal tm2 = new Terminal("20400000002", "APM TERMINALS", "AV. CONTRALMIRANTE RAYGADA 111");
            GestorLogistica.agregarTerminal(tm1);
            GestorLogistica.agregarTerminal(tm2);

            Ubicacion ubCochera = new Ubicacion(1, "Callao", "Callao", "Callao", "Av. Néstor Gambetta km 4");
            Ruta r1 = new Ruta(1, ubCochera, tm1, pl1, tm1, 1500.0);
            Ruta r2 = new Ruta(2, ubCochera, tm2, pl2, tm2, 1800.0);
            GestorLogistica.agregarRuta(r1);
            GestorLogistica.agregarRuta(r2);

            // Viajes con diferente proximidad en cola:
            // Viaje 1 (Urgente / Primer turno -> Unidad 1 ABC101 -> P1)
            Viaje v1 = new Viaje(1, cnt1, t1, c1, pl1, tm1, cl1, r1);
            // Viaje 2 (Segundo turno -> Unidad 2 DEF202 -> P1)
            Viaje v2 = new Viaje(2, cnt2, t2, c2, pl2, tm2, cl2, r2);
            // Viaje 3 (Tercer turno -> Unidad 3 GHI303 -> P2)
            Viaje v3 = new Viaje(3, cnt3, t3, c3, pl1, tm1, cl3, r1);
            // Viaje 4 (Cuarto turno -> Unidad 4 JKL404 -> P2)
            Viaje v4 = new Viaje(4, cnt1, t4, c4, pl2, tm2, cl1, r2);
            // (Unidad 5 MNO505 no tiene viaje asignado -> P3)

            GestorViajes.agregarViaje(v1);
            GestorViajes.agregarViaje(v2);
            GestorViajes.agregarViaje(v3);
            GestorViajes.agregarViaje(v4);

        } catch (Exception ex) {
            System.err.println("Aviso al inicializar datos demo: " + ex.getMessage());
        }
    }
}
