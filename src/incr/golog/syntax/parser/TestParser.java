package incr.golog.syntax.parser;

import incr.golog.*;
import incr.golog.syntax.*;
import incr.strips.STRIPSAction;

import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.abego.treelayout.*;
import org.abego.treelayout.util.*;

public class TestParser {
	public static void main(String[] args) throws IOException, ParseException {
		GologParser parser = new GologParser(new FileInputStream("programs/blocksworld.txt"));
		parser.parse();
		Set<STRIPSAction> actions = parser.environment.getActions();
		Set<Proc> procs = parser.environment.getProcs();
		
		System.out.println("PARSING RESULT:\n");
		{int i=0; for(STRIPSAction a : actions) {
			System.out.println("ACTION #" + ++i + ":\n" + a);
		}}
		{int i=0; for(Proc p : procs) {
			System.out.println("PROC #" + ++i + ":\n" + p);
			showAST(p);
		}}
		{
			System.out.println("PROGRAM:\n" + parser.program);
			showAST(parser.program);
		}
	}
	
	private static void showAST(AbstractEntity ast) {
		AbstractTreeForTreeLayout<AbstractEntity> layout = new MyLayout(ast);
		
        double gapBetweenLevels = 50;
        double gapBetweenNodes = 10;
        DefaultConfiguration<AbstractEntity> configuration = new DefaultConfiguration<AbstractEntity>(
                        gapBetweenLevels, gapBetweenNodes);

        JDialog dialog = new JDialog();
        Container contentPane = dialog.getContentPane();
        ((JComponent) contentPane).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        MyNodeExtentProvider nodeExtentProvider = new MyNodeExtentProvider(dialog.getContentPane().getGraphics());
        TreeLayout<AbstractEntity> treeLayout = new TreeLayout<AbstractEntity>(layout, nodeExtentProvider, configuration);

        MyTreePane panel = new MyTreePane(treeLayout);
        contentPane.add(panel);
        dialog.pack();
	}
}

class MyLayout extends AbstractTreeForTreeLayout<AbstractEntity> {
	public MyLayout(AbstractEntity root) {
		super(root);
	}

	private List<AbstractEntity> l(AbstractEntity a) {
		return Arrays.asList(a);
	}
	
	private List<AbstractEntity> l(AbstractEntity a, AbstractEntity b) {
		return Arrays.asList(a, b);
	}
	
	@Override
	public List<AbstractEntity> getChildrenList(AbstractEntity n) {
		if(n instanceof If) {
			If x = (If)n;
			return l(x.getThenBranch(), x.getElseBranch());
		} else if(n instanceof NDet) {
			NDet x = (NDet)n;
			return l(x.getP1(), x.getP2());
		} else if(n instanceof Pi) {
			Pi x = (Pi)n;
			return l(x.getP1());
		} else if(n instanceof Proc) {
			Proc x = (Proc)n;
			return l(x.getBody());
		} else if(n instanceof Sequence) {
			Sequence x = (Sequence)n;
			return l(x.getP1(), x.getP2());
		} else if(n instanceof Star) {
			Star x = (Star)n;
			return l(x.getP1());
		} else if(n instanceof While) {
			While x = (While)n;
			return l(x.getBody());
		}
		return Collections.emptyList();
	}

	@Override
	public AbstractEntity getParent(AbstractEntity n) {
		return getParent(getRoot(), n);
	}
	
	private AbstractEntity getParent(AbstractEntity root, AbstractEntity n) {
		AbstractEntity tmp;
		for(AbstractEntity n1 : getChildrenList(root)) {
			if(n1.equals(n))
				return root;
			if((tmp = getParent(n1, n)) != null)
				return tmp;
		}
		return null;
	}
}

class MyTreePane extends JComponent {
	private static final long serialVersionUID = 1L;

	private final TreeLayout<AbstractEntity> treeLayout;

    private TreeForTreeLayout<AbstractEntity> getTree() {
            return treeLayout.getTree();
    }

    private Iterable<AbstractEntity> getChildren(AbstractEntity parent) {
            return getTree().getChildren(parent);
    }

    private Rectangle2D.Double getBoundsOfNode(AbstractEntity node) {
            return treeLayout.getNodeBounds().get(node);
    }

