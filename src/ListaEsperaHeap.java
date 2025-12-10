import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// --- ESTRUCTURA DE DATOS: MONÍTCULO (HEAP) ---
// Esta clase implementa una "Fila de Espera Inteligente".
// A diferencia de una fila normal (donde el primero que llega es el primero en ser atendido),
// aquí el sistema reorganiza la fila automáticamente para que el alumno con MAYOR PRIORIDAD
// (mejor promedio/semestre) siempre esté en la primera posición, listo para entrar.
public class ListaEsperaHeap {

    // Es la lista interna donde guardamos a los alumnos esperando.
    // Visualmente imagina que es un árbol genealógico, aunque aquí se guarde en una lista plana.
    private List<Alumno> heap;

    // Es el "Juez" o reglamento. Nos dice quién es más importante que quién
    // para decidir si un alumno debe adelantarse en la fila.
    private Comparator<Alumno> comparador;

    public ListaEsperaHeap(Comparator<Alumno> comparador) {
        this.heap = new ArrayList<>();
        this.comparador = comparador; // Guardamos las reglas de prioridad (Promedio > Semestre)
    }

    // --- Navegación del Árbol ---
    // Estos métodos matemáticos nos ayudan a movernos por la estructura jerárquica.
    // Imaginamos que cada alumno tiene un "padre" (alguien con más prioridad encima de él)
    // y "hijos" (gente con menos prioridad debajo).

    private int padre(int i) {
        return (i - 1) / 2; // Ubica al superior inmediato
    }

    private int hijoIzq(int i) {
        return 2 * i + 1;
    }

    private int hijoDer(int i) {
        return 2 * i + 2;
    }

    // Método auxiliar para cambiar de lugar a dos alumnos en la fila
    private void intercambiar(int i, int j) {
        Alumno temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /**
     * INGRESAR A LA LISTA (offer)
     * Cuando llega un nuevo alumno a la espera:
     * 1. Lo ponemos al final de la cola.
     * 2. Inmediatamente comparamos sus notas con las del alumno que tiene delante ("padre").
     * 3. Si el nuevo tiene mejores notas, intercambian lugares. Esto se repite hasta que encuentre su lugar correcto.
     */
    public void offer(Alumno alumno) {
        // Paso 1: Entra al final
        heap.add(alumno);
        int i = heap.size() - 1;

        // Paso 2 y 3: "Subir" de categoría (Escalar posiciones)
        // Mientras no sea el jefe (índice 0) Y sea mejor que su superior... sube.
        while (i > 0 && comparador.compare(heap.get(i), heap.get(padre(i))) > 0) {
            intercambiar(i, padre(i)); // El alumno "salta" una posición hacia adelante
            i = padre(i);
        }
    }

    /**
     * ATENDER AL SIGUIENTE (poll)
     * Cuando se libera un cupo en el curso:
     * 1. Sacamos al alumno que está en la cima (el de mejor promedio).
     * 2. Ponemos al último de la fila en la cima temporalmente.
     * 3. "Reordenamos" hacia abajo: Si este alumno temporal tiene menos méritos que los que están debajo,
     *    baja posiciones hasta que la jerarquía se restaure.
     */
    public Alumno poll() {
        if (isEmpty()) return null; // Nadie esperando
        if (heap.size() == 1) return heap.remove(0); // Solo había uno, se va directo

        // Guardamos al "Ganador" (el de mayor prioridad) para devolverlo al final
        Alumno maximo = heap.get(0);

        // Movemos al último de la fila al puesto de honor (temporalmente)
        heap.set(0, heap.remove(heap.size() - 1));

        // Reorganización descendente (Hundir)
        // El alumno que subimos baja niveles si encuentra a alguien con más mérito debajo de él.
        int i = 0;
        int tam = heap.size();

        while (i < tam / 2) {
            int hijoMayor = hijoIzq(i);
            int der = hijoDer(i);

            // Buscar cuál de los dos "subordinados" tiene más derecho a subir
            if (der < tam && comparador.compare(heap.get(der), heap.get(hijoMayor)) > 0) {
                hijoMayor = der;
            }

            // Si el jefe actual tiene menos mérito que el subordinado, cambian puesto
            if (comparador.compare(heap.get(i), heap.get(hijoMayor)) < 0) {
                intercambiar(i, hijoMayor);
                i = hijoMayor;
            } else {
                break; // El orden es correcto, terminamos.
            }
        }
        return maximo; // Entregamos al alumno que ganó el cupo
    }

    // --- Herramientas de Consulta ---

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    // Convierte la estructura compleja en una lista simple para poder imprimirla en pantalla
    public List<Alumno> toList() {
        return new ArrayList<>(heap);
    }

    // Verifica si un alumno específico ya está haciendo fila
    public boolean contains(Alumno alumno) {
        return heap.contains(alumno);
    }
}