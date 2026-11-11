package servicio;

import estructuras.ColaArreglo;
import estructuras.ColaStringLES;
import estructuras.PilaArreglo;
import estructuras.PilaTerminalLES;
import estructuras.PilaTractorLES;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import modelo.Carreta;
import modelo.Cliente;
import modelo.Contenedor;
import modelo.Direccion;
import modelo.Planta;
import modelo.Ruta;
import modelo.Terminal;
import modelo.Tractor;
import modelo.Ubicacion;
import modelo.UnidadTransporte;
import modelo.Usuario;
import modelo.Viaje;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Suite de pruebas unitarias para evidenciar y validar el funcionamiento
 * de todas las estructuras de datos requeridas en el sistema de transporte:
 * 1. Listas Enlazadas Simples (LES)
 * 2. Listas Enlazadas Dobles (LED)
 * 3. Listas Enlazadas Circulares Simples (LECS)
 * 4. Pilas (Stacks - sobre arreglos y sobre listas enlazadas)
 * 5. Colas (Queues - circular sobre arreglo y sobre lista enlazada)
 * 6. Colas con Prioridad (usando Comparator)
 * 7. Arreglos estáticos 1D, 2D y 3D
 * 8. ArrayLists dinámicos
 */
public class EstructurasDatosIntegracionTest {

    // -------------------------------------------------------------
    // 1. LISTAS ENLAZADAS SIMPLES (LES)
    // -------------------------------------------------------------
    @Test
    public void testListaEnlazadaSimpleContenedores() {
        ContenedorServicio servicio = new ContenedorServicio();
        assertTrue("La lista de contenedores debe iniciar vacía", servicio.estaVacia());

        servicio.guardarContenedor("CONT001", 20000, 2500);
        servicio.guardarContenedor("CONT002", 24000, 2600);
        servicio.insertarInicio(new Contenedor("CONT000", 18000, 2200));

        assertEquals("Debe tener 3 contenedores enlazados", 3, servicio.getTamanio());

        Contenedor primero = servicio.getContenedores().get(0);
        assertEquals("El insertado al inicio debe ser CONT000", "CONT000", primero.getCodigo());

        Contenedor c = servicio.obtenerContenedorPörCodigo("CONT001");
        assertNotNull("Debe encontrar el contenedor mediante recorrido de punteros", c);

        servicio.eliminarContenedor("CONT001");
        assertEquals("El tamaño debe ser 2 tras la eliminación", 2, servicio.getTamanio());
        assertNull("CONT001 ya no debe existir en la lista simple", servicio.obtenerContenedorPörCodigo("CONT001"));
    }

    // -------------------------------------------------------------
    // 2. LISTAS ENLAZADAS DOBLES (LED)
    // -------------------------------------------------------------
    @Test
    public void testListaEnlazadaDobleTerminales() {
        TerminalServicio servicio = new TerminalServicio();
        servicio.guardarTerminal("20100000001", "Terminal DP World", "Callao");
        servicio.guardarTerminal("20100000002", "Terminal APM Terminals", "Callao");
        servicio.guardarTerminal("20100000003", "Terminal Tramarsa", "Paita");

        assertEquals("Debe haber 3 terminales en la lista doble", 3, servicio.getTerminales().size());

        Terminal primera = servicio.irPrimera();
        assertEquals("Primera terminal debe ser DP World", "20100000001", primera.getRuc());

        Terminal siguiente = servicio.irSiguiente();
        assertEquals("Siguiente nodo debe ser APM Terminals", "20100000002", siguiente.getRuc());

        Terminal ultima = servicio.irSiguiente();
        assertEquals("Siguiente nodo debe ser Tramarsa", "20100000003", ultima.getRuc());

        Terminal anterior = servicio.irAnterior();
        assertEquals("Navegación hacia atrás (anterior) debe volver a APM Terminals", "20100000002", anterior.getRuc());
    }

