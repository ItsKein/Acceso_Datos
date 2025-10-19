package EjercicioJackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // --- 1. Crear algunos productos ---
            Producto p1 = new Producto("P001", "Portátil HP", 799.99, 10);
            Producto p2 = new Producto("P002", "Ratón Logitech", 25.50, 50);
            Producto p3 = new Producto("P003", "Teclado Mecánico", 89.90, 20);

            List<Producto> lista = new ArrayList<>();
            lista.add(p1);
            lista.add(p2);
            lista.add(p3);

            // --- 2. Crear el inventario ---
            Inventario inventario = new Inventario("TechStore Valencia", lista);

            // --- 3. Serializar a JSON ---
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT); // formato legible
            File fichero = new File("inventario.json");
            mapper.writeValue(fichero, inventario);

            System.out.println("Inventario guardado correctamente en " + fichero.getAbsolutePath());

            // --- 4. Deserializar desde el fichero ---
            Inventario inventarioLeido = mapper.readValue(fichero, Inventario.class);

            // --- 5. Verificación: mostrar datos ---
            System.out.println("\nInventario leído desde el JSON:");
            System.out.println("Tienda: " + inventarioLeido.getNombreTienda());
            for (Producto p : inventarioLeido.getProductos()) {
                System.out.println("  - " + p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
