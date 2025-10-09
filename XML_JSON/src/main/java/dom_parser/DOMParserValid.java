package dom_parser;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.io.PrintStream;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.SchemaFactory;
import javax.xml.XMLConstants;

/**
 * Clase principal que demuestra el uso de un analizador DOM para parsear
 * y validar un documento XML contra un DTD o un esquema XSD.
 * El programa recibe el nombre del fichero XML como argumento y, opcionalmente,
 * un segundo argumento con el fichero de esquema (.xsd).
 * Si no se proporciona un esquema, intentará validar con el DTD especificado en el XML.
 */
public class DOMParserValid {

    // Constante para la indentación en la salida por consola, facilitando la visualización de la estructura del árbol DOM.
    static final String INDENT_NIVEL = "  ";

    /**
     * Método recursivo que recorre el árbol DOM y muestra información de cada nodo.
     * @param nodo El nodo actual a procesar.
     * @param nivel El nivel de profundidad en el árbol para una correcta indentación.
     * @param ps El stream de salida donde se imprimirá la información (p.ej. System.out).
     */
    public static void muestraNodo(Node nodo, int nivel, PrintStream ps) {

        // Imprime la indentación correspondiente al nivel actual del nodo en el árbol.
        for (int i = 0; i < nivel; i++) {
            ps.print(INDENT_NIVEL);
        }

        // Evalúa el tipo de nodo para mostrar la información adecuada.
        switch (nodo.getNodeType()) {
            case Node.DOCUMENT_NODE:    // Si es la raíz del documento.
                ps.println("DOCUMENTO");
                break;
            case Node.ELEMENT_NODE:    // Si es una etiqueta o elemento XML.
                ps.println("ELEMENTO(" + nodo.getNodeName() + ")");
                break;
            case Node.TEXT_NODE:   // Si es un nodo de texto (contenido de un elemento).
                // Se imprime el valor del nodo, que es el texto que contiene.
                ps.println(nodo.getNodeName() + "[" + nodo.getNodeValue() + "]");
                break;
        }
        // Obtiene la lista de nodos hijos del nodo actual.
        NodeList nodosHijos = nodo.getChildNodes();
        // Bucle para llamar recursivamente a la función con cada uno de los nodos hijos.
        for (int i = 0; i < nodosHijos.getLength(); i++) {
            muestraNodo(nodosHijos.item(i), nivel + 1, ps);
        }
    }

    /**
     * Método principal de la aplicación.
     * @param args Argumentos de la línea de comandos. Se espera el nombre del fichero XML
     * y opcionalmente el nombre del fichero de esquema XSD.
     */
    public static void main(String[] args) {

        File f = null, fEsq = null; // Objetos File para el XML y el esquema.

        // Comprobación de los argumentos de entrada.
        if (args.length < 1) {
            System.out.println("Indicar por favor nombre de fichero");
            return;
        } else {
            String nomFich = args[0];
            f = new File(nomFich);
            if (!f.isFile()) {
                System.err.println("Fichero " + nomFich + " no existe.");
                return;
            }
            // Si se proporciona un segundo argumento, se asume que es el esquema.
            if (args.length >= 2) {
                String nomFichEsquema = args[1];
                fEsq = new File(nomFichEsquema);
                if (!fEsq.isFile()) {
                    System.err.println("Fichero " + nomFichEsquema + " no existe.");
                    return;
                }
            }
        }

        // Creación de la factoría de constructores de documentos DOM.
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // Ignora los comentarios en el XML.
        dbf.setIgnoringComments(true);
        // Ignora los espacios en blanco irrelevantes entre elementos.
        dbf.setIgnoringElementContentWhitespace(true);

        System.out.println("Fichero XML: " + f.getAbsolutePath());
        if (fEsq != null) {    // Si se proporcionó un fichero de esquema, se configura la validación con él.
            System.out.println("Validación con esquema: " + fEsq.getAbsolutePath());
            try {
                // Asocia el esquema a la factoría para que los parsers que cree validen con él.
                dbf.setSchema(SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(fEsq));
            } catch (SAXException e) {
                System.err.println(e.getMessage());
                return;
            }
        } else {    // Si no hay esquema, se activa la validación con DTD.
            System.out.println("Validación con DTD");
            dbf.setValidating(true);
        }

        try {
            // Se crea un constructor de documentos a partir de la factoría configurada.
            DocumentBuilder db = dbf.newDocumentBuilder();
            // Se establece un manejador de errores personalizado para capturar eventos de validación.
            db.setErrorHandler(new dom_parser.GestorEventos2());

            // Se parsea el fichero XML y se obtiene el objeto Document, que es la raíz del árbol DOM.
            Document domDoc = db.parse(f);

            // Se inicia el recorrido y la muestra del árbol DOM.
            muestraNodo(domDoc, 0, System.out);
        } catch (ParserConfigurationException e) {
            System.err.println("Error de configuración del parser: " + e.getMessage());
        } catch (SAXParseException e) {
            // Esta excepción se captura cuando el GestorEventos la relanza.
            // Indica un error de validación (DTD o Esquema).
            System.err.println("Error de parsing SAX (validación) en línea " + e.getLineNumber() + ": " + e.getMessage());
        } catch (Exception e) {
            // Captura de otras posibles excepciones durante el proceso.
            e.printStackTrace();
        }

    }

}

/**
 * Clase que gestiona los eventos de error, warning y error fatal que pueden
 * ocurrir durante el proceso de parsing y validación del XML.
 * Extiende DefaultHandler para sobreescribir solo los métodos necesarios.
 */
class GestorEventos2 extends DefaultHandler {

    /**
     * Se invoca cuando el parser encuentra un error recuperable.
     * Por ejemplo, una violación de las reglas de validación del DTD o Esquema.
     */
    @Override
    public void error(SAXParseException e) throws SAXParseException {
        System.err.println("Error recuperable: " + e.toString());
        // Relanza la excepción para que pueda ser capturada en el bloque try-catch del main.
        throw (e);
    }

    /**
     * Se invoca cuando el parser encuentra un error no recuperable (fatal).
     * Generalmente, se trata de un error de "well-formedness" (XML mal formado).
     * El parsing se detiene.
     */
    @Override
    public void fatalError(SAXParseException e) throws SAXParseException {
        System.err.println("Error no recuperable: " + e.toString());
        throw (e);
    }

    /**
     * Se invoca para notificar avisos del parser.
     * Suelen ser problemas que no violan las reglas del XML pero que podrían ser incorrectos.
     */
    @Override
    public void warning(SAXParseException e) throws SAXParseException {
        System.err.println("Aviso: " + e.toString());
        throw (e);
    }

}
