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

    // --- 1. Submenú de Gestión ---

    public static void menuGestion() {
        String op = "";

        // El bucle do-while mantiene al usuario en el submenú (persistencia)
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

            // El switch evalúa la opción elegida
            switch (op) {
                case "1.1": // Listar Cursos
                    listarCursos();
                    break;

                case "1.2": // Consultar Curso por ID
                    System.out.print("ID del Curso: ");
                    String idC = scanner.nextLine();
                    Curso c = gestor.consultarCurso(idC);
                    System.out.println(c != null ? c : "⚠️ Curso no encontrado.");
                    break;

                case "1.3": // Consultar Alumno por ID
                    System.out.print("ID del Alumno: ");
                    String idA = scanner.nextLine();
                    Alumno a = gestor.consultarAlumno(idA);
                    System.out.println(a != null ? a : "⚠️ Alumno no encontrado.");
                    break;

                case "1.4": // Registrar Nuevo Curso
                    registrarNuevoCurso();
                    break;

                case "1.5": // Registrar Nuevo Alumno
                    registrarNuevoAlumno();
                    break;

                case "0":
                    System.out.println("Saliendo de Gestión...");
                    break; // La acción es salir, el bucle lo detecta

                default:
                    System.out.println("⚠️ Opción no válida en el submenú de Gestión.");
            }

        } while (!op.equals("0")); // La condición se repite hasta que op sea "0"
    }

    public static void listarCursos() {
        System.out.println("\n--- LISTA DE CURSOS EXISTENTES ---");
        Collection<Curso> listaCursos = gestor.obtenerTodosLosCursos();

        if (listaCursos.isEmpty()) {
            System.out.println("No hay cursos registrados en el sistema.");
            return;
        }

        for (Curso curso : listaCursos) {
            System.out.println("------------------------------------------");
            System.out.println("🆔 ID: " + curso.getIdCurso());
            System.out.println("📚 Nombre: " + curso.getNombre());
            System.out.println("👨‍🏫 Docente: " + curso.getDocente());
            System.out.println("➡️ Cupos: " + curso.getCuposDisponibles() + "/" + curso.getCupoMaximo() + (curso.getCuposDisponibles() == 0 ? " (LLENO!)" : ""));
            System.out.println("🏷️  Áreas: " + curso.getAreas());
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
            System.out.println("⚠️ Error. Asegúrese de ingresar números para Cupo O Créditos Y que el Alumno no este previamente registrado");
            scanner.nextLine(); // Limpiar buffer
        }
    }

    public static void registrarNuevoAlumno() {
        System.out.println("\n--- REGISTRO DE NUEVO ALUMNO ---");

        try {
            System.out.print("ID del Alumno (ej: A100): ");
            String idAlumno = scanner.nextLine();

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();

            // --- Solución 1: Leer todo como String y convertir ---
            // (Esto es más robusto para la entrada de números y la gestión del buffer)

            System.out.print("Semestre: ");
            String semestreStr = scanner.nextLine();
            int semestre = Integer.parseInt(semestreStr);

            System.out.print("Promedio (ej: 9.5): ");
            String promedioStr = scanner.nextLine();
            double promedio = Double.parseDouble(promedioStr);

            // --- Intereses (Ahora sí lee la línea completa) ---
            System.out.print("Intereses (separados por coma, ej: etica, IA): ");
            String interesesStr = scanner.nextLine();

            Set<String> intereses = new HashSet<>(Arrays.asList(interesesStr.split("\\s*,\\s*")));

            //registro de objeto alumno
            Alumno nuevoAlumno = new Alumno(idAlumno, nombre, semestre, promedio, intereses);
            gestor.registrarAlumno(nuevoAlumno);

        } catch (InputMismatchException e) {
            // Esto captura errores si se usa nextInt/nextDouble
            System.out.println("⚠️ Error en la entrada de datos. El buffer del scanner falló.");
            // Ya no es estrictamente necesario, pero es un buen guardián.
        } catch (NumberFormatException e) {
            // Esto captura si el usuario escribe texto donde se espera un número
            System.out.println("⚠️ Error: Asegúrese de ingresar valores numéricos válidos para Semestre y Promedio.");
        }
    }

    // --- 2. Submenú de Inscripciones ---

    public static void menuInscripciones() {
        String op = "";

        // El bucle do-while mantiene la persistencia
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
                case "1": // Inscribir
                    System.out.print("ID del Alumno a inscribir: ");
                    String idA_inscribir = scanner.nextLine();
                    System.out.print("ID del Curso: ");
                    String idC_inscribir = scanner.nextLine();
                    gestor.inscribirAlumnoEnCurso(idA_inscribir, idC_inscribir);
                    break;

                case "2": // Dar de baja
                    System.out.print("ID del Alumno a dar de baja: ");
                    String idA_baja = scanner.nextLine();
                    System.out.print("ID del Curso: ");
                    String idC_baja = scanner.nextLine();
                    gestor.darDeBajaAlumnoDelCurso(idA_baja, idC_baja);
                    break;

                case "0":
                    System.out.println("Saliendo de Inscripciones...");
                    break; // Sale del switch, el do-while termina

                default:
                    System.out.println("⚠️ Opción no válida.");
            }

        } while (!op.equals("0"));
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
        String op = "";

        // El bucle do-while mantiene al usuario en el submenú de Reportes
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

            // Usamos Integer.parseInt() para el switch, manejando el error
            int opcionNumerica;
            try {
                opcionNumerica = Integer.parseInt(op);
            } catch (NumberFormatException e) {
                opcionNumerica = -1; // Valor no válido
            }

            switch (opcionNumerica) {
                case 1:
                    System.out.print("ID del Alumno: ");
                    String idA = scanner.nextLine();
                    gestor.cargaAcademica(idA);
                    break;
                case 2:
                    System.out.print("ID del Curso: ");
                    String idC = scanner.nextLine();
                    gestor.listarAlumnosInscritosEnCurso(idC);
                    break;
                case 3:
                    gestor.cursosConMasDemanda();
                    break;
                case 0:
                    System.out.println("Saliendo de Reportes...");
                    break;
                default:
                    System.out.println("⚠️ Opción no válida en el submenú de Reportes.");
            }

        } while (!op.equals("0")); // Condición: salir solo si la opción es "0"
    }
}