/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Semantico;

import Inicio.AText;
import Lexico.Cambiar;
import static Lexico.Inicio.todosErrores;
import Postfijo.Postfijo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Unico
 */
public class Analisis {

    int param = 0;
    ArrayList<String> def = new ArrayList();
    public ArrayList<String> fails = new ArrayList();
    String vect;
    Pattern patv = Pattern.compile("\\[\\d\\]");//encuentra los numeros de arreglos y vectores
    Matcher mat;
    AText at;
    File f;
    ArrayList<String> lines;
    public Map<String, String> errores = new HashMap<>();
    ArrayList<String> error = new ArrayList();
    public Map<String, String> tipos = new HashMap<>();
    ArrayList<String> x, y, lexema, token;
    public String[][] table;
    int fila;

    public Analisis(ArrayList<String> token, ArrayList<String> lexema, ArrayList<String> x, ArrayList<String> y) throws IOException {
        this.table = new String[lexema.size()][12];
        this.lexema = lexema;
        this.token = token;
        this.x = x;
        this.y = y;
//        for (String line : lines) {
//            System.out.println(line);
//        }
    }

    public void getErrores() throws IOException {
        f = new File("Tablas/errores.txt");
        at = new AText(f, false);
        lines = at.Leer();
        
        for (String line : lines) {
            errores.put(line.substring(0, 3), line.substring(4));
        }
    }

    //2 VI ENTERO
    //3 VB BOOLEAN
    //4 VF FLOTANTE
    //5 VS CADENA
    //6 VC CARACTER
    public ArrayList<String> getVariablesGlobales() {
        //lexema,tipo,token,valor,uso
        String[][] s = new String[lexema.size()][5];
        Map<String, String> globales = new HashMap<>();
        String valor = "";
        for (int i = 0; i < lexema.size(); i++) {
            if (lexema.get(i).equals("{") || lexema.get(i).equals("(")) {
                while (!lexema.get(i).equals("}") || !lexema.get(i).equals("}")) {
                    i++;
                }
            } else if (lexema.get(i).equals("VI") || lexema.get(i).equals("VB") || lexema.get(i).equals("VF") || lexema.get(i).equals("VS") || lexema.get(i).equals("VC")) {
                System.out.println("MOST: " + lexema.get(i) + lexema.get(i + 1));
                globales.put(lexema.get(i + 1), lexema.get(i));
                i++;
                while (!lexema.get(i).equals(";")) {
                    valor = valor + lexema.get(i);
                    i++;
                }
                if (valor.contains("=")) {
                    System.out.println("VALOR: " + valor);

                }
                valor = "";

            }

        }
        return null;
    }

    public void getTipos() throws IOException {
        String[] cad;
        f = new File("Tablas/tipos.txt");
        at = new AText(f, false);
        lines = at.Leer();
        
        for (String line : lines) {
            cad = line.split(" ");
            // System.out.println("" + cad[0] + "--" + cad[1]);
            tipos.put(cad[0], cad[1]);
        }
    }

    public boolean existe(String lexe) {
        for (int i = 0; i < lexema.size(); i++) {
            if (table[i][0] != null && table[i][0].equals(lexe)) {
                return true;
            }
        }
        return false;
    }

    public String[] buscar(String lex) {
        String[] var = new String[2];
        for (int i = 0; i < lexema.size(); i++) {
            if (table[i][0] != null && table[i][0].equals(lex)) {
                var[0] = table[i][1];//tipo
                var[1] = table[i][3];//valor

                return var;

            }
        }
        return null;
    }

