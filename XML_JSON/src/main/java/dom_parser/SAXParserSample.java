package dom_parser;

// Parsing con SAX

import java.io.PrintStream;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;

/**
 * GestorEventos es una clase personalizada que hereda de DefaultHandler.
 * Se utiliza para manejar los eventos generados por el parser SAX a medida que
 * recorre el documento XML. Sobrescribe métodos para reaccionar al inicio
 * de un documento, al inicio de un elemento, al encontrar texto y al final
 * de un elemento.
 */
class GestorEventos extends DefaultHandler {

    private static final String INDENT_NIVEL = "  ";  // Constante para la cadena de indentación.
    private final PrintStream ps; // Flujo de salida donde se imprimirá el resultado del análisis.
    private int nivel; // Nivel de profundidad actual en el árbol XML para la indentación.

    /**
     * Imprime espacios de indentación según el nivel de profundidad actual.
     */
    private void indenta() {
        for (int i = 0; i < nivel; i++) {    // Bucle para imprimir la indentación.
            ps.print(INDENT_NIVEL);
        }
    }

    /**
     * Constructor que recibe el flujo de salida.
     * @param ps El PrintStream donde se escribirá la salida (ej. System.out).
     */
    public GestorEventos(PrintStream ps) {
        this.ps = ps;
    }

    /**
     * Método invocado por el parser SAX al inicio del documento XML.
     * Inicializa el nivel de indentación a 0.
     */
    @Override
    public void startDocument() {
        nivel = 0;
    }

    /**
     * Método invocado al encontrar una etiqueta de inicio de elemento.
     * Incrementa el nivel, indenta, imprime el nombre del elemento y recorre
     * e imprime sus atributos con sus valores.
     * @param uri El Namespace URI.
     * @param nombreLocal El nombre local (sin prefijo).
     * @param nombreCualif El nombre cualificado (con prefijo).
     * @param atributos Los atributos asociados al elemento.
     */
    @Override
    public void startElement(String uri, String nombreLocal, String nombreCualif, Attributes atributos) {
        nivel++; // Aumenta el nivel de profundidad.
        indenta(); // Aplica la indentación.
        ps.print("<" + nombreCualif); // Imprime el nombre del elemento.
        // Recorre e imprime todos los atributos del elemento.
        for (int i = 0; i < atributos.getLength(); i++) {
            ps.print(" @" + atributos.getLocalName(i) + "[" + atributos.getValue(i) + "]");
        }
        ps.println(">"); // Cierra la etiqueta de apertura.
    }

    /**
     * Método invocado al encontrar una etiqueta de fin de elemento.
     * Simplemente decrementa el nivel de indentación para el siguiente elemento.
     * @param uri El Namespace URI.
     * @param nombreLocal El nombre local (sin prefijo).
     * @param nombreCualif El nombre cualificado (con prefijo).
     */
    @Override
    public void endElement(String uri, String nombreLocal, String nombreCualif) {
        nivel--; // Disminuye el nivel de profundidad.
    }

    /**
     * Método invocado al encontrar contenido de texto dentro de un elemento.
     * Ignora los espacios en blanco y saltos de línea. Si hay texto real,
     * lo indenta y lo imprime precedido por "#text".
     * @param cars Array de caracteres con el texto.
     * @param inicio Posición de inicio del texto en el array.
     * @param longitud Número de caracteres a leer desde el inicio.
     */
    @Override
    public void characters(char[] cars, int inicio, int longitud) {
        String texto = new String(cars, inicio, longitud);
        // Si el texto recortado (sin espacios al principio/final) está vacío, no hace nada.
        // Esto evita imprimir nodos de texto que solo contienen saltos de línea o espacios de formato.
        if (texto.trim().length() == 0) {
            return;
        }
        nivel++; // Incrementa el nivel para indentar el texto.
        indenta(); // Aplica la indentación.
        nivel--; // Lo restaura inmediatamente para el siguiente elemento.
        ps.println("#text[" + texto + "]"); // Imprime el contenido del texto.
    }
}

/**
 * Clase principal que inicia el proceso de parsing de un fichero XML usando SAX.
 */
public class SAXParserSample {

    /**
     * Método principal de la aplicación.
     * @param args Argumentos de la línea de comandos. Se espera el nombre del fichero XML como primer argumento.
     */
    public static void main(String[] args) {
        String nomFich;
        // Comprueba si se ha proporcionado un nombre de fichero como argumento.
        if (args.length < 1) {
            System.out.println("Indicar por favor nombre de fichero");
            return; // Termina la ejecución si no hay argumento.
        } else {
            nomFich = args[0]; // Asigna el primer argumento a la variable nomFich.
        }
        try {
            // Crea una instancia del parser SAX (XMLReader) usando la factoría.
            XMLReader parserSAX = XMLReaderFactory.createXMLReader();
            // Crea una instancia de nuestro manejador de eventos personalizado.
            dom_parser.GestorEventos gestorEventos = new dom_parser.GestorEventos(System.out);
            // Establece nuestro gestor como el manejador de contenido para el parser.
            // A partir de aquí, el parser notificará a 'gestorEventos' de todos los eventos de parsing.
            parserSAX.setContentHandler(gestorEventos);
            // Inicia el análisis del fichero XML. El parser leerá el fichero y llamará
            // a los métodos correspondientes en 'gestorEventos' secuencialmente.
            parserSAX.parse(nomFich);
        } catch (SAXException e) {
            // Captura errores específicos del proceso de parsing SAX.
            System.err.println(e.getMessage());
        } catch (Exception e) {
            // Captura cualquier otra excepción (ej. fichero no encontrado).
            e.printStackTrace();
        }
    }
}
