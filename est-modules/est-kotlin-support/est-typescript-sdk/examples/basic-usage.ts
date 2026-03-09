import { EstClient, PluginMarketplaceClient } from '../src';

console.log('EST Framework TypeScript SDK - 基础使用示例');
console.log('='.repeat(60));

const client = new EstClient({ baseUrl: 'http://localhost:8080' });
console.log(`✓ EST客户端已创建: ${(client as any).config.baseUrl}`);

const marketplace = new PluginMarketplaceClient(client);
console.log('✓ 插件市场客户端已创建');

console.log('\n' + '='.repeat(60));
console.log('SDK功能:');
console.log('- 插件搜索');
console.log('- 插件详情获取');
console.log('- 插件版本管理');
console.log('- 评论功能');
console.log('- 插件安装/卸载');
console.log('- 插件更新');
console.log('\n提示: 请确保EST Framework服务已在 http://localhost:8080 启动');
