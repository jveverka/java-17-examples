package itx.examples.textblocks.tests;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TextBlocksTest {

    private static String html =
              """
              <html>
                  <body>
                      <p>Hello, world</p>
                  </body>
              </html>
              """;

    private static String query =
              """
              SELECT "EMP_ID", "LAST_NAME" FROM "EMPLOYEE_TB"
              WHERE "CITY" = 'INDIANAPOLIS'
              ORDER BY "EMP_ID", "LAST_NAME";
              """;

    @Test
    public void testStrings() {
        assertNotNull(html);
        assertNotNull(query);
        assertEquals(66, html.length());
        assertEquals(110, query.length());
    }

}
