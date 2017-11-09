/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Lexico;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 * @author rms
 */
public class Cambiar {

    public ArrayList<String> cambiar(ArrayList<String> lines) {
        ArrayList<String> newList = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains("int")) {
                newList.add(lines.get(i).replace("int", "VI").replace("function", "F").replace("for", "CSF").replace("while", "CSW").replace("for", "CSF"));
            } else if (lines.get(i).contains("float")) {
                newList.add(lines.get(i).replace("float", "VF").replace("function", "F").replace("for", "CSF"));
            } else if (lines.get(i).contains("boolean")) {
                newList.add(lines.get(i).replace("boolean", "VB").replace("function", "F").replace("for", "CSF"));
            } else if (lines.get(i).contains("string")) {
                newList.add(lines.get(i).replace("string", "VS").replace("function", "F").replace("for", "CSF"));
            } else if (lines.get(i).contains("char")) {
                newList.add(lines.get(i).replace("char", "VC").replace("function", "F").replace("for", "CSF"));
            } else if (lines.get(i).contains("function")) {
                newList.add(lines.get(i).replace("function", "F").replace("for", "CSF"));
            } else if (lines.get(i).contains("msj")) {
                newList.add(lines.get(i).replace("msj", "OM"));
            } else if (lines.get(i).contains("if")) {
                newList.add(lines.get(i).replace("if", "CSI"));
            } else if (lines.get(i).contains("while")) {
                newList.add(lines.get(i).replace("while", "CSW"));
            } else if (lines.get(i).contains("do")) {
                newList.add(lines.get(i).replace("do", "CSD"));
            } else if (lines.get(i).contains("for")) {
                newList.add(lines.get(i).replace("for", "CSF"));
            } else if (lines.get(i).contains("switch")) {
                newList.add(lines.get(i).replace("switch", "CSS"));
            } else if (lines.get(i).contains("case")) {
                newList.add(lines.get(i).replace("case", "CSC"));
            } else if (lines.get(i).contains("break")) {
                newList.add(lines.get(i).replace("break", "CSB"));
            } else if (lines.get(i).contains("return")) {
                newList.add(lines.get(i).replace("return", "RE"));
            } else if (lines.get(i).contains("else")) {
                newList.add(lines.get(i).replace("else", "CSE"));
            } else if (lines.get(i).contains("go")) {
                newList.add(lines.get(i).replace("go", "IR"));
            } //TODOS LOS DEMÁS
            else {
                newList.add(lines.get(i));
            }
        }

        /*
        1 msj Mensaje
        7 if SI
        8 while MIENTRAS
        9 do HACER MIENTRAS
        10 for PARA
        11 switch INTERRUPTOR
        12 case CASOS
        13 break ESCAPE
        17 RE RETORNO
        35 else ELSE
        36 go LECTURA
         */
        lines = newList;
        Collections.replaceAll(lines, "#INICIO", "<INICIO");
        Collections.replaceAll(lines, "#", "/>");

        return lines;
    }
    
    public void clearFile() throws FileNotFoundException {
        String archivo = "test.txt";
        PrintWriter writer = new PrintWriter(archivo);
        writer.close();
    }

    public ArrayList<String> readFile(String archivo) {
        ArrayList<String> records = new ArrayList<>();
        try {
            //Abrir "programa.txt"
            BufferedReader reader = new BufferedReader(new FileReader(archivo));
            String line;
            while ((line = reader.readLine()) != null) {
                records.add(line);
            }
            reader.close();
//            System.out.println("readFile " + records);

            //Cambiar
            records = cambiar(records);

            //Guardar test.txt
//            List<String> lines = Arrays.asList("The first line", "The second line");
            Path file = Paths.get("test.txt");
            Files.write(file, records, Charset.forName("UTF-8"));

            return records;
        } catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", archivo);
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> readFile2(String archivo, String destination) {
        ArrayList<String> records = new ArrayList<>();
        try {
            //Abrir "programa.txt"
            BufferedReader reader = new BufferedReader(new FileReader(archivo));
            String line;
            while ((line = reader.readLine()) != null) {
                records.add(line);
            }
            reader.close();
//            System.out.println("readFile " + records);

            //Cambiar
            records = regresar(records);

            //Guardar test.txt
//            List<String> lines = Arrays.asList("The first line", "The second line");
            Path file = Paths.get(destination);
            Files.write(file, records, Charset.forName("UTF-8"));

            return records;
        } catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", archivo);
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> regresar(ArrayList<String> lines) {
        ArrayList<String> newList = new ArrayList<String>();

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains("VI")) {
                newList.add(lines.get(i).replace("VI", "int").replace("F", "function"));
            } else if (lines.get(i).contains("VF")) {
                newList.add(lines.get(i).replace("VF", "float").replace("F", "function"));
            } else if (lines.get(i).contains("VB")) {
                newList.add(lines.get(i).replace("VB", "boolean").replace("F", "function"));
            } else if (lines.get(i).contains("VS")) {
                newList.add(lines.get(i).replace("VS", "string").replace("F", "function"));
            } else if (lines.get(i).contains("VC")) {
                newList.add(lines.get(i).replace("VC", "char").replace("F", "function"));
            } else if (lines.get(i).contains("F") && lines.get(i).length() == 1) {
                newList.add(lines.get(i).replace("F", "function").replace("F", "function"));
            } else if (lines.get(i).contains("<INICIO")) {
                newList.add(lines.get(i).replace("<INICIO", "#INICIO"));
            } else if (lines.get(i).contains("/>")) {
                newList.add(lines.get(i).replace("/>", "#"));
            } else if (lines.get(i).contains("OM")) {
                newList.add(lines.get(i).replace("OM", "msj"));
            } else if (lines.get(i).contains("CSI")) {
                newList.add(lines.get(i).replace("CSI", "if"));
            } else if (lines.get(i).contains("CSW")) {
                newList.add(lines.get(i).replace("CSW", "while"));
            } else if (lines.get(i).contains("CSD")) {
                newList.add(lines.get(i).replace("CSD", "do"));
            } else if (lines.get(i).contains("CSF")) {
                newList.add(lines.get(i).replace("CSF", "for"));
            } else if (lines.get(i).contains("CSS")) {
                newList.add(lines.get(i).replace("CSS", "switch"));
            } else if (lines.get(i).contains("CSC")) {
                newList.add(lines.get(i).replace("CSC", "case"));
            } else if (lines.get(i).contains("CSB")) {
                newList.add(lines.get(i).replace("CSB", "break"));
            } else if (lines.get(i).contains("RE") && lines.get(i).length() == 2) {
                newList.add(lines.get(i).replace("RE", "return"));
            } else if (lines.get(i).contains("CSE")) {
                newList.add(lines.get(i).replace("CSE", "else"));
            } else if (lines.get(i).contains("IR")) {
                newList.add(lines.get(i).replace("IR", "go"));
            } else {
                newList.add(lines.get(i));
            }
        }

        lines = newList;
        Collections.replaceAll(lines, "<INICIO", "#INICIO");
        Collections.replaceAll(lines, "/>", "#");

        return lines;
    }

    public String regresar2(String string) {
        String temp = string;

        if (temp.contains("<INICIO")) {
            temp = temp.replace("<INICIO", "#INICIO");
        } else if (temp.contains("/>")) {
            temp = temp.replace("/>", "#");
        } else if (temp.contains("VI")) {
            temp = temp.replace("VI", "int").replace("F", "function");
        } else if (temp.contains("VF")) {
            temp = temp.replace("VF", "float").replace("F", "function");
        } else if (temp.contains("VB")) {
            temp = temp.replace("VB", "boolean").replace("F", "function");
        } else if (temp.contains("VS")) {
            temp = temp.replace("VS", "string").replace("F", "function");
        } else if (temp.contains("VC")) {
            temp = temp.replace("VC", "char").replace("F", "function");
        } else if (temp.contains("F")) {
            temp = temp.replace("F", "function").replace("F", "function");
        } else if (temp.contains("OM")) {
            temp = temp.replace("OM", "msj");
        } else if (temp.contains("CSI")) {
            temp = temp.replace("CSI", "if");
        } else if (temp.contains("CSW")) {
            temp = temp.replace("CSW", "while");
        } else if (temp.contains("CSD")) {
            temp = temp.replace("CSD", "do");
        } else if (temp.contains("CSF")) {
            temp = temp.replace("CSF", "for");
        } else if (temp.contains("CSS")) {
            temp = temp.replace("CSS", "switch");
        } else if (temp.contains("CSC")) {
            temp = temp.replace("CSC", "case");
        } else if (temp.contains("CSB")) {
            temp = temp.replace("CSB", "break");
        } else if (temp.contains("RE")) {
            temp = temp.replace("RE", "return");
        } else if (temp.contains("CSE")) {
            temp = temp.replace("CSE", "else");
        } else if (temp.contains("IR")) {
            temp = temp.replace("IR", "go");
        } else {
            return temp;
        }

        return temp;
    }

}
