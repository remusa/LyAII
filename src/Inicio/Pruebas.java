/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inicio;

import java.util.logging.Level;
import java.util.logging.Logger;
import sun.java2d.pipe.AATileGenerator;

/**
 *
 * @author RSG
 */
public class Pruebas implements Runnable {

    int i = 0;

    @Override
    public void run() {
        while (i<600) {
            try {
                System.out.println("HOLA: " + i);
                i++;
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Pruebas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
