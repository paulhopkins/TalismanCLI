import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class TalismanCLI {
	static TalismanBoard board = new TalismanBoard();
    static TalismanPlayer[] players = new TalismanPlayer[2];
    
	static BufferedReader input = (new BufferedReader(new InputStreamReader(System.in)));
	
    public static void main(String[] args) {
        System.out.println("Welcome to Terry Stygall's Talisman");
        System.out.println("This version Paul Hopkins (2012)");
        System.out.println("");
        
        System.out.println("Enter number of human players; 0, 1 or 2?");
        Integer numPlayers = 0;
        while (true)
        {
        	String numPlayersInput;
			try {
				numPlayersInput = input.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				numPlayersInput = "";
			}
        	numPlayers = Integer.parseInt(numPlayersInput);
        	if (numPlayers >= 0 && numPlayers <= 2) {
        		break;
        	} else {
        		System.out.println("Number of human players must be 0,1,2!");
        	}
        }
        
        System.out.format("%d players selected.\n", numPlayers);

    	String levelInput;
    	int level;
        
        for (int i=0; i < 2; ++i) {
        	if (i < numPlayers) {
        		players[i] = new TalismanPlayerCLI(board, i);
        	} else {
        		System.out.format("Please enter a level for player %d\n", i+1);
    	       while (true)
    	        {
    				try {
    					levelInput = input.readLine();
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    					levelInput = "";
    				}
    	        	level = Integer.parseInt(levelInput);
    	        	if (level > 0) {
    	        		break;
    	        	} else {
    	        		System.out.println("Enter a level greater than 0!");
    	        	}
    	        }
        		players[i] = new TalismanPlayerAI(board, i,level);
        	}
        }

    	int points;

    	boolean phase2Begin = false;
    	int[] move;
        
        if (numPlayers > 0) {
        	System.out.println("Automatically complete phase 1?");
        	String autoPhase1Input;
			try {
				autoPhase1Input = input.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				autoPhase1Input = "N";
			}
        	if (autoPhase1Input.length() > 0 && autoPhase1Input.toUpperCase().charAt(0) == 'Y') {
        		for (int i = 0; i < 12; i++) {
        			board.makeMove(board.getLegalMoves()[1-board.player], 0);
        		}
        	} 
        }

    	
    	//int[] coords;
//    	for (int i=0; i<19; i++) {
//    		coords = board.getCoords(i);
//    		System.out.format("%d %d %d %d\n", i, coords[0], coords[1], coords[2]);
//    	}
    	
    	printBoard();
    	
    	int turnCount = 0;
    	
        while (!board.gameFinished()) {
        	move = players[board.player].move();
        	points = board.makeMove(move[0], move[1]);
        	
        	if (points < 0) {
        		System.out.println("Illegal Move!"); 
        	}
        	
        	if (!phase2Begin && board.phase2) {
        		System.out.println("Phase 1 complete! Moving to phase2.");
        		phase2Begin = true;
        	}

        	printBoard();
        	turnCount++;
        	if (turnCount > 1000000) break;
        }
        
        System.out.println("Game Over!");
        printBoard();
        
    }
    
	static void printBoard() {
        Integer[] line;
        char nodeChar;
        String nodePlayer;
        char triChar;
        int[] triangleLimits = {0, 5, 12, 19, 24, 24};
        
        System.out.format("Player 1: %-4d   Player2: %-4d\n", board.score[0], board.score[1]);
        
        for (int lineIdx = 0; lineIdx < 5; lineIdx++) {
       	 for (int i = 0; i < Math.abs(lineIdx - 2); i++) {
       		 System.out.format("  ");
       	 }
       	 
       	 line = board.lines[lineIdx];
       	 for (int node : line) {
       		 nodeChar = (char) (node + 65);
       		 if (board.nodes[node] != -1) {
       			 nodePlayer = "" + (char) (board.nodes[node] + 49);
       		 } else {
       			 nodePlayer = " ";
       		 }
       		 System.out.format("%c%s  ", nodeChar, nodePlayer);
       	 }
       	 System.out.format("\n");
       	 
       	nodePlayer = "";
       	if (lineIdx == 0 || lineIdx == 3) nodePlayer = "  ";
       	 
       	for (int i = triangleLimits[lineIdx]; i < triangleLimits[lineIdx + 1]; i++) {
       		triChar = '-';
       		if (board.triangles[i] > -1) triChar = (char) (board.triangles[i] + 49);
       		nodePlayer += " " + triChar;
   		 }
   		 System.out.format(" %s\n", nodePlayer);
        }
        
        
        
        int phase = 1;
        if (board.phase2) phase = 2;              
        System.out.format("Phase %d. Player %d to move.\n", phase, board.player + 1);
   }

}

/**
        A' ---- B' ---- C'   
      /   \   /   \   /   \
    D' ---- E' ---- F' ---- G   
  /   \   /   \   /   \   /   \
H' ---- I' ---- J' ---- K' ---- L'   
  \   /   \   /   \   /   \   /
    M' ---- N' ---- O' ---- P
      \   /   \   /   \   /  
        Q' ---- R' ---- S'  
  */ 

/**
Talisman, which is TalismanCLI or TalismanAndroid:
	1x TalismanBoard
	2x TalismanPlayer, which is either TalismanPlayerCLI or TalismanPlayerAI, or TalismanPlayerAndroid etc.
	*/