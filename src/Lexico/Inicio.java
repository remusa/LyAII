/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lexico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author rms
 */
public class Inicio {

    public static String todosErrores = "";
    Validador validador = new Validador();
    public ArrayList<String> tabla = new ArrayList();
    ArrayList<String> describe = new ArrayList();
    public ArrayList<String> x = new ArrayList();
    public ArrayList<String> y = new ArrayList();
    ArrayList<String> errores = new ArrayList();
    String aux;
    Tokens token;
    Lexemas l;
    Map<String, String> lenTI;//lenguaje Token-identificador
    Map<String, String> lenIT;//lenguaje Identificador-token
    Map<String, String> desc;
    public ArrayList<String> lexema;
    ArrayList<String> linea;

    String I = "NUMERO ENTERO";
    String N = "NUMERO FLOTANTE";
    String T = "VALOR TEXTO";
    String V = "VARIABLE";
    String F = "NOMBRE_FUNCION";
    String C = "COMENTARIO";
    String E = "ERROR";
    String M = "MATRIZ";
    String A = "ARREGLO";
    String MV = "VALOR MATRIZ";
    String AV = "ARREGLO VECTOR";
    String X = "EXPRECION";

    public Inicio() throws IOException {
        token = new Tokens();
//        l = new Lexemas(JOptionPane.showInputDialog(null, "Nombre Del Archivo", "analisis Semantico", JOptionPane.PLAIN_MESSAGE) + ".txt");
        l = new Lexemas("test.txt");
        this.lenIT = token.lenIT;
        this.lenTI = token.lenTI;
        this.desc = token.desc;
        lexema = l.obtenerLexema();
        this.linea = l.lines;
    }
    
    public Inicio(String file) throws IOException {
        token = new Tokens();
        l = new Lexemas(file);
        this.lenIT = token.lenIT;
        this.lenTI = token.lenTI;
        this.desc = token.desc;
        lexema = l.obtenerLexema();
        this.linea = l.lines;
    }

    public ArrayList<String> analisis() {
        Pattern a, b, c, d;

        Matcher e, f, g, h;
        a = Pattern.compile("[a-zA-Z]+[0-9]*\\[\\]");
        b = Pattern.compile("[a-zA-Z]+[0-9]*\\[\\]\\[\\]");
        c = Pattern.compile("[a-zA-Z]+[0-9]*\\[([a-zA-Z]+|[0-9]+)\\]");
        d = Pattern.compile("[a-zA-Z]+[0-9]*\\[([a-zA-Z]+|[0-9]+)\\]\\[([a-zA-Z]+|[0-9]+)\\]");
        for (int i = 0; i < lexema.size(); i++) {

            if (lenTI.containsKey(lexema.get(i).toUpperCase())) {//si es una reservada
                aux = lenTI.get(lexema.get(i).toUpperCase());
                tabla.add(aux);
                describe.add(desc.get(aux).toUpperCase());
            } else if (lexema.get(i).contains("[") && lexema.get(i).contains("]")) {

                e = a.matcher(lexema.get(i));
                f = b.matcher(lexema.get(i));
                g = c.matcher(lexema.get(i));
                h = d.matcher(lexema.get(i));
                
                if (e.matches()) {
                    tabla.add("A");
                    describe.add(A);
                } else if (f.matches()) {
                    tabla.add("M");
                    describe.add(M);
                } else if (g.matches()) {
                    tabla.add("AV");
                    describe.add(AV);
                } else if (h.matches()) {
                    tabla.add("MV");
                    describe.add(MV);
                } else {
                    tabla.add("E");
                    describe.add(E);
                }

            } else if (lexema.get(i).charAt(0) == '(' || (lexema.get(i).split(" ").length > 1 && !lexema.get(i).contains("\""))) {
                tabla.add("X");
                describe.add(X);
            } else if (Character.isDigit(lexema.get(i).charAt(0))) {//si es un digito
                if (lexema.get(i).lastIndexOf(".") == -1) {
                    tabla.add("I");
                    describe.add(I);
                } else {
                    tabla.add("N");
                    describe.add(N);
                }
            } else if (Character.isLetter(lexema.get(i).charAt(0))) {//si es una letra
                if (lexema.get(i - 1).toUpperCase().equals("F")) {
                    tabla.add("F");
                    describe.add(F);
                } else {
                    tabla.add("V");
                    describe.add(V);
                }
            } else if (lexema.get(i).charAt(0) == '\"') {
                aux = lenTI.get("" + '\"').toUpperCase();
                tabla.add("T");
                describe.add(T);

            } else if (lexema.get(i).charAt(0) == '@') {
                aux = lenTI.get("@");
                tabla.add(aux);
                describe.add(desc.get(aux).toUpperCase());

            } else if (lexema.get(i).charAt(0) == '/' || lexema.get(i).charAt(0) == '#') {
                tabla.add(i, "C");
                describe.add(i, C);
            } else {
                tabla.add("E");
                describe.add(E);
            }

        }
        return tabla;
    }

    public void mostrar() {
//        System.out.println("-------------ANALISIS-------------");
//        System.out.println("ttt " + tabla.size() + " xx " + x.size() + " yy " + x.size());
//        for (int i = 0; i < lexema.size(); i++) {
//            System.out.println(tabla.get(i) + " " + lexema.get(i) + " " + " " + y.get(i) + " " + describe.get(i));
//        }
    }

    public void guardar() {
        Guardar g = new Guardar(tabla, lexema, describe, x, y);
        g.Generar();
        g.Generar2();
    }

    public void ubicar() {//ubica cada lexema
        int i = 0;
        int j = 0;
        int k;
        while (i < linea.size()) {

            while (j < lexema.size() && linea.get(i).contains(lexema.get(j).split(" ")[0])) {
                x.add("" + (i + 1));
                k = linea.get(i).indexOf(lexema.get(j));
                if (k > -1) {
                    y.add("" + k);
                } else {
                    y.add("0");
                }

                j++;
            }

            i++;
        }
//        System.out.println("xxx "+x.size());
//        System.out.println("yyyy "+y.size());
    }

    public void validar() {
        int p;
        for (int i = 0; i < tabla.size(); i++) {

            switch (tabla.get(i)) {
                case "V":
                    if (!validador.Variables(lexema.get(i))) {
                        errores.add("" + i);
                    }
                    break;
                case "I":
                    if (!validador.Numenteros(lexema.get(i))) {
                        errores.add("" + i);
                    }
                    break;
                case "N":
                    if (!validador.Numflotantes(lexema.get(i))) {
                        errores.add("" + i);
                    }
                    break;
            }
        }
        System.out.println("----------------------------ERRORES LEXICOS----------------------------");
        todosErrores += "\n----------------------------ERRORES LEXICOS----------------------------";
        for (String e : errores) {
            p = Integer.parseInt(e);
            System.out.println("Error de sintaxis en: " + lexema.get(p) + " Fila: " + x.get(p) + " Columna: " + y.get(p));
            todosErrores += "\nError: " + lexema.get(p) + " Fila: " + x.get(p) + " Columna: " + y.get(p);
        }
        todosErrores += "\n----------------------------ERRORES LEXICOS----------------------------";
        
    }

}
