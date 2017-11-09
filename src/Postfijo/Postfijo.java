/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Postfijo;

import Inicio.AText;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rms
 */
public class Postfijo {

    String aux;
    Pila operadores;
    Pila operandos;
    ArrayList<String> lines;
    ArrayList<String> lexema;
    public Map<String, Integer> prioridad = new HashMap<>();
    File f = new File("Tablas/Prioridad.txt");
    AText at;

    /**
     * @param lexema
     */
    public Postfijo(ArrayList<String> lexema) throws IOException {
        this.lexema = lexema;
        operadores = new Pila(lexema.size());
        operandos = new Pila(lexema.size());
        this.getPrioridad();
    }

    public void getPrioridad() throws IOException {
        String[] cad;
        at = new AText(f, false);
        lines = at.Leer();
        for (String line : lines) {
            cad = line.split(" ");
            // System.out.println("" + cad[0] + "--" + cad[1]);
            prioridad.put(cad[0], Integer.parseInt(cad[1]));
        }
    }

    public ArrayList<String> getPosfijo() {
        for (String lex : lexema) {
            switch (lex) {
                case ")":
                    while (!operadores.empty() && !"(".equals(operadores.stacktop())) {//mientras no este vacia y operadores no sea un (
                        aux = operadores.pop();
                        operandos.push(aux);//cambia lo que esta en la pila operadores a la pila operandos
                    }
                    if (operadores.empty()) {
                        System.out.println("Error en expresión");
                    } else {
                        operadores.pop();//sacamos el parentesis que abre
                    }
                    break;
                case "(":
                    operadores.push(lex);
                    break;
                case "OR":
                case "NOT":
                case "AND":
                case "^":
                case "+":
                case "-":
                case "*":
                case "/":
                case ">":
                case "<":
                case ">=":
                case "<=":
                case "!=":
                case "==":
                case "=":
                    if (operadores.empty()) {

                        operadores.push(lex);
                    } else {

                        while (!operadores.empty() && !operadores.stacktop().equals("(") && this.prioridad.get(lex) >= this.prioridad.get(operadores.stacktop())) {
                            aux = operadores.pop();
                            operandos.push(aux);
                        }

                        operadores.push(lex);
                    }
                    break;
                default:

                    operandos.push(lex);
                    break;
            }
        }
        while (!operandos.empty()) {
            operadores.push(operandos.pop());
        }

        return MostrarPila(operadores);
    }

    public ArrayList<String> MostrarPila(Pila p) {
        ArrayList<String> posfijo = new ArrayList();

        while (!p.empty()) {
            posfijo.add(p.pop());

        }
        return posfijo;
    }

//    public static void main(String[] args) throws IOException {
//        ArrayList<String> l = new ArrayList();
//        String[] h = {"(", "(", "a", "+", "b", "*", "carr", ")", "/", "d", ")", "+", "(", "a", "*", "b", "/", "d", ")"};
//        String[] j = {"Z", "=", "(", "a", ">", "b", "OR", "(", "(", "a", "+", "b", "/", "d", "*", "c", ")", ">", "d", ")", "AND", "d", ")"};
//        String[] a = {"Z", "=", "NOT", "(", "(", "b", "+", "c", "*", "d", "/", "a", ")", ">", "(", "b", "*", "c", ")", ")"};
//        // a = "Z=NOT((b+c*d/a)>(b*c))";
//
//        String[] v = {"Z", "=", "\"hola\""};
//
//        for (int i = 0; i < j.length; i++) {
//            l.add(j[i]);
//            // System.out.println(h[i]);
//        }
//        Postfijo p = new Postfijo(l);
//        l = p.getPosfijo();
//        System.out.println("lllllllllllll " + l);
//
//    }
}
