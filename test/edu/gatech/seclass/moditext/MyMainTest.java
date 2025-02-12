package edu.gatech.seclass.moditext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;


@Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class MyMainTest {
    // Place all of your tests in this class, optionally using MainTest.java as an example
    private final String usageStr =
        "Usage: moditext [ -k substring | -p ch num | -t num | -g | -f style substring | -r ] FILE"
            + System.lineSeparator();

    @TempDir
    Path tempDirectory;

    @RegisterExtension
    OutputCapture capture = new OutputCapture();

    /* ----------------------------- Test Utilities ----------------------------- */

    /**
     * Returns path of a new "input.txt" file with specified contents written
     * into it. The file will be created using {@link TempDir TempDir}, so it
     * is automatically deleted after test execution.
     *
     * @param contents the text to include in the file
     * @return a Path to the newly written file, or null if there was an
     *         issue creating the file
     */
    private Path createFile(String contents) {
        return createFile(contents, "input.txt");
    }

    /**
     * Returns path to newly created file with specified contents written into
     * it. The file will be created using {@link TempDir TempDir}, so it is
     * automatically deleted after test execution.
     *
     * @param contents the text to include in the file
     * @param fileName the desired name for the file to be created
     * @return a Path to the newly written file, or null if there was an
     *         issue creating the file
     */
    private Path createFile(String contents, String fileName) {
        Path file = tempDirectory.resolve(fileName);
        try {
            Files.writeString(file, contents);
        } catch (IOException e) {
            return null;
        }

        return file;
    }

    /**
     * Takes the path to some file and returns the contents within.
     *
     * @param file the path to some file
     * @return the contents of the file as a String, or null if there was an
     *         issue reading the file
     */
    private String getFileContent(Path file) {
        try {
            return Files.readString(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* ------------------------------- Test Cases ------------------------------- */
    // Frame #: 1
    @Test
    public void moditextTest1() {
        String[] args = {"nonexistent.txt"};
        Main.main(args);
        assertEquals(usageStr, capture.stderr());
        assertTrue(capture.stdout().isEmpty());
    }

    // Frame #: 2
    @Test
    public void moditextTest2() {
        String input = "This is a test file with no newline at the end";
        Path inputFile = createFile(input);
        String[] args = {inputFile.toString()};
        Main.main(args);
        assertEquals(usageStr, capture.stderr());
        assertTrue(capture.stdout().isEmpty());
    }

    // Frame #: 3
    @Test
    public void moditextTest3() {
        String[] args = {"-x", "sample.txt"};
        Main.main(args);
        assertEquals(usageStr, capture.stderr());
        assertTrue(capture.stdout().isEmpty());
    }

    // Frame #: 4
    @Test
    public void moditextTest4() {
        String[] args = {"-p", "#", "5", "-t", "10", "sample.txt"};
        Main.main(args);
        assertEquals(usageStr, capture.stderr());
        assertTrue(capture.stdout().isEmpty());
    }

    // Frame #: 5
    @Test
    public void moditextTest5() {
        String[] args = {"-p", "##", "5", "sample.txt"};
        Main.main(args);
        assertEquals(usageStr, capture.stderr());
        assertTrue(capture.stdout().isEmpty());
    }

    // Frame #: 6
    @Test
    public void moditextTest6() {
        String[] args = {"-p", "#", "101", "sample.txt"};
        Main.main(args);
        assertEquals(usageStr, capture.stderr());
        assertTrue(capture.stdout().isEmpty());
    }

    // Frame #: 7
    @Test
    public void moditextTest7() {
        String[] args = {"-t", "101", "sample.txt"};
        Main.main(args);
        assertEquals(usageStr, capture.stderr());
        assertTrue(capture.stdout().isEmpty());
    }

    // Frame #: 8
    @Test
    public void moditextTest8() {
        String[] args = {"-f", "underline", "text", "sample.txt"};
        Main.main(args);
        assertEquals(usageStr, capture.stderr());
        assertTrue(capture.stdout().isEmpty());
    }

    // Frame #: 9
    @Test
    public void moditextTest9() {
        String[] args = {"-f", "bold", "nonempty", "sample.txt"};
        Main.main(args);
        assertEquals(usageStr, capture.stderr());
        assertTrue(capture.stdout().isEmpty());
    }

    // Frame #: 10
    @Test
    public void moditextTest10() {
        String[] args = {"-k", "test"};
        Main.main(args);
        assertEquals(usageStr, capture.stderr());
        assertTrue(capture.stdout().isEmpty());
    }

    // Frame #: 11
    @Test
    public void moditextTest11() {
        String[] args = {"-k", "test", "-p", "#", "5", "sample.txt"};
        Main.main(args);
        assertEquals(usageStr, capture.stderr());
        assertTrue(capture.stdout().isEmpty());
    }

    // Frame #: 12
    @Test
    public void moditextTest12() {
        String input = "";
        Path inputFile = createFile(input);
        String[] args = {inputFile.toString()};
        Main.main(args);
        assertTrue(capture.stdout().isEmpty());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest13() {
        String input = " " + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {inputFile.toString()};
        Main.main(args);
        assertEquals(" " + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest14() {
        String input = "  " + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-t", "1", inputFile.toString()};
        Main.main(args);
        assertEquals(" " + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest15() {
        String input = "\t" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "\t", inputFile.toString()};
        Main.main(args);
        assertEquals("\t" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }
    // Frame #: 18
    @Test
    public void moditextTest18() {
        String input = "Test line" + System.lineSeparator() + "Another line" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "", inputFile.toString()};
        Main.main(args);
        assertEquals(input, capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 19
    @Test
    public void moditextTest19() {
        String input = "Test line" + System.lineSeparator() + "Another line" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", " ", inputFile.toString()};
        Main.main(args);
        assertEquals(input, capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest20() {
        String input = "Short" + System.lineSeparator() + "Longer line" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-p", "#", "10", inputFile.toString()};
        Main.main(args);
        assertEquals("#####Short" + System.lineSeparator() + "Longer line" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

// Update tests 21, 22, 23, 48, 49, 50, 51 similarly

    @Test
    public void moditextTest21() {
        String input = "Short" + System.lineSeparator() + "Longer line" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-p", "*", "10", inputFile.toString()};
        Main.main(args);
        assertEquals("*****Short" + System.lineSeparator() + "Longer line" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest22() {
        String input = "A" + System.lineSeparator() + "BC" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-p", "-", "3", inputFile.toString()};
        Main.main(args);
        assertEquals("--A" + System.lineSeparator() + "-BC" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest23() {
        String input = "1" + System.lineSeparator() + "22" + System.lineSeparator() + "333" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-p", "+", "5", inputFile.toString()};
        Main.main(args);
        assertEquals("++++1" + System.lineSeparator() + "+++22" + System.lineSeparator() + "++333" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 24
    @Test
    public void moditextTest24() {
        String input = "Long line" + System.lineSeparator() + "Short" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-t", "5", inputFile.toString()};
        Main.main(args);
        assertEquals("Long " + System.lineSeparator() + "Short" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 25
    @Test
    public void moditextTest25() {
        String input = "Long line" + System.lineSeparator() + "Short" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-t", "4", inputFile.toString()};
        Main.main(args);
        assertEquals("Long" + System.lineSeparator() + "Shor" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 26
    @Test
    public void moditextTest26() {
        String input = "Long line" + System.lineSeparator() + "Short" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-t", "6", inputFile.toString()};
        Main.main(args);
        assertEquals("Long l" + System.lineSeparator() + "Short" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 27
    @Test
    public void moditextTest27() {
        String input = "Long line" + System.lineSeparator() + "Short" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-t", "0", inputFile.toString()};
        Main.main(args);
        assertEquals(System.lineSeparator() + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 28
    @Test
    public void moditextTest28() {
        String input = "Format this text" + System.lineSeparator() + "Don't format this" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-f", "bold", "this", inputFile.toString()};
        Main.main(args);
        assertEquals("Format **this** text" + System.lineSeparator() + "Don't format **this**" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 29
    @Test
    public void moditextTest29() {
        String input = "Format this text" + System.lineSeparator() + "Don't format this" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-f", "italic", "this", inputFile.toString()};
        Main.main(args);
        assertEquals("Format *this* text" + System.lineSeparator() + "Don't format *this*" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 30
    @Test
    public void moditextTest30() {
        String input = "Format this text" + System.lineSeparator() + "Don't format this" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-f", "code", "this", inputFile.toString()};
        Main.main(args);
        assertEquals("Format `this` text" + System.lineSeparator() + "Don't format `this`" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 31
    @Test
    public void moditextTest31() {
        String input = "Format this text" + System.lineSeparator() + "Don't format this" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-f", "bold", "Format", inputFile.toString()};
        Main.main(args);
        assertEquals("**Format** this text" + System.lineSeparator() + "Don't format this" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 32
    @Test
    public void moditextTest32() {
        String input = "First line" + System.lineSeparator() + "Second line" + System.lineSeparator() + "Third line" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", inputFile.toString()};
        Main.main(args);
        assertEquals("Third line" + System.lineSeparator() + "Second line" + System.lineSeparator() + "First line" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 33
    @Test
    public void moditextTest33() {
        String input = "Only line" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", inputFile.toString()};
        Main.main(args);
        assertEquals("Only line" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 34
    @Test
    public void moditextTest34() {
        String input = "First line" + System.lineSeparator() + "Second line" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", inputFile.toString()};
        Main.main(args);
        assertEquals("Second line" + System.lineSeparator() + "First line" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }
    // Frame #: 35
    @Test
    public void moditextTest35() {
        String input = "Line 1" + System.lineSeparator() + "Line 2" + System.lineSeparator() + "Line 3" + System.lineSeparator() + "Line 4" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", inputFile.toString()};
        Main.main(args);
        assertEquals("Line 4" + System.lineSeparator() + "Line 3" + System.lineSeparator() + "Line 2" + System.lineSeparator() + "Line 1" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest36() {
        String input = "Test test test" + System.lineSeparator() + "Another line" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-g", "-f", "bold", "test", inputFile.toString()};
        Main.main(args);
        assertEquals("Test **test** **test**" + System.lineSeparator() + "Another line" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 37
    @Test
    public void moditextTest37() {
        String input = "Test TEST Test" + System.lineSeparator() + "Another line" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-g", "-f", "bold", "Test", inputFile.toString()};
        Main.main(args);
        assertEquals("**Test** TEST **Test**" + System.lineSeparator() + "Another line" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest38() {
        String input = "Format this and this" + System.lineSeparator() + "But not That" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-g", "-f", "italic", "this", inputFile.toString()};
        Main.main(args);
        assertEquals("Format *this* and *this*" + System.lineSeparator() + "But not That" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest39() {
        String input = "Code this and This" + System.lineSeparator() + "Not this" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-g", "-f", "code", "this", inputFile.toString()};
        Main.main(args);
        assertEquals("Code `this` and This" + System.lineSeparator() + "Not `this`" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 40
    @Test
    public void moditextTest40() {
        String input = "This is a non-empty file." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {inputFile.toString()};
        Main.main(args);
        assertEquals(input, capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 41
    @Test
    public void moditextTest41() {
        String input = "Line 1" + System.lineSeparator() + "Line 2" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {inputFile.toString()};
        Main.main(args);
        assertEquals(input, capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest42() {
        String input = "Single line without newline";
        Path inputFile = createFile(input);
        String[] args = {"-k", "without", inputFile.toString()};
        Main.main(args);
        assertEquals("", capture.stdout());
        assertEquals("Usage: moditext [ -k substring | -p ch num | -t num | -g | -f style substring | -r ] FILE" + System.lineSeparator(), capture.stderr());
    }



    // Frame #: 43
    @Test
    public void moditextTest43() {
        String input = "Line with trailing spaces  " + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {inputFile.toString()};
        Main.main(args);
        assertEquals(input, capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 44
    @Test
    public void moditextTest44() {
        String input = "Keep this line" + System.lineSeparator() + "Don't keep this" + System.lineSeparator() + "Keep this too" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "Keep", inputFile.toString()};
        Main.main(args);
        assertEquals("Keep this line" + System.lineSeparator() + "Keep this too" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 45
    @Test
    public void moditextTest45() {
        String input = "KEEP this line" + System.lineSeparator() + "Don't keep this" + System.lineSeparator() + "Keep THIS too" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "KEEP", inputFile.toString()};
        Main.main(args);
        assertEquals("KEEP this line" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 46
    @Test
    public void moditextTest46() {
        String input = "Keep this line" + System.lineSeparator() + "Don't keep this" + System.lineSeparator() + "Keep this too" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "", inputFile.toString()};
        Main.main(args);
        assertEquals(input, capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 47
    @Test
    public void moditextTest47() {
        String input = "Keep this line" + System.lineSeparator() + "Don't keep this" + System.lineSeparator() + "Keep this too" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", " ", inputFile.toString()};
        Main.main(args);
        assertEquals(input, capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest48() {
        String input = "A" + System.lineSeparator() + "BB" + System.lineSeparator() + "CCC" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-p", "#", "4", inputFile.toString()};
        Main.main(args);
        assertEquals("###A" + System.lineSeparator() + "##BB" + System.lineSeparator() + "#CCC" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest49() {
        String input = "1" + System.lineSeparator() + "22" + System.lineSeparator() + "333" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-p", "@", "3", "-k", "2", inputFile.toString()};
        Main.main(args);
        assertEquals("@22" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest50() {
        String input = "Keep" + System.lineSeparator() + "Don't" + System.lineSeparator() + "Also Keep" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-p", "-", "8", "-k", "Keep", inputFile.toString()};
        Main.main(args);
        assertEquals("----Keep" + System.lineSeparator() + "Also Keep" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest51() {
        String input = "Format" + System.lineSeparator() + "Don't Format" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-p", "+", "8", "-f", "bold", "Format", inputFile.toString()};
        Main.main(args);
        assertEquals("++**Format**" + System.lineSeparator() + "Don't **Format**" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 52
    @Test
    public void moditextTest52() {
        String input = "This is a long line" + System.lineSeparator() + "Short" + System.lineSeparator() + "Another long line here" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-t", "10", inputFile.toString()};
        Main.main(args);
        assertEquals("This is a " + System.lineSeparator() + "Short" + System.lineSeparator() + "Another lo" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 53
    @Test
    public void moditextTest53() {
        String input = "This is a long line" + System.lineSeparator() + "Short" + System.lineSeparator() + "Another long line here" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-t", "5", inputFile.toString()};
        Main.main(args);
        assertEquals("This " + System.lineSeparator() + "Short" + System.lineSeparator() + "Anoth" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 54
    @Test
    public void moditextTest54() {
        String input = "This is a long line" + System.lineSeparator() + "Short" + System.lineSeparator() + "Another long line here" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-t", "15", inputFile.toString()};
        Main.main(args);
        assertEquals("This is a long " + System.lineSeparator() + "Short" + System.lineSeparator() + "Another long li" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 55
    @Test
    public void moditextTest55() {
        String input = "This is a long line" + System.lineSeparator() + "Short" + System.lineSeparator() + "Another long line here" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-t", "0", inputFile.toString()};
        Main.main(args);
        assertEquals(System.lineSeparator() + System.lineSeparator() + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 56
    @Test
    public void moditextTest56() {
        String input = "Format this word" + System.lineSeparator() + "Don't format this" + System.lineSeparator() + "Format again" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-f", "bold", "Format", inputFile.toString()};
        Main.main(args);
        assertEquals("**Format** this word" + System.lineSeparator() + "Don't format this" + System.lineSeparator() + "**Format** again" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 57
    @Test
    public void moditextTest57() {
        String input = "Format this word" + System.lineSeparator() + "Don't format this" + System.lineSeparator() + "Format again" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-f", "italic", "Format", inputFile.toString()};
        Main.main(args);
        assertEquals("*Format* this word" + System.lineSeparator() + "Don't format this" + System.lineSeparator() + "*Format* again" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 58
    @Test
    public void moditextTest58() {
        String input = "Format this word" + System.lineSeparator() + "Don't format this" + System.lineSeparator() + "Format again" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-f", "code", "Format", inputFile.toString()};
        Main.main(args);
        assertEquals("`Format` this word" + System.lineSeparator() + "Don't format this" + System.lineSeparator() + "`Format` again" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 59
    @Test
    public void moditextTest59() {
        String input = "Format this word" + System.lineSeparator() + "Don't format this" + System.lineSeparator() + "Format again" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-f", "bold", "this", inputFile.toString()};
        Main.main(args);
        assertEquals("Format **this** word" + System.lineSeparator() + "Don't format **this**" + System.lineSeparator() + "Format again" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 60
    @Test
    public void moditextTest60() {
        String input = "First line" + System.lineSeparator() + "Second line" + System.lineSeparator() + "Third line" + System.lineSeparator() + "Fourth line" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", inputFile.toString()};
        Main.main(args);
        assertEquals("Fourth line" + System.lineSeparator() + "Third line" + System.lineSeparator() + "Second line" + System.lineSeparator() + "First line" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest61() {
        String input = "Reverse this line";
        Path inputFile = createFile(input);
        String[] args = {"-r", inputFile.toString()};
        Main.main(args);
        assertEquals("", capture.stdout());
        assertEquals("Usage: moditext [ -k substring | -p ch num | -t num | -g | -f style substring | -r ] FILE" + System.lineSeparator(), capture.stderr());
    }

    @Test
    public void moditextTest62() {  // or choose another appropriate name
        String input = "First line" + System.lineSeparator() +
                System.lineSeparator() +
                "Third line" + System.lineSeparator() +
                System.lineSeparator() +
                "Fifth line" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", inputFile.toString()};
        Main.main(args);
        assertEquals("Fifth line" + System.lineSeparator() +
                System.lineSeparator() +
                "Third line" + System.lineSeparator() +
                System.lineSeparator() +
                "First line" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 63
    @Test
    public void moditextTest63() {
        String input = "Line 1" + System.lineSeparator() + "Line 2" + System.lineSeparator() + "Line 3" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", inputFile.toString()};
        Main.main(args);
        assertEquals("Line 3" + System.lineSeparator() + "Line 2" + System.lineSeparator() + "Line 1" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 64
    @Test
    public void moditextTest64() {
        String input = "Keep this" + System.lineSeparator() + "Format that" + System.lineSeparator() + "Reverse all" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "Keep", "-f", "bold", "this", "-r", inputFile.toString()};
        Main.main(args);
        assertEquals("Keep **this**" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 65
    @Test
    public void moditextTest65() {
        String input = "Keep this" + System.lineSeparator() + "Format that" + System.lineSeparator() + "Reverse all" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "Keep", "-f", "italic", "this", "-r", inputFile.toString()};
        Main.main(args);
        assertEquals("Keep *this*" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 66
    @Test
    public void moditextTest66() {
        String input = "Keep this" + System.lineSeparator() + "Format that" + System.lineSeparator() + "Reverse all" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "Format", "-f", "bold", "that", "-r", inputFile.toString()};
        Main.main(args);
        assertEquals("Format **that**" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 67
    @Test
    public void moditextTest67() {
        String input = "Keep this" + System.lineSeparator() + "Format that" + System.lineSeparator() + "Reverse all" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "Reverse", "-f", "code", "all", "-r", inputFile.toString()};
        Main.main(args);
        assertEquals("Reverse `all`" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest78() {
        String input = "A".repeat(200) + System.lineSeparator() + "B".repeat(50) + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-t", "100", inputFile.toString()};
        Main.main(args);
        assertEquals("A".repeat(100) + System.lineSeparator() + "B".repeat(50) + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest79() {
        String input = "Short" + System.lineSeparator() + "Medium line" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-p", "#", "100", inputFile.toString()};
        Main.main(args);
        assertEquals("#".repeat(95) + "Short" + System.lineSeparator() + "#".repeat(89) + "Medium line" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest80() {
        String input = "Format this and that" + System.lineSeparator() + "Don't format this" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-g", "-f", "bold", "this", "-k", "Don't", inputFile.toString()};
        Main.main(args);
        assertEquals("Don't format **this**" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest81() {
        String input = "Keep this" + System.lineSeparator() + "And this" + System.lineSeparator() + "Not this" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "", inputFile.toString()};
        Main.main(args);
        assertEquals(input, capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest82() {
        String input = "こんにちは" + System.lineSeparator() + "你好" + System.lineSeparator() + "Hello" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "-t", "2", inputFile.toString()};
        Main.main(args);
        assertEquals("He" + System.lineSeparator() + "你好" + System.lineSeparator() + "こん" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest83() {
        String input = "Test" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-x", inputFile.toString()};
        Main.main(args);
        assertTrue(capture.stdout().isEmpty());
        assertEquals("Usage: moditext [ -k substring | -p ch num | -t num | -g | -f style substring | -r ] FILE" + System.lineSeparator(), capture.stderr());
    }
    @Test
    public void moditextTest85() {
        String input = "Line1" + System.lineSeparator() + "Line2" + System.lineSeparator() + "Line3" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", System.lineSeparator(), inputFile.toString()};
        Main.main(args);
        assertEquals("", capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest86() {
        String input = "Bold this" + System.lineSeparator() + "Italic that" + System.lineSeparator() + "Code those" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-f", "bold", "this", "-f", "italic", "that", "-f", "code", "those", inputFile.toString()};
        Main.main(args);
        assertEquals("Bold this" + System.lineSeparator() + "Italic that" + System.lineSeparator() + "Code `those`" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest87() {
        String input = "Pad this" + System.lineSeparator() + "And this" + System.lineSeparator() + "And this too" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-p", " ", "10", "-t", "8", inputFile.toString()};
        Main.main(args);
        assertTrue(capture.stderr().contains("Usage:"));
        assertTrue(capture.stdout().isEmpty());
    }

    @Test
    public void moditextTest88() {
        String input = "Format all this" + System.lineSeparator() + "And this" + System.lineSeparator() + "But not this" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-g", "-f", "bold", "this", "-r", "-t", "15", inputFile.toString()};
        Main.main(args);
        assertEquals("But not **this**" + System.lineSeparator() + "And **this**" + System.lineSeparator() + "Format all **this**" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest89() {
        String input = "a" + System.lineSeparator() + "aa" + System.lineSeparator() + "aaa" + System.lineSeparator() + "aaaa" + System.lineSeparator() + "aaaaa" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-p", "b", "5", "-k", "aaa", inputFile.toString()};
        Main.main(args);
        assertEquals("bbaaa" + System.lineSeparator() + "baaaa" + System.lineSeparator() + "aaaaa" + System.lineSeparator(), capture.stdout());
        assertTrue(capture.stderr().isEmpty());
    }

    @Test
    public void moditextTest90() {
        String input = "First line" + System.lineSeparator() + "Second line" + System.lineSeparator() + "Third line" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-f", "bold", "", inputFile.toString()};
        Main.main(args);
        assertTrue(capture.stderr().contains("Usage:"));
        assertTrue(capture.stdout().isEmpty());
    }

    @Test
    public void moditextTest91() {
        String input = "Line with spaces  " + System.lineSeparator() + "Another line " + System.lineSeparator() + "No spaces" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-t", "100", "-p", "-", "15", inputFile.toString()};
        Main.main(args);
        assertEquals("", capture.stdout());
        assertTrue(capture.stderr().contains("Usage:"));
    }
}

