package Tema2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BuscarTexto {
    public static void main(String[] args) {
        // Comprobamos que se han pasado los argumentos necesarios
        if (args.length < 2) {
            System.out.println("Uso: java BuscarTextoEnFichero <ruta_del_fichero> <texto_a_buscar>");
            return;
        }

        String ruta = args[0];          // Primer argumento: la ruta del fichero
        String textoBuscado = args[1];  // Segundo argumento: el texto que queremos buscar

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            int numeroLinea = 1;

            // Leemos el fichero línea a línea
            while ((linea = br.readLine()) != null) {
                int indice = linea.indexOf(textoBuscado);

                // Buscar todas las apariciones del texto en la línea
                while (indice != -1) {
                    System.out.println("Encontrado en línea " + numeroLinea + ", columna " + (indice + 1));
                    indice = linea.indexOf(textoBuscado, indice + 1); // Buscar la siguiente aparición
                }

                numeroLinea++;
            }

        } catch (IOException e) {
            System.out.println("Error al leer el fichero: " + e.getMessage());
        }
    }
}
