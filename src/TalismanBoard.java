import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/*
Board:

     0   1   2
   3   4   5   6
 7   8   9  10  11
  12  13  14  15
    16  17  18
 */

class TalismanBoard {
	boolean phase2 = false;
	int player = 0;
	int[] nodes = new int[19];
	int[] triangles = new int[24];
	int[] bigTriangles = new int[12];
	int[] score = new int[2];
	
	Integer[][] lines = {{0,1,2}, {3,4,5,6}, {7,8,9,10,11}, {12, 13, 14, 15}, {16, 17, 18},
			{7, 12, 16}, {3, 8, 13, 17}, {0, 4, 9, 14, 18}, {1, 5, 10, 15}, {2, 6, 11}, 
			{0, 3, 7}, {1, 4, 8, 12}, {2, 5, 9, 13, 16}, {6, 10, 14, 17}, {11, 15, 18}};

	int[][] triangleNodes = {{0, 3, 4}, {0, 1, 4}, {1, 4, 5}, {1, 2, 5}, {2, 5, 6}, 
			{3, 7, 8}, {2, 4, 8}, {4, 8, 9}, {4, 5, 9}, {5, 9, 10}, {5, 6, 10}, {6, 10, 11},
			{7, 8, 12}, {8, 12, 13}, {8, 9, 13}, {9, 13, 14}, {9, 10, 14}, {10, 14, 15}, {10, 11, 15}, 
			{12, 13, 16}, {13, 16, 17}, {13, 14, 17}, {14, 17, 18}, {14, 15, 18}};
	
	int[][] bigTriangleTriangles = {{0, 5, 6, 7}, {2, 7, 9, 9}, {1, 2, 3, 8}, {4, 9, 10, 11}, {6, 7, 8, 14},
			{8, 9, 10, 16}, {7, 13, 14, 15}, {9, 15, 16, 17}, {12, 13, 14, 19}, {14, 15, 16, 21},
			{16, 17, 18, 23}, {20, 21, 22, 15}};
	
	public TalismanBoard() {
		reset();
	}
	
	void reset() {
		phase2 = false;
		player = 0;
		Arrays.fill(nodes, -1);
		Arrays.fill(triangles, -1);
		Arrays.fill(bigTriangles, -1);
		Arrays.fill(score, 0);
	}
	
	TalismanBoardState getState() {
		TalismanBoardState state = new TalismanBoardState();
		state.phase2 = phase2;
		state.player = player;
		state.nodes = nodes.clone();
		state.triangles = triangles.clone();
		state.bigTriangles = bigTriangles.clone();
		state.score = score.clone();
		
		return state;
	}
	
	void setState(TalismanBoardState state) {
		phase2 = state.phase2;
		player = state.player;
		nodes = state.nodes.clone();
		triangles = state.triangles.clone();
		bigTriangles = state.bigTriangles.clone();
		score = state.score.clone();
	}
	
	boolean gameFinished() {
		int[] playerCount = {0,0};
		for (int node : nodes) {
			if (node > -1) {
				playerCount[node]++;
			}
		}
		
		if (phase2 && (playerCount[0] == 0 || playerCount[1] == 0)) {
			return true;
		}
			
		int triangleCount = 0;
		
		for (int triangle = 0; triangle < 24; triangle++) {
			if (triangles[triangle] != -1) {
				triangleCount++;
			}
		}
		
		if (triangleCount >= 20) {
			return true;
		}
		return false;
	}
	
