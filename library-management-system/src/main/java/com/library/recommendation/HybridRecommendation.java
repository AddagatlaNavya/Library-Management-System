package com.library.recommendation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.library.model.Book;
import com.library.model.Patron;

/**
 * Hybrid recommendation strategy
 * Combines multiple recommendation approaches
 */
class HybridRecommendation implements RecommendationEngine {
    private static final Logger logger = Logger.getLogger(HybridRecommendation.class.getName());
    
    private final List<RecommendationEngine> engines;
    private final List<Double> weights;
    
    public HybridRecommendation(List<RecommendationEngine> engines, List<Double> weights) {
        if (engines.size() != weights.size()) {
            throw new IllegalArgumentException("Engines and weights must have same size");
        }
        this.engines = engines;
        this.weights = weights;
    }
    
    @Override
    public List<Book> getRecommendations(Patron patron, List<Book> availableBooks, int limit) {
        Map<Book, Double> combinedScores = new HashMap<>();
        
        // Combine recommendations from all engines
        for (int i = 0; i < engines.size(); i++) {
            RecommendationEngine engine = engines.get(i);
            double weight = weights.get(i);
            
            List<Book> recommendations = engine.getRecommendations(patron, availableBooks, limit * 2);
            
            // Assign scores based on rank (higher rank = higher score)
            for (int j = 0; j < recommendations.size(); j++) {
                Book book = recommendations.get(j);
                double score = (recommendations.size() - j) * weight;
                combinedScores.merge(book, score, Double::sum);
            }
        }
        
        // Sort by combined score and return top N
        List<Book> finalRecommendations = combinedScores.entrySet().stream()
            .sorted(Map.Entry.<Book, Double>comparingByValue().reversed())
            .limit(limit)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
        
        logger.info(String.format("Generated %d hybrid recommendations for patron %s", 
            finalRecommendations.size(), patron.getPatronId()));
        
        return finalRecommendations;
    }
}
