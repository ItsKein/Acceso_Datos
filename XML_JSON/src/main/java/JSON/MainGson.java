package JSON;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.List;

public class MainGson {
    public static void main(String[] args) {
        // 1. Creamos el objeto Java
        List<String> plataformas = Arrays.asList("PC", "PS4", "Xbox One");
        VideoJuego theWitcher3 = new VideoJuego("The Witcher 3: Wild Hunt", 2015, "CD Projekt Red", plataformas);

        // 2. Creamos una instancia de Gson.
        // Usamos GsonBuilder para crear un JSON formateado ("pretty printing")
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // --- SERIALIZACIÓN: Objeto Java a String JSON ---
        System.out.println("### SERIALIZACIÓN (Java -> JSON) ###");
        String jsonString = gson.toJson(theWitcher3);
        System.out.println(jsonString);

        // --- DESERIALIZACIÓN: String JSON a Objeto Java ---
        System.out.println("\n### DESERIALIZACIÓN (JSON -> Java) ###");
        String jsonParaDeserializar = "{\"titulo\":\"Red Dead Redemption 2\",\"anioLanzamiento\":2018,\"desarrollador\":\"Rockstar Games\",\"plataformas\":[\"PS4\",\"Xbox One\",\"PC\"]}";

        // El método fromJson recibe el String y la clase a la que convertirlo
        VideoJuego rdr2 = gson.fromJson(jsonParaDeserializar, VideoJuego.class);
        System.out.println("Objeto Java deserializado: " + rdr2);
        System.out.println("Desarrollador del juego: " + rdr2.getDesarrollador());
    }
}

