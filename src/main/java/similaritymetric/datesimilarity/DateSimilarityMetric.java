package similaritymetric.datesimilarity;

import java.time.LocalDate;

public interface DateSimilarityMetric {

  double compute(LocalDate firstDate, LocalDate secondDate);
}
