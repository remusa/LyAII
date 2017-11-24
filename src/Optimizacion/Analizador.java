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
    public ArrayList<ArrayList> bloques = new ArrayList();

    Optimizador opt = new Optimizador();

    public Analizador(ArrayList<Cuadruple> cuadruples) {
        this.tabla = cuadruples;
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
                for (int w = i; w < tabla.size(); w++) {//se busca en todos los temporales
                    Cuadruple bn = tabla.get(i);
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
                    for (int j = i + 1; j < tabla.size(); j++) {
                        Cuadruple cuadChecandoSustituir = tabla.get(j);
                        //se compara contra todos
                        if (cuadChecandoSustituir.op1.equals(b.r)) {
                            cuadChecandoSustituir.op1 = bin.r;
                        }
                        if (cuadChecandoSustituir.op2.equals(b.r)) {
                            cuadChecandoSustituir.op2 = bin.r;
                        }

                    }
                    tabla.remove(i);
                    i--;
                    huboCambio = true;
                    this.despliegue();
                } else if (bin.op.equals("+") || bin.op.equals("*")) {
                    if (bin.op.equals(b.op)) {
                        if ((bin.op1.equals(b.op1) && bin.op2.equals(b.op2))
                                || (bin.op1.equals(b.op2) && bin.op2.equals(b.op1))) {
                            System.out.println("----> " + bin.r + " == " + b.r);
                            for (int j = i + 1; j < tabla.size(); j++) {
                                Cuadruple cuadChecandoSustituir = tabla.get(j);
                                //se compara contra todos
                                if (cuadChecandoSustituir.op1.equals(b.r)) {
                                    cuadChecandoSustituir.op1 = bin.r;
                                }
                                if (cuadChecandoSustituir.op2.equals(b.r)) {
                                    cuadChecandoSustituir.op2 = bin.r;
                                }

                            }
                            tabla.remove(i);
                            i--;
                            huboCambio = true;
                            this.despliegue();
                        }
                    }
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

    private void SepararBloques() {
        if (tabla.size() > 0) {
            if (!tabla.get(0).op1.equals("NUEVO")) {
                tabla.add(0, new Cuadruple("NUEVO", "BLOQUE", "?", "->", "."));
            }
            boolean buscar;
            do {
                System.out.println("jaja");
                buscar = false;
                int posicion = 0;
                for (Cuadruple cuadruple : tabla) {
                    if (cuadruple.op.equals("GOTO")) {
                        String aDondeSalta = cuadruple.r;
                        for (Cuadruple buscando : tabla) {
                            if (buscando.r.equals(aDondeSalta) && buscando.op1.equals("") && buscando.op2.equals("") && buscando.op.equals("")) {
                                posicion = tabla.indexOf(buscando);
                                break;
                            }
                        }
                        if (posicion != 0) {
                            if (!tabla.get(posicion - 1).op1.equals("NUEVO")) {
                                tabla.add(posicion, new Cuadruple("NUEVO", "BLOQUE", "?", "->", "."));
                                buscar = true;
                                break;
                            }

                        }
                    }
                }
            } while (buscar);
            do {
                System.out.println("jeje");
                buscar = false;
                for (Cuadruple cuadruple : tabla) {
                    if (cuadruple.op.equals("GOTO") && !tabla.get(tabla.indexOf(cuadruple) + 1).op1.equals("NUEVO")) {
                        tabla.add(tabla.indexOf(cuadruple) + 1, new Cuadruple("NUEVO", "BLOQUE", "?", "->", "."));
                        buscar = true;
                        break;
                    }

                }
            } while (buscar);

        }
    }

    public void inicio() {

        SepararBloques();

        this.despliegue();

        bloques();

        for (ArrayList bl : bloques) {
            tabla = bl;

            int a, b;
            a = 0;
            b = 0;
            if (tabla != null) {
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
        }
        tabla = new ArrayList<>();
        for (ArrayList<Cuadruple> bl : bloques) {
            if (bl != null) {
                tabla.add(new Cuadruple("NUEVO", "BLOQUE", "?", "->", "."));
                for (Cuadruple cuad : bl) {
                    tabla.add(cuad);
                }
            }
        }
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
