import java.util.ArrayList;
import java.util.List;

/**
 * This class has one method that takes an item and adds it to a list
 *
 * @author Logan Nunno
 */
public final class Utils {
    /**
     * Given an item and the number of items in the list before calling the repeat method
     * creat a list
     * loop for n number of times
     * adding the item to list that number of times
     * the return the new list
     *
     * @param n    the number of times the item will be added to a list
     * @param item the object being added to the list
     * @param <T>  the type of the list being created
     * @return a list of type T that is of length n filled with the given item
     */
    public static <T> List<T> repeat(int n, T item) {
        List<T> ls = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            ls.add(item);
        }
        return ls;
    }
}
