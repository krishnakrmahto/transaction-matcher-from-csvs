package service.aggregate.impl;

import org.apache.commons.csv.CSVRecord;

public class CsvRecordMatches extends RecordMatches<CSVRecord>{

  public CsvRecordMatches(CSVRecord firstRecord, CSVRecord secondRecord) {
    super(firstRecord, secondRecord);
  }
}
