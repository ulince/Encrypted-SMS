package com.exmaple.aes;
import java.io.UnsupportedEncodingException;
//import org.apache.commons.codec.binary.StringUtils;
import java.nio.charset.Charset;
import java.util.Arrays;
import android.util.Base64;
//import org.apache.commons.codec.binary.Base64;

public class Utils {
	public static void msg(String message) {
		System.out.println(message);
	}

	/**
	 * Print a byte as a 2-character hexadecimal digit
	 */
	public static String byteHexStr(int message) {
		return String.format("%02X", message);
	}

	/**
	 * Convert a byte to an array of bits.
	 * 
	 * @return the array of bits.
	 */
	public static int[] byteToBits(int inByte) {
		int[] bits = new int[8];
		int mask = 0x80;
		for (int i = 0; i < 8; i++) {
			bits[i] = (inByte & mask) != 0 ? 1 : 0;
			mask >>= 1;
		}
		return bits;
	}

	/**
	 * Convert a byte into a reversed array of bits.
	 * 
	 * @return the reversed array of bits.
	 */
	public static int[] byteToReversedBits(int inByte) {
		int[] bits = new int[8];
		int mask = 1;
		for (int i = 0; i < 8; i++) {
			bits[i] = (inByte & mask) != 0 ? 1 : 0;
			mask <<= 1;
		}
		return bits;
	}

	/**
	 * Convert an array of bits to a byte.
	 * 
	 * @return the byte
	 */
	public static int bitsToByte(int[] bits) {
		int byteVal = 0;
		for (int i = 0; i < 8; i++) {
			byteVal <<= 1;
			byteVal |= bits[i];
		}
		return byteVal;
	}

	/**
	 * Convert an array of reversed bits to a byte.
	 * 
	 * @return the byte
	 */
	public static int reverseBitsToByte(int[] bits) {
		int byteVal = 0;
		for (int i = 7; i >= 0; i--) {
			byteVal <<= 1;
			byteVal |= bits[i];
		}
		return byteVal;
	}

	/**
	 * Display a 2d matrix
	 */
	public static void display(int[][] matrix) {
		Utils.msg(matrixToString(matrix));
	}

	public static String matrixToString(int[][] matrix) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				b.append(Utils.byteHexStr(matrix[j][i]) + " ");
			}
			b.append(" ");
		}
		return b.toString();
	}
	
	public static String matrixToHexString(int[][] matrix) {
		StringBuilder b = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				b.append(Utils.byteHexStr(matrix[i][j]) + " ");
			}
			b.append(" ");
		}
		return b.toString();
	}

	/**
	 * @return the positive modulus of 2 integers
	 */
	public static int posMod(int rhs, int lhs) {
		return (rhs % lhs + lhs) % lhs;
	}

	/**
	 * @return the XOR of the elements of the 2d matrices
	 */
	public static int[][] XOR(int[][] lhs, int[][] rhs) {
		int[][] out = new int[lhs.length][lhs.length];
		for (int i = 0; i < lhs.length; i++) {
			for (int j = 0; j < lhs.length; j++) {
				out[i][j] = lhs[i][j] ^ rhs[i][j];
			}
		}
		return out;
	}

	/**
	 * @return the XOR of the elements of the matrices
	 */
	public static int[] XOR(int[] lhs, int[] rhs) {
		int[] out = new int[lhs.length];
		for (int i = 0; i < lhs.length; i++) {
			out[i] = lhs[i] ^ rhs[i];
		}
		return out;
	}
	
	/*
	 * @return return a int[][] encoding of the string
	 */
	public static int[][] stringToBytesEncrypt(String str){
		int result[][] = new int[4][4];
		byte bytes[] = null;
		try {
			bytes = str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int k = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (str.length() == 16) {
					result[i][j] = new Byte(bytes[k]).intValue();
					k++;
				} else {
					if (k < str.length()) {
						result[i][j] = new Byte(bytes[k]).intValue();
						k++;
					} else {
						result[i][j] = 0;
						k++;
					}
				}
			}
		}
		return result;
	}

	/*
	 * @return return a int[][] base64 decoding of the string
	 */
	public static int[][] stringToBytesDecrypt(String msg) {
		int result[][] = new int[4][4];
		byte array[]= null;
		//Base64 b = new Base64();
		//array = Base64.decodeBase64(msg);
		array = Base64.decode(msg, Base64.DEFAULT); 
		System.out.println(msg+"\n------------------");
		/*for(byte b:array){
			System.out.print(Integer.toBinaryString((int)b)+" ");
		}
		*/
		int k = 0;
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				result[i][j] = array[k] & 0xff;
				k++;
			}
		}	
		return result;
	}

	/*
	 * @return a String of a int[][]
	 */
	public static String bytesToStringEncrypt(int bytes[][]) {
		byte array[] = new byte[16];
		int k = 0;
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				array[k] = (byte) bytes[i][j];
				k++;
			}
		}
		String result = "";
		//array = Base64.encodeBase64String(array);	
		result = Base64.encodeToString(array,Base64.DEFAULT);	
		return result;//new String(array, "UTF-8");
		//return "";
	}
	
	/*
	 * @return a String of a int[][]
	 */
	public static String bytesToStringDecrypt(int bytes[][]) {
		byte array[] = new byte[16];
		int k = 0;
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				array[k] = (byte) bytes[i][j];
				k++;
			}
		}
		try {
			return new String(array, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/*
	 * @return rotates 2d array
	 */
	public static int[][] rotate(int toRotate[][]) {
		int out[][] = new int[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				out[i][j] = toRotate[j][i];
			}
		}
		return out;
	}
	

	
	public static void printMatrix(int matrix[][]) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				System.out.print(byteHexStr(matrix[i][j])+" ");
			}
			System.out.print(" ");
		}
		System.out.println();
	}
	

}
