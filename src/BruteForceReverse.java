import java.util.Arrays;

public class BruteForceReverse extends BruteForce
{
    /**
     * Constructor
     *
     * @param hash the hash to crack
     * @param salt the salt that was used (empty string for no salt)
     * @param min  the lowest ASCII character in the character set
     * @param max  the highest ASCII character in the character set
     * @param len  the password length
     */
    public BruteForceReverse(String hash, String salt, char min, char max,
                             int len)
    {
        super(hash, salt, min, max, len);
    }

    /**
     *
     */
    @Override
    protected void fill()
    {
        Arrays.fill(chars, 1, chars.length, (byte) max);
    }

    /**
     * Loop backwards over the charset
     */
    @Override
    protected void increment()
    {
        for (int i = chars.length - 1; i >= 0; i--)
        {
            if (chars[i] > min)
            {
                if (chars[i] == 97)
                    chars[i] = 57;
                else
                    chars[i]--;
                return;
            }
            chars[i] = (byte) max;
        }
    }
}
