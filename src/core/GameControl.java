package core;
import enumerations.GridType;
import enumerations.Status;

public class GameControl {
    private Board board;
    private Player currPlayer;
    private Status gameState;

    public GameControl() {
        this.board = Board.getInstance();
        //默认从第一个玩家开始
        this.currPlayer = Player.PLAYER_1;
        this.gameState = Status.CONTINUE;
    }

    /**
     * 在某一列落子
     * @param column 落子列
     * @return
     */
    public Status dropAt(int column) {
        int top = -1;
        while (top < board.getROW() - 1 && board.getGameBoard()[top + 1][column] == GridType.EMPTY.value()) {
            top++;
        }
        if (top == -1) {
            // 没有空位
            return Status.FULL;
        }
        board.getGameBoard()[top][column] = GridType.of(currPlayer);

        // 打印棋盘
        board.printBoard();

        // 每次落子后判断一次游戏状态
        // (top, column) 为本次落子位置
        this.gameState = board.judge(top, column);
        return this.gameState;
    }

    /**
     * 返回游戏状态
     * @return 游戏状态
     */
    public Status getGameStatus() {
        return this.gameState;
    }

    /**
     * 返回当前玩家
     * @return 当前玩家
     */
    public Player getCurrPlayer() {
        return this.currPlayer;
    }

    /**
     * 重置棋盘
     */
    public void reset() {
        this.gameState = Status.CONTINUE;
        board.resetBoard();
        resetPlayer();
    }

    private void resetPlayer() {
        this.currPlayer = Player.PLAYER_1;
    }

    /**
     * 设置当前玩家
     * @param currPlayer 当前玩家
     */
    public void setCurrPlayer(Player currPlayer) {
        this.currPlayer = currPlayer;
    }

    /**
     * 设置游戏状态
     * @param gameState 游戏状态
     */
    public void setGameState(Status gameState) {
        this.gameState = gameState;
    }

    /**
     * 交换玩家
     * @return
     */
    public Player switchPlayer() {
        this.currPlayer = currPlayer.next();
        return this.currPlayer;
    }

}
