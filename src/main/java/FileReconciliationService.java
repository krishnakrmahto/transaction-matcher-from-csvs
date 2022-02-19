import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import org.apache.commons.csv.CSVRecord;

public class FileReconciliationService {

  private final List<CSVRecord> firstFile;
  private final List<CSVRecord> secondFile;

  private List<DataType> dataTypes;

  public FileReconciliationService(List<CSVRecord> firstFile, List<CSVRecord> secondFile) {
    this.firstFile = firstFile;
    this.secondFile = secondFile;
    populateDataTypes();
  }

  public ReconciliationAggregate reconcile() {

    ReconciliationAggregate reconciliationAggregate = new ReconciliationAggregate();

    List<CSVRecord> exactMatches = getExactMatches();

    List<CSVRecord> partialMatches = getPartialMatches(exactMatches);

    return new ReconciliationAggregate();
  }

  private void populateDataTypes() {
    CSVRecord firstRecord = firstFile.get(0);
    firstRecord.stream()
        .map(value -> {
          if (isValueDate(value)) {
            return "date_time";
          } else if (isValueNumber(value)) {
            return "numeric";
          }
          return "string";
        })
        .forEach(dataType -> dataTypes.add(new DataType(dataType)));
  }

  private List<CSVRecord> getExactMatches() {

//    firstFile.stream().filter(csvRecord -> secondFile)
    return Collections.emptyList();


  }

  private List<CSVRecord> getPartialMatches(List<CSVRecord> exactMatches) {
    return Collections.emptyList();
  }

  private boolean isValueDate(String value) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS");
    try {
      LocalDateTime.parse(value, formatter);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  private boolean isValueNumber(String value) {
    try {
      Double.parseDouble(value);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
