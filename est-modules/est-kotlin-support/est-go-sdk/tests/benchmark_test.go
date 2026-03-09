package est

import (
	"testing"
	"time"
)

func BenchmarkClientInitialization(b *testing.B) {
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_ = NewClient("http://localhost:8080")
	}
}

func BenchmarkClientInitializationWithOptions(b *testing.B) {
	options := ClientOptions{
		APIKey:     "test-api-key",
		Timeout:    60 * time.Second,
		MaxRetries: 5,
	}
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_ = NewClientWithOptions("http://localhost:8080", options)
	}
}

func BenchmarkPluginMetadataCreation(b *testing.B) {
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_ = PluginMetadata{
			PluginID:    "test-plugin",
			Name:        "Test Plugin",
			Description: "A test plugin",
			Version:     "1.0.0",
			Author:      "Test Author",
			Category:    "test",
			Tags:        []string{"test", "example"},
			License:     "MIT",
		}
	}
}

func BenchmarkPluginVersionCreation(b *testing.B) {
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_ = PluginVersion{
			Version:      "1.0.0",
			ReleaseNotes: "Initial release",
			ReleasedAt:   time.Now(),
			DownloadURL:  "http://example.com/plugin.zip",
			FileSize:     1024,
		}
	}
}

func BenchmarkPluginReviewCreation(b *testing.B) {
	title := "Great plugin!"
	updatedAt := time.Now()
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_ = PluginReview{
			ReviewID:     "review-1",
			PluginID:     "test-plugin",
			UserID:       "user-1",
			UserName:     "Test User",
			Rating:       5,
			Title:        &title,
			Content:      "Excellent plugin!",
			CreatedAt:    time.Now(),
			UpdatedAt:    &updatedAt,
			HelpfulCount: 10,
		}
	}
}

func BenchmarkPluginSearchCriteriaCreation(b *testing.B) {
	query := "web"
	category := "web"
	author := "john"
	minRating := 4
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_ = PluginSearchCriteria{
			Query:     &query,
			Category:  &category,
			Tags:      []string{"framework", "ui"},
			Author:    &author,
			MinRating: &minRating,
			Page:      1,
			PageSize:  20,
			SortBy:    "rating",
			SortOrder: "desc",
		}
	}
}

func BenchmarkPluginSearchResultCreation(b *testing.B) {
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_ = PluginSearchResult{
			Plugins: []PluginMetadata{
				{
					PluginID: "plugin-1",
					Name:     "Plugin 1",
				},
				{
					PluginID: "plugin-2",
					Name:     "Plugin 2",
				},
			},
			Total:    2,
			Page:     1,
			PageSize: 20,
		}
	}
}

func BenchmarkPluginPublishRequestCreation(b *testing.B) {
	homepage := "https://example.com"
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_ = PluginPublishRequest{
			Name:        "New Plugin",
			Description: "A new plugin",
			Version:     "1.0.0",
			Author:      "Test Author",
			Category:    "test",
			Tags:        []string{"new", "plugin"},
			License:     "MIT",
			Homepage:    &homepage,
			FileData:    []byte("plugin-content"),
		}
	}
}

func BenchmarkPluginPublishResultCreation(b *testing.B) {
	pluginID := "new-plugin"
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_ = PluginPublishResult{
			Success:  true,
			PluginID: &pluginID,
			Message:  "Plugin published successfully",
			Warnings: []string{"warning-1", "warning-2"},
		}
	}
}

func BenchmarkEstErrorCreation(b *testing.B) {
	details := map[string]interface{}{
		"resource_id": "123",
		"path":        "/api/test",
	}
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_ = &EstError{
			Code:    "VALIDATION_ERROR",
			Message: "Validation failed",
			Details: details,
		}
	}
}

func BenchmarkEstErrorErrorMethod(b *testing.B) {
	err := &EstError{
		Code:    "NOT_FOUND",
		Message: "Resource not found",
	}
	b.ResetTimer()
	for i := 0; i < b.N; i++ {
		_ = err.Error()
	}
}

func BenchmarkParallelClientInitialization(b *testing.B) {
	b.RunParallel(func(pb *testing.PB) {
		for pb.Next() {
			_ = NewClient("http://localhost:8080")
		}
	})
}

func BenchmarkMemoryAllocation(b *testing.B) {
	b.ReportAllocs()
	for i := 0; i < b.N; i++ {
		_ = NewClient("http://localhost:8080")
	}
}

func TestBenchmarkClientInitializationSpeed(t *testing.T) {
	if testing.Short() {
		t.Skip("skipping benchmark in short mode")
	}
	
	iterations := 10000
	start := time.Now()
	
	for i := 0; i < iterations; i++ {
		_ = NewClient("http://localhost:8080")
	}
	
	elapsed := time.Since(start)
	avgTime := elapsed / time.Duration(iterations)
	
	t.Logf("Client initialization benchmark:")
	t.Logf("  Iterations: %d", iterations)
	t.Logf("  Total time: %v", elapsed)
	t.Logf("  Average: %v per init", avgTime)
	
	if avgTime > 100*time.Microsecond {
		t.Errorf("Initialization too slow: %v", avgTime)
	}
}

func TestBenchmarkPluginMetadataCreationSpeed(t *testing.T) {
	if testing.Short() {
		t.Skip("skipping benchmark in short mode")
	}
	
	iterations := 100000
	start := time.Now()
	
	for i := 0; i < iterations; i++ {
		_ = PluginMetadata{
			PluginID:    "test-plugin",
			Name:        "Test Plugin",
			Description: "A test plugin",
			Version:     "1.0.0",
			Author:      "Test Author",
			Category:    "test",
		}
	}
	
	elapsed := time.Since(start)
	avgTime := elapsed / time.Duration(iterations)
	
	t.Logf("PluginMetadata creation benchmark:")
	t.Logf("  Iterations: %d", iterations)
	t.Logf("  Total time: %v", elapsed)
	t.Logf("  Average: %v per creation", avgTime)
	
	if avgTime > 10*time.Microsecond {
		t.Errorf("PluginMetadata creation too slow: %v", avgTime)
	}
}
