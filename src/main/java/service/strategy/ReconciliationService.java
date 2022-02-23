package service.strategy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import request.ReconciliationRequest;
import service.SupportedValueDataTypes;
import service.aggregate.ReconciliationAggregate;
import similaritymetric.SimilarityMetricFactory;
import similaritymetric.datesimilarity.DateSimilarityMetric;
import similaritymetric.numbersimilarity.NumberSimilarityMetric;
import similaritymetric.textsimilarity.TextSimilarityMetric;

public abstract class ReconciliationService<T> {

  protected DateSimilarityMetric dateSimilarityMetric;
  protected NumberSimilarityMetric numberSimilarityMetric;
  protected TextSimilarityMetric textSimilarityMetric;

  protected List<SupportedValueDataTypes> dataTypeSequence = new ArrayList<>();

  public final ReconciliationAggregate<T> reconcile(ReconciliationRequest request) {
    initializeSimilarityMetrics(request);
    List<T> firstReconciliationEntityList = getFirstReconciliationEntityList(request);
    List<T> secondReconciliationEntityList = getSecondReconciliationEntityList(request);

    populateDataTypeSequence(firstReconciliationEntityList, secondReconciliationEntityList);

    return process(firstReconciliationEntityList, secondReconciliationEntityList);
  }

  protected abstract List<T> getFirstReconciliationEntityList(ReconciliationRequest request);

  protected abstract List<T> getSecondReconciliationEntityList(
      ReconciliationRequest request);

  protected abstract ReconciliationAggregate<T> process(List<T> firstReconciliationEntityList,
      List<T> secondReconciliationEntityList);

  private void initializeSimilarityMetrics(ReconciliationRequest request) {
    dateSimilarityMetric = SimilarityMetricFactory.getDateSimilarityMetric(request);
    numberSimilarityMetric = SimilarityMetricFactory.getNumberSimilarityMetric(request);
    textSimilarityMetric = SimilarityMetricFactory.getTextSimilarityMetric(request);
  }

  protected boolean isValueDate(String value) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    try {
      LocalDate.parse(value, formatter);
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  protected boolean isValueNumber(String value) {
    try {
      Double.parseDouble(value);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  protected Optional<LocalDate> convertToDate(String value) {
    List<DateTimeFormatter> knownDateFormatters =
        Arrays.asList(DateTimeFormatter.ofPattern("d-M-yy"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("d/M/yy"),
            DateTimeFormatter.ofPattern("dd/M/yy"));

    for(DateTimeFormatter knownFormatter: knownDateFormatters) {
      try {
        return Optional.of(LocalDate.parse(value, knownFormatter));
      } catch (DateTimeParseException e) {
        System.out.println("Could not parse date: " + value + " to pattern: " + knownFormatter);
      }
    }

    return Optional.empty();
  }

  protected abstract void populateDataTypeSequence(List<T> firstReconciliationEntity, List<T> secondReconciliationEntity);

}
