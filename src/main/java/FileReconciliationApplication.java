import request.ReconciliationRequest;
import service.FileReconciliationService;
import service.ReconciliationAggregate;
import service.ReconciliationService;
import similaritymetric.DateSimilarityMetricStrategy;
import similaritymetric.NumberSimilarityMetricStrategy;
import similaritymetric.TextSimilarityMetricStrategy;

public class FileReconciliationApplication {

  public static void main(String[] args) {

    String firstFileName = args[0];
    String secondFileName = args[1];

//    List<CSVRecord> firstFile = CsvRepository.readCsvRecords(args[0]);
//    List<CSVRecord> secondFile = CsvRepository.readCsvRecords(args[1]);

    ReconciliationRequest request = new ReconciliationRequest(DateSimilarityMetricStrategy.THRESHOLD_BASED, NumberSimilarityMetricStrategy.THRESHOLD_BASED,
        TextSimilarityMetricStrategy.LEVENSHTEIN_DISTANCE);

    ReconciliationService reconciliationService = new FileReconciliationService();

    ReconciliationAggregate reconciliationAggregate = reconciliationService.reconcile(request);
  }

}
