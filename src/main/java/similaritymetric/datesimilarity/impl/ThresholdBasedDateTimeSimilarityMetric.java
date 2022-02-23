package similaritymetric.datesimilarity.impl;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
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

    long largerDateTimeEpoch = Math.max(firstDateEpoch, secondDateEpoch);
    long epochDifference = Math.abs(ChronoUnit.SECONDS.between(firstDate, secondDate));
    double similarityIndex = (largerDateTimeEpoch - epochDifference)/(double) largerDateTimeEpoch;

    return similarityIndex <= threshold? similarityIndex: 0;
  }
}
