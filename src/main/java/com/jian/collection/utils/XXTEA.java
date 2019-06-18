package com.jian.collection.utils;

public class XXTEA {
	
	/**
	 * Encrypt data with key.
	 * 
	 * @param data	
	 * @param key
	 * @return
	 */
	public static byte[] encrypt(byte[] data, byte[] key) {
		if(data.length < 128){
			byte[] temp = new byte[128];
			System.arraycopy(data, 0, temp, 0, data.length);
			data = temp;
		}
		return unsignedShortToByte(
				encrypt(byteToUnsignedShort(data), byteToUnsignedShort(key)));
	}

	/**
	 * Decrypt data with key.
	 * 
	 * @param data
	 * @param key
	 * @return
	 */
	public static byte[] decrypt(byte[] data, byte[] key) {
		return unsignedShortToByte(
				decrypt(byteToUnsignedShort(data), byteToUnsignedShort(key)));
	}
	
	private static int[] byteToUnsignedShort(byte[] src) {
		int count = src.length / 2;
		int[] dest = new int[count];
		for (int i = 0; i < count; i++) {
			//byte & 0x0FF  无符号byte  (0x0ff == 0xff)
	        dest[i] = ((src[i * 2 + 1] & 0x0ff) << 8 | src[i * 2] & 0x0ff) & 0x0ffff;
	    }

		return dest;
	}

	
	private static byte[] unsignedShortToByte(int[] src) {
		int count = src.length ;
		byte[] dest = new byte[count * 2];
		for (int i = 0; i < count; i++) {
	        dest[i * 2] = (byte) src[i];
	        dest[i * 2 + 1] = (byte) (src[i] >>> 8);
	    }

		return dest;
	}
	
	private static int transformToUnsignedShort(int src) {
		return src & 0x0ffff;
	}

	/**
	 * Encrypt data with key.
	 * 
	 * @param v
	 * @param k
	 * @return
	 */
	public static int[] encrypt(int[] v, int[] k) {
		int n = v.length;

		int y = v[0];
		int p;
		int rounds = 5 + 52 / n;
		int sum = 0;
		int z = v[n - 1];
		int delta = 0x79B9;
		int mx = 0;
		do {
			sum = transformToUnsignedShort(sum + delta); 
			int e = ((sum >>> 2) & 3);
			for (p = 0; p < n - 1; p++) {
				y = transformToUnsignedShort(v[p + 1]);
				mx = (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
				z = v[p] =  transformToUnsignedShort(transformToUnsignedShort(v[p]) + transformToUnsignedShort(mx));
			}
			y = v[0];
			mx = (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
			z = v[n-1] =  transformToUnsignedShort(transformToUnsignedShort(v[n-1]) + transformToUnsignedShort(mx));
		} while (--rounds > 0);

		return v;
	}

	/**
	 * Decrypt data with key.
	 * 
	 * @param v
	 * @param k
	 * @return
	 */
	public static int[] decrypt(int[] v, int[] k) {
		int n = v.length;
		int z = v[n - 1], y = v[0], delta = 0x79B9, sum;
		int e;
		int p;
		int rounds = 5 + 52 / n;
		sum = transformToUnsignedShort(rounds * delta);
		y = transformToUnsignedShort(v[0]);
		int mx = 0;
		do {
			e = (sum >>> 2) & 3;
			for (p = n - 1; p > 0; p--) {
				z = transformToUnsignedShort(v[p - 1]);
				mx = (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
				y = v[p] = transformToUnsignedShort(transformToUnsignedShort(v[p])-transformToUnsignedShort(mx));
			}
			z = v[n - 1];
			mx = (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
			y = v[0] = transformToUnsignedShort(transformToUnsignedShort(v[0])-transformToUnsignedShort(mx));
		} while ((sum = transformToUnsignedShort(sum - delta)) != 0);
		return v;
	}


	public static void main(String[] args) {
		String str = "Hello World!";
		byte[] key = {'6','6','6','P','C','M','0','1'};
		
		byte[] data = new byte[128];
		byte[] strs = str.getBytes();
		System.arraycopy(strs, 0, data, 0, strs.length);
		
		byte[] test = XXTEA.encrypt(data, key);
		for (byte b : test) {
			if(b < 0) {
				System.out.print((256+b)+" ");
			}else {
				System.out.print(b+" ");
			}
		}
		System.out.println();
		byte[] res = XXTEA.decrypt(test, key);
		System.out.println(new String(res));
		
		//128
		int[] tea_test_buf = {0x81,0xED,0xFA,0xA0,0x49,0x3B,0xC0,0xD5,0x81,0xD2,0xAB,0x76,0x6C,0x00,0x88,0xD5,0x2C,0x45,0x1A,0xB6,0x6A,0xB6,0xCA,0x2C,0x9C,0xC3,0x8F,0x50,0xB6,0x34,0x1F,0xDE,0xA7,0xA8,0x37,0x0F,0x8A,0x11,0xE0,0x2D,0x90,0xF6,0xDE,0x04,0x82,0xCD,0xED,0x69,0x89,0x13,0x18,0x11,0xE8,0x6B,0xE3,0xE0,0xB6,0x79,0xF7,0xA3,0x2B,0xB2,0x9B,0x1C,0xA1,0x68,0x79,0x6A,0xF1,0xA4,0x06,0xDE,0x6A,0x7D,0x4B,0xD0,0xC1,0x9D,0x58,0x25,0xBA,0x8F,0x0F,0xEA,0x44,0x42,0xB1,0xB4,0x02,0x17,0x1B,0xD4,0xC8,0x64,0x6C,0x6A,0xEA,0xC1,0xA6,0x6C,0x87,0x90,0x40,0x3B,0xBD,0xFA,0xE6,0x65,0x5A,0xBC,0x59,0x30,0xA3,0x6A,0xC4,0x10,0xA5,0x9A,0xAF,0x23,0xE2,0xC2,0x9E,0xE1,0x0C,0xCA,0x88,0xBF};

		byte[] test2 = new byte[tea_test_buf.length];
		for (int i = 0; i < tea_test_buf.length; i++) {
			test2[i] = (byte) tea_test_buf[i];
		}
		
		byte[] res2 = XXTEA.decrypt(test2, key);
		System.out.println(new String(res2));
	}
	
}
