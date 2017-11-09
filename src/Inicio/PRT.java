/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inicio;

import CodigoIntermedio.Cuadruple;
import Optimizacion.Analizador;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * s
 *
 * @author Unico
 */
public class PRT {

    /*public static void main(String[] args) {
////        Pattern patv = Pattern.compile("\\[\\d\\]");
////        String l;
////        Matcher mat;
////
////        String[] sp;
////        String vl = " bhola2[3]";
////        String v2 = "hola4[3][a]";
////        System.out.println("\n\na "+vl.contains("[]"));
//////       StringTokenizer sto = new StringTokenizer(vl, "]"); //separa en caracteres
//////                    while (sto.hasMoreTokens()) {
//////                       System.out.println("L: "+sto.nextToken());//almacena
//////                        //System.out.println(token.get(i));
//////
//////                    }
////     /*
////         recupera el titulo del programa */
////      
////        mat = patv.matcher(v2);
////        while (mat.find()) {
////            l = mat.group();
////            System.out.println("Enocntre: " +l.substring(1, l.length()-1));
////        }
//
//        Cuadruple a = new Cuadruple("2", "3", "*", "T0");
//        Cuadruple b = new Cuadruple("2", "2", "*", "T7");
//
//        Cuadruple c = new Cuadruple("T7", "T0", "-", "T8");
//        Cuadruple d = new Cuadruple("T7", "T0", "-", "T9");
//        Cuadruple e = new Cuadruple("T7", "T0", "-", "T10");
//        Cuadruple f = new Cuadruple("T8", "T9", "-", "T5");
//        Cuadruple g = new Cuadruple("T10", "T9", "/", "T6");
//        Cuadruple h = new Cuadruple("T9", "2", "*", "T7");
//
//        ArrayList<Bin> t = new ArrayList();
//        t.add(a);
//        t.add(b);
//        t.add(c);
//        t.add(d);
//        t.add(e);
//        t.add(f);
//        t.add(g);
//        t.add(h);
//
//        Analizador an = new Analizador(t);
//        an.Despliegue();
//        an.Reducir();
//        an.Despliegue();
//        an.Sustituir();
//        an.Despliegue();
//        an.Comparar();
//        an.Despliegue();
//       //Reducir();
//       //Sustituir();
//       //comparar();
//        //Reducir();
//        
//       //Sustituir();
//
//    }
}
