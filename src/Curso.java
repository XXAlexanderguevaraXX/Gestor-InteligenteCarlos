import java.util.Set;

public class Curso {
    private String idCurso;
    private String nombre;
    private String docente;
    private int cupoMaximo;
    private int cuposDisponibles;
    private int creditos;
    private Set<String> areas; // Conjuntos: Áreas/temas del curso.

    public Curso(String idCurso, String nombre, String docente, int cupoMaximo, int creditos, Set<String> areas) {
        this.idCurso = idCurso;
        this.nombre = nombre;
        this.docente = docente;
        this.cupoMaximo = cupoMaximo;
        this.cuposDisponibles = cupoMaximo;
        this.creditos = creditos;
        this.areas = areas;
    }

    // Getters
    public String getIdCurso() { return idCurso; }
    public String getNombre() { return nombre; }
    public String getDocente() { return docente; }
    public int getCupoMaximo() { return cupoMaximo; }
    public int getCuposDisponibles() { return cuposDisponibles; }
    public int getCreditos() { return creditos; }
    public Set<String> getAreas() { return areas; }

    // Métodos de utilidad para cupos
    public boolean disminuirCupo() {
        if (cuposDisponibles > 0) {
            cuposDisponibles--;
            return true;
        }
        return false;
    }

    public void aumentarCupo() {
        if (cuposDisponibles < cupoMaximo) {
            cuposDisponibles++;
        }
    }

    @Override
    public String toString() {
        return "ID: " + idCurso + " | Nombre: " + nombre + " | Docente: " + docente +
                " | Cupo: " + cuposDisponibles + "/" + cupoMaximo + " | Créditos: " + creditos +
                " | Áreas: " + areas;
    }
}