/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package vista;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import modelo.Planta;
import modelo.Ruta;
import modelo.Terminal;
import modelo.Ubicacion;
import servicio.GestorLogistica;

/**
 * Panel de gestión de Rutas de transporte.
 * Permite registrar, editar, listar y eliminar rutas operativas
 * sincronizadas con GestorLogistica.
 */
public class PanelRuta extends javax.swing.JPanel {

    private String opcion;
    private DefaultTableModel modeloTabla;

    /**
     * Creates new form PanelRuta
     */
    public PanelRuta() {
        initComponents();

        modeloTabla = (DefaultTableModel) tblRutas.getModel();

        jSplitPane.setResizeWeight(0);
        panelFormulario.setMinimumSize(new Dimension(0, 0));
        panelTabla.setMinimumSize(new Dimension(0, 0));
        ocultarFormulario();

        tblRutas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tblRutas.getSelectedRow();
                if (fila >= 0) {
                    Object val = modeloTabla.getValueAt(fila, 0);
                    if (val != null) {
                        txtId.setText(val.toString());
                    }
                }
            }
        });

        cargarDatos();
    }

    public void cargarDatos() {
        cargarCombos();
        actualizarTabla(GestorLogistica.getRutas());
    }

    public void cargarCombos() {
        DefaultComboBoxModel<String> modeloRetiro = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> modeloFinal = new DefaultComboBoxModel<>();
        for (Terminal t : GestorLogistica.getTerminales()) {
            modeloRetiro.addElement(t.getNombre());
            modeloFinal.addElement(t.getNombre());
        }
        cboTerminalRetiro.setModel(modeloRetiro);
        cboTerminalFinal.setModel(modeloFinal);

        DefaultComboBoxModel<String> modeloPlanta = new DefaultComboBoxModel<>();
        for (Planta p : GestorLogistica.getPlantas()) {
            modeloPlanta.addElement(p.getNombre());
        }
        cboPlantaCarga.setModel(modeloPlanta);
    }

    private void actualizarTabla(List<Ruta> lista) {
        modeloTabla.setRowCount(0);
        for (Ruta r : lista) {
            String nomCochera = (r.getCochera() != null) ? r.getCochera().getDireccion() : "";
            String nomRetiro = (r.getTerminalRetiro() != null) ? r.getTerminalRetiro().getNombre() : "";
            String nomPlanta = (r.getPlantaCarga() != null) ? r.getPlantaCarga().getNombre() : "";
            String nomFinal = (r.getTerminalFinal() != null) ? r.getTerminalFinal().getNombre() : "";
            Object[] fila = new Object[]{
                r.getId(),
                nomCochera,
                nomRetiro,
                nomPlanta,
                nomFinal,
                r.getTarifa()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void mostrarFormulario() {
        jSplitPane.setDividerSize(8);
        SwingUtilities.invokeLater(() -> {
            int h = jSplitPane.getHeight();
            int loc = (h > 0) ? Math.min(220, Math.max(180, h / 3)) : 200;
            jSplitPane.setDividerLocation(loc);
        });
        btnAgregar.setText("Cerrar formulario");
    }

    private void ocultarFormulario() {
        jSplitPane.setDividerSize(0);
        SwingUtilities.invokeLater(() -> jSplitPane.setDividerLocation(0));
        btnAgregar.setText("Agregar");
        btnOpcion.setVisible(false);
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtCochera.setText("");
        txtTarifa.setText("");
        if (cboTerminalRetiro.getItemCount() > 0) {
            cboTerminalRetiro.setSelectedIndex(0);
        }
        if (cboPlantaCarga.getItemCount() > 0) {
            cboPlantaCarga.setSelectedIndex(0);
        }
        if (cboTerminalFinal.getItemCount() > 0) {
            cboTerminalFinal.setSelectedIndex(0);
        }
        txtId.setEditable(true);
        txtCochera.setEditable(true);
        txtTarifa.setEditable(true);
        cboTerminalRetiro.setEnabled(true);
        cboPlantaCarga.setEnabled(true);
        cboTerminalFinal.setEnabled(true);
    }

    private void prepararEdicionDeRuta(Ruta r) {
        mostrarFormulario();
        cargarCombos();

        txtId.setText(String.valueOf(r.getId()));
        txtId.setEditable(false);
        txtCochera.setText(r.getCochera() != null ? r.getCochera().getDireccion() : "");
        txtTarifa.setText(String.valueOf(r.getTarifa()));

        if (r.getTerminalRetiro() != null) {
            cboTerminalRetiro.setSelectedItem(r.getTerminalRetiro().getNombre());
        }
        if (r.getPlantaCarga() != null) {
            cboPlantaCarga.setSelectedItem(r.getPlantaCarga().getNombre());
        }
        if (r.getTerminalFinal() != null) {
            cboTerminalFinal.setSelectedItem(r.getTerminalFinal().getNombre());
        }

        lblDetalleAccion.setText("Editar datos de la ruta (ID protegido):");
        opcion = "guardar";
        btnOpcion.setText("Guardar");
        btnOpcion.setBackground(new Color(255, 153, 102));
        btnOpcion.setForeground(Color.WHITE);
        btnOpcion.setVisible(true);

        txtCochera.requestFocus();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBarra = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        panelBotones = new javax.swing.JPanel();
        btnAgregar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jSplitPane = new javax.swing.JSplitPane();
        panelFormulario = new javax.swing.JPanel();
        contenidoPanelFormulario = new javax.swing.JPanel();
        lblId = new javax.swing.JLabel();
        txtId = new javax.swing.JTextField();
        lblCochera = new javax.swing.JLabel();
        txtCochera = new javax.swing.JTextField();
        lblTerminalRetiro = new javax.swing.JLabel();
        cboTerminalRetiro = new javax.swing.JComboBox<>();
        lblPlantaCarga = new javax.swing.JLabel();
        cboPlantaCarga = new javax.swing.JComboBox<>();
        lblTerminalFinal = new javax.swing.JLabel();
        cboTerminalFinal = new javax.swing.JComboBox<>();
        lblTarifa = new javax.swing.JLabel();
        txtTarifa = new javax.swing.JTextField();
        lblDetalleAccion = new javax.swing.JLabel();
        btnOpcion = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        panelTabla = new javax.swing.JPanel();
        jScrollPane = new javax.swing.JScrollPane();
        tblRutas = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(810, 540));
        setMinimumSize(new java.awt.Dimension(810, 540));
        setPreferredSize(new java.awt.Dimension(810, 540));
        setLayout(new java.awt.BorderLayout());

        panelBarra.setBackground(new java.awt.Color(255, 255, 255));
        panelBarra.setLayout(new java.awt.BorderLayout());

        lblTitulo.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        lblTitulo.setText("      RUTAS");
        panelBarra.add(lblTitulo, java.awt.BorderLayout.CENTER);

        panelBotones.setBackground(new java.awt.Color(255, 255, 255));
        panelBotones.setLayout(new java.awt.FlowLayout());

        btnAgregar.setBackground(new java.awt.Color(0, 153, 255));
        btnAgregar.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        panelBotones.add(btnAgregar);

        btnEditar.setBackground(new java.awt.Color(255, 153, 102));
        btnEditar.setForeground(new java.awt.Color(255, 255, 255));
        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });
        panelBotones.add(btnEditar);

        btnEliminar.setBackground(new java.awt.Color(255, 51, 51));
        btnEliminar.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });
        panelBotones.add(btnEliminar);

        panelBarra.add(panelBotones, java.awt.BorderLayout.LINE_END);

        add(panelBarra, java.awt.BorderLayout.PAGE_START);

        jSplitPane.setBackground(new java.awt.Color(255, 255, 255));
        jSplitPane.setDividerLocation(200);
        jSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        panelFormulario.setBackground(new java.awt.Color(255, 255, 255));
        panelFormulario.setMinimumSize(new java.awt.Dimension(100, 100));
        panelFormulario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        contenidoPanelFormulario.setBackground(new java.awt.Color(255, 255, 255));
        contenidoPanelFormulario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblId.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        lblId.setText("ID");
        contenidoPanelFormulario.add(lblId, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, -1, -1));

        txtId.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contenidoPanelFormulario.add(txtId, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 70, 30));

        lblCochera.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        lblCochera.setText("Cochera");
        contenidoPanelFormulario.add(lblCochera, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, -1, -1));

        txtCochera.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contenidoPanelFormulario.add(txtCochera, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 150, 30));

        lblTerminalRetiro.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        lblTerminalRetiro.setText("Terminal Retiro");
        contenidoPanelFormulario.add(lblTerminalRetiro, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 30, -1, -1));

        cboTerminalRetiro.setBackground(new java.awt.Color(255, 255, 255));
        contenidoPanelFormulario.add(cboTerminalRetiro, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, 160, 30));

        lblPlantaCarga.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        lblPlantaCarga.setText("Planta Carga");
        contenidoPanelFormulario.add(lblPlantaCarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, -1, -1));

        cboPlantaCarga.setBackground(new java.awt.Color(255, 255, 255));
        contenidoPanelFormulario.add(cboPlantaCarga, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 50, 160, 30));

        lblTerminalFinal.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        lblTerminalFinal.setText("Terminal Final");
        contenidoPanelFormulario.add(lblTerminalFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 30, -1, -1));

        cboTerminalFinal.setBackground(new java.awt.Color(255, 255, 255));
        contenidoPanelFormulario.add(cboTerminalFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 50, 160, 30));

        lblTarifa.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        lblTarifa.setText("Tarifa (S/.)");
        contenidoPanelFormulario.add(lblTarifa, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        txtTarifa.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contenidoPanelFormulario.add(txtTarifa, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 100, 30));

        lblDetalleAccion.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        lblDetalleAccion.setText("Configurar puntos de ruta y costo:");
        contenidoPanelFormulario.add(lblDetalleAccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 90, 350, -1));

        btnOpcion.setForeground(new java.awt.Color(255, 255, 255));
        btnOpcion.setText("Opcion");
        btnOpcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpcionActionPerformed(evt);
            }
        });
        contenidoPanelFormulario.add(btnOpcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 110, 130, -1));

        btnCancelar.setBackground(new java.awt.Color(255, 102, 102));
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        contenidoPanelFormulario.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 110, 120, -1));

        panelFormulario.add(contenidoPanelFormulario, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 810, 200));

        jSplitPane.setTopComponent(panelFormulario);

        panelTabla.setBackground(new java.awt.Color(255, 255, 255));
        panelTabla.setLayout(new java.awt.BorderLayout());

        jScrollPane.setBackground(new java.awt.Color(255, 255, 255));

        tblRutas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Cochera", "Terminal Retiro", "Planta Carga", "Terminal Final", "Tarifa (S/.)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane.setViewportView(tblRutas);

        panelTabla.add(jScrollPane, java.awt.BorderLayout.CENTER);

        jSplitPane.setRightComponent(panelTabla);

        add(jSplitPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        if (btnAgregar.getText().equals("Cerrar formulario")) {
            ocultarFormulario();
            return;
        }

        cargarCombos();
        if (cboTerminalRetiro.getItemCount() == 0 || cboPlantaCarga.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Debe haber al menos un terminal y una planta registrados para crear una ruta.",
                    "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        limpiarCampos();
        // Proponer siguiente ID disponible
        int maxId = 0;
        for (Ruta r : GestorLogistica.getRutas()) {
            if (r.getId() > maxId) {
                maxId = r.getId();
            }
        }
        txtId.setText(String.valueOf(maxId + 1));
        txtCochera.setText("Cochera Principal");

        lblDetalleAccion.setText("Registrar nueva ruta:");
        opcion = "registrar";
        btnOpcion.setText("Registrar");
        btnOpcion.setBackground(new Color(0, 153, 255));
        btnOpcion.setForeground(Color.WHITE);
        btnOpcion.setVisible(true);

        mostrarFormulario();
        txtCochera.requestFocus();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una ruta de la tabla o ingrese el ID a editar.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Ruta r = GestorLogistica.buscarRutaPorId(id);
            if (r == null) {
                JOptionPane.showMessageDialog(this, "No se encontró ninguna ruta con ID: " + id);
                return;
            }
            prepararEdicionDeRuta(r);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero válido.");
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        String idStr = txtId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una ruta de la tabla o ingrese el ID a eliminar.");
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            Ruta r = GestorLogistica.buscarRutaPorId(id);
            if (r == null) {
                JOptionPane.showMessageDialog(this, "No se encontró ninguna ruta con ID: " + id);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Está seguro de eliminar la Ruta ID " + id + "?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                GestorLogistica.eliminarRuta(id);
                JOptionPane.showMessageDialog(this, "Ruta eliminada correctamente.");
                limpiarCampos();
                ocultarFormulario();
                actualizarTabla(GestorLogistica.getRutas());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero válido.");
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnOpcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpcionActionPerformed
        if ("registrar".equals(opcion)) {
            try {
                String idStr = txtId.getText().trim();
                String cocheraTexto = txtCochera.getText().trim();
                String tarifaStr = txtTarifa.getText().trim();

                if (idStr.isEmpty() || cocheraTexto.isEmpty() || tarifaStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Complete todos los campos de la ruta.");
                    return;
                }

                int id = Integer.parseInt(idStr);
                if (id <= 0) {
                    JOptionPane.showMessageDialog(this, "El ID debe ser mayor a 0.");
                    return;
                }

                if (GestorLogistica.buscarRutaPorId(id) != null) {
                    JOptionPane.showMessageDialog(this, "Ya existe una ruta con ID " + id);
                    return;
                }

                double tarifa = Double.parseDouble(tarifaStr);
                if (tarifa < 0) {
                    JOptionPane.showMessageDialog(this, "La tarifa no puede ser negativa.");
                    return;
                }

                String nomRetiro = (String) cboTerminalRetiro.getSelectedItem();
                String nomPlanta = (String) cboPlantaCarga.getSelectedItem();
                String nomFinal = (String) cboTerminalFinal.getSelectedItem();

                if (nomRetiro == null || nomPlanta == null || nomFinal == null) {
                    JOptionPane.showMessageDialog(this, "Seleccione las ubicaciones de retiro, planta y terminal final.");
                    return;
                }

                Terminal terminalRetiro = null;
                Terminal terminalFinal = null;
                for (Terminal t : GestorLogistica.getTerminales()) {
                    if (t.getNombre().equalsIgnoreCase(nomRetiro)) {
                        terminalRetiro = t;
                    }
                    if (t.getNombre().equalsIgnoreCase(nomFinal)) {
                        terminalFinal = t;
                    }
                }

                Planta plantaCarga = null;
                for (Planta p : GestorLogistica.getPlantas()) {
                    if (p.getNombre().equalsIgnoreCase(nomPlanta)) {
                        plantaCarga = p;
                    }
                }

                if (terminalRetiro == null || terminalFinal == null || plantaCarga == null) {
                    JOptionPane.showMessageDialog(this, "No se encontraron los puntos seleccionados en el sistema.");
                    return;
                }

                Ubicacion cochera = new Ubicacion(id, "", "", "", cocheraTexto);
                Ruta nuevaRuta = new Ruta(id, cochera, terminalRetiro, plantaCarga, terminalFinal, tarifa);
                GestorLogistica.agregarRuta(nuevaRuta);

                JOptionPane.showMessageDialog(this, "Ruta registrada correctamente.");
                limpiarCampos();
                ocultarFormulario();
                actualizarTabla(GestorLogistica.getRutas());

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El ID debe ser entero y la tarifa debe ser un número válido.");
            }
        } else if ("guardar".equals(opcion)) {
            try {
                int id = Integer.parseInt(txtId.getText().trim());
                Ruta r = GestorLogistica.buscarRutaPorId(id);
                if (r == null) {
                    JOptionPane.showMessageDialog(this, "Ruta no encontrada.");
                    return;
                }

                String cocheraTexto = txtCochera.getText().trim();
                String tarifaStr = txtTarifa.getText().trim();

                if (cocheraTexto.isEmpty() || tarifaStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Complete todos los campos de la ruta.");
                    return;
                }

                double tarifa = Double.parseDouble(tarifaStr);
                if (tarifa < 0) {
                    JOptionPane.showMessageDialog(this, "La tarifa no puede ser negativa.");
                    return;
                }

                String nomRetiro = (String) cboTerminalRetiro.getSelectedItem();
                String nomPlanta = (String) cboPlantaCarga.getSelectedItem();
                String nomFinal = (String) cboTerminalFinal.getSelectedItem();

                Terminal terminalRetiro = null;
                Terminal terminalFinal = null;
                for (Terminal t : GestorLogistica.getTerminales()) {
                    if (t.getNombre().equalsIgnoreCase(nomRetiro)) {
                        terminalRetiro = t;
                    }
                    if (t.getNombre().equalsIgnoreCase(nomFinal)) {
                        terminalFinal = t;
                    }
                }

                Planta plantaCarga = null;
                for (Planta p : GestorLogistica.getPlantas()) {
                    if (p.getNombre().equalsIgnoreCase(nomPlanta)) {
                        plantaCarga = p;
                    }
                }

                if (terminalRetiro == null || terminalFinal == null || plantaCarga == null) {
                    JOptionPane.showMessageDialog(this, "No se encontraron los puntos seleccionados en el sistema.");
                    return;
                }

                r.setCochera(new Ubicacion(id, "", "", "", cocheraTexto));
                r.setTerminalRetiro(terminalRetiro);
                r.setPlantaCarga(plantaCarga);
                r.setTerminalFinal(terminalFinal);
                r.setTarifa(tarifa);

                JOptionPane.showMessageDialog(this, "Ruta actualizada correctamente.");
                limpiarCampos();
                ocultarFormulario();
                actualizarTabla(GestorLogistica.getRutas());

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Formato numérico inválido en tarifa o ID.");
            }
        }
    }//GEN-LAST:event_btnOpcionActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        limpiarCampos();
        ocultarFormulario();
    }//GEN-LAST:event_btnCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnOpcion;
    private javax.swing.JComboBox<String> cboPlantaCarga;
    private javax.swing.JComboBox<String> cboTerminalFinal;
    private javax.swing.JComboBox<String> cboTerminalRetiro;
    private javax.swing.JPanel contenidoPanelFormulario;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JSplitPane jSplitPane;
    private javax.swing.JLabel lblCochera;
    private javax.swing.JLabel lblDetalleAccion;
    private javax.swing.JLabel lblId;
    private javax.swing.JLabel lblPlantaCarga;
    private javax.swing.JLabel lblTarifa;
    private javax.swing.JLabel lblTerminalFinal;
    private javax.swing.JLabel lblTerminalRetiro;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel panelBarra;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JPanel panelFormulario;
    private javax.swing.JPanel panelTabla;
    private javax.swing.JTable tblRutas;
    private javax.swing.JTextField txtCochera;
    private javax.swing.JTextField txtId;
    private javax.swing.JTextField txtTarifa;
    // End of variables declaration//GEN-END:variables
}
