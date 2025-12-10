import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// Representa a un estudiante dentro del sistema universitario.
// Actúa como una "ficha" que guarda tanto su información personal como su historial académico actual.
public class Alumno {

    // --- Datos Identificativos y Académicos ---
    private String idAlumno;    // Matrícula única del estudiante
    private String nombre;      // Nombre completo
    private int semestre;       // Nivel o semestre que cursa actualmente
    private double promedio;    // Calificación promedio (importante para prioridades)

    // --- Preferencias y Carga Actual ---
    // Guarda los temas que le gustan (ej: "IA", "Redes") para poder recomendarle cursos afines.
    private Set<String> intereses;

    // Mantiene un registro de los códigos de las materias que está cursando actualmente.
    private List<String> cursosInscritos;

    // --- Constructor ---
    // Crea un nuevo estudiante con sus datos básicos e inicializa su lista de cursos vacía,
    // lista para empezar a inscribir materias.
    public Alumno(String idAlumno, String nombre, int semestre, double promedio, Set<String> intereses) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.semestre = semestre;
        this.promedio = promedio;
        this.intereses = intereses;
        this.cursosInscritos = new ArrayList<>(); // Inicializa la lista vacía
    }

    // --- Métodos de Consulta (Getters) ---
    // Permiten a otras partes del sistema "leer" la información del alumno sin modificarla.
    public String getIdAlumno() { return idAlumno; }
    public String getNombre() { return nombre; }
    public int getSemestre() { return semestre; }
    public double getPromedio() { return promedio; }
    public Set<String> getIntereses() { return intereses; }
    public List<String> getCursosInscritos() { return cursosInscritos; }

    // --- Gestión de Materias del Alumno ---

    // Agrega una materia a la lista personal del alumno.
    // Incluye una protección para evitar que se inscriba dos veces al mismo curso.
    public void addCursoInscrito(String idCurso) {
        if (!cursosInscritos.contains(idCurso)) {
            cursosInscritos.add(idCurso);
        }
    }

    // Elimina una materia de su lista (cuando el alumno se da de baja).
    public void removeCursoInscrito(String idCurso) {
        cursosInscritos.remove(idCurso);
    }

    // --- Visualización ---
    // Genera un resumen de texto limpio con los datos clave del alumno.
    // Útil para mostrar la información en la consola de forma rápida.
    @Override
    public String toString() {
        return "ID: " + idAlumno + " | Nombre: " + nombre + " | Semestre: " + semestre + " | Promedio: " + promedio +
                " | Intereses: " + intereses + " | Cursos: " + cursosInscritos.size();
    }
}