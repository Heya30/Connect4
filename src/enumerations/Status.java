package enumerations;

public enum Status {
    /**
     * 表示游戏状态
     * WIN: 一方胜利
     * FAIL: 和局
     * CONTINUE: 未决胜负
     */
    WIN(0), FAIL(1), CONTINUE(2), FULL(3);

    private final int value;

    Status(int val) {
        this.value = val;
    }

    public int value() {
        return this.value;
    }

    public static Status of(int value) {
        if (value == WIN.value) {
            return WIN;
        } else if (value == FAIL.value) {
            return FAIL;
        } else if (value == CONTINUE.value) {
            return CONTINUE;
        }else if(value == FULL.value){
            return FULL;
        }
        return null;
    }
}
