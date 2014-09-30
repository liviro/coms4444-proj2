package offset.group4;

import java.util.*;

import offset.sim.Pair;
import offset.sim.Point;
import offset.sim.movePair;

public class Player extends offset.sim.Player {
	int size = 32;
	public Player(Pair prin, int idin) {
		super(prin, idin);
		// TODO Auto-generated constructor stub
	}

	public void init() {

	}
	
	public ArrayList<Point> giveValidPts(Pair pr, Point orig, Point[] grid) {
		ArrayList<Point> valid = new ArrayList<Point>();
		Point newPt = new Point();
		for(int ordr = 0; ordr <=1; ordr++) {
			for(int sgn1 = -1; sgn1 <=1; sgn1 += 2) {
				for(int sgn2 = -1; sgn2 <=1; sgn2 += 2) {
					// create new point
					if(ordr == 0) {
						newPt.x = orig.x + sgn1*pr.p;
						newPt.y = orig.y + sgn2*pr.q;
					} else {
						newPt.x = orig.x + sgn1*pr.q;
						newPt.y = orig.y + sgn2*pr.p;
					}
					// check if valid
					if(newPt.x >= 0 && newPt.x < size && newPt.y >= 0 && newPt.y < size 
							// why is this one not working...?
							&& grid[newPt.x*size+newPt.y].value == grid[orig.x*size+orig.y].value
							&& grid[orig.x*size+orig.y].value > 0) {
						// add to list
						System.out.println("ADDED:");
						System.out.println(orig.x + ", "+  orig.y);
						System.out.println(newPt.x + ", " + newPt.y);
						valid.add(newPt);
					}
				}
			}
		}
		return valid;	
	}

	public movePair move(Point[] grid, Pair pr, Pair pr0, ArrayList<ArrayList> history) {
		movePair movepr = new movePair();
		// Manhattan distance from corners
		for(int d = 0; d < size; d++) {
			for(int cellNum = 0; cellNum < d; cellNum++) {
				int x = d-cellNum;
				int y = cellNum;
				ArrayList<Point> valid = giveValidPts(pr, new Point(x, y, 1, -1), grid);
				// there is more than 1 valid point for that point...
				if(!valid.isEmpty()) {
					movepr.move = true;
					movepr.src = grid[valid.get(0).x * size + valid.get(0).y];
					movepr.target = grid[x * size + y];
					System.out.println("CHOSEN:");
					System.out.println(movepr.src.x + ", "+  movepr.src.y);
					System.out.println(movepr.target.x + ", " + movepr.target.y);
					return movepr;
				}
			}
			
		}
//		for (int i = 0; i < size; i++) {
//			for (int j = 0; j < size; j++) {
//				for (int i_pr=0; i_pr<size; i_pr++) {
//					for (int j_pr=0; j_pr <size; j_pr++) {
//						movepr.move = false;
//						movepr.src = grid[i*size+j];
//						movepr.target = grid[i_pr*size+j_pr];
//						if (validateMove(movepr, pr)) {
//							movepr.move = true;
//							return movepr;
//						}
//					}
//				}
//			/*	if (i + pr.x >= 0 && i + pr.x < size) {
//					if (j + pr.y >= 0 && j + pr.y < size) {
//						
//					}
//					if (j - pr.y >= 0 && j - pr.y < size) {
//
//					}
//				}
//				if (i - pr.x >= 0 && i - pr.x < size) {
//					if (j + pr.y >= 0 && j + pr.y < size) {
//
//					}
//					if (j - pr.y >= 0 && j - pr.y < size) {
//
//					}
//				}
//				if (i + pr.y >= 0 && i + pr.y < size) {
//					if (j + pr.x >= 0 && j + pr.x < size) {
//
//					}
//					if (j - pr.x >= 0 && j - pr.x < size) {
//
//					}
//				}
//				if (i - pr.y >= 0 && i - pr.y < size) {
//					if (j + pr.x >= 0 && j + pr.x < size) {
//
//					}
//					if (j - pr.x >= 0 && j - pr.x < size) {
//
//					}
//				}
//*/
//			}
//		}
		return movepr;
	}


boolean validateMove(movePair movepr, Pair pr) {
    	
    	Point src = movepr.src;
    	Point target = movepr.target;
    	boolean rightposition = false;
    	if (Math.abs(target.x-src.x)==Math.abs(pr.p) && Math.abs(target.y-src.y)==Math.abs(pr.q)) {
    		rightposition = true;
    	}
    	//if (Math.abs(target.x-src.x)==Math.abs(pr.y) && Math.abs(target.y-src.y)==Math.abs(pr.x)) {
    		//rightposition = true;
    	//}
        if (rightposition  && src.value == target.value && src.value >0) {
        	return true;
        }
        else {
        	return false;
        }
    }
}