package itx.examples.helpfulnpe.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HelpfulNPETests {

    @Test
    public void testNPEAssignment() {
        StringWrapper sw = null;
        NullPointerException npe = assertThrows(NullPointerException.class, () -> {
            sw.data = "hi";
        });
        assertTrue(npe instanceof NullPointerException);
        npe.printStackTrace();
    }

}
