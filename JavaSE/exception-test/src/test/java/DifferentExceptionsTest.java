import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by vika on 04.12.14.
 */
public class DifferentExceptionsTest {
    private static final String CLASS_TO_LOAD = "main.java.Utils";

    public void recursivePrint(int num) {
        recursivePrint(++num);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testShouldThrowArrayIndexOutOfBoundsException() {
        int[] intArray = {1, 5, 6};
        intArray[3] = 1;

    }

    @Test(expected = ClassCastException.class)
    public void testShouldThrowClassCastException() {
        Object variableForCasting = 10;
        String a = (String) variableForCasting;

    }

    @Test(expected = ClassNotFoundException.class)
    public void testShouldThrowClassNotFoundException() throws ClassNotFoundException {
        Class.forName(CLASS_TO_LOAD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShouldThrowIllegalArgumentException() {
        List<String> list = Arrays.asList("First", "Second", "Third");
        list.subList(2, 1);
    }

    @Test(expected = NullPointerException.class)
    public void testShouldThrowNullPointerException() {
        new ArrayList<String>(null);

    }

    @Test(expected = NumberFormatException.class)
    public void testShouldThrowNumberFormatException() {
        String s = "String";
        Integer.parseInt(s);
    }

    @Test(expected = AssertionError.class)
    public void testShouldThrowAssertionError() {
        int numberForCompare = 1;
        int otherNumberForCompare = 2;
        assertEquals(numberForCompare, otherNumberForCompare);
    }

    @Test(expected = StackOverflowError.class)
    public void testShouldThrowStackOverflowError() {
        recursivePrint(1);
    }

    @Test(expected = IllegalStateException.class)
    public final void testShouldThrowIllegalStateException() {
        List<String> MyList = new ArrayList<String>();
        Iterator<String> itr = MyList.iterator();
        itr.remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testShouldThrowUnsupportedOperationException() throws UnsupportedOperationException {
        Collection<Integer> unmodifiableCollection = Collections.unmodifiableCollection(new ArrayList<Integer>());
        unmodifiableCollection.add(5);
    }

    @Test(expected = OutOfMemoryError.class)
    public void testShouldThrowOutOfMemoryError() {
        long[][] arrayForMemoryErrorThrowing = new long[Integer.MAX_VALUE][Integer.MAX_VALUE];

    }

}
