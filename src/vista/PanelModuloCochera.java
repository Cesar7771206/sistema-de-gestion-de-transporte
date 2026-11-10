package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import modelo.UnidadTransporte;
import modelo.Viaje;
import servicio.CocheraServicio;
import servicio.GestorEquipos;
import servicio.GestorViajes;

/**
 * Panel visual para el Módulo de Cochera.
 * Representa la cuadrícula 10x10 del patio de estacionamiento y gestiona
 * la asignación y reubicación de unidades de transporte según la proximidad
 * de sus viajes programados (Prioridad 1: inmediata, 2: media, 3: baja/sin viaje).
 */
public class PanelModuloCochera extends javax.swing.JPanel {

    // Paleta de colores para la cochera
    private static final Color COLOR_FONDO_PASILLO = new Color(33, 37, 41);
    private static final Color COLOR_P1_LIBRE = new Color(255, 193, 7);      // Ámbar: Salida rápida
    private static final Color COLOR_P2_LIBRE = new Color(23, 162, 184);     // Cian: Salida intermedia
    private static final Color COLOR_P3_LIBRE = new Color(108, 117, 125);    // Gris: Estancia larga
    private static final Color COLOR_OCUPADO = new Color(40, 167, 69);       // Verde: Unidad en cochera
    private static final Color COLOR_SELECCION = new Color(255, 87, 34);     // Naranja: Celda activa

    private final CocheraServicio servicio = new CocheraServicio();
    private final JLabel[][] celdas = new JLabel[10][10];

    private int celdaSeleccionadaFila = -1;
    private int celdaSeleccionadaCol = -1;

    // Componentes del panel lateral de detalles y operaciones
    private JLabel lblCapacidad;
    private JLabel lblDetalleCoord;
    private JLabel lblDetalleZona;
    private JLabel lblDetalleEstado;
    private JLabel lblDetalleTractor;
    private JLabel lblDetalleCarreta;
    private JLabel lblDetalleViaje;
    private JLabel lblDetalleProximidad;

    private JButton btnDespacharViaje;
    private JButton btnLiberarEspacio;
    private JComboBox<String> cboUnidadesFuera;
    private JButton btnRegistrarLlegada;
    private JButton btnReorganizarProximidad;

    public PanelModuloCochera() {
        initComponents();
        configurarEstructuraVisual();
        inicializarDatosDemoSiEsNecesario();
        construirCuadricula();
        refrescar();
    }

    /**
     * Reconfigura el diseño general del panel con una cabecera informativa,
     * el patio cuadriculado central y la consola de control y detalle lateral.
     */
    private void configurarEstructuraVisual() {
        this.removeAll();
        this.setLayout(new BorderLayout(15, 15));
        this.setBackground(Color.WHITE);
        this.setBorder(new EmptyBorder(15, 20, 15, 20));

        // 1. Cabecera superior
        JPanel panelCabecera = new JPanel(new BorderLayout());
        panelCabecera.setOpaque(false);

        JLabel lblTitulo = new JLabel("MÓDULO COCHERA - CONTROL DE PATIO");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(30, 41, 59));

        lblCapacidad = new JLabel("Cargando estado...");
        lblCapacidad.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCapacidad.setForeground(new Color(71, 85, 105));

        panelCabecera.add(lblTitulo, BorderLayout.WEST);
        panelCabecera.add(lblCapacidad, BorderLayout.EAST);
        this.add(panelCabecera, BorderLayout.NORTH);

        // 2. Panel Central (Cuadrícula Cochera)
        JPanel panelContenedorCuadricula = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelContenedorCuadricula.setOpaque(false);

