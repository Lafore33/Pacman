public class Melon extends Upgrade {

    public Melon(Board board, Upgrades type) {
        super(board, type);
    }

    @Override
    void upgrade() {
        board.pacman.basicPoints *= 2;
    }

    @Override
    void restore(){
        board.pacman.basicPoints /= 2;
    }
}
