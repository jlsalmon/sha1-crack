import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Cracker
{
    public static void main(String[] args)
    {
        final String[] hashes = {

                // [a-z0-9]{1..6}, no salt
//                "c2543fff3bfa6f144c2f06a7de6cd10c0b650cae", // this
//                "b47f363e2b430c0647f14deea3eced9b0ef300ce", // is
//                "e74295bfc2ed0b52d40073e8ebad555100df1380", // very
//                "0f7d0d088b6ea936fb25b477722d734706fe8b40", // simple
//                "77cfc481d3e76b543daf39e7f9bf86be2e664959", // fail7
//                "5cc48a1da13ad8cef1f5fad70ead8362aabc68a1", // 5you5
//                "4bcc3a95bdd9a11b28883290b03086e82af90212", // 3crack
//                "7302ba343c5ef19004df7489794a0adaee68d285", // 1you1
//                "21e7133508c40bbdf2be8a7bdc35b7de0b618ae4", // 00if00
//                "6ef80072f39071d4118a6e7890e209d4dd07e504", // cannot
//                "02285af8f969dc5c7b12be72fbce858997afe80a", // 4this4
//                "57864da96344366865dd7cade69467d811a7961b", // 6will

                // [a-z0-9]{1..6}, salt="uwe.ac.uk" at start
//                "2cb3c01f1d6851ac471cc848cba786f9edf9a15b", // salt
//                "a20cdc214b652b8f9578f7d9b7a9ad0b13021aef", // doesnt
//                "76bcb777cd5eb130893203ffd058af2d4f46e495", // work
//                "9208c87b4d81e7026f354e63e04f7a6e9ca8b535", // for
//                "d5e694e1182362ee806e4b03eee9bb453a535482", // brute
                "120282760b8322ad7caed09edc011fc8dafb2f0b", // force

        };

        int numTestRuns = 5;
        double totalTime = 0;

        String charset = "0123456789abcdefghijklmnopqrstuvwxyz";
//        String charset = "0123456789etaoinshrdlcumwfgypbvkjxqz";
        String salt = "uwe.ac.uk";
//        String salt = "";
        int maxLength = 6;

        List<Callable<Double>> tasks = new ArrayList<Callable<Double>>();

        for (int i = 1; i <= maxLength; i++)
        {
            tasks.add(new BruteForce(hashes[0], salt, '0', 'z', i));
//            tasks.add(new BruteForceReverse(hashes[0], salt, '0', 'z', i));
        }


        for (int i = 0; i < numTestRuns; i++)
        {
            try
            {
                ExecutorService pool = Executors.newCachedThreadPool();
                double time = pool.invokeAny(tasks);
                totalTime += time;
                pool.shutdownNow();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        }

        System.out.println("avg time over " + numTestRuns + " runs: "
                + (totalTime / numTestRuns) + "s");
    }
}
