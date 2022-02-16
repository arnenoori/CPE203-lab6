import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Comparator;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.Before;

public class TestCases
{
   private static final Song[] songs = new Song[] {
         new Song("Decemberists", "The Mariner's Revenge Song", 2005), // 1
         new Song("Rogue Wave", "Love's Lost Guarantee", 2005), // 2
         new Song("Avett Brothers", "Talk on Indolence", 2006), // 3
         new Song("Gerry Rafferty", "Baker Street", 1998), // 4
         new Song("City and Colour", "Sleeping Sickness", 2007), // 5
         new Song("Foo Fighters", "Baker Street", 1997), // 6
         new Song("Queen", "Bohemian Rhapsody", 1975), // 7
         new Song("Gerry Rafferty", "Baker Street", 1978) // 8
      };

   @Test
   public void testArtistComparator()
   {
      Comparator<Song> artist = new ArtistComparator();
      assertTrue(artist.compare(songs[0], songs[7]) < 0);
      assertEquals(true, artist.compare(songs[5], songs[3]) < 0);
      assertEquals(true, artist.compare(songs[3], songs[7]) == 0);
      assertEquals(true, artist.compare(songs[1], songs[0]) > 0);
   }

   @Test
   public void testLambdaTitleComparator()
   {
      Comparator<Song> lambda = (Song s1, Song s2) -> s1.getTitle().compareTo(s2.getTitle());
      assertEquals(true, lambda.compare(songs[5],songs[3]) == 0);
      assertEquals(true, lambda.compare(songs[5],songs[7]) == 0);
      assertEquals(true, lambda.compare(songs[0],songs[1]) > 0);
      assertEquals(true, lambda.compare(songs[3],songs[4]) < 0);
   }

   @Test
   public void testYearExtractorComparator()
   {
      Comparator<Song> extractor = Comparator.comparing(Song::getYear).reversed();
      assertEquals(true, extractor.compare(songs[0],songs[1]) == 0);
      assertEquals(true, extractor.compare(songs[1],songs[2]) > 0);
      assertEquals(true, extractor.compare(songs[3],songs[4]) > 0);
      assertEquals(true, extractor.compare(songs[5],songs[6]) < 0);
   }

   @Test
   public void testComposedComparator()
   {
      Comparator<Song> c1 = (s1, s2) -> s1.getArtist().compareTo(s2.getArtist());
      Comparator<Song> c2 = Comparator.comparing(Song::getYear);
      ComposedComparator comp = new ComposedComparator(c1, c2);
      assertEquals(true, comp.compare(songs[0],songs[1]) < 0);
      assertEquals(true, comp.compare(songs[1],songs[2]) > 0);
      assertEquals(true, comp.compare(songs[3],songs[4]) > 0);
      assertEquals(true, comp.compare(songs[5],songs[6]) < 0);
      assertEquals(true, comp.compare(songs[3],songs[7]) > 0);

   }

   @Test
   public void testThenComparing()
   {
      Comparator<Song> singleComp = (s1, s2) -> s1.getTitle().compareTo(s2.getTitle());
      Comparator<Song> thenComp = singleComp.thenComparing((s1, s2) -> s1.getArtist().compareTo(s2.getArtist()));
      assertEquals(true, thenComp.compare(songs[3],songs[5]) > 0);
      assertEquals(true, thenComp.compare(songs[3],songs[7]) == 0);
      assertEquals(true, thenComp.compare(songs[0],songs[2]) > 0);
      assertEquals(true, thenComp.compare(songs[5],songs[3]) < 0);
   }

   @Test
   public void runSort()
   {
      List<Song> songList = new ArrayList<>(Arrays.asList(songs));
      List<Song> expectedList = Arrays.asList(
         new Song("Avett Brothers", "Talk on Indolence", 2006),
         new Song("City and Colour", "Sleeping Sickness", 2007),
         new Song("Decemberists", "The Mariner's Revenge Song", 2005),
         new Song("Foo Fighters", "Baker Street", 1997),
         new Song("Gerry Rafferty", "Baker Street", 1978),
         new Song("Gerry Rafferty", "Baker Street", 1998),
         new Song("Queen", "Bohemian Rhapsody", 1975),
         new Song("Rogue Wave", "Love's Lost Guarantee", 2005)
         );

      Comparator<Song> byArtist = (s1, s2) -> s1.getArtist().compareTo(s2.getArtist());
      Comparator<Song> byTitle = (s1, s2) -> s1.getTitle().compareTo(s2.getTitle());
      Comparator<Song> byYear = Comparator.comparing(Song::getYear);

      songList.sort(
        byArtist.thenComparing(byTitle).thenComparing(byYear)
      );
      assertEquals(songList, expectedList);
   }
}
