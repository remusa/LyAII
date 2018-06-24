/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CodigoIntermedio;

import Postfijo.Postfijo;
import Semantico.Analisis;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rms
 */
public class TraductorEtiquetas {

    String lexSwitch;
    CodigoIntermedio codigo = new CodigoIntermedio("Codigo Internedio");
    ArrayList<String> lexema;
    ArrayList<String> token;
    ArrayList<String> temp = new ArrayList();
    String lex;
    int et = 0;
    int ef = 0;
    int t = 0;
    int aux = 0;
    int Eaux = 0;
    public Map<String, String> nombresF = new HashMap<>();

    public TraductorEtiquetas(ArrayList<String> token, ArrayList<String> lexema) { //lista tokens (que son), lexema tipo variables
        this.lexema = lexema;
        this.token = token;
    }

    public int getE() {
        et += 10;
        if ((et % 100 == 0)) {
            return et += 10;
        }
        return et;
    }

    public int getF() {
        return ef += 100;
    }

    public String getT() {
        t++;
        return "T" + t;
    }
    //puede generar errores

    public ArrayList<String> generarPostfijo(ArrayList<String> list) {
        int i = 0;
        String[] a;
        ArrayList<String> l = new ArrayList();
        if (list.get(i).contains("\"") || !list.get(i).contains(" ")) {//si es un valor texto
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
        //  System.out.println("LISTA" + lex);
        Postfijo p = null;
        try {
            p = new Postfijo(l);
        } catch (IOException ex) {
            Logger.getLogger(Analisis.class.getName()).log(Level.SEVERE, null, ex);
        }

        return p.getPosfijo();
    }

    public ArrayList<String> evaluacionPostfijo(ArrayList<String> ex) {
        System.out.println("POSTFIJO: " + ex);
        String resul = null;
        String tipo = null;

        //   [d, a, c, *, =]
        for (int i = 0; i < ex.size(); i++) {

            if (ex.get(i).equals("*")
                    || ex.get(i).equals("+")
                    || ex.get(i).equals("/")
                    || ex.get(i).equals("-")
                    || ex.get(i).equals("=")) {
                String op = ex.get(i);
                String b = ex.get(i - 1);
                String a = ex.get(i - 2);
                System.out.println("A: " + a + " " + op + " B: " + b);
                i = i - 2;
                ex.remove(i);
                ex.remove(i);
                ex.remove(i);
                ex.add(i, codigo.setTemp(this.getT(), a, op, b));
            }
        }
        System.out.println("Final " + ex);

        return ex;
    }

    public ArrayList<String> lexemasOpMatematico() {
        temp.clear();
        while (aux < lexema.size() && (!lexema.get(aux).equals(")"))) {
            //  System.out.println("LLEX" + lexema.get(aux));
            temp.add(lexema.get(aux));
            aux++;
        }
        //  System.out.println("ORIGINAl " + temp);
        ArrayList<String> ex = this.generarPostfijo(temp);

        return ex;
    }

    public ArrayList<String> lexemas() {
        temp.clear();
        while (aux < lexema.size()
                && (!lexema.get(aux).equals(";")
                && !lexema.get(aux).equals(",")
                && !lexema.get(aux).equals("{")
                && !lexema.get(aux).equals(":"))) {
            //System.out.println("LEXEMAS" + lexema.get(aux));
            temp.add(lexema.get(aux));
            aux++;
        }
        // System.out.println("ORIGINAl " + temp);
        ArrayList<String> ex = this.generarPostfijo(temp);

        return ex;
    }

    public int expresionLogica(int EV, int EF, ArrayList<String> temp, int i) {
        System.out.println("TEMP: " + temp);

        switch (temp.get(i).toUpperCase()) {
            case "AND": {
                //System.out.println("lllllllllll " + temp);
                int E1V = this.getE();
                int E1F = EF;
                int E2V = EV;
                int E2F = EF;
                i--;
                i = expresionLogica(E1V, E1F, temp, i);
                codigo.setE("" + E1V);
                expresionLogica(E2V, E2F, temp, i);
                temp.remove(i);
                break;
            }

            case "OR": {
                //temp.remove(i);
                int E1V = EV;
                int E1F = this.getE();
                int E2V = EV;
                int E2F = EF;
                i--;
                i = expresionLogica(E1V, E1F, temp, i);
                codigo.setE("" + E1F);
                expresionLogica(E2V, E2F, temp, i);
                temp.remove(i);
                break;
            }

            case "NOT": {
                int E1V = EF;
                int E1F = EV;
                temp.remove(i);
                i--;
                i = expresionLogica(E1V, E1F, temp, i);
                break;
            }

            default:
                if (i > 1) {
                    i--;
                    this.expresionLogica(EV, EF, temp, i);
                } else {
                    codigo.setIf(temp.get(0), temp.get(2), temp.get(1), "" + EV, "" + EF);
                    temp.remove(0);
                    temp.remove(0);
                    temp.remove(0);

                    return temp.size() - 1;
                }
                break;
        }
        return 0;
    }

    public int condicionLogica(int EV, int EF) {
        ArrayList<String> tempAux = evaluacionPostfijo(lexemas());
        //System.out.println("tempAux " + tempAux);
        this.expresionLogica(EV, EF, tempAux, tempAux.size() - 1);
        return 0;
    }

    public int sentencia() { //inicio de la traducción
        for (aux = aux; aux < token.size(); aux++) {
            lex = token.get(aux);
            //System.out.println(lex + " lex " + lexema.get(aux));
            switch (lex) {
                case "26": //cerrar llave
                    return 0;

                case "F": //funcion
                    codigo.setSpace();
                    int ax = aux;
                    String eli = "";
                    String nomf = lexema.get(ax);
                    ax++;
                    while (!lexema.get(ax).equals(";") && !lexema.get(ax).equals("{")) {
                        eli += lexema.get(ax);
                        ax++;
                    }
                    if (lexema.get(ax).equals(";")) {
                        System.out.println("------>LLAMADA FUNCION ");
                        int eti = this.getF();
                        nombresF.put(nomf, "" + eti);
                        codigo.setFunc("" + eti, eli);
                        aux = ax;
                        //System.out.println("TIPO LLAMADA");
                    } else {
                        System.out.println("------>FUNCION ");
                        // System.out.println("TIPO FUNCION");
                        codigo.setE(nombresF.get(lexema.get(aux)));
                        aux++;
                        this.sentencia();
                        codigo.setReturn();
                    }
                    break;

                case "1": //opMat
                    codigo.setSpace();
                    String tempo1;
                    System.out.println("-----------OPMAT---------");
                    aux++;

                    while (!lexema.get(aux).equals(";")) {
                        if (!lexema.get(aux).equals("+")) {
                            if (lexema.get(aux).equals("(")) {
                                aux++;
                                tempo1 = this.evaluacionPostfijo(this.lexemasOpMatematico()).get(0);

                                System.out.println(codigo.setPrint(tempo1));
                            } else {
                                System.out.println(codigo.setPrint(lexema.get(aux)));
                            }
                        }
                        aux++;
                    }
                    break;

                case "8": //while
                    codigo.setSpace();
                    int EIw = this.getE();
                    int EVw = this.getE();
                    int EFw = this.getE();
                    System.out.println("--------WHILE-------------");
                    System.out.println("EIN: " + EIw + " EV: " + EVw + " EF: " + EFw);
                    aux += 1;
                    codigo.setE("" + EIw); //generar Inicio
                    this.condicionLogica(EVw, EFw);
                    codigo.setE("" + EVw); //generar EV
                    this.sentencia();
                    codigo.setGoE(EIw);//goto inicio
                    codigo.setE("" + EFw); //generar EV
                    break;

                case "7": //if
                    codigo.setSpace();
                    int EV = this.getE(); //inicio -> todos los valores iguales
                    int EF = this.getE();
                    int ES = this.getE();
                    System.out.println("--------IF-------------");
                    System.out.println("EV: " + EV + " EF: " + EF + " ESIG: " + ES);
                    aux += 1;               //incrementar contador arreglo lexemas
                    this.condicionLogica(EV, EF);
                    codigo.setE("" + EV); //generar EV
                    this.sentencia();
                    codigo.setGoE(ES);//goto a una etiqueta en este caso siguiente
                    codigo.setE("" + EF);//generar EF
                    this.sentencia();
                    this.Eaux = ES;
                    break;

                case "35": //else
                    codigo.setSpace();
                    aux += 2;
                    this.sentencia();
                    codigo.setE("" + Eaux);
                    Eaux = 0;
                    break;

                case "10": //for(int
                    codigo.setSpace();
                    aux += 3;
                    String ot = lexema.get(aux);
                    aux += 2;
                    ArrayList<String> tempo = evaluacionPostfijo(lexemas());
                    codigo.setVar(ot, "=", tempo.get(0));
                    aux++;
                    int EIf = this.getE();
                    int EVf = this.getE();
                    int EFf = this.getE();
                    codigo.setE("" + EIf);
                    this.condicionLogica(EVf, EFf);
                    codigo.setE("" + EVf);
                    aux += 2;
                    String op = "" + lexema.get(aux).charAt(0);
                    this.sentencia();
                    codigo.setVar(ot, op, "" + 1);
                    codigo.setGoE(EIf);
                    codigo.setE("" + EFf);
                    break;

                case "11"://switch
                    codigo.setSpace();
                    aux += 1;
                    Eaux = this.getE();
                    lexSwitch = evaluacionPostfijo(lexemas()).get(0);
                    this.sentencia();
                    lexSwitch = "";
                    Eaux = 0;
                    break;

                case "12"://casos
                    codigo.setSpace();
                    aux++;

                    if (!lexema.get(aux).equals("{")) {
                        int EVs = this.getE();
                        int EFs = this.getE();
                        codigo.setIf(lexSwitch, "==", lexema.get(aux), "" + EVs, "" + EFs);
                        codigo.setE("" + EVs);
                        this.sentencia();
                        codigo.setGoE(Eaux);
                        codigo.setE("" + EFs);
                    } else {
                        this.sentencia();
                        codigo.setE("" + Eaux);
                    }
                    break;

                case "36"://leer mensaje
                    codigo.setSpace();
                    aux++;
                    codigo.setLectura(lexema.get(aux));
                    break;

                case "V"://variable
                case "A"://vector
                case "M"://matriz
                    codigo.setSpace();
                    if (lexema.get(aux + 1).equals(";") || lexema.get(aux + 1).equals(",") || lexema.get(aux + 1).equals(")")) {
                        System.out.println("----->VARIABLE: " + codigo.setVAR(lexema.get(aux - 1), lexema.get(aux)));
                    } else {
                        String l = lexema.get(aux);
                        aux = aux + 2;
                        ArrayList<String> temp = evaluacionPostfijo(lexemas());
                        System.out.println("----->VARIABLE ASIGNADA: " + codigo.setVar(l, "=", temp.get(0)));
                        // System.out.println("TEMVVVVV" + temp);
                    }
                    break;

                case "MV"://valor matriz
                case "AV"://valor arreglo
                    codigo.setSpace();
                    String a = lexema.get(aux);
                    aux = aux + 2;
                    ArrayList<String> temp9 = evaluacionPostfijo(lexemas());
                    String tempo2 = codigo.setVar(this.getT(), "=", a);
                    codigo.setTemp(this.getT(), tempo2, "=", temp9.get(0));
                    break;
            }
        }

        return 0;
    }

    public void iniciar() {
        this.sentencia();
        codigo.cerrar();
        codigo.BajarTabla();
        codigo.BajarTabla2();
    }

    public ArrayList<Cuadruple> getCuadruples() {
        return codigo.tabla;
    }
}
