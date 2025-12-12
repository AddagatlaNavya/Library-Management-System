package com.library.recommendation;

import java.util.Arrays;
import java.util.List;

/**
 * Factory for creating recommendation engines
 */
public class RecommendationEngineFactory {

	public static RecommendationEngine createEngine(RecommendationType type) {
		switch (type) {
		case CONTENT_BASED:
			return new ContentBasedRecommendation();
		case POPULARITY_BASED:
			return new PopularityBasedRecommendation();
		case HYBRID:
			List<RecommendationEngine> engines = Arrays.asList(new ContentBasedRecommendation(),
					new PopularityBasedRecommendation());
			List<Double> weights = Arrays.asList(0.6, 0.4);
			return new HybridRecommendation(engines, weights);
		default:
			throw new IllegalArgumentException("Unknown recommendation type: " + type);
		}
	}
}