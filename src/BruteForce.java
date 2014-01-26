import java.util.Arrays;

public class BruteForce implements Runnable
{
    private final int min;
    private final int max;
    private final int stringLength;
    private final String hash;
    private final byte[] salt;
    private final byte[] chars;
    private long startTime;

    public BruteForce(String hash, String salt, char min, char max, int len)
    {
        this.min = min;
        this.max = max;
        this.stringLength = len;
        this.hash = hash;
        this.salt = salt.getBytes();

        chars = new byte[stringLength + 1];
        Arrays.fill(chars, 1, chars.length, (byte) min);
    }

    public static void main(String[] args)
    {
        long startTime = System.nanoTime();
        String hash = "e74295bfc2ed0b52d40073e8ebad555100df1380";
        String salt = "";

        new BruteForce(hash, salt, '0', 'z', 4).run();
        long endTime = System.nanoTime();

        long duration = endTime - startTime;
        System.out.println(duration);
    }

    @Override
    public void run()
    {
        startTime = System.nanoTime();

        while (chars[0] == 0)
        {
//            print();
            if (check()) return;
            increment();
        }
    }

    private boolean check()
    {
        byte[] combined = new byte[salt.length + (chars.length - 1)];

        System.arraycopy(salt, 0, combined, 0, salt.length);
        System.arraycopy(chars, 1, combined, salt.length, chars.length - 1);

        if (SHA1.encode(combined, 0, combined.length).equals(this.hash))
        {
            long endTime = System.nanoTime();
            long duration = endTime - startTime;

            System.out.println("cracked: " + new String(chars)
                    + " (" + this.hash + ") " + duration);
            return true;
        }

        return false;
    }

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

    private void print()
    {
        for (int i = 1; i < chars.length; i++)
        {
            System.out.print((char) chars[i]);
        }
        System.out.println();
    }

}
