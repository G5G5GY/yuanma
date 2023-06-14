
import java.security.*;

public class Test13 {

    private KeyPair keyPair = null;
    public Test13(){
        try {
            keyPair = KeyPairGenerator.getInstance("SM2", "SwxaJCE").generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        System.out.println("iiiiiiiifdsljfljsafjda;j");
    }


    public String sign(String data){
        try {
            //KeyPair keyPair = KeyPairGenerator.getInstance("SM2", "SwxaJCE").generateKeyPair();
            Signature signature = Signature.getInstance("SM3WithSM2", "SwxaJCE");
            signature.initSign(keyPair.getPrivate());
            signature.update(data.getBytes());
            byte[] out = signature.sign();
            return byte2hex(out);
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("RuntimeException");
        }
    }


   // KeyPair keyPair = KeyPairGenerator.getInstance("SM2", "SwxaJCE").generateKeyPair();
    public void validate(String data,String sign){

        try {

            Signature signatureVerify = Signature.getInstance("SM3WithSM2","SwxaJCE");
            signatureVerify.initVerify(keyPair.getPublic());
            signatureVerify.update(data.getBytes());
            boolean flag = signatureVerify.verify(hex2byte(sign));
            if(!flag){
                throw new RuntimeException("RuntimeException1122");
            }
        } catch(Exception e) {
            e.printStackTrace();
            throw new RuntimeException("RuntimeException");
        }
    }

    public void test() throws Exception, NoSuchProviderException {
        String data ="XXXX";
        KeyPair keyPair = KeyPairGenerator.getInstance("SM2","SwxaJCE").generateKeyPair();
        Signature signature = Signature.getInstance("SM3WithSM2","SwxaJCE");
        signature.initSign(keyPair.getPrivate());
        signature.update(data.getBytes());
        byte[] out = signature.sign();
        System.out.println("=============44"+byte2hex(out));

       // KeyPair keyPair11 = KeyPairGenerator.getInstance("SM2", "SwxaJCE").generateKeyPair();
        Signature signatureVerify = Signature.getInstance("SM3WithSM2","SwxaJCE");
        signatureVerify.initVerify(keyPair.getPublic());
        signatureVerify.update(data.getBytes());
        System.out.println("===================================");
        boolean flag = signatureVerify.verify(hex2byte(byte2hex(out)));
        System.out.println("=============49"+flag);
    }

    public static void main(String[] args) throws Exception {

        KeyPair keyPair = KeyPairGenerator.getInstance("SM2", "SwxaJCE").generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        System.out.println(publicKey);
        System.out.println(privateKey);
        System.out.println("===============================");
        keyPair = KeyPairGenerator.getInstance("SM2", "SwxaJCE").generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
        System.out.println(publicKey);
        System.out.println(privateKey);


      // String str = test13.sign("kkkkkkkkkkkkkkkkkkkk");
       //test13.validate("kkkkkkkkkkkkkkkkkkkk",str);


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
