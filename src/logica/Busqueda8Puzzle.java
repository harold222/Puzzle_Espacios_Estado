package logica;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import puzzle.secuenciaObtenida;

public class Busqueda8Puzzle{

    private int matriz[][] = new int[3][3];
    
    public Busqueda8Puzzle() {}

    public Busqueda8Puzzle(String ficha) {
        String[] numeroficha = ficha.split(",");
        
        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {
                int col = columna;
                if (fila == 1) {
                    col = columna + 3;
                }
                if (fila == 2) {
                    col = columna + 6;
                }
                this.matriz[fila][columna] = Integer.parseInt(numeroficha[col]);
                this.matriz[fila][columna] = Integer.parseInt(numeroficha[col]); //inicializo la matriz
                this.matriz[fila][columna] = Integer.parseInt(numeroficha[col]);
            }
        }
        
    }

    // Movimientos posibles que se pueden generar con  el 0
    
    public void mueveDerecha(int fila, int col) {
        int ficha = this.matriz[fila][col];
        this.matriz[fila][col] = this.matriz[fila][col + 1];
        this.matriz[fila][col + 1] = ficha;
    }

    public void mueveIzquierda(int fila, int col) {
        int ficha = this.matriz[fila][col];
        this.matriz[fila][col] = this.matriz[fila][col - 1];
        this.matriz[fila][col - 1] = ficha;
    }

    public void mueveArriba(int fila, int col) {
        int ficha = this.matriz[fila][col];
        this.matriz[fila][col] = this.matriz[fila - 1][col];
        this.matriz[fila - 1][col] = ficha;
    }

    public void mueveAbajo(int fila, int col) {
        int ficha = this.matriz[fila][col];
        this.matriz[fila][col] = this.matriz[fila + 1][col];
        this.matriz[fila + 1][col] = ficha;
    }

    // genero la llave con la matriz en el estado que solicite para que no se repita
    public String generatId() {
        StringBuffer salida = new StringBuffer();
        for (int fila = 0; fila < 3; fila++) {
            for (int col = 0; col < 3; col++) {
                salida.append(matriz[fila][col]);
                // si no es la ultima posicion
                if (!(fila == 2 && col == 2)) 
                    salida.append(",");
            }
        }
        
        return salida.toString();
    }

    public Integer ubicaFila() {
        Integer posicion = -1;
        for (int fila = 0; fila < 3; fila++) {
            for (int col = 0; col < 3; col++) {
                if (matriz[fila][col] == 0) {
                    posicion = fila;
                }
            }
        }
        return posicion;
    }

    public Integer ubicaColumna() {
        Integer posicion = -1;
        for (int fila = 0; fila < 3; fila++) {
            for (int col = 0; col < 3; col++) {
                if (matriz[fila][col] == 0) posicion = col;
            }
        }
        return posicion;
    }

    public List<Busqueda8Puzzle> determinaEstados() {
        List<Busqueda8Puzzle> estados = new ArrayList<Busqueda8Puzzle>();
        Integer filaMatriz = ubicaFila();
        Integer columnaMatriz = ubicaColumna();
        
        // Verificar posicion 0 en la matriz
        Busqueda8Puzzle pEst;
        if (filaMatriz == 0) {
            pEst = new Busqueda8Puzzle(generatId());
            pEst.mueveAbajo(filaMatriz, columnaMatriz);
            estados.add(pEst);
        }
        if (filaMatriz == 1) {
            pEst = new Busqueda8Puzzle(generatId());
            pEst.mueveAbajo(filaMatriz, columnaMatriz);
            estados.add(pEst);
        }
        if (filaMatriz == 1) {
            pEst = new Busqueda8Puzzle(generatId());
            pEst.mueveArriba(filaMatriz, columnaMatriz);
            estados.add(pEst);
        }
        if (filaMatriz == 2) {
            pEst = new Busqueda8Puzzle(generatId());
            pEst.mueveArriba(filaMatriz, columnaMatriz);
            estados.add(pEst);
        }
        if (columnaMatriz == 0) {
            pEst = new Busqueda8Puzzle(generatId());
            pEst.mueveDerecha(filaMatriz, columnaMatriz);
            estados.add(pEst);

        }
        if (columnaMatriz == 1) {
            pEst = new Busqueda8Puzzle(generatId());
            pEst.mueveDerecha(filaMatriz, columnaMatriz);
            estados.add(pEst);

        }
        if (columnaMatriz == 2) {
            pEst = new Busqueda8Puzzle(generatId());
            pEst.mueveIzquierda(filaMatriz, columnaMatriz);
            estados.add(pEst);
        }
        if (columnaMatriz == 1) {
            pEst = new Busqueda8Puzzle(generatId());
            pEst.mueveIzquierda(filaMatriz, columnaMatriz);
            estados.add(pEst);
        }

        return estados;
    }

    public void dibujar() throws IOException{
        int cont  = 0;
        int a[] = new int [9];
        
        for(int i = 0; i < matriz.length; i++) {
            for(int j = 0; j < matriz[i].length; j++) {
                a[cont] = matriz[i][j]; 
                cont++;
            }
        }
        
        secuenciaObtenida lista = new secuenciaObtenida();
        
        lista.setSecuencia(Arrays.toString(a));
        lista.dibujar();
    }
    
}