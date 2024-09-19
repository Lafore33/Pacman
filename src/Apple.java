
public class Apple extends Upgrade {

    public Apple(Board board, Upgrades type) {
        super(board, type);
    }

    @Override
    void upgrade() {
        board.pacman.setVelocity(board.pacman.velocity / 2);
    }

    @Override
    void restore(){
        board.pacman.setVelocity(board.pacman.velocity * 2);

    }
}
