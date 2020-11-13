package puzzle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import logica.Busqueda;
import logica.Busqueda8Puzzle;

public class Puzzle extends JPanel {
    
    /*----------Variables para la interfaz------------*/
        public static JFrame frame;
        // tamano del talbero del puzzle, por defecto de 3*3
        private int tamanoPuzzle = 3;
        //numero de celdas en el puzzle
        private int celdasPuzzle = 8;
        // dimension de las celdas en la interfaz
        private int dimensionCeldas = 450;
        // margen que tendra el tablero
        private int margen = 15;
        // Tamaño general del tablero
        private int tamanoGrid = 420;
        // tamaño que tendra cada celda
        private int tamanoCeldas = 140;
        //panel para los botones
        private static JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        
        private JButton buttonNuevoJuego;
        private JButton buttonSolve;
        private JTextField textoMovimientos;
        
   /*----------Variables para la creacion del puzzle------------*/
  
        // Random object to shuffle tiles
        private static final Random RANDOM = new Random();
        // array donde guardare los numeros del puzzle
        private int[] arregloCeldas = new int[9];
        // celda que sera la ultimo valor
        private int celdaEnBlanco = 8;
        
    /*----------Variables para la logica del programa------------*/
        private boolean juegoTerminado;
  
        
  public Puzzle() {
    setPreferredSize(new Dimension(dimensionCeldas, dimensionCeldas + margen));
    // color del fondo
    setBackground(Color.BLACK);
    // Color de las celdas
    setForeground(new Color(200, 3, 50));
    //tipo de letra y tamaño letra
    setFont(new Font("SansSerif", Font.BOLD, 50));
    //Creacion de los dos botones 
    buttonNuevoJuego = new JButton("Nuevo Juego");
    buttonSolve = new JButton("Solucionar");
    
    buttonSolve.setEnabled(false);

    // inicializo los botones con funciones a ejecutar
    initBotones();
  }
  
  private void initBotones(){
    // añado la accion al dar click a este boton
    buttonNuevoJuego.addActionListener(new ActionListener() {		
        public void actionPerformed(ActionEvent e) {
            //creo un nuevo juego valido 
            nuevoJuego();
            //dibujo el puzzle, funcion propia de Jframe
            repaint();
            // activo el boton
            buttonSolve.setEnabled(true);
        }	
    });
    
    buttonSolve.addActionListener(new ActionListener() {		
        public void actionPerformed(ActionEvent e) {
            buttonNuevoJuego.setEnabled(false);
            buttonSolve.setEnabled(false);
            try {
                solucionarAlgoritmo();
            } catch (IOException ex) {
                Logger.getLogger(Puzzle.class.getName()).log(Level.SEVERE, null, ex);
            }
        }	
    });
    
    // creo elementos para el panel de la interfaz
    Font fuente=new Font("Dialog", Font.BOLD, 18);
    
    textoMovimientos = new JTextField(15);
    // estilo al JTextField
    textoMovimientos.setEnabled(false);
    textoMovimientos.setFont(fuente);
    textoMovimientos.setDisabledTextColor(Color.black);
            
    //añado estos botones al panel y el texto de los movimientos generados
    panel2.add(textoMovimientos);
    panel2.add(buttonNuevoJuego);
    panel2.add(buttonSolve);
  }

    private void nuevoJuego() {
        //genero los numeros del 1 al 8 y 0
        generarNumeros();
        //creo el juego valido con los movimientos
        caos();
        //inicio el juego como no terminado
        juegoTerminado = false;
        // vuelvo el tablero sin el mensaje
        repaint();
    }
  
    private int[][] pasarAMatriz(){
        int matriz[][] = new int[3][3];
        int cont = 0;

        for (int x = 0; x < 3; x++) {
          for (int y = 0; y < 3; y++) {
            matriz[x][y] = arregloCeldas[cont];
            cont++;
          }
        }
        
        return matriz;
    }
    
    public void pasarAVector(int matriz[][]) {
        int cont  = 0;
        
        for(int i = 0; i<matriz.length; i++) {
        // remplazamos i por j , condición del for
            for(int j = 0; j < matriz[i].length; j++) {
                arregloCeldas[cont] = matriz[i][j]; 
                cont++;
            }
        }
    }
    
