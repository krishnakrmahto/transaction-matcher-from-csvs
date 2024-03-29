package similaritymetric.datesimilarity.impl;

import java.time.LocalDate;
import java.time.ZoneOffset;
import similaritymetric.datesimilarity.DateSimilarityMetric;

public class ThresholdBasedDateTimeSimilarityMetric implements DateSimilarityMetric {

  private double threshold;

  public ThresholdBasedDateTimeSimilarityMetric(double threshold) {
    this.threshold = threshold;
  }

  @Override
  public double compute(LocalDate firstDate, LocalDate secondDate) {

    if (firstDate.equals(secondDate)) {
      return 1;
    }

    long firstDateEpoch = firstDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC);
    long secondDateEpoch = secondDate.atStartOfDay().toEpochSecond(ZoneOffset.UTC);


    long smallerDateTimeEpoch = Math.min(firstDateEpoch, secondDateEpoch);
    long largerDateTimeEpoch = Math.max(firstDateEpoch, secondDateEpoch);

    double similarityIndex = (double) smallerDateTimeEpoch/largerDateTimeEpoch ;

    return similarityIndex <= threshold? similarityIndex: 0;
  }
}
