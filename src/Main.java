import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Collection;

public class Main {

    // --- Configuración Inicial ---
    // Preparamos las herramientas principales:
    // 1. El 'gestor' que contiene toda la lógica de la universidad.
    // 2. El 'scanner' para poder leer lo que escribe el usuario.
    private static GestorUniversidad gestor = new GestorUniversidad();
    private static Scanner scanner = new Scanner(System.in);

    // --- Punto de Inicio del Programa ---
    public static void main(String[] args) {
        System.out.println("--- Gestor Universitario: Cursos, Inscripciones y Listas de Espera ---");
        int opcion;

        // Este ciclo mantiene el programa encendido mostrando el menú
        // hasta que el usuario decida elegir la opción de salir (6).
        do {
            mostrarMenuPrincipal();
            opcion = leerOpcionPrincipal();

            // Aquí dirigimos al usuario al submenú correspondiente según su elección
            switch (opcion) {
                case 1: menuGestion(); break;       // Administrar datos (Altas y consultas)
                case 2: menuInscripciones(); break; // Matrículas y bajas
                case 3: menuListasEspera(); break;  // Ver y gestionar colas
                case 4: pedirRecomendacion(); break;// Sugerencias automáticas
                case 5: menuReportes(); break;      // Estadísticas
                case 6: System.out.println("👋 Saliendo del sistema..."); break;
                default: System.out.println("Opción no válida. Intente de nuevo.");
            }

        } while (opcion != 6);

        // Cerramos la lectura de datos antes de terminar.
        scanner.close();
    }

    // --- Herramientas de Ayuda ---

    // Método seguro para leer números del menú.
    // Evita que el programa se cierre de golpe si el usuario escribe letras por error.
    private static int leerOpcionPrincipal() {
        try {
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpieza necesaria para la siguiente lectura
            return opcion;
        } catch (InputMismatchException e) {
            System.out.println("⚠️ Error: Ingrese solo números para las opciones principales.");
            scanner.nextLine(); // Limpiamos el error para intentar de nuevo
            return -1;
        }
    }

    // --- Menú Principal (Visual) ---
    // Simplemente muestra las opciones disponibles en pantalla.
    public static void mostrarMenuPrincipal() {
        System.out.println("\n=============================================");
        System.out.println("✨ UNIVERSIDAD GESTIÓN 💻 | MENÚ PRINCIPAL");
        System.out.println("=============================================");
        System.out.println("1️⃣. 📚 Gestión de Alumnos y Cursos");
        System.out.println("2️⃣. ✍️ Inscripciones (Matrícula y Bajas)");
        System.out.println("3️⃣. ⏳ Listas de Espera (Prioridad: Heap Manual)");
        System.out.println("4️⃣. ⭐ Recomendaciones (Afinidad de Intereses)");
        System.out.println("5️⃣. 📊 Reportes y Estadísticas");
        System.out.println("6️⃣. 🚪 Salir del Sistema");
        System.out.println("=============================================");
    }

    // --- SECCIÓN 1: Administración de Datos (Gestión) ---

    // Submenú encargado de crear nuevos registros o consultar información existente.
    public static void menuGestion() {
        String op = "";
        do {
            System.out.println("\n--- 📚 GESTIÓN DE ALUMNOS Y CURSOS ---");
            System.out.println("------------------------------------");
            System.out.println("1.1. 📜 Listar todos los cursos existentes");
            System.out.println("1.2. 🔍 Consultar curso por ID");
            System.out.println("1.3. 🧑‍🎓 Consultar alumno por ID");
            System.out.println("1.4. ➕ Registrar nuevo curso");
            System.out.println("1.5. 👤 Registrar nuevo alumno");
            System.out.println("0. 🔙 Volver al Menú Principal");
            System.out.println("------------------------------------");
            System.out.print("Elige una opción: ");
            op = scanner.nextLine();

            switch (op) {
                case "1.1":
                    listarCursos(); // Muestra todo el catálogo
                    break;
                case "1.2":
                    // Busca un curso específico por su código
                    System.out.print("ID del Curso: ");
                    String idC = scanner.nextLine();
                    Curso c = gestor.consultarCurso(idC);
                    System.out.println(c != null ? "✅ " + c : "❌ Curso no encontrado.");
                    break;
                case "1.3":
                    // Busca un alumno específico por su matrícula/ID
                    System.out.print("ID del Alumno: ");
                    String idA = scanner.nextLine();
                    Alumno a = gestor.consultarAlumno(idA);
                    System.out.println(a != null ? "✅ " + a : "❌ Alumno no encontrado.");
                    break;
                case "1.4":
                    registrarNuevoCurso(); // Formulario de alta de curso
                    break;
                case "1.5":
                    registrarNuevoAlumno(); // Formulario de alta de alumno
                    break;
                case "0":
                    System.out.println("🔙 Saliendo de Gestión...");
                    break;
                default:
                    System.out.println("⚠️ Opción no válida. Intenta de nuevo.");
            }
        } while (!op.equals("0"));
    }

