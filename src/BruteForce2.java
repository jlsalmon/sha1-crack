import java.util.Arrays;

public class BruteForce2 implements Runnable
{
    final int stringLength;
    final byte[] charset;
    final String hash;
    final int[] charsetIndices;
    private final byte[] chars;

    public BruteForce2(String hash, String charset, int len)
    {
        this.stringLength = len;
        this.charset = charset.getBytes();
        this.hash = hash;

        chars = new byte[stringLength];
        Arrays.fill(chars, 0, chars.length, this.charset[0]);

        charsetIndices = new int[stringLength];
    }

    public static void main(String[] args)
    {
        long startTime = System.nanoTime();
//        String charset = "0123456789abcdefghijklmnopqrstuvwxyz";
        String charset = "abcdefghijklmnopqrstuvwxyz";
        String hash = "e74295bfc2ed0b52d40073e8ebad555100df1380";
        new BruteForce2(hash, charset, 4).run();
        long endTime = System.nanoTime();

        long duration = endTime - startTime;
        System.out.println(duration);
    }

    @Override
    public void run()
    {
        for (int i = 0; i < Math.pow(this.charset.length, stringLength); i++)
        {
//            print();
//            if (check()) return;
            increment();
        }
    }

    private boolean check()
    {
        if (SHA1.encode(chars, 0, chars.length).equals(this.hash))
        {
            System.out.println("cracked: " + new String(chars) + " (" + this.hash + ")");
            return true;
        }

        return false;
    }

    private void increment()
    {
        for (int i = chars.length - 1; i >= 0; i--)
        {
            if (chars[i] < charset[charset.length - 1])
            {
                chars[i] = charset[++charsetIndices[i]];
                return;
            }

            chars[i] = charset[0];
            charsetIndices[i] = 0;
        }
    }

    private void print()
    {
        for (int i = 0; i < chars.length; i++)
        {
            System.out.print((char) chars[i]);
        }
        System.out.println();
    }

}