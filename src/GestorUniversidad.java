import java.util.*;

public class GestorUniversidad {
    // 1. Tabla Hash (HashMap): Almacena Alumnos y Cursos por ID
    private Map<String, Curso> cursos;
    private Map<String, Alumno> alumnos;

    // 2. Montículo (ListaEsperaHeap): Lista de espera por curso (¡Implementación Manual!)
    private Map<String, ListaEsperaHeap> listasEspera;

    // 3. Diccionario (HashMap): idCurso -> lista de idAlumnos inscritos
    private Map<String, List<String>> inscripcionesCurso;

    public GestorUniversidad() {
        this.cursos = new HashMap<>();
        this.alumnos = new HashMap<>();
        this.listasEspera = new HashMap<>();
        this.inscripcionesCurso = new HashMap<>();
        cargarDatosIniciales();
    }

    // --- Utilidades del Heap (Compartido con Main para ordenamiento) ---

    // Define la prioridad para el Heap (Lista de Espera)
    // Criterios: 1. Mayor Promedio, 2. Mayor Semestre.
    private Comparator<Alumno> obtenerComparadorListaEspera() {
        return (a1, a2) -> {
            // 1. Promedio (Descendente: a2.promedio - a1.promedio)
            if (a1.getPromedio() != a2.getPromedio()) {
                return Double.compare(a2.getPromedio(), a1.getPromedio());
            }
            // 2. Semestre (Descendente: si los promedios son iguales, el de mayor semestre va primero)
            return Integer.compare(a2.getSemestre(), a1.getSemestre());
        };
    }

    private void inicializarListaEspera(String idCurso) {
        if (!listasEspera.containsKey(idCurso)) {
            // Usamos nuestra clase implementada ListaEsperaHeap
            listasEspera.put(idCurso, new ListaEsperaHeap(obtenerComparadorListaEspera()));
            inscripcionesCurso.put(idCurso, new ArrayList<>());
        }
    }

    // ... (Método obtenerTodosLosCursos() y métodos de gestión (registrar, consultar) sin cambios)
    public Collection<Curso> obtenerTodosLosCursos() {
        return cursos.values();
    }

    public void registrarAlumno(Alumno alumno) {
        if (alumnos.containsKey(alumno.getIdAlumno())) {
            System.out.println("⚠️ Error: Alumno con ID " + alumno.getIdAlumno() + " ya existe.");
            return;
        }
        alumnos.put(alumno.getIdAlumno(), alumno);
        System.out.println("✅ Alumno " + alumno.getNombre() + " registrado correctamente.");
    }

    public void registrarCurso(Curso curso) {
        if (cursos.containsKey(curso.getIdCurso())) {
            System.out.println("⚠️ Error: Curso con ID " + curso.getIdCurso() + " ya existe.");
            return;
        }
        cursos.put(curso.getIdCurso(), curso);
        inicializarListaEspera(curso.getIdCurso());
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
            System.out.println("⚠️ Error: Alumno o curso no encontrado.");
            return;
        }

        if (alumno.getCursosInscritos().contains(idCurso)) {
            System.out.println("⚠️ Alumno " + alumno.getNombre() + " ya está inscrito en " + curso.getNombre());
            return;
        }

        if (curso.getCuposDisponibles() > 0) {
            curso.disminuirCupo();
            alumno.addCursoInscrito(idCurso);
            inscripcionesCurso.get(idCurso).add(idAlumno);
            System.out.println("✅ Inscripción exitosa: " + alumno.getNombre() + " en " + curso.getNombre());
        } else {
            // Usamos nuestra ListaEsperaHeap
            ListaEsperaHeap espera = listasEspera.get(idCurso);
            if (espera != null) {
                if (!espera.contains(alumno)) {
                    espera.offer(alumno); // Agregar al Heap (offer/insertar)
                    System.out.println("➡️ Cupo lleno. " + alumno.getNombre() + " enviado a Lista de Espera de " + curso.getNombre() + ".");
                } else {
                    System.out.println("⚠️ Alumno ya se encuentra en la lista de espera.");
                }
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

            // Al liberarse un cupo, procesar lista de espera
            procesarListaDeEspera(idCurso);
        } else {
            // Si no estaba inscrito, verificar si estaba en la lista de espera (Heap manual)
            ListaEsperaHeap espera = listasEspera.get(idCurso);
            if (espera != null && espera.toList().remove(alumno)) { // Remove de la lista subyacente
                System.out.println("✅ Baja exitosa de Lista de Espera: " + alumno.getNombre() + " removido de la espera de " + curso.getNombre() + ".");
            } else {
                System.out.println("⚠️ Error: El alumno no está inscrito ni en lista de espera en este curso.");
            }
        }
    }

