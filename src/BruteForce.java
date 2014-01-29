import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class BruteForce implements Callable<Double>
{
    protected final int min;
    protected final int max;
    private final int stringLength;
    private final byte[] salt;
    protected final byte[] chars;
    private byte[] hash;
    private long startTime;
    private long endTime;

    /**
     * Constructor
     *
     * @param hash the hash to crack
     * @param salt the salt that was used (empty string for no salt)
     * @param min  the lowest ASCII character in the character set
     * @param max  the highest ASCII character in the character set
     * @param len  the password length
     */
    public BruteForce(String hash, String salt, char min, char max, int len)
    {
        this.min = min;
        this.max = max;
        this.stringLength = len;
        this.hash = SHA1.hexStringToByteArray(hash);
        this.salt = salt.getBytes();
        this.chars = new byte[stringLength + 1];
    }

    /**
     * Testing harness
     */
    public static void main(String[] args) throws ExecutionException
    {
        int numTestRuns = 5;
        long startTime, endTime;
        double total = 0;
        String hash = "4bcc3a95bdd9a11b28883290b03086e82af90212";
        String salt = "";

        for (int i = 0; i < numTestRuns; i++)
        {
            startTime = System.nanoTime();
            new BruteForce(hash, salt, '0', 'z', 6).call();
            endTime = System.nanoTime();
            total += (endTime - startTime);
        }

        total = total / 1000000000.0;

        System.out.println("total: " + total + "s");
        System.out.println("avg: " + (total / numTestRuns) + "s");
    }

    /**
     * Run the algorithm
     */
    @Override
    public Double call() throws ExecutionException
    {
        fill();
        startTime = System.nanoTime();

        while (chars[0] == 0 && !Thread.currentThread().isInterrupted())
        {
            if (check()) return seconds(startTime, endTime);
            increment();
        }

        throw new ExecutionException(new Throwable());
    }

    /**
     *
     */
    protected void fill()
    {
        Arrays.fill(chars, 1, chars.length, (byte) min);
    }

    /**
     * Hash the current password and check if it matches the given hash
     *
     * @return the password that matched the given hash if any, false otherwise
     */
    private boolean check()
    {
        byte[] combined;
        int offset = 1;

        if (salt.length > 0)
        {
            combined = new byte[salt.length + (chars.length - 1)];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(chars, 1, combined, salt.length, chars.length - 1);
            offset = 0;
        }
        else
        {
            combined = chars;
        }

        byte[] sha1 = SHA1.encode(combined, offset, combined.length - offset);

        if (Arrays.equals(sha1, this.hash))
        {
            endTime = System.nanoTime();
            String time = String.valueOf(seconds(startTime, endTime));
            String pass = new String(chars);

            System.out.println("cracked: " + pass + " (" + this.hash + ") " +
                    "in " + time + "s");
            return true;
        }

        return false;
    }

    /**
     * Increment the current password (generate the next password in sequence)
     */
    protected void increment()
    {
        for (int i = chars.length - 1; i >= 0; i--)
        {
            if (chars[i] < max)
            {
                if (chars[i] == 57)
                    chars[i] = 97;
                else
                    chars[i]++;
                return;
            }
            chars[i] = (byte) min;
        }
    }

    /**
     * Print the current password
     */
    protected void print()
    {
        for (int i = 1; i < chars.length; i++)
        {
            System.out.print((char) chars[i]);
        }
        System.out.println();
    }

    /**
     * Convert a nanosecond time duration to seconds
     *
     * @param startTime the start time in nanoseconds
     * @param endTime   the end time in nanoseconds
     *
     * @return the duration in seconds
     */
    private double seconds(long startTime, long endTime)
    {
        return ((endTime - startTime) / 1000000000.0);
    }
}