	// Return -1 if illegal move, else returns player point change
	int makeMove(int start, int end) {
		int points = 0;
		
		// Phase two mode
		if (phase2) {
			// Check that there is a piece that can be moved 
			if (nodes[start] != player) {
				System.err.format("Not player at start (%d).", nodes[start]);
				return -1;
			}

			// Check if dest is empty
			if (nodes[end] != -1) {
				System.err.format("End not empty (%d).\n", nodes[end]);
				return -1;
			}

			
			//  If node is on same line
			boolean found = false;
			Integer[] currentLine = null;

			for (Integer[] line : lines) {
				if (Arrays.asList(line).contains(new Integer(start)) &&
						Arrays.asList(line).contains(new Integer(end))) {
					currentLine = line;					
					found = true;
					
					break;
				}
			}
			
			if(!found) {
				System.err.println("Start and end are not on the same line.");
				return -1;
			}
			

			// If only rival pieces between start and end
			boolean inside = false;
			boolean onlyOther = true;

			for (int pos : currentLine) {
				if (pos == start || pos == end) {
					inside = !inside;
				} else {
					if (inside && nodes[pos] != (1-player) ) {
						onlyOther = false;
					}
				}
			}

			 if (!onlyOther) {
				 System.err.println("Did not find just other player between start and end");
				 return -1;
			 }
			
			// Then Move piece
			nodes[start] = -1;
			nodes[end] = player;
			
			// Convert rivals
			for (int pos : currentLine) {
				if (pos == start || pos == end) {
					inside = !inside;
				} else {
					if (inside) {
						nodes[pos] = player;
						points += 10;
					}
				}
			}
				
			// Check for new triangles
			int[] triNodes;
			for (int triangle = 0; triangle < 24; triangle++) {
				//System.err.format("Got: %d %d %d %d %d\n", triangle, triangles[triangle], 
				//		nodes[triNodes[0]], nodes[triNodes[1]], nodes[triNodes[2]]);
				if (triangles[triangle] == -1) {
					triNodes = triangleNodes[triangle];
					if (nodes[triNodes[0]] == player && nodes[triNodes[1]] == player && nodes[triNodes[2]] == player) {
						triangles[triangle] = player;
						points += 50;
					}
				}
			}

			// Check for new big triangles
			int[] bigTriTriangles;
			for (int bigTri = 0; bigTri < 12; bigTri++) {
				if (bigTriangles[bigTri] == -1) {
					bigTriTriangles = bigTriangleTriangles[bigTri];
					if (triangles[bigTriTriangles[0]] == player && triangles[bigTriTriangles[1]] == player && 
							triangles[bigTriTriangles[2]] == player && triangles[bigTriTriangles[3]] == player) {
						bigTriangles[bigTri] = player;
						points += 250;
					}
				}
			}
							
		} else {
			/*
			Integer[] edge = {0, 1, 2, 3, 6, 7, 11, 12, 15, 16, 17, 18};
			if (!Arrays.asList(edge).contains(new Integer(start))) {
				System.err.format("Node %d is not an edge piece.\n", start);
				return -1;
			}
			*/
			
			int[] coords = getCoords(start);
			
			if (coords[0] != 0 && coords[0] != 4 && coords[1] != 0 && coords[1] != 4 && 
					coords[2] != 0 && coords[2] != 4) {
				System.err.format("Node %d is not an edge piece.\n", start);
				return -1;
			}
			
			if (nodes[start] != -1) {
				System.err.println("Already occupied");
				return -1;
			}
			
			nodes[start] = player;
			
			points += 10;
		}
		
		// Increase points
		score[player] += points;
			
		// Increase Player
		player = (1-player);
		
		if (score[1] == 60) phase2 = true;
			
		// Return Points
		return points;
	}
	
	int[] start = {0, 3, 7, 12, 16};
	
	int[] getCoords(int node) {
		int x=0, y = 0, d=0;
		
		for (int i = 4; i > -1; i--) {
			if (node >= start[i]) {
				y = i;
				break;
			}
		}
		
		x = node - start[y];
		if (y > 2) x += (y - 2);
		
		d = y - x + 2;
		
		return new int[] {x, y, d};
	}
	
	public static int[] convertIntegers(List<Integer> integers) {
	    int[] ret = new int[integers.size()];
	    Iterator<Integer> iterator = integers.iterator();
	    for (int i = 0; i < ret.length; i++)
	    {
	        ret[i] = iterator.next().intValue();
	    }
	    return ret;
	}
	
	int[][] getLegalMovePairs() {
		ArrayList<int[]> legalMovePairs = new ArrayList<int[]>();

		int[] pair = {-1,-1};
		int[] possibleStarts = getLegalMoves();
		for (int start: possibleStarts) {
			int[] possibleEnds = getLegalMoves(start);
			for (int end : possibleEnds) {
				pair[0] = start;
				pair[1] = end;
				legalMovePairs.add(pair.clone());
			}
		}
		
		int[][] ret = new int[legalMovePairs.size()][2];
	    Iterator<int[]> iterator = legalMovePairs.iterator();
	    for (int i = 0; i < ret.length; i++)
	    {
	        ret[i] = iterator.next();
	    }
	    return ret;
	}
	
	int[] getLegalMoves() {
		ArrayList<Integer> legalMoves = new ArrayList<Integer>();
		
		if (phase2) {
			for (int i = 0; i < 19; ++i) {
				if (nodes[i] == player) {
					legalMoves.add(i);
				}
			}
		} else {
			int[] edges = {0, 1, 2, 3, 6, 7, 11, 12, 15, 16, 17, 18};
			
			for (int edge : edges) {
				if (nodes[edge] == -1) {
					legalMoves.add(edge);
				}
			}
		}

	    return convertIntegers(legalMoves);
	}

	int[] getLegalMoves(int start) {
		ArrayList<Integer> legalMoves = new ArrayList<Integer>();
		
		if (phase2) {
			if (nodes[start] == player) {				
				// If only rival pieces between start and end
				boolean inside = false;
				boolean onlyOther = true;
				
				for (Integer[] line : lines) {
					if (Arrays.asList(line).contains(new Integer(start))) {
						for (int end : line) {
							if (nodes[end] == -1 && end != start) {
								inside = false;
								onlyOther = true;
							
								for (int pos : line) {
									if (pos == start || pos == end) {
										inside = !inside;
									} else {
										if (inside && nodes[pos] != (1-player) ) {
											onlyOther = false;
										}
									}
								}
								
								if (onlyOther) {
									legalMoves.add(end);
								}
							}
						}

					}
				}								
			}
		} else {
			if (nodes[start] == -1) {
				legalMoves.add(0);
			}
		}

	    return convertIntegers(legalMoves);
	}

	
	
}

