/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vista;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Cliente;
import modelo.Contenedor;
import modelo.Planta;
import modelo.Ruta;
import modelo.Terminal;
import modelo.UnidadTransporte;
import modelo.Viaje;
import servicio.GestorEquipos;
import servicio.GestorLogistica;
import servicio.GestorViajes;

/**
 * Panel para el registro, edición y gestión operativa de Viajes de Transporte.
 */
public class PanelModuloViajes extends javax.swing.JPanel {

    private int idViajeEditando = -1;

    /**
     * Creates new form PanelControlViajes
     */
    public PanelModuloViajes() {
        initComponents();

        tblViajes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cargarViajeSeleccionado();
            }
        });

        cargarDatos();
    }

    public void cargarDatos() {
        cargarCombos();
        cargarTablaViajes();
        actualizarContadorPendientes();
    }

    public void cargarCombos() {
        cboContenedor.removeAllItems();
        cboTractor.removeAllItems();
        cboPlanta.removeAllItems();
        cboTerminal.removeAllItems();
        cboCliente.removeAllItems();
        cboRuta.removeAllItems();

        for (Contenedor contenedor : GestorEquipos.getContenedores()) {
            cboContenedor.addItem(contenedor.getCodigo());
        }

        for (UnidadTransporte unidad : GestorEquipos.getUnidades()) {
            String carretaInfo = (unidad.getCarreta() != null ? unidad.getCarreta().getPlaca() : "Sin Carreta");
            cboTractor.addItem(unidad.getPlaca() + " - " + carretaInfo);
        }

        for (Planta planta : GestorLogistica.getPlantas()) {
            cboPlanta.addItem(planta.getNombre());
        }

        for (Terminal terminal : GestorLogistica.getTerminales()) {
            cboTerminal.addItem(terminal.getNombre());
        }

        for (Cliente cliente : GestorLogistica.getClientes()) {
            cboCliente.addItem(cliente.getNombre());
        }

        for (Ruta ruta : GestorLogistica.getRutas()) {
            cboRuta.addItem("Ruta " + ruta.getId());
        }
    }

    public void cargarTablaViajes() {
        DefaultTableModel modelo = (DefaultTableModel) tblViajes.getModel();
        modelo.setRowCount(0);

        for (Viaje viaje : GestorViajes.getViajes()) {
            String placaUnidad = viaje.getUnidad() != null ? viaje.getUnidad().getPlaca() : (viaje.getTractor() != null ? viaje.getTractor().getPlaca() : "-");
            String placaCarreta = viaje.getCarreta() != null ? viaje.getCarreta().getPlaca() : "(Sin Carreta)";
            String nomCliente = viaje.getCliente() != null ? viaje.getCliente().getNombre() : "-";
            String codContenedor = viaje.getContenedor() != null ? viaje.getContenedor().getCodigo() : "-";
            String rutaDesc = viaje.getRuta() != null ? "Ruta " + viaje.getRuta().getId() : "-";

            Object[] fila = {
                viaje.getId(),
                codContenedor,
                placaUnidad,
                placaCarreta,
                nomCliente,
                rutaDesc
            };

            modelo.addRow(fila);
        }
    }

    private void cargarViajeSeleccionado() {
        int fila = tblViajes.getSelectedRow();
        if (fila == -1) {
            return;
        }

        idViajeEditando = (int) tblViajes.getValueAt(fila, 0);

        Viaje viajeSeleccionado = null;
        for (Viaje viaje : GestorViajes.getViajes()) {
            if (viaje.getId() == idViajeEditando) {
                viajeSeleccionado = viaje;
                break;
            }
        }

        if (viajeSeleccionado == null) {
            return;
        }

        if (viajeSeleccionado.getContenedor() != null) {
            cboContenedor.setSelectedItem(viajeSeleccionado.getContenedor().getCodigo());
        }

        if (viajeSeleccionado.getUnidad() != null) {
            String placaTractor = viajeSeleccionado.getUnidad().getPlaca();
            for (int i = 0; i < GestorEquipos.getUnidades().size(); i++) {
                if (GestorEquipos.getUnidades().get(i).getPlaca().equalsIgnoreCase(placaTractor)) {
                    cboTractor.setSelectedIndex(i);
                    break;
                }
            }
        }

        if (viajeSeleccionado.getPlanta() != null) {
            cboPlanta.setSelectedItem(viajeSeleccionado.getPlanta().getNombre());
        }

        if (viajeSeleccionado.getTerminal() != null) {
            cboTerminal.setSelectedItem(viajeSeleccionado.getTerminal().getNombre());
        }

        if (viajeSeleccionado.getCliente() != null) {
            cboCliente.setSelectedItem(viajeSeleccionado.getCliente().getNombre());
        }

        if (viajeSeleccionado.getRuta() != null) {
            cboRuta.setSelectedItem("Ruta " + viajeSeleccionado.getRuta().getId());
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

        jLabel1 = new javax.swing.JLabel();
        pnlContenido = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cboTerminal = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        cboCliente = new javax.swing.JComboBox<>();
        cboRuta = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        btnRegistrarUrgencia = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cboContenedor = new javax.swing.JComboBox<>();
        cboTractor = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        cboPlanta = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblViajes = new javax.swing.JTable();
        btnEliminar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnRecuperar = new javax.swing.JButton();
        btnAtender = new javax.swing.JButton();
        btnPendientes = new javax.swing.JButton();
        lblContadorPendientes = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(880, 680));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("JetBrainsMono NF", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("MODULO VIAJES");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 25, 400, 50));

        pnlContenido.setBackground(new java.awt.Color(255, 255, 255));
        pnlContenido.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel2.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(51, 51, 51));
        jLabel2.setText("Contenedor:");

        cboTerminal.setBackground(new java.awt.Color(255, 255, 255));

        jLabel3.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setText("Unidad:");

        cboCliente.setBackground(new java.awt.Color(255, 255, 255));

        cboRuta.setBackground(new java.awt.Color(255, 255, 255));

        jLabel5.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setText("Terminal:");

        jButton1.setBackground(new java.awt.Color(0, 153, 255));
        jButton1.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("REGISTRAR VIAJE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnRegistrarUrgencia.setBackground(new java.awt.Color(0, 153, 255));
        btnRegistrarUrgencia.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        btnRegistrarUrgencia.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrarUrgencia.setText("REGISTRAR CON URGENCIA");
        btnRegistrarUrgencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarUrgenciaActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setText("Cliente:");

        jLabel7.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setText("Ruta:");

        jLabel8.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Planta:");

        cboContenedor.setBackground(new java.awt.Color(255, 255, 255));

        cboTractor.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("REGISTRAR / EDITAR VIAJE");

        cboPlanta.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlContenidoLayout = new javax.swing.GroupLayout(pnlContenido);
        pnlContenido.setLayout(pnlContenidoLayout);
        pnlContenidoLayout.setHorizontalGroup(
            pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContenidoLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(pnlContenidoLayout.createSequentialGroup()
                        .addGroup(pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGroup(pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlContenidoLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cboTractor, 0, 230, Short.MAX_VALUE)
                                    .addComponent(cboPlanta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cboContenedor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(40, 40, 40)
                                .addGroup(pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(18, 18, 18)
                                .addGroup(pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cboTerminal, 0, 230, Short.MAX_VALUE)
                                    .addComponent(cboCliente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cboRuta, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlContenidoLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(btnRegistrarUrgencia, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        pnlContenidoLayout.setVerticalGroup(
            pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContenidoLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addGroup(pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cboContenedor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(cboTerminal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cboTractor, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(cboCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cboPlanta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(cboRuta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegistrarUrgencia, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        add(pnlContenido, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 85, 800, 245));

        tblViajes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Contenedor", "Unidad", "Carreta", "Cliente", "Ruta"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblViajes);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 345, 670, 310));

        btnEliminar.setBackground(new java.awt.Color(255, 51, 51));
        btnEliminar.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        add(btnEliminar, new org.netbeans.lib.awtextra.AbsoluteConstraints(735, 370, 115, 34));

        btnActualizar.setBackground(new java.awt.Color(0, 153, 255));
        btnActualizar.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        btnActualizar.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizar.setText("ACTUALIZAR");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        add(btnActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(735, 420, 115, 34));

        btnEditar.setBackground(new java.awt.Color(255, 153, 102));
        btnEditar.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        btnEditar.setForeground(new java.awt.Color(255, 255, 255));
        btnEditar.setText("EDITAR");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(735, 470, 115, 34));

        btnRecuperar.setBackground(new java.awt.Color(153, 0, 153));
        btnRecuperar.setFont(new java.awt.Font("sansserif", 1, 11)); // NOI18N
        btnRecuperar.setForeground(new java.awt.Color(255, 255, 255));
        btnRecuperar.setText("RECUPERAR");
        btnRecuperar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRecuperarActionPerformed(evt);
            }
        });
        add(btnRecuperar, new org.netbeans.lib.awtextra.AbsoluteConstraints(735, 520, 115, 34));

        btnAtender.setBackground(new java.awt.Color(0, 153, 51));
        btnAtender.setFont(new java.awt.Font("sansserif", 1, 11)); // NOI18N
        btnAtender.setForeground(new java.awt.Color(255, 255, 255));
        btnAtender.setText("ATENDER");
        btnAtender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtenderActionPerformed(evt);
            }
        });
        add(btnAtender, new org.netbeans.lib.awtextra.AbsoluteConstraints(735, 570, 115, 34));

        btnPendientes.setBackground(new java.awt.Color(0, 102, 204));
        btnPendientes.setFont(new java.awt.Font("sansserif", 1, 11)); // NOI18N
        btnPendientes.setForeground(new java.awt.Color(255, 255, 255));
        btnPendientes.setText("PENDIENTES");
        btnPendientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPendientesActionPerformed(evt);
            }
        });
        add(btnPendientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(735, 620, 115, 34));

        lblContadorPendientes.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        lblContadorPendientes.setForeground(new java.awt.Color(0, 102, 255));
        lblContadorPendientes.setText("VIAJES PENDIENTES: 0");
        add(lblContadorPendientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 35, 300, 30));
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (cboContenedor.getSelectedIndex() == -1
                || cboTractor.getSelectedIndex() == -1
                || cboPlanta.getSelectedIndex() == -1
                || cboTerminal.getSelectedIndex() == -1
                || cboCliente.getSelectedIndex() == -1
                || cboRuta.getSelectedIndex() == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar todos los datos del viaje (asegúrese de haber unido al menos una Unidad en Módulo Data).",
                    "Campos requeridos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Contenedor contenedor
                = GestorEquipos.getContenedores()
                        .get(cboContenedor.getSelectedIndex());

        UnidadTransporte unidad
                = GestorEquipos.getUnidades()
                        .get(cboTractor.getSelectedIndex());

        Planta planta
                = GestorLogistica.getPlantas()
                        .get(cboPlanta.getSelectedIndex());

        Terminal terminal
                = GestorLogistica.getTerminales()
                        .get(cboTerminal.getSelectedIndex());

        Cliente cliente
                = GestorLogistica.getClientes()
                        .get(cboCliente.getSelectedIndex());

        Ruta ruta
                = GestorLogistica.getRutas()
                        .get(cboRuta.getSelectedIndex());

        int id = GestorViajes.generarId();

        Viaje nuevoViaje = new Viaje(
                id,
                contenedor,
                unidad,
                planta,
                terminal,
                cliente,
                ruta
        );

        GestorViajes.agregarViaje(nuevoViaje);
        cargarTablaViajes();
        actualizarContadorPendientes();

        JOptionPane.showMessageDialog(
                this,
                "Viaje registrado correctamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
        );
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnRegistrarUrgenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarUrgenciaActionPerformed
        if (cboContenedor.getSelectedIndex() == -1
                || cboTractor.getSelectedIndex() == -1
                || cboPlanta.getSelectedIndex() == -1
                || cboTerminal.getSelectedIndex() == -1
                || cboCliente.getSelectedIndex() == -1
                || cboRuta.getSelectedIndex() == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar todos los datos del viaje (asegúrese de haber unido al menos una Unidad en Módulo Data).",
                    "Campos requeridos",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Contenedor contenedor
                = GestorEquipos.getContenedores()
                        .get(cboContenedor.getSelectedIndex());

        UnidadTransporte unidad
                = GestorEquipos.getUnidades()
                        .get(cboTractor.getSelectedIndex());

        Planta planta
                = GestorLogistica.getPlantas()
                        .get(cboPlanta.getSelectedIndex());

        Terminal terminal
                = GestorLogistica.getTerminales()
                        .get(cboTerminal.getSelectedIndex());

        Cliente cliente
                = GestorLogistica.getClientes()
                        .get(cboCliente.getSelectedIndex());

        Ruta ruta
                = GestorLogistica.getRutas()
                        .get(cboRuta.getSelectedIndex());

        int id = GestorViajes.generarId();

        Viaje nuevoViaje = new Viaje(
                id,
                contenedor,
                unidad,
                planta,
                terminal,
                cliente,
                ruta
        );

        GestorViajes.agregarViajeUrgente(nuevoViaje);
        cargarTablaViajes();
        actualizarContadorPendientes();

        JOptionPane.showMessageDialog(
                this,
                "Viaje urgente registrado al inicio de la cola con prioridad.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
        );
    }//GEN-LAST:event_btnRegistrarUrgenciaActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        if (idViajeEditando == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Primero seleccione un viaje de la tabla y modifique los campos antes de presionar ACTUALIZAR.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (cboContenedor.getSelectedIndex() == -1
                || cboTractor.getSelectedIndex() == -1
                || cboPlanta.getSelectedIndex() == -1
                || cboTerminal.getSelectedIndex() == -1
                || cboCliente.getSelectedIndex() == -1
                || cboRuta.getSelectedIndex() == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar todos los datos del viaje.",
                    "Campos requeridos",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Viaje viajeEditar = null;
        for (Viaje viaje : GestorViajes.getViajes()) {
            if (viaje.getId() == idViajeEditando) {
                viajeEditar = viaje;
                break;
            }
        }

        if (viajeEditar == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró el viaje a actualizar.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Contenedor contenedor
                = GestorEquipos.getContenedores()
                        .get(cboContenedor.getSelectedIndex());

        UnidadTransporte unidad
                = GestorEquipos.getUnidades()
                        .get(cboTractor.getSelectedIndex());

        Planta planta
                = GestorLogistica.getPlantas()
                        .get(cboPlanta.getSelectedIndex());

        Terminal terminal
                = GestorLogistica.getTerminales()
                        .get(cboTerminal.getSelectedIndex());

        Cliente cliente
                = GestorLogistica.getClientes()
                        .get(cboCliente.getSelectedIndex());

        Ruta ruta
                = GestorLogistica.getRutas()
                        .get(cboRuta.getSelectedIndex());

        viajeEditar.setContenedor(contenedor);
        viajeEditar.setUnidad(unidad);
        viajeEditar.setPlanta(planta);
        viajeEditar.setTerminal(terminal);
        viajeEditar.setCliente(cliente);
        viajeEditar.setRuta(ruta);

        cargarTablaViajes();

        idViajeEditando = -1;

        JOptionPane.showMessageDialog(
                this,
                "Viaje actualizado correctamente.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
        );
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        int fila = tblViajes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un viaje de la tabla para editar.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        cargarViajeSeleccionado();

        JOptionPane.showMessageDialog(
                this,
                "Datos cargados en el formulario. Modifique los campos y presione ACTUALIZAR.",
                "Editar Viaje",
                JOptionPane.INFORMATION_MESSAGE
        );
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int fila = tblViajes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un viaje de la tabla para eliminar.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int id = (int) tblViajes.getValueAt(fila, 0);

        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar el viaje con ID " + id + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        Viaje viajeEliminar = null;
        for (Viaje viaje : GestorViajes.getViajes()) {
            if (viaje.getId() == id) {
                viajeEliminar = viaje;
                break;
            }
        }

        if (viajeEliminar == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se encontró el viaje.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        GestorViajes.eliminarViaje(viajeEliminar);

        cargarTablaViajes();
        actualizarContadorPendientes();

        idViajeEditando = -1;

        JOptionPane.showMessageDialog(
                this,
                "Viaje eliminado correctamente y guardado en la pila de recuperación.",
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE
        );
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnRecuperarActionPerformed(java.awt.event.ActionEvent evt) {
        Viaje viaje = GestorViajes.recuperarViaje();
        actualizarContadorPendientes();

        if (viaje != null) {
            cargarTablaViajes();
            JOptionPane.showMessageDialog(
                    this,
                    "Se recuperó de la pila el viaje ID: " + viaje.getId(),
                    "Recuperación Exitosa",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay viajes eliminados para recuperar en la pila.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void btnAtenderActionPerformed(java.awt.event.ActionEvent evt) {
        Viaje viaje = GestorViajes.atenderSiguienteViaje();
        actualizarContadorPendientes();

        if (viaje != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Se atendió y despachó de la cola el viaje ID: " + viaje.getId()
                    + "\nContenedor: " + (viaje.getContenedor() != null ? viaje.getContenedor().getCodigo() : "-")
                    + "\nUnidad: " + (viaje.getUnidad() != null ? viaje.getUnidad().getPlaca() : "-"),
                    "Viaje Atendido (Cola FIFO)",
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No hay viajes pendientes en la cola de atención.",
                    "Cola Vacía",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    private void btnPendientesActionPerformed(java.awt.event.ActionEvent evt) {
        actualizarContadorPendientes();
        StringBuilder mensaje = new StringBuilder();

        for (Viaje viaje : GestorViajes.getColaPendientes()) {
            mensaje.append("Viaje ID: ").append(viaje.getId())
                    .append(" | Contenedor: ").append(viaje.getContenedor() != null ? viaje.getContenedor().getCodigo() : "-")
                    .append(" | Unidad: ").append(viaje.getUnidad() != null ? viaje.getUnidad().getPlaca() : "-")
                    .append("\n");
        }

        if (mensaje.length() == 0) {
            mensaje.append("No hay viajes pendientes en la cola.");
        }

        JOptionPane.showMessageDialog(
                this,
                mensaje.toString(),
                "VIAJES PENDIENTES EN COLA",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void actualizarContadorPendientes() {
        int cantidad = GestorViajes.getCantidadPendientes();
        if (lblContadorPendientes != null) {
            lblContadorPendientes.setText("VIAJES PENDIENTES: " + cantidad);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAtender;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnPendientes;
    private javax.swing.JButton btnRecuperar;
    private javax.swing.JButton btnRegistrarUrgencia;
    private javax.swing.JComboBox<String> cboCliente;
    private javax.swing.JComboBox<String> cboContenedor;
    private javax.swing.JComboBox<String> cboPlanta;
    private javax.swing.JComboBox<String> cboRuta;
    private javax.swing.JComboBox<String> cboTerminal;
    private javax.swing.JComboBox<String> cboTractor;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblContadorPendientes;
    private javax.swing.JPanel pnlContenido;
    private javax.swing.JTable tblViajes;
    // End of variables declaration//GEN-END:variables
}
