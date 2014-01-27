import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrackerThread implements Runnable
{
    private String charset;
    private String hash;
    private String salt;
    private int maxLength;

    public CrackerThread(String charset, String hash, String salt, int maxLength)
    {
        this.charset = charset;
        this.hash = hash;
        this.salt = salt;
        this.maxLength = maxLength;
    }

    @Override
    public void run()
    {
        crack();
    }

    private boolean crack()
    {
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 1; i <= this.maxLength; i++)
        {
            executor.execute(new BruteForce(this.hash, this.salt, '0', 'z', i));
        }

        return true;
    }
}
