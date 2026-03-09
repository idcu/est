"""
EST Framework Python SDK - 微服务治理API客户端
"""

from typing import Optional, Dict, Any, List
from .client import EstClient
from .types import ServiceInfo, CircuitBreakerStatus, RateLimitConfig


class MicroserviceClient:
    """微服务治理API客户端"""
    
    def __init__(self, client: EstClient):
        self.client = client
        self.base_path = "/api/microservice"
    
    def get_services(self, service_name: Optional[str] = None,
                    namespace: Optional[str] = None) -> List[ServiceInfo]:
        """获取服务列表
        
        Args:
            service_name: 服务名称过滤
            namespace: 命名空间过滤
        
        Returns:
            服务信息列表
        """
        params = {}
        if service_name:
            params["serviceName"] = service_name
        if namespace:
            params["namespace"] = namespace
        
        response = self.client._request("GET", f"{self.base_path}/services", params=params)
        return [ServiceInfo(**item) for item in response]
    
    def get_service_detail(self, service_id: str) -> ServiceInfo:
        """获取服务详情
        
        Args:
            service_id: 服务ID
        
        Returns:
            服务详情
        """
        response = self.client._request("GET", f"{self.base_path}/services/{service_id}")
        return ServiceInfo(**response)
    
    def get_circuit_breakers(self, service_name: Optional[str] = None) -> List[CircuitBreakerStatus]:
        """获取熔断器状态
        
        Args:
            service_name: 服务名称过滤
        
        Returns:
            熔断器状态列表
        """
        params = {}
        if service_name:
            params["serviceName"] = service_name
        
        response = self.client._request("GET", f"{self.base_path}/circuit-breakers", params=params)
        return [CircuitBreakerStatus(**item) for item in response]
    
    def reset_circuit_breaker(self, circuit_breaker_id: str) -> Dict[str, Any]:
        """重置熔断器
        
        Args:
            circuit_breaker_id: 熔断器ID
        
        Returns:
            操作结果
        """
        return self.client._request("POST", f"{self.base_path}/circuit-breakers/{circuit_breaker_id}/reset")
    
    def get_rate_limits(self, service_name: Optional[str] = None) -> List[RateLimitConfig]:
        """获取限流配置
        
        Args:
            service_name: 服务名称过滤
        
        Returns:
            限流配置列表
        """
        params = {}
        if service_name:
            params["serviceName"] = service_name
        
        response = self.client._request("GET", f"{self.base_path}/rate-limits", params=params)
        return [RateLimitConfig(**item) for item in response]
    
    def update_rate_limit(self, rate_limit_id: str, config: Dict[str, Any]) -> RateLimitConfig:
        """更新限流配置
        
        Args:
            rate_limit_id: 限流配置ID
            config: 新的配置
        
        Returns:
            更新后的限流配置
        """
        response = self.client._request("PUT", f"{self.base_path}/rate-limits/{rate_limit_id}", json=config)
        return RateLimitConfig(**response)
    
    def get_service_instances(self, service_name: str) -> List[Dict[str, Any]]:
        """获取服务实例列表
        
        Args:
            service_name: 服务名称
        
        Returns:
            服务实例列表
        """
        response = self.client._request("GET", f"{self.base_path}/services/{service_name}/instances")
        return response
    
    def register_service(self, service_info: Dict[str, Any]) -> Dict[str, Any]:
        """注册服务
        
        Args:
            service_info: 服务信息
        
        Returns:
            注册结果
        """
        return self.client._request("POST", f"{self.base_path}/services/register", json=service_info)
    
    def deregister_service(self, service_id: str) -> Dict[str, Any]:
        """注销服务
        
        Args:
            service_id: 服务ID
        
        Returns:
            注销结果
        """
        return self.client._request("DELETE", f"{self.base_path}/services/{service_id}")
    
    def get_config(self, service_name: str, namespace: Optional[str] = None) -> Dict[str, Any]:
        """获取服务配置
        
        Args:
            service_name: 服务名称
            namespace: 命名空间
        
        Returns:
            服务配置
        """
        params = {}
        if namespace:
            params["namespace"] = namespace
        
        return self.client._request("GET", f"{self.base_path}/config/{service_name}", params=params)
    
    def update_config(self, service_name: str, config: Dict[str, Any],
                     namespace: Optional[str] = None) -> Dict[str, Any]:
        """更新服务配置
        
        Args:
            service_name: 服务名称
            config: 配置内容
            namespace: 命名空间
        
        Returns:
            更新结果
        """
        params = {}
        if namespace:
            params["namespace"] = namespace
        
        return self.client._request("PUT", f"{self.base_path}/config/{service_name}", json=config, params=params)
