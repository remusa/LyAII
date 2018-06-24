/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Semantico;

import Lexico.Cambiar;
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
public class GuardarTS {

    File f = new File("Generado/" + "Semántico" + ".xls");
    File f2 = new File("Resultados/" + "Semántico" + ".txt");
    String[][] ts;
    WritableWorkbook w;
    WritableSheet s;

    public GuardarTS(String[][] ts) {
        this.ts = ts;
    }

    public String Generar() {
        // String nom = JOptionPane.showInputDialog(null, "Nombre Del Archivo", "Analisis Lexico", JOptionPane.PLAIN_MESSAGE);
        try {
            w = Workbook.createWorkbook(f);
            s = w.createSheet("TS", 0);

            Cambiar cambiar = new Cambiar();
            for (int i = 0; i < ts.length; i++) {
                if (ts[i][1] != null) {
                    ts[i][1] = cambiar.regresar2(ts[i][1]);
                    System.out.println(ts[i][1]);
                }
            }

            WritableCellFormat u = new WritableCellFormat();
            WritableCellFormat b = new WritableCellFormat();
            b.setAlignment(Alignment.CENTRE);
//            u.setBackground(jxl.format.Colour.AQUA);
            u.setBackground(jxl.format.Colour.GREY_25_PERCENT);

            for (int j = 0; j <= ts.length; j++) {
                if (j == 0) {
                    s.addCell(new Label(0, j, "LEXEMA", u));
                    s.addCell(new Label(1, j, "TIPO", u));
                    s.addCell(new Label(2, j, "TOKEN", u));
                    s.addCell(new Label(3, j, "VALOR", u));
                    s.addCell(new Label(4, j, "USO", u));
                    s.addCell(new Label(5, j, "RENGLON", u));
                    s.addCell(new Label(6, j, "COLUMNA", u));
                    s.addCell(new Label(7, j, "TIPO2", u));
                    s.addCell(new Label(8, j, "PARAMETROS", u));
                    s.addCell(new Label(9, j, "FILAS", u));
                    s.addCell(new Label(10, j, "COLUMNAS", u));
                    s.addCell(new Label(11, j, "COLUMNAS", u));

                } else {
                    s.addCell(new Label(0, j, ts[j - 1][0]));
                    s.addCell(new Label(1, j, ts[j - 1][1]));
                    s.addCell(new Label(2, j, ts[j - 1][2]));
                    s.addCell(new Label(3, j, ts[j - 1][3]));
                    s.addCell(new Label(4, j, ts[j - 1][4]));
                    s.addCell(new Label(5, j, ts[j - 1][5]));
                    s.addCell(new Label(6, j, ts[j - 1][6]));
                    s.addCell(new Label(7, j, ts[j - 1][7]));
                    s.addCell(new Label(8, j, ts[j - 1][8]));
                    s.addCell(new Label(9, j, ts[j - 1][9]));
                    s.addCell(new Label(10, j, ts[j - 1][10]));
                    s.addCell(new Label(11, j, ts[j - 1][11]));
                }
            }
            w.write();
            w.close();
            //System.out.println(f.getAbsolutePath());
            //Process p = Runtime.getRuntime().exec("cmd /c" + f.getAbsolutePath());

        } catch (WriteException ex) {
            JOptionPane.showMessageDialog(null, "Error de escritura", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, "Error de entrada\\salida", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return "Archivo Generado como " + "Semántico" + ".xls";
    }

    public String Generar2() {
        String nom = "Semántico";
        String archivo = "";

        File f = new File("Resultados/" + nom + ".txt");
        for (int j = 0; j <= ts.length; j++) {
            if (j == 0) {
                archivo += "LEXEMA\t"
                        + "TIPO\t"
                        + "TOKEN\t"
                        + "VALOR\t"
                        + "RENGLON\t"
                        + "COLUMNA\t"
                        + "TIPO2\t"
                        + "PARAMETROS\t"
                        + "FILAS\t"
                        + "COLUMNAS\t"
                        + "COLUMNAS"
                        + "\n";
            } else {
                if (ts[j - 1][0] != null) {
                    archivo += ts[j - 1][0]
                            + "\t" + ts[j - 1][1]
                            + "\t" + ts[j - 1][2]
                            + "\t" + ts[j - 1][3]
                            + "\t" + ts[j - 1][4]
                            + "\t" + ts[j - 1][5]
                            + "\t" + ts[j - 1][6]
                            + "\t" + ts[j - 1][7]
                            + "\t" + ts[j - 1][8]
                            + "\t" + ts[j - 1][9]
                            + "\t" + ts[j - 1][10]
                            + "\t" + ts[j - 1][11]
                            + "\n";
                }
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
    }
}
