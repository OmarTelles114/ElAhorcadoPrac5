import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Main {
    private static final ArrayList<Jugador> jugadores = new ArrayList<>();
    private static final HashSet<Character> letrasUsadas = new HashSet<>();
    private static final Random random = new Random();
    private static int puntosParaGanar;
    private static boolean continuarJugando = true;
    private static int jugadorActual = 0;  // índice del jugador actual
    private static JLabel fraseLabel;
    private static JLabel puntajesLabel;
    private static Frase frase;
    private static Jugador ganadorRonda;

    public static void main(String[] args) {
        iniciarJuego();
        crearVentanaJuego();
    }

    private static void iniciarJuego() {
        JFrame frame = new JFrame("Ahorcado - Configuración");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Configuración de jugadores
        int numJugadores = 0;
        while (numJugadores < 2 || numJugadores > 4) {
            try {
                numJugadores = Integer.parseInt(JOptionPane.showInputDialog(frame, "Ingrese el número de jugadores (2-4):"));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Por favor, ingrese un número válido.");
            }
        }

        for (int i = 0; i < numJugadores; i++) {
            String nombre = JOptionPane.showInputDialog(frame, "Ingrese el nombre del jugador " + (i + 1) + ":");
            jugadores.add(new Jugador(nombre));
        }

        // Configuración de puntos para ganar
        while (puntosParaGanar <= 0) {
            try {
                puntosParaGanar = Integer.parseInt(JOptionPane.showInputDialog(frame, "Ingrese los puntos necesarios para ganar (mayor a 0):"));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Por favor, ingrese un número válido.");
            }
        }

        frame.dispose();
    }

    private static void crearVentanaJuego() {
        JFrame juegoFrame = new JFrame("Juego de Ahorcado");
        juegoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        juegoFrame.setSize(600, 400);
        juegoFrame.setLayout(new BorderLayout());

        iniciarNuevaRonda();

        // Panel para mostrar la frase oculta
        fraseLabel = new JLabel(frase.mostrarFraseOculta());
        fraseLabel.setFont(new Font("Arial", Font.BOLD, 24));
        fraseLabel.setHorizontalAlignment(SwingConstants.CENTER);
        juegoFrame.add(fraseLabel, BorderLayout.NORTH);

        // Panel de letras (botones para el alfabeto)
        JPanel letrasPanel = new JPanel(new GridLayout(4, 7));
        for (char letra = 'a'; letra <= 'z'; letra++) {
            JButton letraBtn = new JButton(String.valueOf(letra));
            letraBtn.addActionListener(new LetraButtonListener(letra));
            letrasPanel.add(letraBtn);
        }
        juegoFrame.add(letrasPanel, BorderLayout.CENTER);

        // Panel de puntajes
        puntajesLabel = new JLabel();
        actualizarPuntajes();
        juegoFrame.add(puntajesLabel, BorderLayout.SOUTH);

        juegoFrame.setVisible(true);
    }

    private static void iniciarNuevaRonda() {
        frase = new Frase();
        frase.seleccionarFraseAleatoria();
        letrasUsadas.clear();
    }

    private static void actualizarPuntajes() {
        StringBuilder puntajes = new StringBuilder("<html>Puntajes actuales:<br>");
        for (Jugador jugador : jugadores) {
            puntajes.append(jugador.getNombre()).append(": ").append(jugador.getPuntos()).append(" puntos<br>");
        }
        puntajes.append("</html>");
        puntajesLabel.setText(puntajes.toString());
    }

    private static void siguienteTurno() {
        jugadorActual = (jugadorActual + 1) % jugadores.size();
    }

    private static class LetraButtonListener implements ActionListener {
        private final char letra;

        public LetraButtonListener(char letra) {
            this.letra = letra;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Jugador jugador = jugadores.get(jugadorActual);
            if (letrasUsadas.contains(letra)) {
                JOptionPane.showMessageDialog(null, "Ya se ha usado esa letra. Pierdes 3 puntos.");
                jugador.agregarPuntos(-3);
                siguienteTurno();
            } else if (frase.letraEstaEnFrase(letra)) {
                int ocurrencias = frase.contarOcurrenciasLetra(letra);
                int puntosGanados = 3 * ocurrencias;
                jugador.agregarPuntos(puntosGanados);
                letrasUsadas.add(letra);
                frase.agregarLetraAdivinada(letra);
                fraseLabel.setText(frase.mostrarFraseOculta());

                if (frase.fraseAdivinada()) {
                    ganadorRonda = jugador;
                    JOptionPane.showMessageDialog(null, "¡" + ganadorRonda.getNombre() + " ha adivinado la frase!");
                    preguntarNuevaRonda();
                }
            } else {
                JOptionPane.showMessageDialog(null, "La letra no está en la frase. Pierdes 1 punto.");
                jugador.agregarPuntos(-1);
                letrasUsadas.add(letra);
                siguienteTurno();
            }
            actualizarPuntajes();
        }
    }

    private static void preguntarNuevaRonda() {
        int respuesta = JOptionPane.showConfirmDialog(null, "¿Desean empezar una nueva partida?", "Nueva Partida", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            iniciarNuevaRonda();
            jugadorActual = jugadores.indexOf(ganadorRonda);  // el ganador de la ronda comienza la siguiente
            fraseLabel.setText(frase.mostrarFraseOculta());
        } else {
            continuarJugando = false;
            anunciarGanador();
        }
    }

    private static void anunciarGanador() {
        Jugador ganador = jugadores.stream().max((j1, j2) -> Integer.compare(j1.getPuntos(), j2.getPuntos())).orElse(null);
        if (ganador != null) {
            JOptionPane.showMessageDialog(null, "¡El ganador del juego es " + ganador.getNombre() + " con " + ganador.getPuntos() + " puntos!");
        }
    }
}
