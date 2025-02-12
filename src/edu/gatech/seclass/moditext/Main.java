package edu.gatech.seclass.moditext;
import java.io.*;
import java.util.*;
public class Main {
    private static final String USAGE = "Usage: moditext [ -k substring | -p ch num | -t num | -g | -f style substring | -r ] FILE";

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println(USAGE);
            return;
        }

        String inputFile = args[args.length - 1];
        List<String> options = new ArrayList<>(Arrays.asList(args).subList(0, args.length - 1));

        try {
            List<String> lines = readFile(inputFile);
            processOptions(options, lines);
            for (String line : lines) {
                System.out.print(line);
            }
        } catch (IOException e) {
            System.err.println(USAGE);
        } catch (IllegalArgumentException e) {
            System.err.println(USAGE);
        }
    }

    private static List<String> readFile(String fileName) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line + System.lineSeparator());
            }
        }
        return lines;
    }

    private static void processOptions(List<String> options, List<String> lines) {
        String keepSubstring = null;
        String padChar = null;
        Integer padLength = null;
        Integer trimLength = null;
        boolean globalReplace = false;
        String formatStyle = null;
        String formatSubstring = null;
        boolean reverse = false;

        for (int i = 0; i < options.size(); i++) {
            switch (options.get(i)) {
                case "-k":
                    if (i + 1 >= options.size()) throw new IllegalArgumentException();
                    keepSubstring = options.get(++i);
                    break;
                case "-p":
                    if (i + 2 >= options.size()) throw new IllegalArgumentException();
                    padChar = options.get(++i);
                    padLength = Integer.parseInt(options.get(++i));
                    if (padLength < 1 || padLength > 100 || padChar.length() != 1) throw new IllegalArgumentException();
                    break;
                case "-t":
                    if (i + 1 >= options.size()) throw new IllegalArgumentException();
                    trimLength = Integer.parseInt(options.get(++i));
                    if (trimLength < 0 || trimLength > 100) throw new IllegalArgumentException();
                    break;
                case "-g":
                    globalReplace = true;
                    break;
                case "-f":
                    if (i + 2 >= options.size()) throw new IllegalArgumentException();
                    formatStyle = options.get(++i);
                    formatSubstring = options.get(++i);
                    if (!formatStyle.equals("bold") && !formatStyle.equals("italic") && !formatStyle.equals("code"))
                        throw new IllegalArgumentException();
                    if (formatSubstring.isEmpty()) throw new IllegalArgumentException();
                    break;
                case "-r":
                    reverse = true;
                    break;
                default:
                    throw new IllegalArgumentException();
            }
        }

        if (padLength != null && trimLength != null) throw new IllegalArgumentException();
        if (globalReplace && formatStyle == null) throw new IllegalArgumentException();

        if (keepSubstring != null) keepLines(lines, keepSubstring);
        if (padLength != null) padLines(lines, padChar, padLength);
        if (trimLength != null) trimLines(lines, trimLength);
        if (formatStyle != null) formatLines(lines, formatStyle, formatSubstring, globalReplace);
        if (reverse) Collections.reverse(lines);
    }

    private static void keepLines(List<String> lines, String substring) {
        lines.removeIf(line -> !line.contains(substring));
    }

    private static void padLines(List<String> lines, String padChar, int padLength) {
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int padding = padLength - line.length() + 1;  // +1 for the newline character
            if (padding > 0) {
                lines.set(i, String.join("", Collections.nCopies(padding, padChar)) + line);
            }
        }
    }

    private static void trimLines(List<String> lines, int trimLength) {
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.length() > trimLength + 1) {  // +1 for the newline character
                lines.set(i, line.substring(0, trimLength) + System.lineSeparator());
            }
        }
    }

    private static void formatLines(List<String> lines, String style, String substring, boolean global) {
        String prefix, suffix;
        switch (style) {
            case "bold":
                prefix = "**";
                suffix = "**";
                break;
            case "italic":
                prefix = "*";
                suffix = "*";
                break;
            case "code":
                prefix = "`";
                suffix = "`";
                break;
            default:
                return;
        }

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (global) {
                line = line.replace(substring, prefix + substring + suffix);
            } else {
                int index = line.indexOf(substring);
                if (index != -1) {
                    line = line.substring(0, index) + prefix + substring + suffix + line.substring(index + substring.length());
                }
            }
            lines.set(i, line);
        }
    }

    private static void usage() {
        System.err.println("Usage: moditext [ -k substring | -p ch num | -t num | -g | -f style substring | -r ] FILE");
    }
}
