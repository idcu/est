export interface PluginInfo {
  name: string;
  version: string;
  description: string;
  author: string;
  mainClass: string;
  dependencies: string[];
  softDependencies: string[];
  category: string;
  tags: string[];
  icon?: string;
  homepage?: string;
  repository?: string;
  license: string;
  rating: number;
  downloadCount: number;
  screenshots: string[];
  changelog?: string;
  publishTime: number;
  lastUpdateTime: number;
  certified: boolean;
  compatibleVersions: string[];
  minFrameworkVersion: string;
}

export interface PluginReview {
  id: string;
  pluginId: string;
  userId: string;
  userName: string;
  rating: number;
  title: string;
  content: string;
  createdAt: number;
  updatedAt: number;
  helpfulCount: number;
}

export interface EstClientConfig {
  baseUrl: string;
  apiKey?: string;
  timeout?: number;
  headers?: Record<string, string>;
}

export interface SearchOptions {
  query?: string;
  category?: string;
  tags?: string[];
  page?: number;
  pageSize?: number;
  sortBy?: 'rating' | 'downloads' | 'date';
  sortOrder?: 'asc' | 'desc';
}

export interface PublishResult {
  success: boolean;
  pluginId: string;
  version: string;
  message?: string;
  errors?: string[];
}
