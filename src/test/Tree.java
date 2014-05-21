package test;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

public class Tree extends JPanel {

    // current selection
    Node selection = null;
    
    // node being edited
    NodeEditor editor = null;
    
    // feedback for link dragging
    Point linkEnd = null;
    Node linkTarget = null;
    
    // font size radio button group
    JComboBox fontSizeCombo;

    // main program
    public static void main(String[] args) {
	Tree tree = new Tree ();
	JFrame frame = tree.makeFrame ();
	frame.show ();
    }
    
    // Tree is a panel
    public Tree () {
        // no layout manager
        setLayout (null);
        
        // mouse listeners
        addMouseListener (mouseListener);
        addMouseMotionListener (mouseListener);
        
        // set size
        setPreferredSize (new Dimension (1000, 1000));

        // clear the selection, so that buttons and menu items get disabled
	setSelection (null);

        // add a fake tree
        addDebugData ();
    }

    // make a frame around the tree panel
    public JFrame makeFrame () {
	JFrame f = new JFrame ("Tree Editor");
		
	// exit on close
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
	// put a scroller around tree
	JScrollPane scroller = new JScrollPane (this);
	scroller.setPreferredSize (new Dimension (400, 300));
      
	// make menu bar
	JMenuBar menubar = new JMenuBar ();
	       
	JMenu fileMenu = new JMenu ("File");
	fileMenu.add (exitAction);
	menubar.add (fileMenu);

	JMenu editMenu = new JMenu ("Edit");
	editMenu.add (deleteAction);
	menubar.add (editMenu);
	        
	f.setJMenuBar (menubar);

	// make toolbar
	Box toolbar = new Box (BoxLayout.X_AXIS);

	// make delete button		
	JButton deleteButton = new JButton (deleteAction);
	deleteButton.setText ("");
	deleteButton.setIcon (new ImageIcon ("delete.gif"));
	toolbar.add (deleteButton);

	// make font size combo box		
	toolbar.add (new JLabel ("         Font size:  "));
			
	fontSizeCombo = new JComboBox (FONT_SIZES);
	fontSizeCombo.setSelectedItem (DEFAULT_FONT_SIZE);
	fontSizeCombo.addActionListener (fontSizeAction);
	fontSizeCombo.setEditable (false);
	fontSizeCombo.setPreferredSize (new Dimension (40, 0));
	toolbar.add (fontSizeCombo);
		
	toolbar.add (Box.createGlue());
	toolbar.setBorder (BorderFactory.createEmptyBorder (2, 2, 2, 2));

	// lay out window
	Container contentPane = f.getContentPane ();
	contentPane.setLayout (new BorderLayout ());
	contentPane.add (toolbar, BorderLayout.NORTH);
	contentPane.add (scroller, BorderLayout.CENTER);
	f.pack ();

	return f;		
    }
	
    // make a simple tree for debugging
    private void addDebugData () {
        Node chuckVest = makeNode ("Charles Vest", new Point (70, 20));
        
        Node bobBrown = makeNode ("Robert Brown", new Point (25, 60));
        bobBrown.setParent (chuckVest);
        
        Node philClay = makeNode ("Philip Clay", new Point (120, 60));
        philClay.setParent (chuckVest);
    }


    // Tree nodes are components, extending JLabels.
    // (Tree links are strokes drawn by paintComponent().)
    public class Node extends JLabel {
        Node parent = null;
        Set children = new HashSet ();
        // invariants:
	//   if parent != null, parent.children contains this
	//   c in children if and only if c.parent = this
	// Preserved by setParent().

        public Node (String text, Point location) {
            super (text);
            setLocation (location);
            setSize (getPreferredSize ());
        }

        @Override
		public void setText (String text) {
            super.setText (text);
            setSize (getPreferredSize ());
        }
        
        public void setFontSize (int size) {
	    Font f = getFont ();
	    setFont (new Font (f.getName(), f.getStyle (), size));
	    setSize (getPreferredSize ());
	    Tree.this.repaint ();
        }
        
        public int getFontSize () {
	    return getFont ().getSize ();
        }
        
