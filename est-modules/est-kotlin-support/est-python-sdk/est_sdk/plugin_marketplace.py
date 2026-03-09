from typing import List, Optional
from .client import EstClient
from .types import (
    PluginMetadata,
    PluginVersion,
    PluginReview,
    PluginSearchCriteria,
    PluginSearchResult,
    PluginPublishRequest,
    PluginPublishResult,
)


class PluginMarketplaceClient:
    def __init__(self, client: EstClient):
        self.client = client

    def search_plugins(
        self,
        query: Optional[str] = None,
        category: Optional[str] = None,
        tags: Optional[List[str]] = None,
        author: Optional[str] = None,
        min_rating: Optional[int] = None,
        page: int = 1,
        page_size: int = 20,
        sort_by: str = "relevance",
        sort_order: str = "desc",
    ) -> PluginSearchResult:
        criteria = PluginSearchCriteria(
            query=query,
            category=category,
            tags=tags,
            author=author,
            min_rating=min_rating,
            page=page,
            page_size=page_size,
            sort_by=sort_by,
            sort_order=sort_order,
        )
        
        response = self.client.post(
            "/api/v1/plugins/search",
            json=criteria.model_dump(exclude_none=True),
        )
        
        return PluginSearchResult(**response.json())

    def get_plugin(self, plugin_id: str) -> PluginMetadata:
        response = self.client.get(f"/api/v1/plugins/{plugin_id}")
        return PluginMetadata(**response.json())

    def get_plugin_versions(self, plugin_id: str) -> List[PluginVersion]:
        response = self.client.get(f"/api/v1/plugins/{plugin_id}/versions")
        return [PluginVersion(**v) for v in response.json()]

    def get_reviews(
        self,
        plugin_id: str,
        page: int = 1,
        page_size: int = 20,
    ) -> List[PluginReview]:
        params = {"page": page, "page_size": page_size}
        response = self.client.get(f"/api/v1/plugins/{plugin_id}/reviews", params=params)
        return [PluginReview(**r) for r in response.json()]

    def add_review(
        self,
        plugin_id: str,
        rating: int,
        content: str,
        title: Optional[str] = None,
    ) -> PluginReview:
        data = {
            "plugin_id": plugin_id,
            "rating": rating,
            "content": content,
        }
        if title:
            data["title"] = title
        
        response = self.client.post(f"/api/v1/plugins/{plugin_id}/reviews", json=data)
        return PluginReview(**response.json())

    def publish_plugin(self, request: PluginPublishRequest) -> PluginPublishResult:
        response = self.client.post(
            "/api/v1/plugins/publish",
            json=request.model_dump(exclude={"file_data"}),
        )
        return PluginPublishResult(**response.json())

    def download_plugin(self, plugin_id: str, version: Optional[str] = None) -> bytes:
        endpoint = f"/api/v1/plugins/{plugin_id}/download"
        if version:
            endpoint += f"?version={version}"
        
        response = self.client.get(endpoint)
        return response.content
