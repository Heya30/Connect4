package ui.components;

import javax.swing.*;
import java.awt.*;
import core.Board;
import core.GameControl;
import enumerations.GridType;

/**
 * @author Fimon, Raymond
 */
public class BoardPanel extends JPanel {

    public static final int SQUARE_SIZE = 80;
    public static final int GAP = 10;


    public static final int PADDING_HORIZONTAL = 8;
    public static final int PADDING_VERTICAL = 8;
    public static final int COL = Board.getInstance().getCOL();
    public static final int ROW = Board.getInstance().getROW();
    public static final int WIDTH = COL * SQUARE_SIZE + (COL - 1) * GAP + (PADDING_HORIZONTAL << 1);
    public static final int HEIGHT = ROW * SQUARE_SIZE + (ROW - 1) * GAP + (PADDING_VERTICAL << 1);

    private static final Color BG_COLOR1 = new Color(121, 161, 109);
    private static final Color BG_COLOR2 = new Color(202, 196, 102);

    private static final Color CHESS_COLOR1 = Color.BLACK;
    private static final Color CHESS_COLOR2 = Color.WHITE;

    private GameControl gameControl;

    public GameControl getCore() {
        return gameControl;
    }

    public BoardPanel() {
        super();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setLayout(new GridLayout(ROW, COL));

        //TODO 右边框暂时不能显示
        this.setBorder(BorderFactory.createEtchedBorder());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        int[][] boardStatus = Board.getInstance().getGameBoard();
        for (int r = 0; r < ROW; r++) {
            for (int c = 0; c < COL; c++) {
                if ((r + c) % 2 != 0) {
                    g.setColor(BG_COLOR1);
                } else {
                    g.setColor(BG_COLOR2);
                }
                g.fillRect((PADDING_HORIZONTAL + c * (SQUARE_SIZE + GAP)), (PADDING_VERTICAL + r * (SQUARE_SIZE + GAP)), SQUARE_SIZE, SQUARE_SIZE);

                if (boardStatus[r][c] != GridType.EMPTY.value()) {
                    g.setColor(boardStatus[r][c] == GridType.PLAYER_1.value() ? CHESS_COLOR1 : CHESS_COLOR2);
                    g.fillOval((PADDING_HORIZONTAL + c * (SQUARE_SIZE + GAP)), (PADDING_VERTICAL + r * (SQUARE_SIZE + GAP)), SQUARE_SIZE, SQUARE_SIZE);
                }

            }
        }
    }

    public void setGameControl(GameControl gameControl) {
        this.gameControl = gameControl;
    }
}