        public void setParent (Node newParent) {
            if (parent != null)
                parent.children.remove (this);
            this.parent = newParent;
            if (newParent != null)
                newParent.children.add (this);
        }
        
	// unlinks this node from both parents and children,
	// in preparation for deleting it
        public void removeAllLinks () {
            setParent (null);

	    // change children to a different set,
	    // so that it's safe to iterate even though
	    // children's setParent() will try to mutate it
            Set oldChildren = children;
            children = new HashSet ();
            for (Iterator g = oldChildren.iterator (); g.hasNext (); )
                ((Node) g.next ()).setParent (null);
        }

        // paint node, including possibly selection handles and link hotspot
        @Override
		public void paint (Graphics g) {
            super.paint (g);

            if (selection == this || linkTarget == this) {
                Dimension d = getSize ();
            	
            	// draw selection handles for selected node
            	// and for link target
                g.setColor (Color.black);           
                g.fillRect(0, 0, 4, 4);
                g.fillRect(d.width - 4, 0, 4, 4);
                g.fillRect(0, d.height - 4, 4, 4);
                g.fillRect(d.width - 4, d.height - 4, 4, 4);
                
                if (selection == this) {
		    // draw link hotspot only for selected node
                    g.setColor (Color.red);
                    g.drawArc(d.width/2 - 4, 0, 8, 8, 0, 360);
                }
            }
        }

        // true if pt (in Tree coordinate system) falls in this node's
	// link hotspot
        public boolean hitsLinkHotspot (Point pt) {
            Rectangle r = getBounds ();
            int x = pt.x - r.x;
            int y = pt.y - r.y;
            x -= (r.width/2 - 4);
            return (-2 < x && x < 10 && -2 < y && y < 10);
        }
        
	// paint this node's link to its parent
	// g is a Graphics for the Tree, not just for this Node.
        public void paintLink (Graphics g) {
            Rectangle myRect = getBounds ();           
            int x1 = myRect.x + myRect.width/2;
            int y1 = myRect.y + 4;

            int x2;
            int y2;
            if (parent != null) {
                Rectangle parentRect = parent.getBounds ();
                x2 = parentRect.x + parentRect.width/2;
                y2 = parentRect.y + parentRect.height;
            } else if (selection == this && linkEnd != null) {
                x2 = linkEnd.x;
                y2 = linkEnd.y;
            } else {
                return;
            } 
            
            g.drawLine (x1, y1, x2, y2);
        }

    } // end of Node class


    // Resuming Tree methods.

    // paint links and nodes
    @Override
	public void paintChildren (Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        
        int n = getComponentCount ();
        for (int i = 0; i < n; ++i) {
            Component c = getComponent (i);
            if (c instanceof Node)
                ((Node) c).paintLink (g2);
        }

        super.paintChildren (g);        
    }

    // make a new node
    public Node makeNode (String text, Point pt) {
        Node n = new Node (text, pt);
        add (n);
        return n;
    }
        
    // remove a node
    public void removeNode (Node node) {
	remove (node);
	node.removeAllLinks ();
	repaint ();
    }
    

    // edit in place
    public void startEditing (Node node) {
        editor = new NodeEditor (node);
        add (editor);        
        editor.grabFocus ();
    }

    public void stopEditing () {
        if (editor == null)
            return;

	editor.stop ();
        remove (editor);
        editor = null;
        repaint ();
    }

    // NodeEditor is a standard text box. 
    public class NodeEditor extends JTextField {
    	Node node;
    	
    	public NodeEditor (Node node) {
	    super (node.getText ());
    		
	    this.node = node;
    		
	    setFont (node.getFont ());
	    addKeyListener (new KeyAdapter () {
		    @Override
			public void keyTyped(KeyEvent e) {
			switch (e.getKeyChar ()) {
			case '\n':
			case 0x1B:
			    stopEditing ();
			    break;
			default:
			    break;
			}
		    }
    		});
	    selectAll ();

	    Dimension d = getPreferredSize ();
	    d.width += 50;
	    setSize (d);

	    setLocation (node.getLocation ());
    	}
    	
