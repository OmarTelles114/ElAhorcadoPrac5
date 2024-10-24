import java.util.HashSet;

public class Jugador {
    private String nombre;
    private int puntos;
    private HashSet<Character> letrasIngresadas;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.puntos = 0;
        this.letrasIngresadas = new HashSet<>();
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntos() {
        return puntos;
    }

    public void agregarPuntos(int puntos) {
        this.puntos += puntos;
    }

    public void reiniciarLetras() {
        letrasIngresadas.clear();
    }

    public boolean haIngresadoLetra(char letra) {
        return letrasIngresadas.contains(letra);
    }

    public void agregarLetra(char letra) {
        letrasIngresadas.add(letra);
    }
}
