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
 * Panel para el Módulo de Cochera.
 * Representa la cuadrícula de estacionamiento (10x10) donde cada espacio
 * está compuesto estrictamente por 3 coordenadas físicas.
 * Asigna y reorganiza las unidades según la proximidad de los viajes en cola
 * y las prioridades asignadas (1, 2 o 3).
 */
public class PanelModuloCochera extends javax.swing.JPanel {

    private static final Color COLOR_PASILLO = new Color(30, 30, 30);
    private static final Color COLOR_LIBRE = new Color(160, 160, 160);
    private static final Color COLOR_OCUPADO = new Color(60, 150, 80);
    private static final Color COLOR_SELECCION = new Color(255, 153, 0);

    private final CocheraServicio servicio = new CocheraServicio();
    private final JLabel[][] celdas = new JLabel[10][10];

    private int celdaSeleccionadaFila = -1;
    private int celdaSeleccionadaCol = -1;

    /**
     * Creates new form PanelModuloCochera
     */
    public PanelModuloCochera() {
        initComponents();
        construirCuadricula();
        refrescar();
    }

    /**
     * Construye la cuadrícula de 10x10 dentro del contenedor panelCochera.
     */
    private void construirCuadricula() {
        panelCochera.removeAll();

        for (int f = 0; f < 10; f++) {
            for (int c = 0; c < 10; c++) {
                JLabel celda = new JLabel("", SwingConstants.CENTER);
                celda.setOpaque(true);
                celda.setBackground(COLOR_PASILLO);
                celda.setForeground(Color.WHITE);
                celda.setFont(new Font("Sans Serif", Font.BOLD, 10));
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
     * Actualiza la visualización de la cuadrícula, unidades estacionadas
     * y el combo de unidades disponibles para ingresar a cochera.
     */
    public void refrescar() {
        // 1. Restaurar pasillos y pintar espacios disponibles por prioridad
        for (int f = 0; f < 10; f++) {
            for (int c = 0; c < 10; c++) {
                JLabel celda = celdas[f][c];
                if (celda == null) continue;
                celda.setBorder(new LineBorder(new Color(50, 50, 50), 1));
                celda.setForeground(Color.WHITE);

                int prioridad = servicio.getPrioridadDeEspacio(f, c);
                if (prioridad == 0) {
                    celda.setBackground(COLOR_PASILLO);
                    celda.setText("");
                    celda.setToolTipText("Pasillo de maniobras [" + f + ", " + c + "]");
                } else {
                    celda.setBackground(COLOR_LIBRE);
                    celda.setText("P" + prioridad);
                    celda.setToolTipText("Espacio disponible - Prioridad " + prioridad + " [" + f + ", " + c + "]");
                }
            }
        }

        // 2. Pintar las unidades actualmente estacionadas
        for (UnidadTransporte unidad : servicio.getUnidades()) {
            pintarUnidad(unidad);
        }

        // 3. Restaurar selección si existe
        if (celdaSeleccionadaFila != -1 && celdaSeleccionadaCol != -1) {
            mostrarDatos(celdaSeleccionadaFila, celdaSeleccionadaCol);
        } else {
            lblCoord.setText("Coords: Seleccione celda");
            lblPrioridad.setText("Prioridad: -");
            lblEstado.setText("Estado: -");
            lblTractor.setText("Tractor: -");
            lblCarreta.setText("Carreta: -");
            lblViaje.setText("Próximo Viaje: -");
            btnDespachar.setEnabled(false);
            btnLiberar.setEnabled(false);
        }

        // 4. Actualizar combo de unidades que están fuera de cochera
        actualizarComboUnidades();

        panelCochera.revalidate();
        panelCochera.repaint();
    }

    /**
     * Dibuja una unidad ocupando sus 3 coordenadas en la cuadrícula.
     */
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
                String carreta = (unidad.getCarreta() != null) ? unidad.getCarreta().getPlaca() : "-";
                l.setText(carreta);
            }

            l.setToolTipText("Unidad " + unidad.getPlaca() + " | Prioridad "
                    + unidad.getPrioridadCochera() + " [" + f + ", " + c + "]");
        }
    }

    /**
     * Localiza el bloque de 3 coordenadas al que pertenece la celda (fila, col).
     */
    public int[][] buscarEspacio(int fila, int col) {
        for (int p = 1; p <= 3; p++) {
            for (int[][] esp : servicio.getEspacios(p)) {
                for (int[] celda : esp) {
                    if (celda[0] == fila && celda[1] == col) {
                        return esp;
                    }
                }
            }
        }
        return null;
    }

