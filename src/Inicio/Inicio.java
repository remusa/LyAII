/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inicio;

import Optimizacion.Analizador;
import CodigoIntermedio.Cuadruple;
import CodigoIntermedio.TraductorEtiquetas;
import Excel.TraductorEn;
import Lexico.Cambiar;
import Semantico.GuardarTS;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Unico
 */
public class Inicio {

    /*
    public static void main(String[] args) throws IOException {

        Lexico.Inicio i = new Lexico.Inicio();
        AText a;
        
        Cambiar cambiar = new Cambiar();
        cambiar.readFile("programa.txt");
        
        i.Anlisis();
        i.ubicar();
        i.Mostrar();
        i.Validar();
        i.Guardar();

        Semantico.Analisis s = new Semantico.Analisis(i.tabla, i.lexema, i.x, i.y);
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
        a = new AText(f, true);
        for (String arg : s.fails) {
            a.Guardar(arg + "\n");
        }
        a.Cerrar();
        
        File f2 = new File("Resultados/" + "Errores" + ".txt");
        a = new AText(f2, true);
        for (String arg : s.fails) {
            a.Guardar(arg + "\n");
        }
        a.Cerrar();
        
        cambiar.readFile2("Resultados/Léxico.txt", "Resultados/Léxico.txt");
        cambiar.readFile2("Resultados/Semántico.txt", "Resultados/Semántico.txt");
        
        ////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////
        //Codigo intermedio
        TraductorEtiquetas t = new TraductorEtiquetas(i.tabla, i.lexema);
        t.init();
        
        //Optimizacion
        Analizador an = new Analizador(t.getCuadruples());
        an.Despliegue();
        an.Inicio();

        File fx = new File("Generado/" + "Optimizado" + ".rtf");
        a = new AText(fx, true);
        for (ArrayList<Bin> arg : an.bloques) {
            for (Cuadruple arg1 : arg) {
                if (!arg1.op1.equals("") && !arg1.op2.equals("")) {
                    a.Guardar(arg1.r + " = " + arg1.op1 + " " + arg1.op + " " + arg1.op2 + "\n");
                } else if (arg1.op1.equals("") && !arg1.op2.equals("")) {
                    a.Guardar(arg1.r + " = " + arg1.op2 + "\n");
                } else if (!arg1.op1.equals("") && arg1.op2.equals("")) {
                    a.Guardar(arg1.r + " = " + arg1.op1 + "\n");

                }
            }

        }
        a.Cerrar();
        
        //Ensamblador
        ArrayList<Bin> table = new ArrayList();
        for (ArrayList<Bin> b : an.bloques) {
            for (Cuadruple bin : b) {
                table.add(bin);
                System.out.println(" -- " + bin.op1 + " -- " + bin.op2 + " -- " + bin.op + " -- " + bin.r + " -- " + bin.rn);
            }
        }

        TraductorEn ta = new TraductorEn(table);
        ta.init();
    }
*/
}
