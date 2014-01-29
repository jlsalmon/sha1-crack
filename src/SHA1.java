import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class SHA1
{
    private final static char[] hexArray = "0123456789abcdef".toCharArray();

//    public static String encode(byte[] text, int offset, int length)
    public static byte[] encode(byte[] text, int offset, int length)
    {
        MessageDigest md;
        byte[] sha1hash = {};

        try
        {
            md = MessageDigest.getInstance("SHA-1");
            md.update(text, offset, length);
            sha1hash = md.digest();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return sha1hash;
//        return convertToHex(sha1hash);
    }

    public static String encode(byte[] text)
    {
        return convertToHex(encode(text, 0, text.length));
    }

    private static String convertToHex(byte[] data)
    {
        char[] hexChars = new char[data.length * 2];
        for (int j = 0; j < data.length; j++)
        {
            int v = data[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String s)
    {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2)
        {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

}
