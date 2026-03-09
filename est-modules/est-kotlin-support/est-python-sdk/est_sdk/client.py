import requests
from typing import Optional, Dict, Any
from .types import EstError


class EstClient:
    def __init__(
        self,
        base_url: str,
        api_key: Optional[str] = None,
        timeout: int = 30,
        max_retries: int = 3,
    ):
        self.base_url = base_url.rstrip("/")
        self.api_key = api_key
        self.timeout = timeout
        self.max_retries = max_retries
        self.session = requests.Session()
        
        if api_key:
            self.session.headers["Authorization"] = f"Bearer {api_key}"
        
        self.session.headers.update({
            "Content-Type": "application/json",
            "Accept": "application/json",
            "User-Agent": f"EST-Python-SDK/2.4.0",
        })

    def _request(
        self,
        method: str,
        endpoint: str,
        params: Optional[Dict[str, Any]] = None,
        json: Optional[Dict[str, Any]] = None,
        **kwargs,
    ) -> requests.Response:
        url = f"{self.base_url}{endpoint}"
        retry_count = 0
        
        while retry_count <= self.max_retries:
            try:
                response = self.session.request(
                    method=method,
                    url=url,
                    params=params,
                    json=json,
                    timeout=self.timeout,
                    **kwargs,
                )
                response.raise_for_status()
                return response
            except requests.exceptions.RequestException as e:
                retry_count += 1
                if retry_count > self.max_retries:
                    raise self._wrap_error(e)
        
        raise Exception("Max retries exceeded")

    def _wrap_error(self, exception: requests.exceptions.RequestException) -> Exception:
        if hasattr(exception, "response") and exception.response is not None:
            try:
                error_data = exception.response.json()
                return EstError(
                    code=error_data.get("code", "UNKNOWN_ERROR"),
                    message=error_data.get("message", str(exception)),
                    details=error_data.get("details"),
                )
            except ValueError:
                pass
        
        return EstError(
            code="NETWORK_ERROR",
            message=str(exception),
        )

    def get(self, endpoint: str, params: Optional[Dict[str, Any]] = None, **kwargs) -> requests.Response:
        return self._request("GET", endpoint, params=params, **kwargs)

    def post(self, endpoint: str, json: Optional[Dict[str, Any]] = None, **kwargs) -> requests.Response:
        return self._request("POST", endpoint, json=json, **kwargs)

    def put(self, endpoint: str, json: Optional[Dict[str, Any]] = None, **kwargs) -> requests.Response:
        return self._request("PUT", endpoint, json=json, **kwargs)

    def delete(self, endpoint: str, **kwargs) -> requests.Response:
        return self._request("DELETE", endpoint, **kwargs)

    def close(self):
        self.session.close()

    def __enter__(self):
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.close()
