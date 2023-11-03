/******************************************************************************
 *
 * Module: LZ77
 *
 * File Name: LZ77.java
 *
 * Description: This class represents the LZ77 tag.
 *
 * Authors: Habiba Elbehairy, Mariam Haitham.
 *
 *******************************************************************************/

/*
 * This class represents the LZ77 tag
 */
public class LZ77 {
    /*
     * These are the attributes of the class
     */
    public int length;
    public int pos;
    public char nextSymbol;

    /*
     * This is the constructor of the class
     */
    public LZ77(int length, int pos, char nextSymbol) {
        this.length = length;
        this.pos = pos;
        this.nextSymbol = nextSymbol;
    }

    /*
     * This function returns the length of the match
     */
    public int getLength() {
        return length;
    }

    /*
     * This function returns the position of the match
     */
    public int getPosition() {
        return pos;
    }

    /*
     * This function returns the next symbol after the match
     */
    public char getNextSymbol() {
        return nextSymbol;
    }

    /*
     * This function returns the tag in the form of a string
     */
    @Override
    public String toString() {
        return "<" + pos + "," + length + "," + nextSymbol + ">";
    }
}
