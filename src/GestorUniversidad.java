import java.util.*;

public class GestorUniversidad {
    // 1. Tabla Hash: Almacena Alumnos y Cursos por ID
    private Map<String, Curso> cursos;
    private Map<String, Alumno> alumnos;

    // 2. Montículo (Heap): Lista de espera por curso. PriorityQueue usa el comparador
    private Map<String, PriorityQueue<Alumno>> listasEspera;

    // 3. Diccionario: idCurso -> lista de idAlumnos inscritos (para reportes y consultas)
    private Map<String, List<String>> inscripcionesCurso;

    public GestorUniversidad() {
        this.cursos = new HashMap<>();
        this.alumnos = new HashMap<>();
        this.listasEspera = new HashMap<>();
        this.inscripcionesCurso = new HashMap<>();
        cargarDatosIniciales();
    }

    // --- Utilidades ---

    // Define la prioridad para el Heap (Lista de Espera)
    // Criterios: 1. Mayor Promedio, 2. Mayor Semestre.
    private Comparator<Alumno> obtenerComparadorListaEspera() {
        return (a1, a2) -> {
            // 1. Promedio (Descendente: a2.promedio - a1.promedio)
            if (a1.getPromedio() != a2.getPromedio()) {
                return Double.compare(a2.getPromedio(), a1.getPromedio());
            }
            // 2. Semestre (Descendente: a2.semestre - a1.semestre)
            return Integer.compare(a2.getSemestre(), a1.getSemestre());
        };
    }

    private void inicializarListaEspera(String idCurso) {
        if (!listasEspera.containsKey(idCurso)) {
            listasEspera.put(idCurso, new PriorityQueue<>(obtenerComparadorListaEspera()));
            inscripcionesCurso.put(idCurso, new ArrayList<>());
        }
    }

    // --- 1. Gestión de alumnos y cursos ---

    public void registrarAlumno(Alumno alumno) {
        if (alumnos.containsKey(alumno.getIdAlumno())) {
            System.out.println("Error: Alumno con ID " + alumno.getIdAlumno() + " ya existe.");
            return;
        }
        alumnos.put(alumno.getIdAlumno(), alumno);
        System.out.println("✅ Alumno " + alumno.getNombre() + " registrado correctamente.");
    }

    public void registrarCurso(Curso curso) {
        if (cursos.containsKey(curso.getIdCurso())) {
            System.out.println("Error: Curso con ID " + curso.getIdCurso() + " ya existe.");
            return;
        }
        cursos.put(curso.getIdCurso(), curso);
        inicializarListaEspera(curso.getIdCurso()); // Inicializa las estructuras relacionadas
        System.out.println("✅ Curso " + curso.getNombre() + " registrado correctamente.");
    }

    public Curso consultarCurso(String idCurso) {
        return cursos.get(idCurso);
    }

    public Alumno consultarAlumno(String idAlumno) {
        return alumnos.get(idAlumno);
    }

    // --- 2. Inscripciones ---

    public void inscribirAlumnoEnCurso(String idAlumno, String idCurso) {
        Alumno alumno = consultarAlumno(idAlumno);
        Curso curso = consultarCurso(idCurso);

        if (alumno == null || curso == null) {
            System.out.println("Error: Alumno o curso no encontrado.");
            return;
        }

        // Verificar si ya está inscrito
        if (alumno.getCursosInscritos().contains(idCurso)) {
            System.out.println("⚠️ Alumno " + alumno.getNombre() + " ya está inscrito en " + curso.getNombre());
            return;
        }

        if (curso.getCuposDisponibles() > 0) {
            // Si hay cupo: Inscribir
            curso.disminuirCupo();
            alumno.addCursoInscrito(idCurso);
            inscripcionesCurso.get(idCurso).add(idAlumno);
            System.out.println("✅ Inscripción exitosa: " + alumno.getNombre() + " en " + curso.getNombre());
        } else {
            // Si no hay cupo: Enviar a la lista de espera (Heap)
            PriorityQueue<Alumno> espera = listasEspera.get(idCurso);
            if (espera != null) {
                if (!espera.contains(alumno)) {
                    espera.offer(alumno); // Agregar al Heap
                    System.out.println("➡️ Cupo lleno. " + alumno.getNombre() + " enviado a Lista de Espera de " + curso.getNombre() + ".");
                } else {
                    System.out.println("⚠️ Alumno ya se encuentra en la lista de espera.");
                }
            } else {
                System.out.println("Error interno: Lista de espera no inicializada.");
            }
        }
    }

