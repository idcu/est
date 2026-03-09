#!/usr/bin/env python3
"""
EST Framework Python SDK - 基础使用示例
"""

from est_sdk import EstClient, PluginMarketplaceClient
import sys


def main():
    print("EST Framework Python SDK - 基础使用示例")
    print("=" * 60)
    
    client = EstClient(base_url="http://localhost:8080")
    print(f"✓ EST客户端已创建: {client.base_url}")
    
    marketplace = PluginMarketplaceClient(client)
    print("✓ 插件市场客户端已创建")
    
    print("\n" + "=" * 60)
    print("SDK功能:")
    print("- 插件搜索")
    print("- 插件详情获取")
    print("- 插件版本管理")
    print("- 评论功能")
    print("- 插件发布")
    print("- 插件下载")
    print("\n提示: 请确保EST Framework服务已在 http://localhost:8080 启动")


if __name__ == "__main__":
    main()
