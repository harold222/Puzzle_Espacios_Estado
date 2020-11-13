package logica;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Busqueda {

    private Busqueda8Puzzle puzzleInicio;
    private Busqueda8Puzzle puzzleFin;
    private Map<String, Busqueda8Puzzle> nodosGenerados;
    private int nodosAnalizados = 0;

    public Busqueda(Busqueda8Puzzle puzzleInicio, Busqueda8Puzzle puzzleFin) {
        // inicializo las variables con los puzzles de la otra clase que ya tengo los valores
        this.puzzleInicio = puzzleInicio;
        this.puzzleFin = puzzleFin;
    }
    
    // Funcion para regresar la cantidad de nodos que se realizaron en total
    public Integer leeNodosAnalizados() {
        return nodosAnalizados;
    }

    public void realizarBusquedaAnchura() throws IOException {
        // guardo la llave del estado actual con la matriz dada
        nodosGenerados = new HashMap<String, Busqueda8Puzzle>();
        Queue<Busqueda8Puzzle> colaAuxiliar = new LinkedList<Busqueda8Puzzle>();
        //añado en la cola el puzzle para comenzar
        colaAuxiliar.add(puzzleInicio);
        while (colaAuxiliar.size() != 0) {
            //voy eliminando la cabeza de la cola
            Busqueda8Puzzle nodo = colaAuxiliar.poll();
            nodosAnalizados++; // incremento los nodos analizados
            nodo.dibujar(); 
            // si el nodo actual que estoy analizando es igual al nodo final 123456780, termino
            if (nodo.generatId().equals(puzzleFin.generatId())) 
                break;
            else {
                List<Busqueda8Puzzle> nuevosEstados = nodo.determinaEstados();

                for (int i = nuevosEstados.size() - 1; i >= 0; i--) {
                    // dibujo el ultimo estado generado
                    if (nuevosEstados.get(i).generatId().equals(puzzleFin.generatId())) {
                        puzzleFin.dibujar();
                        colaAuxiliar.clear();
                        break;
                    }
                    // si no se repite un estado ya dado
                    if (!nuevosEstados.get(i).generatId().equals(puzzleInicio.generatId()) && !nodosGenerados.containsKey(nuevosEstados.get(i).generatId())) {
                        colaAuxiliar.add(nuevosEstados.get(i));
                        System.out.println(i);
                        nodosGenerados.put(nuevosEstados.get(i).generatId(), nuevosEstados.get(i));
                    }
                }
            }
        }
    }
}
