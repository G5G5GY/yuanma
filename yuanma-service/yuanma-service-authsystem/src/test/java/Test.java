import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        String alg = "SM4";
        String transformation = getTransform("SM4", "CBC", "PKCS5PADDING");
        int keynum = 1;
        try {
            KeyGenerator kg = KeyGenerator.getInstance(alg, "SwxaJCE");
            kg.init(keynum << 16);
            Key key1 = kg.generateKey();
            Key key2 = kg.generateKey();
            System.out.println("Key1 == Key2 ?" + key1.equals(key2));

            //Should be same, suppose key1 and  key2 are pointing to the same key im remote
            String plain = "Hello world!";

            byte[] et1 =  encrypt(key1, transformation, plain.getBytes());



            byte[] et2 =  encrypt(key2, transformation, plain.getBytes());
            System.out.println("encrypted1 == encrypted2? " + Arrays.equals(et1,et2));

            kg.init(128);
            Key key3 = kg.generateKey();
            Key key4 = kg.generateKey();

            //Should not be same, as always generate two different key in remote memory
            byte[] et3 =  encrypt(key3, transformation, plain.getBytes());
            byte[] et4 =  encrypt(key4, transformation, plain.getBytes());
            System.out.println("encrypted3 == encrypted4? " + Arrays.equals(et3,et4));
            System.out.println("encrypted1 == encrypted3? " + Arrays.equals(et1,et3));

            //Decryption
            String dt1 = decrypt(key1, transformation, et1);
            System.out.println("decrypt with key1, plain = " + dt1);
            String dt2 = decrypt(key2, transformation, et1);
            System.out.println("decrypt with key2, plain = " + dt2);


            KeyGenerator kg1 = KeyGenerator.getInstance(alg, "SwxaJCE");
        	System.out.println("kg == kg1? " + (kg==kg1)); 
            kg.init(keynum << 16);
            Key keyx = kg.generateKey();
            System.out.println("Key1 == Keyx ?" + key1.equals(keyx));
            byte[] etx =  encrypt(key1, transformation, plain.getBytes());
            System.out.println("encrypted1 == encryptedX? " + Arrays.equals(et1,etx));

        } catch (Exception e) {
              e.printStackTrace();
        }
    }

    public static String getTransform(String alg, String mode, String padding) {
        return String.valueOf(alg) + "/" + mode + "/" + padding;
    }

    public static byte[] encrypt(Key key, String transformation, byte[] plain) {
        try {
            Cipher cipher = Cipher.getInstance(transformation, "SwxaJCE");
            cipher.init(1, key);
            return cipher.doFinal(plain);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String decrypt(Key key, String transformation, byte[] encrypted) {
        try {
            Cipher cipher = Cipher.getInstance(transformation, "SwxaJCE");
            cipher.init(2, key);
            byte[] origin = cipher.doFinal(encrypted);
            return new String(origin);
        } catch (Exception e) {
              e.printStackTrace();
        }

        return null;
    }
}