    public void darDeBajaAlumnoDelCurso(String idAlumno, String idCurso) {
        Alumno alumno = consultarAlumno(idAlumno);
        Curso curso = consultarCurso(idCurso);

        if (alumno == null || curso == null) return;

        if (alumno.getCursosInscritos().remove(idCurso)) {
            curso.aumentarCupo();
            inscripcionesCurso.get(idCurso).remove(idAlumno);
            System.out.println("✅ Baja exitosa: " + alumno.getNombre() + " dado de baja de " + curso.getNombre() + ".");

            // Al liberarse un cupo, intentar inscribir al siguiente de la lista de espera
            procesarListaDeEspera(idCurso);
        } else {
            // Si no está inscrito, verificar si está en la lista de espera
            PriorityQueue<Alumno> espera = listasEspera.get(idCurso);
            if (espera != null && espera.remove(alumno)) {
                System.out.println("✅ Baja exitosa de Lista de Espera: " + alumno.getNombre() + " removido de la espera de " + curso.getNombre() + ".");
            } else {
                System.out.println("⚠️ Error: El alumno no está inscrito ni en lista de espera en este curso.");
            }
        }
    }

    // --- 3. Listas de espera ---

    public void mostrarListaDeEspera(String idCurso) {
        Curso curso = consultarCurso(idCurso);
        if (curso == null) {
            System.out.println("Error: Curso no encontrado.");
            return;
        }
        PriorityQueue<Alumno> espera = listasEspera.get(idCurso);
        System.out.println("\n--- Lista de Espera para " + curso.getNombre() + " (Cupos disponibles: " + curso.getCuposDisponibles() + ") ---");
        if (espera.isEmpty()) {
            System.out.println("La lista de espera está vacía.");
            return;
        }

        // Copiamos la cola para poder iterar en orden de prioridad sin modificar la original
        List<Alumno> listaOrdenada = new ArrayList<>(espera);
        listaOrdenada.sort(obtenerComparadorListaEspera());

        int i = 1;
        for (Alumno a : listaOrdenada) {
            System.out.println(i++ + ". " + a.toString());
        }
    }

    public void procesarListaDeEspera(String idCurso) {
        Curso curso = consultarCurso(idCurso);
        PriorityQueue<Alumno> espera = listasEspera.get(idCurso);

        if (curso == null || espera == null || curso.getCuposDisponibles() <= 0 || espera.isEmpty()) {
            return;
        }

        // Sacar del heap (poll) al siguiente alumno de mayor prioridad
        Alumno siguiente = espera.poll();

        // Intentar inscribirlo. Ya sabemos que hay cupo (curso.getCuposDisponibles() > 0)
        // Usamos disminuirCupo() para manejar la transacción final.
        if (curso.disminuirCupo()) {
            siguiente.addCursoInscrito(idCurso);
            inscripcionesCurso.get(idCurso).add(siguiente.getIdAlumno());
            System.out.println("\n📣 ¡Cupo liberado! " + siguiente.getNombre() + " ha sido inscrito en " + curso.getNombre() + " desde la Lista de Espera.");
        } else {
            // Si falla la inscripción por alguna razón (debería ser imposible aquí)
            espera.offer(siguiente); // Devolver al heap
        }
    }

    // --- 4. Recomendaciones (Conjuntos) ---

    public void recomendarCursos(String idAlumno) {
        Alumno alumno = consultarAlumno(idAlumno);
        if (alumno == null) {
            System.out.println("Error: Alumno no encontrado.");
            return;
        }

        Map<String, Integer> afinidadCursos = new HashMap<>(); // idCurso -> Nro de coincidencias

        for (Curso curso : cursos.values()) {
            Set<String> interesesAlumno = new HashSet<>(alumno.getIntereses());
            Set<String> areasCurso = curso.getAreas();

            // Intersección de conjuntos (afinidades)
            interesesAlumno.retainAll(areasCurso);

            int coincidencias = interesesAlumno.size();
            if (coincidencias > 0) {
                afinidadCursos.put(curso.getIdCurso(), coincidencias);
            }
        }

        // Ordenar los cursos por afinidad (descendente)
        List<Map.Entry<String, Integer>> listaAfinidad = new ArrayList<>(afinidadCursos.entrySet());
        listaAfinidad.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        System.out.println("\n--- Recomendaciones para " + alumno.getNombre() + " ---");
        if (listaAfinidad.isEmpty()) {
            System.out.println("No se encontraron cursos con afinidad a sus intereses.");
            return;
        }

        for (Map.Entry<String, Integer> entry : listaAfinidad) {
            Curso curso = consultarCurso(entry.getKey());
            System.out.println("⭐ " + curso.getNombre() + " (Afinidad: " + entry.getValue() + " coincidencias)");
        }
    }

    // --- 5. Reportes ---

