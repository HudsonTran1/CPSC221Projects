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
		BidirectionalNode<E> previous = getPreceding(current, index); // node preceding current
		
		temp.setNext(current); // set next of new node
		temp.setPrevious(previous); // set previous of new node

		// correct front reference or set previous of new node
		if(index == 0) { // if at front
			front = temp; // set front to new node
	    } else { // if not front
			previous.setNext(temp); // point the next of the previous node to the new node
		}
		// set rear to new node if applicable
		if(index == count) { // if at rear
			rear = temp; // set rear
		} else { // if not at rear
			current.setPrevious(temp); // set previous of current to new node
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

	@Override
	public void set(int index, E element) {
		if(index < 0 || index >= count) { throw new IndexOutOfBoundsException(); } // guard for indices out of bounds

		BidirectionalNode<E> temp = new BidirectionalNode<>(element); // new node to add
		BidirectionalNode<E> current = getBidirectionalNode(index); // current node
		BidirectionalNode<E> previous = getPreceding(current, index);
		BidirectionalNode<E> following = getFollowing(current, index);

		temp.setNext(following); // point the next of the new node to the following node 
		temp.setPrevious(previous); // set the preious of the new node

		// correct front reference or set previous of new node
		if(index == 0) {
			front = temp; // set front to new node
	    } else {
			previous.setNext(temp); // point the next of the previous node to the new node
		}
		// set rear to new node if applicable
		if(index == (count - 1)) {
			rear = temp;
		} else {
			following.setPrevious(temp); // set following's previous
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
	
	private E removeElement(BidirectionalNode<E> current) {
		// Grab element
		E result = current.getElement();
		BidirectionalNode<E> previous = current.getPrevious();
		BidirectionalNode<E> following = current.getNext();
		// If not the first element in the list
		if (previous == null) {
			front = following;			
		} else { // If the first element in the list
			previous.setNext(following);
		}
		// If the last element in the list
		if (following == null) {
			rear = previous;
		} else {
			following.setPrevious(previous);
		}
		count--;
		modCount++;

		return result;
	}

    @Override
    public Iterator<E> iterator() {
        return new DLLIterator();
    }

    public ListIterator<E> listIterator() {
        return new DLLListIterator(0);
    }

    @Override
    public ListIterator<E> listIterator(int startingIndex) {
        return new DLLListIterator(startingIndex);
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
		// Unused:
		// current = front; // start at front
		// // bump current to the next node until index is reached
		// for (int i = 0; i < index; i++) {
		// 	current = current.getNext(); // bump to next node
		// }

		return current; // return the node at the index
	}

	//private support method to easily return previous node or null
	private BidirectionalNode<E> getPreceding(BidirectionalNode<E> current, int index) { 
		BidirectionalNode<E> result;
		try {
			result = current.getPrevious();
		} catch (Exception e) {
			result = getBidirectionalNode(index-1);
		}
		return result;
	}
	//private support method to easily return next node or null
	private BidirectionalNode<E> getFollowing(BidirectionalNode<E> current, int index) { 
		BidirectionalNode<E> result;
		try {
			result = current.getNext();
		} catch (Exception e) {
			result = getBidirectionalNode(index+1);
		}
		return result;
	}

    private class DLLListIterator implements ListIterator<E>{
        BidirectionalNode<E> current, next;
        int nextIndex;
        int interModCount;
        boolean removeCalled, addCalled;
		ListIteratorState state = ListIteratorState.NONE;

		DLLListIterator(int index){
			if(index < 0 || index > count) { throw new IndexOutOfBoundsException();}
            if(index == count){
                next = null;
            }
            else{
                next = getBidirectionalNode(index);
            }
            current = null;
            nextIndex = index;
            interModCount = modCount;
            removeCalled = addCalled = false;

        }

        private void  checkForModification(){
            if(interModCount != modCount){
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public boolean hasNext() {
            checkForModification();
            return nextIndex < count;
        }

        @Override
        public E next() {
            checkForModification();
            if(!hasNext()){
                throw new NoSuchElementException();
            }

            current = next;
            next = next.getNext();
            nextIndex++;
            removeCalled = addCalled = false;

            return current.getElement();
        }

        @Override
        public boolean hasPrevious() {
            checkForModification();
            return nextIndex > 0;
        }

        @Override
        public E previous() {
            checkForModification();
            if(!hasPrevious()){
                throw new NoSuchElementException();
            }

			current = getPreceding(current, nextIndex);

            if(next == null){
                next = rear;
            }
            else{
          		next = next.getPrevious();
            }
			
            nextIndex--;
            removeCalled = addCalled = false;
			state = ListIteratorState.PREVIOUS;

            return current.getElement();
        }

        @Override
        public int nextIndex() {
            checkForModification();
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            checkForModification();
            return nextIndex - 1;
        }

        @Override
        public void remove() {
            checkForModification();
            if(current == null || addCalled){
                throw new IllegalStateException();
            }

            removeElement(current);
            iterModCount++;
            removeCalled = true;
            current = null;
        }

        @Override
        public void set(E e) {
            checkForModification();
            if(current == null){
                throw new IllegalStateException();
            }
            current.setElement(e);
			interModCount++;
        }

        @Override
        public void add(E e) {
            checkForModification();
            IUDoubleLinkedList.this.add(nextIndex, e);
            interModCount++;
        } 
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

	private enum ListIteratorState {
		NONE,
		PREVIOUS,
		NEXT
	}

}
