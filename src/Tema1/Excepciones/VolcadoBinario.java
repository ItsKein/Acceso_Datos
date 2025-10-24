package Tema1.Excepciones;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Clase que permite realizar un volcado binario (en formato hexadecimal) de un fichero.
 * Ahora puede escribir el resultado en cualquier PrintStream.
 */
public class VolcadoBinario {
    static int TAM_FILA = 32;
    static int MAX_BYTES = 2048;
    InputStream is = null;
    PrintStream ps = null;

    /**
     * Constructor de la clase.
     * @param is El InputStream desde el que se leerán los datos.
     * @param ps El PrintStream donde se escribirá el volcado.
     */
    public VolcadoBinario(InputStream is, PrintStream ps) {
        this.is = is;
        this.ps = ps;
    }

    /**
     * Realiza el volcado de los bytes del fichero.
     * @throws IOException Si ocurre un error de entrada/salida durante la lectura.
     */
    public void volcar() throws IOException {
        byte buffer[] = new byte[TAM_FILA];
        int bytesLeidos;
        int offset = 0;

        do {
            bytesLeidos = is.read(buffer);
            if (bytesLeidos == -1) break; // fin de fichero

            ps.format("[%5d] ", offset);
            for (int i = 0; i < bytesLeidos; i++) {
                ps.format("%02x ", buffer[i]);
            }
            ps.println();
            offset += bytesLeidos;
        } while (bytesLeidos == TAM_FILA && offset < MAX_BYTES);
    }

    /**
     * Método principal: realiza el volcado binario de un fichero hacia otro fichero de texto.
     * @param args args[0] -> fichero de entrada, args[1] -> fichero de salida.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java Excepciones.VolcadoBinario <fichero_entrada> <fichero_salida>");
            return;
        }

        String nomFich = args[0];
        String nomSalida = args[1];

        try (
                FileInputStream fis = new FileInputStream(nomFich);
                PrintStream ps = new PrintStream(new FileOutputStream(nomSalida))
        ) {
            VolcadoBinario vb = new VolcadoBinario(fis, ps);
            vb.volcar();
            System.out.println("Volcado completado. Resultado guardado en: " + nomSalida);
        } catch (FileNotFoundException e) {
            System.err.println("ERROR: no existe fichero " + nomFich);
        } catch (IOException e) {
            System.err.println("ERROR de E/S: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
