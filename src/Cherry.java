public class Cherry extends Upgrade {

    public Cherry(Board board, Upgrades type) {
        super(board, type);
    }

    @Override
    void upgrade() {board.score += 100;}

    @Override
    void restore(){}
}
