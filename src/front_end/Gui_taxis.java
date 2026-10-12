/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package front_end;

import back_end.ListaTaxis;
import back_end.Taxi;

/**
 * Interfaz Gráfica de Usuario para el TDA Lista Unidireccional de Taxis.
 *
 * <p>Capa de vista exclusiva: toda la lógica de la estructura de datos reside
 * en {@link back_end.ListaTaxis}. Esta clase únicamente captura datos del
 * formulario, invoca las operaciones del TDA y refleja el resultado en
 * la interfaz.</p>
 *
 * <p>Distribución de controles:</p>
 * <ul>
 *   <li><b>jPanel3</b>: formulario de captura (ID, Nombre, Apellido, Edad,
 *       CURP, Placas, Modelo) + campo de posición (jTextField8) y campo de
 *       búsqueda por ID (en jTextField8 también).</li>
 *   <li><b>jPanel2</b>: botones de operaciones (insertar/remover/ver por
 *       principio, final, posición y búsqueda).</li>
 *   <li><b>jTextArea1</b>: área de lista completa (se refresca en cada
 *       operación).</li>
 *   <li><b>jTextArea2</b>: área de resultado / mensaje de la última
 *       operación.</li>
 * </ul>
 *
 * @author Manuel Ángel Espinoza López. Geovani Gael Carmona Barbosa. Victor Manuel Leyva Perez
 */
public class Gui_taxis extends javax.swing.JFrame {

    // ---------------------------------------------------------------
    // TDA — instancia compartida por todos los handlers
    // ---------------------------------------------------------------
    private final ListaTaxis lista = new ListaTaxis();

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(Gui_taxis.class.getName());

    /**
     * Creates new form Gui_taxis
     */
    public Gui_taxis() {
        initComponents();
        setTitle("Sistema de Gestión de Taxis — Lista Unidireccional TDA");
        configurarEtiquetasBotones();
        refrescarLista();
    }

    // ---------------------------------------------------------------
    // Configuración inicial de etiquetas (los JButton del panel3
    // se usan como etiquetas no-clickeables en la plantilla NetBeans)
    // ---------------------------------------------------------------
    private void configurarEtiquetasBotones() {
        // Etiquetas del formulario izquierdo
        jButton12.setEnabled(false);  // "ID"
        jButton13.setEnabled(false);  // "Name"
        jButton14.setEnabled(false);  // "Apellido"
        jButton15.setEnabled(false);  // "Edad"
        // Etiquetas del formulario derecho
        jButton16.setEnabled(false);  // "CURP"
        jButton17.setEnabled(false);  // "Placas"
        jButton18.setEnabled(false);  // "Modelo"
        // jButton19 sigue siendo funcional ("Buscar(ID)")

        // Ajustar tooltips de botones de operación
        jButton1.setToolTipText("Insertar el taxi del formulario al principio de la lista");
        jButton7.setToolTipText("Insertar el taxi del formulario al final de la lista");
        jButton11.setToolTipText("Insertar el taxi del formulario en la posición indicada");
        jButton2.setToolTipText("Remover el primer taxi de la lista");
        jButton6.setToolTipText("Remover el último taxi de la lista");
        jButton10.setToolTipText("Remover el taxi en la posición indicada");
        jButton3.setToolTipText("Consultar el primer taxi sin modificar la lista");
        jButton5.setToolTipText("Consultar el último taxi sin modificar la lista");
        jButton4.setToolTipText("Consultar el taxi en la posición indicada");
        jButton9.setToolTipText("Buscar un taxi por su ID");
        jButton8.setToolTipText("Vaciar la lista completamente");
        jButton19.setToolTipText("Buscar taxi por el ID del campo inferior derecho");
        jButton19.addActionListener(this::jButton19ActionPerformed);

        // Placeholder text en áreas de texto
        jTextArea1.setText("(La lista está vacía)");
        jTextArea2.setText("Resultado de la última operación...");

        // Tip posición
        jTextField8.setToolTipText("Ingresa la posición (0-based) o el ID para búsqueda");
    }

    // ---------------------------------------------------------------
    // Helpers de validación y extracción de datos del formulario
    // ---------------------------------------------------------------

