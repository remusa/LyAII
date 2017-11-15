package Vista;

import CodigoIntermedio.Cuadruple;
import CodigoIntermedio.TraductorEtiquetas;
import Excel.TraductorEn;
import Inicio.AText;
import Lexico.Cambiar;
import Optimizacion.Analizador;
import Semantico.GuardarTS;
import java.io.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Vista extends javax.swing.JFrame {

//    ArrayList<Token> tmp;
//    ArrayList<TokendeTabla> tdt, r;
//    public boolean isCorrect;
    JTable table;
    JScrollPane scroll;
    JButton jbClick;
    JFileChooser jChooser;
    int tableWidth = 0; // set the tableWidth
    int tableHeight = 0; // set the tableHeight

    Vector headers = new Vector();
    Vector data = new Vector();
    DefaultTableModel model = null;

    Vector headers2 = new Vector();
    Vector data2 = new Vector();
    DefaultTableModel model2 = null;

    Vector headers3 = new Vector();
    Vector data3 = new Vector();
    DefaultTableModel model3 = null;

    public Vista() {
        initComponents();
    }

    private void analisis() {
        taErroresSemanticos.setText("");
        String text = "";
        File program = new File("programa" + ".txt");
        if (program != null) {
            FileReader Fichero = null;
            try {
                taEditor.setText("");
                Fichero = new FileReader(program);
                BufferedReader read = new BufferedReader(Fichero);
                while ((text = read.readLine()) != null) {
                    taEditor.append(text + "\n");
                }
                read.close();
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }

        Lexico.Inicio inicioAnalisis;
        Semantico.Analisis s;
        try {
            ////////////////////////////////////////////////////////////////////
            //LÉXICO
            ////////////////////////////////////////////////////////////////////
            inicioAnalisis = new Lexico.Inicio();
            inicioAnalisis.analisis();
            inicioAnalisis.ubicar();
            inicioAnalisis.mostrar();
            inicioAnalisis.validar();
            inicioAnalisis.guardar();

            Cambiar cambiar = new Cambiar();
            cambiar.clearFile();
            cambiar.readFile("programa.txt");

            File file = new File("Generado/" + "Léxico" + ".xls");
            fillLexico(file);
            model = new DefaultTableModel(data, headers);
            tableWidth = model.getColumnCount() * 150;
            tableHeight = model.getRowCount() * 25;
            tbLexico.setModel(model);

            ////////////////////////////////////////////////////////////////////
            //SEMÁNTICO
            ////////////////////////////////////////////////////////////////////
            s = new Semantico.Analisis(inicioAnalisis.tabla, inicioAnalisis.lexema, inicioAnalisis.x, inicioAnalisis.y);
            // s.getVariablesGlobales();
            s.getTipos();
            s.getErrores();
            s.proceso();
            s.Mostrar();
            s.mostrar2();
            s.mostrarErrores();
            GuardarTS ts = new GuardarTS(s.table);
            ts.Generar();
            ts.Generar2();

            File f = new File("Generado/" + "Errores" + ".txt");
            AText a;
            a = new AText(f, true);
            for (String arg : s.fails) {
                a.Guardar(arg + "\n");
                taErroresSemanticos.append(arg + "\n");
            }
            a.Cerrar();

            cambiar.readFile2("Resultados/Léxico.txt", "Resultados/Léxico.txt");
            cambiar.readFile2("Resultados/Semántico.txt", "Resultados/Semántico.txt");

            File f2 = new File("Resultados/" + "Errores" + ".txt");
            a = new AText(f2, true);
            for (String arg : s.fails) {
                a.Guardar(arg + "\n");
            }
            a.Cerrar();

//            headers.clear();
//            data.clear();
//            model = null;
            File file2 = new File("Generado/" + "Semántico" + ".xls");
            fillSemantico(file2);
            model2 = new DefaultTableModel(data2, headers2);
            tableWidth = model2.getColumnCount() * 150;
            tableHeight = model2.getRowCount() * 25;
            tbSemantico.setModel(model2);

            ////////////////////////////////////////////////////////////////////
            //CODIGO INTERMEDIO
            ////////////////////////////////////////////////////////////////////
            TraductorEtiquetas traductor = new TraductorEtiquetas(inicioAnalisis.tabla, inicioAnalisis.lexema);
            traductor.iniciar();

            File file3 = new File("Generado/" + "Tabla_CI" + ".xls");
            fillCuadruples(file3);
            model3 = new DefaultTableModel(data3, headers3);
            tableWidth = model3.getColumnCount() * 150;
            tableHeight = model3.getRowCount() * 25;
            tbCodigoIntermedio.setModel(model3);

            cambiar.readFile2("Resultados/CodigoIntermedio.txt", "Resultados/CodigoIntermedio.txt");
            File file4 = new File("Resultados/" + "CodigoIntermedio" + ".txt");
            fillCodigoIntermedio(file4);

            ////////////////////////////////////////////////////////////////////
            //OPTIMIZACIÓN
            ////////////////////////////////////////////////////////////////////
            Analizador analizador = new Analizador(traductor.getCuadruples());
            analizador.despliegue();
            analizador.inicio();

            File file5 = new File("Resultados/" + "Optimizado" + ".txt");
            a = new AText(file5, true);
            ArrayList<Cuadruple> arg = analizador.tabla;
            DefaultTableModel modelow = (DefaultTableModel) tbCodigoIntermedio1.getModel();
            for (Cuadruple arg1 : arg) {
                modelow.addRow(new Object[]{
                    arg1.op1,
                    arg1.op2,
                    arg1.op,
                    arg1.r
                });
            }

            tbCodigoIntermedio1.setModel(modelow);

            a.Cerrar();

            pnlAnalisis.setSelectedIndex(3);

        } catch (IOException ex) {
            Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fillLexico(File file) {
        Workbook workbook;
        try {
            try {
                workbook = Workbook.getWorkbook(file);
                Sheet sheet = workbook.getSheet(0);

                headers.clear();
                for (int i = 0; i < sheet.getColumns(); i++) {
                    Cell cell1 = sheet.getCell(i, 0);
                    headers.add(cell1.getContents());
                }

                data.clear();
                for (int j = 1; j < sheet.getRows(); j++) {
                    Vector d = new Vector();
                    for (int i = 0; i < sheet.getColumns(); i++) {
                        Cell cell = sheet.getCell(i, j);
                        d.add(cell.getContents());
                    }
                    d.add("\n");
                    data.add(d);
                }
            } catch (IOException ex) {
                System.out.println("Error de lectura");
            }
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

    private void fillSemantico(File file) {
        Workbook workbook;
        try {
            try {
                workbook = Workbook.getWorkbook(file);
                Sheet sheet = workbook.getSheet(0);

                headers2.clear();
                for (int i = 0; i < sheet.getColumns(); i++) {
                    Cell cell1 = sheet.getCell(i, 0);
                    headers2.add(cell1.getContents());
                }

                data2.clear();
                for (int j = 1; j < sheet.getRows(); j++) {
                    Vector d = new Vector();
                    for (int i = 0; i < sheet.getColumns(); i++) {
                        Cell cell = sheet.getCell(i, j);
                        d.add(cell.getContents());
                    }
                    d.add("\n");
                    data2.add(d);
                }
            } catch (IOException ex) {
                System.out.println("Error de lectura");
            }
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

    private void fillCuadruples(File file) {
        Workbook workbook;
        try {
            try {
                workbook = Workbook.getWorkbook(file);
                Sheet sheet = workbook.getSheet(0);

                headers3.clear();
                for (int i = 0; i < sheet.getColumns(); i++) {
                    Cell cell1 = sheet.getCell(i, 0);
                    headers3.add(cell1.getContents());
                }

                data3.clear();
                for (int j = 1; j < sheet.getRows(); j++) {
                    Vector d = new Vector();
                    for (int i = 0; i < sheet.getColumns(); i++) {
                        Cell cell = sheet.getCell(i, j);
                        d.add(cell.getContents());
                    }
                    d.add("\n");
                    data3.add(d);
                }
            } catch (IOException ex) {
                System.out.println("Error de lectura");
            }
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

    private void fillCodigoIntermedio(File file) {
        taCodigoIntermedio.setText("");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(file));
            String str;
            while ((str = in.readLine()) != null) {
                taCodigoIntermedio.append(str + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                in.close();
            } catch (Exception ex) {
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        pnl = new javax.swing.JPanel();
        spEditor = new javax.swing.JScrollPane();
        taEditor = new javax.swing.JTextArea();
        pnlAnalisis = new javax.swing.JTabbedPane();
        pnlLexico = new javax.swing.JScrollPane();
        tbLexico = new javax.swing.JTable();
        pnlSemantico1 = new javax.swing.JPanel();
        pnlSemantico = new javax.swing.JScrollPane();
        tbSemantico = new javax.swing.JTable();
        pnlSemantico2 = new javax.swing.JScrollPane();
        taErroresSemanticos = new javax.swing.JTextArea();
        pnlCodigoIntermedio = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbCodigoIntermedio = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        taCodigoIntermedio = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbCodigoIntermedio1 = new javax.swing.JTable();
        barraMenu = new javax.swing.JMenuBar();
        mnuArchivo = new javax.swing.JMenu();
        itmAbrir = new javax.swing.JMenuItem();
        itmGuardarCodigo = new javax.swing.JMenuItem();
        itmSalir = new javax.swing.JMenuItem();
        itmAbrirTest = new javax.swing.JMenuItem();
        mnuAnalisis = new javax.swing.JMenu();
        itmSemantico = new javax.swing.JMenuItem();
        mnuAyuda = new javax.swing.JMenu();
        itmAyuda = new javax.swing.JMenuItem();
        itmAcerca = new javax.swing.JMenuItem();

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        jMenuItem6.setText("jMenuItem6");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(Strings.name);
        addWindowFocusListener(new java.awt.event.WindowFocusListener() {
            public void windowGainedFocus(java.awt.event.WindowEvent evt) {
                formWindowGainedFocus(evt);
            }
            public void windowLostFocus(java.awt.event.WindowEvent evt) {
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        taEditor.setColumns(20);
        taEditor.setRows(5);
        spEditor.setViewportView(taEditor);

        tbLexico.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null}
            },
            new String [] {
                "Lexema", "Etiqueta"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbLexico.setEnabled(false);
        pnlLexico.setViewportView(tbLexico);

        pnlAnalisis.addTab("tab1", pnlLexico);

        tbSemantico.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "1", "2", "3", "4"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbSemantico.setEnabled(false);
        pnlSemantico.setViewportView(tbSemantico);

        taErroresSemanticos.setColumns(20);
        taErroresSemanticos.setRows(5);
        pnlSemantico2.setViewportView(taErroresSemanticos);

        javax.swing.GroupLayout pnlSemantico1Layout = new javax.swing.GroupLayout(pnlSemantico1);
        pnlSemantico1.setLayout(pnlSemantico1Layout);
        pnlSemantico1Layout.setHorizontalGroup(
            pnlSemantico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSemantico1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlSemantico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlSemantico, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(pnlSemantico2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlSemantico1Layout.setVerticalGroup(
            pnlSemantico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlSemantico1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlSemantico, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlSemantico2, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlAnalisis.addTab("tab2", pnlSemantico1);

        tbCodigoIntermedio.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tbCodigoIntermedio);

        taCodigoIntermedio.setColumns(20);
        taCodigoIntermedio.setRows(5);
        jScrollPane2.setViewportView(taCodigoIntermedio);

        javax.swing.GroupLayout pnlCodigoIntermedioLayout = new javax.swing.GroupLayout(pnlCodigoIntermedio);
        pnlCodigoIntermedio.setLayout(pnlCodigoIntermedioLayout);
        pnlCodigoIntermedioLayout.setHorizontalGroup(
            pnlCodigoIntermedioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCodigoIntermedioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlCodigoIntermedioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlCodigoIntermedioLayout.setVerticalGroup(
            pnlCodigoIntermedioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCodigoIntermedioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlAnalisis.addTab("tab3", pnlCodigoIntermedio);

        tbCodigoIntermedio1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Operador1", "Operador2", "Operando", "Resultado"
            }
        ));
        jScrollPane4.setViewportView(tbCodigoIntermedio1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 526, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlAnalisis.addTab("tab4", jPanel1);

        javax.swing.GroupLayout pnlLayout = new javax.swing.GroupLayout(pnl);
        pnl.setLayout(pnlLayout);
        pnlLayout.setHorizontalGroup(
            pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(spEditor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlAnalisis, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlLayout.setVerticalGroup(
            pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlAnalisis)
                    .addComponent(spEditor)))
        );

        mnuArchivo.setText("Archivo");

        itmAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        itmAbrir.setText("Abrir código");
        itmAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmAbrirActionPerformed(evt);
            }
        });
        mnuArchivo.add(itmAbrir);

        itmGuardarCodigo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        itmGuardarCodigo.setText("Guardar código");
        itmGuardarCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmGuardarCodigoActionPerformed(evt);
            }
        });
        mnuArchivo.add(itmGuardarCodigo);

        itmSalir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        itmSalir.setText("Salir");
        itmSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmSalirActionPerformed(evt);
            }
        });
        mnuArchivo.add(itmSalir);

        itmAbrirTest.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        itmAbrirTest.setText("Abrir test");
        itmAbrirTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmAbrirTestActionPerformed(evt);
            }
        });
        mnuArchivo.add(itmAbrirTest);

        barraMenu.add(mnuArchivo);

        mnuAnalisis.setText("Análisis");

        itmSemantico.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        itmSemantico.setText("Análizar");
        itmSemantico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmSemanticoActionPerformed(evt);
            }
        });
        mnuAnalisis.add(itmSemantico);

        barraMenu.add(mnuAnalisis);

        mnuAyuda.setText("Ayuda");

        itmAyuda.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        itmAyuda.setText("Ayuda");
        itmAyuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmAyudaActionPerformed(evt);
            }
        });
        mnuAyuda.add(itmAyuda);

        itmAcerca.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        itmAcerca.setText("Acerca");
        itmAcerca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itmAcercaActionPerformed(evt);
            }
        });
        mnuAyuda.add(itmAcerca);

        barraMenu.add(mnuAyuda);

        setJMenuBar(barraMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void itmSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itmSalirActionPerformed
        System.exit(EXIT_ON_CLOSE);
    }//GEN-LAST:event_itmSalirActionPerformed

    private void itmGuardarCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itmGuardarCodigoActionPerformed
        BufferedWriter escribe;
        try {
            JFileChooser file = new JFileChooser(System.getProperty("user.dir"));
            file.showSaveDialog(this);
            File archivo = file.getSelectedFile();
            if (archivo != null) {
                escribe = new BufferedWriter(new FileWriter(archivo + ".txt"));
                String[] data = taEditor.getText().split("\n");
                for (String data1 : data) {
                    escribe.write(data1);
                    escribe.newLine();
                }
                escribe.close();
            } else {
                JOptionPane.showMessageDialog(this, "El archivo no se guardo.");
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }//GEN-LAST:event_itmGuardarCodigoActionPerformed

    private void itmAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itmAbrirActionPerformed
        String text = "";
        JFileChooser file = new JFileChooser(System.getProperty("user.dir"));
        file.showOpenDialog(this);
        File archivo = file.getSelectedFile();
        if (archivo != null) {
            FileReader Fichero = null;
            try {
                taEditor.setText("");
                Fichero = new FileReader(archivo);
                BufferedReader read = new BufferedReader(Fichero);
                while ((text = read.readLine()) != null) {
                    taEditor.append(text + "\n");
                }
                read.close();
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }
    }//GEN-LAST:event_itmAbrirActionPerformed

    private void itmAyudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itmAyudaActionPerformed
        new Ayuda().setVisible(true);
    }//GEN-LAST:event_itmAyudaActionPerformed

    private void itmAcercaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itmAcercaActionPerformed
        new Acerca().setVisible(true);
    }//GEN-LAST:event_itmAcercaActionPerformed

    private void itmAbrirTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itmAbrirTestActionPerformed
        String text = "";
        File archivo = new File("test.txt");
        if (archivo != null) {
            FileReader Fichero = null;
            try {
                taEditor.setText("");
                Fichero = new FileReader(archivo);
                BufferedReader read = new BufferedReader(Fichero);
                while ((text = read.readLine()) != null) {
                    taEditor.append(text + "\n");
                }
                read.close();
            } catch (IOException ioe) {
                System.out.println(ioe);
            }
        }

        itmSemanticoActionPerformed(evt);
    }//GEN-LAST:event_itmAbrirTestActionPerformed

    private void itmSemanticoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itmSemanticoActionPerformed
        analisis();
    }//GEN-LAST:event_itmSemanticoActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        Cambiar cambiar = new Cambiar();
        try {
            cambiar.clearFile();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Vista.class.getName()).log(Level.SEVERE, null, ex);
        }

