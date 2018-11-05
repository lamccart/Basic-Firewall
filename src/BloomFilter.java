/**
 * Bloom filter implemented by using 3 independent hash functions.
 *
 * @author Liam McCarthy
 * @since 10/28/2018
 */
public class BloomFilter {
    public static final int NUMBITS = 8;

    byte[] table; // the hash table store in bits
    int size; // the number of slots (bits) in the hash table

    private static final int CRC_HASH_SHIFT = 5;
    private static final int WORD_WIDTH = Integer.SIZE * NUMBITS;
    private static final int CRC_HASH_RIGHT_SHIFT = 27;


    /**
     * The constructor that creates a bloom filter of given size in byte with 8 * tableSize slots.
     *
     * @param tableSize the given table size in byte
     */
    public BloomFilter(int tableSize) {

        this.table = new byte[tableSize];
        this.size = tableSize * NUMBITS;
    }

    /**
     * Hash a given string using the CRC modified function discussed in lecture
     * @param str the given string
     */
    private int hashValCRC(String str){

        int hashValue = 0;
        for(int i=0; i<str.length(); i++){
            int leftShiftedValue = hashValue << CRC_HASH_SHIFT;
            int rightShiftedValue = hashValue >>> CRC_HASH_RIGHT_SHIFT;

            hashValue = (leftShiftedValue | rightShiftedValue ^ str.charAt(i));
        }
        return Math.abs(hashValue % size);
    }


    /**
     * Hash a given string using the CRC modified function discussed in lecture
     * @param str the given string
     */

    private int hashValClass(String str){
        int hashVal = 0;
        for(int i = 0; i < str.length(); i++){
            int letter = str.charAt(i);
            hashVal = (hashVal * CRC_HASH_RIGHT_SHIFT + letter) % size;
        }
        return hashVal;
    }

    /**
     * Hash a given string using the Base256 function from sample hash functions file
     * @param str the given string
     */
    private int hashValBase256(String str) {

        int hashValue = 0;
        for (int i=0; i<str.length();i++) {
            hashValue = (hashValue << NUMBITS) + str.charAt(i);
            hashValue %= size;
        }
        return hashValue;
    }

    /**
     * Insert a given string to bloom filter
     * @param str the given string
     */
    public void insert(String str) throws NullPointerException{

        if(str == null){
            throw new NullPointerException();
        }else{
            //find all three keys for string
            int keyCRC = hashValCRC(str);
            int keyClass = hashValClass(str);
            int keyBase256 = hashValBase256(str);
            //find byte index of keys
            int byteIndexCRC = keyCRC / NUMBITS;
            int byteIndexClass = keyClass / NUMBITS;
            int byteIndexBase256 = keyBase256 / NUMBITS;
            //find bit index of keys
            int bitIndexCRC = keyCRC % NUMBITS;
            int bitIndexClass = keyClass % NUMBITS;
            int bitIndexBase256 = keyBase256 % NUMBITS;
            //put each bit in bloom filter
            setBit(byteIndexCRC, bitIndexCRC);
            setBit(byteIndexClass, bitIndexClass);
            setBit(byteIndexBase256, bitIndexBase256);
        }
    }

    /**
     * Helper method to set a bit in the table to 1, which is specified by the given byteIndex 
     * and bitIndex
     * @param byteIndex the index of the byte in hash table
     * @param bitIndex the index of the bit in the byte at byteIndex. Range is [0, 7]
     */
    private void setBit(int byteIndex, int bitIndex) {
        // set the bit at bitIndex of the byte at byteIndex
        table[byteIndex] = (byte) (table[byteIndex] | ((1 << (NUMBITS - 1)) >> bitIndex));
    }

    /**
     * Find if a string could exist in bloom filter
     * @param str the given string
     * @return true if given string is "believed" to be in the hash table, false otherwise
     */
    public boolean find(String str) throws NullPointerException{
        if(str == null){
            throw new NullPointerException();
        }else{
            int keyCRC = hashValCRC(str);
            int keyClass = hashValClass(str);
            int keyBase256 = hashValBase256(str);
            int byteIndexCRC = keyCRC / NUMBITS;
            int byteIndexClass = keyClass / NUMBITS;
            int byteIndexBase256 = keyBase256 / NUMBITS;
            int bitIndexCRC = keyCRC % NUMBITS;
            int bitIndexClass = keyClass % NUMBITS;
            int bitIndexBase256 = keyBase256 % NUMBITS;
            return getSlot(byteIndexCRC, bitIndexCRC) == 1 && getSlot(byteIndexClass, bitIndexClass) == 1
                    && getSlot(byteIndexBase256, bitIndexBase256) == 1;
        }
    }

    /**
     * Helper method to get the bit value at the slot, which is specified by the given byteIndex 
     * and bitIndex
     * @param byteIndex the index of the byte in hash table
     * @param bitIndex the index of the bit in the byte at byteIndex. Range is [0, 7]
     */
    private int getSlot(int byteIndex, int bitIndex) {
        return (table[byteIndex] >> ((NUMBITS - 1) - bitIndex)) & 1;
    }
}