    private void solucionarAlgoritmo() throws IOException{
        
        if(EstaSolucionado()){
            juegoTerminado = true;
            buttonNuevoJuego.setEnabled(true);
            buttonSolve.setEnabled(true);
        } else {
            String puzzleInicial = "";
            
            for (int i = 0; i < 9; i++) 
                puzzleInicial += arregloCeldas[i]+",";
            
            Busqueda8Puzzle puzzleInicio = new Busqueda8Puzzle(puzzleInicial);
            Busqueda8Puzzle puzzleFin = new Busqueda8Puzzle("1,2,3,4,5,6,7,8,0");
            
            Busqueda busqueda = new Busqueda(puzzleInicio, puzzleFin);
            
            busqueda.realizarBusquedaAnchura();

            if(busqueda.leeNodosAnalizados() != 0){
                System.out.println("Nodos Analizados :" + busqueda.leeNodosAnalizados());
                
                    Thread b = new Thread(new Runnable() {
                    @Override
                        public void run() {
                            try {
                                secuenciaFinalizada();
                                //compruebo que la respuesta este terminada
                                juegoTerminado = EstaSolucionado();
                                        
                                if(juegoTerminado){
                                    buttonNuevoJuego.setEnabled(true);
                                    buttonSolve.setEnabled(true);
                                    repaint();
                                }
                            } catch (InterruptedException ex) {
                                Logger.getLogger(Puzzle.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                            Logger.getLogger(Puzzle.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        }
                    });

                b.setName("second_thread");
                b.start();
                        
            }
        }
    }
    
    private void secuenciaFinalizada() throws InterruptedException, IOException{
        Thread a = new Thread(new Runnable() {
            @Override
                public void run() {
                    FileReader fr = null;
                    
                    try {
                        File archivo = new File("C:\\Temp\\secuencia.txt");
                        fr = new FileReader(archivo);
                        BufferedReader br = new BufferedReader(fr);

                        String linea;
                        while((linea=br.readLine())!=null){
                            System.out.println(linea);
                            arregloCeldas = Arrays.stream(linea.substring(1, linea.length()-1).split(","))
                                .map(String::trim).mapToInt(Integer::parseInt).toArray();
                            
                            repaint(); // dibujo la interfaz nuevamente
                            
                            Thread.sleep(1000);
                        }
                     }catch(Exception e){e.printStackTrace();
                    }finally{
                        try{                    
                           if( null != fr ) fr.close();     
                        }catch (Exception e2){e2.printStackTrace();}
                    }
                }
            });

        a.setName("fist_thread");
        a.start();
        a.join();  
        
        String ruta = "C:\\Temp\\secuencia.txt";
        File archivo = new File(ruta);
        BufferedWriter bw;

        bw = new BufferedWriter(new FileWriter(archivo));
        bw.write("");
        bw.close();
        
    }
  
    private void generarNumeros() {
        for (int i = 0; i < arregloCeldas.length-1; i++)
            arregloCeldas[i] = i+1; //genero numeros del 1 al 8
        arregloCeldas[arregloCeldas.length-1] = 0; // el ultimo numero es el 0
    }
  
    private void caos(){
        Random r = new Random();
        //creo la variable matriz
        int matriz[][] = new int [3][3];
        //inicializo la matriz con el estado inicial que es 1 2 3 4 5 6 7 8 0
        matriz = pasarAMatriz();
        // creo las variables de cada lado para hacerlo random
        int L =1, D = 2, R = 3, U = 4;
        // en esta variable guardare los movimientos que se generaron
        String MovimientosGenerados = "-";

        //genero numeros aleatorios entre 4 a 10, para los giros que tendra el puzzle
        int cantidadVueltas = r.nextInt(6) + 6;
        
        // variable que guardara el movimiento random que realizara el puzzle
        int movimiento;
        
        for (int i = 0; i <= cantidadVueltas; i++) {
            // genero el movimiento aleatorio
            movimiento = (int) (Math.random()*4)+1;
            switch(movimiento){
                case 1:
                    //valido que el movimiento left se pueda hacer
                    if (matriz[0][0] != 0 && matriz[1][0] != 0 && matriz[2][0] != 0){
                        // guardo la nueva matriz
                        matriz = celdaActualCero(matriz, movimiento);
                        MovimientosGenerados += "L-";
                    }
                    break;
                case 2:
                    //valido que se pueda hacer el movimiento down
                    if(matriz[2][0] != 0 && matriz[2][1] != 0 && matriz[2][2] != 0){
                        // guardo la nueva matriz
                        matriz = celdaActualCero(matriz, movimiento);
                        MovimientosGenerados += "D-";
                    }
                    break;
                case 3:
                    //valido que se pueda hacer el movimiento right
                    if(matriz[0][2] != 0 && matriz[1][2] != 0 && matriz[2][2] != 0){
                        // guardo la nueva matriz
                        matriz = celdaActualCero(matriz, movimiento);
                        MovimientosGenerados += "R-";
                    }
                    break;
                case 4:
                    //valido que se pueda hacer el movimiento up
                    if(matriz[0][0] != 0 && matriz[0][1] != 0 && matriz[0][2] != 0){
                        // guardo la nueva matriz
                        matriz = celdaActualCero(matriz, movimiento);
                        MovimientosGenerados += "U-";
                    }
                    break;
                default: 
                    break;
            }
        }
        // escribo los movimientos generados en el cuadro de texto
        textoMovimientos.setText(MovimientosGenerados);
        // una vez generado el movimiento lo paso nuevamente a un array para dibujarlo en pantalla
        pasarAVector(matriz);
    }
    
    private int[][] celdaActualCero(int matriz[][], int movimiento){
        int x = 0;
        int y = 0;

        switch(movimiento){
            case 1:
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if(matriz[i][j] == 0) {
                            matriz[i][j] = matriz[i][j-1];
                            matriz[i][j-1] = 0;
                            return matriz;
                        }
                    }
                }
                break;
            case 2:
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if(matriz[i][j] == 0) {
                            matriz[i][j] = matriz[i+1][j];
                            matriz[i+1][j] = 0;
                            return matriz;
                        }
                    }
                }
                break;
            case 3:
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if(matriz[i][j] == 0) {
                            matriz[i][j] = matriz[i][j+1];
                            matriz[i][j+1] = 0;
                            return matriz;
                        }
                    }
                }
                break;
            case 4:
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        if(matriz[i][j] == 0) {
                            matriz[i][j] = matriz[i-1][j];
                            matriz[i-1][j] = 0;
                            return matriz;
                        }
                    }
                }
                break;
        }
        
        return matriz;
    }

    private void generarCaos() {
      int n = celdasPuzzle;

      //añado aleatoreamente los numeros en distintas posiciones
      while (n > 1) {
        int r = RANDOM.nextInt(n--);
        int tmp = arregloCeldas[r];

        arregloCeldas[r] = arregloCeldas[n];
        arregloCeldas[n] = tmp;
      }
    }
  
  private boolean EstaSolucionado() {
    if (arregloCeldas[arregloCeldas.length - 1] != 0)
      return false;
    
    for (int i = celdasPuzzle - 1; i >= 0; i--) {
      if (arregloCeldas[i] != i + 1)
        return false;      
    }
    
    return true;
  }
  
  private void dibujarCUadricula(Graphics2D g) {
    for (int i = 0; i < arregloCeldas.length; i++) {
        
      int r = i / tamanoPuzzle;
      int c = i % tamanoPuzzle;
      
      int x = margen + c * tamanoCeldas;
      int y = margen + r * tamanoCeldas;
      
      // check special case for blank tile
      if(arregloCeldas[i] == 0) {
        if (!juegoTerminado) {
          g.setColor(new Color(100, 100, 100));
          drawCenteredString(g,"0", x, y);
        }else{
            g.setColor(new Color(255, 255, 255));
            drawCenteredString(g,"☻", x, y);
        }
        continue;
      }
      
      g.setColor(getForeground());
      g.fillRoundRect(x, y, tamanoCeldas, tamanoCeldas, 200, 200);
      g.setColor(Color.BLACK);
      g.drawRoundRect(x, y, tamanoCeldas, tamanoCeldas, 200, 200);
      g.setColor(Color.WHITE);
      
      drawCenteredString(g, String.valueOf(arregloCeldas[i]), x , y);
    }
  }
  
  private void mensajeFinalizado(Graphics2D g) {
    if (juegoTerminado) {
        //si el juego ya fue terminado, muestro el mensaje
        g.setFont(getFont().deriveFont(Font.BOLD, 18));
        g.setColor(new Color(200, 3, 50));
        String s = "Haz terminado el juego";
        g.drawString(s, (getWidth() - g.getFontMetrics().stringWidth(s)) / 2, getHeight() - margen);
    }
  }
  
  private void drawCenteredString(Graphics2D g, String s, int x, int y) {
    FontMetrics fm = g.getFontMetrics();
    int asc = fm.getAscent();
    int desc = fm.getDescent();
    g.drawString(s,  x + (tamanoCeldas - fm.stringWidth(s)) / 2, 
        y + (asc + (tamanoCeldas - (asc + desc)) / 2));
  }
  
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2D = (Graphics2D) g;
    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    dibujarCUadricula(g2D);
    mensajeFinalizado(g2D);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      Puzzle s = new Puzzle();
      frame = new JFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setTitle("Puzzle 8");
      frame.setResizable(false);
      
      frame.add(s, BorderLayout.CENTER);
      frame.getContentPane().add(panel2, BorderLayout.SOUTH);
      frame.pack();
      
      frame.setLocationRelativeTo(null);
      frame.setVisible(true);
    });
  }
}
