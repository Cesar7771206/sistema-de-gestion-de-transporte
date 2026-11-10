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
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import modelo.Tractor;
import servicio.GestorEquipos;
import servicio.TractorServicio;

/**
 * Panel de gestión de Tractores.
 * Permite registrar, editar, listar y eliminar tractores de la flota
 * manteniendo sincronización con GestorEquipos.
 */
public class PanelTractor extends javax.swing.JPanel {

    private String opcion;
    private TractorServicio tractorServicio;
    private DefaultTableModel modeloTabla;

    /**
     * Creates new form PanelTractor
     */
    public PanelTractor() {
        initComponents();

        tractorServicio = new TractorServicio();
        modeloTabla = (DefaultTableModel) tblTractors.getModel();

        jSplitPane.setResizeWeight(0);
        panelFormulario.setMinimumSize(new Dimension(0, 0));
        panelTabla.setMinimumSize(new Dimension(0, 0));
        ocultarFormulario();

        tblTractors.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = tblTractors.getSelectedRow();
                if (fila >= 0) {
                    String placa = modeloTabla.getValueAt(fila, 0).toString();
                    txtPlaca.setText(placa);
                }
            }
        });

        cargarDatos();
    }

    public void cargarDatos() {
        for (Tractor t : GestorEquipos.getTractores()) {
            if (tractorServicio.getTractorByPlaca(t.getPlaca()) == null) {
                tractorServicio.registrarTractor(
                        t.getPlaca(),
                        t.getEjes(),
                        t.getMarca(),
                        t.getModelo(),
                        t.getCarroceria(),
                        t.getAnio(),
                        t.getMTC()
                );
            }
        }
        actualizarTabla(tractorServicio.getTractors());
    }

    private void actualizarTabla(List<Tractor> listaTractors) {
        modeloTabla.setRowCount(0);
        for (Tractor t : listaTractors) {
            Object[] fila = new Object[]{
                t.getPlaca(),
                t.getEjes(),
                t.getMarca(),
                t.getModelo(),
                t.getCarroceria(),
                t.getAnio(),
                t.getMTC()
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

    private void mostrarCampos(boolean mostrar) {
        txtEjes.setVisible(mostrar);
        txtMarca.setVisible(mostrar);
        txtModelo.setVisible(mostrar);
        txtCarroceria.setVisible(mostrar);
        txtMTC.setVisible(mostrar);
        txtAnio.setVisible(mostrar);

        lblEjes.setVisible(mostrar);
        lblMarca.setVisible(mostrar);
        lblModelo.setVisible(mostrar);
        lblCarroceria.setVisible(mostrar);
        lblMTC.setVisible(mostrar);
        lblAnio.setVisible(mostrar);
    }

    private void editarCampos(boolean editar) {
        txtPlaca.setEditable(editar);
        txtEjes.setEditable(editar);
        txtMarca.setEditable(editar);
        txtModelo.setEditable(editar);
        txtCarroceria.setEditable(editar);
        txtMTC.setEditable(editar);
        txtAnio.setEditable(editar);
    }

    private void limpiarCampos() {
        txtPlaca.setText("");
        txtEjes.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtCarroceria.setText("");
        txtAnio.setText("");
        txtMTC.setText("");
        editarCampos(true);
    }

    private void prepararEdicionDeTractor(Tractor t) {
        mostrarFormulario();
        mostrarCampos(true);
        txtPlaca.setText(t.getPlaca());
        txtEjes.setText(String.valueOf(t.getEjes()));
        txtMarca.setText(t.getMarca());
        txtModelo.setText(t.getModelo());
        txtCarroceria.setText(t.getCarroceria());
        txtAnio.setText(String.valueOf(t.getAnio()));
        txtMTC.setText(t.getMTC());

        txtPlaca.setEditable(false);
        lblPlaca.setText("Editar datos del tractor (Placa protegida):");

        opcion = "guardar";
        btnOpcion.setText("Guardar");
        btnOpcion.setBackground(new Color(40, 167, 69));
        btnOpcion.setForeground(Color.WHITE);
        btnOpcion.setVisible(true);

        txtEjes.requestFocus();
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
        txtPlaca = new javax.swing.JTextField();
        lblMarca = new javax.swing.JLabel();
        lblModelo = new javax.swing.JLabel();
        lblPlaca = new javax.swing.JLabel();
        txtModelo = new javax.swing.JTextField();
        txtCarroceria = new javax.swing.JTextField();
        txtMarca = new javax.swing.JTextField();
        lblCarroceria = new javax.swing.JLabel();
        lblAnio = new javax.swing.JLabel();
        txtAnio = new javax.swing.JTextField();
        lblMTC = new javax.swing.JLabel();
        txtMTC = new javax.swing.JTextField();
        txtEjes = new javax.swing.JTextField();
        lblEjes = new javax.swing.JLabel();
        btnOpcion = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        panelTabla = new javax.swing.JPanel();
        jScrollPane = new javax.swing.JScrollPane();
        tblTractors = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(810, 540));
        setMinimumSize(new java.awt.Dimension(810, 540));
        setPreferredSize(new java.awt.Dimension(810, 540));
        setLayout(new java.awt.BorderLayout());

        panelBarra.setBackground(new java.awt.Color(255, 255, 255));
        panelBarra.setLayout(new java.awt.BorderLayout());

        lblTitulo.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        lblTitulo.setText("      TRACTORES");
        panelBarra.add(lblTitulo, java.awt.BorderLayout.CENTER);

        panelBotones.setBackground(new java.awt.Color(255, 255, 255));

        btnAgregar.setBackground(new java.awt.Color(51, 153, 255));
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

        txtPlaca.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contenidoPanelFormulario.add(txtPlaca, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 190, 30));

        lblMarca.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        lblMarca.setText("Marca");
        contenidoPanelFormulario.add(lblMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, -1, -1));

        lblModelo.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        lblModelo.setText("Modelo");
        contenidoPanelFormulario.add(lblModelo, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, -1, -1));

        lblPlaca.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        lblPlaca.setText("Placa");
        contenidoPanelFormulario.add(lblPlaca, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        txtModelo.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contenidoPanelFormulario.add(txtModelo, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 50, 130, 30));

        txtCarroceria.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contenidoPanelFormulario.add(txtCarroceria, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 50, 130, 30));

        txtMarca.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contenidoPanelFormulario.add(txtMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 130, 30));

        lblCarroceria.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        lblCarroceria.setText("Carrocería");
        contenidoPanelFormulario.add(lblCarroceria, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 30, -1, -1));

        lblAnio.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        lblAnio.setText("Año");
        contenidoPanelFormulario.add(lblAnio, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 30, -1, -1));

        txtAnio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contenidoPanelFormulario.add(txtAnio, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 50, 80, 30));

        lblMTC.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        lblMTC.setText("MTC");
        contenidoPanelFormulario.add(lblMTC, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 30, -1, -1));

        txtMTC.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contenidoPanelFormulario.add(txtMTC, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 50, 130, 30));

        txtEjes.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        contenidoPanelFormulario.add(txtEjes, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 80, 30));

        lblEjes.setFont(new java.awt.Font("sansserif", 1, 13)); // NOI18N
        lblEjes.setText("Ejes");
        contenidoPanelFormulario.add(lblEjes, new org.netbeans.lib.awtextra.AbsoluteConstraints(23, 28, -1, -1));

        btnOpcion.setForeground(new java.awt.Color(255, 255, 255));
        btnOpcion.setText("Opcion");
        btnOpcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpcionActionPerformed(evt);
            }
        });
        contenidoPanelFormulario.add(btnOpcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 110, 130, -1));

        btnCancelar.setBackground(new java.awt.Color(255, 102, 102));
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        contenidoPanelFormulario.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 110, 120, -1));

        panelFormulario.add(contenidoPanelFormulario, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 810, 200));

        jSplitPane.setTopComponent(panelFormulario);

        panelTabla.setBackground(new java.awt.Color(255, 255, 255));
        panelTabla.setLayout(new java.awt.BorderLayout());

        jScrollPane.setBackground(new java.awt.Color(255, 255, 255));

        tblTractors.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Placa", "Ejes", "Marca", "Modelo", "Carrocería", "Año", "MTC"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane.setViewportView(tblTractors);

        panelTabla.add(jScrollPane, java.awt.BorderLayout.CENTER);

        jSplitPane.setRightComponent(panelTabla);

        add(jSplitPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        if (btnAgregar.getText().equalsIgnoreCase("Cerrar formulario") && "registrar".equals(opcion)) {
            ocultarFormulario();
            limpiarCampos();
            return;
        }

        mostrarFormulario();
        mostrarCampos(true);
        limpiarCampos();

        lblPlaca.setText("Placa");
        opcion = "registrar";

        btnOpcion.setText("Registrar");
        btnOpcion.setBackground(new Color(40, 167, 69));
        btnOpcion.setForeground(Color.WHITE);
        btnOpcion.setVisible(true);

        txtEjes.requestFocus();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnOpcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpcionActionPerformed
        switch (opcion) {
            case "registrar":
                try {
                    String placa = txtPlaca.getText().trim();
                    String marca = txtMarca.getText().trim();
                    String modelo = txtModelo.getText().trim();
                    String carroceria = txtCarroceria.getText().trim();
                    String MTC = txtMTC.getText().trim();

                    if (placa.isEmpty() || marca.isEmpty() || modelo.isEmpty() || carroceria.isEmpty() || MTC.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Complete todos los campos del tractor.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    int ejes = Integer.parseInt(txtEjes.getText().trim());
                    int anio = Integer.parseInt(txtAnio.getText().trim());

                    tractorServicio.registrarTractor(placa, ejes, marca, modelo, carroceria, anio, MTC);
                    Tractor tractorRegistrado = tractorServicio.getTractorByPlaca(placa);
                    GestorEquipos.agregarTractor(tractorRegistrado);

                    JOptionPane.showMessageDialog(this, "Tractor " + placa + " registrado exitosamente.");
                    limpiarCampos();
                    btnOpcion.setVisible(false);
                    actualizarTabla(tractorServicio.getTractors());
                    ocultarFormulario();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Ejes y Año deben ser valores numéricos enteros.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException | IllegalStateException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Error al Registrar", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case "seleccionarGuardar":
                try {
                    String placa = txtPlaca.getText().trim();
                    Tractor t = tractorServicio.getTractorByPlaca(placa);
                    if (t == null) {
                        t = GestorEquipos.buscarTractorPorPlaca(placa);
                    }

                    if (t == null) {
                        JOptionPane.showMessageDialog(this, "El tractor con placa " + placa + " no está registrado.", "No Encontrado", JOptionPane.WARNING_MESSAGE);
                        txtPlaca.setText("");
                        txtPlaca.requestFocus();
                    } else {
                        prepararEdicionDeTractor(t);
                    }
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
                }
                break;

            case "guardar":
                try {
                    String placa = txtPlaca.getText().trim();
                    String marca = txtMarca.getText().trim();
                    String modelo = txtModelo.getText().trim();
                    String carroceria = txtCarroceria.getText().trim();
                    String MTC = txtMTC.getText().trim();

                    if (marca.isEmpty() || modelo.isEmpty() || carroceria.isEmpty() || MTC.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Complete todos los campos a actualizar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                        return;
                    }

                    int ejes = Integer.parseInt(txtEjes.getText().trim());
                    int anio = Integer.parseInt(txtAnio.getText().trim());

                    tractorServicio.actualizarTractor(placa, ejes, marca, modelo, carroceria, anio, MTC);

                    Tractor tGestor = GestorEquipos.buscarTractorPorPlaca(placa);
                    if (tGestor != null) {
                        tGestor.ActualizarTodosAtributos(ejes, marca, modelo, carroceria, anio, MTC);
                    }

                    JOptionPane.showMessageDialog(this, "Tractor " + placa + " actualizado correctamente.");
                    actualizarTabla(tractorServicio.getTractors());
                    limpiarCampos();
                    ocultarFormulario();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Ejes y Año deben ser valores numéricos enteros.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException | IllegalStateException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Error al Actualizar", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case "seleccionarEliminar":
                try {
                    String placa = txtPlaca.getText().trim();
                    Tractor t = tractorServicio.getTractorByPlaca(placa);
                    if (t == null) {
                        t = GestorEquipos.buscarTractorPorPlaca(placa);
                    }

                    if (t == null) {
                        JOptionPane.showMessageDialog(this, "El tractor con placa " + placa + " no está registrado.", "No Encontrado", JOptionPane.WARNING_MESSAGE);
                        txtPlaca.setText("");
                    } else {
                        mostrarCampos(true);
                        txtEjes.setText(String.valueOf(t.getEjes()));
                        txtMarca.setText(t.getMarca());
                        txtModelo.setText(t.getModelo());
                        txtCarroceria.setText(t.getCarroceria());
                        txtAnio.setText(String.valueOf(t.getAnio()));
                        txtMTC.setText(t.getMTC());

                        editarCampos(false);
                        lblPlaca.setText("¿Desea confirmar la eliminación del tractor " + placa + "?");

                        opcion = "eliminar";
                        btnOpcion.setText("Confirmar");
                        btnOpcion.setBackground(new Color(220, 53, 69));
                        btnOpcion.setForeground(Color.WHITE);
                        btnOpcion.setVisible(true);
                    }
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
                }
                break;

            case "eliminar":
                try {
                    String placa = txtPlaca.getText().trim();

                    if (GestorEquipos.buscarUnidadPorTractor(placa) != null) {
                        JOptionPane.showMessageDialog(
                                this,
                                "No se puede eliminar el tractor " + placa + " porque está vinculado a una Unidad Operativa activa.\n"
                                        + "Desvincule primero la unidad en el módulo Data -> Unidad.",
                                "Tractor en Uso",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }

                    tractorServicio.eliminarTractor(placa);
                    GestorEquipos.getTractores().removeIf(t -> t.getPlaca().equalsIgnoreCase(placa));

                    JOptionPane.showMessageDialog(this, "Tractor " + placa + " eliminado correctamente.");
                    actualizarTabla(tractorServicio.getTractors());
                    limpiarCampos();
                    ocultarFormulario();
                } catch (IllegalArgumentException | IllegalStateException e) {
                    JOptionPane.showMessageDialog(this, e.getMessage(), "Error al Eliminar", JOptionPane.ERROR_MESSAGE);
                }
                break;

            default:
                break;
        }
    }//GEN-LAST:event_btnOpcionActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // Si hay una fila seleccionada en la tabla o placa escrita, editar directamente
        int fila = tblTractors.getSelectedRow();
        String placa = (fila >= 0) ? modeloTabla.getValueAt(fila, 0).toString() : txtPlaca.getText().trim();

        if (!placa.isEmpty()) {
            Tractor t = tractorServicio.getTractorByPlaca(placa);
            if (t == null) {
                t = GestorEquipos.buscarTractorPorPlaca(placa);
            }
            if (t != null) {
                prepararEdicionDeTractor(t);
                return;
            }
        }

        mostrarFormulario();
        mostrarCampos(false);
        limpiarCampos();

        opcion = "seleccionarGuardar";
        lblPlaca.setText("Ingrese la placa del tractor a editar:");
        btnOpcion.setText("Seleccionar");
        btnOpcion.setBackground(new Color(255, 193, 7));
        btnOpcion.setForeground(Color.BLACK);
        btnOpcion.setVisible(true);

        txtPlaca.requestFocus();
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        int fila = tblTractors.getSelectedRow();
        String placa = (fila >= 0) ? modeloTabla.getValueAt(fila, 0).toString() : txtPlaca.getText().trim();

        if (!placa.isEmpty()) {
            Tractor t = tractorServicio.getTractorByPlaca(placa);
            if (t == null) {
                t = GestorEquipos.buscarTractorPorPlaca(placa);
            }
            if (t != null) {
                int r = JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de eliminar el tractor con placa " + placa + "?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION
                );
                if (r == JOptionPane.YES_OPTION) {
                    if (GestorEquipos.buscarUnidadPorTractor(placa) != null) {
                        JOptionPane.showMessageDialog(
                                this,
                                "No se puede eliminar el tractor " + placa + " porque está vinculado a una Unidad Operativa activa.",
                                "Tractor en Uso",
                                JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    tractorServicio.eliminarTractor(placa);
                    GestorEquipos.getTractores().removeIf(x -> x.getPlaca().equalsIgnoreCase(placa));
                    actualizarTabla(tractorServicio.getTractors());
                    limpiarCampos();
                    ocultarFormulario();
                }
                return;
            }
        }

        mostrarFormulario();
        mostrarCampos(false);
        limpiarCampos();

        opcion = "seleccionarEliminar";
        lblPlaca.setText("Ingrese la placa del tractor a eliminar:");
        btnOpcion.setText("Seleccionar");
        btnOpcion.setBackground(new Color(255, 193, 7));
        btnOpcion.setForeground(Color.BLACK);
        btnOpcion.setVisible(true);

        txtPlaca.requestFocus();
    }//GEN-LAST:event_btnEliminarActionPerformed

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
    private javax.swing.JPanel contenidoPanelFormulario;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JSplitPane jSplitPane;
    private javax.swing.JLabel lblAnio;
    private javax.swing.JLabel lblCarroceria;
    private javax.swing.JLabel lblEjes;
    private javax.swing.JLabel lblMTC;
    private javax.swing.JLabel lblMarca;
    private javax.swing.JLabel lblModelo;
    private javax.swing.JLabel lblPlaca;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel panelBarra;
    private javax.swing.JPanel panelBotones;
    private javax.swing.JPanel panelFormulario;
    private javax.swing.JPanel panelTabla;
    private javax.swing.JTable tblTractors;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JTextField txtCarroceria;
    private javax.swing.JTextField txtEjes;
    private javax.swing.JTextField txtMTC;
    private javax.swing.JTextField txtMarca;
    private javax.swing.JTextField txtModelo;
    private javax.swing.JTextField txtPlaca;
    // End of variables declaration//GEN-END:variables
}
