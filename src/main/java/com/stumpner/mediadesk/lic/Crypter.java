package com.stumpner.mediadesk.lic;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.PBEKeySpec;

/*********************************************************
 Copyright 2017 by Franz STUMPNER (franz@stumpner.com)

 openMEDIADESK is licensed under Apache License Version 2.0

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 *********************************************************/

/**
 * Created by IntelliJ IDEA.
 * User: franz.stumpner
 * Date: 27.11.2006
 * Time: 18:51:38
 * To change this template use File | Settings | File Templates.
 */
public class Crypter {

  final private transient static String password = "susidepassword";
  final private transient byte [] salt = { (byte) 0xc9, (byte) 0xc9,(byte) 0xc9,(byte) 0xc9,(byte) 0xc9,(byte) 0xc9,(byte) 0xc9,(byte) 0xc9};
  final int iterations = 3;

  protected Crypter() {
//    java.security.Security.addProvider(new com.sun.crypto.provider.SunJCE()); // implizit bereits erledigt!
  }

  /** instance */
  private static Crypter instance;

  /** Singleton Factory
   * @return instance
   */
  public static Crypter getInstance () {
    if (instance == null) {
      instance = new Crypter ();
    }
    return instance;

  }


  /** Notwendige Instanczen */
  private Cipher encryptCipher;
  private Cipher decryptCipher;
  private sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
  private sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();

  /** Verwendete Zeichendecodierung */
  private String charset = "UTF16";

  /**
   * Initialisiert den Verschlüsselungsmechanismus
   * @param pass char[]
   * @param salt byte[]
   * @param iterations int
   * @throws SecurityException
   */
  public void initCrypt (final char[] pass, final byte[] salt, final int iterations) throws SecurityException {
    try {
      final PBEParameterSpec ps = new PBEParameterSpec(salt, 20);
      final SecretKeyFactory kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
      final SecretKey k = kf.generateSecret(new PBEKeySpec(pass));
      encryptCipher = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
      encryptCipher.init (Cipher.ENCRYPT_MODE, k, ps);
      decryptCipher = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
      decryptCipher.init (Cipher.DECRYPT_MODE, k, ps);
    }
    catch (Exception e) {
      throw new SecurityException("Could not initialize CryptoLibrary: " +
                                  e.getMessage());
    }
  }

  /**
   * Verschlüsselt eine Zeichenkette
   *
   * @param str Description of the Parameter
   * @return String the encrypted string.
   * @exception SecurityException Description of the Exception
   */
  public synchronized String encrypt(String str) throws SecurityException {
    try {
      byte[] b = str.getBytes(this.charset);
      byte[] enc = encryptCipher.doFinal(b);
      return encoder.encode(enc);
    }
    catch (Exception e){
      throw new SecurityException("Could not encrypt: " + e.getMessage());
    }

  }

  /**
   * Entschlüsselt eine Zeichenkette, welche mit der Methode encrypt
   * verschlüsselt wurde.
   *
   * @param str Description of the Parameter
   * @return String the encrypted string.
   * @exception SecurityException Description of the Exception
   */
  public synchronized String decrypt(String str) throws SecurityException  {
    try {
      byte[] dec = decoder.decodeBuffer(str);
      byte[] b = decryptCipher.doFinal(dec);
      return new String(b, this.charset);
    }
    catch (Exception e) {
      throw new SecurityException("Could not decrypt: " + e.getMessage());
    }
  }

  public String encrypt (String string, String password) {
    Crypter man = Crypter.getInstance();
    man.initCrypt(password.toCharArray(), man.salt, man.iterations);
    final String encrypted = man.encrypt(string);
    //System.out.println ("Verschlüsselt :"+encrypted);
      return encrypted;
//    final String decrypted = man.decrypt (encrypted);
//    System.out.println("Entschlüsselt :"+decrypted);
  }

  public String decrypt (String string, String password) {
    Crypter man = Crypter.getInstance();
    man.initCrypt(password.toCharArray(), man.salt, man.iterations);
    final String decrypted = man.decrypt (string);
    //System.out.println("Entschlüsselt :"+decrypted);
      return decrypted;
  }
}