//        analisis();
    }//GEN-LAST:event_formWindowOpened

    private void formWindowGainedFocus(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowGainedFocus
//        analisis();
    }//GEN-LAST:event_formWindowGainedFocus

    public static void main(String args[]) {
        new Vista().setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar barraMenu;
    private javax.swing.JMenuItem itmAbrir;
    private javax.swing.JMenuItem itmAbrirTest;
    private javax.swing.JMenuItem itmAcerca;
    private javax.swing.JMenuItem itmAyuda;
    private javax.swing.JMenuItem itmGuardarCodigo;
    private javax.swing.JMenuItem itmSalir;
    private javax.swing.JMenuItem itmSemantico;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JMenu mnuAnalisis;
    private javax.swing.JMenu mnuArchivo;
    private javax.swing.JMenu mnuAyuda;
    private javax.swing.JPanel pnl;
    private javax.swing.JTabbedPane pnlAnalisis;
    private javax.swing.JPanel pnlCodigoIntermedio;
    private javax.swing.JScrollPane pnlLexico;
    private javax.swing.JScrollPane pnlSemantico;
    private javax.swing.JPanel pnlSemantico1;
    private javax.swing.JScrollPane pnlSemantico2;
    private javax.swing.JScrollPane spEditor;
    private javax.swing.JTextArea taCodigoIntermedio;
    private javax.swing.JTextArea taEditor;
    private javax.swing.JTextArea taErroresSemanticos;
    private javax.swing.JTable tbCodigoIntermedio;
    private javax.swing.JTable tbCodigoIntermedio1;
    private javax.swing.JTable tbLexico;
    private javax.swing.JTable tbSemantico;
    // End of variables declaration//GEN-END:variables
}
