import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class TalismanPlayerCLI extends TalismanPlayer {

	BufferedReader input = (new BufferedReader(new InputStreamReader(System.in)));
	String moveInput;
	String[] temp;

	TalismanPlayerCLI(TalismanBoard board, int player) {
		super(board, player);
	}

	@Override
	int[] move() {
		int[] move = new int[2];
		
		while (true) {
			try {
				moveInput = input.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				moveInput = "";
			}
			temp = moveInput.split(" ");
			try {
				if (temp[0].length() == 0) continue;
				move[0] = temp[0].toUpperCase().charAt(0) - 65;
				move[1] = -1;
				if (temp.length > 1) {
					if(temp[1].length() == 0) {
						System.out.println("Must be a pair of numbers");
						continue;
					}
					move[1] = temp[1].toUpperCase().charAt(0) - 65;
				} else {
					if(board.phase2) {
						System.out.println("Must be a pair of numbers");
						continue;
					}
				}
			} catch (NumberFormatException e) {
				if (board.phase2) {
					System.out.println("Must be a pair of numbers");
				} else {
					System.out.println("Must be a single number");
				}
				continue;
			}
		
			break;

		}

	  return move;
	}
	
}