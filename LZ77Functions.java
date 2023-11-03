/******************************************************************************
 *
 * Module: LZ77 Functions
 *
 * File Name: LZ77Functions.java
 *
 * Description: This class contains the functions used to compress and decompress
 *
 * Authors: Habiba Elbehairy, Mariam Haitham.
 *
 *******************************************************************************/

import java.io.*;
import java.util.*;

public class LZ77Functions {
    /*
     * the function compresses the data in the file and returns a list of tags
     * the function takes the file path, the size of the search buffer and the size
     * of the look ahead buffer
     */
    public List<LZ77> compressToLZ77(String filePath, int SB_size, int LHB_size) throws IOException, Exception {
        /*
         * This part of the code reads the content from the file specified by the filePath and appends it to a StringBuilder named sb.
         * The BufferedReader is used to read the file content line by line.
         */
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        /*
         * This part of the code initializes a list of LZ77 tags named tags and an
         * integer i to keep track of the current position in the data.
         */
        String data = sb.toString();
        List<LZ77> tags = new ArrayList<>();
        int i = 0;
        while (i < data.length()) {
            /*
             * These lines initialize variables for the length of the match (length),
             * the position of the match (pos), and the next symbol after the match (nextSymbol).
             * They are used to keep track of the current longest match found.
             *
             */
            int length = 0;
            int pos = 0;
            char nextSymbol = data.charAt(i);
            /*
             * This part of the code iterates through the sliding window to find the longest match.
             * This for loop iterates through the preceding characters of the current index i
             * (within the range of the last look head buffer LHB_size)
             */
            for (int j = i - 1; j >= 0 && i - j <= LHB_size; j--) {
                /*
                 * k and l are used to keep track of the current position in the sliding window
                 * and the look ahead buffer, respectively.
                 */
                int k = j;
                int l = i;
                int currLength = 0;
                /*
                 * This while loop iterates through the sliding window to find the longest match.
                 */
                while (k >= 0 && l < data.length() && data.charAt(k) == data.charAt(l) && currLength < SB_size) {
                    k++;
                    l++;
                    currLength++;
                }
                /*
                 * The if statement checks if the current length of the match is greater than the previous longest match.
                 * If it is, it updates the length, position, and next symbol of the longest match.
                 */
                if (currLength > length) {
                    length = currLength;
                    pos = i - j;
                    nextSymbol = l < data.length() ? data.charAt(l) : '\0';
                }
            }
            /*
             * This part of the code adds the longest match to the list of tags.
             */
            tags.add(new LZ77(length, pos, nextSymbol));
            i += length + 1;
        }
        /*
         * This part prints the compressed text, displaying the generated LZ77 tags.
         */
        System.out.println("Compressed Text: ");
        for (LZ77 tag : tags) {
            System.out.println(tag.toString());
        }
        System.out.println("Data is added to the fileCompressed.txt" + "\n");
        /*
         * This part writes the compressed data to a file named "fileCompressed.txt" using a custom LZ77BitsFunctions class.
         * The packFields method packs the length, position, and next symbol into an integer,
         * and the writePackedIntToFile method writes the packed integer to the file.
         */
        LZ77BitsFunctions lz77BitsFunctions = new LZ77BitsFunctions();
        File compressed = new File("fileCompressed.txt");
        compressed.createNewFile();
        try (FileWriter writer = new FileWriter(compressed)) {
            for (LZ77 tag : tags) {
                int packedValue = lz77BitsFunctions.packFields(tag.getLength(), tag.getPosition(), tag.getNextSymbol());
                lz77BitsFunctions.writePackedIntToFile(packedValue, "fileCompressed.txt");
            }
        }
        return tags;
    }