    private String formatoCoordenadas(int[][] espacio) {
        if (espacio == null || espacio.length == 0) return "Coords: -";
        StringBuilder sb = new StringBuilder("Coords: ");
        for (int i = 0; i < espacio.length; i++) {
            sb.append("(").append(espacio[i][0]).append(",").append(espacio[i][1]).append(")");
            if (i < espacio.length - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    /**
     * Muestra la información del espacio (de 3 coordenadas) seleccionado.
     */
    private void mostrarDatos(int fila, int col) {
        this.celdaSeleccionadaFila = fila;
        this.celdaSeleccionadaCol = col;

        int[][] espacio = buscarEspacio(fila, col);

        // Resaltar las celdas del espacio seleccionado
        for (int f = 0; f < 10; f++) {
            for (int c = 0; c < 10; c++) {
                if (celdas[f][c] == null) continue;
                boolean seleccionado = false;
                if (espacio != null) {
                    for (int[] celda : espacio) {
                        if (celda[0] == f && celda[1] == c) {
                            seleccionado = true;
                            break;
                        }
                    }
                } else if (f == fila && c == col) {
                    seleccionado = true;
                }

                if (seleccionado) {
                    celdas[f][c].setBorder(new LineBorder(COLOR_SELECCION, 2));
                } else {
                    celdas[f][c].setBorder(new LineBorder(new Color(50, 50, 50), 1));
                }
            }
        }

        if (espacio == null) {
            lblCoord.setText("Coords: (" + fila + "," + col + ") [Pasillo]");
            lblPrioridad.setText("Prioridad: Pasillo");
            lblEstado.setText("Estado: Maniobra libre");
            lblEstado.setForeground(Color.BLACK);
            lblTractor.setText("Tractor: -");
            lblCarreta.setText("Carreta: -");
            lblViaje.setText("Próximo Viaje: -");
            btnDespachar.setEnabled(false);
            btnLiberar.setEnabled(false);
            return;
        }

        lblCoord.setText(formatoCoordenadas(espacio));

        UnidadTransporte unidad = servicio.getUnidadEn(fila, col);
        if (unidad != null) {
            lblPrioridad.setText("Prioridad: " + unidad.getPrioridadCochera());
            lblEstado.setText("Estado: OCUPADO");
            lblEstado.setForeground(new Color(40, 140, 60));
            lblTractor.setText("Tractor: " + unidad.getPlaca());
            lblCarreta.setText("Carreta: " + (unidad.getCarreta() != null ? unidad.getCarreta().getPlaca() : "(Sin Carreta)"));

            Viaje viaje = buscarProximoViaje(unidad);
            if (viaje != null) {
                lblViaje.setText("Próximo Viaje: #" + viaje.getId() + " - " + viaje.getCliente().getNombre());
            } else {
                lblViaje.setText("Próximo Viaje: Sin viaje asignado");
            }

            btnDespachar.setEnabled(true);
            btnLiberar.setEnabled(true);
        } else {
            int prioridadEspacio = servicio.getPrioridadDeEspacio(fila, col);
            lblPrioridad.setText("Prioridad: " + prioridadEspacio);
            lblEstado.setText("Estado: DISPONIBLE");
            lblEstado.setForeground(new Color(0, 102, 204));
            lblTractor.setText("Tractor: -");
            lblCarreta.setText("Carreta: -");
            lblViaje.setText("Próximo Viaje: -");
            btnDespachar.setEnabled(false);
            btnLiberar.setEnabled(false);
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

    public void actualizarComboUnidades() {
        cboUnidadLlegada.removeAllItems();
        List<UnidadTransporte> unidadesRegistradas = GestorEquipos.getUnidades();
        int fuera = 0;

        for (UnidadTransporte u : unidadesRegistradas) {
            if (!u.estaEnCochera()) {
                int p = servicio.calcularPrioridadPorProximidad(u);
                cboUnidadLlegada.addItem(u.getPlaca() + " (Prioridad " + p + ")");
                fuera++;
            }
        }

        btnRegistrarLlegada.setEnabled(fuera > 0 && !servicio.estaLlena());
    }

    public CocheraServicio getServicio() {
        return servicio;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        panelCochera = new javax.swing.JPanel();
        pnlContenido = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        jLabelSubDetalle = new javax.swing.JLabel();
        lblCoord = new javax.swing.JLabel();
        lblPrioridad = new javax.swing.JLabel();
        lblEstado = new javax.swing.JLabel();
        lblTractor = new javax.swing.JLabel();
        lblCarreta = new javax.swing.JLabel();
        lblViaje = new javax.swing.JLabel();
        btnDespachar = new javax.swing.JButton();
        btnLiberar = new javax.swing.JButton();
        jLabelSubLlegada = new javax.swing.JLabel();
        jLabelSelUnidad = new javax.swing.JLabel();
        cboUnidadLlegada = new javax.swing.JComboBox<>();
        btnRegistrarLlegada = new javax.swing.JButton();
        btnReorganizar = new javax.swing.JButton();
        jLabelLeyenda = new javax.swing.JLabel();
        lblLeyenda1 = new javax.swing.JLabel();
        lblLeyenda2 = new javax.swing.JLabel();
        lblLeyenda3 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(880, 680));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Javanese Text", 1, 36)); // NOI18N
        jLabel1.setText("MODULO COCHERA");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 15, 450, 55));

        panelCochera.setBackground(new java.awt.Color(30, 30, 30));
        panelCochera.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panelCochera.setMaximumSize(new java.awt.Dimension(540, 540));
        panelCochera.setPreferredSize(new java.awt.Dimension(540, 540));
        panelCochera.setLayout(new java.awt.GridLayout(10, 10, 2, 2));
        add(panelCochera, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 80, 540, 540));

        pnlContenido.setBackground(new java.awt.Color(204, 204, 204));
        pnlContenido.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlContenido.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabelTitulo.setFont(new java.awt.Font("Sans Serif Collection", 1, 13)); // NOI18N
        jLabelTitulo.setForeground(new java.awt.Color(51, 51, 51));
        jLabelTitulo.setText("CONTROL DE COCHERA");
        pnlContenido.add(jLabelTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 10, 215, 25));

        jLabelSubDetalle.setFont(new java.awt.Font("Sans Serif Collection", 1, 11)); // NOI18N
        jLabelSubDetalle.setForeground(new java.awt.Color(51, 51, 51));
        jLabelSubDetalle.setText("ESPACIO (3 COORDENADAS):");
        pnlContenido.add(jLabelSubDetalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 38, 215, 18));

