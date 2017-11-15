/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Optimizacion;

import CodigoIntermedio.Cuadruple;
import java.util.ArrayList;

/**
 *
 * @author rms
 */
public class Analizador {

    public ArrayList<Cuadruple> tabla;
    Optimizador opt = new Optimizador();

    public Analizador(ArrayList<Cuadruple> cuadruples) {
        this.tabla = cuadruples;
    }

    public void reducir() {//resuelve las operaciones numericas y anomalas
        for (Cuadruple bin : tabla) {
            if (opt.evaluador(bin)) {
                huboCambio = true;
            }
        }
    }

    public void sustituir() {//sustituye las variables de tipo T2=6 y T6=T7
        int b = 0;//bandera importante se utiliza para saber cual temporal ya no se utiliza esto evita eliminar un  temporal importante que despues se usara
        Cuadruple bin;
        for (int i = 0; i < tabla.size(); i++) {//se obtiene un elemento
            bin = tabla.get(i);
            if (!bin.r.equals("") && bin.r.charAt(0) == 'T' && bin.op2.equals("")) {//se verifica que solo tenga un operando
                System.out.println("Sustituyendo --->" + bin.r);
                for (Cuadruple bn : tabla) {//se busca en todos los temporales
                    if (bn.op1.equals(bin.r)) {//cuando es igual al operando 1
                        b = 1;
                        bn.op1 = bin.op1;
                        huboCambio = true;
                    } 
                    if (bn.op2.equals(bin.r)) {//cuando es igual al operando 2
                        b = 1;
                        bn.op2 = bin.op1;
                        huboCambio = true;
                    }
                }
                if (b == 1) {
                    tabla.remove(i);
                    i--;
                    b = 0;
                    huboCambio = true;
                }
                this.despliegue();
            } else if (!bin.r.equals("") && bin.op2.equals("") && bin.op.equals("=")) {//se verifica que solo tenga un operando
                System.out.println("Sustituyendo --->" + bin.r);
                for (Cuadruple bn : tabla) {//se busca en todos los temporales
                    if (bn.op1.equals(bin.r)) {//cuando es igual al operando 1
                        bn.op1 = bin.op1;
                        huboCambio = true;
                    } 
                    if (bn.op2.equals(bin.r)) {//cuando es igual al operando 2
                        bn.op2 = bin.op1;
                        huboCambio = true;
                    }
                }
                this.despliegue();
            }
        }
    }

    public void comparar() {        //compara operaciones iguales
        Cuadruple b;
        Cuadruple bin;
        for (int a = 0; a < tabla.size(); a++) {//selecciona el comparador
            bin = tabla.get(a);
            for (int i = a + 1; i < tabla.size(); i++) {//se compara contra todos
                b = tabla.get(i);
                if (!bin.op1.equals("NUEVO") && bin.op1.equals(b.op1) && bin.op.equals(b.op) && bin.op2.equals(b.op2) && (!bin.op2.equals("") || !bin.op1.equals(""))) {//se comparan los elementos
                    System.out.println("----> " + bin.r + " == " + b.r);
                    b.op1 = bin.r;
                    b.op2 = "";
                    b.op = "=";
                    huboCambio = true;
                }
            }
        }

    }

    public void despliegue() {
        String op1, op2, r;
        for (Cuadruple b : tabla) {
            op1 = b.op1;
            op2 = b.op2;
            r = b.r;
            while (op1.length() < 6) {
                op1 += " ";
            }
            while (op2.length() < 6) {
                op2 += " ";
            }
            while (r.length() < 6) {
                r += " ";
            }
            System.out.println(op1 + "  " + op2 + "  " + b.op + "  " + r);
        }
        System.out.println("-----------------------");
    }

    boolean huboCambio = false;

    public void inicio() {

        int a, b;
        a = tabla.size();
        b = 0;

        do {
            a = tabla.size();

            System.out.println("Sumas con Cero");
            SumasConCero();
            this.despliegue();

            System.out.println("Sumas con Cero");
            MultiplicacionesPorCero();
            this.despliegue();

            System.out.println("Sumas con Cero");
            MultiplicacionesPorUno();
            this.despliegue();

            System.out.println("Sumas con Cero");
            DivisionesPorUno();
            this.despliegue();

            System.out.println("--->Reducir");
            reducir();
            this.despliegue();

            System.out.println("--->Sustituir");
            sustituir();
            this.despliegue();

            System.out.println("--->Comparar");
            comparar();
            this.despliegue();

            b = tabla.size();
        } while (a != b && huboCambio == true);

    }

    private void MultiplicacionesPorCero() {
        tabla.forEach((cuad) -> {
            if (cuad.op.equals("*") && cuad.op1.equals("0")) {
                cuad.op = ("=");
                cuad.op1 = ("0");
                cuad.op2 = ("");
                huboCambio = true;
            } else if (cuad.op.equals("*") && cuad.op2.equals("0")) {
                cuad.op = ("=");
                cuad.op1 = ("0");
                cuad.op2 = ("");
                huboCambio = true;
            }
        });
    }

    private void MultiplicacionesPorUno() {
        tabla.forEach((cuad) -> {
            if (cuad.op.equals("*") && cuad.op1.equals("1")) {
                cuad.op = ("=");
                cuad.op1 = (cuad.op2);
                cuad.op2 = ("");
                huboCambio = true;
            } else if (cuad.op.equals("*") && cuad.op2.equals("1")) {
                cuad.op = ("=");
                cuad.op2 = ("");
                huboCambio = true;
            }
        });
    }

    private void DivisionesPorUno() {
        tabla.forEach((cuad) -> {
            if (cuad.op.equals("/") && cuad.op1.equals("1")) {
                cuad.op = ("=");
                cuad.op1 = (cuad.op2);
                cuad.op2 = ("");
                huboCambio = true;
            } else if (cuad.op.equals("/") && cuad.op2.equals("1")) {
                cuad.op = ("=");
                cuad.op2 = ("");
                huboCambio = true;
            }
        });
    }

    private void SumasConCero() {
        tabla.forEach((cuad) -> {
            if (cuad.op.equals("+") && cuad.op1.equals("0")) {
                cuad.op = ("=");
                cuad.op1 = (cuad.op2);
                cuad.op2 = ("0");
                huboCambio = true;
            } else if (cuad.op.equals("+") && cuad.op2.equals("0")) {
                cuad.op = ("=");
                cuad.op2 = ("");
                huboCambio = true;
            }
        });
    }
}
