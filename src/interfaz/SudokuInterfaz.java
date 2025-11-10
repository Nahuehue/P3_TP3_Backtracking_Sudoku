package interfaz;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import logica.Solver; 
import logica.Generador;

public class SudokuInterfaz extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int TAMANIO = 9;
    private JPanel panelContenido;
    private JTextField[][] camposTexto = new JTextField[TAMANIO][TAMANIO];
    
    private Solver solver = new Solver(); 
    private Generador generador = new Generador();
    private JSpinner selectorPrefijados; 

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    SudokuInterfaz ventana = new SudokuInterfaz();
                    ventana.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public SudokuInterfaz() {
        setTitle("Solucionador de Sudoku (TP3)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 480, 580); 
        panelContenido = new JPanel();
        panelContenido.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(panelContenido);
        panelContenido.setLayout(null); 

        // === NUEVO: Texto de Ayuda ===
        JLabel lblInstruccion = new JLabel("Ingrese un tablero manualmente o genere uno aleatorio.");
        lblInstruccion.setFont(new Font("Tahoma", Font.PLAIN, 12));
        lblInstruccion.setHorizontalAlignment(SwingConstants.CENTER);
        lblInstruccion.setBounds(30, 5, 405, 15);
        panelContenido.add(lblInstruccion);

        // --- Panel de la Grilla (un poco más abajo) ---
        JPanel panelGrilla = new JPanel();
        panelGrilla.setBounds(30, 25, 405, 405); // Ajustado
        panelContenido.add(panelGrilla);
        panelGrilla.setLayout(new GridLayout(TAMANIO, TAMANIO, 4, 4));

        Font fuente = new Font("Arial", Font.BOLD, 20);
        for (int fila = 0; fila < TAMANIO; fila++) {
            for (int col = 0; col < TAMANIO; col++) {
                camposTexto[fila][col] = new JTextField();
                camposTexto[fila][col].setFont(fuente);
                camposTexto[fila][col].setHorizontalAlignment(SwingConstants.CENTER);
                
                if (((fila / 3) + (col / 3)) % 2 == 0) {
                    camposTexto[fila][col].setBackground(new Color(220, 220, 220));
                }
                panelGrilla.add(camposTexto[fila][col]);
            }
        }

        // === Fila 1 de Botones (Acciones) ===
        JButton btnResolver = new JButton("Resolver");
        btnResolver.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnResolver.setBounds(30, 450, 120, 30);
        btnResolver.addActionListener(e -> resolverSudoku());
        panelContenido.add(btnResolver);

        // === NUEVO: Botón Validar ===
        JButton btnValidar = new JButton("Validar");
        btnValidar.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnValidar.setBounds(160, 450, 120, 30);
        btnValidar.addActionListener(e -> validarTablero());
        panelContenido.add(btnValidar);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnLimpiar.setBounds(290, 450, 120, 30); // Movido
        btnLimpiar.addActionListener(e -> limpiarTablero()); 
        panelContenido.add(btnLimpiar);

        // === Fila 2 de Botones (Generación) ===
        JLabel etiquetaPrefijados = new JLabel("Valores Prefijados:");
        etiquetaPrefijados.setBounds(30, 500, 120, 14); 
        panelContenido.add(etiquetaPrefijados);
        
        selectorPrefijados = new JSpinner();
        selectorPrefijados.setModel(new SpinnerNumberModel(25, 17, 60, 1));
        selectorPrefijados.setBounds(160, 495, 70, 20); 
        panelContenido.add(selectorPrefijados);

        JButton btnGenerar = new JButton("Generar Aleatorio");
        btnGenerar.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnGenerar.setBounds(290, 490, 145, 30); 
        btnGenerar.addActionListener(e -> generarSudoku());
        panelContenido.add(btnGenerar);
    }
    
    // ... (generarSudoku, resolverSudoku, limpiarTablero, leer..., actualizar...) ...
    // Esos métodos no cambian.
    
    /**
     * NUEVO: Llama al Solver para validar el tablero ingresado
     * sin intentar resolverlo.
     */
    private void validarTablero() {
        try {
            int[][] tablero = leerTableroDeInterfaz();
            
            // Usamos el método que hicimos público en el Solver
            if (solver.esTableroValido(tablero)) {
                JOptionPane.showMessageDialog(this, "El tablero ingresado es válido.", "Validación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Error: El tablero tiene números repetidos\nen una fila, columna o bloque.", "Tablero Inválido", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: Ingresa solo números del 1 al 9.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarSudoku() {
        int cantidad = (Integer) selectorPrefijados.getValue();
        int[][] tablero = generador.generar(cantidad);
        actualizarInterfazDesdeTablero(tablero);
    }

    private void resolverSudoku() {
        try {
            int[][] tablero = leerTableroDeInterfaz();

            if (solver.resolver(tablero)) { 
                actualizarInterfazDesdeTablero(tablero);
                JOptionPane.showMessageDialog(this, "¡Sudoku Resuelto!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Ahora, el usuario sabe si es inválido (por el botón Validar)
                // o si simplemente no tiene solución.
                JOptionPane.showMessageDialog(this, "No se encontró solución para este tablero.", "Fallo", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: Ingresa solo números del 1 al 9.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarTablero() {
        for (int fila = 0; fila < TAMANIO; fila++) {
            for (int col = 0; col < TAMANIO; col++) {
                camposTexto[fila][col].setText("");
            }
        }
    }

    private int[][] leerTableroDeInterfaz() throws NumberFormatException {
        int[][] tablero = new int[TAMANIO][TAMANIO];
        for (int fila = 0; fila < TAMANIO; fila++) {
            for (int col = 0; col < TAMANIO; col++) {
                String texto = camposTexto[fila][col].getText();
                if (texto.isEmpty()) {
                    tablero[fila][col] = 0;
                } else {
                    int numero = Integer.parseInt(texto);
                    if (numero < 1 || numero > 9) throw new NumberFormatException();
                    tablero[fila][col] = numero;
                }
            }
        }
        return tablero;
    }

    private void actualizarInterfazDesdeTablero(int[][] tablero) {
        for (int fila = 0; fila < TAMANIO; fila++) {
            for (int col = 0; col < TAMANIO; col++) {
                if (tablero[fila][col] == 0) {
                    camposTexto[fila][col].setText("");
                } else {
                    camposTexto[fila][col].setText(String.valueOf(tablero[fila][col]));
                }
            }
        }
    }
}