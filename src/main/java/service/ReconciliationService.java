package service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import request.ReconciliationRequest;
import similaritymetric.SimilarityMetricFactory;
import similaritymetric.datesimilarity.DateSimilarityMetric;
import similaritymetric.numbersimilarity.NumberSimilarityMetric;
import similaritymetric.textsimilarity.TextSimilarityMetric;

public abstract class ReconciliationService<T> {

  protected ReconciliationEntityReferences entityReferences;

  protected DateSimilarityMetric dateSimilarityMetric;
  protected NumberSimilarityMetric numberSimilarityMetric;
  protected TextSimilarityMetric textSimilarityMetric;

  protected List<SupportedValueDataTypes> dataTypeSequence;

  public final ReconciliationAggregate<T> reconcile(ReconciliationRequest request) {
    initializeSimilarityMetrics(request);
//    initializeReconciliationEntities(request);
    ReconciliationAggregate<T> reconciliationAggregate = initReconciliationAggregateWithExactMatches();

    return null;
  }

  private void initializeSimilarityMetrics(ReconciliationRequest request) {
    entityReferences = request.getEntityReferences();
    dateSimilarityMetric = SimilarityMetricFactory.getDateSimilarityMetric(
        request.getDateSimilarityMetricStrategy());
    numberSimilarityMetric = SimilarityMetricFactory.getNumberSimilarityMetric(
        request.getNumberSimilarityMetricStrategy());
    textSimilarityMetric = SimilarityMetricFactory.getTextSimilarityMetric(
        request.getTextSimilarityMetricStrategy());
  }

  protected boolean isValueDate(String value) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss:SSS");
    try {
      LocalDateTime.parse(value, formatter);
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

  protected abstract void populateDataTypeSequence(T singleReconciliationEntity);

//  protected abstract void initializeReconciliationEntities(ReconciliationRequest request);

  protected abstract ReconciliationAggregate<T> initReconciliationAggregateWithExactMatches();

//  protected abstract void fillAggregateWithPartialMatches(ReconciliationAggregate<T> reconciliationAggregate);

//  protected abstract void fillAggregateOnlyInBuyer(Recon)
}
