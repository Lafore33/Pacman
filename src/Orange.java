public class Orange extends Upgrade {
    public Orange(Board board, Upgrades type) {
        super(board, type);
    }

    @Override
    void upgrade() {
        board.pacman.isInvincible = true;
    }

    @Override
    void restore(){
        board.pacman.isInvincible = false;
    }

}
