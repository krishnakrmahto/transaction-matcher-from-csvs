package textsimilarity;

public interface TextSimilarityMetric {

  double compute(String firstText, String secondText);

}
