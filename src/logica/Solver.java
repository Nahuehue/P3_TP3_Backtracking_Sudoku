package logica;

public class Solver {

    private static final int TAMANIO = 9;
    private static final int VACIO = 0;

    /**
     * Método público para resolver.
     * AHORA: Primero valida el tablero inicial.
     */
    public boolean resolver(int[][] tablero) {
        // 1. NUEVO: Validar los números prefijados primero.
        if (!esTableroValido(tablero)) {
            return false; // Falla rápido si el tablero es inválido
        }
        
        // 2. Si es válido, intenta resolverlo
        return resolverRecursivo(tablero);
    }

    /**
     * Función recursiva de backtracking
     */
    private boolean resolverRecursivo(int[][] tablero) {
        for (int fila = 0; fila < TAMANIO; fila++) {
            for (int columna = 0; columna < TAMANIO; columna++) {
                if (tablero[fila][columna] == VACIO) {
                    for (int numero = 1; numero <= TAMANIO; numero++) {
                        if (esSeguro(tablero, fila, columna, numero)) {
                            tablero[fila][columna] = numero;
                            if (resolverRecursivo(tablero)) {
                                return true;
                            }
                            tablero[fila][columna] = VACIO;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Método para validar los números prefijados.
     * Comprueba que ningún número prefijado viole las reglas.
     */
    public boolean esTableroValido(int[][] tablero) {
        for (int f = 0; f < TAMANIO; f++) {
            for (int c = 0; c < TAMANIO; c++) {
                // Si la celda NO está vacía, la validamos
                if (tablero[f][c] != VACIO) {
                    int numero = tablero[f][c];
                    tablero[f][c] = VACIO; 
                    boolean seguro = esSeguro(tablero, f, c, numero);
                    tablero[f][c] = numero; // Lo restauramos
                    
                    if (!seguro) {
                        return false; // Se encontró una violación
                    }
                }
            }
        }
        return true; // El tablero es válido
    }

    /**
     * Verifica si es seguro poner un 'numero'.
     * No es private para que Generador.java pueda usarlo.
     */
    public boolean esSeguro(int[][] tablero, int fila, int columna, int numero) {
        
        // Comprobar fila
        for (int i = 0; i < TAMANIO; i++) {
            if (tablero[fila][i] == numero) {
                return false;
            }
        }

        // Comprobar columna
        for (int f = 0; f < TAMANIO; f++) {
            if (tablero[f][columna] == numero) {
                return false;
            }
        }

        // Comprobar bloque 3x3
        int raiz = 3;
        int inicioFilaBloque = fila - fila % raiz;
        int inicioColumnaBloque = columna - columna % raiz;

        for (int f = inicioFilaBloque; f < inicioFilaBloque + raiz; f++) {
            for (int c = inicioColumnaBloque; c < inicioColumnaBloque + raiz; c++) {
                if (tablero[f][c] == numero) {
                    return false;
                }
            }
        }
        
        return true;
    }
}