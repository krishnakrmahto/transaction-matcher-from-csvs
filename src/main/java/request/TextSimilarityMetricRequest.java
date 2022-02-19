package request;

import similaritymetric.TextSimilarityMetricStrategy;

public class TextSimilarityMetricRequest {

  private TextSimilarityMetricStrategy strategy;
  private double threshold;

  public TextSimilarityMetricRequest(TextSimilarityMetricStrategy strategy, double threshold) {
    this.strategy = strategy;
    this.threshold = threshold;
  }

  public TextSimilarityMetricStrategy getStrategy() {
    return strategy;
  }

  public double getThreshold() {
    return threshold;
  }
}