        lblCoord.setFont(new java.awt.Font("Sans Serif Collection", 0, 10)); // NOI18N
        lblCoord.setForeground(new java.awt.Color(102, 102, 102));
        lblCoord.setText("Coords: Seleccione celda");
        pnlContenido.add(lblCoord, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 58, 215, 16));

        lblPrioridad.setFont(new java.awt.Font("Sans Serif Collection", 0, 10)); // NOI18N
        lblPrioridad.setForeground(new java.awt.Color(102, 102, 102));
        lblPrioridad.setText("Prioridad: -");
        pnlContenido.add(lblPrioridad, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 74, 215, 16));

        lblEstado.setFont(new java.awt.Font("Sans Serif Collection", 0, 10)); // NOI18N
        lblEstado.setForeground(new java.awt.Color(102, 102, 102));
        lblEstado.setText("Estado: -");
        pnlContenido.add(lblEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 90, 215, 16));

        lblTractor.setFont(new java.awt.Font("Sans Serif Collection", 0, 10)); // NOI18N
        lblTractor.setForeground(new java.awt.Color(102, 102, 102));
        lblTractor.setText("Tractor: -");
        pnlContenido.add(lblTractor, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 106, 215, 16));

        lblCarreta.setFont(new java.awt.Font("Sans Serif Collection", 0, 10)); // NOI18N
        lblCarreta.setForeground(new java.awt.Color(102, 102, 102));
        lblCarreta.setText("Carreta: -");
        pnlContenido.add(lblCarreta, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 122, 215, 16));

        lblViaje.setFont(new java.awt.Font("Sans Serif Collection", 0, 10)); // NOI18N
        lblViaje.setForeground(new java.awt.Color(102, 102, 102));
        lblViaje.setText("Próximo Viaje: -");
        pnlContenido.add(lblViaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 138, 215, 16));

        btnDespachar.setBackground(new java.awt.Color(0, 153, 255));
        btnDespachar.setFont(new java.awt.Font("Sans Serif Collection", 1, 11)); // NOI18N
        btnDespachar.setForeground(new java.awt.Color(255, 255, 255));
        btnDespachar.setText("DESPACHAR A VIAJE");
        btnDespachar.setEnabled(false);
        btnDespachar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDespacharActionPerformed(evt);
            }
        });
        pnlContenido.add(btnDespachar, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 160, 215, 30));

        btnLiberar.setBackground(new java.awt.Color(255, 51, 51));
        btnLiberar.setFont(new java.awt.Font("Sans Serif Collection", 1, 11)); // NOI18N
        btnLiberar.setForeground(new java.awt.Color(255, 255, 255));
        btnLiberar.setText("LIBERAR ESPACIO");
        btnLiberar.setEnabled(false);
        btnLiberar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLiberarActionPerformed(evt);
            }
        });
        pnlContenido.add(btnLiberar, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 196, 215, 28));

        jLabelSubLlegada.setFont(new java.awt.Font("Sans Serif Collection", 1, 11)); // NOI18N
        jLabelSubLlegada.setForeground(new java.awt.Color(51, 51, 51));
        jLabelSubLlegada.setText("LLEGADA DE UNIDAD:");
        pnlContenido.add(jLabelSubLlegada, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 235, 215, 18));

        jLabelSelUnidad.setFont(new java.awt.Font("Sans Serif Collection", 0, 10)); // NOI18N
        jLabelSelUnidad.setForeground(new java.awt.Color(51, 51, 51));
        jLabelSelUnidad.setText("Unidad fuera de cochera:");
        pnlContenido.add(jLabelSelUnidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 253, 215, 16));

        cboUnidadLlegada.setBackground(new java.awt.Color(255, 255, 255));
        pnlContenido.add(cboUnidadLlegada, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 271, 215, 28));

        btnRegistrarLlegada.setBackground(new java.awt.Color(0, 153, 255));
        btnRegistrarLlegada.setFont(new java.awt.Font("Sans Serif Collection", 1, 11)); // NOI18N
        btnRegistrarLlegada.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrarLlegada.setText("REGISTRAR LLEGADA");
        btnRegistrarLlegada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarLlegadaActionPerformed(evt);
            }
        });
        pnlContenido.add(btnRegistrarLlegada, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 306, 215, 32));

        btnReorganizar.setBackground(new java.awt.Color(0, 153, 255));
        btnReorganizar.setFont(new java.awt.Font("Sans Serif Collection", 1, 11)); // NOI18N
        btnReorganizar.setForeground(new java.awt.Color(255, 255, 255));
        btnReorganizar.setText("ORDENAR POR PRIORIDAD");
        btnReorganizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReorganizarActionPerformed(evt);
            }
        });
        pnlContenido.add(btnReorganizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 350, 215, 32));

        jLabelLeyenda.setFont(new java.awt.Font("Sans Serif Collection", 1, 11)); // NOI18N
        jLabelLeyenda.setForeground(new java.awt.Color(51, 51, 51));
        jLabelLeyenda.setText("LEYENDA:");
        pnlContenido.add(jLabelLeyenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 395, 215, 18));

        lblLeyenda1.setFont(new java.awt.Font("Sans Serif Collection", 0, 10)); // NOI18N
        lblLeyenda1.setForeground(new java.awt.Color(60, 150, 80));
        lblLeyenda1.setText("Verde: Unidad en cochera");
        pnlContenido.add(lblLeyenda1, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 415, 215, 16));

        lblLeyenda2.setFont(new java.awt.Font("Sans Serif Collection", 0, 10)); // NOI18N
        lblLeyenda2.setForeground(new java.awt.Color(102, 102, 102));
        lblLeyenda2.setText("Gris: Espacio disponible (1, 2, 3)");
        pnlContenido.add(lblLeyenda2, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 433, 215, 16));

        lblLeyenda3.setFont(new java.awt.Font("Sans Serif Collection", 0, 10)); // NOI18N
        lblLeyenda3.setForeground(new java.awt.Color(30, 30, 30));
        lblLeyenda3.setText("Oscuro: Pasillo de maniobra");
        pnlContenido.add(lblLeyenda3, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 451, 215, 16));

        add(pnlContenido, new org.netbeans.lib.awtextra.AbsoluteConstraints(605, 80, 245, 540));
    }// </editor-fold>//GEN-END:initComponents

    private void btnDespacharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDespacharActionPerformed
        if (celdaSeleccionadaFila == -1 || celdaSeleccionadaCol == -1) return;
        UnidadTransporte unidad = servicio.getUnidadEn(celdaSeleccionadaFila, celdaSeleccionadaCol);
        if (unidad == null) return;

        int r = JOptionPane.showConfirmDialog(
                this,
                "¿Desea despachar la unidad " + unidad.getPlaca() + " a su viaje?\nEl espacio en cochera quedará liberado.",
                "Despachar Unidad",
                JOptionPane.YES_NO_OPTION
        );

        if (r == JOptionPane.YES_OPTION) {
            boolean exito = servicio.registrarSalida(unidad);
            if (exito) {
                JOptionPane.showMessageDialog(this, "Unidad " + unidad.getPlaca() + " despachada a viaje. Espacio liberado.");
                refrescar();
            }
        }
    }//GEN-LAST:event_btnDespacharActionPerformed

    private void btnLiberarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLiberarActionPerformed
        if (celdaSeleccionadaFila == -1 || celdaSeleccionadaCol == -1) return;
        UnidadTransporte unidad = servicio.getUnidadEn(celdaSeleccionadaFila, celdaSeleccionadaCol);
        if (unidad == null) return;

        int r = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de desalojar la unidad " + unidad.getPlaca() + " de la cochera?",
                "Liberar Espacio",
                JOptionPane.YES_NO_OPTION
        );

        if (r == JOptionPane.YES_OPTION) {
            servicio.liberarEspacio(unidad);
            JOptionPane.showMessageDialog(this, "Espacio de cochera liberado correctamente.");
            refrescar();
        }
    }//GEN-LAST:event_btnLiberarActionPerformed

    private void btnRegistrarLlegadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarLlegadaActionPerformed
        int idx = cboUnidadLlegada.getSelectedIndex();
        if (idx == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una unidad fuera de cochera.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String item = (String) cboUnidadLlegada.getSelectedItem();
        if (item == null) return;
        String placa = item.split(" ")[0].trim();

        UnidadTransporte unidad = GestorEquipos.buscarUnidadPorTractor(placa);
        if (unidad == null) {
            JOptionPane.showMessageDialog(this, "No se encontró la unidad seleccionada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            boolean ok = servicio.registrarLlegada(unidad);
            if (ok) {
                JOptionPane.showMessageDialog(
                        this,
                        "Llegada registrada exitosamente.\nUnidad: " + unidad.getPlaca()
                                + "\nPrioridad asignada por proximidad: P" + unidad.getPrioridadCochera()
                                + "\nEspacio asignado (3 coordenadas).",
                        "Llegada Exitosa",
                        JOptionPane.INFORMATION_MESSAGE
                );
                refrescar();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error al Asignar", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRegistrarLlegadaActionPerformed

    private void btnReorganizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReorganizarActionPerformed
        int r = JOptionPane.showConfirmDialog(
                this,
                "Se recalcularán las prioridades de todas las unidades estacionadas\n"
                        + "según la proximidad de viajes en cola y se reubicarán.\n¿Desea continuar?",
                "Reorganizar Cochera",
                JOptionPane.YES_NO_OPTION
        );

        if (r == JOptionPane.YES_OPTION) {
            servicio.reorganizarSegunProximidadViajes();
            JOptionPane.showMessageDialog(this, "Cochera reorganizada según proximidad de viajes.");
            refrescar();
        }
    }//GEN-LAST:event_btnReorganizarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDespachar;
    private javax.swing.JButton btnLiberar;
    private javax.swing.JButton btnRegistrarLlegada;
    private javax.swing.JButton btnReorganizar;
    private javax.swing.JComboBox<String> cboUnidadLlegada;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelLeyenda;
    private javax.swing.JLabel jLabelSelUnidad;
    private javax.swing.JLabel jLabelSubDetalle;
    private javax.swing.JLabel jLabelSubLlegada;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel lblCarreta;
    private javax.swing.JLabel lblCoord;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblLeyenda1;
    private javax.swing.JLabel lblLeyenda2;
    private javax.swing.JLabel lblLeyenda3;
    private javax.swing.JLabel lblPrioridad;
    private javax.swing.JLabel lblTractor;
    private javax.swing.JLabel lblViaje;
    private javax.swing.JPanel panelCochera;
    private javax.swing.JPanel pnlContenido;
    // End of variables declaration//GEN-END:variables
}
