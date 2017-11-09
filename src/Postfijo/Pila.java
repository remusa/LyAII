/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Postfijo;

/**
 *
 * @author curso
 */
public class Pila {

    private int tope;
    private String[] arreglo;

    public Pila(int longuitud) {
        arreglo = new String[longuitud];
        tope = -1;

    }

    public void push(String dato) {//agregar un nuevo dato a la pila
        tope++;
        arreglo[tope] = dato;
    }

    public String pop() {//eliminar y conocer el valor eliminado
        String temporal;

        temporal = arreglo[tope];
        tope--;
        return temporal;
    }

    public String stacktop() {//conocer el valor que se encuentra en el tope
        return arreglo[tope];

    }

    public boolean empty() {//pila vacia si es asi retornar true si no esta vacia retornara falso
        if (tope == -1) {
            return true;
        } else {
            return false;
        }

    }
    public boolean full(){//verifica si la pila esta llena
        if(tope==arreglo.length-1)
            return true;
        else 
            return false;
    }
}
