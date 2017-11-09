/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lexico;

import Inicio.AText;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 *
 * @author Unico
 */
public class Tokens {

    int i = 0;
    File f = new File("Tablas/tokens.txt");
    AText at;//false indica que se abrira
    ArrayList<String> lines;
    public Map<String, String> lenTI = new HashMap<>();//lenguaje Token-identificador
    public Map<String, String> lenIT = new HashMap<>();//lenguaje Identificador-token
    public Map<String, String> desc = new HashMap<>();

    public Tokens() throws IOException {
        at = new AText(f, false);
        lines = at.Leer();
        Estructurar();
    }

    public void Estructurar() {
        String temp[] = new String[5];
        for (String line : lines) {
            i = 0;
            StringTokenizer st = new StringTokenizer(line.trim(), " ");
            while (st.hasMoreTokens()) {
                temp[i] = st.nextToken();
                i++;
            }
            i = 0;

//            System.out.println(temp[2]);
            lenTI.put(temp[1], temp[0]);
            // System.out.println(temp[2]);
            lenIT.put(temp[0], temp[1]);
            desc.put(temp[0], temp[2]);
        }
    }

//    public static void main(String[] args) throws IOException {
//        new TOKENS();
//    }
}
