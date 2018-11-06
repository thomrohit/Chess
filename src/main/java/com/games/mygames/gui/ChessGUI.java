package com.games.mygames.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.games.mygames.Platform;
import com.games.mygames.chess.Box;
import com.games.mygames.chess.ChessGame;
import com.games.mygames.chess.Spot;

public class ChessGUI implements Platform {

    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JButton[][] chessBoardSquares = new JButton[8][8];
    private JPanel chessBoard;
    private final JLabel message = new JLabel(
            "Chess Champ is ready to play!");
    private static final String COLS = "ABCDEFGH";
    public HashMap <String,List<ChessImage>> mapImages = new  HashMap <String,List<ChessImage>>();
    private static ChessGame chess;
    private boolean clicked = Boolean.FALSE;
    private Spot currentSpot = new Spot(-1,-1); // initialize a default value
    private List<Spot> currentpossibilities;
    private HashMap<Box, List<Spot>> saveKing;
    private static  ChessGUI cg;
    private static  JFrame f;
    
    ChessGUI() {
        initializeGui();
    }
    
    public final void initializeGui() {
        // create the images for the chess pieces
        createImages();

        // set up the main GUI
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        Action newGameAction = new AbstractAction("New") {

            @Override
            public void actionPerformed(ActionEvent e) {
            	chess = playChess(cg,f);
                setupNewGame(chess.board());
                startGame();
            }
        };
        tools.add(newGameAction);
        tools.add(new JButton("Save")); // TODO - add functionality!
        tools.add(new JButton("Restore")); // TODO - add functionality!
        tools.addSeparator();
        tools.add(new JButton("Resign")); // TODO - add functionality!
        tools.addSeparator();
        tools.add(message);

        gui.add(new JLabel("?"), BorderLayout.LINE_START);

        chessBoard = new JPanel(new GridLayout(0, 9)) {

            /**
             * Override the preferred size to return the largest it can, in
             * a square shape.  Must (must, must) be added to a GridBagLayout
             * as the only component (it uses the parent as a guide to size)
             * with no GridBagConstaint (so it is centered).
             */
            @Override
            public final Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                Dimension prefSize = null;
                Component c = getParent();
                if (c == null) {
                    prefSize = new Dimension(
                            (int)d.getWidth(),(int)d.getHeight());
                } else if (c!=null &&
                        c.getWidth()>d.getWidth() &&
                        c.getHeight()>d.getHeight()) {
                    prefSize = c.getSize();
                } else {
                    prefSize = d;
                }
                int w = (int) prefSize.getWidth();
                int h = (int) prefSize.getHeight();
                // the smaller of the two sizes
                int s = (w>h ? h : w);
                return new Dimension(s,s);
            }
        };
        chessBoard.setBorder(new CompoundBorder(
                new EmptyBorder(8,8,8,8),
                new LineBorder(Color.BLACK)
                ));
        // Set the BG to be ochre
        Color ochre = new Color(204,119,34);
        chessBoard.setBackground(ochre);
        JPanel boardConstrain = new JPanel(new GridBagLayout());
        boardConstrain.setBackground(ochre);
        boardConstrain.add(chessBoard);
        gui.add(boardConstrain);

