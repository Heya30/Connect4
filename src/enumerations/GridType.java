package enumerations;

import core.Player;

public enum GridType {
    /**
     * 表示棋盘上某个位置的状态
     * EMPTY: 未落子
     * PLAYER_1: 玩家1落子
     * PLAYER_2: 玩家2落子
     */
    EMPTY(0), PLAYER_1(1), PLAYER_2(2);

    private final int value;

    GridType(int val) {
        this.value = val;
    }

    public int value() {
        return this.value;
    }

    public static int of(Player p) {
        return p == Player.PLAYER_1 ? PLAYER_1.value : PLAYER_2.value;
    }
}