    /**
     * Construye un {@link Taxi} a partir de los campos del formulario.
     * Lanza {@link IllegalArgumentException} con mensaje amigable si
     * algún campo está vacío o tiene formato incorrecto.
     */
    private Taxi leerFormulario() {
        String sId      = jTextField1.getText().trim();
        String nombre   = jTextField2.getText().trim();
        String apellido = jTextField3.getText().trim();
        String sEdad    = jTextField4.getText().trim();
        String curp     = jTextField5.getText().trim();
        String placas   = jTextField6.getText().trim();
        String modelo   = jTextField7.getText().trim();

        if (sId.isEmpty() || nombre.isEmpty() || apellido.isEmpty()
                || sEdad.isEmpty() || curp.isEmpty() || placas.isEmpty()
                || modelo.isEmpty()) {
            throw new IllegalArgumentException("Todos los campos del formulario deben estar llenos.");
        }

        int id;
        try {
            id = Integer.parseInt(sId);
        } catch (NumberFormatException e) {
            jTextField1.requestFocus();
            throw new IllegalArgumentException("El ID debe ser un número entero positivo.");
        }

        int edad;
        try {
            edad = Integer.parseInt(sEdad);
        } catch (NumberFormatException e) {
            jTextField4.requestFocus();
            throw new IllegalArgumentException("La edad debe ser un número entero válido.");
        }

        // El constructor compacto del record valida el resto
        return new Taxi(id, nombre, apellido, edad, curp, placas, modelo);
    }

    /**
     * Lee la posición del campo {@code jTextField8}.
     *
     * @return entero de posición (0-based).
     * @throws IllegalArgumentException si está vacío o no es un número.
     */
    private int leerPosicion() {
        String s = jTextField8.getText().trim();
        if (s.isEmpty()) {
            jTextField8.requestFocus();
            throw new IllegalArgumentException("Ingresa una posición en el campo inferior derecho.");
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            jTextField8.requestFocus();
            throw new IllegalArgumentException("La posición debe ser un número entero.");
        }
    }

