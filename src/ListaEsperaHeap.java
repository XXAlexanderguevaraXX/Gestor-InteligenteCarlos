import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListaEsperaHeap {
    private List<Alumno> heap;
    private Comparator<Alumno> comparador;

    public ListaEsperaHeap(Comparator<Alumno> comparador) {
        this.heap = new ArrayList<>();
        this.comparador = comparador;
    }

    // Método para obtener el tamaño
    public int size() {
        return heap.size();
    }

    // Método para verificar si está vacío
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    // Método para insertar un elemento (offer)
    public void offer(Alumno alumno) {
        heap.add(alumno);
        siftUp(heap.size() - 1);
    }

    // Método para eliminar el máximo (poll)
    public Alumno poll() {
        if (isEmpty()) return null;
        if (heap.size() == 1) return heap.remove(0);

        // Mover el último elemento a la raíz
        Alumno maximo = heap.get(0);
        heap.set(0, heap.remove(heap.size() - 1));
        siftDown(0);
        return maximo;
    }

    // --- Lógica del Heap (Implementación Manual) ---

    // Mueve el elemento hacia arriba para restaurar la propiedad del Heap
    private void siftUp(int index) {
        int padreIndex = (index - 1) / 2;
        while (index > 0 && comparador.compare(heap.get(index), heap.get(padreIndex)) > 0) {
            intercambiar(index, padreIndex);
            index = padreIndex;
            padreIndex = (index - 1) / 2;
        }
    }

    // Mueve el elemento hacia abajo para restaurar la propiedad del Heap
    private void siftDown(int index) {
        int tamano = heap.size();
        while (index < tamano / 2) { // Mientras tenga al menos un hijo
            int hijoIzquierdo = 2 * index + 1;
            int hijoDerecho = 2 * index + 2;
            int hijoMayor = hijoIzquierdo;

            // Determinar cuál es el hijo mayor (o el único hijo si el derecho no existe)
            if (hijoDerecho < tamano && comparador.compare(heap.get(hijoDerecho), heap.get(hijoIzquierdo)) > 0) {
                hijoMayor = hijoDerecho;
            }

            // Si el elemento actual es menor que el hijo mayor, intercambiar
            if (comparador.compare(heap.get(index), heap.get(hijoMayor)) < 0) {
                intercambiar(index, hijoMayor);
                index = hijoMayor;
            } else {
                break; // El Heap está restaurado en este subárbol
            }
        }
    }

    private void intercambiar(int i, int j) {
        Alumno temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    // Método de utilidad para iterar y mostrar (no garantiza el orden)
    public List<Alumno> toList() {
        return new ArrayList<>(heap);
    }

    // Método para verificar si un alumno ya está en la lista (necesario para la inscripción)
    public boolean contains(Alumno alumno) {
        return heap.contains(alumno);
    }
}