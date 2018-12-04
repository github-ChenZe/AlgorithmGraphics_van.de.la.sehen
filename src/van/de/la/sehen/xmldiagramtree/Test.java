package van.de.la.sehen.xmldiagramtree;

public class Test {
    public static void main(String[] args) {
        XMLDiagramTreeBuilder builder = new XMLDiagramTreeBuilder();
        XMLDiagramTree tree = builder.toTree("src/testTable.xml");
        System.out.println(tree.toString());
    }
}
