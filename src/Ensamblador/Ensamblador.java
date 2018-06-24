/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ensamblador;

import CodigoIntermedio.CodigoIntermedio;
import Inicio.AText;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rms
 */
public class Ensamblador {

    File f;
    AText file;
    String line;

    public Ensamblador(String nom) {
        try {
            f = new File("Resultados/" + nom + ".asm");
            file = new AText(f, true);
        } catch (IOException ex) {
            Logger.getLogger(CodigoIntermedio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cerrar() {
        try {
            file.Cerrar();

//            System.out.println("PATH: " + f.getAbsolutePath());
//            Process p = Runtime.getRuntime().exec("cmd \\c" + f.getAbsolutePath());
//            BufferedInputStream bf = new BufferedInputStream(p.getInputStream());
//            int c = bf.read();
//
//            while (c != -1) {
//                System.out.print((char) c);
//                c = bf.read();
//            }
        } catch (IOException ex) {
            Logger.getLogger(CodigoIntermedio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String setVariables(String n) {
        if (!n.equals("CALL")) {
            line = n + " dw ?";
            this.Escribir(line);
        } else {
            n = "";
        }
        return "" + n;
    }

    public String setConstantes(String n, String va) {
        line = va + " = " + n;
        this.Escribir(line);
        return "" + n;
    }

    public String setMensajes(String num, String n) {
        line = num + " db 10,13," + n + "  ,'$'";
        this.Escribir(line);
        return "" + n;
    }

    public String Inicio(String n) {
        line = "org 100h\n";
        line += "include 'emu8086.inc'\n";

        line += "\nDEFINE_PRINT_STRING\n";
        line += "DEFINE_PRINT_NUM\n";
        line += "DEFINE_SCAN_NUM\n";
        line += "DEFINE_PRINT_NUM_UNS\n";

        line += "\nTITLE " + n + "\n";
        line += ".MODEL SMALL\n";
        line += ".STACK 64M\n";
        line += "\n.DATA";
        this.Escribir(line);
        return "";
    }

    public String CODE() {
        line = "\n.CODE\n";
        line += "inicio PROC FAR\n";
        line += "MOV AX,@data\n";
        line += "MOV DS,AX\n";
        this.Escribir(line);
        return "";
    }

    public String setSuma(String a, String b, String r) {
        line = "MOV AX," + a + "\n";
        line += "ADD AX," + b + "\n";
        line += "MOV " + r + ",AX\n";
        this.Escribir(line);
        return "";
    }

    public String setResta(String a, String b, String r) {
        line = "MOV AX," + a + "\n";
        line += "SUB AX," + b + "\n";
        line += "MOV " + r + ",AX\n";
        this.Escribir(line);
        return "";
    }

    public String setMultiplicacion(String a, String b, String r) {
        line = "MOV AX," + a + "\n";
        line += "MOV BX," + b + "\n";
//        line += "MUL " + b + " \n";
        line += "MUL BX" + " \n";
        line += "MOV " + r + ",AX\n";
        this.Escribir(line);
        return "";
    }

    public String setDivision(String a, String b, String r) {
        line = "MOV AX," + a + "\n";
        line += "XOR DX,DX" + "\n";     //DX = 0
//        line += "DIV " + b + "\n";
        line += "MOV BX," + b + "\n";
        line += "DIV BX" + "\n";
        line += "MOV " + r + ",AX\n";
        this.Escribir(line);
        return "";
    }

    public String setIncremento(String a, String r) {
        line = "MOV AX," + r + "\n";
        line += "ADD AX," + a + "\n";
        line += "MOV " + r + ",AX\n";
        this.Escribir(line);
        return "";
    }

    public String setDecremento(String a, String r) {
        line = "MOV AX," + r + "\n";
        line += "SUB AX," + a + "\n";
        line += "MOV " + r + ",AX\n";
        this.Escribir(line);
        return "";
    }

    public String setIgual(String a, String b, String r, String rn) {
//        line = "MOV CX," + a + "\n";
//        line += "CMP CX," + b + "\n";
//        line += "JE A" + r + "\n";
//        line += "JMP A" + rn + "\n";

        line = "MOV CX," + a + "\n";
        line += "CMP CX," + b + "\n";
        line += "JNE A" + rn + "\n";
        line += "JMP A" + r + "\n";
        this.Escribir(line);
        return "";
    }

    public String setDiferente(String a, String b, String r, String rn) {
        line = "MOV CX," + a + "\n";
        line += "CMP CX," + b + "\n";
        line += "JNE A" + r + "\n";
        line += "JMP A" + rn + "\n";
        this.Escribir(line);
        return "";
    }

    public String setMayor(String a, String b, String r, String rn) {
        line = "MOV CX," + a + "\n";
        line += "CMP CX," + b + "\n";
        line += "JA A" + r + "\n";
        line += "JMP A" + rn + "\n";
        this.Escribir(line);
        return "";
    }

    public String setMenor(String a, String b, String r, String rn) {
        line = "MOV CX," + a + "\n";
        line += "CMP CX," + b + "\n";
        line += "JB A" + r + "\n";
        line += "JMP A" + rn + "\n";
        this.Escribir(line);
        return "";
    }

    public String setMayorIgual(String a, String b, String r, String rn) {
//        line = "MOV CX," + a + "\n";
//        line += "CMP CX," + b + "\n";
//        line += "JAE A" + r + "\n";
//        line += "JMP A" + rn + "\n";

        line = "MOV CX," + a + "\n";
        line += "CMP CX," + b + "\n";
        line += "JBE A" + r + "\n";
        line += "JMP A" + rn + "\n";
        this.Escribir(line);
        return "";
    }

    public String setMenorIgual(String a, String b, String r, String rn) {
        line = "MOV CX," + a + "\n";
        line += "CMP CX," + b + "\n";
        line += "JBE A" + r + "\n";
        line += "JMP A" + rn + "\n";
        this.Escribir(line);
        return "";
    }

    public String setE(String r) {  //etiquetas
        line = "A" + r + ":\n";
        this.Escribir(line);
        return "";
    }

    public String setRetorno(String a) {
        line = "JA A" + a + "\n";
        this.Escribir(line);
        return "";
    }

    public String setLeer(String r) {
        line = "CALL SCAN_NUM\n";
        line += "MOV " + r + ",CX\n";
        this.Escribir(line);
        return "";
    }

    public String setImprimirMensaje(String a) {
        line = "MOV AH,09h\n";
        line += "LEA DX," + a + "\n";
        line += "INT 21h\n";
        this.Escribir(line);
        return "";
    }

    public String setImprimirVariable(String a) {
        line = "MOV AX," + a + "\n";
        line += "CALL PRINT_NUM\n";
        this.Escribir(line);
        return "";
    }

    public String setImprimirAsignacion(String a, String b) {
        line = "MOV AX," + a + "\n";
        line += "MOV " + b + ",AX\n";
        this.Escribir(line);
        return "";
    }

    public String setFinal() {
        line = "MOV AH,4CH\n";
        line += "INT 21h\n";
        line += "inicio ENDP\n\n";
        line += "END\n";
        line += "ret\n";
        this.Escribir(line);
        return "";
    }

    public boolean Escribir(String line) {
        try {
            file.Guardar(line + "\n");
            return true;
        } catch (IOException ex) {
            Logger.getLogger(CodigoIntermedio.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
