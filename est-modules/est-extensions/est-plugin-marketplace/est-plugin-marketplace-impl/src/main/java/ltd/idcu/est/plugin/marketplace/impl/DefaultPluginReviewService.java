package ltd.idcu.est.plugin.marketplace.impl;

import ltd.idcu.est.plugin.marketplace.api.PluginReview;
import ltd.idcu.est.plugin.marketplace.api.PluginReviewService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DefaultPluginReviewService implements PluginReviewService {
    
    private final Map<String, PluginReview> reviewsById = new ConcurrentHashMap<>();
    private final Map<String, List<PluginReview>> reviewsByPlugin = new ConcurrentHashMap<>();
    private final Map<String, List<PluginReview>> reviewsByUser = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> helpfulVotes = new ConcurrentHashMap<>();
    
    @Override
    public Optional<PluginReview> getReview(String reviewId) {
        return Optional.ofNullable(reviewsById.get(reviewId));
    }
    
    @Override
    public List<PluginReview> getReviewsForPlugin(String pluginId) {
        return reviewsByPlugin.getOrDefault(pluginId, Collections.emptyList());
    }
    
    @Override
    public List<PluginReview> getReviewsForPlugin(String pluginId, int page, int pageSize) {
        List<PluginReview> all = getReviewsForPlugin(pluginId);
        int start = page * pageSize;
        int end = Math.min(start + pageSize, all.size());
        if (start >= all.size()) {
            return Collections.emptyList();
        }
        return all.subList(start, end);
    }
    
    @Override
    public List<PluginReview> getReviewsByUser(String userId) {
        return reviewsByUser.getOrDefault(userId, Collections.emptyList());
    }
    
    @Override
    public double getAverageRating(String pluginId) {
        List<PluginReview> reviews = getReviewsForPlugin(pluginId);
        if (reviews.isEmpty()) {
            return 0.0;
        }
        return reviews.stream()
            .mapToInt(PluginReview::getRating)
            .average()
            .orElse(0.0);
    }
    
    @Override
    public int getReviewCount(String pluginId) {
        return getReviewsForPlugin(pluginId).size();
    }
    
    @Override
    public PluginReview addReview(String pluginId, String userId, String userName,
                                  int rating, String title, String content) {
        String reviewId = UUID.randomUUID().toString();
        long now = System.currentTimeMillis();
        
        PluginReview review = PluginReview.builder()
            .id(reviewId)
            .pluginId(pluginId)
            .userId(userId)
            .userName(userName)
            .rating(rating)
            .title(title)
            .content(content)
            .createdAt(now)
            .updatedAt(now)
            .helpfulCount(0)
            .build();
        
        reviewsById.put(reviewId, review);
        reviewsByPlugin.computeIfAbsent(pluginId, k -> new ArrayList<>()).add(review);
        reviewsByUser.computeIfAbsent(userId, k -> new ArrayList<>()).add(review);
        
        return review;
    }
    
    @Override
    public PluginReview updateReview(String reviewId, int rating, String title, String content) {
        PluginReview oldReview = reviewsById.get(reviewId);
        if (oldReview == null) {
            return null;
        }
        
        PluginReview newReview = PluginReview.builder()
            .id(oldReview.getId())
            .pluginId(oldReview.getPluginId())
            .userId(oldReview.getUserId())
            .userName(oldReview.getUserName())
            .rating(rating)
            .title(title)
            .content(content)
            .createdAt(oldReview.getCreatedAt())
            .updatedAt(System.currentTimeMillis())
            .helpfulCount(oldReview.getHelpfulCount())
            .build();
        
        reviewsById.put(reviewId, newReview);
        replaceReviewInList(reviewsByPlugin.get(newReview.getPluginId()), oldReview, newReview);
        replaceReviewInList(reviewsByUser.get(newReview.getUserId()), oldReview, newReview);
        
        return newReview;
    }
    
    @Override
    public boolean deleteReview(String reviewId) {
        PluginReview review = reviewsById.remove(reviewId);
        if (review == null) {
            return false;
        }
        removeReviewFromList(reviewsByPlugin.get(review.getPluginId()), review);
        removeReviewFromList(reviewsByUser.get(review.getUserId()), review);
        return true;
    }
    
    @Override
    public boolean markHelpful(String reviewId, String userId) {
        String key = reviewId + ":" + userId;
        Set<String> voters = helpfulVotes.computeIfAbsent(reviewId, k -> ConcurrentHashMap.newKeySet());
        if (voters.add(userId)) {
            PluginReview review = reviewsById.get(reviewId);
            if (review != null) {
                PluginReview updated = PluginReview.builder()
                    .id(review.getId())
                    .pluginId(review.getPluginId())
                    .userId(review.getUserId())
                    .userName(review.getUserName())
                    .rating(review.getRating())
                    .title(review.getTitle())
                    .content(review.getContent())
                    .createdAt(review.getCreatedAt())
                    .updatedAt(review.getUpdatedAt())
                    .helpfulCount(review.getHelpfulCount() + 1)
                    .build();
                reviewsById.put(reviewId, updated);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean unmarkHelpful(String reviewId, String userId) {
        Set<String> voters = helpfulVotes.get(reviewId);
        if (voters != null && voters.remove(userId)) {
            PluginReview review = reviewsById.get(reviewId);
            if (review != null) {
                PluginReview updated = PluginReview.builder()
                    .id(review.getId())
                    .pluginId(review.getPluginId())
                    .userId(review.getUserId())
                    .userName(review.getUserName())
                    .rating(review.getRating())
                    .title(review.getTitle())
                    .content(review.getContent())
                    .createdAt(review.getCreatedAt())
                    .updatedAt(review.getUpdatedAt())
                    .helpfulCount(Math.max(0, review.getHelpfulCount() - 1))
                    .build();
                reviewsById.put(reviewId, updated);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean hasUserReviewed(String pluginId, String userId) {
        List<PluginReview> reviews = reviewsByUser.get(userId);
        if (reviews == null) {
            return false;
        }
        return reviews.stream().anyMatch(r -> pluginId.equals(r.getPluginId()));
    }
    
    private void replaceReviewInList(List<PluginReview> list, PluginReview oldReview, PluginReview newReview) {
        if (list != null) {
            int index = list.indexOf(oldReview);
            if (index >= 0) {
                list.set(index, newReview);
            }
        }
    }
    
    private void removeReviewFromList(List<PluginReview> list, PluginReview review) {
        if (list != null) {
            list.remove(review);
        }
    }
}
