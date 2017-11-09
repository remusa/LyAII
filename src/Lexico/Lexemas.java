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
import java.util.Collections;
import java.util.StringTokenizer;

/**
 * @author Unico
 */
public class Lexemas {

    String file;
    public ArrayList<String> lines;

    public Lexemas(String file) {
        this.file = file;
    }

    public ArrayList<String> obtenerLexema() throws IOException {
        //Lectura

        AText ab = new AText(new File(file), false);
        lines = ab.Leer();

        //Obtener Tokens
        ArrayList<String> lexema = new ArrayList();//almacena toquens compuestos
        ArrayList<String> token = new ArrayList();//almacena toquens simples
        lines.stream().forEach((sp1) -> {

            if (sp1.length() > 1 && (sp1.trim().charAt(0) == '#' || sp1.trim().charAt(0) == '@')) {//checa si tiene comentario la linea y si es de uno solo
                token.add(sp1);
            } else {//si no es uhnn comentario de una linea lo separa
                StringTokenizer st = new StringTokenizer(sp1.trim(), " "); //separa en espacios
                while (st.hasMoreTokens()) {
                    StringTokenizer sto = new StringTokenizer(st.nextToken(), ",!=(){}<>%;/*\"+-:", true); //separa en caracteres
                    while (sto.hasMoreTokens()) {
                        token.add(sto.nextToken());//almacena
                        //System.out.println(token.get(i));
                    }
//            }if(sp1.length()>1){//saber cuando termino una linea
//              token.add(i, "END");//linea
//                  i++;
                }
            }
        });

        String l = null;
        String ll = null;

        int z;
        for (int j = 0; j < token.size() - 1; j++) {
            ll = token.get(j);
            if (j < token.size() - 1) {

                l = token.get(j) + token.get(j + 1);
            }
            if (l.equals(">=") || l.equals("!=") || l.equals("<=") || l.equals("--") || l.equals("++") || l.equals("==")
                    //                    || l.toUpperCase().equals("<ENFORCE") || l.equals("/>")) {
                    || l.toUpperCase().equals("<INICIO") || l.equals("/>")) {
                lexema.add(l);
                j++;
            } else if (l.equals("/*")) {//comentario de varias lineas
                l = "";
                while (!(token.get(j) + token.get(j + 1)).equals("*/")) {
                    l = l + token.get(j) + " ";
                    j++;
                }
                l = l + " " + token.get(j) + token.get(j + 1);

                j++;
                l = "/*" + l.substring(3, l.length());//elimina los espacios de las comillas ejemolo " bebe "="bebe"
                lexema.add(l);
            } else {
                if (ll.equals("\"")) {//Valor texto
                    l = token.get(j);
                    j++;
                    while (!(token.get(j)).equals("\"")) {
                        l = l + " " + token.get(j);
                        j++;
                    }
                    l = l + " " + token.get(j);
                    l = "\"" + l.substring(2, l.length() - 1) + "\"";//elimina los espacios de las comillas ejemolo " bebe "="bebe"

                    lexema.add(l);

                } else if (token.get(j - 1).equals("=")
                        && !token.get(j + 1).equals(":")
                        && !token.get(j - 2).equals(">")
                        && !token.get(j - 2).equals("<")
                        && !token.get(j - 2).equals("=")
                        && !token.get(j - 2).equals("!")) {
                    l = token.get(j);
                    j++;
                    while (!(token.get(j)).equals(";") && !(token.get(j)).equals(":")) {
                        l = l + " " + token.get(j);
                        j++;
                    }
                    j--;
                    System.err.println("xol " + l);
                    lexema.add(l);
                } else {
                    lexema.add(token.get(j));
                }
            }

        }

        //muestra lexemas
        System.out.println("-----------------LEXEMAS-----------------");
        for (String token1 : lexema) {
            System.out.println(token1);
        }

        /// -----------
//        //analisis
//        AnalisisLexico al = new AnalisisLexico(lexema);
//        //Validar
//        //Guardar
//        ArrayList<String[]> an = al.Anlisis();
//
//        Analisis a = new Analisis(an, lexema, cad);
//        a.ArmarTablaVariable();
//        ArrayList<String[]> id = a.QuitarBasura();
////        for (String[] id1 : id) {
////            System.out.println(""+id1[1]);
////        }
//        Gramatica1 g = new Gramatica1(id, cad, a.lexema);
//        g.ConstruirGramatica();
//        g.VariablesNoDeclaradas();
//        g.Compile();
//
//        Guardar gu = new Guardar(g.id, g.lexema);
//        gu.Generar();
////     Guardar op = new Guardar(token, g.lexema);
////    gu.Generar();
        return lexema;
    }
}
