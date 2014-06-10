public abstract class TalismanPlayer {

	TalismanBoard board;
	int player;
	
	public TalismanPlayer(TalismanBoard board, int player) {
		this.board = board;
		this.player = player;
	}

	abstract int[] move();
}