    // --- 3. Listas de espera (Heap manual) ---

    public void mostrarListaDeEspera(String idCurso) {
        Curso curso = consultarCurso(idCurso);
        if (curso == null) {
            System.out.println("⚠️ Error: Curso no encontrado.");
            return;
        }
        ListaEsperaHeap espera = listasEspera.get(idCurso);
        System.out.println("\n--- Lista de Espera para " + curso.getNombre() + " (Cupos disponibles: " + curso.getCuposDisponibles() + ") ---");
        if (espera.isEmpty()) {
            System.out.println("La lista de espera está vacía.");
            return;
        }

        // Obtener la lista subyacente y ordenarla para mostrar el orden de prioridad
        List<Alumno> listaOrdenada = espera.toList();
        listaOrdenada.sort(obtenerComparadorListaEspera());

        int i = 1;
        for (Alumno a : listaOrdenada) {
            System.out.println(i++ + ". " + a.toString());
        }
    }

    public void procesarListaDeEspera(String idCurso) {
        Curso curso = consultarCurso(idCurso);
        ListaEsperaHeap espera = listasEspera.get(idCurso); // Usando Heap manual

        if (curso == null || espera == null || espera.isEmpty() || curso.getCuposDisponibles() <= 0) {
            if (curso != null && curso.getCuposDisponibles() <= 0 && !espera.isEmpty()) {
                System.out.println("⚠️ No se puede procesar la lista: No hay cupos disponibles en el curso.");
            }
            return;
        }

        // Sacar del heap (poll) al siguiente alumno de mayor prioridad
        Alumno siguiente = espera.poll();

        // Intentar inscribirlo.
        if (curso.disminuirCupo()) {
            siguiente.addCursoInscrito(idCurso);
            inscripcionesCurso.get(idCurso).add(siguiente.getIdAlumno());
            System.out.println("\n📣 ¡Cupo liberado! " + siguiente.getNombre() + " ha sido inscrito en " + curso.getNombre() + " desde la Lista de Espera.");
        } else {
            // Devolverlo si falla la inscripción
            espera.offer(siguiente);
        }
    }

    // --- 4. Recomendaciones (Conjuntos) ---
    // (Lógica sin cambios, ya que usa HashSet)
    public void recomendarCursos(String idAlumno) {
        Alumno alumno = consultarAlumno(idAlumno);
        if (alumno == null) {
            System.out.println("⚠️ Error: Alumno no encontrado.");
            return;
        }

        Map<String, Integer> afinidadCursos = new HashMap<>();

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

        List<Map.Entry<String, Integer>> listaAfinidad = new ArrayList<>(afinidadCursos.entrySet());
        listaAfinidad.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        System.out.println("\n--- Recomendaciones para " + alumno.getNombre() + " (Intereses: " + alumno.getIntereses() + ") ---");
        if (listaAfinidad.isEmpty()) {
            System.out.println("No se encontraron cursos con afinidad a sus intereses.");
            return;
        }

        for (Map.Entry<String, Integer> entry : listaAfinidad) {
            Curso curso = consultarCurso(entry.getKey());
            System.out.println("⭐ " + curso.getNombre() + " (Afinidad: " + entry.getValue() + " coincidencias) | Áreas: " + curso.getAreas());
        }
    }

    // --- 5. Reportes ---
    // (Lógica sin cambios, ya que usa HashMap y List)

    public void cargaAcademica(String idAlumno) {
        Alumno alumno = consultarAlumno(idAlumno);
        if (alumno == null) {
            System.out.println("⚠️ Error: Alumno no encontrado.");
            return;
        }

        System.out.println("\n--- Carga Académica de " + alumno.getNombre() + " ---");
        if (alumno.getCursosInscritos().isEmpty()) {
            System.out.println("El alumno no está inscrito en ningún curso.");
            return;
        }

        int totalCreditos = 0;
        for (String idCurso : alumno.getCursosInscritos()) {
            Curso curso = consultarCurso(idCurso);
            if (curso != null) {
                System.out.println(" - " + curso.getNombre() + " (" + curso.getIdCurso() + ") - " + curso.getCreditos() + " créditos.");
                totalCreditos += curso.getCreditos();
            }
        }
        System.out.println("Total de créditos inscritos: " + totalCreditos);
    }

