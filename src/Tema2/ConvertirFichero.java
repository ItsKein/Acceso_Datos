package Tema2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ConvertirFichero {
    public static void main(String[] args) {
        // Comprobamos que se ha pasado el nombre del archivo de entrada
        if (args.length < 1) {
            System.out.println("Uso: java ConversorCodificacionFusion <archivoUTF8>");
            return;
        }

        // Archivo de entrada y salida
        String archivoOriginal = args[0];
        String archivoISO = "salida_ISO_8859_1.txt";
        String archivoUTF16 = "salida_UTF16.txt";

        File original = new File(archivoOriginal);

        // Verificar que el archivo existe antes de intentar leerlo
        if (!original.exists()) {
            System.err.println("ERROR: El archivo original no existe.");
            System.err.println("Ruta probada: " + original.getAbsolutePath());
            return;
        }

        // Bloque try-with-resources para asegurar el cierre automático de flujos
        try (
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(new FileInputStream(original), "UTF-8"));

                BufferedWriter bwISO = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(archivoISO), "ISO-8859-1"));

                BufferedWriter bwUTF16 = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(archivoUTF16), "UTF-16"))
        ) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Escribir la línea en los dos archivos de salida
                bwISO.write(linea);
                bwISO.newLine();

                bwUTF16.write(linea);
                bwUTF16.newLine();
            }

            System.out.println("Conversión completada con éxito.");
            System.out.println("Archivos generados:");
            System.out.println(" → " + archivoISO);
            System.out.println(" → " + archivoUTF16);

        } catch (IOException e) {
            System.out.println("Error al procesar los archivos: " + e.getMessage());
        }
    }
}
