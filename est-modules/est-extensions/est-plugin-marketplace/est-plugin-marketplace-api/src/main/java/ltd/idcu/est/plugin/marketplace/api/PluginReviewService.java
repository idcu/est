package ltd.idcu.est.plugin.marketplace.api;

import java.util.List;
import java.util.Optional;

public interface PluginReviewService {
    
    Optional<PluginReview> getReview(String reviewId);
    
    List<PluginReview> getReviewsForPlugin(String pluginId);
    
    List<PluginReview> getReviewsForPlugin(String pluginId, int page, int pageSize);
    
    List<PluginReview> getReviewsByUser(String userId);
    
    double getAverageRating(String pluginId);
    
    int getReviewCount(String pluginId);
    
    PluginReview addReview(String pluginId, String userId, String userName,
                          int rating, String title, String content);
    
    PluginReview updateReview(String reviewId, int rating, String title, String content);
    
    boolean deleteReview(String reviewId);
    
    boolean markHelpful(String reviewId, String userId);
    
    boolean unmarkHelpful(String reviewId, String userId);
    
    boolean hasUserReviewed(String pluginId, String userId);
}
