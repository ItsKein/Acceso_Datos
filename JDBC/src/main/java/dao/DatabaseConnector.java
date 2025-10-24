package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnector {

    private static final String PROPERTIES_FILE = "config.properties";
    private static Properties properties = new Properties();
    private String user;
    private String password;
    private String url;
    public Connection connection;

    public DatabaseConnector() throws SQLException{
        if(connection == null) {
            connection = getConnection();
        }
    }

    // Define un bloque de código estático. Este bloque se ejecutará automáticamente
    // una sola vez cuando la clase que lo contiene sea cargada por primera vez
    // en la Máquina Virtual de Java (JVM), incluso antes de que se cree cualquier
    // objeto (instancia) de esa clase.
    static {
        // Inicia un bloque 'try-with-resources'. Esto asegura que el recurso
        // (en este caso, el InputStream) se cierre automáticamente al finalizar,
        // evitando fugas de memoria y recursos.
        // Usa un bloque try-with-resources para asegurar que el InputStream se cierre
        // Pide al ClassLoader que busque el recurso "config.properties" en el classpath
        try (InputStream input = DatabaseConnector.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            // Cargar el fichero de propiedades.
            // El objeto 'properties' (que debe ser una variable estática de la clase)
            // lee el flujo de datos del archivo y mapea las claves y valores
            // definidos en 'database.properties'.
            properties.load(input);

            // Carga la clase del driver explícitamente.
            // Aunque los drivers JDBC 4.0+ pueden auto-registrarse, esta línea
            // fuerza la carga de la clase del driver de MySQL en memoria.
            // El valor de 'db.driver' se obtiene del fichero de propiedades recién cargado.
            Class.forName(properties.getProperty("db.driver"));

            // Captura dos tipos de excepciones que podrían ocurrir en el bloque 'try'.
            // IOException: Si hay un problema al leer el archivo (no existe, no hay permisos, etc.).
            // ClassNotFoundException: Si la clase del driver JDBC no se encuentra en el classpath
            // (falta el JAR del conector de MySQL).
        } catch (IOException | ClassNotFoundException e) {

            // Imprime la traza de la excepción en la consola de error. Es útil para depurar,
            // pero en una aplicación de producción se debería usar un sistema de logging (como Log4j).
            e.printStackTrace();

            // Lanza una excepción más específica.
            // En lugar de dejar que la aplicación continúe en un estado inconsistente (sin
            // configuración de base de datos), se detiene la ejecución lanzando una
            // RuntimeException. Esto se conoce como "fallar rápido" (fail-fast) y es una
            // buena práctica porque evita errores más difíciles de rastrear más adelante.
            throw new RuntimeException("Error al cargar la configuración de la base de datos.", e);
        }
    }
    /**
     * Obtiene una conexión a la base de datos utilizando las propiedades cargadas.
     * @return Una conexión a la base de datos.
     * @throws SQLException Si ocurre un error al intentar conectar.
     */
    private Connection getConnection() throws SQLException {
        user = properties.getProperty("db.user");
        password = properties.getProperty("db.password");
        url = properties.getProperty("db.url");
        return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.user"),
                properties.getProperty("db.password")
        );
    }
}