    // Recorre la base de datos de cursos y los muestra en un formato legible.
    public static void listarCursos() {
        System.out.println("\n==========================================");
        System.out.println("✅ LISTA DE CURSOS EXISTENTES EN EL SISTEMA");
        System.out.println("==========================================");
        Collection<Curso> listaCursos = gestor.obtenerTodosLosCursos();

        if (listaCursos.isEmpty()) {
            System.out.println("No hay cursos registrados.");
            return;
        }

        // Bucle para imprimir detalle por detalle de cada curso
        for (Curso curso : listaCursos) {
            System.out.println("------------------------------------------");
            System.out.println("🆔 ID: " + curso.getIdCurso());
            System.out.println("📚 Nombre: " + curso.getNombre());
            System.out.println("👨‍🏫 Docente: " + curso.getDocente());
            // Muestra cuántos espacios quedan y avisa si está lleno
            System.out.println("➡️ Cupos: " + curso.getCuposDisponibles() + "/" + curso.getCupoMaximo() + (curso.getCuposDisponibles() == 0 ? " (LLENO!)" : ""));
            System.out.println("🏷️  Áreas: " + curso.getAreas());
        }
        System.out.println("------------------------------------------");
    }

    // Pide paso a paso los datos para crear una nueva materia.
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
            scanner.nextLine(); // Limpieza tras leer números

            // Las áreas se ingresan como texto y se separan automáticamente por comas
            System.out.print("Áreas/Temas (separados por coma, ej: IA, redes): ");
            String areasStr = scanner.nextLine();

            Set<String> areas = new HashSet<>(Arrays.asList(areasStr.split("\\s*,\\s*")));

