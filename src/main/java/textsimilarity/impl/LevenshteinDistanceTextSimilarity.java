package textsimilarity.impl;

import org.apache.commons.text.similarity.LevenshteinDistance;
import textsimilarity.TextSimilarityMetric;

public class LevenshteinDistanceTextSimilarity implements TextSimilarityMetric {

  @Override
  public double compute(String firstText, String secondText) {
    return LevenshteinDistance.getDefaultInstance().apply(firstText, secondText);
  }
}
