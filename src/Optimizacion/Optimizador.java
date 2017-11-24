/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Optimizacion;

import CodigoIntermedio.Cuadruple;

/**
 *
 * @author rms
 */
public class Optimizador {

    public boolean evaluador(Cuadruple b) {
        if (!b.op1.equals("") && Character.isLetter(b.op1.charAt(0)) && b.op2.equals("0") && b.op.equals(">") && b.op.equals("<") && b.op.equals("!=") && b.op.equals(">=") && b.op.equals("<=") && b.op.equals("==")) {//Temporal Op Cero
            if (b.op.equals("*") || b.op.equals("/")) {
                b.op1 = "0";
                b.op2 = "";
                b.op = "=";
                return true;
            } else {
                b.op2 = "";
                b.op = "=";
                return true;
            }

        } else if (!b.op2.equals("") && b.op1.equals("0") && Character.isLetter(b.op2.charAt(0))) {//Cero Op Temporal
            if (b.op.equals("*") || b.op.equals("/")) {
                b.op1 = "0";
                b.op2 = "";
                b.op = "=";
                return true;
            } else {
                b.op1 = b.op2;
                b.op2 = "";
                b.op = "=";
                return true;
            }

        } else if (!b.op2.equals("") && b.op.equals("*") && b.op1.equals("1") && Character.isLetter(b.op2.charAt(0))) {//1*var
            b.op1 = b.op2;
            b.op2 = "";
            b.op = "=";
            return true;

        } else if (!b.op1.equals("") && b.op.equals("*") && b.op2.equals("1") && Character.isLetter(b.op1.charAt(0))) {//var *1
            b.op2 = "";
            b.op = "=";
            return true;

        } else if ((!b.op2.equals("") && !b.op1.equals("") && (b.op1.charAt(0) == '-' || b.op2.charAt(0) == '-' || (Character.isDigit(b.op1.charAt(0)) && Character.isDigit(b.op2.charAt(0))))) && !b.op.equals("<=")) {//Numero Op Numero
            b.op1 = this.evaluacion(b.op1, b.op2, b.op);
            b.op2 = "";
            b.op = "=";
            return true;

        } else if ((b.op1.equals("2") || b.op2.equals("2")) && !b.op1.equals("") && b.op1.charAt(0) != 'T' && !b.op2.equals("") && b.op2.charAt(0) != 'T' && (Character.isLetter(b.op1.charAt(0)) || Character.isLetter(b.op2.charAt(0))) && b.op.equals("*")) {//2*x
            if (Character.isLetter(b.op1.charAt(0))) {
                b.op = "+";
                b.op2 = b.op1;
                return true;
            } else {
                b.op = "+";
                b.op1 = b.op2;
                return true;
            }

        }
        return false;
    }

    public String evaluacion(String a, String b, String op) {
        String f = null;
        switch (op) {
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
                f = "" + (Double.parseDouble(a) / Double.parseDouble(b));
                break;
            case "+":
                if (!a.contains(".") && !b.contains(".")) {
                    f = "" + (Integer.parseInt(a) + Integer.parseInt(b));
                } else {
                    f = "" + (Double.parseDouble(a) + Double.parseDouble(b));
                }
                break;

        }
        return f;

    }

}