        panelCochera.setPreferredSize(new Dimension(560, 560));
        panelCochera.setMaximumSize(new Dimension(560, 560));
        panelCochera.setBackground(new Color(20, 24, 27));
        panelCochera.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(51, 65, 85), 2),
                new EmptyBorder(4, 4, 4, 4)
        ));
        panelContenedorCuadricula.add(panelCochera);
        this.add(panelContenedorCuadricula, BorderLayout.CENTER);

        // 3. Panel Lateral Este (Información y Acciones)
        JPanel panelLateral = construirPanelLateral();
        JScrollPane scrollLateral = new JScrollPane(panelLateral);
        scrollLateral.setBorder(null);
        scrollLateral.setPreferredSize(new Dimension(285, 560));
        scrollLateral.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.add(scrollLateral, BorderLayout.EAST);
    }

    /**
     * Construye el panel lateral derecho con secciones de detalle, acciones
     * de llegada/salida y leyenda interactiva.
     */
    private JPanel construirPanelLateral() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 5, 5, 5));

        // Tarjeta: Detalle de Celda Seleccionada
        JPanel pnlDetalle = new JPanel();
        pnlDetalle.setLayout(new BoxLayout(pnlDetalle, BoxLayout.Y_AXIS));
        pnlDetalle.setBackground(new Color(248, 250, 252));
        pnlDetalle.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(203, 213, 225), 1),
                "Información del Espacio",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(30, 41, 59)
        ));

        lblDetalleCoord = new JLabel("Coordenadas: Seleccione una celda");
        lblDetalleZona = new JLabel("Zona: -");
        lblDetalleEstado = new JLabel("Estado: -");
        lblDetalleTractor = new JLabel("Tractor: -");
        lblDetalleCarreta = new JLabel("Carreta: -");
        lblDetalleViaje = new JLabel("Próximo Viaje: -");
        lblDetalleProximidad = new JLabel("Proximidad: -");

        Font fontEtiquetas = new Font("Segoe UI", Font.PLAIN, 12);
        for (JLabel lbl : new JLabel[]{lblDetalleCoord, lblDetalleZona, lblDetalleEstado,
                lblDetalleTractor, lblDetalleCarreta, lblDetalleViaje, lblDetalleProximidad}) {
            lbl.setFont(fontEtiquetas);
            lbl.setAlignmentX(LEFT_ALIGNMENT);
            lbl.setBorder(new EmptyBorder(2, 4, 2, 4));
            pnlDetalle.add(lbl);
        }

        pnlDetalle.add(Box.createVerticalStrut(8));

        // Botones de acción para la celda seleccionada
        btnDespacharViaje = new JButton("Despachar a Viaje (Salida)");
        btnDespacharViaje.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnDespacharViaje.setBackground(new Color(13, 110, 253));
        btnDespacharViaje.setForeground(Color.WHITE);
        btnDespacharViaje.setAlignmentX(LEFT_ALIGNMENT);
        btnDespacharViaje.setMaximumSize(new Dimension(260, 30));
        btnDespacharViaje.setEnabled(false);
        btnDespacharViaje.addActionListener(e -> despacharUnidadSeleccionada());

        btnLiberarEspacio = new JButton("Liberar Espacio");
        btnLiberarEspacio.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnLiberarEspacio.setBackground(new Color(220, 53, 69));
        btnLiberarEspacio.setForeground(Color.WHITE);
        btnLiberarEspacio.setAlignmentX(LEFT_ALIGNMENT);
        btnLiberarEspacio.setMaximumSize(new Dimension(260, 28));
        btnLiberarEspacio.setEnabled(false);
        btnLiberarEspacio.addActionListener(e -> liberarEspacioSeleccionado());

        pnlDetalle.add(btnDespacharViaje);
        pnlDetalle.add(Box.createVerticalStrut(4));
        pnlDetalle.add(btnLiberarEspacio);
        panel.add(pnlDetalle);

        panel.add(Box.createVerticalStrut(10));

        // Tarjeta: Llegada y Asignación por Prioridad
        JPanel pnlLlegada = new JPanel();
        pnlLlegada.setLayout(new BoxLayout(pnlLlegada, BoxLayout.Y_AXIS));
        pnlLlegada.setBackground(new Color(248, 250, 252));
        pnlLlegada.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(203, 213, 225), 1),
                "Llegada de Unidad (Fin de Viaje)",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(30, 41, 59)
        ));

        JLabel lblComboTexto = new JLabel("Unidad en arribo / retorno:");
        lblComboTexto.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblComboTexto.setAlignmentX(LEFT_ALIGNMENT);
        pnlLlegada.add(lblComboTexto);
        pnlLlegada.add(Box.createVerticalStrut(4));

        cboUnidadesFuera = new JComboBox<>();
        cboUnidadesFuera.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cboUnidadesFuera.setAlignmentX(LEFT_ALIGNMENT);
        cboUnidadesFuera.setMaximumSize(new Dimension(260, 28));
        pnlLlegada.add(cboUnidadesFuera);

        pnlLlegada.add(Box.createVerticalStrut(6));

        btnRegistrarLlegada = new JButton("Registrar Llegada a Cochera");
        btnRegistrarLlegada.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnRegistrarLlegada.setBackground(new Color(25, 135, 84));
        btnRegistrarLlegada.setForeground(Color.WHITE);
        btnRegistrarLlegada.setAlignmentX(LEFT_ALIGNMENT);
        btnRegistrarLlegada.setMaximumSize(new Dimension(260, 30));
        btnRegistrarLlegada.addActionListener(e -> registrarLlegadaDeUnidad());
        pnlLlegada.add(btnRegistrarLlegada);

        panel.add(pnlLlegada);

        panel.add(Box.createVerticalStrut(10));

        // Tarjeta: Sincronización y Reorganización
        JPanel pnlAcciones = new JPanel();
        pnlAcciones.setLayout(new BoxLayout(pnlAcciones, BoxLayout.Y_AXIS));
        pnlAcciones.setBackground(new Color(248, 250, 252));
        pnlAcciones.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(203, 213, 225), 1),
                "Optimización de Patio",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 12),
                new Color(30, 41, 59)
        ));

        btnReorganizarProximidad = new JButton("Reorganizar según Proximidad");
        btnReorganizarProximidad.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnReorganizarProximidad.setBackground(new Color(102, 16, 242));
        btnReorganizarProximidad.setForeground(Color.WHITE);
        btnReorganizarProximidad.setAlignmentX(LEFT_ALIGNMENT);
        btnReorganizarProximidad.setMaximumSize(new Dimension(260, 30));
        btnReorganizarProximidad.addActionListener(e -> reorganizarPorProximidad());
        pnlAcciones.add(btnReorganizarProximidad);

        panel.add(pnlAcciones);

        panel.add(Box.createVerticalStrut(10));

        // Tarjeta: Leyenda de Prioridades
        JPanel pnlLeyenda = new JPanel();
        pnlLeyenda.setLayout(new BoxLayout(pnlLeyenda, BoxLayout.Y_AXIS));
        pnlLeyenda.setBackground(Color.WHITE);
        pnlLeyenda.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(new Color(226, 232, 240), 1),
                "Leyenda de Zonas y Prioridad",
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 11),
                new Color(71, 85, 105)
        ));

        pnlLeyenda.add(crearFilaLeyenda(COLOR_OCUPADO, "Ocupado: Unidad en Cochera"));
        pnlLeyenda.add(crearFilaLeyenda(COLOR_P1_LIBRE, "P1: Salida Inmediata (Próximo)"));
        pnlLeyenda.add(crearFilaLeyenda(COLOR_P2_LIBRE, "P2: Salida Media (Intermedio)"));
        pnlLeyenda.add(crearFilaLeyenda(COLOR_P3_LIBRE, "P3: Estancia Larga / Sin Viaje"));
        pnlLeyenda.add(crearFilaLeyenda(COLOR_FONDO_PASILLO, "Vía de Maniobras y Circulación"));

        panel.add(pnlLeyenda);

        return panel;
    }

    private JPanel crearFilaLeyenda(Color color, String texto) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 2));
        fila.setOpaque(false);
        JLabel muestra = new JLabel();
        muestra.setOpaque(true);
        muestra.setBackground(color);
        muestra.setPreferredSize(new Dimension(14, 14));
        muestra.setBorder(new LineBorder(Color.DARK_GRAY, 1));

        JLabel lblTexto = new JLabel(texto);
        lblTexto.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblTexto.setForeground(new Color(51, 65, 85));

        fila.add(muestra);
        fila.add(lblTexto);
        return fila;
    }

    /**
     * Inicializa datos demostrativos si es necesario y asigna las unidades iniciales
     * a sus espacios en cochera según la proximidad de los viajes registrados.
     */
    private void inicializarDatosDemoSiEsNecesario() {
        GestorEquipos.inicializarDatosDemo();

        if (servicio.getCantidadUnidades() == 0) {
            for (UnidadTransporte u : GestorEquipos.getUnidades()) {
                if (!u.estaEnCochera() && !servicio.estaLlena()) {
                    try {
                        servicio.registrarLlegada(u);
                    } catch (IllegalStateException ignored) {
                    }
                }
            }
        }
    }

    /**
     * Construye la cuadrícula de 10x10 JLabels dentro del panel de cochera.
     */
    private void construirCuadricula() {
        panelCochera.removeAll();
        panelCochera.setLayout(new GridLayout(10, 10, 2, 2));

        for (int f = 0; f < 10; f++) {
            for (int c = 0; c < 10; c++) {
                JLabel celda = new JLabel("", SwingConstants.CENTER);
                celda.setOpaque(true);
                celda.setBackground(COLOR_FONDO_PASILLO);
                celda.setForeground(Color.WHITE);
                celda.setFont(new Font("Segoe UI", Font.BOLD, 10));
                celda.setCursor(new Cursor(Cursor.HAND_CURSOR));

                final int fila = f;
                final int col = c;
                celda.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        mostrarDatos(fila, col);
                    }
                });

                celdas[f][c] = celda;
                panelCochera.add(celda);
            }
        }
    }

    /**
     * Actualiza la visualización de todos los espacios libres, unidades estacionadas,
     * capacidad global y lista de unidades fuera de cochera.
     */
    public void refrescar() {
        // 1. Restaurar pasillos y pintar espacios disponibles por prioridad
        for (int f = 0; f < 10; f++) {
            for (int c = 0; c < 10; c++) {
                JLabel celda = celdas[f][c];
                celda.setBorder(new LineBorder(new Color(40, 44, 52), 1));
                celda.setForeground(Color.WHITE);

                int prioridad = servicio.getPrioridadDeEspacio(f, c);
                if (prioridad == 0) {
                    celda.setBackground(COLOR_FONDO_PASILLO);
                    celda.setText("");
                    celda.setToolTipText("Vía de circulación [" + f + ", " + c + "]");
                } else {
                    celda.setText("P" + prioridad);
                    celda.setToolTipText("Espacio Libre - Prioridad " + prioridad + " [" + f + ", " + c + "]");
                    switch (prioridad) {
                        case 1 -> celda.setBackground(COLOR_P1_LIBRE);
                        case 2 -> celda.setBackground(COLOR_P2_LIBRE);
                        case 3 -> celda.setBackground(COLOR_P3_LIBRE);
                    }
                }
            }
        }

        // 2. Pintar unidades actualmente estacionadas en cochera
        for (UnidadTransporte unidad : servicio.getUnidades()) {
            pintarUnidad(unidad);
        }

        // 3. Resaltar la celda seleccionada si existe
        if (celdaSeleccionadaFila != -1 && celdaSeleccionadaCol != -1) {
            celdas[celdaSeleccionadaFila][celdaSeleccionadaCol].setBorder(
                    new LineBorder(COLOR_SELECCION, 2)
            );
            mostrarDatos(celdaSeleccionadaFila, celdaSeleccionadaCol);
        }

        // 4. Actualizar contador de capacidad
        lblCapacidad.setText("Ocupación: " + servicio.getCantidadUnidades() + " / "
                + servicio.getCapacidadTotal() + " espacios ("
                + servicio.getEspaciosDisponibles() + " disponibles)");

        // 5. Actualizar combo de unidades que se encuentran fuera de cochera
        actualizarComboUnidadesFuera();

        panelCochera.revalidate();
        panelCochera.repaint();
    }

    private void pintarUnidad(UnidadTransporte unidad) {
        int[][] coordenadas = unidad.getCoordenadasCochera();
        if (coordenadas == null) return;

        for (int i = 0; i < coordenadas.length; i++) {
            int f = coordenadas[i][0];
            int c = coordenadas[i][1];
            JLabel l = celdas[f][c];
            l.setBackground(COLOR_OCUPADO);
            l.setForeground(Color.WHITE);

            if (i == 0) {
                l.setText(unidad.getPlaca());
            } else if (i == 1) {
                l.setText("P" + unidad.getPrioridadCochera());
            } else {
                String carretaTexto = (unidad.getCarreta() != null) ? unidad.getCarreta().getPlaca() : "";
                l.setText(carretaTexto);
            }

            l.setToolTipText("Unidad " + unidad.getPlaca() + " | Prioridad "
                    + unidad.getPrioridadCochera() + " [" + f + ", " + c + "]");
        }
    }

    /**
     * Muestra en el panel lateral los detalles completos de la celda pulsada.
     */
    private void mostrarDatos(int fila, int col) {
        this.celdaSeleccionadaFila = fila;
        this.celdaSeleccionadaCol = col;

        // Quitar bordes de selección previos y colocar el borde en la celda activa
        for (int f = 0; f < 10; f++) {
            for (int c = 0; c < 10; c++) {
                if (f == fila && c == col) {
                    celdas[f][c].setBorder(new LineBorder(COLOR_SELECCION, 2));
                } else {
                    celdas[f][c].setBorder(new LineBorder(new Color(40, 44, 52), 1));
                }
            }
        }

        lblDetalleCoord.setText("Coordenadas: Fila " + fila + ", Columna " + col);

        UnidadTransporte unidad = servicio.getUnidadEn(fila, col);
        if (unidad != null) {
            lblDetalleZona.setText("Zona: Prioridad " + unidad.getPrioridadCochera());
            lblDetalleEstado.setText("Estado: OCUPADO");
            lblDetalleEstado.setForeground(new Color(40, 167, 69));
            lblDetalleTractor.setText("Tractor: " + unidad.getPlaca());
            lblDetalleCarreta.setText("Carreta: " + (unidad.getCarreta() != null ? unidad.getCarreta().getPlaca() : "Ninguna"));

            Viaje proximoViaje = buscarProximoViaje(unidad);
            if (proximoViaje != null) {
                int turno = obtenerTurnoEnCola(proximoViaje);
                lblDetalleViaje.setText("Próximo Viaje: #" + proximoViaje.getId() + " - " + proximoViaje.getCliente().getNombre());
                lblDetalleProximidad.setText("Proximidad: Turno #" + (turno + 1) + " (Prioridad " + unidad.getPrioridadCochera() + ")");
            } else {
                lblDetalleViaje.setText("Próximo Viaje: Sin viaje asignado");
                lblDetalleProximidad.setText("Proximidad: Sin urgencia (P3)");
            }

            btnDespacharViaje.setEnabled(true);
            btnLiberarEspacio.setEnabled(true);
        } else {
            int prioridadZona = servicio.getPrioridadDeEspacio(fila, col);
            if (prioridadZona > 0) {
                lblDetalleZona.setText("Zona: Prioridad " + prioridadZona);
                lblDetalleEstado.setText("Estado: DISPONIBLE");
                lblDetalleEstado.setForeground(new Color(13, 110, 253));
                lblDetalleTractor.setText("Tractor: -");
                lblDetalleCarreta.setText("Carreta: -");
                lblDetalleViaje.setText("Próximo Viaje: -");
                lblDetalleProximidad.setText("Proximidad: Espacio libre para P" + prioridadZona);
            } else {
                lblDetalleZona.setText("Zona: Vía de circulación");
                lblDetalleEstado.setText("Estado: TRÁNSITO LIBRE");
                lblDetalleEstado.setForeground(Color.DARK_GRAY);
                lblDetalleTractor.setText("Tractor: -");
                lblDetalleCarreta.setText("Carreta: -");
                lblDetalleViaje.setText("Próximo Viaje: -");
                lblDetalleProximidad.setText("Proximidad: -");
            }
            btnDespacharViaje.setEnabled(false);
            btnLiberarEspacio.setEnabled(false);
        }
    }

    private Viaje buscarProximoViaje(UnidadTransporte unidad) {
        if (unidad == null || unidad.getTractor() == null) return null;
        String placa = unidad.getPlaca();
        for (Viaje v : GestorViajes.getViajes()) {
            if (v.getTractor() != null && v.getTractor().getPlaca().equalsIgnoreCase(placa)) {
                return v;
            }
        }
        return null;
    }

    private int obtenerTurnoEnCola(Viaje viaje) {
        return GestorViajes.getViajes().indexOf(viaje);
    }

    private void actualizarComboUnidadesFuera() {
        cboUnidadesFuera.removeAllItems();
        List<UnidadTransporte> todas = GestorEquipos.getUnidades();
        int conteoFuera = 0;

        for (UnidadTransporte u : todas) {
            if (!u.estaEnCochera()) {
                int prioridadSugerida = servicio.calcularPrioridadPorProximidad(u);
                cboUnidadesFuera.addItem(u.getPlaca() + " (Prioridad estimada: P" + prioridadSugerida + ")");
                conteoFuera++;
            }
        }

        btnRegistrarLlegada.setEnabled(conteoFuera > 0 && !servicio.estaLlena());
    }

    /**
     * Acción: Despacha la unidad seleccionada para iniciar su viaje programado,
     * liberando el espacio que ocupaba en cochera.
     */
    private void despacharUnidadSeleccionada() {
        if (celdaSeleccionadaFila == -1 || celdaSeleccionadaCol == -1) return;

        UnidadTransporte unidad = servicio.getUnidadEn(celdaSeleccionadaFila, celdaSeleccionadaCol);
        if (unidad == null) return;

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea despachar la unidad " + unidad.getPlaca() + " a su viaje?\nEl espacio en cochera quedará liberado.",
                "Confirmar Despacho a Viaje",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            String placa = unidad.getPlaca();
            boolean exito = servicio.registrarSalida(unidad);
            if (exito) {
                JOptionPane.showMessageDialog(this,
                        "Unidad " + placa + " despachada exitosamente.\nEspacio liberado en cochera.",
                        "Despacho Exitoso",
                        JOptionPane.INFORMATION_MESSAGE);
                refrescar();
            }
        }
    }

    /**
     * Acción: Libera manualmente el espacio seleccionado.
     */
    private void liberarEspacioSeleccionado() {
        if (celdaSeleccionadaFila == -1 || celdaSeleccionadaCol == -1) return;

        UnidadTransporte unidad = servicio.getUnidadEn(celdaSeleccionadaFila, celdaSeleccionadaCol);
        if (unidad == null) return;

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de desalojar la unidad " + unidad.getPlaca() + " de la cochera?",
                "Confirmar Liberación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            servicio.liberarEspacio(unidad);
            JOptionPane.showMessageDialog(this,
                    "Espacio liberado correctamente.",
                    "Operación Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            refrescar();
        }
    }

    /**
     * Acción: Registra la llegada de una unidad a cochera, determinando su prioridad
     * según la proximidad del próximo viaje en cola y asignándole un espacio.
     */
    private void registrarLlegadaDeUnidad() {
        int indiceSeleccionado = cboUnidadesFuera.getSelectedIndex();
        if (indiceSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una unidad fuera de cochera.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String item = (String) cboUnidadesFuera.getSelectedItem();
        if (item == null) return;
        String placa = item.split(" ")[0].trim();

        UnidadTransporte unidad = GestorEquipos.buscarUnidadPorTractor(placa);
        if (unidad == null) {
            JOptionPane.showMessageDialog(this, "No se encontró la unidad seleccionada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean asignado = servicio.registrarLlegada(unidad);
            if (asignado) {
                JOptionPane.showMessageDialog(this,
                        "Llegada registrada exitosamente.\nUnidad: " + unidad.getPlaca()
                                + "\nPrioridad calculada según proximidad de viaje: P" + unidad.getPrioridadCochera()
                                + "\nEspacio asignado en patio.",
                        "Llegada a Cochera",
                        JOptionPane.INFORMATION_MESSAGE);
                refrescar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Asignación", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Acción: Reorganiza las unidades actualmente en cochera en función del
     * estado actualizado de la cola de viajes en GestorViajes.
     */
    private void reorganizarPorProximidad() {
        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "Se recalcularán las prioridades de todas las unidades estacionadas\n"
                        + "según el orden actual de viajes en cola y se reubicarán en los mejores espacios.\n¿Desea continuar?",
                "Reorganizar por Proximidad de Viajes",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            servicio.reorganizarSegunProximidadViajes();
            JOptionPane.showMessageDialog(this,
                    "Cochera reorganizada según la proximidad actual de viajes.",
                    "Reorganización Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
            refrescar();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelCochera = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));

        panelCochera.setMaximumSize(new java.awt.Dimension(600, 600));
        panelCochera.setPreferredSize(new java.awt.Dimension(600, 600));

        javax.swing.GroupLayout panelCocheraLayout = new javax.swing.GroupLayout(panelCochera);
        panelCochera.setLayout(panelCocheraLayout);
        panelCocheraLayout.setHorizontalGroup(
            panelCocheraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
        panelCocheraLayout.setVerticalGroup(
            panelCocheraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(panelCochera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(239, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(panelCochera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panelCochera;
    // End of variables declaration//GEN-END:variables
}
