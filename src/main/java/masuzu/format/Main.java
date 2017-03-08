package masuzu.format;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.google.googlejavaformat.java.JavaFormatterOptions;

import static com.google.googlejavaformat.java.JavaFormatterOptions.Style;


public class Main {
    public static void main(String [] args) throws FormatterException, IOException {
        if (args.length != 1) {
            throw new IOException("Usage: java masuzu.format.Main <input_java_file_to_format>");
        }
        String fileName = args[0];

        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            String sourceString = String.join("\n", stream.collect(Collectors.toList()));
            JavaFormatterOptions options = JavaFormatterOptions.builder().style(Style.AOSP).build();
            String formattedSource = new Formatter(options).formatSource(sourceString);

            Path outputFilePath = Paths.get(fileName + ".bak");
            try (BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
                writer.write(formattedSource);
            }
            new File(outputFilePath.toString()).renameTo(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
