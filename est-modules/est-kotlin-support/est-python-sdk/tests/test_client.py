"""
EST Framework Python SDK - 客户端测试
"""

import pytest
from unittest.mock import Mock, patch
from est_sdk import EstClient
from est_sdk.types import EstError
import requests


class TestEstClient:
    
    def test_client_initialization(self):
        client = EstClient(base_url="http://localhost:8080")
        assert client.base_url == "http://localhost:8080"
        assert client.timeout == 30
        assert client.max_retries == 3
    
    def test_client_with_api_key(self):
        client = EstClient(
            base_url="http://localhost:8080",
            api_key="test-api-key"
        )
        assert client.api_key == "test-api-key"
        assert client.session.headers["Authorization"] == "Bearer test-api-key"
    
    def test_client_with_custom_options(self):
        client = EstClient(
            base_url="http://localhost:8080",
            timeout=60,
            max_retries=5
        )
        assert client.timeout == 60
        assert client.max_retries == 5
    
    def test_client_context_manager(self):
        with EstClient(base_url="http://localhost:8080") as client:
            assert client is not None
    
    def test_base_url_trailing_slash(self):
        client = EstClient(base_url="http://localhost:8080/")
        assert client.base_url == "http://localhost:8080"
    
    def test_user_agent_header(self):
        client = EstClient(base_url="http://localhost:8080")
        assert "User-Agent" in client.session.headers
        assert "EST-Python-SDK" in client.session.headers["User-Agent"]
    
    def test_content_type_header(self):
        client = EstClient(base_url="http://localhost:8080")
        assert client.session.headers["Content-Type"] == "application/json"
    
    def test_accept_header(self):
        client = EstClient(base_url="http://localhost:8080")
        assert client.session.headers["Accept"] == "application/json"
    
    @patch('requests.Session.request')
    def test_get_request(self, mock_request):
        mock_response = Mock()
        mock_response.raise_for_status.return_value = None
        mock_response.json.return_value = {"key": "value"}
        mock_request.return_value = mock_response
        
        client = EstClient(base_url="http://localhost:8080")
        response = client.get("/test", params={"foo": "bar"})
        
        mock_request.assert_called_once()
        assert response.json() == {"key": "value"}
    
    @patch('requests.Session.request')
    def test_post_request(self, mock_request):
        mock_response = Mock()
        mock_response.raise_for_status.return_value = None
        mock_response.json.return_value = {"success": True}
        mock_request.return_value = mock_response
        
        client = EstClient(base_url="http://localhost:8080")
        response = client.post("/test", json={"data": "test"})
        
        mock_request.assert_called_once()
        assert response.json() == {"success": True}
    
    @patch('requests.Session.request')
    def test_put_request(self, mock_request):
        mock_response = Mock()
        mock_response.raise_for_status.return_value = None
        mock_response.json.return_value = {"updated": True}
        mock_request.return_value = mock_response
        
        client = EstClient(base_url="http://localhost:8080")
        response = client.put("/test", json={"data": "updated"})
        
        mock_request.assert_called_once()
        assert response.json() == {"updated": True}
    
    @patch('requests.Session.request')
    def test_delete_request(self, mock_request):
        mock_response = Mock()
        mock_response.raise_for_status.return_value = None
        mock_response.json.return_value = {"deleted": True}
        mock_request.return_value = mock_response
        
        client = EstClient(base_url="http://localhost:8080")
        response = client.delete("/test")
        
        mock_request.assert_called_once()
        assert response.json() == {"deleted": True}
    
    @patch('requests.Session.request')
    def test_retry_on_failure(self, mock_request):
        mock_response = Mock()
        mock_response.raise_for_status.side_effect = [
            requests.exceptions.ConnectionError("Connection failed"),
            requests.exceptions.ConnectionError("Connection failed"),
            requests.exceptions.ConnectionError("Connection failed"),
        ]
        mock_request.return_value = mock_response
        
        client = EstClient(base_url="http://localhost:8080", max_retries=2)
        
        with pytest.raises(EstError) as exc_info:
            client.get("/test")
        
        assert mock_request.call_count == 3
        assert "NETWORK_ERROR" in str(exc_info.value)
    
    @patch('requests.Session.request')
    def test_est_error_parsing(self, mock_request):
        mock_response = Mock()
        mock_response.raise_for_status.side_effect = requests.exceptions.HTTPError("404 Not Found")
        mock_response.json.return_value = {
            "code": "NOT_FOUND",
            "message": "Resource not found",
            "details": {"resource_id": "123"}
        }
        type(mock_response).status_code = 404
        mock_request.return_value = mock_response
        
        client = EstClient(base_url="http://localhost:8080", max_retries=0)
        
        with pytest.raises(EstError) as exc_info:
            client.get("/test")
        
        assert exc_info.value.code == "NOT_FOUND"
        assert exc_info.value.message == "Resource not found"
        assert exc_info.value.details == {"resource_id": "123"}
    
    @patch('requests.Session.request')
    def test_network_error(self, mock_request):
        mock_response = Mock()
        mock_response.raise_for_status.side_effect = requests.exceptions.ConnectionError("Connection refused")
        mock_request.return_value = mock_response
        
        client = EstClient(base_url="http://localhost:8080", max_retries=0)
        
        with pytest.raises(EstError) as exc_info:
            client.get("/test")
        
        assert exc_info.value.code == "NETWORK_ERROR"
    
    def test_close_session(self):
        client = EstClient(base_url="http://localhost:8080")
        original_session = client.session
        client.close()
        assert client.session is original_session