    /**
     * Specifies the tree to be displayed by passing in a {@link TreeLayout} for
     * that tree.
     * 
     * @param treeLayout
     */
    public MyTreePane(TreeLayout<AbstractEntity> treeLayout) {
            this.treeLayout = treeLayout;

            Dimension size = treeLayout.getBounds().getBounds().getSize();
            setPreferredSize(size);
    }

    // -------------------------------------------------------------------
    // painting

    private final static int ARC_SIZE = 10;
    private final static Color BOX_COLOR = Color.orange;
    private final static Color BORDER_COLOR = Color.darkGray;
    private final static Color TEXT_COLOR = Color.black;

    private void paintEdges(Graphics g, AbstractEntity parent) {
            if(!getTree().isLeaf(parent)) {
                    Rectangle2D.Double b1 = getBoundsOfNode(parent);
                    double x1 = b1.getCenterX();
                    double y1 = b1.getCenterY();
                    for(AbstractEntity child : getChildren(parent)) {
                            Rectangle2D.Double b2 = getBoundsOfNode(child);
                            g.drawLine((int) x1, (int) y1, (int) b2.getCenterX(),
                                            (int) b2.getCenterY());

                            paintEdges(g, child);
                    }
            }
    }

    private void paintBox(Graphics g, AbstractEntity n) {
            // draw the box in the background
            g.setColor(BOX_COLOR);
            Rectangle2D.Double box = getBoundsOfNode(n);
            g.fillRoundRect((int) box.x, (int) box.y, (int) box.width - 1,
                            (int) box.height - 1, ARC_SIZE, ARC_SIZE);
            g.setColor(BORDER_COLOR);
            g.drawRoundRect((int) box.x, (int) box.y, (int) box.width - 1,
                            (int) box.height - 1, ARC_SIZE, ARC_SIZE);

            // draw the text on top of the box (possibly multiple lines)
            g.setColor(TEXT_COLOR);
            String[] lines = new String[]{Node.toString(n)};
            FontMetrics m = getFontMetrics(getFont());
            int x = (int) box.x + ARC_SIZE / 2;
            int y = (int) box.y + m.getAscent() + m.getLeading() + 1;
            for (int i = 0; i < lines.length; i++) {
                    g.drawString(lines[i], x, y);
                    y += m.getHeight();
            }
    }

    @Override
    public void paint(Graphics g) {
            super.paint(g);

            paintEdges(g, getTree().getRoot());

            // paint the boxes
            for(AbstractEntity textInBox : treeLayout.getNodeBounds().keySet()) {
                    paintBox(g, textInBox);
            }
    }
}

class Node {
	public static String toString(AbstractEntity n) {
        if(n instanceof Atomic) {
        	return ((Atomic)n).getTerm().toString();
        } else if(n instanceof If) {
        	return "if " + ((If)n).getCondition().toString();
        } else if(n instanceof NDet) {
        	return "|";
        } else if(n instanceof Pi) {
        	return "pi . " + ((Pi)n).getVar();
        } else if(n instanceof Proc) {
        	return "proc " + ((Proc)n).getHead().toString();
        } else if(n instanceof Sequence) {
        	return ":";
        } else if(n instanceof Star) {
        	return "*";
        } else if(n instanceof Test) {
        	return ((Test)n).toString();
        } else if(n instanceof While) {
        	return "while " + ((While)n).getCondition();
        } else {
        	return "???";
        }
	}
	
	public static Dimension measure(Graphics g, AbstractEntity n) {
		FontMetrics metrics = g.getFontMetrics();
		int hgt = metrics.getHeight();
		int adv = metrics.stringWidth(toString(n));
		final int padding=4;
		return new Dimension(adv+padding+10, hgt+padding);
	}
}

class MyNodeExtentProvider implements NodeExtentProvider<AbstractEntity> {
	private final Graphics g;
	
	public MyNodeExtentProvider(Graphics g) {
		this.g = g;
	}
	
	@Override
	public double getWidth(AbstractEntity treeNode) {
		if(g == null) return 0;
		return Node.measure(g, treeNode).getWidth();
	}

	@Override
	public double getHeight(AbstractEntity treeNode) {
		if(g == null) return 0;
		return Node.measure(g, treeNode).getHeight();
	}
}