    public int funciones(int i) {//recibe el token f
        System.out.println("----------GUARDANDO FUNCION------------");
        System.out.println(lexema.get(i - 1) + " " + lexema.get(i));
        int a = 0;
        int b = fila + 1;
        fila++;

        //lexema,tipo,token,valor,uso,ren,col,tipo2,parametros,filas,columnas /12
        //FORMA F A;
        table[b][0] = lexema.get(i);//lexema
        table[b][1] = lexema.get(i - 1);//tipo
        table[b][2] = token.get(lexema.indexOf(lexema.get(i)));//token
        table[b][3] = "-";//valor
        table[b][4] = "0";//uso
        table[b][5] = x.get(lexema.indexOf(lexema.get(i)));//renglon
        table[b][6] = y.get(lexema.indexOf(lexema.get(i)));//columna
        table[b][7] = "funcion";//tipo2
        table[b][9] = "-";//numero filas
        table[b][10] = "-";//numero columnas
        table[b][11] = "-";//otrpos
        i += 2;//salta F otro(VF
        while (!lexema.get(i).equals("{")) {
            i++;
            if (!lexema.get(i).equals(",") && !lexema.get(i).equals("{")) {
                // System.out.println("rrrrrrrrrrrrrr" + lexema.get(i));
                i = guardarvariables(i);

                // System.out.println("return " + lexema.get(i));
                a++;
                i++;
            }
            // System.out.println("SIg " + lexema.get(i));

        }
        table[b][8] = "" + a;//numero parametros
        param = def.indexOf(table[b][0]);
        //System.err.println(param + "UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU" + table[b][0]);
        String[] otro;
        int c = b;
        int cc = 0, nn;
        boolean f = false;
        if (param >= 0) {
            param++;
            for (int j = 0; j < a; j++) {
                c++;
                otro = this.buscar(table[c][0]);
                // System.err.println(otro[0] + " LINE " + def.get(param));
                if (otro != null && otro[0].equals(def.get(param))) {

                } else if (!def.get(param).equals(";")) {
                    f = true;
                    error.add("107 " + lexema.get(lexema.indexOf(table[b][0])) + " " + x.get(lexema.indexOf(table[b][0])) + " " + y.get(lexema.indexOf(table[b][0])));
                }

                param++;
            }
            cc = param;
            while (cc < def.size() && !def.get(cc).equals(";")) {
                cc++;
            }
            nn = cc - param;
            if (nn > 0) {
                error.add("107 " + lexema.get(lexema.indexOf(table[b][0])) + " " + x.get(lexema.indexOf(table[b][0])) + " " + y.get(lexema.indexOf(table[b][0])));

            }

            if (!f) {
                this.modificarUso(table[b][0]);
            }
        } else {
            error.add("108 " + lexema.get(lexema.indexOf(table[b][0])) + " " + x.get(lexema.indexOf(table[b][0])) + " " + y.get(lexema.indexOf(table[b][0])));
        }

        System.out.println("NUMERO DE PARAMETROS: " + a);
        return i;
    }

    public int guardarvariables(int i) {
        ArrayList<String> var = new ArrayList();
        int a = i;
        //lexema,tipo,token,valor,uso,ren,col,tipo2,parametros,filas,columnas /12
        i--;
        var.add(lexema.get(i));
        System.out.println("----------------GUARDANDO VARIABLE---------------");
        i++;

        while (i < lexema.size() && (!lexema.get(i).equals(";") && !lexema.get(i).equals(",") && !lexema.get(i).equals(")") && !lexema.get(i).equals(":"))) {
            //System.out.println(lexema.get(i));
            var.add(lexema.get(i));
            i++;
        }
        System.out.println("VARIABLE " + var);

        if (var.size() == 2) {//FORMA VI A;
            fila++;
            table[fila][0] = var.get(1);//lexema
            table[fila][1] = var.get(0);//tipo
            table[fila][2] = token.get(lexema.indexOf(lexema.get(a)));//token
            table[fila][3] = "-";//valor
            table[fila][4] = "0";//uso
            table[fila][5] = x.get(lexema.indexOf(lexema.get(a)));;//renglon
            table[fila][6] = y.get(lexema.indexOf(lexema.get(a)));;//columna
            table[fila][7] = "-";//tipo2
            table[fila][8] = "-";//numero parametros
            table[fila][9] = "-";//numero filas
            table[fila][10] = "-";//numero columnas
            table[fila][11] = "-";//otro

        } else if (var.size() == 4) {//FORMA VI A=3;

            fila++;

            table[fila][0] = var.get(1);//lexema
            table[fila][1] = var.get(0);//tipo
            table[fila][2] = token.get(lexema.indexOf(lexema.get(a)));//token
            table[fila][3] = this.eval(var, a);//valor

            table[fila][4] = "0";//uso
            table[fila][5] = x.get(lexema.indexOf(lexema.get(a)));;//renglon
            table[fila][6] = y.get(lexema.indexOf(lexema.get(a)));;//columna
            table[fila][7] = "-";//tipo2
            table[fila][8] = "-";//numero parametros
            table[fila][9] = "-";//numero filas
            table[fila][10] = "-";//numero columnas
            table[fila][11] = "-";//otro  
            if (table[fila][3] != null && table[fila][3].equals("S/R")) {
                fila--;

            }
        }
        return i;

    }

