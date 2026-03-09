package main

import (
	"fmt"
	"log"

	est "github.com/idcu/est-sdk-go"
)

func main() {
	fmt.Println("EST Framework Go SDK - 基础使用示例")
	fmt.Println("=")

	client := est.NewClient("http://localhost:8080")
	fmt.Println("✓ EST客户端已创建: http://localhost:8080")

	marketplace := est.NewPluginMarketplaceClient(client)
	fmt.Println("✓ 插件市场客户端已创建")

	fmt.Println("\n" + "=")
	fmt.Println("SDK功能:")
	fmt.Println("- 插件搜索")
	fmt.Println("- 插件详情获取")
	fmt.Println("- 插件版本管理")
	fmt.Println("- 评论功能")
	fmt.Println("- 插件发布")
	fmt.Println("- 插件下载")
	fmt.Println("\n提示: 请确保EST Framework服务已在 http://localhost:8080 启动")
}
