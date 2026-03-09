"""
EST Framework Python SDK - 插件市场客户端测试
"""

import pytest
from unittest.mock import Mock, patch
from datetime import datetime
from est_sdk import EstClient, PluginMarketplaceClient
from est_sdk.types import (
    PluginMetadata,
    PluginVersion,
    PluginReview,
    PluginSearchCriteria,
    PluginSearchResult,
    PluginPublishRequest,
    PluginPublishResult,
    EstError,
)


class TestPluginMarketplaceClient:
    
    @pytest.fixture
    def client(self):
        return EstClient(base_url="http://localhost:8080")
    
    @pytest.fixture
    def marketplace_client(self, client):
        return PluginMarketplaceClient(client)
    
    @patch('requests.Session.request')
    def test_search_plugins(self, mock_request, marketplace_client):
        mock_response = Mock()
        mock_response.raise_for_status.return_value = None
        mock_response.json.return_value = {
            "plugins": [
                {
                    "plugin_id": "test-plugin-1",
                    "name": "Test Plugin 1",
                    "description": "A test plugin",
                    "version": "1.0.0",
                    "author": "Test Author",
                    "category": "test",
                    "rating": 4.5,
                }
            ],
            "total": 1,
            "page": 1,
            "page_size": 20,
        }
        mock_request.return_value = mock_response
        
        result = marketplace_client.search_plugins(
            query="test",
            category="test",
            page=1,
            page_size=20,
        )
        
        mock_request.assert_called_once()
        assert result.total == 1
        assert len(result.plugins) == 1
        assert result.plugins[0].plugin_id == "test-plugin-1"
    
    @patch('requests.Session.request')
    def test_search_plugins_with_all_options(self, mock_request, marketplace_client):
        mock_response = Mock()
        mock_response.raise_for_status.return_value = None
        mock_response.json.return_value = {
            "plugins": [],
            "total": 0,
            "page": 1,
            "page_size": 20,
        }
        mock_request.return_value = mock_response
        
        result = marketplace_client.search_plugins(
            query="web",
            category="web",
            tags=["framework", "ui"],
            author="john",
            min_rating=4,
            page=2,
            page_size=10,
            sort_by="rating",
            sort_order="asc",
        )
        
        mock_request.assert_called_once()
        assert result.total == 0
    
    @patch('requests.Session.request')
    def test_get_plugin(self, mock_request, marketplace_client):
        mock_response = Mock()
        mock_response.raise_for_status.return_value = None
        mock_response.json.return_value = {
            "plugin_id": "test-plugin",
            "name": "Test Plugin",
            "description": "A test plugin",
            "version": "1.0.0",
            "author": "Test Author",
            "category": "test",
        }
        mock_request.return_value = mock_response
        
        plugin = marketplace_client.get_plugin("test-plugin")
        
        mock_request.assert_called_once()
        assert plugin.plugin_id == "test-plugin"
        assert plugin.name == "Test Plugin"
    
    @patch('requests.Session.request')
    def test_get_plugin_versions(self, mock_request, marketplace_client):
        mock_response = Mock()
        mock_response.raise_for_status.return_value = None
        mock_response.json.return_value = [
            {
                "version": "1.0.0",
                "release_notes": "Initial release",
                "released_at": "2024-01-01T00:00:00",
                "download_url": "http://example.com/plugin.zip",
                "file_size": 1024,
            },
            {
                "version": "1.1.0",
                "release_notes": "Bug fixes",
                "released_at": "2024-02-01T00:00:00",
                "download_url": "http://example.com/plugin-v1.1.0.zip",
                "file_size": 2048,
            },
        ]
        mock_request.return_value = mock_response
        
        versions = marketplace_client.get_plugin_versions("test-plugin")
        
        mock_request.assert_called_once()
        assert len(versions) == 2
        assert versions[0].version == "1.0.0"
        assert versions[1].version == "1.1.0"
    
    @patch('requests.Session.request')
    def test_get_reviews(self, mock_request, marketplace_client):
        mock_response = Mock()
        mock_response.raise_for_status.return_value = None
        mock_response.json.return_value = [
            {
                "review_id": "review-1",
                "plugin_id": "test-plugin",
                "user_id": "user-1",
                "user_name": "Test User",
                "rating": 5,
                "content": "Great plugin!",
                "created_at": "2024-01-01T00:00:00",
            }
        ]
        mock_request.return_value = mock_response
        
        reviews = marketplace_client.get_reviews("test-plugin", page=1, page_size=20)
        
        mock_request.assert_called_once()
        assert len(reviews) == 1
        assert reviews[0].rating == 5
    
    @patch('requests.Session.request')
    def test_add_review(self, mock_request, marketplace_client):
        mock_response = Mock()
        mock_response.raise_for_status.return_value = None
        mock_response.json.return_value = {
            "review_id": "review-2",
            "plugin_id": "test-plugin",
            "user_id": "user-2",
            "user_name": "Another User",
            "rating": 4,
            "content": "Good plugin",
            "created_at": "2024-01-02T00:00:00",
        }
        mock_request.return_value = mock_response
        
        review = marketplace_client.add_review(
            plugin_id="test-plugin",
            rating=4,
            content="Good plugin",
        )
        
        mock_request.assert_called_once()
        assert review.rating == 4
        assert review.content == "Good plugin"
    
    @patch('requests.Session.request')
    def test_add_review_with_title(self, mock_request, marketplace_client):
        mock_response = Mock()
        mock_response.raise_for_status.return_value = None
        mock_response.json.return_value = {
            "review_id": "review-3",
            "plugin_id": "test-plugin",
            "user_id": "user-3",
            "user_name": "User with Title",
            "rating": 5,
            "title": "Excellent!",
            "content": "Best plugin ever",
            "created_at": "2024-01-03T00:00:00",
        }
        mock_request.return_value = mock_response
        
        review = marketplace_client.add_review(
            plugin_id="test-plugin",
            rating=5,
            content="Best plugin ever",
            title="Excellent!",
        )
        
        mock_request.assert_called_once()
        assert review.title == "Excellent!"
    
    @patch('requests.Session.request')
    def test_publish_plugin(self, mock_request, marketplace_client):
        mock_response = Mock()
        mock_response.raise_for_status.return_value = None
        mock_response.json.return_value = {
            "plugin_id": "new-plugin",
            "version": "1.0.0",
            "published_at": "2024-01-01T00:00:00",
            "status": "published",
        }
        mock_request.return_value = mock_response
        
        publish_request = PluginPublishRequest(
            name="New Plugin",
            description="A new plugin",
            version="1.0.0",
            author="Test Author",
            category="test",
            file_data=b"plugin-content",
        )
        
        result = marketplace_client.publish_plugin(publish_request)
        
        mock_request.assert_called_once()
        assert result.plugin_id == "new-plugin"
        assert result.status == "published"
    
    @patch('requests.Session.request')
    def test_download_plugin(self, mock_request, marketplace_client):
        mock_response = Mock()
        mock_response.raise_for_status.return_value = None
        mock_response.content = b"plugin-binary-data"
        mock_request.return_value = mock_response
        
        content = marketplace_client.download_plugin("test-plugin")
        
        mock_request.assert_called_once()
        assert content == b"plugin-binary-data"
    
    @patch('requests.Session.request')
    def test_download_plugin_with_version(self, mock_request, marketplace_client):
        mock_response = Mock()
        mock_response.raise_for_status.return_value = None
        mock_response.content = b"plugin-binary-data-v1.0.0"
        mock_request.return_value = mock_response
        
        content = marketplace_client.download_plugin("test-plugin", version="1.0.0")
        
        mock_request.assert_called_once()
        assert content == b"plugin-binary-data-v1.0.0"
    
    @patch('requests.Session.request')
    def test_search_plugins_error(self, mock_request, marketplace_client):
        mock_response = Mock()
        mock_response.raise_for_status.side_effect = Exception("Server error")
        mock_request.return_value = mock_response
        
        with pytest.raises(Exception):
            marketplace_client.search_plugins(query="test")
    
    @patch('requests.Session.request')
    def test_get_plugin_not_found(self, mock_request, marketplace_client):
        mock_response = Mock()
        mock_response.raise_for_status.side_effect = EstError(
            code="NOT_FOUND",
            message="Plugin not found"
        )
        mock_request.return_value = mock_response
        
        with pytest.raises(EstError):
            marketplace_client.get_plugin("nonexistent-plugin")
