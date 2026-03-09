import axios, { AxiosInstance, AxiosRequestConfig } from 'axios';
import { EstClientConfig } from './types';

export class EstClient {
  private axios: AxiosInstance;
  private config: EstClientConfig;

  constructor(config: EstClientConfig) {
    this.config = {
      timeout: 30000,
      ...config
    };

    this.axios = axios.create({
      baseURL: this.config.baseUrl,
      timeout: this.config.timeout,
      headers: {
        'Content-Type': 'application/json',
        ...this.config.headers
      }
    });

    if (this.config.apiKey) {
      this.axios.interceptors.request.use((req) => {
        req.headers.Authorization = `Bearer ${this.config.apiKey}`;
        return req;
      });
    }
  }

  async request<T>(config: AxiosRequestConfig): Promise<T> {
    const response = await this.axios.request<T>(config);
    return response.data;
  }

  async get<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return this.request<T>({ method: 'GET', url, ...config });
  }

  async post<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return this.request<T>({ method: 'POST', url, data, ...config });
  }

  async put<T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> {
    return this.request<T>({ method: 'PUT', url, data, ...config });
  }

  async delete<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return this.request<T>({ method: 'DELETE', url, ...config });
  }
}
