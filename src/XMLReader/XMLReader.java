package XMLReader;

import Diagram.DisplayedDiagram.*;
import Diagram.PrototypeDiagram.Diagram;
import Diagram.PseudoDiagram.PseudoDiagramAccessor;
import DiagramStyle.DiagramStyle;
import org.w3c.dom.*;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

@Deprecated
public class XMLReader {
    public static void main(String[] args) throws Exception {
        toDiagramAndSave("src/testTable.xml");
    }

    public static byte[] newBytes(int length) {
        return new byte[length];
    }

    public static byte[] toDiagram(Document document) throws Exception {
        Element root = document.getDocumentElement();
        Diagram diagram = buildDiagram(root, null);

        BufferedImage image = diagram.generateImage();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", bos);
        return bos.toByteArray();
    }

    public static void toDiagramAndSave(String filename) throws Exception {
        DocumentBuilder builder = newBuilder();

        File file = new File(filename);
        Document document = builder.parse(file);

        Element root = document.getDocumentElement();
        Diagram diagram = buildDiagram(root, null);

        BufferedImage image = diagram.generateImage();
        ImageIO.write(image, "PNG", new File("result.png"));
    }

    public static DocumentBuilder newBuilder() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        return  factory.newDocumentBuilder();
    }

    public static byte[] toDiagram(String filename) throws Exception {
        DocumentBuilder builder = newBuilder();

        File file = new File(filename);
        Document document = builder.parse(file);

        return toDiagram(document);
    }

    public static byte[] toDiagram(byte[] byteArray) throws Exception {
        DocumentBuilder builder = newBuilder();
        Document document = builder.parse(new ByteArrayInputStream(byteArray));

        return toDiagram(document);
    }

    public static byte[] toDiagram(InputStream in) throws Exception {
        DocumentBuilder builder = newBuilder();
        Document document = builder.parse(in);

        return toDiagram(document);
    }

    private static HashMap<String, Diagram> idDiagram;

    private static DiagramStyle toStyle(Element element) {
        return new DiagramStyle(element.getAttributes());
    }

    public static Diagram buildDiagram(Element root, Diagram parent) {
        if (parent == null) idDiagram = new HashMap<>();
        Diagram result = null;
        if (root.getTagName().equals("cell")) {
            result = cellDiagram(root, parent);
        }
        if (root.getTagName().equals("array")) {
            result = arrayDiagram(root, parent);
        }
        if (root.getTagName().equals("table")) {
            result = tableDiagram(root, parent);
        }
        if (root.getTagName().equals("pane")) {
            result = paneDiagram(root, parent);
        }
        if (root.getTagName().equals("arrow")) {
            result = arrowDiagram(root, parent);
        }
        if (result == null) throw new IllegalArgumentException("Not a valid tag name.");
        if (!root.getAttribute("id").isEmpty()) {
            idDiagram.put(root.getAttribute("id"), result);
        }
        return result;
    }

    private static Diagram cellDiagram(Element cell, Diagram parent) {
        Node node = cell.getFirstChild();
        Text text = (Text) node;
        return new CellDiagram(text.getData().trim(), parent,toStyle(cell));
    }

    private static Diagram arrayDiagram(Element array, Diagram parent) {
        ArrayDiagram arrayDiagram = new ArrayDiagram(parent, toStyle(array));
        NodeList nodes = array.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                Element child = (Element) node;
                arrayDiagram.push(buildDiagram(child, arrayDiagram));
            }
        }
        return arrayDiagram;
    }

    private static Diagram tableDiagram(Element table, Diagram parent) {
        TableDiagram tableDiagram = new TableDiagram(parent, toStyle(table));
        NodeList nodes = table.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                pushRow((Element) node, tableDiagram);
            }
        }
        return tableDiagram;
    }

    private static void pushRow(Element row, TableDiagram tableDiagram) {;
        NodeList cells = row.getChildNodes();
        for (int i = 0; i < cells.getLength(); i++) {
            Node cellNode = cells.item(i);
            if (cellNode instanceof Element) {
                Element cell = (Element) cellNode;
                tableDiagram.push(buildDiagram(cell, tableDiagram));
            }
        }
        tableDiagram.pushRow();
    }

    private static Diagram paneDiagram(Element pane, Diagram parent) {
        PaneDiagram paneDiagram = new PaneDiagram(parent, toStyle(pane));
        NodeList nodes = pane.getChildNodes();
        boolean baseSet = false;
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node instanceof Element) {
                Element child = (Element) node;
                if (!baseSet) {
                    paneDiagram.setBase(buildDiagram(child, paneDiagram));
                    baseSet = true;
                    continue;
                }
                paneDiagram.pushChild(buildDiagram(child, paneDiagram));
            }
        }
        return paneDiagram;
    }

    private static PseudoDiagramAccessor getPseudoDiagramAccessor(String id) {
        String[] strings = id.split("::");
        String diagramId =strings[0];
        String pseudoName = strings[1];
        Diagram diagram = idDiagram.get(diagramId);
        return new PseudoDiagramAccessor(diagram, pseudoName);
    }

    private static Diagram arrowDiagram(Element arrow, Diagram parent) {
        ArrowDiagram arrowDiagram = new ArrowDiagram(parent, toStyle(arrow));
        arrowDiagram.setFrom(getPseudoDiagramAccessor(arrow.getAttribute("from")));
        arrowDiagram.setTo(getPseudoDiagramAccessor(arrow.getAttribute("to")));
        return arrowDiagram;
    }
}
