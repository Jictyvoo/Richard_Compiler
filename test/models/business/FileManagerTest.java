package models.business;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class FileManagerTest {

    @Test
    void forLine() {
        String line = "A simple test god";
        try {
            FileManager fileManager = new FileManager("README.md");
            int counter = 0;
            for (char character : fileManager.forLine(line)) {
                counter += 1;
                assertEquals(character, line.charAt(counter));
            }
            assert (counter == line.length());
        } catch (FileNotFoundException e) {
            assertFalse(true);
        }
    }
}