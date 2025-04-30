import java.util.*;

public class IUDoubleLinkedList<E> implements IndexedUnsortedList<E>{
    private BidirectionalNode<E> front, rear;
	private int count;
	private int modCount;
	private int iterModCount, listIterModCount;
	private static final int NOT_FOUND = -1;

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
		
		BidirectionalNode<E> temp = new BidirectionalNode<>(element); // new node to add
		BidirectionalNode<E> current = getBidirectionalNode(index); // node originally at the index

		temp.setNext(current); // point the next of the new node to the next node 
		
		// correct front reference or set previous of new node
		if(index == 0) {
			front = temp; // set front to new node
	    } else {
			current.getPrevious().setNext(temp); // point the next of the previous node to the new node
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
		return removeElement(front);
	}

	@Override
	public E removeLast() {
		if (isEmpty()) { throw new NoSuchElementException(); } // guard for empty list
		return removeElement(rear); // previous here is one node before the last, which would be at [count-1]
	}

	@Override
	public E remove(E element) {
		if (isEmpty()) { throw new NoSuchElementException(); } // guard for empty list
		BidirectionalNode<E> current = front;
		while (current != null && !current.getElement().equals(element)) {
			current = current.getNext();
		}
		// Matching element not found
		if (current == null) {
			throw new NoSuchElementException();
		}

		return removeElement(current);		
	}

	@Override
	public E remove(int index) {
		return remove(get(index)); // remove element found with get()
	}

    private E removeElement(BidirectionalNode<E> current) {
		// Grab element
		E result = current.getElement();
		// If not the first element in the list
		if (current.getPrevious() != null) {
			current.getPrevious().setNext(current.getNext());
		} else { // If the first element in the list
			front = current.getNext();
		}
		// If the last element in the list
		if (current.getNext() == null) {
			rear = current.getPrevious();
		}
		count--;
		modCount++;

		return result;
	}
    @Override
	public void set(int index, E element) {
		if(index < 0 || index >= count) { throw new IndexOutOfBoundsException(); } // guard for indices out of bounds

		BidirectionalNode<E> temp = new BidirectionalNode<>(element); // new node to add
		BidirectionalNode<E> previous = getBidirectionalNode(index - 1); // node preceding 
		BidirectionalNode<E> following = getBidirectionalNode(index + 1); // node following

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
		BidirectionalNode<E> current = front; // node to walk through the linked structure

		// bump current to the next node until node at index is reached
		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}
		
		return current.getElement(); // return the element from the appropriate node
	}

	@Override
	public int indexOf(E element) {
		BidirectionalNode<E> current = front; // node to walk through the linked structure
		
		// check each node for the element
		for(int i = 0; current != null; i++) {
			if (current.getElement().equals(element)) {
				return i; // return the index of the node
			}
			current = current.getNext(); // bump to next node
		}
		return NOT_FOUND; // return constant to denote that the element was not found
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
		BidirectionalNode<E> current = front; // node to walk through the linked structure
		
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
		BidirectionalNode<E> current = front;
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

    @Override
    public Iterator<E> iterator() {
        return new DLLIterator();
    }

    @Override
    public ListIterator<E> listIterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    @Override
    public ListIterator<E> listIterator(int startingIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listIterator'");
    }

    // support method to find node at index 
	private BidirectionalNode<E> getBidirectionalNode(int index) {
		if(index < 0 || index >= count) { return null; } // guard for invalid indices
		// logic to start at the beginning or end of the list, whichever is more efficient
        BidirectionalNode<E> current; // node to walk through the linked structure
        if(index <= count/2) { // if index is close to the start            
            current = front; // start at front
            // bump current to the next node until index is reached
            for (int i = 0; i < index; i++) {
                current = current.getNext(); // bump to next node
		    }
        } else { // if index is not close to the start
            current = rear; // start at rear
            // bump current to the next node until index is reached
            for (int i = count; i > index; i--) {
                current = current.getPrevious(); // bump to next node
		    }
        }

		return current; // return the node at the index
	}

    private class DLLIterator implements Iterator<E> {
        private BidirectionalNode<E> previous;
		private BidirectionalNode<E> current;
		private BidirectionalNode<E> next;
		private int iterModCount;
		boolean canRemove;
		
		/** Creates a new iterator for the list */
		public DLLIterator() {
			previous = null;
			current = null;
			next = front;
			iterModCount = modCount;
			canRemove = false;
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
			canRemove = true;

			return current.getElement();
		}
		
		@Override
		public void remove() {
			if(iterModCount != modCount){
				throw new ConcurrentModificationException();
			}
			
			if(!canRemove){
				throw new IllegalStateException();
			}

			removeElement(current);
			current = previous; //to account for the shift
			iterModCount++;
			canRemove = false; 
		}
	}
}
