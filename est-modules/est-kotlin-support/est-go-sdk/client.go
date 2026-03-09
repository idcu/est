package est

import (
	"fmt"
	"github.com/go-resty/resty/v2"
	"time"
)

type Client struct {
	baseURL     string
	apiKey      string
	timeout     time.Duration
	maxRetries  int
	restyClient *resty.Client
}

func NewClient(baseURL string) *Client {
	return NewClientWithOptions(baseURL, ClientOptions{})
}

type ClientOptions struct {
	APIKey     string
	Timeout    time.Duration
	MaxRetries int
}

func NewClientWithOptions(baseURL string, options ClientOptions) *Client {
	timeout := options.Timeout
	if timeout == 0 {
		timeout = 30 * time.Second
	}
	
	maxRetries := options.MaxRetries
	if maxRetries == 0 {
		maxRetries = 3
	}
	
	restyClient := resty.New()
	restyClient.SetTimeout(timeout)
	restyClient.SetBaseURL(baseURL)
	restyClient.SetHeader("Content-Type", "application/json")
	restyClient.SetHeader("Accept", "application/json")
	restyClient.SetHeader("User-Agent", "EST-Go-SDK/2.4.0")
	
	if options.APIKey != "" {
		restyClient.SetHeader("Authorization", "Bearer "+options.APIKey)
	}
	
	return &Client{
		baseURL:     baseURL,
		apiKey:      options.APIKey,
		timeout:     timeout,
		maxRetries:  maxRetries,
		restyClient: restyClient,
	}
}

func (c *Client) R() *resty.Request {
	return c.restyClient.R()
}

func (c *Client) Get(endpoint string, result interface{}) error {
	return c.request("GET", endpoint, nil, result)
}

func (c *Client) Post(endpoint string, body interface{}, result interface{}) error {
	return c.request("POST", endpoint, body, result)
}

func (c *Client) Put(endpoint string, body interface{}, result interface{}) error {
	return c.request("PUT", endpoint, body, result)
}

func (c *Client) Delete(endpoint string, result interface{}) error {
	return c.request("DELETE", endpoint, nil, result)
}

func (c *Client) request(method, endpoint string, body interface{}, result interface{}) error {
	var lastErr error
	
	for i := 0; i <= c.maxRetries; i++ {
		req := c.R()
		
		if body != nil {
			req.SetBody(body)
		}
		
		resp, err := req.Execute(method, endpoint)
		if err != nil {
			lastErr = err
			continue
		}
		
		if resp.IsError() {
			estErr := &EstError{}
			if err := resp.Unmarshal(estErr); err != nil {
				lastErr = fmt.Errorf("request failed with status %d: %s", resp.StatusCode(), resp.String())
			} else {
				lastErr = estErr
			}
			continue
		}
		
		if result != nil {
			if err := resp.Unmarshal(result); err != nil {
				return err
			}
		}
		
		return nil
	}
	
	return lastErr
}
