
/** Name: Ryunki Song (cantstoptheunk)
 * Instructor: Andrew Predoehl
 * Class: CSC 345
 * Assignment: Project 5 (Breadth First Search)
 * 
 * Description The filled out skeleton file of the project.
 * NOTE: The implementation of the method was handled in the MyBST class.
 * NOTE: I implemented the DELETE method for extra credit
 */

import java.awt.Point;
import java.lang.Comparable;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.lang.Math;
import java.lang.InstantiationException;

class SubwayGraph {

	MyBST BST;
	
	// construct an empty graph
	public SubwayGraph() {
		BST = new MyBST();
	}

	// Add vertex to the graph
	// (Point class is from project 4)
	public void insert(Point loc, String sat) {
		BST.insert(loc, sat);
	}

	// Add edge to the graph
	public void adjacent(Point loc1, Point loc2) {
		if(!BST.search(loc1) || !BST.search(loc2))
			return;
		BST.adjacent(loc1, loc2);
	}

	// Try to find a shortest path between vertices
	public String route(Point start, Point finish) {
		BST.clean();
		if(!BST.search(start) || !BST.search(finish)){
			String str = "NO PATH\n";
			return str.trim();
		}
		return BST.route(start, finish).trim();
	}

	// extra credit -- remove a vertex and all incident edges
	public void delete(Point loc) {
		BST.delete(loc);
	}
}
