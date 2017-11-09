/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lexico;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import jxl.format.Alignment;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.Number;
import jxl.write.NumberFormats;
import jxl.write.WriteException;

/**
 *
 * @author Unico
 */
public class Guardar {

    ArrayList<String> identificador;
    ArrayList<String> lexema;
    ArrayList<String> descripcion;
    ArrayList<String> x, y;

    public Guardar(ArrayList<String> tabla, ArrayList<String> lexema, ArrayList<String> describe, ArrayList<String> x, ArrayList<String> y) {
        this.identificador = tabla;
        this.lexema = lexema;
        this.descripcion = describe;
        this.x = x;
        this.y = y;
    }

    public String Generar() {
//        String nom = JOptionPane.showInputDialog(null, "Nombre Del Archivo", "Analisis Lexico", JOptionPane.PLAIN_MESSAGE);
        String nom = "Léxico";

        File f = new File("Generado/" + nom + ".xls");
        if (identificador != null) {
            try {
                WritableWorkbook w = Workbook.createWorkbook(f);
                WritableSheet s = w.createSheet("Lexico", 0);

                WritableCellFormat u = new WritableCellFormat();
                WritableCellFormat b = new WritableCellFormat();
                b.setAlignment(Alignment.CENTRE);
//                u.setBackground(jxl.format.Colour.AQUA);
                u.setBackground(jxl.format.Colour.GRAY_25);
                
                Cambiar cambiar = new Cambiar();
//                for (int i = 0; i < identificador.size(); i++) {
//                    System.out.println("identificador" + identificador.get(i));
//                    if (identificador.get(i) != null) {
//                        identificador.get(i) = cambiar.regresar2(identificador.get(i));
//                    }
//                }

                for (int j = 0; j <= identificador.size(); j++) {
                    if (j == 0) {
                        s.addCell(new Label(0, j, "TOKEN", u));
                        s.addCell(new Label(1, j, "LEXEMA", u));
                        s.addCell(new Label(2, j, "FILA", u));
                        s.addCell(new Label(3, j, "COLUMNA", u));
                        s.addCell(new Label(4, j, "DESCRIPCION", u));
                    } else {
                        s.addCell(new Label(0, j, identificador.get(j - 1)));
//                        s.addCell(new Label(1, j, lexema.get(j - 1)));
                        s.addCell(new Label(1, j, cambiar.regresar2((lexema.get(j - 1)))));
                        s.addCell(new Number(2, j, Double.parseDouble(x.get(j - 1))));
                        s.addCell(new Number(3, j, Double.parseDouble(y.get(j - 1))));
                        s.addCell(new Label(4, j, descripcion.get(j - 1)));
                    }
                }
                w.write();
                w.close();
                System.out.println(f.getAbsolutePath());

                Process p = Runtime.getRuntime().exec("cmd /c" + f.getAbsolutePath());

            } catch (WriteException ex) {
                JOptionPane.showMessageDialog(null, "Error de escritura", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                System.out.println(ex);
                JOptionPane.showMessageDialog(null, "Error de entrada\\salida", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return "Archivo Generado como " + nom + ".xls";
        } else {
            return "Fallo al generar informe";
        }
    }

    public String Generar2() {
//        String nom = JOptionPane.showInputDialog(null, "Nombre Del Archivo", "Analisis Lexico", JOptionPane.PLAIN_MESSAGE);
        String nom = "Léxico";
        String archivo = "";
        
        Cambiar cambiar = new Cambiar();

        File f = new File("Resultados/" + nom + ".txt");
        if (identificador != null) {
            for (int j = 0; j <= identificador.size(); j++) {
                if (j == 0) {
                    archivo += "TOKEN \tLEXEMA \tFILA \tCOLUMNA \tDESCRIPCION\n";
                } else {
                    archivo += identificador.get(j - 1)
//                            + "\t" + lexema.get(j - 1)
                            + "\t" + cambiar.regresar2(lexema.get(j - 1))
                            + "\t" + Integer.parseInt(x.get(j - 1))
                            + "\t" + Integer.parseInt(y.get(j - 1))
                            + "\t" + descripcion.get(j - 1)
                            + "\n";
                }
            }

            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(f));
                writer.write(archivo);
            } catch (IOException e) {
            } finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                }
            }
            return "Archivo Generado como " + nom + ".txt";
        } else {
            return "Fallo al generar informe";
        }
    }
}
