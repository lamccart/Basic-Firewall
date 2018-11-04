import static org.junit.Assert.*;
import org.junit.Test;

public class HashTableTester {

    @Test
    public void testInsert() {
        HashTable testTable = new HashTable(5);
        assertTrue(testTable.insert("test"));
        testTable.insert("other test");
        assertTrue(testTable.lookup("test"));
        assertEquals(2, testTable.getSize());
        testTable.insert("test");
        assertEquals(2, testTable.getSize());
    }

    @Test
    public void testDelete() {

        HashTable testTable = new HashTable(3);
        testTable.insert("test1");
        testTable.insert("test2");
        testTable.printTable();
        assertTrue(testTable.delete("test1"));
        assertEquals(1, testTable.getSize());
        assertFalse(testTable.delete("test1"));
    }

    @Test
    public void testLookup() {
        HashTable testTable = new HashTable(10);
        testTable.insert("test1");
        testTable.insert("test2");
        testTable.insert("test3");
        assertTrue(testTable.lookup("test1"));
        testTable.insert("1234");
        assertTrue(testTable.lookup("1234"));
        testTable.delete("test2");
        assertFalse(testTable.lookup("test2"));
    }

    @Test
    public void testGetSize() {
        HashTable testTable = new HashTable(10);
        testTable.insert("test1");
        testTable.insert("test2");
        testTable.insert("test3");
        testTable.insert("1234");
        assertEquals(4, testTable.getSize());
        testTable.delete("test3");
        assertEquals(3, testTable.getSize());
        testTable.delete("test3");
        assertEquals(3, testTable.getSize());
    }
}