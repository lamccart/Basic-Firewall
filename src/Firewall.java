
import java.util.Scanner;
import java.io.*;
/**
 * This file acts as a firewall for checking a text file of urls. Making a bloom filter of bad urls.
 *
 * @author Liam McCarthy
 * @since 10/28/2018
 */

/**
 * This program takes in 3 command line arguments. The first one is the malicious URL file. The second one is the
 * mixed URL file, which contains malicious URLs from the given malicious URL file, and other safe URLs as well.
 * The third one is the name of the file to write your output to, which contains every URL from the mixed URL file
 * that are for sure known to be safe after using bloom filter (think about what that means about the
 * false positives), one URL per line.
 *
 * Note that you should only use at most 3 bytes for each 2 malicious URLs in your Bloom Filter. For example, if the 
 * malicious URL file contains 30000 URLs, then you should use at most 45000 bytes in your bloom filter.
 */
public class Firewall {
    public static final double MAX_SPACE = 3.00/2.00;
    /**
     * Main method that drives this program
     * @param args the command line argument
     */
    public static void main(String args[]) {
        double badBloomSize;
        int numMixedUrls = 0;
        int numBadUrls = 0;
        int numOutputUrls = 0;
        BloomFilter badUrlsBloom;
        /* Scanner must be put in try catch block to catch potential exception */
        try {
            //Reads the bad URL file to find number of URLs
            FileReader fr = new FileReader(args[0]);
            LineNumberReader lnr = new LineNumberReader(fr);
            while (lnr.readLine() != null) {
                numBadUrls++;
            }
            lnr.close();
            //Creates the bloom filter of bad urls
            badBloomSize = numBadUrls * MAX_SPACE;
            badUrlsBloom = new BloomFilter((int) badBloomSize);
            Scanner sc = new Scanner(new File(args[0]));
            //Inserts bad url to bloom filter
            while (sc.hasNextLine()) {
                String badUrl = sc.nextLine();
                badUrlsBloom.insert(badUrl);
            }
            sc.close();
            //Scans file of mixed urls
            Scanner mixedSc = new Scanner(new File(args[1]));
            while (mixedSc.hasNextLine()) {
                String mixedUrl = mixedSc.nextLine();
                //Increment number of urls
                numMixedUrls++;
                //Checks if url is in bloom filter
                if(!badUrlsBloom.find(mixedUrl)){
                    //Writes url to output file
                    PrintWriter pw = new PrintWriter(new FileOutputStream(new
                                    File(args[2]),true));
                    pw.println(mixedUrl);
                    pw.close();
                    //Increment number of output urls
                    numOutputUrls++;
                }
            }
            mixedSc.close();
            int numSafeURls = numMixedUrls - numBadUrls;
            // print statistics
            System.out.println("False positive rate: "+ ((numSafeURls - numOutputUrls)/(double) numSafeURls));
            // Get the size of badURL in bytes
            File badURL = new File(args[0]);
            long inputSize = badURL.length();

            System.out.println("Saved memory ratio: " + (inputSize/badBloomSize));
        } catch (IOException e) {
            System.out.println("File not found!");
        }






    }

}
