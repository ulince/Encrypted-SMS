package com.exmaple.aes;
import java.util.Arrays; //AES Master

import com.example.utils.PolynomialBytes;

public class AES {
    private int[][] state;
    private int[][] key;
    private RoundKey[] roundKeys;
    
    public int[][] getState(){
    	return this.state;
    }
    
    
    public int inverseSbox[][] = 
    	{
    	   {0x52, 0x09, 0x6A, 0xD5, 0x30, 0x36, 0xA5, 0x38, 0xBF, 0x40, 0xA3, 0x9E, 0x81, 0xF3, 0xD7, 0xFB},
    	   {0x7C, 0xE3, 0x39, 0x82, 0x9B, 0x2F, 0xFF, 0x87, 0x34, 0x8E, 0x43, 0x44, 0xC4, 0xDE, 0xE9, 0xCB},
    	   {0x54, 0x7B, 0x94, 0x32, 0xA6, 0xC2, 0x23, 0x3D, 0xEE, 0x4C, 0x95, 0x0B, 0x42, 0xFA, 0xC3, 0x4E},
    	   {0x08, 0x2E, 0xA1, 0x66, 0x28, 0xD9, 0x24, 0xB2, 0x76, 0x5B, 0xA2, 0x49, 0x6D, 0x8B, 0xD1, 0x25},
    	   {0x72, 0xF8, 0xF6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xD4, 0xA4, 0x5C, 0xCC, 0x5D, 0x65, 0xB6, 0x92},
    	   {0x6C, 0x70, 0x48, 0x50, 0xFD, 0xED, 0xB9, 0xDA, 0x5E, 0x15, 0x46, 0x57, 0xA7, 0x8D, 0x9D, 0x84},
    	   {0x90, 0xD8, 0xAB, 0x00, 0x8C, 0xBC, 0xD3, 0x0A, 0xF7, 0xE4, 0x58, 0x05, 0xB8, 0xB3, 0x45, 0x06},
    	   {0xD0, 0x2C, 0x1E, 0x8F, 0xCA, 0x3F, 0x0F, 0x02, 0xC1, 0xAF, 0xBD, 0x03, 0x01, 0x13, 0x8A, 0x6B},
    	   {0x3A, 0x91, 0x11, 0x41, 0x4F, 0x67, 0xDC, 0xEA, 0x97, 0xF2, 0xCF, 0xCE, 0xF0, 0xB4, 0xE6, 0x73},
    	   {0x96, 0xAC, 0x74, 0x22, 0xE7, 0xAD, 0x35, 0x85, 0xE2, 0xF9, 0x37, 0xE8, 0x1C, 0x75, 0xDF, 0x6E},
    	   {0x47, 0xF1, 0x1A, 0x71, 0x1D, 0x29, 0xC5, 0x89, 0x6F, 0xB7, 0x62, 0x0E, 0xAA, 0x18, 0xBE, 0x1B},
    	   {0xFC, 0x56, 0x3E, 0x4B, 0xC6, 0xD2, 0x79, 0x20, 0x9A, 0xDB, 0xC0, 0xFE, 0x78, 0xCD, 0x5A, 0xF4},
    	   {0x1F, 0xDD, 0xA8, 0x33, 0x88, 0x07, 0xC7, 0x31, 0xB1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xEC, 0x5F},
    	   {0x60, 0x51, 0x7F, 0xA9, 0x19, 0xB5, 0x4A, 0x0D, 0x2D, 0xE5, 0x7A, 0x9F, 0x93, 0xC9, 0x9C, 0xEF},
    	   {0xA0, 0xE0, 0x3B, 0x4D, 0xAE, 0x2A, 0xF5, 0xB0, 0xC8, 0xEB, 0xBB, 0x3C, 0x83, 0x53, 0x99, 0x61},
    	   {0x17, 0x2B, 0x04, 0x7E, 0xBA, 0x77, 0xD6, 0x26, 0xE1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0C, 0x7d}
    	};
    
