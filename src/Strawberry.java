public class Strawberry extends Upgrade {

    public Strawberry(Board board, Upgrades type) {
        super(board, type);
    }

    @Override
    void upgrade() {
        board.score += 300;
    }

    @Override
    void restore(){}
}
