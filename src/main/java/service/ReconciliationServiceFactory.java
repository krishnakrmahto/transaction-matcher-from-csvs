package service;

import repository.CsvRepository;
import service.strategy.ReconciliationService;
import service.strategy.impl.CsvFileReconciliationService;

public class ReconciliationServiceFactory {

  public static ReconciliationService get(ReconciliationServiceStrategy type) {

    if (type.equals(ReconciliationServiceStrategy.CSV_FILE)) {
      return new CsvFileReconciliationService(new CsvRepository());
    }

    throw new UnsupportedClassVersionError("ReconciliationService type : " + type + " not supported");
  }

}
