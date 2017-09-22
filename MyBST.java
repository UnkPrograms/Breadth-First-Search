
/** Name: Ryunki Song (cantstoptheunk)
 * Instructor: Andrew Predoehl
 * Class: CSC 345
 * Assignment: Project 5 (Breadth First Search)
 * 
 * Description: This class is the core implementation of the project.
 * I used a standard BST to store all the vertex's/stations. 
 * 
 * Each node contains a key value called "record", satellite data,
 * an array list of its neighbors/adjacent nodes, a boolean value 
 * to see if the node has been visited when performing BFS, and 
 * links to its left/right child and its parent.
 * 
 * IMPORTANT NOTE: left and right links are used for the BST implementation
 * of the vertex's. However, the parent link is specifically used only when
 * performing BFS.
 * In other words, since the BST part only stores the vertex's, the nodes don't
 * need to know their parents in the BST. 
 * But in BFS, children must know their parents, thus, I have a parent link 
 * in the private Node class.
 * 
 * Also I implemented the DELETE method for extra credit
 */
import java.awt.Point;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

public class MyBST {

	private class Node {
		public Point record;
		public String satellite;
		public ArrayList<Node> neighborList;
		public Node left, right, parent;
		public boolean visited;

		Node(Point rec, String sat) {
			record = rec;
			satellite = sat;
			visited = false;
			neighborList = new ArrayList<Node>();
			left = null;
			right = null;
			parent = null;
		}

		public String toString() {
			return "record = " + record + ", satellite = " + satellite;
		}
	}

	// The external reference starting point
	Node root;
	private ArrayList<Node> shortestPathList = new ArrayList<Node>();
	private ArrayList<Node> queueList = new ArrayList<Node>();

	/** Basic BST implementation **/
	/** Basic standard insert method in BST **/
	public void insert(Point key, String sat) {
		// Create node
		Node node = new Node(key, sat);
		// If root is null, then the node becomes the root
		if (root == null) {
			root = node;
			return;
		}
		Node current = root;
		Node parent = null;
		while (true) {
			parent = current;
			if (compare(current.record, key) == 0) {
				current.satellite = sat;
				return;
			} else if (compare(current.record, key) < 0) {
				current = current.left;
				if (current == null) {
					parent.left = node;
					return;
				}
			} else {
				current = current.right;
				if (current == null) {
					parent.right = node;
					return;
				}
			}
		}
	}

	/** Basic standard search method in BST **/
	public boolean search(Point point) {
		return searchHelper(point, root);
	}
	private boolean searchHelper(Point point, Node node) {
		if (node == null)
			return false;
		else if (compare(node.record, point) > 0)
			return searchHelper(point, node.right);
		else if (compare(node.record, point) < 0)
			return searchHelper(point, node.left);
		else
			return true;
	}

	/** Returns the Node matching the key value **/
	public Node get(Point point) {
		return getHelper(point, root);
	}
	private Node getHelper(Point point, Node node) {
		if (compare(node.record, point) > 0)
			return getHelper(point, node.right);
		else if (compare(node.record, point) < 0)
			return getHelper(point, node.left);
		else
			return node;
	}
	/** End Basic BST implementation **/

	/** Begin BFS implementation **/
	public void adjacent(Point loc1, Point loc2) {
		// Get the nodes with the corresponding location
		Node node1 = get(loc1);
		Node node2 = get(loc2);
		//Now add the nodes to their neighborList
		node1.neighborList.add(node2);
		node2.neighborList.add(node1);
	}

	public String route(Point start, Point finish) {
		// Get the nodes with the corresponding location
		Node nodeS = get(start);
		Node nodeF = get(finish);
		
		//Take the starting node and set it to visited and add it to the queue.
		nodeS.visited = true;
		nodeS.parent = null;
		queueList.add(nodeS);
		return BFS(nodeS, nodeF);
	}

	public String BFS(Node nodeS, Node nodeF) {
		//We have our initial starting queue node
		while (queueList.size() > 0) {
			//Grab the first node in the queue
			nodeS = queueList.get(0);

			// Immediately check if the current Node is equal to the end node
			// If equal, then we can immediately start printing the path.
			if (compare(nodeS.record, nodeF.record) == 0)
				return printPath(nodeS);
			//Otherwise, go through the neighborList of the node in queue
			for (Node node : queueList.get(0).neighborList) {
				//If neighbor has not been visited, then set true and set it's parent 
				//to the node in queue. Then add that node to the queue
				if (!node.visited) {
					node.visited = true;
					node.parent = queueList.get(0);
					queueList.add(node);
				}
				//Otherwise, node has been visited so do nothing and iterate again
			}
			//We are done with the node, so remove it form queue
			queueList.remove(0);
		}
		//No match was found
		return "NO PATH\n";
	}

	/** This is where we construct the String output once we have 
	 * found the end node
	 */
	public String printPath(Node nodeS) {
		// We found a path, so now just go up the path using node.parent
		// NOTE: The only node without a parent will be the starting node
		while (nodeS.parent != null) {
			shortestPathList.add(nodeS);
			nodeS = nodeS.parent;
		}
		// Add the starting node to the list
		shortestPathList.add(nodeS);

		//We have to grab the Nodes from the end of the array to the beginning
		String result = "";
		while (shortestPathList.size() > 0) {
			result += "Station " + (int) (shortestPathList.get(shortestPathList.size() - 1).record.getX()) + ", "
					+ (int) (shortestPathList.get(shortestPathList.size() - 1).record.getY()) + " "
					+ shortestPathList.get(shortestPathList.size() - 1).satellite + "\n";

			shortestPathList.remove(shortestPathList.size() - 1);
		}
		return result;
	}

	public void delete(Point loc){
		// Overall strategy: Since edges are two way streets, just find the node
		// to be deleted, find it's neighbors and delete the node from it's
		// neighbor's list. Then, delete all the neighbor's in the deleted node's 
		// list. 
		Node node = get(loc);
		for(Node nodeTemp : node.neighborList){
			nodeTemp.neighborList.remove(node);
		}
		node.neighborList = new ArrayList<Node>();
	}
	
	/** Cleaning up the BST found here **/
	/*
	 * Main purpose is to reset the parent links and visited boolean values for
	 * each node in the graph and reset the Array Lists (queueList and
	 * shortestPathList)
	 */
	public void clean() {
		resetBST(); // Reset the node values to its default state
		// Reset the Array Lists
		queueList = new ArrayList<Node>();
		shortestPathList = new ArrayList<Node>();
	}

	private void resetBST() {
		resetBST(root);
	}
	private void resetBST(Node node) {
		// Use inOrder traversal to reset nodes to its default state
		if (node != null) {
			resetBST(node.left);
			node.parent = null;
			node.visited = false;
			resetBST(node.right);
		}
	}

	/** End BFS implementation **/
	/* Basic compareTo method that I wrote for my own purposes */
	public int compare(Point pointP, Point pointC) {
		if (pointP.y > pointC.y)
			return -1;
		else if (pointP.y < pointC.y)
			return 1;
		else {
			if (pointP.x > pointC.x)
				return -1;
			else if (pointP.x < pointC.x)
				return 1;
			else
				return 0;
		}
	}
}