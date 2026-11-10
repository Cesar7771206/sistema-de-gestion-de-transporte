package servicio;

import static org.junit.Assert.*;

import java.util.List;
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
import org.junit.Before;
import org.junit.Test;

public class CocheraServicioTest {

    private CocheraServicio cocheraServicio;

    @Before
    public void setUp() {
        // Limpiar viajes existentes para pruebas controladas
        GestorViajes.getViajes().clear();
        cocheraServicio = new CocheraServicio();
    }

    private Tractor crearTractor(String placa) {
        return new Tractor(placa, 3, "VOLVO", "FH", "TRACTO", 2021, "MTC01");
    }

    private Carreta crearCarreta(String placa) {
        return new Carreta(placa, 3, "RANDON", "PLATAFORMA", "BARANDA", 2021, "MTC01");
    }

    private Viaje crearViaje(int id, Tractor t, Carreta c) {
        Contenedor cnt = new Contenedor("CNTR" + id, 20000, 3000);
        Planta pl = new Planta("20111111111", "PLANTA SUR", "AV CENTRAL 100");
        Terminal tm = new Terminal("20222222222", "TERMINAL MAR", "PUERTO 1");
        Cliente cl = new Cliente("20333333333", "CLIENTE TEST", "AV LIMA 200");
        Ubicacion ub = new Ubicacion(1, "Callao", "Callao", "Callao", "Av Gambetta");
        Ruta r = new Ruta(id, ub, tm, pl, tm, 1000.0);
        return new Viaje(id, cnt, t, c, pl, tm, cl, r);
    }

    @Test
    public void testCalcularPrioridadPorProximidadDeViajes() {
        Tractor t1 = crearTractor("TRK001");
        Tractor t2 = crearTractor("TRK002");
        Tractor t3 = crearTractor("TRK003");
        Tractor t4 = crearTractor("TRK004");
        Tractor t5 = crearTractor("TRK005");
        Tractor t6 = crearTractor("TRK006");
        Tractor tSinViaje = crearTractor("TRK999");

        Carreta c1 = crearCarreta("CAR001");

        // Registrar 6 viajes en orden de proximidad
        GestorViajes.agregarViaje(crearViaje(1, t1, c1)); // Turno 0 -> P1
        GestorViajes.agregarViaje(crearViaje(2, t2, c1)); // Turno 1 -> P1
        GestorViajes.agregarViaje(crearViaje(3, t3, c1)); // Turno 2 -> P2
        GestorViajes.agregarViaje(crearViaje(4, t4, c1)); // Turno 3 -> P2
        GestorViajes.agregarViaje(crearViaje(5, t5, c1)); // Turno 4 -> P2
        GestorViajes.agregarViaje(crearViaje(6, t6, c1)); // Turno 5 -> P3

        UnidadTransporte u1 = new UnidadTransporte(t1, c1);
        UnidadTransporte u2 = new UnidadTransporte(t2, c1);
        UnidadTransporte u3 = new UnidadTransporte(t3, c1);
        UnidadTransporte u4 = new UnidadTransporte(t4, c1);
        UnidadTransporte u5 = new UnidadTransporte(t5, c1);
        UnidadTransporte u6 = new UnidadTransporte(t6, c1);
        UnidadTransporte uSin = new UnidadTransporte(tSinViaje, c1);

        // Turnos 0 y 1 son inmediatos -> Prioridad 1
        assertEquals(1, cocheraServicio.calcularPrioridadPorProximidad(u1));
        assertEquals(1, cocheraServicio.calcularPrioridadPorProximidad(u2));

        // Turnos 2 a 4 son intermedios -> Prioridad 2
        assertEquals(2, cocheraServicio.calcularPrioridadPorProximidad(u3));
        assertEquals(2, cocheraServicio.calcularPrioridadPorProximidad(u4));
        assertEquals(2, cocheraServicio.calcularPrioridadPorProximidad(u5));

        // Turno 5 o más lejano -> Prioridad 3
        assertEquals(3, cocheraServicio.calcularPrioridadPorProximidad(u6));

        // Sin viaje programado -> Prioridad 3
        assertEquals(3, cocheraServicio.calcularPrioridadPorProximidad(uSin));
    }

