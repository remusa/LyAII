/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Excel;

import CodigoIntermedio.Cuadruple;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Fany
 */
public class TraductorEn {

    Map<String, String> map = new HashMap();
    ArrayList<Cuadruple> tabla = new ArrayList();
    ArrayList<String> pila = new ArrayList();
    int msj = 0;

    public int getMsj() {
        return msj += 1;
    }
    int z = 0;
    Excel en = new Excel("Ensamblador");

    public TraductorEn(ArrayList<Cuadruple> cuadruple) {
        this.tabla = cuadruple;

    }

    public int Resultados() {
        en.Inicio("AUTOMATAS");
        ArrayList<String> ab = new ArrayList();
        for (Cuadruple bin : tabla) {
            if (Character.isLetter(bin.r.charAt(0)) && !bin.r.equals("IMPRIME") && !bin.r.equals("LEER")) {
                if (!ab.contains(bin.r)) {
                    ab.add(bin.r);
                    en.setVariables(bin.r);
                }
            } else if (bin.r.equals("IMPRIME") && bin.op1.contains("\"")) {
                int val = this.getMsj();
                en.setMsj("msj" + val, bin.op1);
                map.put(bin.op1, "msj" + val);

            }
        }
        en.CODE();

        return 0;
    }

    public int Operaciones() {

        Cuadruple bin;
        for (int i = 0; i < tabla.size(); i++) {
            bin = tabla.get(i);
 if (Character.isDigit(bin.r.charAt(0)) && bin.rn.equals("") && bin.op1.equals("")) {
              
                if (pila.contains(bin.r)) {

                    en.setRet(bin.r);
                } else {
                    pila.add(bin.r);
                    en.setE(bin.r);
                }
            } else if (bin.r.equals("IMPRIME")) {
                if (bin.op1.contains("\"")) {
                    en.setImpMensaje(map.get(bin.op1));
                } else {
                    en.setImpVarible(bin.op1);
                }

            } else if (bin.r.equals("LEER")) {
                en.setLeer(bin.op1);

            } else if (bin.op.equals("=") && Character.isLetter(bin.op1.charAt(0))) {
                en.setImpAsig(bin.op1, bin.r);
                System.out.println("C: " + bin.r);
            } else {

                switch (bin.op) {
                    case "+":
                        if (bin.op2.equals("")) {
                            en.setIncremento(bin.op1, bin.r);
                        } else {
                            en.setSuma(bin.op1, bin.op2, bin.r);
                        }
                        break;
                    case "-":
                        if (bin.op2.equals("")) {
                            en.setDec(bin.op1, bin.r);
                        } else {
                            en.setResta(bin.op1, bin.op2, bin.r);
                        }
                        break;
                    case "*":
                        en.setMul(bin.op1, bin.op2, bin.r);
                        break;
                    case "=":
                        en.setImpAsig(bin.op1, bin.r);
                        break;
                    case "/":
                        en.setDiv(bin.op1, bin.op2, bin.r);
                        break;
                    case ">=":
                        System.err.println("nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
                        en.setMayoIg(bin.op1, bin.op2, bin.r, bin.rn);
                        break;
                    case "<=":
                        en.setMenorIg(bin.op1, bin.op2, bin.r, bin.rn);
                        break;
                    case ">":
                        en.setMayo(bin.op1, bin.op2, bin.r, bin.rn);
                        break;
                    case "<":
                        en.setMenor(bin.op1, bin.op2, bin.r, bin.rn);
                        break;
                    case "!=":
                        en.setDiferente(bin.op1, bin.op2, bin.r, bin.rn);
                        break;
                    case "==":
                        en.setIgual(bin.op1, bin.op2, bin.r, bin.rn);
                        break;
                }
            }

        }
        en.setFinal();
        System.out.println("+++" + tabla);

        return 0;
    }

    public void init() {
        this.Resultados();
        this.Operaciones();
        en.cerrar();
    }
}