    	public void stop () {
	    if (getText ().length () == 0)
		removeNode (node);
	    else
		node.setText (editor.getText ());
    	}
    }
    
    // Mouse listener
    // for selecting nodes, moving nodes, and dragging out links
    private MouseInputListener mouseListener = new MouseInputAdapter () {
	    boolean dragging = false;
	    int offsetX;
	    int offsetY;
        
	    @Override
		public void mousePressed(MouseEvent e) {
		Component c = getComponentAt (e.getPoint());
		if (c instanceof Node)
		    setSelection ((Node) c);
		else if (selection == null) {
		    Node n = makeNode ("", e.getPoint ());
		    setSelection (n);
		    startEditing (n);
		} else {
		    setSelection (null);
		}
	    }
        
	    @Override
		public void mouseClicked (MouseEvent e) {
		if (selection != null && e.getClickCount () > 1) {
		    startEditing (selection);
		}
	    }
        
	    @Override
		public void mouseDragged(MouseEvent e) {
		if (selection == null)
		    return;
             
		if (dragging) {
		    selection.setLocation (e.getX () + offsetX, 
					   e.getY () + offsetY);
		    repaint ();
		}
		else if (linkEnd != null) {
		    Component c = getComponentAt (e.getPoint());
		    if (c instanceof Node && c != selection)
			linkTarget = (Node) c;
		    else
			linkTarget = null;

		    linkEnd = e.getPoint ();
		    repaint ();
		}
		else if (selection.hitsLinkHotspot (e.getPoint ())) {
		    selection.setParent (null);
		    linkEnd = e.getPoint ();
		    linkTarget = null;
		    repaint ();
		}
		else {
		    dragging = true;
		    offsetX = selection.getX () - e.getX ();
		    offsetY = selection.getY () - e.getY ();
		}
	    }

	    @Override
		public void mouseReleased (MouseEvent e) {
		if (dragging)
		    dragging = false;
		else if (linkEnd != null) {
		    if (linkTarget != null)
			selection.setParent (linkTarget);
		    linkEnd = null;
		    linkTarget = null;
		    repaint ();
		}
	    }
	};


    // select a node
    public void setSelection (Node n) {
        stopEditing ();
        
        if (selection != null)
            selection.repaint ();
            
        selection = n;
        
        if (selection != null)
            selection.repaint ();

	deleteAction.setEnabled (selection != null);

	if (selection != null) {
	    Integer size = new Integer (selection.getFontSize ());
	    for (int i = 0; i < FONT_SIZES.length; ++i)
		if (size.equals (FONT_SIZES[i]))
		    fontSizeCombo.setSelectedItem (FONT_SIZES[i]);
	}
    }


    // exit program
    private Action exitAction = new AbstractAction ("Exit") {
	    @Override
		public void actionPerformed(ActionEvent e) {
		System.exit (0);
	    }
	};   
    {
	exitAction.putValue (Action.ACCELERATOR_KEY, 
			     KeyStroke.getKeyStroke(KeyEvent.VK_Q, 
						    InputEvent.CTRL_MASK));
    }
    
    // delete a node
    private Action deleteAction = new AbstractAction ("Delete") {
	    @Override
		public void actionPerformed(ActionEvent e) {
		if (selection == null) {
		    // shouldn't happen because action should be disabled,
		    // but reject it anyway 
		    getToolkit ().beep ();
		    return;
		}

		removeNode (selection);
		setSelection (null);
	    }
	};

    {
        deleteAction.putValue (Action.ACCELERATOR_KEY, 
			       KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
    }

    // change font size
    private ActionListener fontSizeAction = new ActionListener () {
	    @Override
		public void actionPerformed(ActionEvent e) {
		if (selection != null) {
		    int size = 
			((Integer) fontSizeCombo.getSelectedItem ())
			.intValue ();
		    selection.setFontSize (size);
		}
	    }
	};
	
    private static Integer FONT_SIZES[] = { 
	new Integer (8), 
	new Integer (12), 
	new Integer (18), 
	new Integer (30)
    };
	
    private static Integer DEFAULT_FONT_SIZE = FONT_SIZES[1];

}
