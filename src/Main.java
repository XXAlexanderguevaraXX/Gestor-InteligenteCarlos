import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Collection; // Necesario para Collection<Curso>

public class Main {

    // Instancia del gestor y del scanner (compartidos por los métodos estáticos)
    private static GestorUniversidad gestor = new GestorUniversidad();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("--- Gestor Universitario: Cursos, Inscripciones y Listas de Espera ---");
        int opcion;

        do {
            mostrarMenuPrincipal();
            opcion = leerOpcionPrincipal();

            switch (opcion) {
                case 1: menuGestion(); break;
                case 2: menuInscripciones(); break;
                case 3: menuListasEspera(); break;
                case 4: pedirRecomendacion(); break;
                case 5: menuReportes(); break;
                case 6: System.out.println("👋 Saliendo del sistema..."); break;
                default: System.out.println("Opción no válida. Intente de nuevo.");
            }

        } while (opcion != 6);

        scanner.close();
    }

    // --- Utilidades de Lectura ---

    private static int leerOpcionPrincipal() {
        try {
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea
            return opcion;
        } catch (InputMismatchException e) {
            System.out.println("⚠️ Error: Ingrese solo números para las opciones principales.");
            scanner.nextLine(); // Limpiar buffer
            return -1;
        }
    }

    // --- Menú Principal ---

    public static void mostrarMenuPrincipal() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Gestión de alumnos y cursos");
        System.out.println("2. Inscripciones");
        System.out.println("3. Listas de espera");
        System.out.println("4. Recomendaciones");
        System.out.println("5. Reportes");
        System.out.println("6. Salir");
    }

    // --- 1. Submenú de Gestión ---

    public static void menuGestion() {
        System.out.println("\n--- 1. GESTIÓN DE ALUMNOS Y CURSOS ---");
        System.out.println("1.1. Consultar curso por ID");
        System.out.println("1.2. Consultar alumno por ID");
        System.out.println("1.3. Listar todos los cursos existentes");
        System.out.println("1.4. Registrar nuevo curso");
        System.out.println("1.5. Registrar nuevo alumno");
        System.out.println("0. Volver al Menú Principal");
        System.out.print("Opción: ");
        String op = scanner.nextLine();

        switch (op) {
            case "1.1":
                listarCursos();
                break;

            case "1.2":
                System.out.print("ID del Curso: ");
                String idC = scanner.nextLine();
                Curso c = gestor.consultarCurso(idC);
                System.out.println(c != null ? c : "⚠️ Curso no encontrado.");
                break;

            case "1.3":
                System.out.print("ID del Alumno: ");
                String idA = scanner.nextLine();
                Alumno a = gestor.consultarAlumno(idA);
                System.out.println(a != null ? a : "⚠️ Alumno no encontrado.");
                break;

            case "1.4":
                registrarNuevoCurso();
                break;

            case "1.5":
                registrarNuevoAlumno();
                break;

            case "0":
                System.out.println("Volviendo al menú principal...");
                break;

            default:
                System.out.println("⚠️ Opción no válida en el submenú de Gestión.");
        }
    }

    public static void listarCursos() {
        System.out.println("\n--- LISTA DE CURSOS EXISTENTES ---");
        Collection<Curso> listaCursos = gestor.obtenerTodosLosCursos();

        if (listaCursos.isEmpty()) {
            System.out.println("No hay cursos registrados en el sistema.");
            return;
        }

        for (Curso curso : listaCursos) {
            System.out.println("----------------------------------------");
            System.out.println("ID: " + curso.getIdCurso());
            System.out.println("Nombre: " + curso.getNombre());
            System.out.println("Cupos disponibles: " + curso.getCuposDisponibles() + "/" + curso.getCupoMaximo());
            System.out.println("Áreas: " + curso.getAreas());
        }
        System.out.println("----------------------------------------");
    }

    public static void registrarNuevoCurso() {
        try {
            System.out.println("\n--- REGISTRO DE NUEVO CURSO ---");
            System.out.print("ID del Curso (ej: C201): ");
            String idCurso = scanner.nextLine();
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Docente: ");
            String docente = scanner.nextLine();
            System.out.print("Cupo Máximo: ");
            int cupoMaximo = scanner.nextInt();
            System.out.print("Créditos: ");
            int creditos = scanner.nextInt();
            scanner.nextLine(); // Consumir newline
            System.out.print("Áreas/Temas (separados por coma, ej: IA, redes): ");
            String areasStr = scanner.nextLine();

            Set<String> areas = new HashSet<>(Arrays.asList(areasStr.split("\\s*,\\s*")));

            Curso nuevoCurso = new Curso(idCurso, nombre, docente, cupoMaximo, creditos, areas);
            gestor.registrarCurso(nuevoCurso);

        } catch (InputMismatchException e) {
            System.out.println("⚠️ Error en la entrada de datos. Asegúrese de ingresar números para Cupo y Créditos.");
            scanner.nextLine(); // Limpiar buffer
        }
    }

    public static void registrarNuevoAlumno() {
        try {
            System.out.println("\n--- REGISTRO DE NUEVO ALUMNO ---");
            System.out.print("ID del Alumno (ej: A100): ");
            String idAlumno = scanner.nextLine();
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Semestre: ");
            int semestre = scanner.nextInt();
            System.out.print("Promedio (ej: 9.5): ");
            double promedio = scanner.nextDouble();
            scanner.nextLine(); // Consumir newline
            System.out.print("Intereses (separados por coma, ej: etica, IA): ");
            String interesesStr = scanner.nextLine();

            Set<String> intereses = new HashSet<>(Arrays.asList(interesesStr.split("\\s*,\\s*")));

            Alumno nuevoAlumno = new Alumno(idAlumno, nombre, semestre, promedio, intereses);
            gestor.registrarAlumno(nuevoAlumno);

        } catch (InputMismatchException e) {
            System.out.println("⚠️ Error en la entrada de datos. Asegúrese de ingresar números para Semestre y Promedio.");
            scanner.nextLine(); // Limpiar buffer
        }
    }

    // --- 2. Submenú de Inscripciones ---

    public static void menuInscripciones() {
        System.out.println("\n--- 2. INSCRIPCIONES ---");
        System.out.println("1. Inscribir alumno en curso");
        System.out.println("2. Dar de baja alumno del curso");
        System.out.print("Opción: ");
        int op = leerOpcionPrincipal();

        System.out.print("ID del Alumno: ");
        String idA = scanner.nextLine();
        System.out.print("ID del Curso: ");
        String idC = scanner.nextLine();

        if (op == 1) {
            gestor.inscribirAlumnoEnCurso(idA, idC);
        } else if (op == 2) {
            gestor.darDeBajaAlumnoDelCurso(idA, idC);
        } else {
            System.out.println("⚠️ Opción no válida.");
        }
    }

    // --- 3. Submenú de Listas de Espera ---

    public static void menuListasEspera() {
        System.out.println("\n--- 3. LISTAS DE ESPERA ---");
        System.out.print("ID del Curso para ver la lista de espera: ");
        String idC = scanner.nextLine();

        // Muestra la lista de espera ordenada por prioridad (Heap)
        gestor.mostrarListaDeEspera(idC);

        // Simulación de liberación de cupo (solo si el curso está lleno y hay lista de espera)
        Curso curso = gestor.consultarCurso(idC);
        if (curso != null && curso.getCuposDisponibles() <= 0) {
            System.out.print("\n¿Simular la liberación de un cupo para procesar la lista de espera? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                // Esta llamada ejecuta la lógica de sacar del Heap y inscribir al siguiente
                gestor.procesarListaDeEspera(idC);
            }
        }
    }

    // --- 4. Submenú de Recomendaciones ---

    public static void pedirRecomendacion() {
        System.out.println("\n--- 4. RECOMENDACIONES ---");
        System.out.print("ID del Alumno para recomendaciones: ");
        String idA = scanner.nextLine();
        // Lógica que usa la Intersección de Conjuntos
        gestor.recomendarCursos(idA);
    }

    // --- 5. Submenú de Reportes ---

    public static void menuReportes() {
        System.out.println("\n--- 5. REPORTES ---");
        System.out.println("1. Carga académica de un alumno");
        System.out.println("2. Lista de alumnos inscritos en un curso");
        System.out.println("3. Cursos con más demanda (Inscritos + Lista de Espera)");
        System.out.print("Opción: ");
        int op = leerOpcionPrincipal();

        if (op == 1) {
            System.out.print("ID del Alumno: ");
            String idA = scanner.nextLine();
            gestor.cargaAcademica(idA);
        } else if (op == 2) {
            System.out.print("ID del Curso: ");
            String idC = scanner.nextLine();
            gestor.listarAlumnosInscritosEnCurso(idC);
        } else if (op == 3) {
            gestor.cursosConMasDemanda();
        } else {
            System.out.println("⚠️ Opción no válida.");
        }
    }
}