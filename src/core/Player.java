package core;

public enum Player {
    /**
     * 表示玩家
     * PLAYER_1: 玩家1
     * PLAYER_2: 玩家2
     */
    PLAYER_1(1), PLAYER_2(2);

    private final int value;

    Player(int val) {
        this.value = val;
    }

    public int value() {
        return this.value;
    }

    public Player next() {
        return this == PLAYER_1 ? PLAYER_2 : PLAYER_1;
    }

    public static Player of(int value) {
        if (value == PLAYER_1.value) {
            return PLAYER_1;
        } else if (value == PLAYER_2.value) {
            return PLAYER_2;
        }
        return null;
    }
}