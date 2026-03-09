package est

import "time"

type PluginMetadata struct {
	PluginID    string   `json:"plugin_id"`
	Name      string   `json:"name"`
	Description string  `json:"description"`
	Version    string   `json:"version"`
	Author     string   `json:"author"`
	Category  string   `json:"category"`
	Tags      []string `json:"tags"`
	License    string   `json:"license"`
	Homepage   *string  `json:"homepage,omitempty"`
	Repository *string  `json:"repository,omitempty"`
}

type PluginVersion struct {
	Version      string    `json:"version"`
	ReleaseNotes string    `json:"release_notes"`
	ReleasedAt   time.Time `json:"released_at"`
	DownloadURL  string    `json:"download_url"`
	FileSize     int64     `json:"file_size"`
}

type PluginReview struct {
	ReviewID    string     `json:"review_id"`
	PluginID    string     `json:"plugin_id"`
	UserID      string     `json:"user_id"`
	UserName    string     `json:"user_name"`
	Rating      int        `json:"rating"`
	Title       *string    `json:"title,omitempty"`
	Content     string     `json:"content"`
	CreatedAt   time.Time  `json:"created_at"`
	UpdatedAt   *time.Time `json:"updated_at,omitempty"`
	HelpfulCount int        `json:"helpful_count"`
}

type PluginSearchCriteria struct {
	Query      *string  `json:"query,omitempty"`
	Category   *string  `json:"category,omitempty"`
	Tags       []string `json:"tags,omitempty"`
	Author      *string  `json:"author,omitempty"`
	MinRating  *int     `json:"min_rating,omitempty"`
	Page       int      `json:"page"`
	PageSize    int      `json:"page_size"`
	SortBy      string   `json:"sort_by"`
	SortOrder   string   `json:"sort_order"`
}

type PluginSearchResult struct {
	Plugins  []PluginMetadata `json:"plugins"`
	Total  int              `json:"total"`
	Page   int              `json:"page"`
	PageSize int             `json:"page_size"`
}

type PluginPublishRequest struct {
	Name        string   `json:"name"`
	Description string   `json:"description"`
	Version     string   `json:"version"`
	Author      string   `json:"author"`
	Category    string   `json:"category"`
	Tags        []string `json:"tags"`
	License     string   `json:"license"`
	Homepage    *string  `json:"homepage,omitempty"`
	Repository  *string  `json:"repository,omitempty"`
	FileData    []byte   `json:"-"`
}

type PluginPublishResult struct {
	Success  bool     `json:"success"`
	PluginID *string  `json:"plugin_id,omitempty"`
	Message  string   `json:"message"`
	Warnings []string `json:"warnings"`
}

type EstError struct {
	Code    string                 `json:"code"`
	Message string                 `json:"message"`
	Details map[string]interface{} `json:"details,omitempty"`
}

func (e *EstError) Error() string {
	return e.Message
}
