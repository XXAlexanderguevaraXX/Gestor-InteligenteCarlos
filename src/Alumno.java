import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Alumno {
    private String idAlumno;
    private String nombre;
    private int semestre;
    private double promedio;
    private Set<String> intereses; // Conjuntos
    private List<String> cursosInscritos; // Parte del Diccionario (idAlumno -> lista de cursos)

    public Alumno(String idAlumno, String nombre, int semestre, double promedio, Set<String> intereses) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.semestre = semestre;
        this.promedio = promedio;
        this.intereses = intereses;
        this.cursosInscritos = new ArrayList<>();
    }

    // Getters y Setters
    public String getIdAlumno() { return idAlumno; }
    public String getNombre() { return nombre; }
    public int getSemestre() { return semestre; }
    public double getPromedio() { return promedio; }
    public Set<String> getIntereses() { return intereses; }
    public List<String> getCursosInscritos() { return cursosInscritos; }

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
                " | Intereses: " + intereses + " | Inscritos: " + cursosInscritos.size();
    }
}