    public int Sbox[][] = 
    	{
    	   {0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F, 0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76},
    	   {0xCA, 0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF, 0x9C, 0xA4, 0x72, 0xC0},
    	   {0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC, 0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15},
    	   {0x04, 0xC7, 0x23, 0xC3, 0x18, 0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2, 0xEB, 0x27, 0xB2, 0x75},
    	   {0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0, 0x52, 0x3B, 0xD6, 0xB3, 0x29, 0xE3, 0x2F, 0x84},
    	   {0x53, 0xD1, 0x00, 0xED, 0x20, 0xFC, 0xB1, 0x5B, 0x6A, 0xCB, 0xBE, 0x39, 0x4A, 0x4C, 0x58, 0xCF},
    	   {0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85, 0x45, 0xF9, 0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8},
    	   {0x51, 0xA3, 0x40, 0x8F, 0x92, 0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21, 0x10, 0xFF, 0xF3, 0xD2},
    	   {0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4, 0xA7, 0x7E, 0x3D, 0x64, 0x5D, 0x19, 0x73},
    	   {0x60, 0x81, 0x4F, 0xDC, 0x22, 0x2A, 0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB},
    	   {0xE0, 0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC, 0x62, 0x91, 0x95, 0xE4, 0x79},
    	   {0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E, 0xA9, 0x6C, 0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08},
    	   {0xBA, 0x78, 0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F, 0x4B, 0xBD, 0x8B, 0x8A},
    	   {0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E, 0x61, 0x35, 0x57, 0xB9, 0x86, 0xC1, 0x1D, 0x9E},
    	   {0xE1, 0xF8, 0x98, 0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B, 0x1E, 0x87, 0xE9, 0xCE, 0x55, 0x28, 0xDF},
    	   {0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68, 0x41, 0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16}
    	};

    /**
     * @param state The initial state. For encryption, this is the plaintext. 
     */
    public AES(int[][] state, int[][] key) {
        this.state = state;
        this.key = key;
        roundKeys = keyExpansion(key);
    }



    public void encrypt() {
        addRoundKey(roundKeys[0]);

        for (int i = 1; i <= 9; i++) {
            subBytes();
            shiftRows();
            mixColumn();
            addRoundKey(roundKeys[i]);
        }
        subBytes();
        shiftRows();
        addRoundKey(roundKeys[10]);
    }
    
    public void decrypt(){
        addRoundKey(roundKeys[10]);
           
        for (int i = 9; i > 0; i--) {
            IshiftRows();
            IsubBytes();
            addRoundKey(roundKeys[i]);
            ImixColumn();          
        } 
        IshiftRows();
        IsubBytes();
        addRoundKey(roundKeys[0]);
    }

    /**
     * XOR the state matrix by the round key
     * @param key The round key
     */
    private void addRoundKey(RoundKey key) {
        state = Utils.XOR(key.keyBytes, state);
    }

    /**
     * Calculate the round keys from the initial key
     * @param key
     * @return an array of round keys
     */
    private RoundKey[] keyExpansion(int[][] key) {
        KeyCol[] W = new KeyCol[44];

        key = Utils.rotate(key);

        for (int i = 0; i < 4; i++) {
            W[i] = new KeyCol();
            W[i].colBytes = key[i];
        }

        for (int i = 4; i <= 43; i++) {
            W[i] = new KeyCol();
            if (i % 4 != 0) {
                W[i].colBytes =
                        Utils.XOR(W[i - 1].colBytes, W[i - 4].colBytes);
            }
            else {
                int[] W1Prev = W[i - 1].colBytes;
                int[] W4Prev = W[i - 4].colBytes;
                PolynomialBytes x = new PolynomialBytes(2);
                x = x.pow((i - 4) / 4);
                int[] rhs =
                        { subByteOperation(W1Prev[1]) ^ x.toByte(),
                                subByteOperation(W1Prev[2]),
                                subByteOperation(W1Prev[3]),
                                subByteOperation(W1Prev[0]) };
                W[i].colBytes = Utils.XOR(W4Prev, rhs);
            }
        }

        return getKeysFromW(W);
    }