    public void listarAlumnosInscritosEnCurso(String idCurso) {
        Curso curso = consultarCurso(idCurso);
        if (curso == null) {
            System.out.println("⚠️ Error: Curso no encontrado.");
            return;
        }

        List<String> alumnosInscritos = inscripcionesCurso.get(idCurso);
        System.out.println("\n--- Alumnos Inscritos en " + curso.getNombre() + " (" + alumnosInscritos.size() + " inscritos) ---");
        if (alumnosInscritos.isEmpty()) {
            System.out.println("No hay alumnos inscritos en este curso.");
            return;
        }

        for (String idAlumno : alumnosInscritos) {
            Alumno alumno = consultarAlumno(idAlumno);
            if (alumno != null) {
                System.out.println(" - " + alumno.getNombre() + " (ID: " + alumno.getIdAlumno() + ", Promedio: " + alumno.getPromedio() + ")");
            }
        }
    }

    public void cursosConMasDemanda() {
        Map<String, Integer> demandaCursos = new HashMap<>();

        for (Curso curso : cursos.values()) {
            int inscritos = inscripcionesCurso.getOrDefault(curso.getIdCurso(), new ArrayList<>()).size();
            // Tamaño de nuestra lista de espera manual
            int espera = listasEspera.getOrDefault(curso.getIdCurso(), new ListaEsperaHeap(obtenerComparadorListaEspera())).size();
            int demandaTotal = inscritos + espera;
            demandaCursos.put(curso.getIdCurso(), demandaTotal);
        }

        List<Map.Entry<String, Integer>> listaDemanda = new ArrayList<>(demandaCursos.entrySet());
        listaDemanda.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        System.out.println("\n--- Top Cursos con Más Demanda (Inscritos + Espera) ---");
        if (listaDemanda.isEmpty() || listaDemanda.get(0).getValue() == 0) {
            System.out.println("No hay demanda registrada.");
            return;
        }

        for (int i = 0; i < Math.min(5, listaDemanda.size()); i++) {
            Map.Entry<String, Integer> entry = listaDemanda.get(i);
            Curso curso = consultarCurso(entry.getKey());
            int inscritos = inscripcionesCurso.get(curso.getIdCurso()).size();
            int espera = listasEspera.get(curso.getIdCurso()).size();
            System.out.println((i + 1) + ". " + curso.getNombre() +
                    " | Demanda Total: " + entry.getValue() +
                    " (Inscritos: " + inscritos + ", Espera: " + espera + ") | Cupo Máximo: " + curso.getCupoMaximo());
        }
    }

    // --- Carga de Datos Iniciales (Sin cambios) ---
    private void cargarDatosIniciales() {
        // Cursos
        registrarCurso(new Curso("C101", "Introduccion a IA", "Dr. Lopez", 1, 5, new HashSet<>(Arrays.asList("IA", "algoritmos"))));
        registrarCurso(new Curso("C102", "Redes Avanzadas", "Ing. Perez", 5, 4, new HashSet<>(Arrays.asList("redes", "seguridad"))));
        registrarCurso(new Curso("C103", "Etica y Sociedad", "Dra. Mora", 10, 3, new HashSet<>(Arrays.asList("etica", "filosofia"))));
        registrarCurso(new Curso("C104", "Teoria de Juegos", "Mtro. Sanchez", 3, 4, new HashSet<>(Arrays.asList("matematicas", "algoritmos"))));

        // Alumnos
        registrarAlumno(new Alumno("A001", "Ana Gomez", 8, 9.5, new HashSet<>(Arrays.asList("IA", "redes"))));
        registrarAlumno(new Alumno("A002", "Juan Perez", 4, 8.2, new HashSet<>(Arrays.asList("matematicas", "etica"))));
        registrarAlumno(new Alumno("A003", "Carlos Lopez", 8, 9.0, new HashSet<>(Arrays.asList("IA", "algoritmos"))));
        registrarAlumno(new Alumno("A004", "Maria Díaz", 6, 9.8, new HashSet<>(Arrays.asList("seguridad", "redes"))));

        // Inscripciones Iniciales
        inscribirAlumnoEnCurso("A001", "C101");
        inscribirAlumnoEnCurso("A003", "C101"); // Lista de Espera (9.0, 8)
        inscribirAlumnoEnCurso("A004", "C101"); // Lista de Espera (9.8, 6) -> Mayor prioridad
        inscribirAlumnoEnCurso("A002", "C103");
        inscribirAlumnoEnCurso("A001", "C102");
    }
}