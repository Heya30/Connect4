package core;

import enumerations.GridType;
import enumerations.Status;

public class Board {

    int ROW = 6;
    int COL = 7;
    private int [][] gameBoard;

    private static Board board = new Board();
    private Board(){
        this.gameBoard = new int[ROW][COL];
    }
    public static Board getInstance(){
        return board;
    }

    /**
     * 判断棋盘是否全部填满
     * @return false：为填满；true：填满
     */
    private boolean judgeFail() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                if (gameBoard[i][j] == GridType.EMPTY.value()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean judgeBorder(int x, int size) {
        return x < size && x >= 0;
    }

    /**
     * 判定当前游戏状态
     * @param r     本次落子的行
     * @param c     本次落子的列
     * @return 游戏状态
     */
    public Status judge(int r, int c) {
        if (judgeSuccess(r, c)) {
            return Status.WIN;
        } else if (judgeFail()) {
            return Status.FAIL;
        }
        return Status.CONTINUE;
    }

    /**
     * dr[0], dc[0]: ←
     * dr[1], dc[1]: ↓
     * dr[2], dc[2]: ↖
     * dr[3], dc[3]: ↘
     */
    private static final int[][] DELTA_R = {{0, 0}, {-1, 1}, {-1, 1}, {1, -1}};
    private static final int[][] DELTA_C = {{-1, 1}, {0, 0}, {-1, 1}, {-1, 1}};
    private static final int N_DIRECTION = DELTA_R.length;

    /**
     * 判断是否连成四子
     * 以“上次落子位置”为中心，向四个方向进行搜索，判断是否连成四子
     * @param r     上次落子的行
     * @param c     上次落子的列
     * @return 当前落子后，是否连成四子
     */
    private boolean judgeSuccess(int r, int c) {
        if (gameBoard.length == 0) {
            return false;
        }
        if (gameBoard.length < N_DIRECTION && gameBoard[0].length < N_DIRECTION) {
            return false;
        }

        for (int dirc = 0; dirc < N_DIRECTION; dirc++) {
            if (count(dirc, r, c) >= 4) {
                return true;
            }
        }
        return false;
    }

    /**
     * 统计以(r, c)为中心，在某个方向上的连续棋子总数
     * @param dirc  表示扩展方向，与 dr,dc的下标一致
     * @param r     棋子所在行
     * @param c     棋子所在列
     * @return 以(r, c)为中心的最大连续棋子数
     */
    private int count( int dirc, int r, int c) {
        // nr, nc表示下一个查看位置
        int nr = r, nc = c;
        int result = 0;
        do {
            nr += DELTA_R[dirc][0];
            nc += DELTA_C[dirc][0];
            result++;
            // 如果(nr, nc)在棋盘范围内，并且与当前(r,c)处棋子颜色相同
            // 则继续往后查看
        } while (withinBoard(nr, nc) && gameBoard[nr][nc] == gameBoard[r][c]);
        nr = r;
        nc = c;
        do {
            nr += DELTA_R[dirc][1];
            nc += DELTA_C[dirc][1];
            result++;
        } while (withinBoard(nr, nc) && gameBoard[nr][nc] == gameBoard[r][c]);
        return result - 1;
    }

    /**
     * 判定是否在棋盘内
     * @param r     行
     * @param c     列
     * @return 是否在棋盘范围内
     */
    private boolean withinBoard(int r, int c) {
        return (r >= 0 && c >= 0 && r < gameBoard.length && c < gameBoard[0].length);
    }

    public int getROW() {
        return ROW;
    }

    public int getCOL() {
        return COL;
    }


    public int[][] getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(int[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void resetBoard() {
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                this.gameBoard[i][j] = GridType.EMPTY.value();
            }
        }
    }

    public void printBoard() {
        int[][] gameBoard = this.gameBoard;
        System.out.println("| 1 | 2 | 3 | 4 | 5 | 6 | 7 |");
        System.out.println();
        for (int i=0; i<board.getROW(); i++) {
            for (int j=0; j<board.getCOL(); j++) {
                if (j != board.getCOL() - 1) {
                    if (gameBoard[i][j] != 0) {
                        System.out.print("| " + gameBoard[i][j] + " ");
                    } else {
                        System.out.print("| " + "-" + " ");
                    }
                } else {
                    if (gameBoard[i][j] != 0) {
                        System.out.println("| " + gameBoard[i][j] + " |");
                    } else {
                        System.out.println("| " + "-" + " |");
                    }
                }
            }
        }
        System.out.println("\n*****************************");

    }

}
