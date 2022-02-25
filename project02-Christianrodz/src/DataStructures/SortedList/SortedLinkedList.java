package SortedList;


/**
 * Implementation of a SortedList using a SinglyLinkedList
 * @author Fernando J. Bermudez & Juan O. Lopez
 * @author Christian G Rodriguez Berrios
 * @version 2.0
 * @since 10/16/2021
 */
public class SortedLinkedList<E extends Comparable<? super E>> extends AbstractSortedList<E> {

	@SuppressWarnings("unused")
	private static class Node<E> {

		private E value;
		private Node<E> next;

		public Node(E value, Node<E> next) {
			this.value = value;
			this.next = next;
		}

		public Node(E value) {
			this(value, null); // Delegate to other constructor
		}

		public Node() {
			this(null, null); // Delegate to other constructor
		}

		public E getValue() {
			return value;
		}

		public void setValue(E value) {
			this.value = value;
		}

		public Node<E> getNext() {
			return next;
		}

		public void setNext(Node<E> next) {
			this.next = next;
		}

		public void clear() {
			value = null;
			next = null;
		}				
	} // End of Node class

	
	private Node<E> head; // First DATA node (This is NOT a dummy header node)
	
	public SortedLinkedList() {
		head = null;
		currentSize = 0;
	}

	/**
	 * Adds a given element to the SortedLinkedList, 
	 * keeping the list in ascending order
	 * 
	 * @param e element to add to the list
	 */
	@Override
	public void add(E e) {
		/* Special case: Be careful when the new value is the smallest */
		Node<E> newNode = new Node<>(e);
		if(size() == 0) {
			head = newNode;
			currentSize++;
		}else {
			Node<E> curNode = head;
			if(size() == 1) {//Only one element
				if(head.getValue().compareTo(e) >= 0) { //Head Bigger than element
					newNode.setNext(head);
					head = newNode;
				}else { //Head Smaller than element
					head.setNext(newNode);
				}
				currentSize++;
			}else {//More than 1 element
				if(head.getValue().compareTo(e) >= 0) {//Special case when new Value is smallest
					newNode.setNext(head);
					head = newNode;
					currentSize++;
				}else {//Go through list to find the correct slot
					while(curNode.getNext() != null && curNode.getNext().getValue().compareTo(e) < 0) {
						curNode = curNode.getNext();
					}//Exits loop at the end or when the next node's value is bigger than the element
					if(curNode.getNext() == null) {
						curNode.setNext(newNode);
						currentSize++;
					}else {
						newNode.setNext(curNode.getNext());
						curNode.setNext(newNode);
						currentSize++;
					}
				}
				
			}
		}
		

	}
	
	/**
	 * Attempts to remove the first instance of a given element from the list,
	 * and returns a boolean indicating if it could do so
	 * 
	 * @param e element to remove
	 * @return boolean indicating if it removed the element
	 */
	@Override
	public boolean remove(E e) {
		/* Special case: Be careful when the value is found at the head node */
		Node<E> curNode, nextNode;
		if(size() == 1) {//If there's only one element, check to see if thats the element to remove
			if(head.getValue().equals(e)) {
				head.clear();//free resources
				currentSize--;
				return true;
			}
		}else {//Size is larger than one
			if(head.getValue().equals(e)) {//Check if the value is at the head
				//remove it while making the next value the new head
				nextNode = head.getNext();
				head.clear();
				head = nextNode;
				currentSize--;
				return true;
			}
			curNode = head;
			nextNode = curNode.getNext();
			//Otherwise, loop through list until we find the element or reach the end
			while (nextNode != null && !nextNode.getValue().equals(e)) {
				curNode = nextNode;
				nextNode = nextNode.getNext();
			}
			//Check if we found the element and if we did remove it
			if (nextNode != null) { 
				curNode.setNext(nextNode.getNext());
				nextNode.clear();
				currentSize--;
				return true;
			}
		}
		
		return false; //If elm was not found, return false
	}

	/**
	 * Remove element at a given index and return the element
	 * This method is utilized to build the huffman tree
	 * 
	 * @param index position of element to be removed
	 * @return element that was removed
	 */
	@Override
	public E removeIndex(int index) {
		/* Special case: Be careful when index = 0 */
		Node<E> rmNode, curNode;
		E value = null;
		//confirm index is a valid position
		if(index < 0 || index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		//Special case, if index is 0 move the head over one spot and free resources
		if(index == 0) {
			value = head.getValue();
			Node<E> temp = head;
			head = head.getNext();
			temp.clear();
			currentSize--;
		}else {
			curNode = head;
			//Traverse the list until the currentNode is right before the element to be removed
			for(int curPos = 0; curPos < index-1;curPos++) {
				curNode = curNode.getNext();
			}
			//Remove node next to the current Node and store the value of the element
			rmNode=curNode.getNext();
			value = rmNode.getValue();
			curNode.setNext(rmNode.getNext());
			rmNode.clear();
			currentSize--;
		}
		
		
		
		return value;
	}
	/**
	 * Finds and returns the first position of a given element
	 * 
	 * @param e element to find in the list
	 * @return position of element 
	 */
	@Override
	public int firstIndex(E e) {
		int target = 0;
		Node<E> curNode = head;
		//Loop through the list incrementing the counter until it finds the element or reaches the end
		while(curNode != null && !curNode.getValue().equals(e)) {
			target++;
			curNode = curNode.getNext();
		}
		//if element was found, return the position of the element
		if(curNode != null) {
			return target;
		}
		
		return -1; //If element was not on the list, return -1
	}
	
	/**
	 * Retrieves element in given position
	 * 
	 * @param index position to find the given element
	 * @return element at given position
	 */
	@Override
	public E get(int index) {
		if(index < 0 || index >= size() ) {//Check if given position is valid
			throw new IndexOutOfBoundsException();
		}
		Node<E> curNode = head;
		//Loop through the list until we find the given position
		for(int curPos = 0; curPos < index; curPos++) {
			curNode = curNode.getNext();
		}
		return curNode.getValue(); //Return the element
	}

	

	@SuppressWarnings("unchecked")
	@Override
	public E[] toArray() {
		int index = 0;
		E[] theArray = (E[]) new Comparable[size()]; // Cannot use Object here
		for(Node<E> curNode = this.head; index < size() && curNode  != null; curNode = curNode.getNext(), index++) {
			theArray[index] = curNode.getValue();
		}
		return theArray;
	}

}
