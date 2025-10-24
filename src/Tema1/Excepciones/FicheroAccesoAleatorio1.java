package Tema1.Excepciones;

import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FicheroAccesoAleatorio1 {

    private File f;
    private List<Pair<String, Integer>> campos;
    private long longReg;
    private long numReg = 0;

    /**
     * Constructor para inicializar la clase y el archivo de acceso aleatorio.
     * @param nomFich El nombre del archivo a utilizar.
     * @param campos  Una lista de pares (nombre del campo, longitud del campo)
     * @throws IOException Si ocurre un error al acceder al archivo.
     */
    FicheroAccesoAleatorio1(String nomFich, List<Pair<String, Integer>> campos)
            throws IOException {
        this.campos = campos;
        this.f = new File(nomFich);
        longReg = 0;
        // Calcula la longitud total de un registro sumando las longitudes de todos los campos.
        for (Pair<String, Integer> campo: campos) {
            this.longReg += campo.getValue();
        }
        // Si el archivo ya existe, calcula el número de registros que contiene.
        if (f.exists()) {
            this.numReg=f.length()/this.longReg;
        }
    }

    public long getNumReg() {
        return numReg;
    }

    public void insertar(Map<String, String> reg) throws IOException {
        insertar(reg, this.numReg++);
    }

    /**
     * Inserta un registro en una posición específica del archivo.
     * @param reg Un mapa con los datos del registro (nombre del campo, valor).
     * @param pos La posición (índice) donde se debe insertar el registro.
     * @throws IOException Si ocurre un error de E/S.
     */
    public void insertar(Map<String, String> reg, long pos) throws IOException {

        // Utiliza un bloque try-with-resources para asegurar el cierre automático del RandomAccessFile.
        try (RandomAccessFile faa = new RandomAccessFile(f, "rws")) {

            // Posiciona el puntero de escritura en el lugar correcto.
            // La posición se calcula multiplicando la posición del registro por la longitud de cada registro.
            faa.seek(pos * this.longReg);

            // Itera sobre los campos definidos para el registro.
            for (Pair<String, Integer> campo: this.campos) {
                String nomCampo=campo.getKey();
                Integer longCampo = campo.getValue();
                String valorCampo = reg.get(nomCampo);

                // Si el valor del campo no se encuentra en el mapa, se usa una cadena vacía.
                if (valorCampo == null) {
                    valorCampo = "";
                }

                // Formatea el valor del campo para que tenga la longitud fija.
                // %1$-...s significa que se alinea a la izquierda y se rellena con espacios.
                String valorCampoForm = String.format("%1$-" + longCampo + "s",
                        valorCampo);

                // Escribe los bytes del valor formateado en el archivo.
                faa.write(valorCampoForm.getBytes("UTF-8"), 0, longCampo);
            }
        }
    }

    static void main(String[] args) {

        // Define la estructura de los campos
        List<Pair<String, Integer>> campos = new ArrayList<>();
        campos.add(new Pair<>("DNI", 9));
        campos.add(new Pair<>("NOMBRE", 32));
        campos.add(new Pair<>("CP", 5));

        try {
            // Crea el fichero (si no existe)
            FicheroAccesoAleatorio1 faa = new FicheroAccesoAleatorio1("fic_acceso_aleat.dat", campos);

            // Registro normal
            Map<String, String> reg = new HashMap<>();
            reg.put("DNI", "12345678A");
            reg.put("NOMBRE", "PEREZ");
            reg.put("CP", "28080");
            faa.insertar(reg);

            // Registro en posición 0 (sobrescribe el anterior)
            reg.clear();
            reg.put("DNI", "87654321B");
            reg.put("NOMBRE", "GARCIA");
            reg.put("CP", "29009");
            faa.insertar(reg, 0);

            // 🚨 Registro en una posición mucho mayor (por ejemplo, posición 10)
            // Aunque el fichero tenga solo 1 o 2 registros, intentamos escribir en la posición 10
            reg.clear();
            reg.put("DNI", "99999999Z");
            reg.put("NOMBRE", "LARGO");
            reg.put("CP", "50001");

            System.out.println("Insertando registro en posición 10...");
            faa.insertar(reg, 10);

            System.out.println("Número de registros (según contador interno): " + faa.getNumReg());
            System.out.println("Revisa el tamaño del fichero generado con un editor hexadecimal o 'xxd'.");

        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
