package est

import "fmt"

type PluginMarketplaceClient struct {
	client *Client
}

func NewPluginMarketplaceClient(client *Client) *PluginMarketplaceClient {
	return &PluginMarketplaceClient{
		client: client,
	}
}

func (p *PluginMarketplaceClient) SearchPlugins(criteria PluginSearchCriteria) (*PluginSearchResult, error) {
	result := &PluginSearchResult{}
	err := p.client.Post("/api/v1/plugins/search", criteria, result)
	if err != nil {
		return nil, err
	}
	return result, nil
}

func (p *PluginMarketplaceClient) GetPlugin(pluginID string) (*PluginMetadata, error) {
	result := &PluginMetadata{}
	err := p.client.Get("/api/v1/plugins/"+pluginID, result)
	if err != nil {
		return nil, err
	}
	return result, nil
}

func (p *PluginMarketplaceClient) GetPluginVersions(pluginID string) ([]PluginVersion, error) {
	var result []PluginVersion
	err := p.client.Get("/api/v1/plugins/"+pluginID+"/versions", &result)
	if err != nil {
		return nil, err
	}
	return result, nil
}

func (p *PluginMarketplaceClient) GetReviews(pluginID string, page, pageSize int) ([]PluginReview, error) {
	var result []PluginReview
	req := p.client.R()
	if page > 0 {
		req.SetQueryParam("page", fmt.Sprintf("%d", page))
	}
	if pageSize > 0 {
		req.SetQueryParam("page_size", fmt.Sprintf("%d", pageSize))
	}
	
	resp, err := req.Get("/api/v1/plugins/" + pluginID + "/reviews")
	if err != nil {
		return nil, err
	}
	
	if resp.IsError() {
		return nil, fmt.Errorf("failed to get reviews: %s", resp.Status())
	}
	
	if err := resp.Unmarshal(&result); err != nil {
		return nil, err
	}
	
	return result, nil
}

func (p *PluginMarketplaceClient) AddReview(pluginID string, rating int, content string, title *string) (*PluginReview, error) {
	body := map[string]interface{}{
		"plugin_id": pluginID,
		"rating":    rating,
		"content":   content,
	}
	if title != nil {
		body["title"] = *title
	}
	
	result := &PluginReview{}
	err := p.client.Post("/api/v1/plugins/"+pluginID+"/reviews", body, result)
	if err != nil {
		return nil, err
	}
	return result, nil
}

func (p *PluginMarketplaceClient) PublishPlugin(request PluginPublishRequest) (*PluginPublishResult, error) {
	result := &PluginPublishResult{}
	err := p.client.Post("/api/v1/plugins/publish", request, result)
	if err != nil {
		return nil, err
	}
	return result, nil
}

func (p *PluginMarketplaceClient) DownloadPlugin(pluginID string, version *string) ([]byte, error) {
	endpoint := "/api/v1/plugins/" + pluginID + "/download"
	if version != nil {
		endpoint += "?version=" + *version
	}
	
	resp, err := p.client.R().Get(endpoint)
	if err != nil {
		return nil, err
	}
	
	if resp.IsError() {
		return nil, fmt.Errorf("failed to download plugin: %s", resp.Status())
	}
	
	return resp.Body(), nil
}
