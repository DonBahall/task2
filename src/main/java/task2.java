import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;


public class task2 {
    static Map<String, Double> map = new HashMap<>();

    public static void main(String[] args) {
        File folder = new File("task2\\violation");
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        for (File file : listOfFiles) {
            if (file.isFile()) {
                readFile(new File(file.getName()));
            }
        }
        createXml();
    }

    private static void readFile(File newFile) {
        JSONParser jsonParser = new JSONParser();
        try {
            Object obj = jsonParser.parse(new FileReader("violation\\" + newFile));
            JSONArray jsonArray = (JSONArray) obj;
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                String name = (String) jsonObject.get("type");
                double sum = (double) jsonObject.get("fine_amount");
                if (map.containsKey(name)) {
                    map.put(name, map.get(name) + sum);
                } else {
                    map.put(name, sum);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createXml() {
        sortMap();
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            String[] viol = map.keySet().toArray(new String[0]);
            List<Double> list = map.values().stream().toList();

            Element rootElement = doc.createElement("Violations");
            doc.appendChild(rootElement);
            for (int i = 0; i < map.size(); i++) {
                Element base = doc.createElement("Violation");
                base.appendChild(doc.createTextNode(viol[i]));
                rootElement.appendChild(base);
                Element price = doc.createElement("Price");
                price.appendChild(doc.createTextNode(String.valueOf(list.get(i))));
                rootElement.appendChild(price);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File("result.xml"));
                transformer.transform(source, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sortMap() {
        map = map.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        System.out.println(map);
    }
}
