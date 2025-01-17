package si.fri.algotest.tools;

import java.security.MessageDigest;
import java.util.Random;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author tomaz
 */
public class UniqueIDGenerator {
  static String rndSeed = String.valueOf(new Random().nextInt());
  static int    counter = 0;
  
  public static String sha1(String input, int len) {
    String sha1 = null;
    try {
      MessageDigest msdDigest = MessageDigest.getInstance("SHA-1");
      msdDigest.update(input.getBytes("UTF-8"), 0, input.length());
      sha1 = DatatypeConverter.printHexBinary(msdDigest.digest());
    } catch (Exception e) {}
    return sha1.substring(0, len);
  }
  
  public static String getNextID() {
    return sha1((++counter) + rndSeed, 12);
  }
  
  public static String formatNumber(int number, int digits) {
    String result = Integer.toString(number);
    while (result.length() < digits) result = "0" + result;
    return result;
  }
  
  public static void main(String[] args) {
    for (int i = 0; i < 10; i++) {
      System.out.println(getNextID());
    }
  }

}