    @Test
    public void testAsignarEspaciosSegunPrioridad() {
        Tractor tP1 = crearTractor("PRI001");
        Tractor tP2 = crearTractor("PRI002");
        Tractor tP3 = crearTractor("PRI003");

        Carreta c = crearCarreta("CAR100");

        UnidadTransporte u1 = new UnidadTransporte(tP1, c, 1);
        UnidadTransporte u2 = new UnidadTransporte(tP2, c, 2);
        UnidadTransporte u3 = new UnidadTransporte(tP3, c, 3);

        cocheraServicio.asignarEspacioEnCochera(u1);
        cocheraServicio.asignarEspacioEnCochera(u2);
        cocheraServicio.asignarEspacioEnCochera(u3);

        assertTrue("u1 debe estar en cochera", u1.estaEnCochera());
        assertTrue("u2 debe estar en cochera", u2.estaEnCochera());
        assertTrue("u3 debe estar en cochera", u3.estaEnCochera());

        // Verificar que ocupan 3 celdas cada una
        assertEquals(3, u1.getCoordenadasCochera().length);
        assertEquals(3, u2.getCoordenadasCochera().length);
        assertEquals(3, u3.getCoordenadasCochera().length);

        // Verificar que la primera celda de u1 pertenece a zona P1
        int f1 = u1.getCoordenadasCochera()[0][0];
        int c1 = u1.getCoordenadasCochera()[0][1];
        assertEquals(1, cocheraServicio.getPrioridadDeEspacio(f1, c1));

        // Verificar que la primera celda de u2 pertenece a zona P2
        int f2 = u2.getCoordenadasCochera()[0][0];
        int c2 = u2.getCoordenadasCochera()[0][1];
        assertEquals(2, cocheraServicio.getPrioridadDeEspacio(f2, c2));

        // Verificar que la primera celda de u3 pertenece a zona P3
        int f3 = u3.getCoordenadasCochera()[0][0];
        int c3 = u3.getCoordenadasCochera()[0][1];
        assertEquals(3, cocheraServicio.getPrioridadDeEspacio(f3, c3));
    }

    @Test
    public void testCicloSalidaDespachoYLlegadaRetorno() {
        Tractor t = crearTractor("AUT001");
        Carreta c = crearCarreta("CAR200");
        UnidadTransporte u = new UnidadTransporte(t, c);

        // No tiene viaje asignado inicialmente -> P3
        boolean llegada1 = cocheraServicio.registrarLlegada(u);
        assertTrue(llegada1);
        assertTrue(u.estaEnCochera());
        assertEquals(3, u.getPrioridadCochera());
        assertEquals(UnidadTransporte.EstadoUnidad.EN_COCHERA, u.getEstado());
        assertEquals(1, cocheraServicio.getCantidadUnidades());

        // Despachar la unidad a viaje (salida de cochera)
        boolean salida = cocheraServicio.registrarSalida(u);
        assertTrue(salida);
        assertFalse(u.estaEnCochera());
        assertEquals(UnidadTransporte.EstadoUnidad.EN_VIAJE, u.getEstado());
        assertEquals(0, cocheraServicio.getCantidadUnidades());

        // Ahora se le asigna un viaje urgente en cabeza de cola
        GestorViajes.agregarViajeUrgente(crearViaje(99, t, c));

        // La unidad finaliza su viaje previo y retorna a la cochera
        boolean llegada2 = cocheraServicio.registrarLlegada(u);
        assertTrue(llegada2);
        assertTrue(u.estaEnCochera());
        // Su próximo viaje es el turno 0 -> debe recibir Prioridad 1 automáticamente
        assertEquals(1, u.getPrioridadCochera());
        assertEquals(UnidadTransporte.EstadoUnidad.EN_COCHERA, u.getEstado());
        assertEquals(1, cocheraServicio.getCantidadUnidades());
    }

    @Test
    public void testReorganizarPorProximidadAlAgregarViajeUrgente() {
        Tractor t1 = crearTractor("TRK100");
        Tractor t2 = crearTractor("TRK200");
        Carreta c = crearCarreta("CAR300");

        UnidadTransporte u1 = new UnidadTransporte(t1, c);
        UnidadTransporte u2 = new UnidadTransporte(t2, c);

        // Inicialmente u1 tiene viaje (P1) y u2 no tiene viaje (P3)
        GestorViajes.agregarViaje(crearViaje(1, t1, c));

        cocheraServicio.registrarLlegada(u1);
        cocheraServicio.registrarLlegada(u2);

        assertEquals(1, u1.getPrioridadCochera());
        assertEquals(3, u2.getPrioridadCochera());

        // Ahora se programa un viaje URGENTE para u2 al inicio de la cola
        GestorViajes.agregarViajeUrgente(crearViaje(2, t2, c));

        // Reorganizar espacios por proximidad
        cocheraServicio.reorganizarSegunProximidadViajes();

        // Ahora tanto u2 (turno 0) como u1 (turno 1) tienen máxima proximidad -> P1
        assertEquals(1, u2.getPrioridadCochera());
        assertEquals(1, u1.getPrioridadCochera());
    }

