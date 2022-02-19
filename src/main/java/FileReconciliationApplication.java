import request.ReconciliationRequest;
import service.ReconciliationAggregate;
import service.ReconciliationEntityReferences;
import service.ReconciliationService;
import service.ReconciliationServiceFactory;
import service.ReconciliationServiceStrategy;
import similaritymetric.DateSimilarityMetricStrategy;
import similaritymetric.NumberSimilarityMetricStrategy;
import similaritymetric.TextSimilarityMetricStrategy;

public class FileReconciliationApplication {

  public static void main(String[] args) {

    String firstFileName = args[0];
    String secondFileName = args[1];

    ReconciliationRequest request = new ReconciliationRequest(
        new ReconciliationEntityReferences(firstFileName, secondFileName),
        DateSimilarityMetricStrategy.THRESHOLD_BASED, NumberSimilarityMetricStrategy.THRESHOLD_BASED,
        TextSimilarityMetricStrategy.LEVENSHTEIN_DISTANCE);

    ReconciliationService reconciliationService = ReconciliationServiceFactory.get(ReconciliationServiceStrategy.CSV_FILE);

    ReconciliationAggregate reconciliationAggregate = reconciliationService.reconcile(request);
  }

}
