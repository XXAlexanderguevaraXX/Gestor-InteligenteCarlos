import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static GestorUniversidad gestor = new GestorUniversidad();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("--- Gestor Universitario: Cursos, Inscripciones y Listas de Espera ---");
        int opcion;

        do {
            mostrarMenuPrincipal();
            opcion = leerOpcion();

            switch (opcion) {
                case 1: menuGestion(); break;
                case 2: menuInscripciones(); break;
                case 3: menuListasEspera(); break;
                case 4: pedirRecomendacion(); break;
                case 5: menuReportes(); break;
                case 6: System.out.println("👋 Saliendo del sistema..."); break;
                case 7:;
                default: System.out.println("Opción no válida. Intente de nuevo.");
            }

        } while (opcion != 6);

        scanner.close();
    }

    private static int leerOpcion() {
        try {
            System.out.print("Seleccione una opción: ");
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Error: Ingrese solo números.");
            scanner.next(); // Limpiar buffer
            return -1;
        } finally {
            scanner.nextLine(); // Consumir el salto de línea
        }
    }

    // --- Menús y Lógica de Interacción ---

    public static void mostrarMenuPrincipal() {
        System.out.println("\n--- MENÚ PRINCIPAL ---");
        System.out.println("1. Gestión de alumnos y cursos");
        System.out.println("2. Inscripciones");
        System.out.println("3. Listas de espera");
        System.out.println("4. Recomendaciones");
        System.out.println("5. Reportes");
        System.out.println("6. Salir");
    }

    public static void menuGestion() {
        System.out.println("\n--- 1. GESTIÓN ---");
        System.out.println("1.1. Consultar curso por ID");
        System.out.println("1.2. Consultar alumno por ID");
        System.out.println("1.3. Listar todos los cursos existentes"); // NUEVA OPCIÓN
        System.out.print("Opción: ");
        String op = scanner.nextLine();

        if (op.equals("1.1")) {
            System.out.print("ID del Curso: ");
            String id = scanner.nextLine();
            Curso c = gestor.consultarCurso(id);
            System.out.println(c != null ? c : "⚠️ Curso no encontrado.");
        } else if (op.equals("1.2")) {
            System.out.print("ID del Alumno: ");
            String id = scanner.nextLine();
            Alumno a = gestor.consultarAlumno(id);
            System.out.println(a != null ? a : "⚠️ Alumno no encontrado.");
        } else if (op.equals("1.3")) { // Lógica del nuevo case
            listarCursos();
        } else {
            System.out.println("⚠️ Opción no válida en el submenú de Gestión.");
        }
    }
    public static void menuInscripciones() {
        System.out.println("\n--- 2. INSCRIPCIONES ---");
        System.out.println("1. Inscribir alumno en curso");
        System.out.println("2. Dar de baja alumno del curso");
        System.out.print("Opción: ");
        int op = leerOpcion();

        System.out.print("ID del Alumno: ");
        String idA = scanner.nextLine();
        System.out.print("ID del Curso: ");
        String idC = scanner.nextLine();

        if (op == 1) {
            gestor.inscribirAlumnoEnCurso(idA, idC);
        } else if (op == 2) {
            gestor.darDeBajaAlumnoDelCurso(idA, idC);
        }
    }

    public static void menuListasEspera() {
        System.out.println("\n--- 3. LISTAS DE ESPERA ---");
        System.out.print("ID del Curso para ver la lista de espera: ");
        String idC = scanner.nextLine();
        gestor.mostrarListaDeEspera(idC);

        // Opcional: Procesar lista de espera manualmente (si se quiere simular un cupo liberado)
        // System.out.print("¿Intentar inscribir al siguiente de la lista? (s/n): ");
        // if (scanner.nextLine().equalsIgnoreCase("s")) {
        //     gestor.procesarListaDeEspera(idC);
        // }
    }

    public static void pedirRecomendacion() {
        System.out.println("\n--- 4. RECOMENDACIONES ---");
        System.out.print("ID del Alumno para recomendaciones: ");
        String idA = scanner.nextLine();
        gestor.recomendarCursos(idA);
    }

    public static void menuReportes() {
        System.out.println("\n--- 5. REPORTES ---");
        System.out.println("1. Carga académica de un alumno");
        System.out.println("2. Lista de alumnos inscritos en un curso");
        System.out.println("3. Cursos con más demanda");
        System.out.print("Opción: ");
        int op = leerOpcion();

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
        }
    }
}