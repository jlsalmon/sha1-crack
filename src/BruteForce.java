import java.util.Arrays;

public class BruteForce implements Runnable
{
    private final int min;
    private final int max;
    private final int stringLength;
    private byte[] hash;
//    private final String hash;
    private final byte[] salt;
    private final byte[] chars;
    private long startTime;

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
//        this.hash = hash;
        this.salt = salt.getBytes();

        chars = new byte[stringLength + 1];
        Arrays.fill(chars, 1, chars.length, (byte) min);
    }

    /**
     * Testing harness
     */
    public static void main(String[] args)
    {
        int numTestRuns = 5;
        long startTime, endTime;
        double total = 0;
        String hash = "57864da96344366865dd7cade69467d811a7961b";
        String salt = "";

        for (int i = 0; i < numTestRuns; i++)
        {
            startTime = System.nanoTime();
            new BruteForce(hash, salt, '0', 'z', 5).run();
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
    public void run()
    {
        startTime = System.nanoTime();

        while (chars[0] == 0)
        {
            if (check()) return;
            increment();
        }
    }

    /**
     * Hash the current password and check if it matches the given hash
     *
     * @return true if the current password matches the given hash, false
     * otherwise
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

//        if (SHA1.encode(combined, offset, combined.length - offset)
//                .equals(this.hash))
        byte[] sha1 = SHA1.encode(combined, offset, combined.length - offset);
        if (Arrays.equals(sha1, this.hash))
        {
            long endTime = System.nanoTime();

            System.out.println("cracked: " + new String(chars)
                    + " (" + this.hash + ") in "
                    + seconds(startTime, endTime));
            return true;
        }

        return false;
    }

    /**
     * Increment the current password (generate the next password in sequence)
     */
    private void increment()
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
    private void print()
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
    private String seconds(long startTime, long endTime)
    {
        return ((endTime - startTime) / 1000000000.0) + "s";
    }
}
