/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CodigoIntermedio;

import Inicio.AText;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author rms
 */
public class CodigoIntermedio {

    File f;
    File f2;
    AText file;
    AText file2;
    String line;
    ArrayList<Cuadruple> tabla = new ArrayList();

    public CodigoIntermedio(String nom) {
        try {
            f = new File("Generado/" + nom + ".rtf");
            file = new AText(f, true);

            f2 = new File("Resultados/" + "CodigoIntermedio" + ".txt");
            file2 = new AText(f2, true);
        } catch (IOException ex) {
            Logger.getLogger(CodigoIntermedio.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void cerrar() {
        try {
            file.Cerrar();
            System.out.println("PATH: " + f.getAbsolutePath());
            Process p = Runtime.getRuntime().exec("cmd /c" + f.getAbsolutePath());
            BufferedInputStream bf = new BufferedInputStream(p.getInputStream());
            int c = bf.read();

            while (c != -1) {
                System.out.print((char) c);
                c = bf.read();
            }
        } catch (IOException ex) {
            Logger.getLogger(CodigoIntermedio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean setIf(String T1, String O, String T2, String ev, String ef) {
        tabla.add(new Cuadruple(T1, T2, O, ev, ef));
        line = "IF " + T1 + " " + O + " " + T2 + " GOTO " + ev
                + "\nGOTO " + ef;
        return this.Escribir(line);
    }

    public boolean setSpace() {
        tabla.add(new Cuadruple("NUEVO", "BLOQUE", "?", "->", ""));
        line = "";
        this.Escribir(line);
        return true;
    }

    public String setTemp(String T, String op1, String O, String op2) {
        tabla.add(new Cuadruple(op1, op2, O, T, ""));
        line = T + " = " + op1 + " " + O + " " + op2;
        this.Escribir(line);
        return T;
    }

    public String setVar(String op1, String O, String op2) {
        tabla.add(new Cuadruple(op2, "", O, op1, ""));
        line = op1 + " " + O + " " + op2;
        this.Escribir(line);
        return line;
    }

    public String setE(String E) {
        tabla.add(new Cuadruple("", "", "", E, ""));
        line = E + ":";
        this.Escribir(line);
        return E;
    }

    public String setVAR(String TD, String V) {
        tabla.add(new Cuadruple("", "", "", V, ""));
        line = TD + " " + V;
        this.Escribir(line);
        return line;
    }

    public String setPrint(String S) {
        tabla.add(new Cuadruple(S, "", "", "PRINT", ""));
        line = "PRINT " + S;
        this.Escribir(line);
        return line;
    }

    public String setFunc(String num, String valor) {
        tabla.add(new Cuadruple(num, "", "", "CALL", ""));
        line = "CALL " + num + " " + valor;
        this.Escribir(line);
        return line;
    }

    public String setLectura(String lectura) {
        tabla.add(new Cuadruple(lectura, "", "", "READ", ""));
        line = "READ " + lectura;
        this.Escribir(line);
        return "" + lectura;
    }

    public String setReturn() {
        line = "RETORNA";
        this.Escribir(line);
        return line;
    }

    public String setGoE(int E) {
        tabla.add(new Cuadruple("", "", "", "" + E, ""));
        line = "GOTO " + E;

        this.Escribir(line);
        return "" + E;
    }

    public boolean Escribir(String line) {
        try {
            file.Guardar(line + "\n");

            return true;
        } catch (IOException ex) {
            Logger.getLogger(CodigoIntermedio.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public String BajarTabla() {
        GuardarCI t = new GuardarCI(tabla, "Tabla_CI");
        return t.Generar();
    }

    public String BajarTabla2() {
        GuardarCI t = new GuardarCI(tabla, "Cuadruples");
        return t.Generar2();
    }

}
