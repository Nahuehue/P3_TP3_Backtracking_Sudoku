package logica;

import java.util.Random;
import java.util.Collections; // Para barajar (shuffle)
import java.util.ArrayList;  // Para la lista de números
import java.util.List;        // Para la lista de números

public class Generador {
    
    // El generador sigue necesitando el Solver, pero
    // solo para usar su método esSeguro().
    private Solver solver = new Solver(); 
    private Random aleatorio = new Random();
    private static final int TAMANIO = 9;
    private static final int VACIO = 0;

    /**
     * Genera un tablero con una cantidad específica de números prefijados.
     */
    public int[][] generar(int cantidadPrefijados) {
        int[][] tablero = new int[TAMANIO][TAMANIO];

        // 1. Crear un tablero resuelto de FORMA ALEATORIA
        resolverAleatorio(tablero);

        // 2. Vaciar celdas al azar (esto no cambia)
        int celdasAEliminar = 81 - cantidadPrefijados;
        int eliminadas = 0;

        while (eliminadas < celdasAEliminar) {
            int fila = aleatorio.nextInt(TAMANIO);
            int columna = aleatorio.nextInt(TAMANIO);

            if (tablero[fila][columna] != VACIO) {
                tablero[fila][columna] = VACIO;
                eliminadas++;
            }
        }
        return tablero;
    }

    /**
     * Este es un algoritmo de backtracking que prueba los
     * números (1-9) en un orden aleatorio en lugar de secuencial.
     * Esto garantiza que cada tablero generado sea único.
     */
    private boolean resolverAleatorio(int[][] tablero) {
        for (int fila = 0; fila < TAMANIO; fila++) {
            for (int columna = 0; columna < TAMANIO; columna++) {
                
                // Si la celda está vacía
                if (tablero[fila][columna] == VACIO) {
                    
                    // 1. Crear una lista de números [1, 2, ... 9]
                    List<Integer> numeros = new ArrayList<>();
                    for (int i = 1; i <= TAMANIO; i++) numeros.add(i);
                    
                    // 2. Barajar la lista
                    Collections.shuffle(numeros, aleatorio);
                    // --------------------------

                    // 3. Probar los números en el nuevo orden aleatorio
                    for (int numero : numeros) {
                        
                        // Usamos el método 'esSeguro' del Solver
                        if (solver.esSeguro(tablero, fila, columna, numero)) {
                            
                            tablero[fila][columna] = numero;

                            if (resolverAleatorio(tablero)) {
                                return true;
                            }
                            
                            tablero[fila][columna] = VACIO; // Backtrack
                        }
                    }
                    return false;
                }
            }
        }
        return true; // Tablero completo
    }
}