import java.util.ArrayList;
import java.util.Random;
/**
 * 
 */

/**
 * @author paul
 *
 */
class TalismanPlayerAI extends TalismanPlayer {

	Random random = new Random();
	int depth = 3;
	/**
	 * @param board
	 */
	public TalismanPlayerAI(TalismanBoard board, int player, int depth) {
		super(board, player);
		this.depth = depth;
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see TalismanPlayer#move()
	 */
	@Override
	int[] move() {
		
		TalismanBoardState state = board.getState();
		ArrayList<int[]> moves = new ArrayList<int[]>();
		
		int[][] movePairs= board.getLegalMovePairs();

		int alpha = - Integer.MAX_VALUE;
		int score;
		
		for (int[] pair : movePairs) {	
			board.makeMove(pair[0], pair[1]);
			score = minimax(-1, this.depth-1, -Integer.MAX_VALUE, +Integer.MAX_VALUE);
			if (score > alpha) {
				alpha = score;
				moves.clear();
				moves.add(pair);
			} else if (score == alpha) {
				moves.add(pair);
			}
			board.setState(state);
		}
		
		int[] move = moves.get(random.nextInt(moves.size()));
		System.out.format("**** Move: %d %c %c %d\n", this.player, (char) (move[0] + 65), (char) (move[1] + 65), alpha);
		return move;
//		final int maxDepth = 3;
//		int pointsDiff;
//		int minMaxPoints = -10000;
//		ArrayList<int[]> moves = new ArrayList<int[]>();
//		int[] move0 = {-1, -1};
////		int[][][] movePairs = new int[maxDepth][][];
//		TalismanBoardState[] states = new TalismanBoardState[maxDepth];
//		
//		for (int depth = 0; depth < maxDepth; depth++) {
//			states[depth] = board.getState();
//			movePairs[depth] = board.getLegalMovePairs();
//			for (int[] pair : movePairs[depth]) {
//				if (depth == 0) {
//					move0 = pair.clone();
//				}
//
//				board.makeMove(pair[0], pair[1]);
//				pointsDiff = board.score[states[0].player] - board.score[1 - states[0].player]; 
//				System.out.format("** Start End Points: %d %d %d\n", pair[0], pair[1], pointsDiff);							
//
//				if (depth == (maxDepth-1) && pointsDiff > minMaxPoints) {
//					minMaxPoints = pointsDiff;
//					move = move0.clone();
//				}
//				
//				board.setState(states[depth]);
//			}
//		}
		
/*		int[][] movePairs0, movePairs1, movePairs2;
		
		states[0] = board.getState();
		movePairs0 = board.getLegalMovePairs();
		for (int[] pair0 : movePairs0) {
			move0 = pair0.clone();
			board.makeMove(pair0[0], pair0[1]);
	
			//System.out.format("* Pair0: %d %d", pair0[0], pair0[1]);
			
			states[1] = board.getState();
			movePairs1 = board.getLegalMovePairs();
			for (int[] pair1 : movePairs1) {
				board.makeMove(pair1[0], pair1[1]);
				
				//System.out.format("* Pair1: %d %d", pair1[0], pair1[1]);
				
				states[2] = board.getState();
				movePairs2 = board.getLegalMovePairs();
				for (int[] pair2 : movePairs2) {
					board.makeMove(pair2[0], pair2[1]);
					
					pointsDiff = board.score[states[0].player] - board.score[1 - states[0].player]; 
	
					//System.out.format("* Pair2: %d %d %d\n", pair2[0], pair2[1], pointsDiff);
						
					if (pointsDiff > minMaxPoints) {
						minMaxPoints = pointsDiff;
						moves.clear();
						moves.add(move0);
					} else if (pointsDiff == minMaxPoints) {
						moves.add(move0);
					}
					
					board.setState(states[2]);
				}
				board.setState(states[1]);
			}
			board.setState(states[0]);
		}*/

//		int[] move = moves.get(random.nextInt(moves.size()));
//		System.out.format("**** Move: %d %c %c %d\n", states[0].player, (char) (move[0] + 65), (char) (move[1] + 65), minMaxPoints);
//		return move;
	}
	
	int minimax(int player, int depth, int alpha, int beta) {
//	      -- positive values are good for the maximizing player
//	      -- negative values are good for the minimizing player
		TalismanBoardState state = board.getState();
	
		if (board.gameFinished()) {
			if (board.score[this.player] > board.score[1-this.player]) {
				return Integer.MAX_VALUE;
			} else if (board.score[this.player] < board.score[1-this.player]) {
				return -Integer.MAX_VALUE;
			} else {
				return 0;
			}
		}
		
		if (depth <= 0) {
	      return board.score[this.player] - board.score[1-this.player];
	   }
	   
	
//	   -- maximizing player is (+1)
//	   -- minimizing player is (-1)
	 
		int[][] movePairs= board.getLegalMovePairs();

		int score;
		
		if (player == 1) {	
			for (int[] pair : movePairs) {	
				board.makeMove(pair[0], pair[1]);
				score = minimax(-player, depth-1, alpha, beta);
			
				alpha = Math.max(alpha,  score);
				board.setState(state);
				
				if (beta<=alpha) break;
			}
			
			return alpha;
			
		} else {
			for (int[] pair : movePairs) {	
				board.makeMove(pair[0], pair[1]);
				score = minimax(-player, depth-1, alpha, beta);
			
				beta = Math.min(beta,  score);
				board.setState(state);
				
				if (beta<=alpha) break;
			}
			
			return beta;
		}
	}

}
