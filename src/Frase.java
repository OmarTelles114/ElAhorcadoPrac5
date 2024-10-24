import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Frase {
    private String fraseOriginal;
    private char[] fraseOculta;
    private HashSet<Character> letrasAdivinadas;
    private ArrayList<String> bancoFrases;

    public Frase() {
        letrasAdivinadas = new HashSet<>();
        bancoFrases = new ArrayList<>();
        // Añadir frases al banco
        bancoFrases.add("el perro ladra fuerte");
        bancoFrases.add("la vida es un viaje");
        bancoFrases.add("el sol brilla intensamente");
        bancoFrases.add("la luna ilumina el cielo");
        bancoFrases.add("yo soy tu padre");
    }

    public String seleccionarFraseAleatoria() {
        Random random = new Random();
        fraseOriginal = bancoFrases.get(random.nextInt(bancoFrases.size()));
        fraseOculta = new char[fraseOriginal.length()];

        // Ocultar la frase con guiones bajos
        for (int i = 0; i < fraseOriginal.length(); i++) {
            fraseOculta[i] = fraseOriginal.charAt(i) == ' ' ? ' ' : '_';
        }
        return fraseOriginal;
    }

    public String mostrarFraseOculta() {
        return new String(fraseOculta);
    }

    public boolean letraEstaEnFrase(char letra) {
        return fraseOriginal.indexOf(letra) != -1;
    }

    public int contarOcurrenciasLetra(char letra) {
        int count = 0;
        for (char c : fraseOriginal.toCharArray()) {
            if (c == letra) {
                count++;
            }
        }
        return count;
    }

    public void agregarLetraAdivinada(char letra) {
        letrasAdivinadas.add(letra);
        for (int i = 0; i < fraseOriginal.length(); i++) {
            if (fraseOriginal.charAt(i) == letra) {
                fraseOculta[i] = letra;
            }
        }
    }

    public boolean fraseAdivinada() {
        return !new String(fraseOculta).contains("_");
    }
}
