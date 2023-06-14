
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class Test11 {



    public static void main(String[] args) {

        Test11 test = new Test11();
       System.out.println(test.decode("7868060CA934F89E24F9D7B749ECFE4CF448F267DBF338922368B0F897F715D5"));

        System.out.println(test.encode("I3CR8fibgwvYEJDZ9A3zE+jMNX4="));




    }



        private final String alg = "SM4";

        public String encode(String str) {
            String transformation = getTransform("SM4", "CBC", "PKCS5PADDING");
            int keynum = 1;
            try {
                KeyGenerator kg = KeyGenerator.getInstance(alg, "SwxaJCE");
                kg.init(keynum << 16);
                Key key1 = kg.generateKey();
                byte[] et1 =  encrypt(key1, transformation, str.getBytes());
                return byte2hex(et1);
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }


        public String decode(String str) {
            String transformation = getTransform("SM4", "CBC", "PKCS5PADDING");
            int keynum = 1;
            try {
                KeyGenerator kg = KeyGenerator.getInstance(alg, "SwxaJCE");
                kg.init(keynum << 16);
                Key key1 = kg.generateKey();
                return decrypt(key1, transformation,hex2byte(str));
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;

        }


        public String strategy() {
            return "SwxaJCE";
        }

        private  String getTransform(String alg, String mode, String padding) {
            return String.valueOf(alg) + "/" + mode + "/" + padding;
        }

        public  byte[] encrypt(Key key, String transformation, byte[] plain) {
            try {
                Cipher cipher = Cipher.getInstance(transformation, "SwxaJCE");
                cipher.init(1, key);
                return cipher.doFinal(plain);
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public  String decrypt(Key key, String transformation, byte[] encrypted) {
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



    public static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
                    16);
        }
        return b;
    }


    public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString().toUpperCase();
    }

}
