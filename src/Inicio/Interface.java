/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inicio;

import CodigoIntermedio.Cuadruple;
import CodigoIntermedio.TraductorEtiquetas;
import Excel.TraductorEn;
import Optimizacion.Analizador;
import Semantico.GuardarTS;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author RSG
 */
public class Interface extends JFrame implements KeyListener {

    Lexico.Inicio i;
    AText a;
    TraductorEtiquetas t;
    Analizador an;

    public Interface() {
        Dimension d;
        getContentPane().setBackground(Color.red);
        d = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) d.getWidth(), (int) d.getHeight());
        this.addKeyListener(this);
    }

    public void Lexico() throws IOException {
        i = new Lexico.Inicio();
        i.analisis();
        i.ubicar();
        i.mostrar();
        i.validar();
        i.guardar();
    }

    public void Semantico() throws IOException {
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
        File f = new File("Generado/" + "FAILS" + ".rtf");
        a = new AText(f, true);
        for (String arg : s.fails) {
            a.Guardar(arg + "\n");
        }
        a.Cerrar();
    }

    public void Intermedio() {
        t = new TraductorEtiquetas(i.tabla, i.lexema);
        t.iniciar();
    }

    public void optimizado() throws IOException {
        an = new Analizador(t.getCuadruples());
        an.despliegue();
        an.inicio();

        File fx = new File("Generado/" + "Optimizado" + ".rtf");
        a = new AText(fx, true);
        for (ArrayList<Cuadruple> arg : an.bloques) {
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
    }

    public void Objeto() {
        ArrayList<Cuadruple> table = new ArrayList();
        for (ArrayList<Cuadruple> b : an.bloques) {
            for (Cuadruple bin : b) {
                table.add(bin);
                System.out.println(" -- " + bin.op1 + " -- " + bin.op2 + " -- " + bin.op + " -- " + bin.r + " -- " + bin.rn);
            }
        }

        TraductorEn ta = new TraductorEn(table);
        ta.init();
    }
    Thread ti = new Thread(new Pruebas());

    public static void main(String[] args) {
        Interface in = new Interface();
        in.setVisible(true);
        in.ti.start();

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("COD: " + e.getKeyCode());
        if (e.getKeyCode() == 10) {
            ti.resume();
        } else {
            ti.suspend();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
