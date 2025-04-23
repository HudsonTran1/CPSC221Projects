public class TempIUSingleLinkedListTester {
    public static void main(String[] args) {
        
        IndexedUnsortedList<Integer> list = new IUSingleLinkedList<Integer>();

        list.addToRear(-1);
        list.addToRear(2);

        for (int i = 0; i < 6; i++) {
            list.addToRear(i);
        }

        System.out.println(list.toString());
    }
}
