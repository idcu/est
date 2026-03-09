import { EstClient, PluginMarketplaceClient } from '../src';
import { PluginInfo, PluginReview, SearchOptions, PublishResult, EstClientConfig } from '../src/types';

describe('Benchmark Tests', () => {
  
  test('Client Initialization Benchmark', () => {
    const iterations = 10000;
    const start = performance.now();
    
    for (let i = 0; i < iterations; i++) {
      const config: EstClientConfig = { baseUrl: 'http://localhost:8080' };
      const client = new EstClient(config);
    }
    
    const elapsed = performance.now() - start;
    const avgTime = elapsed / iterations;
    
    console.log('\nClient Initialization Benchmark:');
    console.log(`  Iterations: ${iterations}`);
    console.log(`  Total time: ${elapsed.toFixed(2)}ms`);
    console.log(`  Average: ${avgTime.toFixed(4)}ms per init`);
    
    expect(avgTime).toBeLessThan(0.1);
  });
  
  test('Client Initialization with API Key Benchmark', () => {
    const iterations = 10000;
    const start = performance.now();
    
    for (let i = 0; i < iterations; i++) {
      const config: EstClientConfig = { 
        baseUrl: 'http://localhost:8080',
        apiKey: 'test-api-key'
      };
      const client = new EstClient(config);
    }
    
    const elapsed = performance.now() - start;
    const avgTime = elapsed / iterations;
    
    console.log('\nClient Initialization with API Key Benchmark:');
    console.log(`  Iterations: ${iterations}`);
    console.log(`  Total time: ${elapsed.toFixed(2)}ms`);
    console.log(`  Average: ${avgTime.toFixed(4)}ms per init`);
    
    expect(avgTime).toBeLessThan(0.1);
  });
  
  test('PluginMarketplaceClient Initialization Benchmark', () => {
    const iterations = 5000;
    const config: EstClientConfig = { baseUrl: 'http://localhost:8080' };
    const client = new EstClient(config);
    const start = performance.now();
    
    for (let i = 0; i < iterations; i++) {
      const marketplace = new PluginMarketplaceClient(client);
    }
    
    const elapsed = performance.now() - start;
    const avgTime = elapsed / iterations;
    
    console.log('\nPluginMarketplaceClient Initialization Benchmark:');
    console.log(`  Iterations: ${iterations}`);
    console.log(`  Total time: ${elapsed.toFixed(2)}ms`);
    console.log(`  Average: ${avgTime.toFixed(4)}ms per init`);
    
    expect(avgTime).toBeLessThan(0.05);
  });
  
  test('PluginInfo Creation Benchmark', () => {
    const iterations = 100000;
    const start = performance.now();
    
    for (let i = 0; i < iterations; i++) {
      const plugin: PluginInfo = {
        name: `Test Plugin ${i}`,
        version: '1.0.0',
        description: 'A test plugin',
        author: 'Test Author',
        mainClass: 'com.example.TestPlugin',
        dependencies: [],
        softDependencies: [],
        category: 'test',
        tags: ['test', 'example'],
        license: 'MIT',
        rating: 4.5,
        downloadCount: 1000,
        screenshots: [],
        publishTime: Date.now(),
        lastUpdateTime: Date.now(),
        certified: true,
        compatibleVersions: ['2.3.0', '2.4.0'],
        minFrameworkVersion: '2.3.0'
      };
    }
    
    const elapsed = performance.now() - start;
    const avgTime = elapsed / iterations;
    
    console.log('\nPluginInfo Creation Benchmark:');
    console.log(`  Iterations: ${iterations}`);
    console.log(`  Total time: ${elapsed.toFixed(2)}ms`);
    console.log(`  Average: ${avgTime.toFixed(4)}ms per creation`);
    
    expect(avgTime).toBeLessThan(0.01);
  });
  
  test('PluginReview Creation Benchmark', () => {
    const iterations = 100000;
    const start = performance.now();
    
    for (let i = 0; i < iterations; i++) {
      const review: PluginReview = {
        id: `review-${i}`,
        pluginId: 'plugin-1',
        userId: 'user-1',
        userName: 'Test User',
        rating: 5,
        title: 'Great plugin!',
        content: 'Excellent plugin, works perfectly.',
        createdAt: Date.now(),
        updatedAt: Date.now(),
        helpfulCount: 10
      };
    }
    
    const elapsed = performance.now() - start;
    const avgTime = elapsed / iterations;
    
    console.log('\nPluginReview Creation Benchmark:');
    console.log(`  Iterations: ${iterations}`);
    console.log(`  Total time: ${elapsed.toFixed(2)}ms`);
    console.log(`  Average: ${avgTime.toFixed(4)}ms per creation`);
    
    expect(avgTime).toBeLessThan(0.01);
  });
  
  test('SearchOptions Creation Benchmark', () => {
    const iterations = 100000;
    const start = performance.now();
    
    for (let i = 0; i < iterations; i++) {
      const options: SearchOptions = {
        query: 'web framework',
        category: 'web',
        tags: ['ui', 'framework'],
        page: 2,
        pageSize: 10,
        sortBy: 'rating',
        sortOrder: 'desc'
      };
    }
    
    const elapsed = performance.now() - start;
    const avgTime = elapsed / iterations;
    
    console.log('\nSearchOptions Creation Benchmark:');
    console.log(`  Iterations: ${iterations}`);
    console.log(`  Total time: ${elapsed.toFixed(2)}ms`);
    console.log(`  Average: ${avgTime.toFixed(4)}ms per creation`);
    
    expect(avgTime).toBeLessThan(0.005);
  });
  
  test('PublishResult Creation Benchmark', () => {
    const iterations = 100000;
    const start = performance.now();
    
    for (let i = 0; i < iterations; i++) {
      const result: PublishResult = {
        success: true,
        pluginId: 'new-plugin',
        version: '1.0.0',
        message: 'Plugin published successfully',
        errors: ['error-1', 'error-2']
      };
    }
    
    const elapsed = performance.now() - start;
    const avgTime = elapsed / iterations;
    
    console.log('\nPublishResult Creation Benchmark:');
    console.log(`  Iterations: ${iterations}`);
    console.log(`  Total time: ${elapsed.toFixed(2)}ms`);
    console.log(`  Average: ${avgTime.toFixed(4)}ms per creation`);
    
    expect(avgTime).toBeLessThan(0.005);
  });
  
  test('EstClientConfig Creation Benchmark', () => {
    const iterations = 100000;
    const start = performance.now();
    
    for (let i = 0; i < iterations; i++) {
      const config: EstClientConfig = {
        baseUrl: 'http://localhost:8080',
        apiKey: 'test-api-key',
        timeout: 60000,
        headers: {
          'X-Custom': 'value',
          'Accept': 'application/json'
        }
      };
    }
    
    const elapsed = performance.now() - start;
    const avgTime = elapsed / iterations;
    
    console.log('\nEstClientConfig Creation Benchmark:');
    console.log(`  Iterations: ${iterations}`);
    console.log(`  Total time: ${elapsed.toFixed(2)}ms`);
    console.log(`  Average: ${avgTime.toFixed(4)}ms per creation`);
    
    expect(avgTime).toBeLessThan(0.005);
  });
  
  test('Memory Usage Benchmark', () => {
    const iterations = 10000;
    const clients: EstClient[] = [];
    
    const startMemory = process.memoryUsage().heapUsed;
    
    for (let i = 0; i < iterations; i++) {
      const config: EstClientConfig = { baseUrl: 'http://localhost:8080' };
      clients.push(new EstClient(config));
    }
    
    const endMemory = process.memoryUsage().heapUsed;
    const memoryUsed = endMemory - startMemory;
    const avgMemory = memoryUsed / iterations;
    
    console.log('\nMemory Usage Benchmark:');
    console.log(`  Iterations: ${iterations}`);
    console.log(`  Total memory: ${(memoryUsed / 1024).toFixed(2)}KB`);
    console.log(`  Average: ${avgMemory.toFixed(2)} bytes per client`);
    
    expect(avgMemory).toBeLessThan(1024);
  });
  
  test('Concurrent Operations Benchmark', async () => {
    const iterations = 100;
    const concurrentTasks = 10;
    
    const createClient = async () => {
      const config: EstClientConfig = { baseUrl: 'http://localhost:8080' };
      return new EstClient(config);
    };
    
    const start = performance.now();
    
    const chunks = [];
    for (let i = 0; i < iterations; i += concurrentTasks) {
      const chunk = [];
      for (let j = 0; j < concurrentTasks && i + j < iterations; j++) {
        chunk.push(createClient());
      }
      chunks.push(Promise.all(chunk));
    }
    
    await Promise.all(chunks);
    
    const elapsed = performance.now() - start;
    const avgTime = elapsed / iterations;
    
    console.log('\nConcurrent Operations Benchmark:');
    console.log(`  Iterations: ${iterations}`);
    console.log(`  Concurrent tasks: ${concurrentTasks}`);
    console.log(`  Total time: ${elapsed.toFixed(2)}ms`);
    console.log(`  Average: ${avgTime.toFixed(4)}ms per operation`);
    
    expect(avgTime).toBeLessThan(1.0);
  });
});
