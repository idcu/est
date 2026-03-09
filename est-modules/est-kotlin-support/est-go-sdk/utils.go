package est

import (
	"fmt"
	"strings"
)

func CompareVersions(v1, v2 string) int {
	parts1 := strings.Split(v1, ".")
	parts2 := strings.Split(v2, ".")
	
	maxLen := len(parts1)
	if len(parts2) > maxLen {
		maxLen = len(parts2)
	}
	
	for i := 0; i < maxLen; i++ {
		num1 := 0
		if i < len(parts1) {
			fmt.Sscanf(parts1[i], "%d", &num1)
		}
		
		num2 := 0
		if i < len(parts2) {
			fmt.Sscanf(parts2[i], "%d", &num2)
		}
		
		if num1 < num2 {
			return -1
		} else if num1 > num2 {
			return 1
		}
	}
	
	return 0
}

func IsVersionCompatible(current, required string) bool {
	return CompareVersions(current, required) >= 0
}

func GetLatestVersion(versions []string) string {
	if len(versions) == 0 {
		return ""
	}
	
	latest := versions[0]
	for _, v := range versions[1:] {
		if CompareVersions(v, latest) > 0 {
			latest = v
		}
	}
	return latest
}

func FormatFileSize(sizeBytes int64) string {
	const unit := []string{"B", "KB", "MB", "GB", "TB"}
	size := float64(sizeBytes)
	
	for _, u := range unit {
		if size < 1024.0 {
			return fmt.Sprintf("%.2f %s", size, u)
		}
		size /= 1024.0
	}
	
	return fmt.Sprintf("%.2f PB", size)
}

func TruncateText(text string, maxLength int) string {
	if len(text) <= maxLength {
		return text
	}
	suffix := "..."
	if maxLength <= len(suffix) {
		return text[:maxLength]
	}
	return text[:maxLength-len(suffix)] + suffix
}

func SanitizePluginID(name string) string {
	name = strings.ToLower(name)
	name = strings.ReplaceAll(name, " ", "-")
	name = strings.ReplaceAll(name, "_", "-")
	return name
}
