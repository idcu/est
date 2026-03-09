package est

import (
	"testing"
	"time"
)

func TestClientInitialization(t *testing.T) {
	client := NewClient("http://localhost:8080")
	if client == nil {
		t.Fatal("Expected client to be created")
	}
}

func TestClientWithOptions(t *testing.T) {
	options := ClientOptions{
		APIKey:     "test-api-key",
		Timeout:    60 * time.Second,
		MaxRetries: 5,
	}
	client := NewClientWithOptions("http://localhost:8080", options)
	if client == nil {
		t.Fatal("Expected client to be created with options")
	}
}

func TestClientDefaultOptions(t *testing.T) {
	options := ClientOptions{}
	client := NewClientWithOptions("http://localhost:8080", options)
	if client == nil {
		t.Fatal("Expected client to be created with default options")
	}
}

func TestPluginMetadataCreation(t *testing.T) {
	metadata := PluginMetadata{
		PluginID:    "test-plugin",
		Name:        "Test Plugin",
		Description: "A test plugin",
		Version:     "1.0.0",
		Author:      "Test Author",
		Category:    "test",
		Tags:        []string{"test", "example"},
		License:     "MIT",
	}

	if metadata.PluginID != "test-plugin" {
		t.Errorf("Expected plugin_id to be 'test-plugin', got %s", metadata.PluginID)
	}
	if metadata.Name != "Test Plugin" {
		t.Errorf("Expected name to be 'Test Plugin', got %s", metadata.Name)
	}
}

func TestPluginMetadataWithOptionalFields(t *testing.T) {
	homepage := "https://example.com"
	repository := "https://github.com/example/plugin"
	metadata := PluginMetadata{
		PluginID:    "test-plugin",
		Name:        "Test Plugin",
		Description: "A test plugin",
		Version:     "1.0.0",
		Author:      "Test Author",
		Category:    "test",
		Homepage:    &homepage,
		Repository:  &repository,
	}

	if metadata.Homepage == nil || *metadata.Homepage != homepage {
		t.Errorf("Expected homepage to be '%s', got %v", homepage, metadata.Homepage)
	}
	if metadata.Repository == nil || *metadata.Repository != repository {
		t.Errorf("Expected repository to be '%s', got %v", repository, metadata.Repository)
	}
}

func TestPluginVersionCreation(t *testing.T) {
	version := PluginVersion{
		Version:      "1.0.0",
		ReleaseNotes: "Initial release",
		ReleasedAt:   time.Now(),
		DownloadURL:  "http://example.com/plugin.zip",
		FileSize:     1024,
	}

	if version.Version != "1.0.0" {
		t.Errorf("Expected version to be '1.0.0', got %s", version.Version)
	}
	if version.FileSize != 1024 {
		t.Errorf("Expected file_size to be 1024, got %d", version.FileSize)
	}
}

func TestPluginReviewCreation(t *testing.T) {
	title := "Great plugin!"
	updatedAt := time.Now()
	review := PluginReview{
		ReviewID:    "review-1",
		PluginID:    "test-plugin",
		UserID:      "user-1",
		UserName:    "Test User",
		Rating:      5,
		Title:       &title,
		Content:     "Excellent plugin!",
		CreatedAt:   time.Now(),
		UpdatedAt:   &updatedAt,
		HelpfulCount: 10,
	}

	if review.Rating != 5 {
		t.Errorf("Expected rating to be 5, got %d", review.Rating)
	}
	if review.HelpfulCount != 10 {
		t.Errorf("Expected helpful_count to be 10, got %d", review.HelpfulCount)
	}
}

func TestPluginSearchCriteria(t *testing.T) {
	query := "web"
	category := "web"
	author := "john"
	minRating := 4
	criteria := PluginSearchCriteria{
		Query:     &query,
		Category:  &category,
		Tags:      []string{"framework", "ui"},
		Author:    &author,
		MinRating: &minRating,
		Page:      1,
		PageSize:  20,
		SortBy:    "rating",
		SortOrder: "asc",
	}

	if criteria.Page != 1 {
		t.Errorf("Expected page to be 1, got %d", criteria.Page)
	}
	if criteria.PageSize != 20 {
		t.Errorf("Expected page_size to be 20, got %d", criteria.PageSize)
	}
}

func TestPluginSearchResult(t *testing.T) {
	result := PluginSearchResult{
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

	if result.Total != 2 {
		t.Errorf("Expected total to be 2, got %d", result.Total)
	}
	if len(result.Plugins) != 2 {
		t.Errorf("Expected 2 plugins, got %d", len(result.Plugins))
	}
}

func TestPluginPublishRequest(t *testing.T) {
	homepage := "https://example.com"
	request := PluginPublishRequest{
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

	if request.Name != "New Plugin" {
		t.Errorf("Expected name to be 'New Plugin', got %s", request.Name)
	}
	if len(request.FileData) != 14 {
		t.Errorf("Expected file_data length to be 14, got %d", len(request.FileData))
	}
}

func TestPluginPublishResult(t *testing.T) {
	pluginID := "new-plugin"
	result := PluginPublishResult{
		Success:  true,
		PluginID: &pluginID,
		Message:  "Plugin published successfully",
		Warnings: []string{"warning-1", "warning-2"},
	}

	if !result.Success {
		t.Errorf("Expected success to be true, got false")
	}
	if result.PluginID == nil || *result.PluginID != pluginID {
		t.Errorf("Expected plugin_id to be '%s', got %v", pluginID, result.PluginID)
	}
	if len(result.Warnings) != 2 {
		t.Errorf("Expected 2 warnings, got %d", len(result.Warnings))
	}
}

func TestEstError(t *testing.T) {
	err := &EstError{
		Code:    "NOT_FOUND",
		Message: "Resource not found",
	}

	if err.Error() != "Resource not found" {
		t.Errorf("Expected error message to be 'Resource not found', got %s", err.Error())
	}
}

func TestEstErrorWithDetails(t *testing.T) {
	details := map[string]interface{}{
		"resource_id": "123",
		"path":        "/api/test",
	}
	err := &EstError{
		Code:    "VALIDATION_ERROR",
		Message: "Validation failed",
		Details: details,
	}

	if err.Code != "VALIDATION_ERROR" {
		t.Errorf("Expected code to be 'VALIDATION_ERROR', got %s", err.Code)
	}
	if err.Details == nil {
		t.Errorf("Expected details to be present")
	}
	if err.Details["resource_id"] != "123" {
		t.Errorf("Expected resource_id to be '123', got %v", err.Details["resource_id"])
	}
}
