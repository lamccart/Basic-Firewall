
import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    public static final long MAX_SPACE = 3/2;
    /**
     * Main method that drives this program
     * @param args the command line argument
     */
    public static void main(String args[]) {
        long badBloomSize;
        long numMixedUrls;
        long numBadUrls;
        BloomFilter badUrlsBloom;
        /* Scanner must be put in try catch block to catch potential exception */
        try {
            numBadUrls = Files.lines(Paths.get(new File(args[0]).getPath())).count();
            badBloomSize = numBadUrls * MAX_SPACE;
            badUrlsBloom = new BloomFilter((int) badBloomSize);
            Scanner sc = new Scanner(new File(args[0]));
            while (sc.hasNextLine()) {
                String badUrl = sc.nextLine();
                badUrlsBloom.insert(badUrl);
            }
            sc.close();
            numMixedUrls = Files.lines(Paths.get(new File(args[1]).getPath())).count();
            Scanner mixedSc = new Scanner(new File(args[1]));
            while (mixedSc.hasNextLine()) {
                String mixedUrl = sc.nextLine();
                if(!badUrlsBloom.find(mixedUrl)){
                    PrintWriter pw = new PrintWriter(new FileOutputStream(new
                                    File(args[2]),true));
                    pw.println(mixedUrl);
                    pw.close();
                }
            }
            sc.close();
            long numUrlsOutput = Files.lines(Paths.get(new File(args[2]).getPath())).count();
            long numSafeURls = numMixedUrls - numBadUrls;
            // print statistics
            System.out.println("False positive rate: "+ ((numSafeURls - numUrlsOutput)/numSafeURls));
            // Get the size of badURL in bytes
            File badURL = new File(args[0]);
            long inputSize = badURL.length();

            System.out.println("Saved memory ratio: " + (inputSize/badBloomSize));
        } catch (IOException e) {
            System.out.println("File not found!");
        }






    }

}
