package JSON;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.Arrays;
import java.util.List;

public class MainJackson {
    public static void main(String[] args) {
        // 1. Creamos un objeto Java que queremos convertir a JSON
        List<String> plataformas = Arrays.asList("Nintendo Switch", "Wii U");
        VideoJuego zeldaBOTW = new VideoJuego("The Legend of Zelda: Breath of the Wild", 2017, "Nintendo", plataformas);

        // 2. Creamos una instancia de ObjectMapper, el corazón de Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        // Opcional: para que el JSON de salida esté formateado y sea legible
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            // --- SERIALIZACIÓN: Objeto Java a String JSON ---
            System.out.println("### SERIALIZACIÓN (Java -> JSON) ###");
            String jsonString = objectMapper.writeValueAsString(zeldaBOTW);
            System.out.println(jsonString);

            // --- DESERIALIZACIÓN: String JSON a Objeto Java ---
            System.out.println("\n### DESERIALIZACIÓN (JSON -> Java) ###");
            String jsonParaDeserializar = "{\"titulo\":\"Elden Ring\",\"anioLanzamiento\":2022,\"desarrollador\":\"FromSoftware\",\"plataformas\":[\"PS5\",\"Xbox Series\",\"PC\"]}";

            // Le decimos a Jackson que lea la cadena y la convierta a un objeto de la clase Videojuego
            VideoJuego eldenRing = objectMapper.readValue(jsonParaDeserializar, VideoJuego.class);
            System.out.println("Objeto Java deserializado: " + eldenRing);
            System.out.println("Título del juego: " + eldenRing.getTitulo());

        } catch (JsonProcessingException e) {
            System.err.println("Error al procesar el JSON: " + e.getMessage());
        }
    }
}


