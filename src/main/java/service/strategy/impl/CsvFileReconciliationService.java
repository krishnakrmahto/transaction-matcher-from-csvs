package service.strategy.impl;

import static service.SupportedValueDataTypes.DATETIME;
import static service.SupportedValueDataTypes.NUMBER;
import static service.SupportedValueDataTypes.TEXT;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.csv.CSVRecord;
import repository.CsvRepository;
import request.ReconciliationRequest;
import service.SupportedValueDataTypes;
import service.aggregate.ReconciliationAggregate;
import service.aggregate.impl.CsvReconciliationAggregate;
import service.bestpartialmatch.BestPartialMatchStrategy;
import service.strategy.ReconciliationService;

public class CsvFileReconciliationService extends ReconciliationService<CSVRecord> {

  private final CsvRepository repository;
  private BestPartialMatchStrategy<List<Double>> bestPartialMatcher;

  public CsvFileReconciliationService(CsvRepository repository) {
    this.repository = repository;
  }

  @Override
  protected void populateDataTypeSequence(CSVRecord singleCsvRecord) {
    singleCsvRecord.toList().stream()
        .map(value -> {
          if (isValueDate(value)) {
            return DATETIME;
          } else if (isValueNumber(value)) {
            return NUMBER;
          }
          return TEXT;
        })
        .forEach(dataTypeSequence::add);
  }

  @Override
  protected ReconciliationAggregate<CSVRecord> process(List<CSVRecord> firstFileCsvRecords, List<CSVRecord> secondFileCsvRecords) {

    int firstFileIteratorIndex = 0;
    int secondFileIteratorIndex = 0;

    ReconciliationAggregate<CSVRecord> reconciliationAggregate = new CsvReconciliationAggregate();

    while (firstFileIteratorIndex < firstFileCsvRecords.size()) {
      CSVRecord firstFileCsvRecord = firstFileCsvRecords.get(firstFileIteratorIndex);
      Map<Integer, List<Double>> secondFileIndexToSimilarityVectorMap = new HashMap<>();

      List<Double> similarityVector;
      while (secondFileIteratorIndex < secondFileCsvRecords.size()) {
        CSVRecord secondFileCsvRecord = secondFileCsvRecords.get(secondFileIteratorIndex);

        similarityVector = IntStream.range(0, dataTypeSequence.size())
            .boxed()
            .map(i -> {
              SupportedValueDataTypes currentDataType = dataTypeSequence.get(i);
              if (currentDataType.equals(DATETIME)) {
                LocalDateTime firstDateTime = LocalDateTime.parse(firstFileCsvRecord.get(i));
                LocalDateTime secondDateTime = LocalDateTime.parse(secondFileCsvRecord.get(i));
                return dateSimilarityMetric.compute(firstDateTime, secondDateTime);
              } else if (currentDataType.equals(NUMBER)) {
                double firstNumber = Double.parseDouble(firstFileCsvRecord.get(i));
                double secondNumber = Double.parseDouble(secondFileCsvRecord.get(i));
                return numberSimilarityMetric.compute(firstNumber, secondNumber);
              } else {
                return textSimilarityMetric.compute(firstFileCsvRecord.get(i),
                    secondFileCsvRecord.get(i));
              }
            })
            .collect(Collectors.toList());

        if (areAllSimilarityIndexOneIn(similarityVector)) {
          reconciliationAggregate.putSingleExactMatch(firstFileCsvRecord, secondFileCsvRecord);
          break;
        } else if (isAtleastOneSimilarityIndexZeroIn(similarityVector)) {
          reconciliationAggregate.putSingleOnlyInFirstFile(firstFileCsvRecord);
          break;
        } else {
          secondFileIndexToSimilarityVectorMap.put(secondFileIteratorIndex, similarityVector);
        }
      }

      int bestPartialMatchIndex = getBestPartialMatchRecordIndex(secondFileIndexToSimilarityVectorMap);
      reconciliationAggregate.putSinglePartialMatch(firstFileCsvRecord, secondFileCsvRecords.get(bestPartialMatchIndex));
    }

      return reconciliationAggregate;
  }

  @Override
  protected List<CSVRecord> getFirstReconciliationEntityList(ReconciliationRequest request) {
    return repository.read(request.getEntityReferences().getFirstEntityReference());
  }

  @Override
  protected List<CSVRecord> getSecondReconciliationEntityList(ReconciliationRequest request) {
    return repository.read(request.getEntityReferences().getSecondEntityReference());
  }

  private int getBestPartialMatchRecordIndex(Map<Integer, List<Double>> secondFileIndexToSimilarityVectorMap) {

    LinkedHashMap<Integer, List<Double>> orderedIndexToSimilarityVectorMap = new LinkedHashMap<>(secondFileIndexToSimilarityVectorMap);

    List<List<Double>> partialMatchSimilarityVectors = new ArrayList<>(orderedIndexToSimilarityVectorMap.values());
    List<Integer> similarityIndexPositions = new ArrayList<>(secondFileIndexToSimilarityVectorMap.keySet());

    return similarityIndexPositions.get(bestPartialMatcher.getBestPartialMatchIndex(partialMatchSimilarityVectors));

  }

  private boolean areAllSimilarityIndexOneIn(List<Double> similarityIndexVector) {
    return similarityIndexVector.stream().allMatch(similarityIndex -> similarityIndex == 1);
  }

  private boolean isAtleastOneSimilarityIndexZeroIn(List<Double> similarityIndexVector) {
    return similarityIndexVector.stream().anyMatch(similarityIndex -> similarityIndex == 0);
  }
}
