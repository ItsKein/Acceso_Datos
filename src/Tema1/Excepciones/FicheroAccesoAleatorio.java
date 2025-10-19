package Tema1.Excepciones;

// Almacenamiento de registros de longitud fija en fichero acceso aleatorio
/*
La clase desarrollada en el siguiente ejemplo permite almacenar registros con datos de clientes en un fichero de
acceso aleatorio. Los datos de cada cliente se almacenan en un registro, que es una estructura de longitud fija
dividida en campos de longitud fija. En este caso, los campos son DNI, nombre y código postal. El constructor
de la clase toma una lista con la definición del registro. Cada elemento de la lista contiene la definición
de un campo en un par <nombre, longitud>. Los valores de los campos para un registro se almacenan en un HashMap,
que contiene pares <nombre, valor>, cada uno de los cuales contiene el valor para un campo.
Al constructor se le proporciona un nombre de fichero. Si el fichero no existe, se crea.
Si el fichero existe, se calcula el número de registros que contiene, dividiendo la longitud del fichero
en bytes por la longitud de cada registro.
El método más interesante es insertar(). Tiene dos variantes. Si no se le indica la posición,
añade el registro al final del fichero. Si no, en la posición que se le indique. La posición
del primer registro es 0, no 1. Los textos se almacenan siempre codificados en UTF-8.
Como es relativamente habitual en los métodos, no gestionan las excepciones que puede generar (throws IOException),
y dejan esto para el programa principal.
 */

/*
Para añadir la librería siguiente:
https://openjfx.io/openjfx-docs/#install-javafx
https://gluonhq.com/products/javafx/
La versión javafx-sdk-21.0.8 es suficiente
 */
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FicheroAccesoAleatorio {

    private File f;
    private List<Pair<String, Integer>> campos;
    private long longReg;
    private long numReg = 0;

    FicheroAccesoAleatorio(String nomFich, List<Pair<String, Integer>> campos)
            throws IOException {
        this.campos = campos;
        this.f = new File(nomFich);
        longReg = 0;

        for (Pair<String, Integer> campo : campos) {
            this.longReg += campo.getValue();
        }

        if (f.exists()) {
            this.numReg = f.length() / this.longReg;
        }
    }

    public long getNumReg() {
        return numReg;
    }

    public void insertar(Map<String, String> reg) throws IOException {
        insertar(reg, this.numReg++);
    }

    public void insertar(Map<String, String> reg, long pos) throws IOException {
        try (RandomAccessFile faa = new RandomAccessFile(f, "rws")) {
            faa.seek(pos * this.longReg);

            for (Pair<String, Integer> campo : this.campos) {
                String nomCampo = campo.getKey();
                Integer longCampo = campo.getValue();
                String valorCampo = reg.get(nomCampo);

                if (valorCampo == null) {
                    valorCampo = "";
                }

                String valorCampoForm = String.format("%1$-" + longCampo + "s", valorCampo);
                faa.write(valorCampoForm.getBytes(StandardCharsets.UTF_8), 0, longCampo);
            }
        }
    }

    /**
     * Método que obtiene el valor de un campo dado su nombre y posición de registro.
     *
     * @param pos          posición del registro (empezando en 0)
     * @param nombreCampo  nombre del campo cuyo valor queremos obtener
     * @return el valor leído del campo, como String (sin espacios finales)
     * @throws IOException si ocurre un error de E/S
     */
    public String obtenValorCampo(int pos, String nombreCampo) throws IOException {
        int desplazamiento = 0;
        int longCampo = -1;

        // Calcular desplazamiento dentro del registro y longitud del campo
        for (Pair<String, Integer> campo : this.campos) {
            if (campo.getKey().equalsIgnoreCase(nombreCampo)) {
                longCampo = campo.getValue();
                break;
            } else {
                desplazamiento += campo.getValue();
            }
        }

        if (longCampo == -1) {
            throw new IllegalArgumentException("Campo no encontrado: " + nombreCampo);
        }

        byte[] buffer = new byte[longCampo];

        try (RandomAccessFile faa = new RandomAccessFile(f, "r")) {
            faa.seek(pos * this.longReg + desplazamiento);
            faa.readFully(buffer);
        }

        // Convertir bytes a String (UTF-8) y eliminar espacios en blanco
        return new String(buffer, StandardCharsets.UTF_8).trim();
    }

    public static void main(String[] args) {
        List<Pair<String, Integer>> campos = new ArrayList<>();
        campos.add(new Pair<>("DNI", 9));
        campos.add(new Pair<>("NOMBRE", 32));
        campos.add(new Pair<>("CP", 5));

        try {
            FicheroAccesoAleatorio faa = new FicheroAccesoAleatorio("fic_acceso_aleat.dat", campos);

            Map<String, String> reg = new HashMap<>();
            reg.put("DNI", "56789012B");
            reg.put("NOMBRE", "SAMPER");
            reg.put("CP", "29730");
            faa.insertar(reg);

            reg.clear();
            reg.put("DNI", "89012345E");
            reg.put("NOMBRE", "ROJAS");
            faa.insertar(reg);

            reg.clear();
            reg.put("DNI", "23456789D");
            reg.put("NOMBRE", "DORCE");
            reg.put("CP", "13700");
            faa.insertar(reg);

            reg.clear();
            reg.put("DNI", "78901234X");
            reg.put("NOMBRE", "NADALES");
            reg.put("CP", "44126");
            faa.insertar(reg, 1);

            // Mostrar resultados
            System.out.println("Valor del campo 'NOMBRE' en el registro 0: " + faa.obtenValorCampo(0, "NOMBRE"));
            System.out.println("Valor del campo 'NOMBRE' en el registro 1: " + faa.obtenValorCampo(1, "NOMBRE"));
            System.out.println("Valor del campo 'NOMBRE' en el registro 2: " + faa.obtenValorCampo(2, "NOMBRE"));

        } catch (IOException e) {
            System.err.println("Error de E/S: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
