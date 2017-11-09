/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lexico;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Unico
 */
public class Validador {
    Pattern p;
    Matcher m;

    public boolean Numenteros(String cad) {
        p = Pattern.compile("[0-9]+");
        Matcher m = p.matcher(cad);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean Numflotantes(String cad) {
        p = Pattern.compile("[0-9]+\\.[0-9]+");
        Matcher m = p.matcher(cad);
        if (m.matches()) {
            //System.out.println(m.group());
            return true;
        } else {
            //  System.out.println("nonf");
            return false;
        }
    }

    public boolean Variables(String cad) {
        p = Pattern.compile("[a-zA-Z]+[0-9]*");
        Matcher m = p.matcher(cad);
        if (m.matches()) {
            //System.out.println(m.group());
            return true;
        } else {
            // System.out.println("nov");
            return false;
        }
    }

    public static String Comp(String cad, String pat) {

        for (int i = 0; i < pat.length(); i++) {
            if (cad.charAt(i) != pat.charAt(i)) {
                return "" + pat.charAt(i);
            }

        }
        return null;
    }


//    public static void main(String[] args) {
//        
//        Validador c=new Validador();
//        System.out.println(c.Comp("unotres","unodostres"));
//       
//        
//    }
}
