"""
EST Framework Python SDK - 类型定义测试
"""

from est_sdk.types import (
    PluginMetadata,
    PluginVersion,
    PluginReview,
    PluginSearchCriteria,
    PluginSearchResult,
    EstError
)
from datetime import datetime


class TestTypes:
    
    def test_plugin_metadata_creation(self):
        metadata = PluginMetadata(
            plugin_id="test-plugin",
            name="Test Plugin",
            description="A test plugin",
            version="1.0.0",
            author="Test Author",
            category="test"
        )
        assert metadata.plugin_id == "test-plugin"
        assert metadata.name == "Test Plugin"
        assert metadata.category == "test"
    
    def test_plugin_version_creation(self):
        version = PluginVersion(
            version="1.0.0",
            release_notes="Initial release",
            released_at=datetime.now(),
            download_url="http://example.com/plugin.zip",
            file_size=1024
        )
        assert version.version == "1.0.0"
        assert version.file_size == 1024
    
    def test_plugin_review_creation(self):
        review = PluginReview(
            review_id="review-1",
            plugin_id="test-plugin",
            user_id="user-1",
            user_name="Test User",
            rating=5,
            content="Great plugin!",
            created_at=datetime.now()
        )
        assert review.rating == 5
        assert review.content == "Great plugin!"
    
    def test_plugin_search_criteria(self):
        criteria = PluginSearchCriteria(
            query="web",
            category="web",
            page=1,
            page_size=20
        )
        assert criteria.query == "web"
        assert criteria.page == 1
        assert criteria.page_size == 20
    
    def test_est_error_creation(self):
        error = EstError(
            code="NOT_FOUND",
            message="Resource not found"
        )
        assert error.code == "NOT_FOUND"
        assert error.message == "Resource not found"