    /*
     * the function decompresses the tags in the file and returns the decompressed data
     * the function takes the file path of the file that contains the tags
     * the function writes the decompressed data to a file called fileDecompressed.txt
     * the function returns the decompressed data
     */
    public void decompressFromLZ77s(String TagsFileName) {

        try (InputStream inputStream = new FileInputStream(TagsFileName)) {
            /*
             * a byte array buffer of size 4 and a StringBuilder object finalOutput are initialized.
             * buffer is used to read bytes from the input stream, while finalOutput is used to store the decompressed data.
             */
            byte[] buffer = new byte[4];
            StringBuilder finalOutput = new StringBuilder();
            /*
             * The while loop reads the data from the input stream into the buffer array until there is no more data to read.
             * It then unpacks the bytes into an int packedValue using bitwise operations and the unpackFields method of the lz77BitsFunctions object.
             */
            LZ77BitsFunctions lz77BitsFunctions = new LZ77BitsFunctions();
            while (inputStream.read(buffer) != -1) {
                int packedValue = 0;
                for (int i = 0; i < 4; i++) {
                    packedValue |= (buffer[i] & 0xFF) << (24 - i * 8);
                }
                LZ77 lz77 = lz77BitsFunctions.unpackFields(packedValue);
                /*
                 * If the length of the LZ77 is 0, there is no repeated substring in the decompressed data.
                 * we appends the next symbol to the finalOutput.
                 * This happens when there is no match found in the sliding window, indicating that the symbol is unique.
                 */
                if (lz77.getLength() == 0) {
                    finalOutput.append(lz77.getNextSymbol());
                }
                /*
                 * else means that there is a repeated substring found in the sliding window.
                 * SO,we retrieve the position and length of the data
                 * by performs the following steps:
                 * calculates the starting index for copying the previously occurred substring
                 * iterates through the length of the repeated substring and appends each character to the finalOutput.
                 * Finally, we append the next symbol to the finalOutput.
                 */
                else {
                    int start = finalOutput.length() - lz77.getPosition();
                    for (int i = 0; i < lz77.getLength(); i++) {
                        finalOutput.append(finalOutput.charAt(start + i));
                    }
                    finalOutput.append(lz77.getNextSymbol());
                }
            }
            /*
             * This part of the code prepares a file named fileDecompressed.txt for writing the decompressed data.
             * If the file does not exist, it creates a new file. If the file exists, it clears its content.
             * Then, it writes the decompressed data to the file and closes the file writer.
             */
            System.out.println("Decompressed Text: " + finalOutput.toString() + "\n" + "Data is added to the fileDecompressed.txt");
            File decompressed = new File("fileDecompressed.txt");
            if (!decompressed.exists()) {
                decompressed.createNewFile();
            } else {
                FileWriter clearFile = new FileWriter(decompressed);
                clearFile.write("");
                clearFile.close();
            }
            FileWriter writer = new FileWriter(decompressed, true);
            writer.write(finalOutput.toString());
            writer.close();
        }
        /*
         * This catch block catches any IOException that occurs during file operations and prints the stack trace.
         */ catch (IOException e) {
            e.printStackTrace();
        }
    }

}
/*
 * iteration 1 -> i=0 length =0 pos=0 nextsymbol=A
 * j=-1 for loop terminate because j>=0 && i-j<=LHB_size is false
 * tags.add(new LZ77(length, pos, nextSymbol)); -> tags.add(new LZ77(0, 0, A));
 *
 * iteration 2 -> i=1 length =0 pos=0 nextsymbol=B
 * j=0 k=0 l=1 currlength =0
 * while loop terminate because k>=0 && l<data.length() && data.charAt(k) == data.charAt(l) && currLength < SB_size is false
 * tags.add(new LZ77(length, pos, nextSymbol)); -> tags.add(new LZ77(0, 0, B));
 *
 * iteration 3 -> i=2 length =0 pos=0 nextsymbol=A
 * j=1 --> j=0 k=0 l=2 currlength =0
 * inside while loop k=1 l=3 currlength=1
 * if (currLength > length) is true
 * length = currLength -> length = 1
 * pos = i - j -> pos = 2
 * nextSymbol = A
 * tags.add(new LZ77(length, pos, nextSymbol)); -> tags.add(new LZ77(1, 2, A));
 */