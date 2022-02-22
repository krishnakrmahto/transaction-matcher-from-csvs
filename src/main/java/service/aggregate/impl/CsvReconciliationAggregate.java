package service.aggregate.impl;

import java.util.List;
import org.apache.commons.csv.CSVRecord;
import service.aggregate.ReconciliationAggregate;

public class CsvReconciliationAggregate implements ReconciliationAggregate<CSVRecord> {

  private List<CsvRecordMatches> exactMatches;
  private List<CsvRecordMatches> partialMatches ;
  private List<CSVRecord> onlyInFirstFileList;
  private List<CSVRecord> onlyInSecondFileList;


  @Override
  public void putSingleExactMatch(CSVRecord firstRecord, CSVRecord secondRecord) {
    this.exactMatches.add(new CsvRecordMatches(firstRecord, secondRecord));
  }

  @Override
  public void putSinglePartialMatch(CSVRecord firstRecord, CSVRecord secondRecord) {
    this.partialMatches.add(new CsvRecordMatches(firstRecord, secondRecord));
  }

  @Override
  public void putSingleOnlyInFirstFile(CSVRecord onlyInFirstFile) {
    this.onlyInFirstFileList.add(onlyInFirstFile);
  }

  @Override
  public void putSingleOnlyInSecondFile(CSVRecord onlyInSecondFile) {
    this.onlyInFirstFileList.add(onlyInSecondFile);
  }

  public List<CsvRecordMatches> getExactMatches() {
    return exactMatches;
  }

  public List<CsvRecordMatches> getPartialMatches() {
    return partialMatches;
  }

  public List<CSVRecord> getOnlyInFirstFileList() {
    return onlyInFirstFileList;
  }

  public List<CSVRecord> getOnlyInSecondFileList() {
    return onlyInSecondFileList;
  }
}
