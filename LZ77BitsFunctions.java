/******************************************************************************
 *
 * Module: LZ77 Bits Functions
 *
 * File Name: LZ77BitsFunctions.java
 *
 * Description: This class contains the functions used to pack and unpack the tags
 *
 * Authors: Habiba Elbehairy, Mariam Haitham
 *
 *******************************************************************************/

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LZ77BitsFunctions {

    /*
     * This function takes the length, position and nextSymbol of the tag and packs them into a single integer
     * The first 12 bits are for the length
     * The next 12 bits are for the position
     * The last 8 bits are for the nextSymbol
     * The function returns the packed integer
     */
    public int packFields(int length, int pos, char nextSymbol) {
        int packedValue = (nextSymbol & 0xFF) << 24 | (pos & 0xFFF) << 12 | (length & 0xFFF);
        return packedValue;
    }

    public LZ77 unpackFields(int packedValue) {
        // Unpack the values from the single integer using bitwise operations
        char nextSymbol = (char) ((packedValue >> 24) & 0xFF);
        int pos = (packedValue >> 12) & 0xFFF;
        int length = packedValue & 0xFFF;
        return new LZ77(length, pos, nextSymbol);
    }

    public void writePackedIntToFile(int packedValue, String filePath) {
        // Write the packed integer to the file
        try (OutputStream output = new FileOutputStream(filePath, true)) {
            for (int i = 24; i >= 0; i -= 8) {
                output.write((packedValue >> i) & 0xFF);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
