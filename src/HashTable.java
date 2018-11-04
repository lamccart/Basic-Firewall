
import java.io.PrintWriter;
import java.io.File;
import java.io.*;
import java.text.DecimalFormat;
import java.util.LinkedList;

public class HashTable implements IHashTable {
	
	//You will need a HashTable of LinkedLists. 
	
	private int nelems;  //Number of element stored in the hash table
	private int expand;  //Number of times that the table has been expanded
	private int collision;  //Number of collisions since last expansion
	private String statsFileName;     //FilePath for the file to write statistics upon every rehash
	private boolean printStats = false;   //Boolean to decide whether to write statistics to file or not after rehashing
	private LinkedList<String>[] hashTable;
	private int tableSize;
	private int maxLengthChain;
	private double loadFactor;
	private static final double MAX_LOAD = 2.00/3.00;
	private static final int GROWTH_FACTOR = 2;

	
	//You are allowed to add more :)
	
	public HashTable(int size) {

		this.printStats = false;
		this.hashTable = new LinkedList[size];
		this.tableSize = size;

	}
	
	public HashTable(int size, String fileName){
		
		this.printStats = true;
		this.statsFileName = fileName;
		this.hashTable = new LinkedList[size];
		this.tableSize = size;
	}


	public boolean insert(String value) throws NullPointerException{

		if(value == null){
			throw new NullPointerException();
		}else{
			int key = hashVal(value);
			if(hashTable[key] == null){
				hashTable[key] = new LinkedList<>();
				hashTable[key].add(value);
			}else if(!hashTable[key].contains(value)){
				hashTable[key].add(value);
				if(hashTable[key].size() > maxLengthChain){
					maxLengthChain = hashTable[key].size();
				}
				collision++;
			}else{
				return false;
			}
			nelems++;
			loadFactor = (double) nelems / (double) tableSize;
			if(loadFactor > MAX_LOAD ){
				rehash();
			}
			return true;
		}
	}

	public boolean delete(String value) throws NullPointerException{
		if(value == null){
			throw new NullPointerException();
		}else{
			int key = hashVal(value);
			if(hashTable[key] == null || !hashTable[key].contains(value)){
				return false;
			}else{
				hashTable[key].remove(value);
				if(hashTable[key].size() == 0){
					hashTable[key] = null;
				}
			}
			nelems--;
			loadFactor = (double) nelems / (double) tableSize;
			return true;
		}
	}

	public boolean lookup(String value) throws NullPointerException{

		if(value == null){
			throw new NullPointerException();
		}else{
			int key = hashVal(value);
			return (hashTable[key] != null && hashTable[key].contains(value));

		}
	}

	public void printTable(){

		for(int i = 0; i < tableSize; i++){
		    String hashValues = "";
			if(hashTable[i] != null){
                hashValues = hashTable[i].getFirst();
			    if(hashTable[i].size() > 1){
                    for(int j = 1; j < hashTable[i].size()-2; j++){
                        hashValues = hashValues + ", " + hashTable[i].get(j);
                    }
                    hashValues = hashValues + ", " + hashTable[i].getLast();
                }
			}
			System.out.println(i+": "+ hashValues);
		}
	}
	
	public int getSize(){

		return nelems;
	}

	private int hashVal(String str){

		int hashValue = 0;
		for(int i=0; i<str.length(); i++){
			int leftShiftedValue = hashValue << 5;
			int rightShiftedValue = hashValue >>> 27;

			hashValue = (leftShiftedValue | rightShiftedValue ^ str.charAt(i));
		}
		return hashValue % tableSize;
	}

	private void rehash(){

		if(printStats){
			printStatistics();
		}
		LinkedList<String>[] newHashTable = new LinkedList[tableSize * GROWTH_FACTOR];

		for(int i = 0; i < hashTable.length; i++){
			newHashTable[i] = hashTable[i];
		}
		hashTable = newHashTable;
		tableSize = hashTable.length;
		expand++;
		collision = 0;
		maxLengthChain = 0;
		loadFactor = 0;

	}

	private void printStatistics(){

		DecimalFormat df = new DecimalFormat("#.##");
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream(new
					File(statsFileName),true));
			pw.println(expand + " resizes, "+ df.format(loadFactor) + ", "+ collision+ " collisions, "+
					maxLengthChain + " longest chain");
			pw.close();
		}
		catch (IOException e) { // If the given file doesn’t exist
			System.out.println("File not found!");
		}
	}

}
