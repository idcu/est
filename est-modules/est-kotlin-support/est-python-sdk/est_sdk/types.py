from typing import List, Optional, Dict, Any
from datetime import datetime
from pydantic import BaseModel, Field


class PluginMetadata(BaseModel):
    plugin_id: str = Field(..., description="插件ID")
    name: str = Field(..., description="插件名称")
    description: str = Field(..., description="插件描述")
    version: str = Field(..., description="插件版本")
    author: str = Field(..., description="作者")
    category: str = Field(..., description="分类")
    tags: List[str] = Field(default_factory=list, description="标签")
    license: str = Field(default="MIT", description="许可证")
    homepage: Optional[str] = Field(None, description="主页")
    repository: Optional[str] = Field(None, description="代码仓库")


class PluginVersion(BaseModel):
    version: str = Field(..., description="版本号")
    release_notes: str = Field(..., description="发布说明")
    released_at: datetime = Field(..., description="发布时间")
    download_url: str = Field(..., description="下载地址")
    file_size: int = Field(..., description="文件大小（字节）")


class PluginReview(BaseModel):
    review_id: str = Field(..., description="评论ID")
    plugin_id: str = Field(..., description="插件ID")
    user_id: str = Field(..., description="用户ID")
    user_name: str = Field(..., description="用户名")
    rating: int = Field(..., ge=1, le=5, description="评分（1-5）")
    title: Optional[str] = Field(None, description="标题")
    content: str = Field(..., description="内容")
    created_at: datetime = Field(..., description="创建时间")
    updated_at: Optional[datetime] = Field(None, description="更新时间")
    helpful_count: int = Field(default=0, description="有帮助计数")


class PluginSearchCriteria(BaseModel):
    query: Optional[str] = Field(None, description="搜索关键词")
    category: Optional[str] = Field(None, description="分类")
    tags: Optional[List[str]] = Field(None, description="标签")
    author: Optional[str] = Field(None, description="作者")
    min_rating: Optional[int] = Field(None, ge=1, le=5, description="最低评分")
    page: int = Field(default=1, ge=1, description="页码")
    page_size: int = Field(default=20, ge=1, le=100, description="每页数量")
    sort_by: str = Field(default="relevance", description="排序字段")
    sort_order: str = Field(default="desc", description="排序方向")


class PluginSearchResult(BaseModel):
    plugins: List[PluginMetadata] = Field(..., description="插件列表")
    total: int = Field(..., description="总数")
    page: int = Field(..., description="当前页")
    page_size: int = Field(..., description="每页数量")


class PluginPublishRequest(BaseModel):
    name: str = Field(..., description="插件名称")
    description: str = Field(..., description="插件描述")
    version: str = Field(..., description="版本")
    author: str = Field(..., description="作者")
    category: str = Field(..., description="分类")
    tags: List[str] = Field(default_factory=list, description="标签")
    license: str = Field(default="MIT", description="许可证")
    homepage: Optional[str] = Field(None, description="主页")
    repository: Optional[str] = Field(None, description="代码仓库")
    file_data: bytes = Field(..., description="插件文件数据")


class PluginPublishResult(BaseModel):
    success: bool = Field(..., description="是否成功")
    plugin_id: Optional[str] = Field(None, description="插件ID")
    message: str = Field(..., description="消息")
    warnings: List[str] = Field(default_factory=list, description="警告")


class EstError(BaseModel):
    code: str = Field(..., description="错误代码")
    message: str = Field(..., description="错误消息")
    details: Optional[Dict[str, Any]] = Field(None, description="详细信息")


class ObservabilityMetrics(BaseModel):
    metric_id: str = Field(..., description="指标ID")
    service_name: str = Field(..., description="服务名称")
    metric_name: str = Field(..., description="指标名称")
    value: float = Field(..., description="指标值")
    timestamp: int = Field(..., description="时间戳（毫秒）")
    tags: Dict[str, str] = Field(default_factory=dict, description="标签")
    unit: Optional[str] = Field(None, description="单位")


class ObservabilityLogs(BaseModel):
    log_id: str = Field(..., description="日志ID")
    service_name: str = Field(..., description="服务名称")
    level: str = Field(..., description="日志级别")
    message: str = Field(..., description="日志消息")
    timestamp: int = Field(..., description="时间戳（毫秒）")
    trace_id: Optional[str] = Field(None, description="追踪ID")
    span_id: Optional[str] = Field(None, description="跨度ID")
    metadata: Dict[str, Any] = Field(default_factory=dict, description="元数据")


class ObservabilityTraces(BaseModel):
    trace_id: str = Field(..., description="追踪ID")
    service_name: str = Field(..., description="服务名称")
    spans: List[Dict[str, Any]] = Field(default_factory=list, description="跨度列表")
    duration_ms: int = Field(..., description="持续时间（毫秒）")
    start_time: int = Field(..., description="开始时间戳（毫秒）")
    end_time: int = Field(..., description="结束时间戳（毫秒）")
    status: str = Field(..., description="状态")


class ServiceInfo(BaseModel):
    service_id: str = Field(..., description="服务ID")
    service_name: str = Field(..., description="服务名称")
    version: str = Field(..., description="版本")
    namespace: str = Field(default="default", description="命名空间")
    status: str = Field(..., description="状态")
    endpoints: List[str] = Field(default_factory=list, description="端点列表")
    metadata: Dict[str, Any] = Field(default_factory=dict, description="元数据")
    register_time: int = Field(..., description="注册时间戳（毫秒）")
    last_heartbeat: int = Field(..., description="最后心跳时间戳（毫秒）")


class CircuitBreakerStatus(BaseModel):
    circuit_breaker_id: str = Field(..., description="熔断器ID")
    service_name: str = Field(..., description="服务名称")
    state: str = Field(..., description="状态（CLOSED, OPEN, HALF_OPEN）")
    failure_count: int = Field(default=0, description="失败计数")
    success_count: int = Field(default=0, description="成功计数")
    failure_rate: float = Field(default=0.0, description="失败率")
    last_failure_time: Optional[int] = Field(None, description="最后失败时间戳（毫秒）")


class RateLimitConfig(BaseModel):
    rate_limit_id: str = Field(..., description="限流配置ID")
    service_name: str = Field(..., description="服务名称")
    limit_type: str = Field(..., description="限流类型（QPS, CONCURRENCY）")
    limit_value: int = Field(..., description="限流值")
    window_seconds: int = Field(default=60, description="时间窗口（秒）")
    enabled: bool = Field(default=True, description="是否启用")
    metadata: Dict[str, Any] = Field(default_factory=dict, description="元数据")