    /**
     * Lee el ID de búsqueda del campo {@code jTextField8}.
     *
     * @return entero de ID.
     * @throws IllegalArgumentException si está vacío o no es un número.
     */
    private int leerIdBusqueda() {
        String s = jTextField8.getText().trim();
        if (s.isEmpty()) {
            jTextField8.requestFocus();
            throw new IllegalArgumentException("Ingresa el ID a buscar en el campo inferior derecho.");
        }
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            jTextField8.requestFocus();
            throw new IllegalArgumentException("El ID debe ser un número entero positivo.");
        }
    }

    /**
     * Muestra un diálogo de error estándar.
     *
     * @param mensaje texto del error.
     */
    private void mostrarError(String mensaje) {
        javax.swing.JOptionPane.showMessageDialog(
                this, mensaje, "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un diálogo de información estándar.
     *
     * @param titulo  título del diálogo.
     * @param mensaje contenido.
     */
    private void mostrarInfo(String titulo, String mensaje) {
        javax.swing.JOptionPane.showMessageDialog(
                this, mensaje, titulo, javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Actualiza el {@code jTextArea1} con el contenido actual de la lista.
     * Se llama después de cada operación de inserción/remoción/vaciado.
     */
    private void refrescarLista() {
        jTextArea1.setText(lista.listarTodo()
                + "\n\n── Total: " + lista.getTamanio() + " taxi(s) ──");
        jTextArea1.setCaretPosition(0);
    }

    /**
     * Escribe el resultado de la última operación en {@code jTextArea2}.
     *
     * @param resultado texto a mostrar.
     */
    private void mostrarResultado(String resultado) {
        jTextArea2.setText(resultado);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jButton16 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setBackground(new java.awt.Color(204, 255, 255));
        jButton1.setText("Inset_inicio");
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setBackground(new java.awt.Color(204, 255, 255));
        jButton2.setText("Rmove_inicio");
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jButton3.setBackground(new java.awt.Color(204, 255, 255));
        jButton3.setText("ver_inicio");
        jButton3.addActionListener(this::jButton3ActionPerformed);

        jButton4.setBackground(new java.awt.Color(204, 255, 255));
        jButton4.setText("Buscar_valor");
        jButton4.addActionListener(this::jButton4ActionPerformed);

        jButton7.setBackground(new java.awt.Color(204, 255, 255));
        jButton7.setText("insert_final");
        jButton7.addActionListener(this::jButton7ActionPerformed);

        jButton6.setBackground(new java.awt.Color(204, 255, 255));
        jButton6.setText("Rmove_final");
        jButton6.addActionListener(this::jButton6ActionPerformed);

        jButton5.setBackground(new java.awt.Color(204, 255, 255));
        jButton5.setText("Ver_final");
        jButton5.addActionListener(this::jButton5ActionPerformed);

        jButton8.setBackground(new java.awt.Color(204, 255, 255));
        jButton8.setText("Vaciar_List");
        jButton8.addActionListener(this::jButton8ActionPerformed);

        jButton9.setBackground(new java.awt.Color(204, 255, 255));
        jButton9.setText("Buscar_valor");
        jButton9.addActionListener(this::jButton9ActionPerformed);

        jButton10.setBackground(new java.awt.Color(204, 255, 255));
        jButton10.setText("Rmove_posic");
        jButton10.addActionListener(this::jButton10ActionPerformed);

        jButton11.setBackground(new java.awt.Color(204, 255, 255));
        jButton11.setText("Insert_Posic");
        jButton11.addActionListener(this::jButton11ActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton8))
                .addGap(9, 9, 9)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton10)
                    .addComponent(jButton9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton11)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        jLabel1.setText("Manuel Ángel Espinoza López. Geovani Gael Carmona Barbosa. Victor Manuel Leyva Perez");

        jButton12.setText("ID");

        jButton13.setText("Name");

        jButton14.setText("Apellido");

        jButton15.setText("Edad");

        jButton16.setText("CURP");

        jButton17.setText("Placas");

        jButton18.setText("Modelo");

        jButton19.setText("Buscar(ID)");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton13, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(jButton16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(jButton19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(jButton17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(44, 44, 44)
                        .addComponent(jButton18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton12)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton16)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton13)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton17)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton14)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton18)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton15)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton19)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(47, 47, 47)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jScrollPane1)
                            .addContainerGap())
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // ---------------------------------------------------------------
    // Handlers de botones de operación
    // ---------------------------------------------------------------

    /** Insertar al principio */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            Taxi taxi = leerFormulario();
            lista.insertarAlPrincipio(taxi);
            refrescarLista();
            mostrarResultado("✔ Insertado al PRINCIPIO:\n" + taxi);
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /** Remover al principio */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            Taxi removido = lista.removerAlPrincipio();
            refrescarLista();
            mostrarResultado("✔ Removido del PRINCIPIO:\n" + removido);
        } catch (IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    /** Ver / mostrar al principio */
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            Taxi taxi = lista.mostrarAlPrincipio();
            mostrarResultado("🔍 Primer elemento (PRINCIPIO):\n" + taxi);
            mostrarInfo("Primer elemento", taxi.toString());
        } catch (IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    /** Buscar por posición (panel izquierdo, reutiliza jButton4) */
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            int posicion = leerPosicion();
            Taxi taxi = lista.mostrarEnPosicion(posicion);
            mostrarResultado("🔍 Elemento en POSICIÓN " + posicion + ":\n" + taxi);
            mostrarInfo("Posición " + posicion, taxi.toString());
        } catch (IllegalArgumentException | IllegalStateException | IndexOutOfBoundsException e) {
            mostrarError(e.getMessage());
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    /** Ver / mostrar al final */
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            Taxi taxi = lista.mostrarAlFinal();
            mostrarResultado("🔍 Último elemento (FINAL):\n" + taxi);
            mostrarInfo("Último elemento", taxi.toString());
        } catch (IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    /** Remover al final */
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            Taxi removido = lista.removerAlFinal();
            refrescarLista();
            mostrarResultado("✔ Removido del FINAL:\n" + removido);
        } catch (IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    /** Insertar al final */
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        try {
            Taxi taxi = leerFormulario();
            lista.insertarAlFinal(taxi);
            refrescarLista();
            mostrarResultado("✔ Insertado al FINAL:\n" + taxi);
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    /** Vaciar la lista */
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "¿Deseas vaciar completamente la lista? Esta acción no se puede deshacer.",
                "Confirmar vaciado",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE);
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            lista.vaciar();
            refrescarLista();
            mostrarResultado("🗑 La lista ha sido vaciada.");
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    /** Buscar por ID (panel derecho, botón central) */
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        try {
            int id = leerIdBusqueda();
            Object[] resultado = lista.buscarPorId(id);
            if (resultado == null) {
                mostrarError("No se encontró ningún taxi con ID = " + id + ".");
                mostrarResultado("✘ Búsqueda por ID " + id + ": sin resultados.");
            } else {
                Taxi taxi = (Taxi) resultado[0];
                int  pos  = (Integer) resultado[1];
                mostrarResultado("🔍 Encontrado en posición " + pos + ":\n" + taxi);
                mostrarInfo("Taxi encontrado (ID " + id + ")", "Posición: " + pos + "\n" + taxi);
            }
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    /** Remover por posición */
    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        try {
            int posicion = leerPosicion();
            Taxi removido = lista.removerEnPosicion(posicion);
            refrescarLista();
            mostrarResultado("✔ Removido en POSICIÓN " + posicion + ":\n" + removido);
        } catch (IllegalArgumentException | IllegalStateException | IndexOutOfBoundsException e) {
            mostrarError(e.getMessage());
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    /** Insertar por posición */
    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        try {
            Taxi taxi = leerFormulario();
            int posicion = leerPosicion();
            lista.insertarEnPosicion(taxi, posicion);
            refrescarLista();
            mostrarResultado("✔ Insertado en POSICIÓN " + posicion + ":\n" + taxi);
        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            mostrarError(e.getMessage());
        }
    }//GEN-LAST:event_jButton11ActionPerformed

    /** Buscar por ID — botón del formulario (jButton19) */
    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {
        // Reutiliza la misma lógica que jButton9
        jButton9ActionPerformed(evt);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Gui_taxis().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    // End of variables declaration//GEN-END:variables
}