    public void Mostrar() {
        for (int i = 0; i < lexema.size(); i++) {
            for (int j = 0; j < 12; j++) {
                if (table[i][0] != null) {
                    System.out.print(" , " + table[i][j]);
                }
            }
            if (table[i][0] != null) {
                System.out.println("");
            }
        }
    }

    public void mostrar2() {
        for (int i = 0; i < lexema.size(); i++) {
            if (table[i][0] != null && table[i][4].equals("0")) {
                fails.add("VARIABLE: " + table[i][0] + " NO UTILIZADA");
                System.out.println("VARIABLE: " + table[i][0] + " NO UTILIZADA");
            }//uso)

        }
    }

    public ArrayList<String> Posfijo(ArrayList<String> list) {

        int i = 2;
        String[] a;
        ArrayList<String> l = new ArrayList();

        if (list.get(1).equals("=")) {//asignado
            l.add(list.get(0));
            l.add(list.get(1));

        } else {//if
            if (list.contains(">") || list.contains("<") || list.contains("==") || list.contains(">=") || list.contains("<=") || list.contains("!=")) {
                l.add(list.get(0));
                l.add(list.get(1));

            } else {//
                l.add(list.get(1));
                l.add("=");
                i++;
            }
        }

        if (list.get(i).contains("\"")) {//si es un valor texto
            l.add(list.get(i));
        } else {
            a = list.get(i).split(" ");
            for (int j = 0; j < a.length; j++) {
                l.add(a[j]);
            }
        }
        i++;
        while (i < list.size()) {
            l.add(list.get(i));
            i++;
        }
        Postfijo p = null;
        try {
            p = new Postfijo(l);
        } catch (IOException ex) {
            Logger.getLogger(Analisis.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.err.println("LIST " + list);
        System.err.println("ARR N " + l);
        return p.getPosfijo();
    }

    public String eval(ArrayList<String> val, int ubic) {
        System.err.println(val);
        String resul = null;

        String tipo = null;
        ArrayList<String> ex = this.Posfijo(val);

        System.out.println("POSFIJO INICIAL" + ex);
        //   [d, a, c, *, =]
        while (ex.size() > 1) {
            for (int i = 0; i < ex.size(); i++) {

                if (ex.get(i).equals("*") || ex.get(i).equals("+") || ex.get(i).equals("/") || ex.get(i).equals("-") || ex.get(i).equals("OR") || ex.get(i).equals("AND") || ex.get(i).equals(">") || ex.get(i).equals("<") || ex.get(i).equals(">=") || ex.get(i).equals("<=") || ex.get(i).equals("!=") || ex.get(i).equals("=")) {
                    String b = ex.get(i - 1);
                    String a = ex.get(i - 2);
                    String[] busB = new String[2];
                    String[] busA = new String[2];
                    System.out.println("VAR-A: " + a + " VAR-B: " + b);
                    //donde validas vs ox="";
                    if (a.charAt(0) == '\"') {
                        busA[0] = "VS";
                        busA[1] = a;//valor
                    } else if (a.toUpperCase().equals("TRUE") || a.toUpperCase().equals("FALSE")) {
                        busA[0] = "VB";
                        busA[1] = a;//valor
                    } else if (Character.isLetter(a.charAt(0))) {
                        busA = this.buscar(a);
                        if (busA == null || (ex.size() > 3 && busA[1] == null)) {
                            System.err.println("E-VALOR");
                            error.add("103 " + a + " " + x.get(ubic) + " " + y.get(ubic));
                            return "S/R";
                        }

                    } else if (a.contains(".")) {
                        busA[0] = "VF";
                        busA[1] = a;//valor
                    } else {
                        busA[0] = "VI";
                        busA[1] = a;
                    }
//////////////////////////////////////////////7
                    if (b.charAt(0) == '\"') {
                        busB[0] = "VS";
                        busB[1] = b;//valor
                    } else if (b.toUpperCase().equals("TRUE") || b.toUpperCase().equals("FALSE")) {
                        busB[0] = "VB";
                        busB[1] = b;//valor
                    } else if (Character.isLetter(b.charAt(0))) {
                        busB = this.buscar(b);
                        if (busB == null) {
                            error.add("102 " + b + " " + x.get(ubic) + " " + y.get(ubic));
                            return "S/R";
                        } else if (busB[1] == null) {
                            error.add("103 " + b + " " + x.get(ubic) + " " + y.get(ubic));
                            return "S/R";
                        }

                    } else if (b.contains(".")) {
                        busB[0] = "VF";
                        busB[1] = b;//valor
                    } else {
                        busB[0] = "VI";
                        busB[1] = b;
                    }

                    if (busA[0] != null || busB[0] != null) {
                        tipo = this.tipos.get(busA[0] + ex.get(i) + busB[0]);
                        // System.out.println("entra bbbbbbbbb " + tipo + busA[1] + busB[1] + ex.get(i));
                        if (tipo != null) {
                            resul = this.Evaluacion(busA[1], busB[1], ex.get(i));
                            this.modificarUso(a);
                            this.modificarUso(b);
                            System.out.println("RESULTADO " + resul);

                            if (ex.size() > 2) {
                                i = i - 2;
                                ex.remove(i);
                                ex.remove(i);
                                ex.remove(i);
                                ex.add(i, resul);

                                System.out.println("VUELTA  " + ex);
                            }

                        } else {
                            error.add("101 " + a + ex.get(i) + b + " " + x.get(ubic) + " " + y.get(ubic));
                            return "S/R";
                        }
                    }
                } else if (ex.get(i).equals("NOT")) {
                    String c = ex.get(i - 1);
                    String[] aux = new String[2];
                    aux = this.buscar(c);
                    if (aux != null) {
                        if (aux[0].equals("true") || aux[0].equals("false")) {
                            resul = this.Evaluacion(aux[1], "", ex.get(i));
                        } else {
                            error.add("101 " + lexema.get(ubic) + " " + x.get(ubic) + " " + y.get(ubic));

                        }
                    } else {
                        if (c.equals("true") || c.equals("false")) {
                            resul = this.Evaluacion(c, "", ex.get(i));
                        } else {
                            error.add("101 " + lexema.get(ubic) + " " + x.get(ubic) + " " + y.get(ubic));

                        }

                    }
                    System.out.println("RESULTADO " + resul);
                    if (f == null) {
                        error.add("101 " + lexema.get(ubic) + " " + x.get(ubic) + " " + y.get(ubic));
                    } else {
                        i = i - 1;
                        ex.remove(i);
                        ex.remove(i);
                        ex.add(i, resul);
                        System.out.println("VUELTA  " + ex);
                    }

                }
            }

        }

        return resul;
    }

    public String Evaluacion(String a, String b, String op) {
        boolean c, d;
        System.out.println("A: " + a + " " + op + " B: " + b);
        String f = null;
        switch (op) {
            case "+":
                if (!a.contains(".") && !b.contains(".")) {
                    f = "" + (Integer.parseInt(a) + Integer.parseInt(b));
                } else if (a.contains(".") || b.contains(".")) {
                    f = "" + (Double.parseDouble(a) + Double.parseDouble(b));
                } else {
                    f = "" + (a) + "" + (b);
                }
                break;
            case "*":
                if (!a.contains(".") && !b.contains(".")) {
                    f = "" + (Integer.parseInt(a) * Integer.parseInt(b));
                } else {
                    f = "" + (Double.parseDouble(a) * Double.parseDouble(b));
                }
                break;
            case "-":
                if (!a.contains(".") && !b.contains(".")) {
                    f = "" + (Integer.parseInt(a) - Integer.parseInt(b));
                } else {
                    f = "" + (Double.parseDouble(a) - Double.parseDouble(b));
                }
                break;
            case "/":
                if (!a.contains(".") && !b.contains(".")) {
                    f = "" + (Integer.parseInt(a) / Integer.parseInt(b));
                } else {
                    f = "" + (Double.parseDouble(a) / Double.parseDouble(b));
                }
                break;
            case "=":
                f = "" + b;
                break;
            case ">":
                if (!a.contains(".") && !b.contains(".")) {
                    f = "" + (Integer.parseInt(a) > Integer.parseInt(b));
                } else {
                    f = "" + (Double.parseDouble(a) > Double.parseDouble(b));
                }
                break;
            case "<":
                if (!a.contains(".") && !b.contains(".")) {
                    f = "" + (Integer.parseInt(a) < Integer.parseInt(b));
                } else {
                    f = "" + (Double.parseDouble(a) < Double.parseDouble(b));
                }
                break;
            case "<=":
                if (!a.contains(".") && !b.contains(".")) {
                    f = "" + (Integer.parseInt(a) <= Integer.parseInt(b));
                } else {
                    f = "" + (Double.parseDouble(a) <= Double.parseDouble(b));
                }
                break;
            case ">=":
                if (!a.contains(".") && !b.contains(".")) {
                    f = "" + (Integer.parseInt(a) >= Integer.parseInt(b));
                } else {
                    f = "" + (Double.parseDouble(a) >= Double.parseDouble(b));
                }
                break;
            case "!=":
                if (!a.contains(".") && !b.contains(".")) {
                    f = "" + (Integer.parseInt(a) != Integer.parseInt(b));
                } else {
                    f = "" + (Double.parseDouble(a) != Double.parseDouble(b));
                }
                break;
            case "==":
                if (!a.contains(".") && !b.contains(".")) {
                    f = "" + (Integer.parseInt(a) == Integer.parseInt(b));
                } else {
                    f = "" + (Double.parseDouble(a) == Double.parseDouble(b));
                }
                break;
            case "OR":
                if (a.equals("true")) {
                    c = true;
                } else {
                    c = false;
                }
                if (b.equals("true")) {
                    d = true;
                } else {
                    d = false;
                }
                f = "" + (c || d);
                break;
            case "AND":
                if (a.equals("true")) {
                    c = true;
                } else {
                    c = false;
                }
                if (b.equals("true")) {
                    d = true;
                } else {
                    d = false;
                }
                f = "" + (c && d);
                break;
            case "NOT":
                if (a.equals("true")) {
                    c = true;
                } else {
                    c = false;
                }
                f = "" + (!c);
                break;
        }
        return f;

    }

    public void modificarUso(String lexe) {
        for (int i = 0; i < lexema.size(); i++) {
            if (table[i][0] != null && table[i][0].equals(lexe)) {
                if (table[i][4] != null) {
                    table[i][4] = "" + (Integer.parseInt(table[i][4]) + 1);
                }
            }
        }

    }
// error.add("101 " + lexema.get(i) + " " + x.get(i) + " " + y.get(i));

    public int CSF(int i) {
        System.out.println("--------------EVALUANDO CICLO FOR----------------");
        i += 2;
        if (lexema.get(i).equals("VI")) {
            i++;
        } else {
            error.add("101 " + lexema.get(i) + " " + x.get(i) + " " + y.get(i));
        }
        if (!existe(lexema.get(i))) {
            i = guardarvariables(i);
        } else {
            error.add("100 " + lexema.get(i) + " " + x.get(i) + " " + y.get(i));
            while (!lexema.get(i).equals(":")) {
                i++;
            }
        }
        do {

            i = this.CSI(i);
        } while (lexema.get(i).equals(":"));
        return i;
    }

    public int OM(int i) {
        System.out.println("--------------EVALUANDO RETORNO(OM)------------");
        System.out.println(lexema.get(i));
        i++;//salta OM
        while (!lexema.get(i).equals(";")) {
            if (!lexema.get(i).contains("\"") && Character.isLetter(lexema.get(i).charAt(0))) {
                if (lexema.get(i).contains("[") && this.existeV(lexema.get(i), i) != null) {
                    System.out.println("EXISTE: " + lexema.get(i));
                    this.modificarUso(lexema.get(i));
                } else if (existe(lexema.get(i))) {
                    System.out.println("EXISTE: " + existe(lexema.get(i)));
                    this.modificarUso(lexema.get(i));
                } else {
                    error.add("102 " + lexema.get(i) + " " + x.get(i) + " " + y.get(i));
                }
            }
            i++;
        }
        //  System.out.println("ret " + lexema.get(i));
        return i;
    }

    public int CSI(int i) {
        int a = i;
        boolean e = false;
        ArrayList<String> var1 = new ArrayList();
        i++;
        while (i < lexema.size() && !lexema.get(i).equals("{") && !lexema.get(i).equals(":")) {
            // System.out.println("gggggggggggggg " + !this.existe(lexema.get(i)));
            if (Character.isLetter(lexema.get(i).charAt(0)) && !this.existe(lexema.get(i))) {
                error.add("102 " + lexema.get(i) + " " + x.get(i) + " " + y.get(i));
                e = true;
                i++;
            } else {
                var1.add(lexema.get(i));
                i++;
            }

        }
        if (e) {
            return i;
        }
        if (var1.contains(")") && !var1.contains("(")) {
            if (this.existe(var1.get(0)) && this.buscar(var1.get(0))[0].equals("VI")) {
                System.out.println("AUMENTO: TRUE");
            } else {
                error.add("101 " + lexema.get(i - var1.size() - 1) + " " + x.get(i - var1.size() - 1) + " " + y.get(i - var1.size() - 1));
            }
        } else {
            System.out.println("EXPRESION: " + var1);
            this.eval(var1, a);
        }
        //    System.out.println("termino en " + lexema.get(i));
        return i;
    }

    public int value(int i) {
        int a = i;
        System.out.println("------------EVALUANDO EXPRESION------------------");
        ArrayList<String> var = new ArrayList();
        String l;
        if (token.get(i).equals("MV")) {
            vect += "[][]";
            var.add(vect);
            i++;
        } else if (token.get(i).equals("AV")) {
            vect += "[]";
            var.add(vect);
            i++;
        }

        while (i < lexema.size() && (!lexema.get(i).equals(";") && !lexema.get(i).equals(",") && !lexema.get(i).equals(")"))) {
            //System.out.println(lexema.get(i));
            var.add(lexema.get(i));
            i++;
        }
        System.out.println("EXPRECION " + var);
        l = this.eval(var, a);

        return i;

    }

    public int Vector(int i) {
        System.out.println("-----------------GUARDANDO VECTOR(MATRIZ)---------------");

        i--;
        System.out.println(lexema.get(i) + " " + lexema.get(i + 1));
        ArrayList<String> param = new ArrayList();

        fila++;
        table[fila][1] = lexema.get(i);//tipo
        i++;
        table[fila][0] = lexema.get(i);//lexema
        table[fila][2] = token.get(lexema.indexOf(lexema.get(i)));//token
        table[fila][3] = "-";//valor
        table[fila][4] = "1";//uso
        table[fila][5] = x.get(lexema.indexOf(lexema.get(i)));;//renglon
        table[fila][6] = y.get(lexema.indexOf(lexema.get(i)));;//columna
        table[fila][7] = "V/M";//tipo2
        table[fila][8] = "-";//numero parametros
        i += 2;
        mat = patv.matcher(lexema.get(i));
        while (mat.find()) {
            param.add(mat.group());
        }
        if (param.size() == 1) {
            table[fila][9] = param.get(0).substring(1, param.get(0).length() - 1);//numero filas
            table[fila][10] = "-";//numero columnas
        } else {
            table[fila][9] = param.get(0).substring(1, param.get(0).length() - 1);//numero filas
            table[fila][10] = param.get(1).substring(1, param.get(1).length() - 1);//numero columnas
        }
        table[fila][11] = "-";//otro 
        System.out.println("FILAS: " + table[fila][9] + " COLUMNAS: " + table[fila][10]);
        return i;

    }

    public String[] buscarindices(String lex) {

        String[] var = new String[2];
        int x = 0;
        String aux = "";

        for (int i = 0; i < lexema.size(); i++) {
            x = 0;
            while (table[i][0] != null && x < table[i][0].length() && table[i][0].charAt(x) != '[') {
                aux += table[i][0].charAt(x);
                x++;
            }

            if (aux.equals(lex)) {
                var[0] = table[i][9];//filas
                var[1] = table[i][10];//columnas
                //  System.out.println("(((((((((((((((((((((((((((" + lex + " " + var[1]);
                return var;

            } else {
                aux = "";
            }
        }
        return null;

    }

    public String existeV(String v, int a) {
        // System.out.println("String " + v);
        String l;
        String[] aux;
        String vari = "";
        int i = 0;
        while (v.charAt(i) != '[') {
            vari += v.charAt(i);
            i++;
        }
        i = 0;
        String[] ubic = this.buscarindices(vari);
//        for (String ubic1 : ubic) {
//            System.out.println("UBI C: " + ubic1);
//        }
        ArrayList<String> arr = new ArrayList();
        patv = Pattern.compile("\\[\\d*\\w\\]");
        mat = patv.matcher(v);
        while (mat.find()) {
            l = mat.group();
            arr.add(l.substring(1, l.length() - 1));
        }

        for (String arr1 : arr) {
            if (Character.isLetter(arr1.charAt(0))) {
                // System.out.println("arr1 " + arr1);
                aux = this.buscar(arr1);
                // System.out.println("aux " + aux[1]);
                if (aux != null && aux[0].toUpperCase().equals("VI")) {
                    if (Integer.parseInt(aux[1]) < Integer.parseInt(ubic[i])) {
                        i++;
                    } else {
                        error.add("106 " + lexema.get(a) + " " + x.get(a) + " " + y.get(a));
                        return null;
                    }

                } else {
                    error.add("101 " + lexema.get(a) + " " + x.get(a) + " " + y.get(a));

                }
            } else {
                if (Integer.parseInt(arr1) < Integer.parseInt(ubic[i])) {
                    i++;
                } else {//error indexado
                    //  System.err.println("xxxxxxxxxxxxx " + x.get(i));
                    error.add("106 " + lexema.get(a) + " " + x.get(a) + " " + y.get(a));
                    return null;
                }
            }
        }
        if (arr.size() > 1) {
            return vari;

        } else {

            return vari;
        }

    }

    public void proceso() {

        String[] xc;
        String l;
        int f, c;
        ArrayList<String> var;
        //lexema,tipo,token,valor,uso,ren,col,tipo2,parametros,filas,columnas /12
        

        for (int i = 0; i < token.size(); i++) {
            l = token.get(i);
            switch (l) {
                case "V"://variable
                    if (!existe(lexema.get(i)) && (Integer.parseInt(token.get(i - 1)) <= 6)) {
                        i = guardarvariables(i);
                    } else if (existe(lexema.get(i)) && !(Integer.parseInt(token.get(i - 1)) <= 6)) {
                        i = this.value(i);
                    } else {
                        error.add("100 " + lexema.get(i) + " " + x.get(i) + " " + y.get(i));

                    }
                    break;

                case "A"://vector
                case "M"://Matriz
                    if (!existe(lexema.get(i)) && (Integer.parseInt(token.get(i - 1)) < 6)) {//si no existe y tiene un tipo de dato
                        i = this.Vector(i);

                    } else {
                        error.add("105 " + lexema.get(i) + " " + x.get(i) + " " + y.get(i));

                    }

                    break;
                case "MV"://valor matriz
                case "AV"://valor arreglo

                    vect = this.existeV(lexema.get(i), i);
                    //System.out.println("VVVVVVVVVVVVVVVVVVVVVVVv " + vect);
                    if (vect != null) {
                        i = this.value(i);
                    } else {
                        error.add("102 " + lexema.get(i) + " " + x.get(i) + " " + y.get(i));
                        while (!lexema.get(i).equals(";")) {
                            i++;
                        }
                    }
                    break;

                case "F"://funcion
                    f = i;
                    def.add(lexema.get(i));
                    f++;
                    while (!lexema.get(f).equals(";") && !lexema.get(f).equals("{")) {
                        if (Character.isDigit(lexema.get(f).charAt(0)) && lexema.contains(".")) {
                            def.add("VF");
                        } else if (Character.isDigit(lexema.get(f).charAt(0)) && !lexema.contains(".")) {
                            def.add("VI");
                        } else if (Character.isLetter(lexema.get(f).charAt(0))) {
                            xc = this.buscar(lexema.get(f));
                            if (xc != null) {
                                def.add(xc[0]);
                            }
                        }
                        f++;
                    }
                    if (lexema.get(f).equals(";")) {
                        i = f;
                        def.add(lexema.get(f));
                    } else {
                        if (!existe(lexema.get(i))) {
                            i = funciones(i);
                        } else {
                            error.add("104 " + lexema.get(i) + " " + x.get(i) + " " + y.get(i));
                        }
                    }
                    break;

                case "1"://OM O RE
                case "17":
                    i = this.OM(i);
                    break;
                case "8"://while y
                case "7"://if
                    System.out.println("-------------EVALUANDO WHILE(IF)------------");
                    i = this.CSI(i);
                    break;
                case "10"://CSF
                    i = this.CSF(i);
                    break;
                case "35"://CSE
                    i++;
                    break;
                case "11"://switch y
                    i += 2;
                    System.out.println("-------------EVALUANDO SWITCH------------");
                    if (Character.isLetter(lexema.get(i).charAt(0)) && existe(lexema.get(i))) {
                        System.out.println(existe(lexema.get(i)));
                    } else {
                        System.out.println("E-SWITCH" + lexema.get(i));
                        error.add("102 " + lexema.get(i) + " " + x.get(i) + " " + y.get(i));
                    }
                    break;
            }
        }
        for (String v : def) {
            if (!v.equals("VI") && !v.equals("VF") && !v.equals("VB") && !v.equals("VS") && !v.equals("VC") && !v.equals(";")) {
                if (!this.existe(v)) {
                    error.add("108 " + lexema.get(lexema.indexOf(v)) + " " + x.get(lexema.indexOf(v)) + " " + y.get(lexema.indexOf(v)));

                }
            }
        }

        System.out.println("-----------------tabla---------------");
        // this.mostrar();

    }

    public void mostrarErrores() {
        String[] x;
        fails.add("---------------------------------------------");

        todosErrores += "\n----------------------------ERRORES SEMÁNTICOS----------------------------";

        for (String line : error) {
            x = line.split(" ");
            fails.add("ERROR EN: " + x[1] + " FILA: " + x[2] + " COLUMNA: " + x[3] + " " + errores.get(x[0]));
            System.out.println("ERROR EN: " + x[1] + " FILA: " + x[2] + " COLUMNA: " + x[3] + " " + errores.get(x[0]));

            todosErrores += "VARIABLE: " + x[1] + errores.get(x[0]);
        }
        todosErrores += "\n----------------------------ERRORES SEMÁNTICOS----------------------------";
    }
}
