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
		LinearNode<E> tempNode = new LinearNode<E>(element);
		if (isEmpty()) {
			rear = tempNode;
		} else {
			tempNode.setNext(front);
		}
		front = tempNode;
		tempNode = null;
		count++;
		modCount++;
		
	}

	@Override
	public void addToRear(E element) {
		add(element);
	}

	@Override
	public void add(E element) {
		LinearNode<E> tempNode = new LinearNode<E>(element);
		if (isEmpty()) {
			front = tempNode;
		} else {
			rear.setNext(tempNode);
		}
		rear = tempNode;
		tempNode = null;
		count++;
		modCount++;
	}

	@Override
	public void addAfter(E element, E target) {
		int index = indexOf(target);
		if(index == NOT_FOUND) { throw new NoSuchElementException(); }
		add(index + 1, element);		
	}

	@Override
	public void add(int index, E element) {
		if(index < 0 || index > count) { throw new IndexOutOfBoundsException(); }
		LinearNode<E> temp = new LinearNode<>(element);
		LinearNode<E> previous = getLinearNode(index - 1);
		LinearNode<E> current = getLinearNode(index);

		if(previous != null) {
			temp.setNext(current); // point the next of the new node to the next node 
			previous.setNext(temp); // point the next of the previous node to the new node
	    }
		count++;
		modCount++;		
	}

	@Override
	public E removeFirst() {
		return first();
	}

	@Override
	public E removeLast() {
		return last();
	}

	@Override
	public E remove(E element) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		LinearNode<E> current = front, previous = null;
		while (current != null && !current.getElement().equals(element)) {
			previous = current;
			current = current.getNext();
		}
		// Matching element not found
		if (current == null) {
			throw new NoSuchElementException();
		}
		modCount++;
		return removeElement(previous, current);		
	}

	@Override
	public E remove(int index) {
		return remove(get(index));
	}

	@Override
	public void set(int index, E element) {
		if(index < 0 || index >= count) { throw new IndexOutOfBoundsException(); }
		LinearNode<E> temp = new LinearNode<>(element);
		LinearNode<E> previous = getLinearNode(index - 1);
		LinearNode<E> following = getLinearNode(index + 1);
		if(previous != null) {
			temp.setNext(following); // point the next of the new node to the next node 
			previous.setNext(temp); // point the next of the previous node to the new node
		}
		
		modCount++;		
	}

	@Override
	public E get(int index) {
		if(index < 0 || index >= count) { throw new IndexOutOfBoundsException(); }
		if(front == null) {throw new IndexOutOfBoundsException(); } //TODO
		LinearNode<E> current = front;

		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}

		if(current.getElement() == null) {
			throw new NoSuchElementException();
		} else {
			return current.getElement();
		}
	}

	@Override
	public int indexOf(E element) {
		LinearNode<E> current = this.front;
		
		for(int i = 0; current != null; i++, current = current.getNext()) {
			if (current.getElement().equals(element)) {
				return i;
			}
		}
		return NOT_FOUND;
	}

	//support method to find node with target element -- prevents unnecessary call into indexOf which is insignificantly more efficient but makes me happy
	private LinearNode<E> getLinearNode(E target) {
		LinearNode<E> current = this.front;
		
		while(current != null) {
			if (current.getElement().equals(target)) {
				return current;
			}
		}
		return null;
	}

	private LinearNode<E> getLinearNode(int index) {
		LinearNode<E> current = this.front;

		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}

		return current;
	}

	@Override
	public E first() {
		if (isEmpty()) { throw new NoSuchElementException(); } //TODO fix
		return front.getElement();
	}

	@Override
	public E last() {
		if (isEmpty()) { throw new NoSuchElementException(); }
		return rear.getElement();
	}

	@Override 
	public boolean contains(E target) {
		LinearNode<E> current = this.front;
		while(current != null) {
			if(current.getElement().equals(target)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		return (count == 0);
	}

	@Override
	public int size() {
		return this.count;
	}

	@Override
	public String toString() {
		String result = "[";
		LinearNode<E> current = this.front;
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
		
		/** Creates a new iterator for the list */
		public SLLIterator() {
			previous = null;
			current = null;
			next = front;
			iterModCount = modCount;
		}

		@Override
		public boolean hasNext() {
			// TODO 
			return false;
		}

		@Override
		public E next() {
			// TODO 
			return null;
		}
		
		@Override
		public void remove() {
			// TODO
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