    @Test
    public void testCapacidadMaximaCochera() {
        Carreta c = crearCarreta("CAR400");
        for (int i = 1; i <= 7; i++) {
            Tractor t = crearTractor("TRK00" + i);
            UnidadTransporte u = new UnidadTransporte(t, c, (i <= 2) ? 1 : ((i <= 6) ? 2 : 3));
            cocheraServicio.asignarEspacioEnCochera(u);
        }

        assertEquals(7, cocheraServicio.getCantidadUnidades());
        assertEquals(0, cocheraServicio.getEspaciosDisponibles());
        assertTrue(cocheraServicio.estaLlena());

        // Intentar agregar una octava unidad debe arrojar IllegalStateException
        Tractor tExtra = crearTractor("TRK008");
        UnidadTransporte uExtra = new UnidadTransporte(tExtra, c, 1);
        assertThrows(IllegalStateException.class, () -> {
            cocheraServicio.asignarEspacioEnCochera(uExtra);
        });
    }

    @Test
    public void testPanelModuloCocheraInstanciacion() {
        GestorEquipos.getUnidades().clear();
        vista.PanelModuloCochera panel = new vista.PanelModuloCochera();
        assertNotNull(panel);
        panel.refrescar();
        assertEquals(0, panel.getServicio().getCantidadUnidades());
    }

    @Test
    public void testEspaciosCompuestosDeTresCoordenadas() {
        // P1: 2 espacios x 3 coordenadas
        int[][][] p1 = cocheraServicio.getEspacios(1);
        assertEquals(2, p1.length);
        for (int[][] esp : p1) {
            assertEquals("Cada espacio debe tener exactamente 3 coordenadas", 3, esp.length);
        }

        // P2: 4 espacios x 3 coordenadas
        int[][][] p2 = cocheraServicio.getEspacios(2);
        assertEquals(4, p2.length);
        for (int[][] esp : p2) {
            assertEquals("Cada espacio debe tener exactamente 3 coordenadas", 3, esp.length);
        }

        // P3: 1 espacio x 3 coordenadas
        int[][][] p3 = cocheraServicio.getEspacios(3);
        assertEquals(1, p3.length);
        for (int[][] esp : p3) {
            assertEquals("Cada espacio debe tener exactamente 3 coordenadas", 3, esp.length);
        }
    }

    @Test
    public void testPanelModuloCocheraBuscarEspacio() {
        vista.PanelModuloCochera panel = new vista.PanelModuloCochera();
        // Celda (6, 1) pertenece a espacio P1
        int[][] espP1 = panel.buscarEspacio(6, 1);
        assertNotNull("Debe encontrar el espacio de 3 celdas para (6,1)", espP1);
        assertEquals(3, espP1.length);
        assertArrayEquals(new int[]{6, 1}, espP1[0]);
        assertArrayEquals(new int[]{7, 1}, espP1[1]);
        assertArrayEquals(new int[]{8, 1}, espP1[2]);

        // Celda (0, 0) es pasillo de maniobra
        int[][] espPasillo = panel.buscarEspacio(0, 0);
        assertNull("Un pasillo no debe pertenecer a ningún espacio asignable", espPasillo);
    }

    @Test
    public void testFallbackPrioridadCuandoZonaLlena() {
        Carreta c = crearCarreta("CAR500");
        UnidadTransporte u1 = new UnidadTransporte(crearTractor("P1A"), c, 1);
        UnidadTransporte u2 = new UnidadTransporte(crearTractor("P1B"), c, 1);
        UnidadTransporte u3 = new UnidadTransporte(crearTractor("P1C"), c, 1);

        cocheraServicio.asignarEspacioEnCochera(u1);
        cocheraServicio.asignarEspacioEnCochera(u2);
        // Zona P1 solo tiene 2 espacios, la 3ra unidad con P1 debe reubicarse en fallback (P2)
        cocheraServicio.asignarEspacioEnCochera(u3);

        assertTrue(u3.estaEnCochera());
        int f = u3.getCoordenadasCochera()[0][0];
        int col = u3.getCoordenadasCochera()[0][1];
        assertEquals("Debe ubicarse en espacio P2 por fallback", 2, cocheraServicio.getPrioridadDeEspacio(f, col));
    }
}
