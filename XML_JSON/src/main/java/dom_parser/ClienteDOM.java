package dom_parser;

import java.io.File;
import java.io.FileNotFoundException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
// Añadimos solo estos tres para poder generar el XML de salida
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class ClienteDOM {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Uso: java AnadirClienteDOM <fichero.xml>");
            return;
        }

        String nomFich = args[0];

        try {
            // Crear el parser DOM
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setIgnoringComments(true);
            dbf.setIgnoringElementContentWhitespace(true);

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(nomFich));

            // Obtener el elemento raíz
            Element raiz = doc.getDocumentElement();
            if (raiz == null || !"clientes".equalsIgnoreCase(raiz.getNodeName())) {
                System.err.println("ERROR: el XML no tiene un elemento raíz <clientes>.");
                return;
            }

            // Crear nuevo elemento <cliente>
            Element nuevoCliente = doc.createElement("cliente");

            Element dni = doc.createElement("dni");
            dni.setTextContent("99999999X");
            nuevoCliente.appendChild(dni);

            Element apellidos = doc.createElement("apellidos");
            apellidos.setTextContent("García López");
            nuevoCliente.appendChild(apellidos);

            Element cp = doc.createElement("cp");
            cp.setTextContent("28080");
            nuevoCliente.appendChild(cp);

            // Insertar el nuevo cliente al principio de la lista
            raiz.insertBefore(nuevoCliente, raiz.getFirstChild());

            // Transformar el DOM de vuelta a XML y mostrarlo por consola
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(System.out));

        } catch (FileNotFoundException e) {
            System.err.println("ERROR: no existe el fichero " + nomFich);
        } catch (ParserConfigurationException e) {
            System.err.println("ERROR de configuración del parser: " + e.getMessage());
        } catch (SAXException e) {
            System.err.println("ERROR: el fichero no es un XML bien formado.");
        } catch (Exception e) {
            System.err.println("ERROR general: " + e.getMessage());
        }
    }
}
