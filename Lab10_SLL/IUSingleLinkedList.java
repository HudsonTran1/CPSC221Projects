import java.util.*;

/**
 * Single-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author 
 * 
 * @param <E> type to store
 */
public class IUSingleLinkedList<E> implements IndexedUnsortedList<E> {
	private LinearNode<E> front, rear;
	private int count;
	private int modCount;
	private static final int NOT_FOUND = -1;
	
	/** Creates an empty list */
	public IUSingleLinkedList() {
		front = rear = null;
		count = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(E element) {
		add(0, element);
	}

	@Override
	public void addToRear(E element) {
		add(count, element);
	}

	@Override
	public void add(E element) {
		add(count, element);
	}

	@Override
	public void addAfter(E element, E target) {
		int index = indexOf(target);
		if(index == NOT_FOUND) { throw new NoSuchElementException(); }
		add(index + 1, element);		
	}

	@Override
	public void add(int index, E element) {
		if(index < 0 || index > count) { throw new IndexOutOfBoundsException(); } // guard for indices out of bounds
		
		LinearNode<E> temp = new LinearNode<>(element); // new node to add
		LinearNode<E> previous = getLinearNode(index - 1); // node preceding
		LinearNode<E> current = getLinearNode(index); // node originally at the index

		temp.setNext(current); // point the next of the new node to the next node 
		
		// correct front reference or set previous of new node
		if(index == 0) {
			front = temp; // set front to new node
	    } else {
			previous.setNext(temp); // point the next of the previous node to the new node
		}
		// set rear to new node if applicable
		if(index == count) {
			rear = temp;
		}

		temp = null; // null out temp
		count++;
		modCount++;		
	}

	@Override
	public E removeFirst() {
		if(isEmpty()) { throw new NoSuchElementException(); } // guard for empty list
		return removeElement(null, front);
	}

	@Override
	public E removeLast() {
		if (isEmpty()) { throw new NoSuchElementException(); } // guard for empty list
		return removeElement(getLinearNode(count - 2), rear); // previous here is one node before the last, which would be at [count-1]
	}

	@Override
	public E remove(E element) {
		if (isEmpty()) { throw new NoSuchElementException(); } // guard for empty list
		LinearNode<E> current = front, previous = null;
		while (current != null && !current.getElement().equals(element)) {
			previous = current;
			current = current.getNext();
		}
		// Matching element not found
		if (current == null) {
			throw new NoSuchElementException();
		}

		return removeElement(previous, current);		
	}

	@Override
	public E remove(int index) {
		return remove(get(index)); // remove element found with get()
	}

	@Override
	public void set(int index, E element) {
		if(index < 0 || index >= count) { throw new IndexOutOfBoundsException(); } // guard for indices out of bounds

		LinearNode<E> temp = new LinearNode<>(element); // new node to add
		LinearNode<E> previous = getLinearNode(index - 1); // node preceding 
		LinearNode<E> following = getLinearNode(index + 1); // node following

		temp.setNext(following); // point the next of the new node to the following node 

		// correct front reference or set previous of new node
		if(index == 0) {
			front = temp; // set front to new node
	    } else {
			previous.setNext(temp); // point the next of the previous node to the new node
		}
		// set rear to new node if applicable
		if(index == (count - 1)) {
			rear = temp;
		}
		
		temp = null; // null out temp
		modCount++;		
	}

	@Override
	public E get(int index) {
		if(index < 0 || index >= count) { throw new IndexOutOfBoundsException(); } // guard for indices out of bounds
		LinearNode<E> current = front; // node to walk through the linked structure

		// bump current to the next node until node at index is reached
		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}
		
		return current.getElement(); // return the element from the appropriate node
	}

	@Override
	public int indexOf(E element) {
		LinearNode<E> current = front; // node to walk through the linked structure
		
		// check each node for the element
		for(int i = 0; current != null; i++) {
			if (current.getElement().equals(element)) {
				return i; // return the index of the node
			}
			current = current.getNext(); // bump to next node
		}
		return NOT_FOUND; // return constant to denote that the element was not found
	}

	// support method to find node with target element -- prevents unnecessary call into indexOf which is insignificantly more efficient but makes me happy
	private LinearNode<E> getLinearNode(E target) {
		LinearNode<E> current = front; // node to walk through the linked structure
		
		// check each node for the element
		while(current != null) {
			if (current.getElement().equals(target)) {
				return current; // return the node containing the element
			}
		}
		return null; // return null if no node possesses the element
	}

	// support method to find node at index 
	private LinearNode<E> getLinearNode(int index) {
		if(index < 0 || index >= count) { return null; } // guard for invalid indices
		LinearNode<E> current = front; // node to walk through the linked structure
		
		// bump current to the next node until index is reached
		for (int i = 0; i < index; i++) {
			current = current.getNext(); // bump to next node
		}

		return current; // return the node at the index
	}

	@Override
	public E first() {
		if (isEmpty()) { throw new NoSuchElementException(); } // guard for empty list
		return front.getElement();
	}

	@Override
	public E last() {
		if (isEmpty()) { throw new NoSuchElementException(); } // guard for empty list
		return rear.getElement();
	}

	@Override 
	public boolean contains(E target) {
		if(isEmpty()) { return false; } // return early if list is empty
		LinearNode<E> current = front; // node to walk through the linked structure
		
		// bump current to next node until node with element is located
		while(current != null) {
			// check if node contains element
			if(current.getElement().equals(target)) {
				return true;
			}
			current = current.getNext(); // bump to next node
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		return (count == 0); // check if list is empty
	}

	@Override
	public int size() {
		return count; // return the size
	}

	@Override
	public String toString() {
		String result = "[";
		LinearNode<E> current = front;
		while(current != null) {
			result += current.getElement();
			current = current.getNext();
			if(current != null) {
				result += ", ";
			}
		}
		result += "]";
		return result;
	}

	private E removeElement(LinearNode<E> previous, LinearNode<E> current) {
		// Grab element
		E result = current.getElement();
		// If not the first element in the list
		if (previous != null) {
			previous.setNext(current.getNext());
		} else { // If the first element in the list
			front = current.getNext();
		}
		// If the last element in the list
		if (current.getNext() == null) {
			rear = previous;
		}
		count--;
		modCount++;

		return result;
	}

	@Override
	public Iterator<E> iterator() {
		return new SLLIterator();
	}

	/** Iterator for IUSingleLinkedList */
	private class SLLIterator implements Iterator<E> {
		private LinearNode<E> previous;
		private LinearNode<E> current;
		private LinearNode<E> next;
		private int iterModCount;
		boolean nextCalled;
		
		/** Creates a new iterator for the list */
		public SLLIterator() {
			previous = null;
			current = null;
			next = front;
			iterModCount = modCount;
			nextCalled = false;
		}

		@Override
		public boolean hasNext() {
			if(iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
			return next != null;
		}

		@Override
		public E next() {
			if(iterModCount != modCount){
				throw new ConcurrentModificationException();
			}

			if(next == null){
				throw new NoSuchElementException();
			}

			previous = current;
			current = next;
			next = next.getNext();
			nextCalled = true;

			return current.getElement();
		}
		
		@Override
		public void remove() {
			if(interModCount != modCount){
				throw new ConcurrentMOdificationException();
			}
			if(!nextCalled){
				throw new IllegalStateException();
			}

			removeNode(current, previous);
			interModCount++;
			nextCalled = false;
		}
	}

	// IGNORE THE FOLLOWING CODE
	// DON'T DELETE ME, HOWEVER!!!
	@Override
	public ListIterator<E> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<E> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}
}
