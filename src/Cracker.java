
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
                "2cb3c01f1d6851ac471cc848cba786f9edf9a15b", // salt
//                "a20cdc214b652b8f9578f7d9b7a9ad0b13021aef", // doesnt
//                "76bcb777cd5eb130893203ffd058af2d4f46e495", // work
//                "9208c87b4d81e7026f354e63e04f7a6e9ca8b535", // for
//                "d5e694e1182362ee806e4b03eee9bb453a535482", // brute
//                "120282760b8322ad7caed09edc011fc8dafb2f0b", // force


                // [a-z]{1..6}
//                "1ce1416347075b6070a35ce5e9d26b61d91ea6c3",
//                "8af56de68279cb6f5ed022f31af18b9fcdcc2e92",
//                "6a9f6c3fff9581a22ef10cabd544143e37c61b4f",
//                "81b781d39d62e9d0e272e181ce7802af3f0f94f7",
//                "41fe08fc0dd44e79f799d03ece903e62be25dc7d",
//                "984ff6ee7c78078d4cb1ca08255303fb8741d986",
//                "1f8ac10f23c5b5bc1167bda84b833e5c057a77d2",
//                "08221562385e4197752e11a3206fd47624b05aac",
//                "a0d8a72797b5185f097a4f0f8cc7b19d98b72a5e",
//                "919a79f1cf158d3d1a9dd454cf0c7d5ad69b6b7a",

                // [0-9]{1..8}
//                "fe635ae88967693bc7e7eead87906e62e472c52f",
//                "3ac2d907663deccd843f9bbcf0c63bd3ad885a0e",
//                "3557c095ed6c16a90febda48d6b3a4490107b0d9",
//                "85e04129ed328d4a2b3eedabca74d08b3e6badc1",
//                "70352f41061eda4ff3c322094af068ba70c3b38b",
//                "052bd5b02559d1270866c5626538e720cec0c135",
//                "3e71f65d56cb29521ac16ff1f92ecace156b1db5",
//                "bfc52d4e36cb45cb667749982755e63630f3bc93",
//                "8cb2237d0679ca88db6464eac60da96345513964",
//                "38bbc0a1ca7e9b3e9f6ab33782e0f780f009db1f",
        };

        String charset = "0123456789abcdefghijklmnopqrstuvwxyz";
        String salt = "uwe.ac.uk";
        int maxLength = 6;

        for (String hash : hashes)
        {
            new Thread(new CrackerThread(charset, hash, salt, maxLength)).start();
        }
    }
}
