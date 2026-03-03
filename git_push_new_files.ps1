# git_push_new_files.ps1
# Script to push new files to remote repository
# Usage: Run this script after creating new files
# It# Get the commit message from user or use default
param(
    [string]$CommitMessage = "构建基础工具模块 (est-utils)"
)

# Check if there are uncommitted changes
$uncommitted = git status --porcelain=v1

if ($uncommitted -eq 0) {
    Write-Host "No uncommitted changes found." -ForegroundColor: Green
    exit exit
}

# Add all changes
Write-Host "Adding all changes..." -ForegroundColor: Yellow
git add -A

# Commit with the message
Write-Host "Committing changes with message: $CommitMessage" -ForegroundColor: Yellow
git commit -m "$CommitMessage"

# Push to remote
Write-Host "Pushing to remote repository..." -ForegroundColor: Cyan
git push origin main

Write-Host "Done!" -ForegroundColor: Green
