package server;

import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class Crypto 
{
    private static final String ALGO ="AES";
	private byte[] keyVal;

	public Crypto(String key)
        {
		keyVal =key.getBytes();
	} 

	public String encrypt(String Data)throws Exception{
		Key key =generateKey();
		Cipher c=Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte [] encVal=c.doFinal(Data.getBytes());
		String encryptedValue =new BASE64Encoder().encode(encVal);	
		return encryptedValue;
	}

	public String decrypt(String encryptedData)throws Exception
        {
		Key key =generateKey();
		Cipher c=Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
		byte [] decVal=c.doFinal(decordedValue);
		String decryptedValue =	new String(decVal);
		return decryptedValue;
	}

	private Key generateKey() throws Exception
        {
	Key key =new SecretKeySpec(keyVal,ALGO);
	return key;
	}  
}