        // Create the board
       createBoard();
        // fill the chess board
        fillBoard();
       
    }

    private void createBoard() {
    	 // create the chess board squares
        Insets buttonMargin = new Insets(0, 0, 0, 0);
        for (int ii = 0; ii < chessBoardSquares.length; ii++) {
            for (int jj = 0; jj < chessBoardSquares[ii].length; jj++) {
                JButton b = new JButton();
                b.setMargin(buttonMargin);
                // our chess pieces are 64x64 px in size, so we'll
                // 'fill this in' using a transparent icon..
                ImageIcon icon = new ImageIcon(
                        new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);
                if ((jj % 2 == 1 && ii % 2 == 1)
                        //) {
                        || (jj % 2 == 0 && ii % 2 == 0)) {
                    b.setBackground(Color.WHITE);
                } else {
                    b.setBackground(Color.BLACK);
                }
                chessBoardSquares[ii][jj] = b;
            }
        }

		
	}

	private void fillBoard() {
    	 chessBoard.add(new JLabel(""));
         // fill the top row
         for (int ii = 0; ii < 8; ii++) {
             chessBoard.add(
                     new JLabel(COLS.substring(ii, ii + 1),
                     SwingConstants.CENTER));
         }
         // fill the black non-pawn piece row
         for (int ii = 0; ii < 8; ii++) {
             for (int jj = 0; jj < 8; jj++) {
                 switch (jj) {
                     case 0:
                         chessBoard.add(new JLabel("" + (ii + 1), //(9-(ii + 1))-- Actual board layout
                                 SwingConstants.CENTER));
                     default:
                         chessBoard.add(chessBoardSquares[ii][jj]);
                 }
             }
         }
	}

	public final JComponent getGui() {
        return gui;
    }

    private final void createImages() {
        try {
        	File file = new File("chessPieces.png");
            BufferedImage bi = ImageIO.read(file);
            List<String> pieces = Arrays.asList("Queen","King","Rook","Knight","Bishop","Pawn");
            
            for (int ii = 0; ii < 2; ii++) {
                for (int jj = 0; jj < 6; jj++) {
                	String color = "BLACK";
                	ChessImage chessImage= new ChessImage();
                	
                	chessImage.piece = pieces.get(jj);
                	chessImage.image = bi.getSubimage(
                            jj * 64, ii * 64, 64, 64);
                	if(ii != 0)
                		color = "WHITE";
                	List<ChessImage> images = mapImages.get(color);
                	if(images == null)
                		images = new ArrayList<ChessImage>();
                	images.add(chessImage);
                		
                	mapImages.put(color, images);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Initializes the icons of the initial chess board piece places
     * @param boxs 
     */
    private final void setupNewGame(Box[][] layout) {
    	 message.setText("Make your move!");
    	
    	for(int i = 0; i < 8; i++) {
    		for(int j = 0; j < 8; j++) {
    			if(!layout[i][j].p.pieceName.equals("Empty")) {
    				String color = layout[i][j].p.color;
        			String piece = layout[i][j].p.pieceName;
        			chessBoardSquares[i][j].setIcon(new ImageIcon(
        					getImage(mapImages.get(color), piece)));
    			}
    			chessBoardSquares[i][j].putClientProperty("row", i);
    			chessBoardSquares[i][j].putClientProperty("column", j);
        		chessBoardSquares[i][j].addActionListener(new MyChessActionListener());
    		}
    	}
    }

    private Image getImage(List<ChessImage> images, String piece) {
		for(ChessImage image:images) {
			if(image.piece.equalsIgnoreCase(piece))
				return image.image;
		}
		return null;
		
	}

	public static void main(String[] args) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                cg = new ChessGUI();

                f = new JFrame("ChessChamp");
                f.add(cg.getGui());
             
                // Ensures JVM closes after frame(s) closed and
                // all non-daemon threads are finished
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                // See https://stackoverflow.com/a/7143398/418556 for demo.
                f.setLocationByPlatform(true);

                // ensures the frame is the minimum size it needs to be
                // in order display the components within it
                f.pack();
                // ensures the minimum size is enforced.
                f.setMinimumSize(f.getSize());
                f.setVisible(true);
                //chess = playChess(cg,f);
            }
        };
        // Swing GUIs should be created and updated on the EDT
        // http://docs.oracle.com/javase/tutorial/uiswing/concurrency
        SwingUtilities.invokeLater(r);
    }

	@Override
	public boolean play(String name) {
		status(name+"  your turn");
		return true;
	}

	@Override
	public String move(int srcX, int srcY) {
		while(true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		    if(clicked) {
		    	clicked = Boolean.FALSE;
		    	if(srcX != -1 && srcY != -1) {
		    		JButton btn = null;
		    		boolean validFlg =Boolean.FALSE;
		    		for(Spot position:currentpossibilities) {
		    			if(position.x == currentSpot.x && position.y == currentSpot.y) {
		    				validFlg = Boolean.TRUE;
		    				break;
		    			}
		    		}
		    		if( validFlg == Boolean.FALSE)
		    			callDialog (f,"Not Valid");
		    		else {
		    			btn = chessBoardSquares[srcX][srcY];
		    			chessBoardSquares[currentSpot.x][currentSpot.y].setIcon(btn.getIcon()); // move piece to new location
		    			btn.setIcon(null);// set old location to empty;
		    			
		    		}
		    		clearBorders(btn);
		    	}
		    	return String.valueOf(currentSpot.x) + "," +String.valueOf(currentSpot.y);
		    }
		}
		
	}

	private void clearBorders(JButton btn) {
		for(Spot position:currentpossibilities) {
			btn = chessBoardSquares[position.x][position.y];
			btn.setBorder(null);
		}
		if(saveKing != null) {
			for(Entry<Box,List<Spot>> entry:saveKing.entrySet()) {
				Box b = entry.getKey();
				btn = chessBoardSquares[b.x][b.y];
				btn.setBorder(null);
				for(Spot spot:entry.getValue()) {
					btn = chessBoardSquares[spot.x][spot.y];
					btn.setBorder(null);
				}
			}
		}
	}

	@Override
	public void status(String status) {
		if(status.equals("0")) {
			callDialog(f,"CheckMate !!!");
		} else {
			if(status.equals("-1")) {
				callDialog(f,"Check!!! move King/Piece to prevent CheckMate !!!");
			}
			else
				message.setText(status);
		}
	}
	
	private static ChessGame playChess(ChessGUI cg, JFrame f) {
		LinkedList<String> players = new LinkedList<String>();
		
		if (customDialogBox(f) == 0)
		{
			players.add("BLACK");
			players.add("WHITE");
		}
		else {
			players.add("WHITE");
			players.add("BLACK");
		}
			
		ChessGame chess = new ChessGame(cg,players);
		return chess;
	}
	
	private static int customDialogBox( JFrame f){
		//Custom button text
		Object[] options = {"Black",
		"White"};
		int n = JOptionPane.showOptionDialog(f,
				"Which color would you like to play as ?",
				"A Silly Question",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,     //do not use a custom Icon
				options,  //the titles of buttons
				options[0]); //default button title

		return n;
	}

	@Override
	public void getPossibilities(List<Spot> possibilities) {
		Border redBorder = BorderFactory.createLineBorder(Color.RED,3);
		currentpossibilities = possibilities;
		for(Spot position:possibilities) {
			JButton b = chessBoardSquares[position.x][position.y];
			b.setBorder(redBorder);
		}
	}
	
	private void callDialog( JFrame f,String message) {
		JOptionPane.showMessageDialog(f, message);
	}

	@Override
	public void saveKingPossibilities(HashMap<Box, List<Spot>> map) {
		Border redBorder = BorderFactory.createLineBorder(Color.RED,3);
		Border yellowBorder = BorderFactory.createLineBorder(Color.YELLOW,3);
		saveKing = map;
		
		for(Entry<Box,List<Spot>> entry:map.entrySet()) {
			Box b = entry.getKey();
			JButton b1 = chessBoardSquares[b.x][b.y];
			b1.setBorder(yellowBorder);
			for(Spot spot:entry.getValue()) {
				JButton b2 = chessBoardSquares[spot.x][spot.y];
				b2.setBorder(redBorder);
			}
		}
		
	}
	
	   
    class MyChessActionListener implements ActionListener {

    	@Override
    	public void actionPerformed(ActionEvent e) {
    		JButton btn = (JButton) e.getSource();
    		System.out.println(btn.getText());
    		System.out.println("clicked row " + btn.getClientProperty("row")
    		+ ", column " + btn.getClientProperty("column"));
    		clicked = Boolean.TRUE;
    		currentSpot.x =  Integer.parseInt(btn.getClientProperty("row").toString());
    		currentSpot.y = Integer.parseInt(btn.getClientProperty("column").toString());
    	}
    }
    public void startGame() {
        final SwingWorker chessWorker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() {
                while (!isCancelled()) {
                	chess.start();
                	clearBoard();
                	break;
                }
                return null;
            }
        };

        chessWorker.execute();
    }
    private void clearBoard() {
		for(int i = 0; i < 8; i++) {
    		for(int j = 0; j < 8; j++) {
		chessBoardSquares[i][j].setIcon(null);
    		}
		}
		message.setText("Start a new Game buddy !!!");
	}
	
}

class ChessImage {
	String piece;
	Image image; 
}

