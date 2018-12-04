package van.de.la.sehen.diagram;

import van.de.la.sehen.xmldiagramrenderer.ASCIIXMLDiagramRenderer;
import van.de.la.sehen.xmldiagramrenderer.XMLDiagramRenderer;

import java.awt.*;
import java.io.File;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> ASCIIXMLDiagramRenderer.main(null));
        XMLDiagramRenderer.main(null);
    }
}
