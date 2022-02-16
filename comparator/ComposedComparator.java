import java.util.Comparator;

public class ComposedComparator implements Comparator<Song> {
    private Comparator<Song> c1;
    private Comparator<Song> c2;

    public ComposedComparator(Comparator<Song> c1, Comparator<Song> c2){
        this.c1 = c1;
        this.c2 = c2;
    }

    public int compare(Song o1, Song o2) {
        int compare_value = c1.compare(o1, o2);
        if (compare_value == 0) {
            compare_value = c2.compare(o1, o2);
        }
        return compare_value;
    }

    // implement compare method
}
