package similaritymetric.datesimilarity.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import similaritymetric.datesimilarity.DateSimilarityMetric;

public class ThresholdBasedDateTimeSimilarityMetric implements DateSimilarityMetric {

  @Override
  public long compute(LocalDateTime firstDateTime, LocalDateTime secondDateTime, double threshold) {

    long firstDateTimeSecondDateTimeDiff = Math.abs(ChronoUnit.SECONDS.between(firstDateTime, secondDateTime));

    return firstDateTimeSecondDateTimeDiff <= threshold? firstDateTimeSecondDateTimeDiff: Long.MAX_VALUE;
  }
}