    // -------------------------------------------------------------
    // 3. LISTAS ENLAZADAS CIRCULARES SIMPLES (LECS)
    // -------------------------------------------------------------
    @Test
    public void testListaEnlazadaCircularSimpleRutaCircuito() {
        Ubicacion cochera = new Ubicacion(1, "Ate", "Lima", "Lima", "Av. Separadora Industrial 123");
        Terminal tRetiro = new Terminal("20100000001", "DP World Callao", "Av. Manco Capac");
        Planta pCarga = new Planta("20200000001", "Planta Alicorp", "Av. Argentina");
        Terminal tFinal = new Terminal("20100000002", "APM Terminals", "Av. Contralmirante Raygada");

        Ruta ruta = new Ruta(101, cochera, tRetiro, pCarga, tFinal, 450.0);

        List<Ruta.ParadaCircuito> itinerario = ruta.getItinerarioCircular();
        assertEquals("El circuito cerrado debe constar de 4 paradas", 4, itinerario.size());

        Ruta.ParadaCircuito pInicio = ruta.getParadaActual();
        assertEquals("COCHERA ORIGEN", pInicio.getTipo());

        Ruta.ParadaCircuito p2 = ruta.avanzarParada();
        assertEquals("TERMINAL RETIRO", p2.getTipo());

        Ruta.ParadaCircuito p3 = ruta.avanzarParada();
        assertEquals("PLANTA CARGA", p3.getTipo());

        Ruta.ParadaCircuito p4 = ruta.avanzarParada();
        assertEquals("TERMINAL FINAL", p4.getTipo());

        Ruta.ParadaCircuito vuelta = ruta.avanzarParada();
        assertEquals("El enlace circular debe retornar al nodo inicial (Cochera)", pInicio, vuelta);
    }

    // -------------------------------------------------------------
    // 4. PILAS (STACKS - LIFO)
    // -------------------------------------------------------------
    @Test
    public void testPilasDeshacerOperaciones() {
        // Pila con arreglo estático en PlantaServicio
        PlantaServicio plantaServicio = new PlantaServicio(new java.util.ArrayList<>());
        plantaServicio.guardarPlanta("20500000001", "Planta Gloria", "Huachipa");
        plantaServicio.guardarPlanta("20500000002", "Planta Nestle", "Lima");

        plantaServicio.eliminarPlanta("20500000001");
        assertFalse("Ya no debe existir la planta eliminada", plantaServicio.existePlanta("20500000001"));
        assertTrue("La pila debe contener la planta eliminada", plantaServicio.hayEliminadas());

        Planta recuperada = plantaServicio.deshacerEliminacion();
        assertNotNull("Debe recuperar la planta de la pila", recuperada);
        assertEquals("20500000001", recuperada.getRuc());
        assertTrue("La planta vuelve a estar activa", plantaServicio.existePlanta("20500000001"));

        // Pila con lista enlazada en TerminalServicio
        TerminalServicio terminalServicio = new TerminalServicio();
        terminalServicio.guardarTerminal("20300000001", "Terminal Sur", "Matarani");
        terminalServicio.eliminarPorRuc("20300000001");
        assertTrue("Debe registrar eliminación en la pila", terminalServicio.hayEliminadas());
        Terminal tRecuperada = terminalServicio.deshacerEliminacion();
        assertEquals("20300000001", tRecuperada.getRuc());

        // Pila en GestorViajes (Stack<Viaje>)
        Tractor trac = new Tractor("ABC123", 3, "VOLVO", "FH500", "TRACTO", 2022, "MTC12345");
        UnidadTransporte unidad = new UnidadTransporte(trac, null);
        Contenedor cont = new Contenedor("CONT999", 22000, 2400);
        Viaje v = new Viaje(99, cont, unidad, recuperada, tRecuperada, null, null);

        GestorViajes.agregarViaje(v);
        GestorViajes.eliminarViaje(v);
        assertTrue("Pila de viajes eliminados debe tener al menos 1 elemento", GestorViajes.hayViajesEliminados());
        Viaje vRecuperado = GestorViajes.recuperarViaje();
        assertEquals("El viaje recuperado de la pila debe tener ID 99", 99, vRecuperado.getId());

        // Pila en TractorServicio (Stack<Tractor> y PilaTractorLES)
        TractorServicio tractorServicio = new TractorServicio();
        tractorServicio.registrarTractor("TRA001", 3, "SCANIA", "R450", "TRACTO", 2021, "MTC001");
        tractorServicio.registrarTractor("TRA002", 3, "VOLVO", "FH", "TRACTO", 2022, "MTC002");

        tractorServicio.eliminarTractor("TRA001");
        assertTrue("Debe registrar eliminación en la pila de tractores", tractorServicio.hayEliminados());
        assertEquals("Pila estándar debe tener tamaño 1", 1, tractorServicio.getPilaEliminados().size());
        assertEquals("PilaTractorLES debe tener tamaño 1", 1, tractorServicio.getPilaTractorLES().size());

        Tractor tractorRecuperado = tractorServicio.deshacerEliminacion();
        assertNotNull("Debe recuperar el tractor desapilado", tractorRecuperado);
        assertEquals("TRA001", tractorRecuperado.getPlaca());
        assertNotNull(tractorServicio.getTractorByPlaca("TRA001"));
    }

