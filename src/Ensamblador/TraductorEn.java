/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ensamblador;

import CodigoIntermedio.Cuadruple;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rms
 */
public class TraductorEn {

    Map<String, String> map = new HashMap();
    ArrayList<Cuadruple> tablaCuadruples = new ArrayList();
    ArrayList<String> pila = new ArrayList();
    int msj = 0;

    public int getMsj() {
        return msj += 1;
    }

    int z = 0;
    Ensamblador en = new Ensamblador("Ensamblador");

    public TraductorEn(ArrayList<Cuadruple> cuadruple) {
        this.tablaCuadruples = cuadruple;
    }

    public void init() {
        this.Resultados();
        this.Operaciones();
        en.cerrar();
    }

    /**
    Declaración de variables y mensajes
    @return 
     */
    public int Resultados() {
        ArrayList<String> ab = new ArrayList();

        // Encabezado
        en.Inicio("Codigo Ensamblador");

        // .DATA
        for (Cuadruple bin : tablaCuadruples) {
            // variables
            if (Character.isLetter(bin.r.charAt(0)) && !bin.r.equals("PRINT") && !bin.r.equals("READ")) {
                if (!ab.contains(bin.r)) {
                    ab.add(bin.r);
                    en.setVariables(bin.r);
                }
            } // mensajes
            else if (bin.r.equals("PRINT") && bin.op1.contains("\"")) {
                int val = this.getMsj();
                en.setMensajes("msj" + val, bin.op1);
                map.put(bin.op1, "msj" + val);
            }
        }

        // .CODE
        en.CODE();

        return 0;
    }

    /**
    Operaciones generales
    @return 
     */
    public int Operaciones() {
        Cuadruple bin;

        for (int i = 0; i < tablaCuadruples.size(); i++) {
            bin = tablaCuadruples.get(i);

            // etiquetas de instrucción (10, etc.)
            if (Character.isDigit(bin.r.charAt(0)) && bin.rn.equals("") && bin.op1.equals("")) {
                // si la pila contiene la etiqueta, entonces se regresa
                if (pila.contains(bin.r)) {
                    en.setRetorno(bin.r);
                } 
                // si la pila no contiene la etiqueta, entonces se añade y se cambia el valor de la etiqueta
                else {
                    pila.add(bin.r);
                    en.setE(bin.r);
                }
            } else if (bin.r.equals("PRINT")) { //impresión
                if (bin.op1.contains("\"")) {
                    en.setImprimirMensaje(map.get(bin.op1));
                } else {
                    en.setImprimirVariable(bin.op1);
                }
            } else if (bin.r.equals("READ")) {  //lectura
                en.setLeer(bin.op1);
            } else if (bin.op.equals("=") && Character.isLetter(bin.op1.charAt(0))) {   //asignación
                en.setImprimirAsignacion(bin.op1, bin.r);
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
                            en.setDecremento(bin.op1, bin.r);
                        } else {
                            en.setResta(bin.op1, bin.op2, bin.r);
                        }
                        break;
                    case "*":
                        en.setMultiplicacion(bin.op1, bin.op2, bin.r);
                        break;
                    case "=":
                        en.setImprimirAsignacion(bin.op1, bin.r);
                        break;
                    case "/":
                        en.setDivision(bin.op1, bin.op2, bin.r);
                        break;
                    case ">":
                        en.setMayor(bin.op1, bin.op2, bin.r, bin.rn);
                        break;
                    case "<":
                        en.setMenor(bin.op1, bin.op2, bin.r, bin.rn);
                        break;
                    case ">=":
                        en.setMayorIgual(bin.op1, bin.op2, bin.r, bin.rn);
                        break;
                    case "<=":
                        en.setMenorIgual(bin.op1, bin.op2, bin.r, bin.rn);
                        break;
                    case "==":
                        en.setIgual(bin.op1, bin.op2, bin.r, bin.rn);
                        break;
                    case "!=":
                        en.setDiferente(bin.op1, bin.op2, bin.r, bin.rn);
                        break;
                }
            }

        }

        en.setFinal();
        System.out.println("+++" + tablaCuadruples);

        return 0;
    }

}