            // Envía la información al gestor para guardarla
            Curso nuevoCurso = new Curso(idCurso, nombre, docente, cupoMaximo, creditos, areas);
            gestor.registrarCurso(nuevoCurso);

        } catch (InputMismatchException e) {
            System.out.println("⚠️ Error. Asegúrese de ingresar números para Cupo O Créditos.");
            scanner.nextLine();
        }
    }

    // Pide paso a paso los datos para registrar un estudiante.
    // Incluye manejo especial para convertir texto a números de forma segura.
    public static void registrarNuevoAlumno() {
        System.out.println("\n--- REGISTRO DE NUEVO ALUMNO ---");

        try {
            System.out.print("ID del Alumno (ej: A100): ");
            String idAlumno = scanner.nextLine();

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();

            // Leemos todo como texto y luego intentamos convertirlo a número.
            // Esto es más seguro para evitar errores de lectura.
            System.out.print("Semestre: ");
            String semestreStr = scanner.nextLine();
            int semestre = Integer.parseInt(semestreStr);

            System.out.print("Promedio (ej: 9.5): ");
            String promedioStr = scanner.nextLine();
            double promedio = Double.parseDouble(promedioStr);

            // Captura los gustos académicos del alumno para futuras recomendaciones
            System.out.print("Intereses (separados por coma, ej: etica, IA): ");
            String interesesStr = scanner.nextLine();

            Set<String> intereses = new HashSet<>(Arrays.asList(interesesStr.split("\\s*,\\s*")));

            // Creamos el estudiante y lo guardamos
            Alumno nuevoAlumno = new Alumno(idAlumno, nombre, semestre, promedio, intereses);
            gestor.registrarAlumno(nuevoAlumno);

        } catch (InputMismatchException e) {
            System.out.println("⚠️ Error en la entrada de datos.");
        } catch (NumberFormatException e) {
            // Este error salta si el usuario escribe "hola" en el campo de promedio, por ejemplo.
            System.out.println("⚠️ Error: Asegúrese de ingresar valores numéricos válidos para Semestre y Promedio.");
        }
    }

    // --- SECCIÓN 2: Control de Inscripciones ---

    // Submenú para operaciones diarias: meter o sacar alumnos de clases.
    public static void menuInscripciones() {
        String op = "";
        do {
            System.out.println("\n--- ✍️ INSCRIPCIONES ---");
            System.out.println("-------------------------");
            System.out.println("1. 📝 Inscribir alumno en curso");
            System.out.println("2. ❌ Dar de baja alumno del curso");
            System.out.println("0. 🔙 Volver al Menú Principal");
            System.out.println("-------------------------");
            System.out.print("Elige una opción: ");
            op = scanner.nextLine();

            switch (op) {
                case "1":
                    // Solicita IDs y delega la validación de cupos y requisitos al gestor
                    System.out.print("ID del Alumno: ");
                    String idA_inscribir = scanner.nextLine();
                    System.out.print("ID del Curso: ");
                    String idC_inscribir = scanner.nextLine();
                    gestor.inscribirAlumnoEnCurso(idA_inscribir, idC_inscribir);
                    break;
                case "2":
                    // Proceso inverso: retirar a un alumno
                    System.out.print("ID del Alumno a dar de baja: ");
                    String idA_baja = scanner.nextLine();
                    System.out.print("ID del Curso: ");
                    String idC_baja = scanner.nextLine();
                    gestor.darDeBajaAlumnoDelCurso(idA_baja, idC_baja);
                    break;
                case "0":
                    System.out.println("🔙 Saliendo de Inscripciones...");
                    break;
                default:
                    System.out.println("⚠️ Opción no válida. Intenta de nuevo.");
            }
        } while (!op.equals("0"));
    }

    // --- SECCIÓN 3: Listas de Espera ---

    // Gestiona qué pasa cuando un curso está lleno.
    public static void menuListasEspera() {
        System.out.println("\n--- 3. LISTAS DE ESPERA ---");
        System.out.print("ID del Curso para ver la lista de espera: ");
        String idC = scanner.nextLine();

        // Muestra quiénes están esperando y en qué orden (según prioridad)
        gestor.mostrarListaDeEspera(idC);

        // Si el curso está lleno, permite al usuario simular manualmente
        // que se libera un espacio para ver cómo entra automáticamente el siguiente alumno.
        Curso curso = gestor.consultarCurso(idC);
        if (curso != null && curso.getCuposDisponibles() <= 0) {
            System.out.print("\n¿Simular la liberación de un cupo para procesar la lista de espera? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                gestor.procesarListaDeEspera(idC); // Mueve al primero de la fila adentro del curso
            }
        }
    }

    // --- SECCIÓN 4: Sistema de Recomendaciones ---

    // Cruza los intereses del alumno con las áreas de los cursos.
    public static void pedirRecomendacion() {
        System.out.println("\n--- 4. RECOMENDACIONES ---");
        System.out.print("ID del Alumno para recomendaciones: ");
        String idA = scanner.nextLine();
        // El gestor se encarga de la lógica de coincidencia (intersección)
        gestor.recomendarCursos(idA);
    }

    // --- SECCIÓN 5: Reportes y Estadísticas ---

    // Submenú para consultar información analítica.
    public static void menuReportes() {
        String op = "";
        do {
            System.out.println("\n--- 📊 REPORTES Y ESTADÍSTICAS ---");
            System.out.println("---------------------------------");
            System.out.println("1. 🎓 Carga académica de un alumno (Créditos)");
            System.out.println("2. 📋 Lista de alumnos inscritos en un curso");
            System.out.println("3. 🔥 Cursos con más demanda (Inscritos + Espera)");
            System.out.println("0. 🔙 Volver al Menú Principal");
            System.out.println("---------------------------------");
            System.out.print("Elige una opción: ");
            op = scanner.nextLine();

            // Conversión rápida de la opción para usar switch
            int opcionNumerica;
            try {
                opcionNumerica = Integer.parseInt(op);
            } catch (NumberFormatException e) {
                opcionNumerica = -1;
            }

            switch (opcionNumerica) {
                case 1:
                    // Calcula cuántos créditos lleva el alumno sumando sus cursos
                    System.out.print("ID del Alumno: ");
                    String idA = scanner.nextLine();
                    gestor.cargaAcademica(idA);
                    break;
                case 2:
                    // Muestra quiénes están dentro de una clase específica
                    System.out.print("ID del Curso: ");
                    String idC = scanner.nextLine();
                    gestor.listarAlumnosInscritosEnCurso(idC);
                    break;
                case 3:
                    // Muestra qué cursos son los más populares
                    gestor.cursosConMasDemanda();
                    break;
                case 0:
                    System.out.println("🔙 Saliendo de Reportes...");
                    break;
                default:
                    System.out.println("⚠️ Opción no válida.");
            }
        } while (!op.equals("0"));
    }
}