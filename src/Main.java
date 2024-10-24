import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Jugador> jugadores = new ArrayList<>();
        HashSet<Character> letrasUsadas = new HashSet<>();
        Random random = new Random();

        // Escoger número de jugadores (2-4)
        int numJugadores;
        do {
            System.out.println("Ingrese el número de jugadores (2-4):");
            numJugadores = Integer.parseInt(scanner.nextLine());
            if (numJugadores < 2 || numJugadores > 4) {
                System.out.println("Valor Inválido, intenta de nuevo");
            }
        } while (numJugadores < 2 || numJugadores > 4);

        // Inicializar jugadores
        for (int i = 0; i < numJugadores; i++) {
            System.out.println("Ingrese el nombre del jugador " + (i + 1) + ":");
            String nombre = scanner.nextLine();
            jugadores.add(new Jugador(nombre));
        }

        // Escoger puntaje para ganar (N)
        int puntosParaGanar;
        do {
            System.out.println("Ingrese los puntos necesarios para ganar (mayor a 0):");
            puntosParaGanar = Integer.parseInt(scanner.nextLine());
            if (puntosParaGanar <= 0) {
                System.out.println("Valor Inválido, intenta de nuevo");
            }
        } while (puntosParaGanar <= 0);

        boolean continuarJugando = true;

        while (continuarJugando) {
            // Seleccionar frase al azar
            Frase frase = new Frase();
            String fraseActual = frase.seleccionarFraseAleatoria();
            System.out.println("Frase seleccionada: " + frase.mostrarFraseOculta());

            boolean fraseAdivinada = false;
            letrasUsadas.clear();

            // Ciclo de turnos para los jugadores
            while (!fraseAdivinada) {
                for (Jugador jugador : jugadores) {
                    if (fraseAdivinada) break;
                    fraseAdivinada = turnoJugador(jugador, frase, letrasUsadas, scanner);
                }
            }

            // Asignar 5 puntos al ganador de la ronda
            for (Jugador jugador : jugadores) {
                if (frase.fraseAdivinada()) {
                    jugador.agregarPuntos(5);
                    System.out.println("¡" + jugador.getNombre() + " ganó esta ronda y recibió 5 puntos extra!");
                    break;
                }
            }

            // Mostrar puntajes al final de la ronda
            System.out.println("\nPuntajes al final de la ronda:");
            for (Jugador jugador : jugadores) {
                System.out.println(jugador.getNombre() + ": " + jugador.getPuntos() + " puntos");
            }

            // Verificar si algún jugador ha alcanzado N puntos
            boolean ganadorFinal = false;
            for (Jugador jugador : jugadores) {
                if (jugador.getPuntos() >= puntosParaGanar) {
                    ganadorFinal = true;
                }
            }

            // Preguntar si se desea continuar jugando otra ronda
            if (!ganadorFinal) {
                System.out.println("¿Desean jugar otra ronda? (s/n)");
                String respuesta = scanner.nextLine();
                if (respuesta.equalsIgnoreCase("n")) {
                    continuarJugando = false;
                }
            } else {
                continuarJugando = false;
            }
        }

        // Anunciar el ganador final
        Jugador ganador = jugadores.stream().max(Comparator.comparingInt(Jugador::getPuntos)).orElse(null);
        if (ganador != null) {
            System.out.println("\n¡El ganador del juego es " + ganador.getNombre() + " con " + ganador.getPuntos() + " puntos!");
        }
    }

    private static boolean turnoJugador(Jugador jugador, Frase frase, HashSet<Character> letrasUsadas, Scanner scanner) {
        System.out.println("\nTurno de " + jugador.getNombre() + ".");
        System.out.println("Frase actual: " + frase.mostrarFraseOculta());

        while (true) {
            System.out.println("Ingresa una letra:");
            char letra = scanner.nextLine().toLowerCase().charAt(0);

            if (letrasUsadas.contains(letra)) {
                System.out.println("Ya ingresaste esa letra antes. Pierdes 3 puntos.");
                jugador.agregarPuntos(-3);
            } else if (frase.letraEstaEnFrase(letra)) {
                int ocurrencias = frase.contarOcurrenciasLetra(letra);
                int puntosGanados = 3 * ocurrencias;
                System.out.println("¡La letra '" + letra + "' aparece " + ocurrencias + " veces! Obtienes " + puntosGanados + " puntos.");
                jugador.agregarPuntos(puntosGanados);
                letrasUsadas.add(letra);
                frase.agregarLetraAdivinada(letra);

                // Mostrar estado actualizado de la frase
                System.out.println("Frase actualizada: " + frase.mostrarFraseOculta());

                if (frase.fraseAdivinada()) {
                    return true;
                }
            } else {
                System.out.println("La letra no está en la frase. Pierdes 1 punto.");
                jugador.agregarPuntos(-1);
                letrasUsadas.add(letra);
                break;
            }
        }

        return false;
    }
}