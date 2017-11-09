/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CodigoIntermedio;

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
import jxl.write.WriteException;

/**
 *
 * @author rms
 */
public class GuardarCI {

    ArrayList<Cuadruple> identificador;
    File f;
    File f2;
    Cuadruple bin;

    public GuardarCI(ArrayList<Cuadruple> tabla, String nombre) {
        this.identificador = tabla;
        f = new File("Generado/" + nombre + ".xls");
        f2 = new File("Resultados/" + "Cuadruples" + ".txt");
    }

    public String Generar() {
        if (identificador != null) {
            try {
                WritableWorkbook w = Workbook.createWorkbook(f);
                WritableSheet s = w.createSheet("CodigoI", 0);
                WritableCellFormat u = new WritableCellFormat();
                WritableCellFormat b = new WritableCellFormat();
                b.setAlignment(Alignment.CENTRE);
                u.setBackground(jxl.format.Colour.AQUA);
                s.addCell(new Label(0, 0, "Operador1", u));
                s.addCell(new Label(1, 0, "Operador2", u));
                s.addCell(new Label(2, 0, "Operando", u));
                s.addCell(new Label(3, 0, "Resultado", u));
//                s.addCell(new Label(4, 0, "Resultado Negativo", u));

                int i = 1;

                for (Cuadruple id : identificador) {
                    s.addCell(new Label(0, i, id.op1));
                    s.addCell(new Label(1, i, id.op2));
                    s.addCell(new Label(2, i, id.op));
                    s.addCell(new Label(3, i, id.r));
//                    s.addCell(new Label(4, i, id.rn));
                    i++;
                }
                w.write();
                w.close();
                System.out.println("PATH: " + f.getAbsolutePath());

                Process p = Runtime.getRuntime().exec("cmd /c" + f.getAbsolutePath());

            } catch (WriteException ex) {
                JOptionPane.showMessageDialog(null, "Error de escritura", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                System.out.println(ex);
                JOptionPane.showMessageDialog(null, "Error de entrada\\salida", "Error", JOptionPane.ERROR_MESSAGE);
            }
            return "Archivo Generado";
        } else {
            return "Fallo al generar informe";
        }
    }

    public String Generar2() {
        String nom = "Cuadruples";
        String archivo = "";

        File f = new File("Resultados/" + nom + ".txt");

        archivo += "Operador1\t"
                + "Operador2\t"
                + "Operando\t"
                + "Resultado\t"
                //                + "Resultado Negativo\t"
                + "\n";

        for (Cuadruple id : identificador) {
            archivo += id.op1 + "\t"
                    + id.op2 + "\t"
                    + id.op + "\t"
                    + id.r + "\t"
                    //                    + id.rn + "\t"
                    + "\n";
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
