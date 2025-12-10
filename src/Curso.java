import java.util.Set;

// Representa una materia o asignatura que ofrece la universidad.
// Esta clase controla su propia información, cuántos asientos quedan libres y qué temas abarca.
public class Curso {

    // --- Datos de Identificación ---
    private String idCurso;      // Código único de la materia (ej: "MAT101")
    private String nombre;       // Título de la clase
    private String docente;      // Nombre del profesor encargado

    // --- Gestión de Capacidad (Cupos) ---
    private int cupoMaximo;      // El límite total de alumnos permitidos
    private int cuposDisponibles;// Cuántos asientos quedan libres en este momento

    // --- Datos Académicos ---
    private int creditos;        // Valor académico o "peso" de la materia
    // Etiquetas o temas que cubre el curso (ej: "Matemáticas", "Lógica").
    // Sirve para que el sistema sepa si recomendarlo o no.
    private Set<String> areas;

    // --- Constructor ---
    // Configura la materia nueva. Al crearse, los cupos disponibles son iguales
    // al máximo permitido porque aún no hay nadie inscrito.
    public Curso(String idCurso, String nombre, String docente, int cupoMaximo, int creditos, Set<String> areas) {
        this.idCurso = idCurso;
        this.nombre = nombre;
        this.docente = docente;
        this.cupoMaximo = cupoMaximo;
        this.cuposDisponibles = cupoMaximo; // Inicia totalmente vacío/disponible
        this.creditos = creditos;
        this.areas = areas;
    }

    // --- Métodos de Consulta (Getters) ---
    // Permiten al resto del sistema ver la información de la materia sin alterarla.
    public String getIdCurso() { return idCurso; }
    public String getNombre() { return nombre; }
    public String getDocente() { return docente; }
    public int getCupoMaximo() { return cupoMaximo; }
    public int getCuposDisponibles() { return cuposDisponibles; }
    public int getCreditos() { return creditos; }
    public Set<String> getAreas() { return areas; }

    // --- Gestión de Asientos (Inventario) ---

    // Intenta ocupar un asiento cuando un alumno se quiere inscribir.
    // Retorna 'verdadero' si hubo lugar, o 'falso' si la clase ya estaba llena.
    public boolean disminuirCupo() {
        if (cuposDisponibles > 0) {
            cuposDisponibles--;
            return true; // Éxito: Se ocupó un lugar
        }
        return false; // Fallo: No hay cupo
    }

    // Libera un asiento cuando un alumno se da de baja.
    // Tiene un control de seguridad para no liberar más asientos de los que existen físicamente.
    public void aumentarCupo() {
        if (cuposDisponibles < cupoMaximo) {
            cuposDisponibles++;
        }
    }

    // --- Visualización ---
    // Muestra el estado actual del curso de forma legible, útil para ver rápidamente
    // cuántos lugares quedan (ej: "Cupo: 5/30").
    @Override
    public String toString() {
        return "ID: " + idCurso + " | Nombre: " + nombre + " | Docente: " + docente +
                " | Cupo: " + cuposDisponibles + "/" + cupoMaximo + " | Créditos: " + creditos +
                " | Áreas: " + areas;
    }
}