package itx.examples.records.tests;

import itx.examples.records.RecordId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RecordTests {

    @Test
    public void testEquals() {
        RecordId recordId01 = new RecordId("r-01");
        RecordId recordId02 = new RecordId("r-02");
        RecordId recordId03 = new RecordId("r-01");
        RecordId recordId04 = new RecordId("r-01");
        //reflective
        assertTrue(recordId01.equals(recordId01));
        //symmetric
        assertTrue(recordId01.equals(recordId03));
        assertTrue(recordId03.equals(recordId01));
        //transitive
        assertTrue(recordId01.equals(recordId03));
        assertTrue(recordId03.equals(recordId04));
        assertTrue(recordId03.equals(recordId01));
        //consistent
        assertTrue(recordId01.equals(recordId01));
        assertTrue(recordId01.equals(recordId03));
        //null
        assertFalse(recordId01.equals(null));
        assertFalse(recordId02.equals(null));
        assertFalse(recordId03.equals(null));
        //negative
        assertFalse(recordId01.equals(recordId02));
        assertFalse(recordId02.equals(recordId01));
    }

    @Test
    public void testHashCode() {
        RecordId recordId01 = new RecordId("r-01");
        RecordId recordId02 = new RecordId("r-02");
        RecordId recordId03 = new RecordId("r-01");
        assertEquals(recordId01.hashCode(), recordId01.hashCode());
        assertEquals(recordId01.hashCode(), recordId03.hashCode());
        assertNotEquals(recordId01.hashCode(), recordId02.hashCode());
    }

    @Test
    public void testToString() {
        RecordId recordId = new RecordId("r-01");
        assertNotNull(recordId.toString());
    }

    @Test
    public void testRecordId() {
        RecordId recordId = new RecordId("r-01");
        assertNotNull(recordId.id());
        assertEquals(recordId.id(), "r-01");
    }

}
