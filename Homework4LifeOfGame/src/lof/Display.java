
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/*
 * created by: Ifj. Dravecz Tibor
 * 2013
*/
@SuppressWarnings("serial")
public class Display extends javax.swing.JPanel implements MouseListener,
		MouseMotionListener {

	private int cellWidth = Test.showSize;
	private int cellHeight = cellWidth;

	private int cellsAcross;
	private int cellsDown;
	public static boolean[][] setAblility = new boolean[Test.x][Test.y];
	final int margin = 1;

	private Test game;

	@SuppressWarnings("deprecation")
	public Display(int across, int down, Test aGame) {
		super();
		addMouseListener(this);
		addMouseMotionListener(this);
		this.cellsAcross = across;
		this.cellsDown = down;
		this.game = aGame;
		javax.swing.JFrame frame = new javax.swing.JFrame("Game of Life");
		frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
		int totalWidth = cellWidth * cellsAcross;
		int totalHeight = cellHeight * cellsDown;
		frame.setSize(totalWidth + 50, totalHeight + 50);
		this.setBackground(java.awt.Color.orange);
		this.setPreferredSize(new java.awt.Dimension(totalWidth + 10,
				totalHeight + 10));
		java.awt.Container container = frame.getContentPane();
		container.setBackground(java.awt.Color.gray);
		container.setLayout(new java.awt.FlowLayout());
		container.add(this);

		frame.show();
	}

	public void paintComponent(java.awt.Graphics origG) {
		super.paintComponent(origG);
		java.awt.Graphics2D g = (java.awt.Graphics2D) origG;
		for (int r = 0; r < cellsDown; r++) {
			for (int c = 0; c < cellsAcross; c++) {
				java.awt.Shape cellOutline = new java.awt.Rectangle(c
						* cellWidth + margin, r * cellHeight + margin,
						cellWidth, cellHeight);
				g.setColor(java.awt.Color.black);
				g.draw(cellOutline);
				java.awt.Shape cellShape = new java.awt.Rectangle(c * cellWidth
						+ margin + 1, r * cellHeight + margin + 1,
						cellWidth - 1, cellHeight - 1);
				if (game.cellStatus(r, c) == Test.STATUS_NONE) {
					g.setColor(java.awt.Color.gray);
				} else if (game.cellStatus(r, c) == Test.STATUS_OLD) {
					g.setColor(java.awt.Color.orange);
				} else if (game.cellStatus(r, c) == Test.STATUS_BORN) {
					g.setColor(java.awt.Color.green);
				} else if (game.cellStatus(r, c) == Test.STATUS_DEAD) {
					g.setColor(java.awt.Color.red);
				} else if (game.cellStatus(r, c) >= Test.STATUS_LIVE) {
					g.setColor(java.awt.Color.yellow);
				}
				g.fill(cellShape);
			}
		}
	}

	// ezek az egérrel tőrténő állításokhoz szükségesek
	// újra állíthatóva teszi az összes négyzetet
	private void refillSetAblility() {
		for (int i = 0; i < Test.x; i++)
			for (int j = 0; j < Test.y; j++)
				setAblility[i][j] = true;
	}

	// NONE --> LIVE, minden más --> NONE
	public byte getNextValue(int cellStatus) {
		if (cellStatus == Test.STATUS_NONE)
			return Test.STATUS_LIVE;
		else
			return Test.STATUS_NONE;
	}

	// az egér kordinátály alapján állítja a négyzet(ek)et
	private void setTable(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int c = (x - margin) / cellWidth;
		int r = (y - margin) / cellHeight;
		try {
			if (setAblility[c][r] == true) {
				game.setTable(r, c, getNextValue(game.cellStatus(r, c)));
				setAblility[c][r] = false;
			}
		} catch (ArrayIndexOutOfBoundsException aioobe) {
		}
	}

	public void mouseDragged(MouseEvent e) {
		setTable(e);		
	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		refillSetAblility();
		setTable(e);
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		game.Stop();		
	}

	public void mouseExited(MouseEvent e) {
		game.Start();		
	}
}
