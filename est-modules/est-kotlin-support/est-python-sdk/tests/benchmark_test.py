"""
EST Framework Python SDK - 性能基准测试
"""

import pytest
import time
from est_sdk import EstClient
from est_sdk.types import PluginMetadata, PluginSearchCriteria
from unittest.mock import Mock, patch


class TestBenchmark:
    
    @pytest.fixture
    def client(self):
        return EstClient(base_url="http://localhost:8080")
    
    def test_client_initialization_benchmark(self, client):
        iterations = 1000
        start_time = time.perf_counter()
        
        for _ in range(iterations):
            _ = EstClient(base_url="http://localhost:8080")
        
        elapsed = time.perf_counter() - start_time
        avg_time = (elapsed / iterations) * 1000
        
        print(f"\nClient initialization benchmark:")
        print(f"  Iterations: {iterations}")
        print(f"  Total time: {elapsed:.4f}s")
        print(f"  Average: {avg_time:.4f}ms per init")
        
        assert avg_time < 1.0, f"Initialization too slow: {avg_time:.4f}ms"
    
    def test_plugin_metadata_creation_benchmark(self):
        iterations = 10000
        start_time = time.perf_counter()
        
        for i in range(iterations):
            _ = PluginMetadata(
                plugin_id=f"plugin-{i}",
                name=f"Test Plugin {i}",
                description=f"A test plugin {i}",
                version="1.0.0",
                author="Test Author",
                category="test"
            )
        
        elapsed = time.perf_counter() - start_time
        avg_time = (elapsed / iterations) * 1000
        
        print(f"\nPluginMetadata creation benchmark:")
        print(f"  Iterations: {iterations}")
        print(f"  Total time: {elapsed:.4f}s")
        print(f"  Average: {avg_time:.4f}ms per creation")
        
        assert avg_time < 0.1, f"PluginMetadata creation too slow: {avg_time:.4f}ms"
    
    def test_search_criteria_creation_benchmark(self):
        iterations = 10000
        start_time = time.perf_counter()
        
        for i in range(iterations):
            _ = PluginSearchCriteria(
                query=f"query-{i}",
                category="test",
                page=1,
                page_size=20
            )
        
        elapsed = time.perf_counter() - start_time
        avg_time = (elapsed / iterations) * 1000
        
        print(f"\nPluginSearchCriteria creation benchmark:")
        print(f"  Iterations: {iterations}")
        print(f"  Total time: {elapsed:.4f}s")
        print(f"  Average: {avg_time:.4f}ms per creation")
        
        assert avg_time < 0.1, f"Search criteria creation too slow: {avg_time:.4f}ms"
    
    @patch('requests.Session.request')
    def test_request_performance_benchmark(self, mock_request, client):
        mock_response = Mock()
        mock_response.raise_for_status.return_value = None
        mock_response.json.return_value = {"key": "value"}
        mock_request.return_value = mock_response
        
        iterations = 1000
        start_time = time.perf_counter()
        
        for _ in range(iterations):
            _ = client.get("/test")
        
        elapsed = time.perf_counter() - start_time
        avg_time = (elapsed / iterations) * 1000
        
        print(f"\nRequest performance benchmark (mocked):")
        print(f"  Iterations: {iterations}")
        print(f"  Total time: {elapsed:.4f}s")
        print(f"  Average: {avg_time:.4f}ms per request")
        
        assert avg_time < 0.5, f"Request handling too slow: {avg_time:.4f}ms"
    
    def test_concurrent_client_operations(self):
        from concurrent.futures import ThreadPoolExecutor, as_completed
        
        def create_client():
            return EstClient(base_url="http://localhost:8080")
        
        iterations = 100
        start_time = time.perf_counter()
        
        with ThreadPoolExecutor(max_workers=10) as executor:
            futures = [executor.submit(create_client) for _ in range(iterations)]
            for future in as_completed(futures):
                _ = future.result()
        
        elapsed = time.perf_counter() - start_time
        avg_time = (elapsed / iterations) * 1000
        
        print(f"\nConcurrent client operations benchmark:")
        print(f"  Iterations: {iterations}")
        print(f"  Total time: {elapsed:.4f}s")
        print(f"  Average: {avg_time:.4f}ms per operation")
        
        assert avg_time < 5.0, f"Concurrent operations too slow: {avg_time:.4f}ms"
    
    def test_memory_usage_benchmark(self):
        import sys
        
        clients = []
        iterations = 1000
        
        start_memory = sys.getsizeof(clients)
        
        for i in range(iterations):
            client = EstClient(base_url="http://localhost:8080")
            clients.append(client)
        
        end_memory = sys.getsizeof(clients)
        for client in clients:
            end_memory += sys.getsizeof(client)
            if hasattr(client, 'session'):
                end_memory += sys.getsizeof(client.session)
        
        avg_memory = (end_memory - start_memory) / iterations
        
        print(f"\nMemory usage benchmark:")
        print(f"  Iterations: {iterations}")
        print(f"  Total memory: {end_memory / 1024:.2f}KB")
        print(f"  Average: {avg_memory:.2f} bytes per client")
        
        assert avg_memory < 1024, f"Memory usage too high: {avg_memory:.2f} bytes"
