package ltd.idcu.est.plugin.marketplace.impl;

import ltd.idcu.est.plugin.marketplace.api.PluginReview;
import ltd.idcu.est.plugin.marketplace.api.PluginReviewService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultPluginReviewServiceTest {

    private PluginReviewService reviewService;

    @BeforeEach
    void setUp() {
        reviewService = new DefaultPluginReviewService();
    }

    @AfterEach
    void tearDown() {
        reviewService = null;
    }

    @Test
    void testAddReview() {
        PluginReview review = reviewService.addReview(
            "test-plugin",
            "user1",
            "User One",
            5,
            "Great plugin!",
            "This plugin is exactly what I needed."
        );
        
        assertNotNull(review);
        assertNotNull(review.getId());
        assertEquals("test-plugin", review.getPluginId());
        assertEquals("user1", review.getUserId());
        assertEquals("User One", review.getUserName());
        assertEquals(5, review.getRating());
        assertEquals("Great plugin!", review.getTitle());
        assertEquals("This plugin is exactly what I needed.", review.getContent());
        assertEquals(0, review.getHelpfulCount());
    }

    @Test
    void testGetReview() {
        PluginReview added = reviewService.addReview(
            "test-plugin",
            "user1",
            "User One",
            4,
            "Good",
            "Works well"
        );
        
        Optional<PluginReview> retrieved = reviewService.getReview(added.getId());
        assertTrue(retrieved.isPresent());
        assertEquals(added.getId(), retrieved.get().getId());
    }

    @Test
    void testGetReviewNonExistent() {
        Optional<PluginReview> review = reviewService.getReview("nonexistent-id");
        assertTrue(review.isEmpty());
    }

    @Test
    void testGetReviewsForPlugin() {
        reviewService.addReview("plugin1", "user1", "User 1", 5, "Great", "Excellent");
        reviewService.addReview("plugin1", "user2", "User 2", 4, "Good", "Nice");
        reviewService.addReview("plugin2", "user3", "User 3", 3, "Okay", "Average");
        
        List<PluginReview> reviews = reviewService.getReviewsForPlugin("plugin1");
        assertEquals(2, reviews.size());
    }

    @Test
    void testGetReviewsForPluginWithPagination() {
        for (int i = 0; i < 5; i++) {
            reviewService.addReview(
                "plugin1",
                "user" + i,
                "User " + i,
                5 - i,
                "Review " + i,
                "Content " + i
            );
        }
        
        List<PluginReview> page1 = reviewService.getReviewsForPlugin("plugin1", 0, 2);
        List<PluginReview> page2 = reviewService.getReviewsForPlugin("plugin1", 1, 2);
        List<PluginReview> page3 = reviewService.getReviewsForPlugin("plugin1", 2, 2);
        
        assertEquals(2, page1.size());
        assertEquals(2, page2.size());
        assertEquals(1, page3.size());
    }

    @Test
    void testGetReviewsByUser() {
        reviewService.addReview("plugin1", "user1", "User 1", 5, "Review 1", "Content 1");
        reviewService.addReview("plugin2", "user1", "User 1", 4, "Review 2", "Content 2");
        reviewService.addReview("plugin1", "user2", "User 2", 3, "Review 3", "Content 3");
        
        List<PluginReview> reviews = reviewService.getReviewsByUser("user1");
        assertEquals(2, reviews.size());
    }

    @Test
    void testGetAverageRating() {
        reviewService.addReview("plugin1", "user1", "User 1", 5, "R1", "C1");
        reviewService.addReview("plugin1", "user2", "User 2", 3, "R2", "C2");
        reviewService.addReview("plugin1", "user3", "User 3", 4, "R3", "C3");
        
        double avg = reviewService.getAverageRating("plugin1");
        assertEquals(4.0, avg, 0.001);
    }

    @Test
    void testGetAverageRatingNoReviews() {
        double avg = reviewService.getAverageRating("plugin-with-no-reviews");
        assertEquals(0.0, avg, 0.001);
    }

    @Test
    void testGetReviewCount() {
        reviewService.addReview("plugin1", "user1", "User 1", 5, "R1", "C1");
        reviewService.addReview("plugin1", "user2", "User 2", 4, "R2", "C2");
        
        int count = reviewService.getReviewCount("plugin1");
        assertEquals(2, count);
    }

    @Test
    void testGetReviewCountNoReviews() {
        int count = reviewService.getReviewCount("plugin-with-no-reviews");
        assertEquals(0, count);
    }

    @Test
    void testUpdateReview() {
        PluginReview original = reviewService.addReview(
            "plugin1",
            "user1",
            "User 1",
            3,
            "Original Title",
            "Original content"
        );
        
        PluginReview updated = reviewService.updateReview(
            original.getId(),
            5,
            "Updated Title",
            "Updated content"
        );
        
        assertNotNull(updated);
        assertEquals(original.getId(), updated.getId());
        assertEquals(5, updated.getRating());
        assertEquals("Updated Title", updated.getTitle());
        assertEquals("Updated content", updated.getContent());
        assertTrue(updated.getUpdatedAt() > original.getUpdatedAt());
    }

    @Test
    void testUpdateNonExistentReview() {
        PluginReview result = reviewService.updateReview(
            "nonexistent-id",
            5,
            "Title",
            "Content"
        );
        assertNull(result);
    }

    @Test
    void testDeleteReview() {
        PluginReview review = reviewService.addReview(
            "plugin1",
            "user1",
            "User 1",
            4,
            "R1",
            "C1"
        );
        
        boolean result = reviewService.deleteReview(review.getId());
        assertTrue(result);
        
        Optional<PluginReview> retrieved = reviewService.getReview(review.getId());
        assertTrue(retrieved.isEmpty());
    }

    @Test
    void testDeleteNonExistentReview() {
        boolean result = reviewService.deleteReview("nonexistent-id");
        assertFalse(result);
    }

    @Test
    void testMarkHelpful() {
        PluginReview review = reviewService.addReview(
            "plugin1",
            "user1",
            "User 1",
            5,
            "R1",
            "C1"
        );
        
        boolean result = reviewService.markHelpful(review.getId(), "voter1");
        assertTrue(result);
        
        Optional<PluginReview> updated = reviewService.getReview(review.getId());
        assertTrue(updated.isPresent());
        assertEquals(1, updated.get().getHelpfulCount());
    }

    @Test
    void testMarkHelpfulSameUserTwice() {
        PluginReview review = reviewService.addReview(
            "plugin1",
            "user1",
            "User 1",
            5,
            "R1",
            "C1"
        );
        
        boolean first = reviewService.markHelpful(review.getId(), "voter1");
        boolean second = reviewService.markHelpful(review.getId(), "voter1");
        
        assertTrue(first);
        assertFalse(second);
        
        Optional<PluginReview> updated = reviewService.getReview(review.getId());
        assertTrue(updated.isPresent());
        assertEquals(1, updated.get().getHelpfulCount());
    }

    @Test
    void testUnmarkHelpful() {
        PluginReview review = reviewService.addReview(
            "plugin1",
            "user1",
            "User 1",
            5,
            "R1",
            "C1"
        );
        
        reviewService.markHelpful(review.getId(), "voter1");
        boolean result = reviewService.unmarkHelpful(review.getId(), "voter1");
        
        assertTrue(result);
        
        Optional<PluginReview> updated = reviewService.getReview(review.getId());
        assertTrue(updated.isPresent());
        assertEquals(0, updated.get().getHelpfulCount());
    }

    @Test
    void testUnmarkHelpfulNotMarked() {
        PluginReview review = reviewService.addReview(
            "plugin1",
            "user1",
            "User 1",
            5,
            "R1",
            "C1"
        );
        
        boolean result = reviewService.unmarkHelpful(review.getId(), "voter1");
        assertFalse(result);
    }

    @Test
    void testHasUserReviewed() {
        reviewService.addReview("plugin1", "user1", "User 1", 5, "R1", "C1");
        
        assertTrue(reviewService.hasUserReviewed("plugin1", "user1"));
        assertFalse(reviewService.hasUserReviewed("plugin1", "user2"));
        assertFalse(reviewService.hasUserReviewed("plugin2", "user1"));
    }

    @Test
    void testMultipleHelpfulVotes() {
        PluginReview review = reviewService.addReview(
            "plugin1",
            "user1",
            "User 1",
            5,
            "R1",
            "C1"
        );
        
        reviewService.markHelpful(review.getId(), "voter1");
        reviewService.markHelpful(review.getId(), "voter2");
        reviewService.markHelpful(review.getId(), "voter3");
        
        Optional<PluginReview> updated = reviewService.getReview(review.getId());
        assertTrue(updated.isPresent());
        assertEquals(3, updated.get().getHelpfulCount());
    }
}
