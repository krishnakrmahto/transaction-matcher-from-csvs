package repository;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.csv.CSVFormat.Builder;
import org.apache.commons.csv.CSVRecord;

public class CsvRepository implements Repository<CSVRecord> {

  public List<CSVRecord> read(String fileName) {

    try {
      Reader fileReader = new FileReader(fileName);
      return Builder.create().setSkipHeaderRecord(true).build()
          .parse(fileReader)
          .stream()
          .skip(1)
          .collect(Collectors.toList());

    } catch (IOException e) {
      throw new RuntimeException("Cannot parse: " + fileName);
    }
  }

}
