package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Importamos la clase que vamos a probar
import logica.Solver; // <-- Importa Solver

/**
 * Pruebas unitarias para la clase Solver.
 */
public class SolverTest { // Nombre de la clase cambiado a SolverTest

    // El objeto a probar
    private Solver solver; // <-- Usa el tipo Solver

    @BeforeEach

    public void inicializar() {
        solver = new Solver(); // <-- Crea instancia de Solver
    }

    /**
     * Prueba 1: Un tablero estándar que sabemos que tiene solución.
     */
    @Test
    public void pruebaSudokuSolucionable() {
        int[][] tablero = {
            {5, 3, 0, 0, 7, 0, 0, 0, 0},
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            {0, 9, 8, 0, 0, 0, 0, 6, 0},
            {8, 0, 0, 0, 6, 0, 0, 0, 3},
            {4, 0, 0, 8, 0, 3, 0, 0, 1},
            {7, 0, 0, 0, 2, 0, 0, 0, 6},
            {0, 6, 0, 0, 0, 0, 2, 8, 0},
            {0, 0, 0, 4, 1, 9, 0, 0, 5},
            {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        int[][] tableroResuelto = {
            {5, 3, 4, 6, 7, 8, 9, 1, 2},
            {6, 7, 2, 1, 9, 5, 3, 4, 8},
            {1, 9, 8, 3, 4, 2, 5, 6, 7},
            {8, 5, 9, 7, 6, 1, 4, 2, 3},
            {4, 2, 6, 8, 5, 3, 7, 9, 1},
            {7, 1, 3, 9, 2, 4, 8, 5, 6},
            {9, 6, 1, 5, 3, 7, 2, 8, 4},
            {2, 8, 7, 4, 1, 9, 6, 3, 5},
            {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };

        // Llama al método resolver de la instancia de Solver
        assertTrue(solver.resolver(tablero), "El solver debería encontrar una solución");

        for(int i = 0; i < 9; i++) {
            assertArrayEquals(tableroResuelto[i], tablero[i], "La fila " + i + " no es correcta");
        }
    }

    /**
     * Prueba 2: Un tablero imposible de resolver.
     */
    @Test
    public void pruebaSudokuSinSolucion() {
        int[][] tablero = {
            {9, 9, 0, 0, 7, 0, 0, 0, 0}, // Inválido
            {6, 0, 0, 1, 9, 5, 0, 0, 0},
            // ... (resto del tablero)
        };
        assertFalse(solver.resolver(tablero), "El solver no debería encontrar solución");
    }

    /**
     * Prueba 3: Un tablero completamente vacío.
     */
    @Test
    public void pruebaTableroVacio() {
        int[][] tablero = new int[9][9]; 
        assertTrue(solver.resolver(tablero), "Un tablero vacío debe tener solución");
    }
}