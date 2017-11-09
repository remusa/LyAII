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
    public ArrayList<ArrayList> bloques = new ArrayList();

    public Analizador(ArrayList<Cuadruple> cuadruples) {
        this.tabla = cuadruples;
    }

    public void reducir() {//resuelve las operaciones numericas y anomalas
        for (Cuadruple bin : tabla) {
            opt.evaluador(bin);
        }
    }

    public void bloques() {//divide en bloques
        ArrayList<Cuadruple> ax = null;
        Cuadruple bin;
        for (int i = 0; i < tabla.size(); i++) {//recorre el arreglo principal
            bin = tabla.get(i);
            if (bin.op1.equals("NUEVO")) {//cuando encuentra el identificado de salto de bloque
                if (i == 0) {//si es el primero se crea
                    ax = new ArrayList();
                } else {//si no es el primero se guarda el anterior y se crea uno nuevo
                    bloques.add(ax);
                    ax = new ArrayList();
                }
            } else {
                ax.add(bin);
            }
        }
        bloques.add(ax);
    }

    public void sustituir() {//sustituye las variables de tipo T2=6 y T6=T7
        int b = 0;//bandera importante se utiliza para saber cual temporal ya no se utiliza esto evita eliminar un  temporal importante que despues se usara
        Cuadruple bin;
        for (int i = 0; i < tabla.size(); i++) {//se optiene un elemento
            bin = tabla.get(i);
            if (!bin.r.equals("") && bin.r.charAt(0) == 'T' && bin.op2.equals("")) {//se verifica que solo tenga un operando
                System.out.println("--->" + bin.r);
                for (Cuadruple bn : tabla) {//se busca en todos los temporales
                    if (bn.op1.equals(bin.r)) {//cuando es igual al operando 1
                        b = 1;
                        bn.op1 = bin.op1;
                    } else if (bn.op2.equals(bin.r)) {//cuando es igual al operando 2
                        b = 1;
                        bn.op2 = bin.op1;
                    }
                }
                if (b == 1) {
                    tabla.remove(i);
                    i--;
                    b = 0;
                }
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

    public void inicio() {
        this.bloques();
        int a, b;
        for (ArrayList bl : bloques) {
            System.out.println("=============================>BLOQUE<=============================");
            tabla = bl;
            a = tabla.size();
            b = 0;
            while (a != b) {
                a = tabla.size();
                System.out.println("--->Reducir");
                reducir();
                this.despliegue();
                System.out.println("--->Sustituir");
                sustituir();
                this.despliegue();
                System.out.println("--->Comparar");
                comparar();
                this.despliegue();
//   System.out.println("--->reducir");
//           reducir();
//            this.despliegue();
//            System.out.println("--->sustituir");
//            sustituir();
//            this.despliegue();
                b = tabla.size();
            }
        }
    }
}
