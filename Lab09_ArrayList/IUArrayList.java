import java.util.*;

/**
 * Array-based implementation of IndexedUnsortedList.
 * 
 * @author 
 *
 * @param <E> type to store
 */
public class IUArrayList<E> implements IndexedUnsortedList<E> {
	private static final int DEFAULT_CAPACITY = 10;
	private static final int NOT_FOUND = -1;
	
	private E[] array;
	private int rear;
	private int modCount; // DO NOT REMOVE ME
	
	/** Creates an empty list with default initial capacity */
	public IUArrayList() {
		this(DEFAULT_CAPACITY);
	}
	
	/** 
	 * Creates an empty list with the given initial capacity
	 * @param initialCapacity
	 */
	@SuppressWarnings("unchecked")
	public IUArrayList(int initialCapacity) {
		array = (E[])(new Object[initialCapacity]);
		rear = 0;
		modCount = 0; // DO NOT REMOVE ME
	}
	
	/** Double the capacity of array */
	private void expandCapacity() {
		array = Arrays.copyOf(array, array.length*2);
	}

	@Override
	public void addToFront(E element) {
		add(0, element); // call into add(int index, E element) for code reuse
		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public void addToRear(E element) {
		add(rear, element); // call into add(int index, E element) at the index of the rear
		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public void add(E element) {
		addToRear(element); // call into addToRear
		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public void addAfter(E element, E target) {
		add(indexOf(target) + 1, element); // call into the add at index method, using the index found directly after the indexOf() the target
		modCount++;
	}

	@Override
	public void add(int index, E element) {
		//guard for negative indices or indices past rear of collection
		if (index < 0) {
			throw new IndexOutOfBoundsException(index);
		} else if (index > rear) {
			throw new IndexOutOfBoundsException("Array index out of range: " + index + "; cannot add element beyond the rear of the list.");
		}
		//check if we need to expand the capacity
		if (rear == array.length) {
			expandCapacity();
		}
		//shift elements to make room for the new one
		for (int i = rear; i >= index; i--) {
			array[i+1] = array[i];
		}
		array[index] = element; // set the element at the index
		rear++; // increment rear
		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public E removeFirst() {
		modCount++; // DO NOT REMOVE ME
		return remove(0); // call into the remove(int index); guard statements will be triggered by remove(int index), as will modCount (left modCount++ anyway because we were told to, but technically not necessary)
	}

	@Override
	public E removeLast() {
		// Daniel L
		modCount++; // DO NOT REMOVE ME
		return remove(rear - 1); // remove the last element in the array, directly before the empty slot at index rear; guard statements will be triggered by remove(int index), as will modCount (left modCount++ anyway because we were told to, but technically not necessary)
	}

	@Override
	public E remove(E element) {
		// Daniel L
		modCount++; // DO NOT REMOVE ME
		return remove(indexOf(element)); // call into the remove(int index); guard statements will be triggered by remove(int index), as will modCount (left modCount++ anyway because we were told to, but technically not necessary)
	}

	@Override
	public E remove(int index) {
		// Daniel L
		if (index < 0 || index >= rear) { throw new IndexOutOfBoundsException(); } // guard statement to protect against all invalid indices, including all negative indices, and all indices past the end of the array
        E result = this.array[index]; // store element to return

		rear--; // decrement rear
		// shift elements -- copied from original remove(E element)
		for (int i = index; i < rear; i++) {
			array[i] = array[i+1];
		}
        	this.array[rear] = null; //n ull out the now empty end of the list
		modCount++; // DO NOT REMOVE ME
		return result; // return the removed element
	}

	@Override
	public void set(int index, E element) {
		// Daniel L
		remove(index); // this subcall triggers the necessary guard statements
		add(index, element); // technically this implementation is less efficient as it requires two shifts (one by remove and by add) but the order is the same
		modCount++; // DO NOT REMOVE ME
	}

	@Override
	public E get(int index) {
		// @Ponygator
		if (index < 0 || index >= rear) { throw new IndexOutOfBoundsException(); } // guard statement to protect against all invalid indices, including all negative indices, and all indices past the end of the array
		return array[index];
	}

	@Override
	public int indexOf(E element) {
		int index = NOT_FOUND;
		
		if (!isEmpty()) {
			int i = 0;
			while (index == NOT_FOUND && i < rear) {
				if (element.equals(array[i])) {
					index = i;
				} else {
					i++;
				}
			}
		}
		// Put this guard statement here instead of in the add and remove methods to reduce code duplication
		if (index == NOT_FOUND) {
			throw new NoSuchElementException();
		}
		return index;
	}

	@Override
	public E first() {
		// @Ponygator
		if(isEmpty()) {	throw new NoSuchElementException(); } // guard statement to protect against empty list
		return array[0];
	}

	@Override
	public E last() {
		// @Ponygator
		if(isEmpty()) {	throw new NoSuchElementException(); } // guard statement to protect against empty list
		return array[rear-1];
	}

	@Override
	public boolean contains(E target) {
		return (indexOf(target) != NOT_FOUND);
	}

	@Override
	public boolean isEmpty() {
		return rear == 0;
	}

	@Override
	public int size() {
		return rear;
	}

	@Override
	public String toString() {
		String result = "[";
		for (int i = 0; i < rear; i++) {
			result += array[i];
			if (i < rear - 1) {
				result += ", ";
			}
		}
		result += "]";
		return result;
	}

	// IGNORE THE FOLLOWING COMMENTED OUT CODE UNTIL LAB 10
	// DON'T DELETE ME, HOWEVER!!!
	public Iterator<E> iterator() {
		// return new IUArrayListIterator(); // UNCOMMENT ME IN LAB 10
		return null; // REMOVE ME IN LAB 10
	}

	// UNCOMMENT THE CODE BELOW IN LAB 10
	
	// private class IUArrayListIterator implements Iterator<E> {

	// 	private int iterModCount, current;
	// 	private boolean canRemove;

	// 	public IUArrayListIterator() {
	// 		iterModCount = modCount;
	// 		current = 0;
	// 		canRemove = false;
	// 	}

	// 	@Override
	// 	public boolean hasNext() {
    //         if (iterModCount != modCount) {
    //             throw new ConcurrentModificationException();
    //         }
    //         return current < rear;
	// 	}

	// 	@Override
	// 	public E next() {
    //         if (!hasNext()) {
    //             throw new NoSuchElementException();
    //         }
    //         E item = array[current];
	// 		current++;
    //         canRemove = true;
    //         return item;
	// 	}

	// 	@Override
	// 	public void remove() {
    //         if (iterModCount != modCount) {
    //             throw new ConcurrentModificationException();
    //         }
    //         if (!canRemove) {
    //             throw new IllegalStateException();
    //         }
    //         // remove the element in the array at index current-1
    //         // presumably decrement the rear
    //         // presumably the modCount is getting incremented
	// 		// all indices have to back up by one
	// 		current--;
	// 		rear--;
	// 		// shift elements to the left
	// 		for (int i = current; i < rear; i++) {
	// 			array[i] = array[i + 1];
	// 		}
	// 		array[rear] = null;
	// 		modCount++;
	// 		iterModCount++;
	// 		// Can only remove the LAST "seen" element
	// 		// set back to a non-removal state 
    //         canRemove = false;
	// 	}
		
	// }

	// IGNORE THE FOLLOWING CODE
	// DON'T DELETE ME, HOWEVER!!!
	@Override
	public ListIterator<E> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListIterator<E> listIterator(int startingIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
