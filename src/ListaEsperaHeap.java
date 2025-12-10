import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListaEsperaHeap {
    // Usamos List para evitar problemas de capacidad fija del array, pero aplicamos la lógica de índices.
    private List<Alumno> heap;
    private Comparator<Alumno> comparador;

    public ListaEsperaHeap(Comparator<Alumno> comparador) {
        this.heap = new ArrayList<>();
        this.comparador = comparador; // El comparador define la MAXIMA prioridad
    }

    // Adaptación de los métodos de índice
    private int padre(int i) {
        return (i - 1) / 2;
    }

    private int hijoIzq(int i) {
        return 2 * i + 1;
    }

    private int hijoDer(int i) {
        return 2 * i + 2;
    }

    // Método de utilidad para intercambiar
    private void intercambiar(int i, int j) {
        Alumno temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /**
     * INSERTAR (offer): Agrega un Alumno y lo sube (heapify up / siftUp)
     */
    public void offer(Alumno alumno) {
        // Coloca el elemento al final
        heap.add(alumno);
        int i = heap.size() - 1;

        // Subida (siftUp): mientras el hijo (i) sea MÁS PRIORITARIO que el padre
        while (i > 0 && comparador.compare(heap.get(i), heap.get(padre(i))) > 0) {
            intercambiar(i, padre(i));
            i = padre(i);
        }
    }

    /**
     * ELIMINAR MÁXIMO (poll): Quita y devuelve el elemento de MÁXIMA prioridad (raíz)
     */
    public Alumno poll() {
        if (isEmpty()) return null;
        if (heap.size() == 1) return heap.remove(0);

        Alumno maximo = heap.get(0);

        // Mueve el último elemento a la raíz
        heap.set(0, heap.remove(heap.size() - 1));

        // Baja el elemento desde la raíz (heapify down / siftDown)
        int i = 0;
        int tam = heap.size();

        while (i < tam / 2) { // Mientras tenga al menos un hijo
            int hijoMayor = hijoIzq(i);
            int der = hijoDer(i);

            // 1. Encuentra el hijo de MAYOR prioridad (el que debe subir)
            if (der < tam && comparador.compare(heap.get(der), heap.get(hijoMayor)) > 0) {
                hijoMayor = der;
            }

            // 2. Si el padre (i) es menos prioritario que el hijoMayor, intercambia
            if (comparador.compare(heap.get(i), heap.get(hijoMayor)) < 0) {
                intercambiar(i, hijoMayor);
                i = hijoMayor;
            } else {
                break; // El Heap está restaurado
            }
        }
        return maximo;
    }

    // --- Métodos de utilidad ---

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    public List<Alumno> toList() {
        return new ArrayList<>(heap);
    }

    public boolean contains(Alumno alumno) {
        return heap.contains(alumno);
    }

    // (Opcional: puedes eliminar imprimirArbol, pero es útil para debugging)
}