    public void cargaAcademica(String idAlumno) {
        Alumno alumno = consultarAlumno(idAlumno);
        if (alumno == null) return;

        System.out.println("\n--- Carga Académica de " + alumno.getNombre() + " ---");
        if (alumno.getCursosInscritos().isEmpty()) {
            System.out.println("El alumno no está inscrito en ningún curso.");
            return;
        }

        int totalCreditos = 0;
        for (String idCurso : alumno.getCursosInscritos()) {
            Curso curso = consultarCurso(idCurso);
            if (curso != null) {
                System.out.println(" - " + curso.getNombre() + " (" + curso.getIdCurso() + ") - " + curso.creditos + " créditos.");
                totalCreditos += curso.creditos;
            }
        }
        System.out.println("Total de créditos inscritos: " + totalCreditos);
    }

    public void listarAlumnosInscritosEnCurso(String idCurso) {
        Curso curso = consultarCurso(idCurso);
        if (curso == null) {
            System.out.println("Error: Curso no encontrado.");
            return;
        }

        List<String> alumnosInscritos = inscripcionesCurso.get(idCurso);
        System.out.println("\n--- Alumnos Inscritos en " + curso.getNombre() + " (" + alumnosInscritos.size() + " inscritos) ---");
        for (String idAlumno : alumnosInscritos) {
            Alumno alumno = consultarAlumno(idAlumno);
            if (alumno != null) {
                System.out.println(" - " + alumno.getNombre() + " (ID: " + alumno.getIdAlumno() + ")");
            }
        }
    }

    public void cursosConMasDemanda() {
        // Demanda = Inscritos + Lista de espera
        Map<String, Integer> demandaCursos = new HashMap<>();

        for (Curso curso : cursos.values()) {
            int inscritos = inscripcionesCurso.getOrDefault(curso.getIdCurso(), new ArrayList<>()).size();
            int espera = listasEspera.getOrDefault(curso.getIdCurso(), new PriorityQueue<>()).size();
            int demandaTotal = inscritos + espera;
            demandaCursos.put(curso.getIdCurso(), demandaTotal);
        }

        // Ordenar por demanda total (descendente)
        List<Map.Entry<String, Integer>> listaDemanda = new ArrayList<>(demandaCursos.entrySet());
        listaDemanda.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        System.out.println("\n--- Cursos con Más Demanda (Inscritos + Espera) ---");
        for (int i = 0; i < Math.min(5, listaDemanda.size()); i++) {
            Map.Entry<String, Integer> entry = listaDemanda.get(i);
            Curso curso = consultarCurso(entry.getKey());
            int inscritos = inscripcionesCurso.get(curso.getIdCurso()).size();
            int espera = listasEspera.get(curso.getIdCurso()).size();
            System.out.println((i + 1) + ". " + curso.getNombre() +
                    " | Demanda Total: " + entry.getValue() +
                    " (Inscritos: " + inscritos + ", Espera: " + espera + ")");
        }
    }

    // --- Carga de Datos Iniciales (Para pruebas) ---
    private void cargarDatosIniciales() {
        // Cursos
        registrarCurso(new Curso("C101", "Introduccion a IA", "Dr. Lopez", 1, 5, new HashSet<>(Arrays.asList("IA", "algoritmos"))));
        registrarCurso(new Curso("C102", "Redes Avanzadas", "Ing. Perez", 5, 4, new HashSet<>(Arrays.asList("redes", "seguridad"))));
        registrarCurso(new Curso("C103", "Etica y Sociedad", "Dra. Mora", 10, 3, new HashSet<>(Arrays.asList("etica", "filosofia"))));

        // Alumnos
        registrarAlumno(new Alumno("A001", "Ana Gomez", 8, 9.5, new HashSet<>(Arrays.asList("IA", "redes"))));
        registrarAlumno(new Alumno("A002", "Juan Perez", 4, 8.2, new HashSet<>(Arrays.asList("matematicas", "etica"))));
        registrarAlumno(new Alumno("A003", "Carlos Lopez", 8, 9.0, new HashSet<>(Arrays.asList("IA", "algoritmos"))));
        registrarAlumno(new Alumno("A004", "Maria Díaz", 6, 9.8, new HashSet<>(Arrays.asList("seguridad", "redes"))));

        // Inscripciones Iniciales
        inscribirAlumnoEnCurso("A001", "C101"); // Cupo lleno (1/1)
        inscribirAlumnoEnCurso("A003", "C101"); // Va a Lista de Espera (Promedio 9.0, Semestre 8)
        inscribirAlumnoEnCurso("A004", "C101"); // Va a Lista de Espera (Promedio 9.8, Semestre 6 -> Debería ser el primero en la lista de espera)
        inscribirAlumnoEnCurso("A002", "C103");
    }
}