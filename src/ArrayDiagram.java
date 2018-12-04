/* package van.de.la.sehen.diagram.displayeddiagram;

import van.de.la.sehen.diagram.prototypediagram.Diagram;
import van.de.la.sehen.diagram.prototypediagram.MatrixDiagram;
import van.de.la.sehen.diagramimage.PortableDiagramCanvas;
import van.de.la.sehen.diagramstyle.DiagramStyle;
import math.geom2d.Vector2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/* 仅仅边框本身是被绘制的 */
/*
@Deprecated
public class ArrayDiagram extends MatrixDiagram {
    public ArrayDiagram(Diagram parent, DiagramStyle style) {
        super(parent, style);
    }

    private List<Diagram> array = new ArrayList<>();

    public void push(Diagram diagram) {
        array.add(diagram);
    }

    @Override
    public Diagram getChildByIndex(int i) {
        return array.get(i);
    }

    @Override
    public void paintDiagram(PortableDiagramCanvas canvas) {
        int lineThickness = getLineThickness();
        Color lineColor = getLineColor();
        drawVertical(canvas, 0, lineThickness, lineColor);
        int accumulatedX = toGlobalX(lineThickness);
        for (Diagram child: array) {
            child.paintDiagram(canvas);
            accumulatedX += child.getWidth();
            drawVertical(canvas, accumulatedX, lineThickness, lineColor);
            accumulatedX += lineThickness;
        }
        drawHorizontal(canvas, 0, lineThickness, lineColor);
        drawHorizontal(canvas, (int)getHeight() - lineThickness, lineThickness, lineColor);
    }

    @Override
    public void calculateSize() {
        double width = getLineThickness();
        double height = -1;
        for (Diagram child: array) {
            if (child.getHeight() > height)
                height = child.getHeight();
            width += child.getWidth() + getLineThickness();
        }
        setWidth(width);
        setHeight(height + 2 * getLineThickness());
    }

    @Override
    public void layoutChildren() {
        for (Diagram diagram: array) {
            diagram.layout();
        }
    }

    @Override
    public void setChildrenPosition() {
        int lineThickness = getLineThickness();
        int accumulatedX = lineThickness;
        for (Diagram child: array) {
            child.setPosition(new Vector2D(accumulatedX, lineThickness));
            accumulatedX += child.getWidth() + lineThickness;
        }
    }
} */
