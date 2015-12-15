package xap.lui.core.util;

public class HashFunctionLibrary  {
 public void testHash() {
  // �������
//  System.out.println(this.RSHash("1111"));
//  System.out.println(this.JSHash("1111"));
//  System.out.println(this.PJWHash("1111"));
//  System.out.println(this.ELFHash("1111"));
//  System.out.println(this.BKDRHash("1111"));
//  System.out.println(this.SDBMHash("1111"));
//  System.out.println(this.DJBHash("1111"));
//  System.out.println(this.DEKHash("1111"));
//  System.out.println(this.BPHash("1111"));
//  System.out.println(this.FNVHash("1111"));
//  System.out.println(this.APHash("1111"));
//  System.out.println("-----------------");
//  System.out.println(this.RSHash("2222"));
//  System.out.println(this.JSHash("2222"));
//  System.out.println(this.PJWHash("2222"));
//  System.out.println(this.ELFHash("2222"));
//  System.out.println(this.BKDRHash("2222"));
//  System.out.println(this.SDBMHash("2222"));
//  System.out.println(this.DJBHash("2222"));
//  System.out.println(this.DEKHash("2222"));
//  System.out.println(this.BPHash("2222"));
//  System.out.println(this.FNVHash("2222"));
//  System.out.println(this.APHash("2222"));
 }

 public static long RSHash(String str) {
  int b = 378551;
  int a = 63689;
  long hash = 0;
  for (int i = 0; i < str.length(); i++) {
   hash = hash * a + str.charAt(i);
   a = a * b;
  }
  return hash;
 }

 public static long JSHash(String str) {
  long hash = 1315423911;
  for (int i = 0; i < str.length(); i++) {
   hash ^= ((hash << 5) + str.charAt(i) + (hash >> 2));
  }
  return hash;
 }

 public static long PJWHash(String str) {
  long BitsInUnsignedInt = (long) (4 * 8);
  long ThreeQuarters = (long) ((BitsInUnsignedInt * 3) / 4);
  long OneEighth = (long) (BitsInUnsignedInt / 8);
  long HighBits = (long) (0xFFFFFFFF) << (BitsInUnsignedInt - OneEighth);
  long hash = 0;
  long test = 0;
  for (int i = 0; i < str.length(); i++) {
   hash = (hash << OneEighth) + str.charAt(i);
   if ((test = hash & HighBits) != 0) {
    hash = ((hash ^ (test >> ThreeQuarters)) & (~HighBits));
   }
  }
  return hash;
 }

 public static long ELFHash(String str) {
  long hash = 0;
  long x = 0;
  for (int i = 0; i < str.length(); i++) {
   hash = (hash << 4) + str.charAt(i);
   if ((x = hash & 0xF0000000L) != 0) {
    hash ^= (x >> 24);
   }
   hash &= ~x;
  }
  return hash;
 }

 public static long BKDRHash(String str) {
  long seed = 131; // 31 131 1313 13131 131313 etc..
  long hash = 0;
  for (int i = 0; i < str.length(); i++) {
   hash = (hash * seed) + str.charAt(i);
  }
  return hash;
 }

 public  static long SDBMHash(String str) {
  long hash = 0;
  for (int i = 0; i < str.length(); i++) {
   hash = str.charAt(i) + (hash << 6) + (hash << 16) - hash;
  }
  return hash;
 }

 public  static long DJBHash(String str) {
  long hash = 5381;
  for (int i = 0; i < str.length(); i++) {
   hash = ((hash << 5) + hash) + str.charAt(i);
  }
  return hash;
 }

 public static long DEKHash(String str) {
  long hash = str.length();
  for (int i = 0; i < str.length(); i++) {
   hash = ((hash << 5) ^ (hash >> 27)) ^ str.charAt(i);
  }
  return hash;
 }

 public long BPHash(String str) {
  long hash = 0;
  for (int i = 0; i < str.length(); i++) {
   hash = hash << 7 ^ str.charAt(i);
  }
  return hash;
 }

 public static  long FNVHash(String str) {
  long fnv_prime = 0x811C9DC5;
  long hash = 0;
  for (int i = 0; i < str.length(); i++) {
   hash *= fnv_prime;
   hash ^= str.charAt(i);
  }
  return hash;
 }

 public static long APHash(String str) {
  long hash = 0xAAAAAAAA;
  for (int i = 0; i < str.length(); i++) {
   if ((i & 1) == 0) {
    hash ^= ((hash << 7) ^ str.charAt(i) ^ (hash >> 3));
   } else {
    hash ^= (~((hash << 11) ^ str.charAt(i) ^ (hash >> 5)));
   }
  }
  return hash;
 }
}