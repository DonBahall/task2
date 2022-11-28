import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class task1 {

    public static void main(String[] args) {
        String filePath = "task2\\file.xml";
        File xmlFile = new File(filePath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            updateElementValue(doc);

            doc.getDocumentElement().normalize();

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(new File("new.xml"));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private static void updateElementValue(Document doc) {
        NodeList nodeComponent = doc.getElementsByTagName("person");
        Element lang;

        for (int i = 0; i < nodeComponent.getLength(); i++) {
            lang = (Element) nodeComponent.item(i);
            Node name = lang.getAttributes().item(1).getFirstChild();
            name.setNodeValue(name.getNodeValue() + " " + lang.getAttributes().item(2).getFirstChild().getNodeValue());
            Element element = (Element) nodeComponent.item(i);
            element.removeAttribute("surname");
        }
    }
}
