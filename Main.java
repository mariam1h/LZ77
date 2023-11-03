/**************************************************************************************************
 Project Name : Lempel-Ziv 77 (LZ77) Compression Technique
 Authors      : Habiba Elbehairy, Mariam Haitham
 Description  : This project is an implementation of the LZ77 compression technique. It takes a file
 as an input and compresses it into a file of tags which is written in bits mode.
 It also takes a file of tags as an input and decompresses it into the original file.
 **************************** **********************************************************************/

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        /*
         * This part of the code takes the user's choice and calls the appropriate function
         */
        try (Scanner input = new Scanner(System.in)) {
            LZ77Functions lz77Functions = new LZ77Functions();
            while (true) {
                System.out.println("1-Compression System\n2-Decompression System\n3-Exit");
                int choice = input.nextInt();
                switch (choice) {
                    case 1:
                        System.out.println("----------------------\n| Compression System |\n----------------------");
                        System.out.print("Enter the name of the file that contains the data: ");
                        String filepath = input.next();
                        lz77Functions.compressToLZ77(filepath, 15, 15);
                        break;
                    case 2:
                        System.out.println("------------------------\n| Decompression System |\n------------------------");
                        System.out.print("Enter the name of the file that contains the tags: ");
                        String fileName = input.next();
                        lz77Functions.decompressFromLZ77s(fileName);
                        break;
                    case 3:
                        System.exit(0);
                        break;
                    default:
                        System.exit(0);
                        break;
                }
            }
        }
    }
}