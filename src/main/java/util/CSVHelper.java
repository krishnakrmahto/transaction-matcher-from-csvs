package util;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class CSVHelper {

  public static Stream<CSVRecord> readCsvStream(String fileName) {

    try {
      Reader fileReader = new FileReader(fileName);
      Iterable<CSVRecord> csvRecordIterable = CSVFormat.Builder.create()
          .setSkipHeaderRecord(true)
          .build()
          .parse(fileReader);

      return StreamSupport.stream(csvRecordIterable.spliterator(), true);

    } catch (IOException e) {
      throw new RuntimeException("Cannot parse: " + fileName);
    }
  }

}
