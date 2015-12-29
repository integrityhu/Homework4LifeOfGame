import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JSlider;

/*
 * created by: Ifj. Dravecz Tibor
 * 2013
*/
public class Test {

	static int speed = 500;
	static final int showSize = 7;
	static int x = 100, y = x;
	static byte[][] table = new byte[x][y];

	static final byte STATUS_NONE = 0;
	static final byte STATUS_LIVE = 1;
	static final byte STATUS_DEAD = 2;
	static final byte STATUS_BORN = 3;
	static final byte STATUS_OLD = 127;
	private static Display viewer;
	static boolean stop = false;

	public Test() {
		viewer = new Display(y, x, this);
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {

		JFrame frame = new JFrame("Speed Set");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 100);
		Container content = frame.getContentPane();
		content.setBackground(Color.white);

		JSlider sliderSpeed = new JSlider();
		sliderSpeed.setBorder(BorderFactory
				.createTitledBorder("0 is no sleep time and 100 is stop"));
		sliderSpeed.setMajorTickSpacing(5);
		sliderSpeed.setMinorTickSpacing(1);
		sliderSpeed.setPaintTicks(true);
		sliderSpeed.setPaintLabels(true);
		content.add(sliderSpeed, BorderLayout.CENTER);

		frame.setVisible(true);

		Test aGame = new Test();
		Random rn = new Random();
		// véletlen szerüen 0, 1 el kitölti a táblázatot
		for (int i = 0; i < table.length; i++)
			for (int j = 0; j < table[i].length; j++)
				table[i][j] = (byte) rn.nextInt(2);
		for (;;) {
			speed = sliderSpeed.getValue() * 10;
			// ez itt müködik:
			while (speed == 1000 || stop) {
				speed = sliderSpeed.getValue() * 10;
			}
			tableModifier();
			paint();
			tableClear();
			paint();
		}
	}

	private static void paint() {
		viewer.repaint();
		try {
			Thread.sleep(speed);
		} catch (Exception e) {
		}
	}

	// LIVE-ra és NONE-ra állítja STATUS_BORN, STATUS_DEAD, állapotú négyzeteket
	private static void tableClear() {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				if (table[i][j] == STATUS_BORN)
					table[i][j] = STATUS_LIVE;
				else if (table[i][j] == STATUS_DEAD)
					table[i][j] = STATUS_NONE;
				else if (table[i][j] == STATUS_LIVE)
					table[i][j] = 4;
				else if (table[i][j] >= 4 && table[i][j] < STATUS_OLD)
					table[i][j]++;
			}
		}
	}

	// megnéz minden négyzetet és a szomszédjai alapján eldönti mi legyen vele
	private static void tableModifier() {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				int neighbors = countNeighbors(i, j);
				if (table[i][j] == STATUS_NONE) {
					if (neighbors == 3)
						table[i][j] = STATUS_BORN;
				} else {
					if ((neighbors < 2 || neighbors > 3))
						table[i][j] = STATUS_DEAD;
				}
			}
		}
	}

	//meg számolja a négyzet szomszédait (ez most neveztem át előtte scan() volt)
	private static int countNeighbors(int i, int j) {
		int rtn = 0;
		for (byte m = -1; m < 2; m++)
			for (byte n = -1; n < 2; n++)
				if (!(n == 0 && m == 0) && test(i, j, m, n))
					rtn++;
		return rtn;
	}

	// visszadja hogy az adot négyzettől i,j adott eltolással k, j van-e nem STATUS -_BORN vagy -_NONE négyzet
	private static boolean test(int i, int j, byte k, byte l) {
		try {
			if (table[i + k][j + l] > 0 && table[i + k][j + l] != STATUS_BORN)
				return true;
			else
				return false;
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	public byte cellStatus(int r, int c) {
		return table[r][c];
	}

	public void setTable(int r, int c, byte value) {
		table[r][c] = value;
		viewer.repaint();
	}

	public void Stop() {
		stop = true;
	}

	public void Start() {
		stop = false;
	}
}