    // -------------------------------------------------------------
    // 5. COLAS (QUEUES - FIFO)
    // -------------------------------------------------------------
    @Test
    public void testColasAtencionYDespacho() {
        // Cola circular sobre arreglo en ClienteServicio
        ClienteServicio clienteServicio = new ClienteServicio();
        clienteServicio.guardarCliente("20111111111", "Cliente A", "Lima");
        clienteServicio.guardarCliente("20222222222", "Cliente B", "Callao");
        clienteServicio.guardarCliente("20333333333", "Cliente C", "Lurin");

        clienteServicio.pedirAtencion("20111111111");
        clienteServicio.pedirAtencion("20222222222");
        clienteServicio.pedirAtencion("20333333333");

        assertEquals(3, clienteServicio.cantidadEnCola());
        Cliente atendido1 = clienteServicio.atenderSiguiente();
        assertEquals("Cliente A debe ser el primero atendido (FIFO)", "20111111111", atendido1.getRuc());
        Cliente atendido2 = clienteServicio.atenderSiguiente();
        assertEquals("Cliente B debe ser el segundo atendido", "20222222222", atendido2.getRuc());

        // Cola de despacho en patio de tractores (Queue<Tractor> con LinkedList FIFO)
        TractorServicio tractorServicio = new TractorServicio();
        tractorServicio.registrarTractor("TR-101", 3, "SCANIA", "P360", "TRACTO", 2020, "MTC101");
        tractorServicio.registrarTractor("TR-102", 3, "MACK", "ANTHEM", "TRACTO", 2021, "MTC102");
        tractorServicio.encolarPatio("TR-101");
        tractorServicio.encolarPatio("TR-102");
        assertEquals(2, tractorServicio.getCantidadEnPatio());
        Tractor primerTractor = tractorServicio.despacharSiguientePatio();
        assertEquals("TR-101", primerTractor.getPlaca());
        Tractor segundoTractor = tractorServicio.despacharSiguientePatio();
        assertEquals("TR-102", segundoTractor.getPlaca());
        assertEquals(0, tractorServicio.getCantidadEnPatio());

        // Cola circular en UsuarioServicio (ColaArreglo)
        UsuarioServicio userServicio = new UsuarioServicio();
        Direccion dir = new Direccion(1, "Lima", "Lima", "Lima", "Av. Principal 100");
        userServicio.guardarUsuario("12345678", "Admin", dir, "admin@transporte.pe", "1234");
        Usuario aut = userServicio.autenticar("admin@transporte.pe", "1234");
        assertNotNull(aut);
        List<String> ultimos = userServicio.getUltimosAccesos();
        assertTrue("La cola de accesos debe registrar el login", ultimos.contains("admin@transporte.pe"));

        // Cola en Terminal (ColaStringLES)
        Terminal term = new Terminal("20444444444", "Terminal Norte", "Salaverry");
        term.encolarContenedor("CONT-A");
        term.encolarContenedor("CONT-B");
        assertEquals("CONT-A", term.atenderContenedor());
        assertEquals("CONT-B", term.atenderContenedor());
    }

