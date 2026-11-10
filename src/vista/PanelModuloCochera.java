/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vista;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
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
 * 
 * Totalmente compatible y editable con el diseñador visual de NetBeans GUI Builder.
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

    /**
     * Creates new form PanelModuloCochera
     */
    public PanelModuloCochera() {
        initComponents();
        inicializarDatosDemoSiEsNecesario();
        construirCuadricula();
        refrescar();
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
     * Construye la cuadrícula de 10x10 JLabels dentro del panelCochera
     * configurado por el diseñador visual de NetBeans.
     */
    private void construirCuadricula() {
        panelCochera.removeAll();

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
                if (celda == null) continue;
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
            if (celdas[celdaSeleccionadaFila][celdaSeleccionadaCol] != null) {
                celdas[celdaSeleccionadaFila][celdaSeleccionadaCol].setBorder(
                        new LineBorder(COLOR_SELECCION, 2)
                );
            }
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
            if (l == null) continue;
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
     * Muestra en el panel de detalle los datos completos de la celda pulsada.
     */
    private void mostrarDatos(int fila, int col) {
        this.celdaSeleccionadaFila = fila;
        this.celdaSeleccionadaCol = col;

        for (int f = 0; f < 10; f++) {
            for (int c = 0; c < 10; c++) {
                if (celdas[f][c] == null) continue;
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
                lblDetalleViaje.setText("Próximo: #" + proximoViaje.getId() + " - " + proximoViaje.getCliente().getNombre());
                lblDetalleProximidad.setText("Proximidad: Turno #" + (turno + 1) + " (P" + unidad.getPrioridadCochera() + ")");
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
                lblDetalleProximidad.setText("Proximidad: Espacio libre P" + prioridadZona);
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
                cboUnidadesFuera.addItem(u.getPlaca() + " (Prioridad P" + prioridadSugerida + ")");
                conteoFuera++;
            }
        }

        btnRegistrarLlegada.setEnabled(conteoFuera > 0 && !servicio.estaLlena());
    }

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

        lblTitulo = new javax.swing.JLabel();
        lblCapacidad = new javax.swing.JLabel();
        panelCochera = new javax.swing.JPanel();
        pnlDetalle = new javax.swing.JPanel();
        lblTituloDetalle = new javax.swing.JLabel();
        lblDetalleCoord = new javax.swing.JLabel();
        lblDetalleZona = new javax.swing.JLabel();
        lblDetalleEstado = new javax.swing.JLabel();
        lblDetalleTractor = new javax.swing.JLabel();
        lblDetalleCarreta = new javax.swing.JLabel();
        lblDetalleViaje = new javax.swing.JLabel();
        lblDetalleProximidad = new javax.swing.JLabel();
        btnDespacharViaje = new javax.swing.JButton();
        btnLiberarEspacio = new javax.swing.JButton();
        pnlLlegada = new javax.swing.JPanel();
        lblTituloLlegada = new javax.swing.JLabel();
        lblComboTexto = new javax.swing.JLabel();
        cboUnidadesFuera = new javax.swing.JComboBox<>();
        btnRegistrarLlegada = new javax.swing.JButton();
        pnlAcciones = new javax.swing.JPanel();
        lblTituloOptimizacion = new javax.swing.JLabel();
        btnReorganizarProximidad = new javax.swing.JButton();
        pnlLeyenda = new javax.swing.JPanel();
        lblTituloLeyenda = new javax.swing.JLabel();
        lblLeyendaOcupado = new javax.swing.JLabel();
        lblLeyendaP1 = new javax.swing.JLabel();
        lblLeyendaP2 = new javax.swing.JLabel();
        lblLeyendaP3 = new javax.swing.JLabel();
        lblLeyendaPasillo = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(880, 680));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(30, 41, 59));
        lblTitulo.setText("MÓDULO COCHERA - CONTROL DE PATIO");
        add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 15, 460, 30));

        lblCapacidad.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblCapacidad.setForeground(new java.awt.Color(71, 85, 105));
        lblCapacidad.setText("Ocupación: 0 / 7 espacios");
        add(lblCapacidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 18, 340, 25));

        panelCochera.setBackground(new java.awt.Color(20, 24, 27));
        panelCochera.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 65, 85), 2));
        panelCochera.setMaximumSize(new java.awt.Dimension(560, 560));
        panelCochera.setPreferredSize(new java.awt.Dimension(560, 560));
        panelCochera.setLayout(new java.awt.GridLayout(10, 10, 2, 2));
        add(panelCochera, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 55, 560, 560));

        pnlDetalle.setBackground(new java.awt.Color(248, 250, 252));
        pnlDetalle.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlDetalle.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTituloDetalle.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTituloDetalle.setForeground(new java.awt.Color(30, 41, 59));
        lblTituloDetalle.setText("INFORMACIÓN DEL ESPACIO");
        pnlDetalle.add(lblTituloDetalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 8, 235, 18));

        lblDetalleCoord.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDetalleCoord.setText("Coordenadas: Seleccione celda");
        pnlDetalle.add(lblDetalleCoord, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 28, 235, 16));

        lblDetalleZona.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDetalleZona.setText("Zona: -");
        pnlDetalle.add(lblDetalleZona, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 46, 235, 16));

        lblDetalleEstado.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDetalleEstado.setText("Estado: -");
        pnlDetalle.add(lblDetalleEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 64, 235, 16));

        lblDetalleTractor.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDetalleTractor.setText("Tractor: -");
        pnlDetalle.add(lblDetalleTractor, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 82, 235, 16));

        lblDetalleCarreta.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDetalleCarreta.setText("Carreta: -");
        pnlDetalle.add(lblDetalleCarreta, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 235, 16));

        lblDetalleViaje.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDetalleViaje.setText("Próximo Viaje: -");
        pnlDetalle.add(lblDetalleViaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 118, 235, 16));

        lblDetalleProximidad.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblDetalleProximidad.setText("Proximidad: -");
        pnlDetalle.add(lblDetalleProximidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 136, 235, 16));

        btnDespacharViaje.setBackground(new java.awt.Color(13, 110, 253));
        btnDespacharViaje.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnDespacharViaje.setForeground(new java.awt.Color(255, 255, 255));
        btnDespacharViaje.setText("Despachar");
        btnDespacharViaje.setEnabled(false);
        btnDespacharViaje.addActionListener(this::btnDespacharViajeActionPerformed);
        pnlDetalle.add(btnDespacharViaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 165, 110, 32));

        btnLiberarEspacio.setBackground(new java.awt.Color(220, 53, 69));
        btnLiberarEspacio.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnLiberarEspacio.setForeground(new java.awt.Color(255, 255, 255));
        btnLiberarEspacio.setText("Liberar");
        btnLiberarEspacio.setEnabled(false);
        btnLiberarEspacio.addActionListener(this::btnLiberarEspacioActionPerformed);
        pnlDetalle.add(btnLiberarEspacio, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 165, 110, 32));

        add(pnlDetalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(605, 55, 255, 215));

        pnlLlegada.setBackground(new java.awt.Color(248, 250, 252));
        pnlLlegada.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlLlegada.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTituloLlegada.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTituloLlegada.setForeground(new java.awt.Color(30, 41, 59));
        lblTituloLlegada.setText("LLEGADA DE UNIDAD (RETORNO)");
        pnlLlegada.add(lblTituloLlegada, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 8, 235, 18));

        lblComboTexto.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        lblComboTexto.setText("Unidad fuera de cochera:");
        pnlLlegada.add(lblComboTexto, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 28, 235, 16));

        cboUnidadesFuera.setFont(new java.awt.Font("Segoe UI", 0, 11)); // NOI18N
        pnlLlegada.add(cboUnidadesFuera, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 48, 235, 28));

        btnRegistrarLlegada.setBackground(new java.awt.Color(25, 135, 84));
        btnRegistrarLlegada.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnRegistrarLlegada.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrarLlegada.setText("Registrar Llegada a Cochera");
        btnRegistrarLlegada.addActionListener(this::btnRegistrarLlegadaActionPerformed);
        pnlLlegada.add(btnRegistrarLlegada, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 82, 235, 32));

        add(pnlLlegada, new org.netbeans.lib.awtextra.AbsoluteConstraints(605, 280, 255, 125));

        pnlAcciones.setBackground(new java.awt.Color(248, 250, 252));
        pnlAcciones.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlAcciones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTituloOptimizacion.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblTituloOptimizacion.setForeground(new java.awt.Color(30, 41, 59));
        lblTituloOptimizacion.setText("OPTIMIZACIÓN DE PATIO");
        pnlAcciones.add(lblTituloOptimizacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 6, 235, 18));

        btnReorganizarProximidad.setBackground(new java.awt.Color(102, 16, 242));
        btnReorganizarProximidad.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnReorganizarProximidad.setForeground(new java.awt.Color(255, 255, 255));
        btnReorganizarProximidad.setText("Reorganizar por Proximidad");
        btnReorganizarProximidad.addActionListener(this::btnReorganizarProximidadActionPerformed);
        pnlAcciones.add(btnReorganizarProximidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 26, 235, 30));

        add(pnlAcciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(605, 415, 255, 65));

        pnlLeyenda.setBackground(new java.awt.Color(255, 255, 255));
        pnlLeyenda.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlLeyenda.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTituloLeyenda.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        lblTituloLeyenda.setForeground(new java.awt.Color(51, 65, 85));
        lblTituloLeyenda.setText("LEYENDA DE ZONAS Y PRIORIDAD");
        pnlLeyenda.add(lblTituloLeyenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 4, 235, 18));

        lblLeyendaOcupado.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblLeyendaOcupado.setForeground(new java.awt.Color(40, 167, 69));
        lblLeyendaOcupado.setText("■ Ocupado: Unidad en Cochera");
        pnlLeyenda.add(lblLeyendaOcupado, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 24, 235, 16));

        lblLeyendaP1.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblLeyendaP1.setForeground(new java.awt.Color(255, 193, 7));
        lblLeyendaP1.setText("■ P1: Salida Inmediata (Próximo)");
        pnlLeyenda.add(lblLeyendaP1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 42, 235, 16));

        lblLeyendaP2.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblLeyendaP2.setForeground(new java.awt.Color(23, 162, 184));
        lblLeyendaP2.setText("■ P2: Salida Media (Intermedio)");
        pnlLeyenda.add(lblLeyendaP2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 235, 16));

        lblLeyendaP3.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblLeyendaP3.setForeground(new java.awt.Color(108, 117, 125));
        lblLeyendaP3.setText("■ P3: Estancia Larga / Sin Viaje");
        pnlLeyenda.add(lblLeyendaP3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 78, 235, 16));

        lblLeyendaPasillo.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        lblLeyendaPasillo.setForeground(new java.awt.Color(33, 37, 41));
        lblLeyendaPasillo.setText("■ Vía de Maniobras y Circulación");
        pnlLeyenda.add(lblLeyendaPasillo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 96, 235, 16));

        add(pnlLeyenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(605, 490, 255, 125));
    }// </editor-fold>//GEN-END:initComponents

    private void btnDespacharViajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDespacharViajeActionPerformed
        despacharUnidadSeleccionada();
    }//GEN-LAST:event_btnDespacharViajeActionPerformed

    private void btnLiberarEspacioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLiberarEspacioActionPerformed
        liberarEspacioSeleccionado();
    }//GEN-LAST:event_btnLiberarEspacioActionPerformed

    private void btnRegistrarLlegadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarLlegadaActionPerformed
        registrarLlegadaDeUnidad();
    }//GEN-LAST:event_btnRegistrarLlegadaActionPerformed

    private void btnReorganizarProximidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReorganizarProximidadActionPerformed
        reorganizarPorProximidad();
    }//GEN-LAST:event_btnReorganizarProximidadActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDespacharViaje;
    private javax.swing.JButton btnLiberarEspacio;
    private javax.swing.JButton btnRegistrarLlegada;
    private javax.swing.JButton btnReorganizarProximidad;
    private javax.swing.JComboBox<String> cboUnidadesFuera;
    private javax.swing.JLabel lblCapacidad;
    private javax.swing.JLabel lblComboTexto;
    private javax.swing.JLabel lblDetalleCarreta;
    private javax.swing.JLabel lblDetalleCoord;
    private javax.swing.JLabel lblDetalleEstado;
    private javax.swing.JLabel lblDetalleProximidad;
    private javax.swing.JLabel lblDetalleTractor;
    private javax.swing.JLabel lblDetalleViaje;
    private javax.swing.JLabel lblDetalleZona;
    private javax.swing.JLabel lblLeyendaOcupado;
    private javax.swing.JLabel lblLeyendaP1;
    private javax.swing.JLabel lblLeyendaP2;
    private javax.swing.JLabel lblLeyendaP3;
    private javax.swing.JLabel lblLeyendaPasillo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTituloDetalle;
    private javax.swing.JLabel lblTituloLeyenda;
    private javax.swing.JLabel lblTituloLlegada;
    private javax.swing.JLabel lblTituloOptimizacion;
    private javax.swing.JPanel panelCochera;
    private javax.swing.JPanel pnlAcciones;
    private javax.swing.JPanel pnlDetalle;
    private javax.swing.JPanel pnlLeyenda;
    private javax.swing.JPanel pnlLlegada;
    // End of variables declaration//GEN-END:variables
}
