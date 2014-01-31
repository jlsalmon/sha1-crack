import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class RainbowTable
{
    private final char[] charset;
    private final int passwordLength;
    private final int chainLength;
    private final int numChains;
    private final BigInteger modulo;
    private Map<String, String> table;

    /**
     * @param charset        the character set
     * @param passwordLength the password length
     * @param chainLength    the chain length
     * @param numChains      the number of chains to generate
     */
    public RainbowTable(String charset, int passwordLength, int chainLength,
                        int numChains)
    {
        this.charset = charset.toCharArray();
        this.passwordLength = passwordLength;
        this.chainLength = chainLength;
        this.numChains = numChains;
        this.modulo = getPrimeModulus();
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        String charset = "0123456789abcdefghijklmnopqrstuvwxyz";

        RainbowTable table = new RainbowTable(charset, 5, 5000, 125000);

        // Generate the table
        table.generate();

        // Perform a lookup
        String pass = table.lookup("86f7e437faa5a7fce15d1ddcb9eaeaea377667b8");
        System.out.println("lookup: " + pass);
    }

    /**
     *
     */
    public void generate()
    {
        // Choose a random set of initial passwords from the charset

        // Compute chains of length chainLength for each one

        // Store the first (startpoint) and last (endpoint) password in each
        // chain

        table = new HashMap<String, String>(numChains);
        String start, end;
        int collisions = 0;

        long startTime = System.nanoTime();
        while (table.size() < numChains)
        {
            start = generateRandomPassword(passwordLength);
            end = generateChain(start);

            // Check for duplicate chains (collision merges)
            if (!table.containsKey(end))
            {
                table.put(end, start);
            }
            else
            {
                collisions++;
            }
        }

        long endTime = System.nanoTime();
        System.out.println("chains: " + numChains + " length: " + chainLength
                + " generated in " + seconds(startTime, endTime)
                + " (" + collisions + " collisions)");

        // Serialize the table
        startTime = System.nanoTime();
        serialize("table.dat");
        endTime = System.nanoTime();
        System.out.println("serialized in " + seconds(startTime, endTime));
    }

    /**
     * @param passwordLength
     *
     * @return
     */
    private String generateRandomPassword(int passwordLength)
    {
        StringBuilder builder = new StringBuilder(passwordLength);

        for (int i = 0; i < passwordLength; i++)
        {
            builder.append(charset[(int) (Math.random() * charset.length)]);
        }

        return builder.toString();
    }

    /**
     * Generate a chain from the given starting point and return the endpoint
     *
     * @param start
     *
     * @return
     */
    private String generateChain(String start)
    {
        String pass = start;
        String hash;

        for (int i = 0; i < chainLength; i++)
        {
            hash = SHA1.encode(pass.getBytes());
            pass = reduce(hash, i);
        }

        return pass;
    }

    /**
     * @param hash
     * @param position the current chain position
     *
     * @return
     */
    private String reduce(String hash, int position)
    {
        BigInteger index;
        StringBuilder builder = new StringBuilder();

        BigInteger temp = new BigInteger(hash, 16);
        // Reduction needs to produce a different output for a different chain
        // position
        temp = temp.add(BigInteger.valueOf(position));
        temp = temp.mod(this.modulo);

        while (temp.intValue() > 0)
        {
            index = temp.mod(BigInteger.valueOf(charset.length));
            builder.append(charset[index.intValue()]);
            temp = temp.divide(BigInteger.valueOf(charset.length));
        }

        return builder.toString();
    }

    /**
     *
     * @return
     */
    private BigInteger getPrimeModulus()
    {
        BigInteger max = BigInteger.ZERO;

        for (int i = 1; i <= passwordLength; i++)
        {
            max = max.add(BigInteger.valueOf(charset.length).pow(i));
        }

        BigInteger prime = max.nextProbablePrime();
        System.out.println("prime modulus: " + prime);
        return prime;
    }

    /**
     * @param hashToFind
     *
     * @return
     */
    public String lookup(String hashToFind)
    {
        String hash, pass = null, lookup = null;
        long startTime = System.nanoTime();

        for (int i = chainLength - 1; i >= 0; i--)
        {
            hash = hashToFind;

            for (int j = i; j < chainLength; j++)
            {
                pass = reduce(hash, j);
                hash = SHA1.encode(pass.getBytes());
            }

            if (table.containsKey(pass))
            {
                lookup = lookupChain(table.get(pass), hashToFind);
                if (lookup != null)
                {
                    break;
                }
            }
        }

        long endTime = System.nanoTime();
        System.out.println("lookup took " + seconds(startTime, endTime));
        return lookup;
    }

    /**
     * Create the chain from the start point and look for the hash
     *
     * @param start
     * @param hashToFind
     *
     * @return the password corresponding to the hash to be found, null
     * otherwise
     */
    private String lookupChain(String start, String hashToFind)
    {
        String hash, pass = start, lookup = null;

        for (int j = 0; j < chainLength; j++)
        {
            hash = SHA1.encode(pass.getBytes());

            if (hash.equals(hashToFind))
            {
                lookup = pass;
                System.out.println("matched hash: " + hashToFind
                        + " (" + lookup + ")");
                break;
            }

            pass = reduce(hash, j);
        }

        return lookup;
    }

    /**
     * Write the current rainbow table to a file.
     *
     * @param filename the name of the file to write
     */
    private void serialize(String filename)
    {
        ObjectOutputStream out;
        try
        {
            out = new ObjectOutputStream(new FileOutputStream(filename));
            out.writeObject(table);
            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @param startTime
     * @param endTime
     *
     * @return
     */
    private String seconds(long startTime, long endTime)
    {
        return ((endTime - startTime) / 1000000000.0) + "s";
    }
}
