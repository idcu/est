import { EstClient } from './client';
import { PluginInfo, PluginReview, SearchOptions, PublishResult } from './types';

export class PluginMarketplaceClient {
  private client: EstClient;

  constructor(client: EstClient);
  constructor(baseUrl: string, apiKey?: string);
  constructor(clientOrUrl: EstClient | string, apiKey?: string) {
    if (typeof clientOrUrl === 'string') {
      this.client = new EstClient({ baseUrl: clientOrUrl, apiKey });
    } else {
      this.client = clientOrUrl;
    }
  }

  async getPlugin(pluginId: string): Promise<PluginInfo | null> {
    try {
      return await this.client.get<PluginInfo>(`/api/plugins/${pluginId}`);
    } catch (error) {
      console.error('Failed to get plugin:', error);
      return null;
    }
  }

  async searchPlugins(options: SearchOptions = {}): Promise<PluginInfo[]> {
    const params: Record<string, any> = {};
    if (options.query) params.q = options.query;
    if (options.category) params.category = options.category;
    if (options.tags) params.tags = options.tags.join(',');
    if (options.page !== undefined) params.page = options.page;
    if (options.pageSize !== undefined) params.pageSize = options.pageSize;
    if (options.sortBy) params.sortBy = options.sortBy;
    if (options.sortOrder) params.sortOrder = options.sortOrder;

    return await this.client.get<PluginInfo[]>('/api/plugins/search', { params });
  }

  async getPopularPlugins(limit: number = 10): Promise<PluginInfo[]> {
    return await this.client.get<PluginInfo[]>('/api/plugins/popular', { params: { limit } });
  }

  async getLatestPlugins(limit: number = 10): Promise<PluginInfo[]> {
    return await this.client.get<PluginInfo[]>('/api/plugins/latest', { params: { limit } });
  }

  async getCertifiedPlugins(): Promise<PluginInfo[]> {
    return await this.client.get<PluginInfo[]>('/api/plugins/certified');
  }

  async getCategories(): Promise<string[]> {
    return await this.client.get<string[]>('/api/plugins/categories');
  }

  async getReviews(pluginId: string, page: number = 0, pageSize: number = 20): Promise<PluginReview[]> {
    return await this.client.get<PluginReview[]>(`/api/plugins/${pluginId}/reviews`, { 
      params: { page, pageSize } 
    });
  }

  async addReview(
    pluginId: string,
    userId: string,
    userName: string,
    rating: number,
    title: string,
    content: string
  ): Promise<PluginReview> {
    return await this.client.post<PluginReview>(`/api/plugins/${pluginId}/reviews`, {
      userId,
      userName,
      rating,
      title,
      content
    });
  }

  async getInstalledPlugins(): Promise<PluginInfo[]> {
    return await this.client.get<PluginInfo[]>('/api/plugins/installed');
  }

  async installPlugin(pluginId: string, version?: string): Promise<boolean> {
    const params = version ? { version } : {};
    const result = await this.client.post<PublishResult>(`/api/plugins/${pluginId}/install`, null, { params });
    return result.success;
  }

  async uninstallPlugin(pluginId: string): Promise<boolean> {
    const result = await this.client.post<PublishResult>(`/api/plugins/${pluginId}/uninstall`);
    return result.success;
  }

  async getAvailableUpdates(): Promise<PluginInfo[]> {
    return await this.client.get<PluginInfo[]>('/api/plugins/updates');
  }

  async updatePlugin(pluginId: string): Promise<boolean> {
    const result = await this.client.post<PublishResult>(`/api/plugins/${pluginId}/update`);
    return result.success;
  }
}
