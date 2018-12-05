package testsrc;

import van.de.la.sehen.warning.WarningStream;
import van.de.la.sehen.warning.WarningType;

public class TestDriver {
    public static void main(String[] args) {
        WarningStream.putWarning("Start testing ascii diagrams.", TestDriver.class, WarningType.Note);
        ASCIIDiagramTest.test();
        WarningStream.putWarning("Start testing diagrams.", TestDriver.class, WarningType.Note);
        DiagramTest.test();
    }
}
