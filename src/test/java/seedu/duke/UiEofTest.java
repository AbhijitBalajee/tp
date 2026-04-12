// @@author AfshalG
package seedu.duke;

import java.io.ByteArrayInputStream;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UiEofTest {

    @Test
    void readCommand_emptyInput_returnsBye() {
        Ui ui = new Ui(new ByteArrayInputStream(new byte[0]));
        assertDoesNotThrow(() -> {
            String result = ui.readCommand();
            assertEquals("bye", result,
                    "On EOF, readCommand should return 'bye' for graceful exit");
        });
    }

    @Test
    void readCommand_inputWithoutNewline_returnsBye() {
        // Piped input with content but no trailing newline/bye
        Ui ui = new Ui(new ByteArrayInputStream("add d/T a/5 c/Food".getBytes()));
        assertDoesNotThrow(() -> {
            ui.readCommand(); // consumes the one line
            String second = ui.readCommand(); // should hit EOF
            assertEquals("bye", second,
                    "After input is exhausted, next readCommand should return 'bye'");
        });
    }

    @Test
    void readCommand_normalInput_returnsLine() {
        Ui ui = new Ui(new ByteArrayInputStream("hello\n".getBytes()));
        assertEquals("hello", ui.readCommand(),
                "Normal input should be returned as-is");
    }
}