    /**
     * From W, extract the round keys.
     * @param W The calculated columns from keyExpansion
     * @return The round keys
     */
    private RoundKey[] getKeysFromW(KeyCol[] W) {
        int numKeys = W.length / 4;
        RoundKey[] keys = new RoundKey[numKeys];
        for (int i = 0; i < numKeys; i++) {
            keys[i] = new RoundKey();
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    keys[i].keyBytes[j][k] = W[4 * i + k].colBytes[j];
                }
            }
        }
        return keys;
    }

    /**
     * Substitute the bytes in the entire state matrix
     */
    private void subBytes() {
    	int row, column;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                String hexString = Integer.toHexString(state[i][j]);
                if(hexString.length() == 1){
                	row = 0;
                	column = Integer.parseInt(String.valueOf(hexString.charAt(0)),16);
                }else{
                	row = Integer.parseInt(String.valueOf(hexString.charAt(0)),16);
                	column = Integer.parseInt(String.valueOf(hexString.charAt(1)),16);
                }
                state[i][j] = Sbox[row][column];
            }
        }
    }

    /**
     * Substitute a byte.
     * @return The byte
     */
    private int subByteOperation(int inInt) {
        int z = 0;
        PolynomialBytes y = new PolynomialBytes(inInt);
        z = y.inverse().toByte();
        z = subByteMult(z);
        return z;
    }

    /**
     * Perform the matrix multiplication when substituting a byte.
     * @param inByte The byte to multiply
     * @return The result
     */
    private int subByteMult(int inByte) {
        int[] outBits = new int[8];
        int[] bits = Utils.byteToReversedBits(inByte);
        int[] mult = { 1, 0, 0, 0, 1, 1, 1, 1 };
        int[] add = { 1, 1, 0, 0, 0, 1, 1, 0 };
        for (int i = 0; i < 8; i++) {
            int tmpSum = 0;
            for (int j = 0; j < 8; j++) {
                int multBit = mult[Utils.posMod(j - i, 8)];
                tmpSum += multBit * bits[j];
            }
            outBits[i] = (tmpSum + add[i]) % 2;
        }
        return Utils.reverseBitsToByte(outBits);
    }

    /**
     * Shift the rows of the state matrix.
     */
    private void shiftRows() {
        int[][] oldState = new int[state.length][state.length];

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state.length; j++) {
                oldState[i][j] = state[i][j];
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = oldState[i][(i + j) % 4];
            }
        }
    }
    
    /*
     * Shift the rows inversely
     */
    private void IshiftRows(){
    	int[][] oldState = new int[state.length][state.length];

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state.length; j++) {
                oldState[i][j] = state[i][j];
            }
        }
        

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = oldState[i][(j + (4 - i))% 4];
                //System.out.println(i+","+j+"->"+i+","+((j + (4 - i))% 4));
            }
        }
    }

    /**
     * Generate the matrix M for column mixing.
     */
    static private PolynomialBytes[][] M = new PolynomialBytes[4][4];
    {
        M[0][0] = new PolynomialBytes(2);
        M[0][1] = new PolynomialBytes(3);
        M[0][2] = new PolynomialBytes(1);
        M[0][3] = new PolynomialBytes(1);
        M[1][0] = new PolynomialBytes(1);
        M[1][1] = new PolynomialBytes(2);
        M[1][2] = new PolynomialBytes(3);
        M[1][3] = new PolynomialBytes(1);
        M[2][0] = new PolynomialBytes(1);
        M[2][1] = new PolynomialBytes(1);
        M[2][2] = new PolynomialBytes(2);
        M[2][3] = new PolynomialBytes(3);
        M[3][0] = new PolynomialBytes(3);
        M[3][1] = new PolynomialBytes(1);
        M[3][2] = new PolynomialBytes(1);
        M[3][3] = new PolynomialBytes(2);

    }
    
    /**
     * Generate the inverse matrix M for column mixing.
     */
    static private PolynomialBytes[][] IM = new PolynomialBytes[4][4];
    {
        IM[0][0] = new PolynomialBytes(0xe);
        IM[0][1] = new PolynomialBytes(0xb);
        IM[0][2] = new PolynomialBytes(0xd);
        IM[0][3] = new PolynomialBytes(0x9);
        IM[1][0] = new PolynomialBytes(0x9);
        IM[1][1] = new PolynomialBytes(0xe);
        IM[1][2] = new PolynomialBytes(0xb);
        IM[1][3] = new PolynomialBytes(0xd);
        IM[2][0] = new PolynomialBytes(0xd);
        IM[2][1] = new PolynomialBytes(0x9);
        IM[2][2] = new PolynomialBytes(0xe);
        IM[2][3] = new PolynomialBytes(0xb);
        IM[3][0] = new PolynomialBytes(0xb);
        IM[3][1] = new PolynomialBytes(0xd);
        IM[3][2] = new PolynomialBytes(0x9);
        IM[3][3] = new PolynomialBytes(0xe);

    }

    /**
     * Mix the columns
     */
    private void mixColumn() {
        // create a 2-D array to hold the PolyBytes for calculation.
        PolynomialBytes[][] statepb = new PolynomialBytes[4][4];
        //Matrix multiplication
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                statepb[i][j] = new PolynomialBytes(0);
                for (int k = 0; k < 4; k++) {
                    statepb[i][j] =
                            statepb[i][j].add(M[i][k].mul(new PolynomialBytes(
                                    state[k][j])));
                }
            }
        }
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = statepb[i][j].toByte();
            }
        }
    }
    
    /**
     * Mix the columns for decryption
     */
    private void ImixColumn() {
        // create a 2-D array to hold the PolyBytes for calculation.
        PolynomialBytes[][] statepb = new PolynomialBytes[4][4];
        // Do the matrix multiplication
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                statepb[i][j] = new PolynomialBytes(0);
                for (int k = 0; k < 4; k++) {
                    statepb[i][j] =
                            statepb[i][j].add(IM[i][k].mul(new PolynomialBytes(
                                    state[k][j])));
                }
            }
        }
        // convert from PolyByte to integer byte.
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[i][j] = statepb[i][j].toByte();
            }
        }
    }

    /**
     * A class that wraps a 2d array to hold the round keys in.
     * Used for readability.
     */
    private class RoundKey {
        public int[][] keyBytes;

        public RoundKey() {
            keyBytes = new int[4][4];
        }

    }

    /**
     * A class that wraps an array to hold key columns in.
     * Used for readability. 
     */
    private class KeyCol {
        public int[] colBytes;

        public String toString() {
            return Arrays.toString(colBytes);
        }
    }
    
    /*
     * Substitutes bytes using inverse sbox
     */
    private void IsubBytes() {
    	int row, column;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                String hexString = Integer.toHexString(state[i][j]);
                if(hexString.length() == 1){
                	row = 0;
                	column = Integer.parseInt(String.valueOf(hexString.charAt(0)),16);
                }else{
                	row = Integer.parseInt(String.valueOf(hexString.charAt(0)),16);
                	column = Integer.parseInt(String.valueOf(hexString.charAt(1)),16);
                }
                state[i][j] = inverseSbox[row][column];
            }
        }
    }
    
    public void printKeyExpansion() {
        RoundKey[] roundKeys = keyExpansion(key);

        for (int i = 0; i < roundKeys.length; i++) {
        	Utils.printMatrix((roundKeys[i].keyBytes));
        }
    }
}
