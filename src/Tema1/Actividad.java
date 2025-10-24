package Tema1;

import java.io.*;

public class Actividad {

    public class ConvertirUTF8 {
        public static void main(String[] args) {
            // Nombres de los ficheros
            String inputFile = "entrada_utf8.txt";
            String isoFile = "salida_iso8859_1.txt";
            String utf16File = "salida_utf16.txt";

            try (
                    // Leemos el fichero en UTF-8
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));

                    // Salida ISO-8859-1
                    BufferedWriter isoWriter = new BufferedWriter(
                            new OutputStreamWriter(new FileOutputStream(isoFile), "ISO-8859-1"));

                    // Salida UTF-16
                    BufferedWriter utf16Writer = new BufferedWriter(
                            new OutputStreamWriter(new FileOutputStream(utf16File), "UTF-16"));
            ) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Escribimos la línea en ambos ficheros
                    isoWriter.write(line);
                    isoWriter.newLine();

                    utf16Writer.write(line);
                    utf16Writer.newLine();
                }

                System.out.println("Conversión completada correctamente.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
