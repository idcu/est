from typing import List, Optional
from packaging import version


def compare_versions(v1: str, v2: str) -> int:
    ver1 = version.parse(v1)
    ver2 = version.parse(v2)
    if ver1 < ver2:
        return -1
    elif ver1 > ver2:
        return 1
    else:
        return 0


def is_version_compatible(current: str, required: str) -> bool:
    return compare_versions(current, required) >= 0


def get_latest_version(versions: List[str]) -> Optional[str]:
    if not versions:
        return None
    return max(versions, key=lambda v: version.parse(v))


def format_file_size(size_bytes: int) -> str:
    for unit in ["B", "KB", "MB", "GB", "TB"]:
        if size_bytes < 1024.0:
            return f"{size_bytes:.2f} {unit}"
        size_bytes /= 1024.0
    return f"{size_bytes:.2f} PB"


def truncate_text(text: str, max_length: int = 100, suffix: str = "...") -> str:
    if len(text) <= max_length:
        return text
    return text[:max_length - len(suffix)] + suffix


def sanitize_plugin_id(name: str) -> str:
    return name.lower().replace(" ", "-").replace("_", "-")
