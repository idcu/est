"""
EST Framework Python SDK - 可观测性API客户端
"""

from typing import Optional, Dict, Any, List
from .client import EstClient
from .types import ObservabilityMetrics, ObservabilityLogs, ObservabilityTraces


class ObservabilityClient:
    """可观测性API客户端"""
    
    def __init__(self, client: EstClient):
        self.client = client
        self.base_path = "/api/observability"
    
    def get_metrics(self, service_name: Optional[str] = None, 
                    metric_name: Optional[str] = None,
                    start_time: Optional[int] = None,
                    end_time: Optional[int] = None) -> List[ObservabilityMetrics]:
        """获取指标数据
        
        Args:
            service_name: 服务名称过滤
            metric_name: 指标名称过滤
            start_time: 开始时间戳（毫秒）
            end_time: 结束时间戳（毫秒）
        
        Returns:
            指标数据列表
        """
        params = {}
        if service_name:
            params["serviceName"] = service_name
        if metric_name:
            params["metricName"] = metric_name
        if start_time:
            params["startTime"] = start_time
        if end_time:
            params["endTime"] = end_time
        
        response = self.client._request("GET", f"{self.base_path}/metrics", params=params)
        return [ObservabilityMetrics(**item) for item in response]
    
    def get_logs(self, service_name: Optional[str] = None,
                 level: Optional[str] = None,
                 start_time: Optional[int] = None,
                 end_time: Optional[int] = None,
                 limit: int = 100) -> List[ObservabilityLogs]:
        """获取日志数据
        
        Args:
            service_name: 服务名称过滤
            level: 日志级别过滤（DEBUG, INFO, WARN, ERROR）
            start_time: 开始时间戳（毫秒）
            end_time: 结束时间戳（毫秒）
            limit: 返回数量限制
        
        Returns:
            日志数据列表
        """
        params = {"limit": limit}
        if service_name:
            params["serviceName"] = service_name
        if level:
            params["level"] = level
        if start_time:
            params["startTime"] = start_time
        if end_time:
            params["endTime"] = end_time
        
        response = self.client._request("GET", f"{self.base_path}/logs", params=params)
        return [ObservabilityLogs(**item) for item in response]
    
    def get_traces(self, service_name: Optional[str] = None,
                   trace_id: Optional[str] = None,
                   start_time: Optional[int] = None,
                   end_time: Optional[int] = None,
                   limit: int = 100) -> List[ObservabilityTraces]:
        """获取追踪数据
        
        Args:
            service_name: 服务名称过滤
            trace_id: 追踪ID过滤
            start_time: 开始时间戳（毫秒）
            end_time: 结束时间戳（毫秒）
            limit: 返回数量限制
        
        Returns:
            追踪数据列表
        """
        params = {"limit": limit}
        if service_name:
            params["serviceName"] = service_name
        if trace_id:
            params["traceId"] = trace_id
        if start_time:
            params["startTime"] = start_time
        if end_time:
            params["endTime"] = end_time
        
        response = self.client._request("GET", f"{self.base_path}/traces", params=params)
        return [ObservabilityTraces(**item) for item in response]
    
    def get_trace_detail(self, trace_id: str) -> ObservabilityTraces:
        """获取追踪详情
        
        Args:
            trace_id: 追踪ID
        
        Returns:
            追踪详情
        """
        response = self.client._request("GET", f"{self.base_path}/traces/{trace_id}")
        return ObservabilityTraces(**response)
    
    def get_service_status(self, service_name: Optional[str] = None) -> Dict[str, Any]:
        """获取服务状态
        
        Args:
            service_name: 服务名称（可选，不传则获取所有服务状态）
        
        Returns:
            服务状态信息
        """
        path = f"{self.base_path}/status"
        if service_name:
            path = f"{path}/{service_name}"
        
        return self.client._request("GET", path)
    
    def get_health_check(self, service_name: Optional[str] = None) -> Dict[str, Any]:
        """获取健康检查结果
        
        Args:
            service_name: 服务名称（可选，不传则获取所有服务健康状态）
        
        Returns:
            健康检查结果
        """
        path = f"{self.base_path}/health"
        if service_name:
            path = f"{path}/{service_name}"
        
        return self.client._request("GET", path)
