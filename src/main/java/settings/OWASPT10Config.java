package settings;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

public class OWASPT10Config {

    String parentTag = "owasp-t10";
    String typeTag = "type";
    String idTag = "id";
    String nameTag = "name";
    String descriptionTag = "description";

    String fileName = "OWASPT10.xml";

    public OWASPT10Config() {
    }

    public void createConfigFile(ArrayList<String[]> OWASPT10Types) throws ParserConfigurationException, TransformerException {

        ConfigXMLFileCreator configXMLFileCreator = new ConfigXMLFileCreator();

        configXMLFileCreator.createFile();

        configXMLFileCreator.createParentElement(parentTag);

        for (String[] OWASPType : OWASPT10Types) {

            Element typeElement = configXMLFileCreator.createChildElement(typeTag);

            Element type_idElement = configXMLFileCreator.createChildElement(idTag, OWASPType[0]);
            Element type_nameElement = configXMLFileCreator.createChildElement(nameTag, OWASPType[1]);
            Element type_descriptionElement = configXMLFileCreator.createChildElement(descriptionTag, OWASPType[2]);

            typeElement.appendChild(type_idElement);
            typeElement.appendChild(type_nameElement);
            typeElement.appendChild(type_descriptionElement);

            configXMLFileCreator.addToParent(typeElement);
        }
        configXMLFileCreator.transformAndSaveFile(fileName);
    }

    public ArrayList<String[]> loadConfigFile() throws ParserConfigurationException, IOException, SAXException {

        ArrayList<String[]> OWASP_T10_list = new ArrayList<String[]>();
        //Document document = null;

        ConfigXMLFileReader configXMLFileReader = new ConfigXMLFileReader();
        configXMLFileReader.loadFile(fileName);

        //NodeList list = document.getElementsByTagName(typeTag);

        NodeList nodeList = configXMLFileReader.loadNodesByTagName(typeTag);

        /*String[] row;
        Node node;
        Element element;*/

        for (int i = 0; i < nodeList.getLength(); i++) {

            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element) node;
                String[] row = new String[3];

                row[0] = element.getElementsByTagName(idTag).item(0).getTextContent();
                row[1] = element.getElementsByTagName(nameTag).item(0).getTextContent();
                row[2] = element.getElementsByTagName(descriptionTag).item(0).getTextContent();

                OWASP_T10_list.add(row);
            }
        }
        return OWASP_T10_list;
    }
}