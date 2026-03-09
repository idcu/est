import { EstClient, PluginMarketplaceClient } from '../src';
import { PluginInfo, PluginReview, SearchOptions, PublishResult, EstClientConfig } from '../src/types';

describe('EstClient', () => {
  test('should initialize with base URL', () => {
    const config: EstClientConfig = { baseUrl: 'http://localhost:8080' };
    const client = new EstClient(config);
    expect(client).toBeDefined();
  });

  test('should initialize with API key', () => {
    const config: EstClientConfig = { 
      baseUrl: 'http://localhost:8080', 
      apiKey: 'test-api-key' 
    };
    const client = new EstClient(config);
    expect(client).toBeDefined();
  });

  test('should initialize with custom timeout', () => {
    const config: EstClientConfig = { 
      baseUrl: 'http://localhost:8080', 
      timeout: 60000 
    };
    const client = new EstClient(config);
    expect(client).toBeDefined();
  });

  test('should initialize with custom headers', () => {
    const config: EstClientConfig = { 
      baseUrl: 'http://localhost:8080', 
      headers: { 
        'X-Custom-Header': 'value',
        'X-Another-Header': 'another-value'
      }
    };
    const client = new EstClient(config);
    expect(client).toBeDefined();
  });

  test('should initialize with all options', () => {
    const config: EstClientConfig = { 
      baseUrl: 'http://localhost:8080', 
      apiKey: 'test-api-key',
      timeout: 60000,
      headers: { 'X-Custom': 'value' }
    };
    const client = new EstClient(config);
    expect(client).toBeDefined();
  });
});

describe('PluginMarketplaceClient', () => {
  test('should initialize with EstClient', () => {
    const client = new EstClient({ baseUrl: 'http://localhost:8080' });
    const marketplaceClient = new PluginMarketplaceClient(client);
    expect(marketplaceClient).toBeDefined();
  });

  test('should initialize with base URL string', () => {
    const marketplaceClient = new PluginMarketplaceClient('http://localhost:8080');
    expect(marketplaceClient).toBeDefined();
  });

  test('should initialize with base URL and API key', () => {
    const marketplaceClient = new PluginMarketplaceClient('http://localhost:8080', 'test-api-key');
    expect(marketplaceClient).toBeDefined();
  });
});

describe('Type Definitions', () => {
  test('should create valid PluginInfo', () => {
    const plugin: PluginInfo = {
      name: 'Test Plugin',
      version: '1.0.0',
      description: 'A test plugin',
      author: 'Test Author',
      mainClass: 'com.example.TestPlugin',
      dependencies: ['dep1', 'dep2'],
      softDependencies: ['soft-dep'],
      category: 'test',
      tags: ['test', 'example'],
      license: 'MIT',
      rating: 4.5,
      downloadCount: 1000,
      screenshots: ['screenshot1.jpg'],
      publishTime: Date.now(),
      lastUpdateTime: Date.now(),
      certified: true,
      compatibleVersions: ['2.3.0', '2.4.0'],
      minFrameworkVersion: '2.3.0'
    };
    expect(plugin.name).toBe('Test Plugin');
    expect(plugin.version).toBe('1.0.0');
    expect(plugin.certified).toBe(true);
  });

  test('should create PluginInfo with optional fields', () => {
    const plugin: PluginInfo = {
      name: 'Test Plugin',
      version: '1.0.0',
      description: 'A test plugin',
      author: 'Test Author',
      mainClass: 'com.example.TestPlugin',
      dependencies: [],
      softDependencies: [],
      category: 'test',
      tags: [],
      license: 'MIT',
      rating: 0,
      downloadCount: 0,
      screenshots: [],
      publishTime: Date.now(),
      lastUpdateTime: Date.now(),
      certified: false,
      compatibleVersions: [],
      minFrameworkVersion: '2.3.0',
      icon: 'icon.png',
      homepage: 'https://example.com',
      repository: 'https://github.com/example/plugin',
      changelog: 'Initial release'
    };
    expect(plugin.icon).toBe('icon.png');
    expect(plugin.homepage).toBe('https://example.com');
  });

  test('should create valid PluginReview', () => {
    const review: PluginReview = {
      id: 'review-1',
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
    expect(review.rating).toBe(5);
    expect(review.helpfulCount).toBe(10);
  });

  test('should create SearchOptions with all fields', () => {
    const options: SearchOptions = {
      query: 'web framework',
      category: 'web',
      tags: ['ui', 'framework'],
      page: 2,
      pageSize: 10,
      sortBy: 'rating',
      sortOrder: 'desc'
    };
    expect(options.query).toBe('web framework');
    expect(options.page).toBe(2);
    expect(options.sortBy).toBe('rating');
  });

  test('should create SearchOptions with minimal fields', () => {
    const options: SearchOptions = {};
    expect(options.query).toBeUndefined();
    expect(options.page).toBeUndefined();
  });

  test('should create PublishResult with success', () => {
    const result: PublishResult = {
      success: true,
      pluginId: 'new-plugin',
      version: '1.0.0',
      message: 'Plugin published successfully'
    };
    expect(result.success).toBe(true);
    expect(result.pluginId).toBe('new-plugin');
  });

  test('should create PublishResult with errors', () => {
    const result: PublishResult = {
      success: false,
      pluginId: 'failed-plugin',
      version: '1.0.0',
      message: 'Validation failed',
      errors: ['Invalid version', 'Missing description']
    };
    expect(result.success).toBe(false);
    expect(result.errors?.length).toBe(2);
  });

  test('should create EstClientConfig with minimal config', () => {
    const config: EstClientConfig = {
      baseUrl: 'http://localhost:8080'
    };
    expect(config.baseUrl).toBe('http://localhost:8080');
  });

  test('should create EstClientConfig with full config', () => {
    const config: EstClientConfig = {
      baseUrl: 'http://localhost:8080',
      apiKey: 'test-api-key',
      timeout: 60000,
      headers: {
        'X-Custom': 'value',
        'Accept': 'application/json'
      }
    };
    expect(config.apiKey).toBe('test-api-key');
    expect(config.timeout).toBe(60000);
    expect(config.headers?.['X-Custom']).toBe('value');
  });
});
