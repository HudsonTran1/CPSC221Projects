import java.util.Iterator;
import java.util.ListIterator;

public class IUDoubleLinkedList<E> implements IndexedUnsortedList<E>{

    @Override
    public void addToFront(Object element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addToFront'");
    }

    @Override
    public void addToRear(Object element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addToRear'");
    }

    @Override
    public void add(Object element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public void addAfter(Object element, Object target) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addAfter'");
    }

    @Override
    public void add(int index, Object element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'add'");
    }

    @Override
    public Object removeFirst() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeFirst'");
    }

    @Override
    public Object removeLast() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeLast'");
    }

    @Override
    public Object remove(Object element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public Object remove(int index) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public void set(int index, Object element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'set'");
    }

    @Override
    public Object get(int index) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public int indexOf(Object element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'indexOf'");
    }

    @Override
    public Object first() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'first'");
    }

    @Override
    public Object last() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'last'");
    }

    @Override
    public boolean contains(Object target) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'contains'");
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isEmpty'");
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'size'");
    }

    @Override
    public Iterator iterator() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'iterator'");
    }

    @Override
    public ListIterator listIterator() {
        return new DLLListIterator(0);
    }

    @Override
    public ListIterator listIterator(int startingIndex) {
        return new DLLListIterator(startingIndex);
    }

    private class DLLListIterator implements ListIterator<E>{
        BidirectionalNode<E> current, next;
        int nextIndex;
        int interModCount;
        boolean removeCalled, addCalled;

        DLLListIterator(int index){
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

            if(next == null){
                current = next = rear;
            }
            else{
                current = next = next.getPrevious();
            }
            nextIndex--;
            removeCalled = addCalled = false;

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
            if(current == null || addCalled || removeCalled){
                throw new IllegalStateException();
            }
            current.setElement(e);
        }

        @Override
        public void add(E e) {
            checkForModification();
            if(next==null){
                addToRear(e);
                current = rear.getPrevious();
            }
            else{ 
                next.setElement(e);
                current = next.getPrevious();
            }
            
            nextIndex++;
            interModCount++;
            addCalled = true;
        } 
    }
}
