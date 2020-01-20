package comp2402a4;

import java.lang.reflect.Array;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

/**
* An unfinished implementation of an Countdown tree (for exercises)
* @author morin
*
* @param <T>
*/




/*
Basic Thinking:
	-Every node has timer
	Add:
		-When a node is added, every node on the path from the new node to root has it's timer decremented by 1
			-If any of there times reach 0, explode them and rebuild
	-Remove: 
		-After you remove the node, walk the path back to root, and decrement every nodes timer by 1
			-If any of them reach 0, explode and rebuild
	
	-Rebuild:
		-Pack in to array
		-Follow standard algorithm to rebuild perfect binary tree (Use Algorith implemeneted in scapegoattree)
		-

	-Helper Methods:
		-void walkUpandCount(Node u)
			-u is the parent of the node that we are either removing or adding
			-Function should walk up till it reaches nil decrementing each timer and checking if it needs to be exploded

		-packIntoArray(Node u, a)
	
*/


public class CountdownTree<T> extends
BinarySearchTree<CountdownTree.Node<T>, T> implements SSet<T> {

	// countdown delay factor
	double d;

	//Node sublass
	public static class Node<T> extends BSTNode<Node<T>,T> {
		int timer;  // the height of the node
	}

	//Default constructor
	public CountdownTree(double d) {
		this.d = d;
		sampleNode = new Node<T>();
		c = new DefaultComparator<T>();
	}

	//Should be Done, Just added walk up and count
	public boolean add(T x) {
		Node<T> u = new Node<T>();
		u.timer = (int)Math.ceil(d);
		u.x = x;
		if (super.add(u)) {
			walkUpandCount(u.parent);
			return true;
		}
		return false;
	}


	public void splice(Node<T> u) {
		Node<T> w = u.parent;
		super.splice(u);
		// add some code here (we just removed u from the tree)
		walkUpandCount((w));
	}

	@SuppressWarnings("unchecked")
	protected void explode(Node<T> u) {
		// Write this code to explode u
		// Make sure to update u.parent and/or r (the tree root) as appropriate

		//Make sure we know parent and size of new sublist
		Node<T> parentNode = u.parent;
		int ns = size(u);

		//Pack into array
		Node<T>[] a  = (Node<T>[]) Array.newInstance(Node.class, ns);
		this.packIntoArray(u, a, 0);

		//Reset all their times
		int timerSize = (int) Math.ceil(a.length*d);
		for(int i = 0; i<a.length; i++){
			a[i].timer = timerSize;
		}


		//parent Nil means that u is the root, so add new list at root
		if(parentNode == nil){
			r = buildBalanced(a, 0, ns);
			r.parent = nil;
		}
		//if u was the right child of parent, add new list here
		else if(parentNode.right == u){
			parentNode.right = buildBalanced(a, 0, ns);
			parentNode.right.parent = parentNode;
		}
		//otherwise u was the left child of parent
		else{
			parentNode.left = buildBalanced(a, 0, ns);
			parentNode.left.parent = parentNode;
		}



	}

	//Turns tree rooted at node u in to a list
	private int packIntoArray(Node<T> u, Node<T>[]a, int i){
		
		if(u == nil){
			return i;
		}
		
		//Add the left child
		i = packIntoArray(u.left, a, i);
	
		//Add the current node
		a[i++] = u;
		
		//Add the right child
		return packIntoArray(u.right, a, i);
	}

	//Theoretical method to rebuilt a tree balanced
	protected Node<T> buildBalanced(Node<T>[] a, int i, int ns) {
		if (ns == 0)
			return nil;
		int m = ns / 2;
		a[i + m].left = buildBalanced(a, i, m);
		if (a[i + m].left != nil)
			a[i + m].left.parent = a[i + m];
		a[i + m].right = buildBalanced(a, i + m + 1, ns - m - 1);
		if (a[i + m].right != nil)
			a[i + m].right.parent = a[i + m];
		return a[i + m];
	}

	//Walks up from a node to the root, decreasing each nodes timer
	private void walkUpandCount(Node<T> u){
		//Follow the path to the root
		while(u != nil){
			//Decrement Timer
			u.timer--;
			//Check if explode is necessary
			if(u.timer == 0){
				explode(u);
			}

			//Move up one level
			u = u.parent;	
		}
	}




	// Just main
	public static void main(String[] args) {

		/*
		Testum.sortedSetSanityTests(new SortedSSet<Integer>(new CountdownTree<Integer>(1)), 1000);
		Testum.sortedSetSanityTests(new SortedSSet<Integer>(new CountdownTree<Integer>(2.5)), 1000);
		Testum.sortedSetSanityTests(new SortedSSet<Integer>(new CountdownTree<Integer>(0.5)), 1000);

		java.util.List<SortedSet<Integer>> ell = new java.util.ArrayList<SortedSet<Integer>>();
		ell.add(new java.util.TreeSet<Integer>());
		ell.add(new SortedSSet<Integer>(new CountdownTree<Integer>(1)));
		ell.add(new SortedSSet<Integer>(new CountdownTree<Integer>(2.5)));
		ell.add(new SortedSSet<Integer>(new CountdownTree<Integer>(0.5)));
		Testum.sortedSetSpeedTests(ell, 1000000);
		


		CountdownTree<Integer> t1 = new CountdownTree<>(2.5);

	
		t1.add(20);
		t1.add(40);
		t1.add(60);
		t1.add(10);
		t1.add(5);

		System.out.println(t1.r.x);
		*/

	}
}
