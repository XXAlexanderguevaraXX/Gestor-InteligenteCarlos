import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Alumno {
    private String idAlumno;
    private String nombre;
    private int semestre;
    private double promedio;
    private Set<String> intereses; // Conjuntos: Almacena las áreas de interés.
    private List<String> cursosInscritos; // Lista: Registra los cursos actuales.

    public Alumno(String idAlumno, String nombre, int semestre, double promedio, Set<String> intereses) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.semestre = semestre;
        this.promedio = promedio;
        this.intereses = intereses;
        this.cursosInscritos = new ArrayList<>();
    }

    // Getters
    public String getIdAlumno() { return idAlumno; }
    public String getNombre() { return nombre; }
    public int getSemestre() { return semestre; }
    public double getPromedio() { return promedio; }
    public Set<String> getIntereses() { return intereses; }
    public List<String> getCursosInscritos() { return cursosInscritos; }

    // Métodos para manejo de inscripciones
    public void addCursoInscrito(String idCurso) {
        if (!cursosInscritos.contains(idCurso)) {
            cursosInscritos.add(idCurso);
        }
    }

    public void removeCursoInscrito(String idCurso) {
        cursosInscritos.remove(idCurso);
    }

    @Override
    public String toString() {
        return "ID: " + idAlumno + " | Nombre: " + nombre + " | Semestre: " + semestre + " | Promedio: " + promedio +
                " | Intereses: " + intereses + " | Cursos: " + cursosInscritos.size();
    }
}