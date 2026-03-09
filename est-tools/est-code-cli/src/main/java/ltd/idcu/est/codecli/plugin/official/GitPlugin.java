package ltd.idcu.est.codecli.plugin.official;

import ltd.idcu.est.codecli.plugin.BaseEstPlugin;
import ltd.idcu.est.codecli.plugin.PluginException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GitPlugin extends BaseEstPlugin {
    
    private static final String PLUGIN_ID = "git-plugin";
    private static final String PLUGIN_NAME = "Git Integration Plugin";
    private static final String PLUGIN_VERSION = "1.0.0";
    private static final String PLUGIN_DESCRIPTION = "Git 版本控制集成插件，提供 Git 状态查看、提交、推送等功能";
    private static final String PLUGIN_AUTHOR = "EST Team";
    
    private Path workDir;
    
    public GitPlugin() {
        addCapability("features", Map.of(
            "status", true,
            "commit", true,
            "push", true,
            "pull", true,
            "branch", true,
            "log", true
        ));
        addCapability("commands", new String[]{"git_status", "git_commit", "git_push", "git_pull", "git_branch", "git_log"});
    }
    
    @Override
    public String getId() {
        return PLUGIN_ID;
    }
    
    @Override
    public String getName() {
        return PLUGIN_NAME;
    }
    
    @Override
    public String getVersion() {
        return PLUGIN_VERSION;
    }
    
    @Override
    public String getDescription() {
        return PLUGIN_DESCRIPTION;
    }
    
    @Override
    public String getAuthor() {
        return PLUGIN_AUTHOR;
    }
    
    @Override
    protected void onInitialize() throws PluginException {
        this.workDir = context.getWorkDir();
        logInfo("Git 插件初始化完成");
        logInfo("工作目录: " + workDir);
        
        if (!isGitRepository()) {
            logWarn("当前目录不是 Git 仓库");
        }
    }
    
    public boolean isGitRepository() {
        Path gitDir = workDir.resolve(".git");
        return Files.exists(gitDir) && Files.isDirectory(gitDir);
    }
    
    public String getStatus() throws PluginException {
        return executeGitCommand("status");
    }
    
    public String getStatusShort() throws PluginException {
        return executeGitCommand("status", "--short");
    }
    
    public String commit(String message) throws PluginException {
        return executeGitCommand("commit", "-m", message);
    }
    
    public String commitAll(String message) throws PluginException {
        executeGitCommand("add", ".");
        return executeGitCommand("commit", "-m", message);
    }
    
    public String add(String filePattern) throws PluginException {
        return executeGitCommand("add", filePattern);
    }
    
    public String push() throws PluginException {
        return executeGitCommand("push");
    }
    
    public String push(String remote, String branch) throws PluginException {
        return executeGitCommand("push", remote, branch);
    }
    
    public String pull() throws PluginException {
        return executeGitCommand("pull");
    }
    
    public String pull(String remote, String branch) throws PluginException {
        return executeGitCommand("pull", remote, branch);
    }
    
    public String getBranch() throws PluginException {
        return executeGitCommand("branch", "--show-current");
    }
    
    public List<String> listBranches() throws PluginException {
        String output = executeGitCommand("branch", "-a");
        List<String> branches = new ArrayList<>();
        for (String line : output.split("\n")) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                branches.add(trimmed);
            }
        }
        return branches;
    }
    
    public String checkout(String branch) throws PluginException {
        return executeGitCommand("checkout", branch);
    }
    
    public String createBranch(String branchName) throws PluginException {
        return executeGitCommand("checkout", "-b", branchName);
    }
    
    public String merge(String branch) throws PluginException {
        return executeGitCommand("merge", branch);
    }
    
    public String getLog(int limit) throws PluginException {
        return executeGitCommand("log", "--oneline", "-n", String.valueOf(limit));
    }
    
    public String getLog(String format, int limit) throws PluginException {
        return executeGitCommand("log", "--pretty=" + format, "-n", String.valueOf(limit));
    }
    
    public String diff() throws PluginException {
        return executeGitCommand("diff");
    }
    
    public String diff(String file) throws PluginException {
        return executeGitCommand("diff", file);
    }
    
    public String diffCached() throws PluginException {
        return executeGitCommand("diff", "--cached");
    }
    
    public String fetch() throws PluginException {
        return executeGitCommand("fetch");
    }
    
    public String fetch(String remote) throws PluginException {
        return executeGitCommand("fetch", remote);
    }
    
    public String stash() throws PluginException {
        return executeGitCommand("stash");
    }
    
    public String stashPop() throws PluginException {
        return executeGitCommand("stash", "pop");
    }
    
    public String stashList() throws PluginException {
        return executeGitCommand("stash", "list");
    }
    
    public String clone(String repositoryUrl) throws PluginException {
        return executeGitCommand("clone", repositoryUrl);
    }
    
    public String init() throws PluginException {
        return executeGitCommand("init");
    }
    
    public String remoteAdd(String name, String url) throws PluginException {
        return executeGitCommand("remote", "add", name, url);
    }
    
    public String remoteList() throws PluginException {
        return executeGitCommand("remote", "-v");
    }
    
    private String executeGitCommand(String... args) throws PluginException {
        List<String> command = new ArrayList<>();
        command.add("git");
        command.addAll(List.of(args));
        
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(workDir.toFile());
        processBuilder.redirectErrorStream(true);
        
        try {
            Process process = processBuilder.start();
            
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            
            int exitCode = process.waitFor();
            
            if (exitCode != 0) {
                throw new PluginException("Git 命令执行失败，退出码: " + exitCode + "\n输出: " + output);
            }
            
            return output.toString().trim();
        } catch (IOException e) {
            throw new PluginException("执行 Git 命令时发生 IO 错误", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PluginException("Git 命令执行被中断", e);
        }
    }
}
