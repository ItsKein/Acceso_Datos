package dom_parser;

/*
El siguiente programa crea un documento DOM con parte de los contenidos del documento de XML de ejemplo,
y después lo serializa en un string mediante un StringWriter.
Se indica codificación UTF-8 para la serialización
 */
// Creación de árbol DOM añadiendo elementos y serialización una vez creado

import java.io.StringWriter;

// Importaciones para el manejo del modelo DOM (Document Object Model) de W3C
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// Importaciones para JAXP (Java API for XML Processing) para crear un constructor de documentos
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

// Importaciones para la API de Transformación de Java (TrAX) para serializar el DOM a XML
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.stream.StreamResult;

public class CrearDocumentoDOM {

    public static void main(String[] args) {

        try {
            // --- 1. Creación de un documento DOM vacío ---
            // Se obtiene una factoría de constructores de documentos para poder crear un analizador (parser)
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // Se crea un constructor de documentos a partir de la factoría
            DocumentBuilder db = dbf.newDocumentBuilder();
            // Se crea un documento DOM en memoria, que representa la estructura del XML
            Document doc = db.newDocument();
            // Se establece la versión de XML del documento
            doc.setXmlVersion("1.0");

            // --- 2. Construcción del árbol DOM ---
            // Se crea el elemento raíz del documento: <clientes>
            Element elClientes = doc.createElement("clientes");
            // Se añade el elemento raíz al documento
            doc.appendChild(elClientes);

            // Se crea un elemento <cliente>
            Element elCliente = doc.createElement("cliente");
            // Se le añade un atributo DNI al elemento <cliente>
            elCliente.setAttribute("DNI", "89012345E");

            // Se crea el elemento <apellidos>
            Element elApell = doc.createElement("apellidos");
            // Se crea un nodo de texto con el contenido "ROJAS"
            // y se añade como hijo del elemento <apellidos>
            elApell.appendChild(doc.createTextNode("ROJAS"));
            // Se añade el elemento <apellidos> como hijo de <cliente>
            elCliente.appendChild(elApell);

            // Se crea el elemento <validez>
            Element elValidez = doc.createElement("validez");
            // Se le añaden dos atributos: estado y timestamp
            elValidez.setAttribute("estado", "borrado");
            elValidez.setAttribute("timestamp", "1528286082");
            // Se añade el elemento <validez> como hijo de <cliente>
            elCliente.appendChild(elValidez);

            // Finalmente, se añade el elemento <cliente> (con todos sus hijos) al elemento raíz <clientes>
            elClientes.appendChild(elCliente);

            // --- 3. Serialización del DOM a una cadena de texto (XML) ---
            // Se encapsula el documento DOM en un objeto DOMSource para que pueda ser procesado
            DOMSource domSource = new DOMSource(doc);
            // Se obtiene una instancia de TransformerFactory para crear un objeto Transformer
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            // --- 4. Configuración de las propiedades de salida del XML ---
            // Se especifica que el método de salida es XML
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            // Se establece la codificación de caracteres a UTF-8
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            // Se indica que la salida debe ser indentada (formateada para ser legible)
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            // Se establece el número de espacios para la indentación
            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "4");

            // Se crea un StringWriter para escribir el resultado de la transformación en una cadena
            StringWriter sw = new StringWriter();
            // Se crea un StreamResult que apunta al StringWriter, indicando el destino de la salida
            StreamResult sr = new StreamResult(sw);
            // Se realiza la transformación: el DOM (domSource) se convierte en texto XML y se escribe en el StreamResult (sr)
            transformer.transform(domSource, sr);

            // Se imprime la cadena XML generada en la consola
            System.out.println(sw.toString());

        } catch (ParserConfigurationException e) {
            // Captura de excepción si ocurre un error en la configuración del parser
            System.err.println("Error de configuración del parser: " + e.getMessage());
        } catch (Exception e) {
            // Captura de cualquier otra excepción durante el proceso (ej. en la transformación)
            e.printStackTrace();
        }
    }
}