    // -------------------------------------------------------------
    // 6. COLAS CON PRIORIDAD (USANDO COMPARATOR)
    // -------------------------------------------------------------
    @Test
    public void testColaConPrioridadUsandoComparator() {
        PlantaServicio servicio = new PlantaServicio(new java.util.ArrayList<>());
        Planta pBaja = new Planta("20600000001", "Planta Baja Carga", "Lima");
        Planta pAlta = new Planta("20600000002", "Planta Alta Carga", "Callao");
        Planta pMedia = new Planta("20600000003", "Planta Media Carga", "Chorrillos");

        // Asignar carga en la matriz 2D (días x turnos)
        pBaja.setCarga(0, 0, 10);  // Total: 10
        pMedia.setCarga(0, 0, 50); // Total: 50
        pAlta.setCarga(0, 0, 100); // Total: 100

        servicio.guardarPlanta(pBaja.getRuc(), pBaja.getNombre(), pBaja.getDireccion());
        servicio.guardarPlanta(pAlta.getRuc(), pAlta.getNombre(), pAlta.getDireccion());
        servicio.guardarPlanta(pMedia.getRuc(), pMedia.getNombre(), pMedia.getDireccion());

        servicio.programarCarga("20600000001", 0, 0, 10);
        servicio.programarCarga("20600000002", 0, 0, 100);
        servicio.programarCarga("20600000003", 0, 0, 50);

        PriorityQueue<Planta> cola = servicio.colaPorCarga();
        assertNotNull(cola);
        assertEquals(3, cola.size());

        // La cola con prioridad debe entregar primero la de MAYOR carga
        Planta primera = cola.poll();
        assertEquals("Debe despachar primero a la planta con mayor carga", "PLANTA ALTA CARGA", primera.getNombre());

        Planta segunda = cola.poll();
        assertEquals("Debe despachar segundo a la planta con carga media", "PLANTA MEDIA CARGA", segunda.getNombre());

        Planta tercera = cola.poll();
        assertEquals("Debe despachar al final a la planta con menor carga", "PLANTA BAJA CARGA", tercera.getNombre());
    }

    // -------------------------------------------------------------
    // 7. ARREGLOS ESTÁTICOS 1D, 2D Y 3D
    // -------------------------------------------------------------
    @Test
    public void testArreglosEstaticos() {
        // Arreglo estático 2D en Planta
        Planta p = new Planta("20700000001", "Planta Test", "Lima");
        p.setCarga(0, 0, 25);
        p.setCarga(1, 1, 40);
        assertEquals(25, p.getCarga(0, 0));
        assertEquals(40, p.getCarga(1, 1));
        assertEquals(65, p.getCargaTotal());

        // Arreglo estático 1D con búsqueda binaria en CarretaServicio
        CarretaServicio carretaServicio = new CarretaServicio();
        carretaServicio.guardarCarreta("CAA100", 2, "RANDON", "R2", "PLATAFORMA", 2020, "MTC100");
        carretaServicio.guardarCarreta("CAA200", 3, "FACCHINI", "F3", "FURGON", 2021, "MTC200");
        carretaServicio.guardarCarreta("CAA300", 3, "RANDON", "R3", "TOLVA", 2019, "MTC300");

        Carreta encontrada = carretaServicio.obtenerCarretaPorPlaca("CAA200");
        assertNotNull("Búsqueda binaria debe encontrar la carreta", encontrada);
        assertEquals("FACCHINI", encontrada.getMarca());

        // Respaldo, clonación y comparación de arreglos estáticos
        carretaServicio.crearRespaldo();
        assertTrue("La flota debe ser idéntica al respaldo inicial", carretaServicio.flotaIgualAlRespaldo());

        carretaServicio.guardarCarreta("CAA400", 3, "RANDON", "R4", "PLATAFORMA", 2022, "MTC400");
        assertFalse("Tras agregar una carreta, debe detectar diferencia", carretaServicio.flotaIgualAlRespaldo());

        // Arreglos estáticos en Cochera (matriz 2D y coordenadas 3D de espacios)
        CocheraServicio cocheraServicio = new CocheraServicio();
        assertNotNull(cocheraServicio.getCoordenadasCochera());
        assertNotNull(cocheraServicio.getEspacios(1));
    }
}
