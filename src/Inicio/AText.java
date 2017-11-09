/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inicio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author jose
 */
public class AText {

    private static int bandera;
    private static BufferedReader fr;
    private static BufferedWriter fw;
    private boolean b;

    public AText(File file, boolean guardar) throws IOException {
        if (guardar) {
            fw = new BufferedWriter(new FileWriter(file));
        } else {
            fr = new BufferedReader(new FileReader(file));
        }
        b = guardar;
    }

    public static void Copia(File Origen, File Destino) throws FileNotFoundException, IOException {
        fr = new BufferedReader(new FileReader(Origen));
        fw = new BufferedWriter(new FileWriter(Destino));

        while ((bandera = fr.read()) != -1) {
            fw.write(bandera);
        }
        fr.close();
        fw.close();

    }

    public void Guardar(String ln) throws IOException {
        fw.write(ln);

    }

    public ArrayList<String> Leer() throws IOException {
        ArrayList l = new ArrayList();
        String line = fr.readLine();
        while (line != null) {
            l.add(line);
            line = fr.readLine();
        }

        return l;

    }

    public void Cerrar() throws IOException {
        if (b) {
            fw.close();
        } else {

            fr.close();
        }

    }
    /*USO Copia
     AText.Copia(new File("dos.txt"), new File("siete.txt"));
     *USO Guardar
     File f = new File("Un.txt");
     AText at = new AText(f,true);//indica que se guardara
     for (int i = 0; i < 10; i++) {
     at.Guardar("" + i);
     }
     at.Cerrar()
     *Uso Leer
     File f = new File("Uno.txt");
     AText at = new AText(f, false);//false indica que se abrira
     int[] l = at.Leer();
     for (int i = 0; i < l.length; i++) {
     System.out.print((char) l[i]);
     }
     at.Cerrar();
   
    
     */

//    public static void main(String[] args) throws IOException {
//        Scanner sc = new Scanner(System.in);
//        at.preparar();
//        at.guardar(sc.nextLine());
//        System.out.println("comenzando lectura...");
//        at.leer();
//    }
}
