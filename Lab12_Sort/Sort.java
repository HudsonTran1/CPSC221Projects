import java.util.Comparator;
import java.util.Iterator;

/**
 * Class for sorting lists that implement the IndexedUnsortedList interface,
 * using ordering defined by class of objects in list or a Comparator.
 * As written uses Quicksort algorithm.
 *
 * @author CPSC 221 Instructors
 */
public class Sort {	
	/**
	 * Returns a new list that implements the IndexedUnsortedList interface. 
	 * As configured, uses WrappedDLL. Must be changed if using 
	 * your own IUDoubleLinkedList class. 
	 * 
	 * @return a new list that implements the IndexedUnsortedList interface
	 */
	private static <E> IndexedUnsortedList<E> newList() {
		return new WrappedDLL<E>();
	}
	
	/**
	 * Sorts a list that implements the IndexedUnsortedList interface 
	 * using compareTo() method defined by class of objects in list.
	 * DO NOT MODIFY THIS METHOD
	 * 
	 * @param <E>
	 *            The class of elements in the list, must extend Comparable
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 * @see IndexedUnsortedList 
	 */
	public static <E extends Comparable<E>> void sort(IndexedUnsortedList<E> list) {
		quicksort(list);
	}

	/**
	 * Sorts a list that implements the IndexedUnsortedList interface 
	 * using given Comparator.
	 * DO NOT MODIFY THIS METHOD
	 * 
	 * @param <E>
	 *            The class of elements in the list
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 * @param c
	 *            The Comparator used
	 * @see IndexedUnsortedList 
	 */
	public static <E> void sort(IndexedUnsortedList <E> list, Comparator<E> c) {
		quicksort(list, c);
	}
	
	/**
	 * Quicksort algorithm to sort objects in a list 
	 * that implements the IndexedUnsortedList interface, 
	 * using compareTo() method defined by class of objects in list.
	 * DO NOT MODIFY THIS METHOD SIGNATURE
	 * 
	 * @param <E>
	 *            The class of elements in the list, must extend Comparable
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 */
	private static <E extends Comparable<E>> void quicksort(IndexedUnsortedList<E> list) {
		if(list.size() <= 1) { return; } // base case: when the list is only one element long or shorter; no more sorting required
		// create lists for left and right buckets
		WrappedDLL<E> leftList = new WrappedDLL<>(); 
		WrappedDLL<E> rightList = new WrappedDLL<>(); 

		Iterator<E> itr = list.iterator(); // Iterator to move through the list
		
		// partition element
		E partitionElement = itr.next(); // grab the partition element -- skip the first element for the loop later
		itr.remove(); // remove the partiion from the list
		// current element for iteration
		E current;

		// logic to toss all elements into left or right buckets (elements of same value as parititon placed in the right bucket)
		while (itr.hasNext()) {
			current = itr.next();
			itr.remove();
			if(partitionElement.compareTo(current) <= 0) { // if the element is bigger than or the same as the partition
				rightList.addToRear(current); // add to right bucket
			} else { // if the element is smaller than the partition
				leftList.addToRear(current); // add to left bucket
			}
		}

		// sort left and right lists
		quicksort(leftList);
		quicksort(rightList);

		// splice left and right lists
		for (E element : leftList) { // add all the elements in order from the left list to the original list
			list.addToRear(element);
		}
		list.addToRear(partitionElement); // add the partition back at the end of the original list
		for (E element : rightList) { // add all the elements in order from the right list to the orifinal list
			list.addToRear(element);
		}
	}
		
	/**
	 * Quicksort algorithm to sort objects in a list 
	 * that implements the IndexedUnsortedList interface,
	 * using the given Comparator.
	 * DO NOT MODIFY THIS METHOD SIGNATURE
	 * 
	 * @param <E>
	 *            The class of elements in the list
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 * @param c
	 *            The Comparator used
	 */
	private static <E> void quicksort(IndexedUnsortedList<E> list, Comparator<E> c) {
		if(list.size() <= 1) { return; } // base case: when the list is only one element long or shorter; no more sorting required
		// create lists for left and right buckets
		WrappedDLL<E> leftList = new WrappedDLL<>(); 
		WrappedDLL<E> rightList = new WrappedDLL<>(); 

		Iterator<E> itr = list.iterator(); // Iterator to move through the list
		
		// partition element
		E partitionElement = itr.next(); // grab the partition element -- skip the first element for the loop later
		itr.remove(); // remove the partiion from the list
		// current element for iteration
		E current;

		// logic to toss all elements into left or right buckets (elements of same value as parititon placed in the right bucket)
		while (itr.hasNext()) {
			current = itr.next();
			itr.remove();
			if(c.compare(partitionElement, current) <= 0) { // if the element is bigger than or the same as the partition
				rightList.addToRear(current); // add to right bucket
			} else { // if the element is smaller than the partition
				leftList.addToRear(current); // add to left bucket
			}
		}

		// sort left and right lists
		quicksort(leftList, c);
		quicksort(rightList, c);

		// splice left and right lists
		for (E element : leftList) { // add all the elements in order from the left list to the original list
			list.addToRear(element);
		}
		list.addToRear(partitionElement); // add the partition back at the end of the original list
		for (E element : rightList) { // add all the elements in order from the right list to the orifinal list
			list.addToRear(element);
		}
	}
	
}
