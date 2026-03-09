document.addEventListener('DOMContentLoaded', function() {
    initTabs();
    initLanguageTabs();
    initPluginTabs();
    loadSkills();
    loadTools();
    loadPlugins();
    initChat();
    initSearch();
    initConfig();
    initPluginSearch();
});

function initTabs() {
    const tabs = document.querySelectorAll('.nav-tab');
    const contents = document.querySelectorAll('.tab-content');
    
    tabs.forEach(tab => {
        tab.addEventListener('click', function() {
            const targetTab = this.dataset.tab;
            
            tabs.forEach(t => t.classList.remove('active'));
            contents.forEach(c => c.classList.remove('active'));
            
            this.classList.add('active');
            document.getElementById(targetTab).classList.add('active');
        });
    });
}

function loadSkills() {
    const skillsGrid = document.getElementById('skillsGrid');
    const skills = [
        { name: 'code_review', description: '审查代码质量，提供改进建议，包括EST框架规范符合性、代码质量、性能考虑、安全性等方面' },
        { name: 'refactor', description: '提供代码重构建议，识别代码坏味道，推荐重构模式，提供重构后的代码示例' },
        { name: 'architecture', description: '分析项目架构，评估架构设计，识别架构问题，提供架构改进建议' },
        { name: 'performance_optimization', description: '分析和优化代码性能，识别性能瓶颈，提供内存使用优化建议，推荐EST框架特定优化' },
        { name: 'security_audit', description: '审计代码安全性，识别安全漏洞和风险，包括输入验证、注入防护、XSS/CSRF防护等' }
    ];
    
    skillsGrid.innerHTML = skills.map(skill => `
        <div class="skill-card">
            <h3>${skill.name}</h3>
            <p>${skill.description}</p>
        </div>
    `).join('');
}

function loadTools() {
    const toolsList = document.getElementById('toolsList');
    const tools = [
        { name: 'est_read_file', description: '读取文件内容' },
        { name: 'est_write_file', description: '写入文件内容' },
        { name: 'est_list_dir', description: '列出目录内容' },
        { name: 'est_scaffold', description: '项目脚手架生成' },
        { name: 'est_codegen', description: '代码生成工具' },
        { name: 'est_index_project', description: '项目文件索引' },
        { name: 'est_search', description: '文件搜索' },
        { name: 'est_list_skills', description: '列出EST技能' },
        { name: 'est_list_templates', description: '列出提示模板' },
        { name: 'est_run_tests', description: '运行测试' }
    ];
    
    toolsList.innerHTML = tools.map(tool => `
        <div class="tool-item">
            <h4>${tool.name}</h4>
            <p>${tool.description}</p>
        </div>
    `).join('');
}

function initChat() {
    const sendBtn = document.getElementById('sendBtn');
    const chatInput = document.getElementById('chatInput');
    const chatMessages = document.getElementById('chatMessages');
    
    addMessage('ai', '你好！我是 EST Code CLI AI 助手。有什么我可以帮助你的吗？');
    
    sendBtn.addEventListener('click', sendMessage);
    chatInput.addEventListener('keydown', function(e) {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            sendMessage();
        }
    });
    
    function sendMessage() {
        const message = chatInput.value.trim();
        if (!message) return;
        
        addMessage('user', message);
        chatInput.value = '';
        
        setTimeout(() => {
            addMessage('ai', '收到您的消息！这是一个演示界面，实际的AI对话功能需要后端支持。');
        }, 1000);
    }
    
    function addMessage(type, content) {
        const messageDiv = document.createElement('div');
        messageDiv.className = `chat-message ${type}`;
        messageDiv.innerHTML = `<div class="message-content">${escapeHtml(content)}</div>`;
        chatMessages.appendChild(messageDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }
}

function initSearch() {
    const searchBtn = document.getElementById('searchBtn');
    const searchInput = document.getElementById('searchInput');
    const searchResults = document.getElementById('searchResults');
    
    searchBtn.addEventListener('click', performSearch);
    searchInput.addEventListener('keydown', function(e) {
        if (e.key === 'Enter') {
            performSearch();
        }
    });
    
    function performSearch() {
        const query = searchInput.value.trim();
        if (!query) return;
        
        searchResults.innerHTML = `
            <div style="text-align: center; color: #6c757d; padding: 2rem;">
                <p>搜索: "${escapeHtml(query)}"</p>
                <p style="margin-top: 1rem;">搜索功能需要后端支持，这是一个演示界面。</p>
            </div>
        `;
    }
}

function initConfig() {
    const configForm = document.getElementById('configForm');
    
    configForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const nickname = document.getElementById('nickname').value;
        const workDir = document.getElementById('workDir').value;
        const planningMode = document.getElementById('planningMode').checked;
        const hitlEnabled = document.getElementById('hitlEnabled').checked;
        
        alert('配置保存成功！\n\n' +
              '昵称: ' + (nickname || 'EST') + '\n' +
              '工作目录: ' + (workDir || '当前目录') + '\n' +
              '规划模式: ' + (planningMode ? '启用' : '禁用') + '\n' +
              'HITL安全: ' + (hitlEnabled ? '启用' : '禁用'));
    });
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

const deployConfigs = {
    docker: `FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]`,
    
    kubernetes: `apiVersion: apps/v1
kind: Deployment
metadata:
  name: est-app
spec:
  replicas: 3
  selector:
    matchLabels:
      app: est-app
  template:
    metadata:
      labels:
        app: est-app
    spec:
      containers:
      - name: est-app
        image: est-app:latest
        ports:
        - containerPort: 8080
        resources:
          requests:
            cpu: "250m"
            memory: "256Mi"
          limits:
            cpu: "500m"
            memory: "512Mi"
        livenessProbe:
          httpGet:
            path: /health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
        readinessProbe:
          httpGet:
            path: /ready
            port: 8080
          initialDelaySeconds: 5
          periodSeconds: 5
---
apiVersion: v1
kind: Service
metadata:
  name: est-app-service
spec:
  selector:
    app: est-app
  ports:
  - protocol: TCP
    port: 80
    targetPort: 8080
  type: LoadBalancer`,
    
    aws: `AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31
Description: EST Application Lambda Function

Resources:
  ESTFunction:
    Type: AWS::Serverless::Function
    Properties:
      Handler: ltd.idcu.est.example.EstLambdaHandler::handleRequest
      Runtime: java17
      CodeUri: target/
      MemorySize: 512
      Timeout: 30
      Environment:
        Variables:
          EST_ENV: production
      Events:
        ApiEvent:
          Type: Api
          Properties:
            Path: /{proxy+}
            Method: ANY

Outputs:
  ApiUrl:
    Description: API Gateway endpoint URL
    Value: !Sub "https://\${ServerlessRestApi}.execute-api.\${AWS::Region}.amazonaws.com/Prod/"`,
    
    azure: `{
  "version": "2.0",
  "logging": {
    "applicationInsights": {
      "samplingSettings": {
        "isEnabled": true,
        "excludedTypes": "Request"
      }
    }
  },
  "extensionBundle": {
    "id": "Microsoft.Azure.Functions.ExtensionBundle",
    "version": "[4.*, 5.0.0)"
  }
}`,
    
    alibaba: `ROSTemplateFormatVersion: '2015-09-01'
Transform: 'Aliyun::Serverless-2018-04-03'
Resources:
  est-service:
    Type: 'Aliyun::Serverless::Service'
    Properties:
      Name: est-service
      Description: EST Application Service
    est-function:
      Type: 'Aliyun::Serverless::Function'
      Properties:
        Handler: ltd.idcu.est.example.EstFcHandler::handleRequest
        Runtime: java17
        CodeUri: ./target/
        MemorySize: 512
        Timeout: 30
        EnvironmentVariables:
          EST_ENV: production
        Events:
          httpTrigger:
            Type: HTTP
            Properties:
              AuthType: ANONYMOUS
              Methods: ['GET', 'POST', 'PUT', 'DELETE']`,
    
    google: `functions:
  est-function:
    name: est-function
    runtime: java17
    entry-point: ltd.idcu.est.example.EstCloudFunction
    memory: 512MB
    timeout: 30s
    environment_variables:
      EST_ENV: production
    trigger-http: yes
    max-instances: 10
    min-instances: 0
    labels:
      application: est
      environment: production`
};

function deployTo(platform) {
    const config = deployConfigs[platform];
    if (!config) {
        alert('未找到该平台的部署配置');
        return;
    }
    
    const outputDiv = document.getElementById('deployOutput');
    const outputContent = document.getElementById('outputContent');
    
    outputContent.textContent = config;
    outputDiv.style.display = 'block';
    
    outputDiv.scrollIntoView({ behavior: 'smooth' });
}

function copyOutput() {
    const outputContent = document.getElementById('outputContent');
    const text = outputContent.textContent;
    
    navigator.clipboard.writeText(text).then(() => {
        const originalText = event.target.textContent;
        event.target.textContent = '已复制!';
        event.target.style.background = '#48bb78';
        
        setTimeout(() => {
            event.target.textContent = originalText || '复制';
            event.target.style.background = '';
        }, 2000);
    }).catch(err => {
        console.error('复制失败:', err);
        alert('复制失败，请手动复制');
    });
}

function initLanguageTabs() {
    const langTabs = document.querySelectorAll('.lang-tab');
    const langExamples = document.querySelectorAll('.lang-examples');
    
    if (langTabs.length === 0) return;
    
    langTabs.forEach(tab => {
        tab.addEventListener('click', function() {
            const targetLang = this.dataset.lang;
            
            langTabs.forEach(t => t.classList.remove('active'));
            langExamples.forEach(e => e.classList.remove('active'));
            
            this.classList.add('active');
            document.getElementById(targetLang + '-examples').classList.add('active');
        });
    });
}

const allPlugins = [
    {
        id: 'git-plugin',
        name: 'Git 集成',
        icon: '📦',
        description: 'Git版本控制集成，支持状态查看、提交、推送、拉取、分支管理等功能',
        category: 'integration',
        version: '1.2.0',
        downloads: 15234,
        rating: 4.8,
        author: 'EST Team',
        official: true,
        installed: true,
        hasUpdate: false
    },
    {
        id: 'database-plugin',
        name: '数据库管理',
        icon: '🗄️',
        description: '数据库连接管理、SQL查询执行、表结构查看、数据导出等功能',
        category: 'tools',
        version: '1.1.0',
        downloads: 12456,
        rating: 4.6,
        author: 'EST Team',
        official: true,
        installed: true,
        hasUpdate: true,
        latestVersion: '1.2.0'
    },
    {
        id: 'api-test-plugin',
        name: 'API 测试',
        icon: '🔌',
        description: 'REST API测试工具，支持GET/POST/PUT/DELETE请求，性能测试和断言验证',
        category: 'tools',
        version: '1.0.0',
        downloads: 9876,
        rating: 4.5,
        author: 'EST Team',
        official: true,
        installed: false,
        hasUpdate: false
    },
    {
        id: 'log-analysis-plugin',
        name: '日志分析',
        icon: '📊',
        description: '日志文件解析、级别过滤、关键词搜索、统计分析和日志尾部查看',
        category: 'analytics',
        version: '1.0.5',
        downloads: 8543,
        rating: 4.7,
        author: 'EST Team',
        official: true,
        installed: true,
        hasUpdate: false
    },
    {
        id: 'file-manager-plugin',
        name: '文件管理',
        icon: '📁',
        description: '文件浏览、搜索、复制、移动、删除，目录大小计算等功能',
        category: 'productivity',
        version: '1.1.2',
        downloads: 7654,
        rating: 4.4,
        author: 'EST Team',
        official: true,
        installed: false,
        hasUpdate: false
    },
    {
        id: 'docker-plugin',
        name: 'Docker 管理',
        icon: '🐳',
        description: 'Docker容器管理，支持容器启动、停止、日志查看和镜像管理',
        category: 'integration',
        version: '0.9.0',
        downloads: 5432,
        rating: 4.3,
        author: 'Community',
        official: false,
        installed: false,
        hasUpdate: false
    },
    {
        id: 'redis-plugin',
        name: 'Redis 客户端',
        icon: '⚡',
        description: 'Redis数据库客户端，支持键值操作、过期设置、批量操作等',
        category: 'tools',
        version: '1.0.0',
        downloads: 4321,
        rating: 4.6,
        author: 'Community',
        official: false,
        installed: false,
        hasUpdate: false
    },
    {
        id: 'code-formatter-plugin',
        name: '代码格式化',
        icon: '🎨',
        description: '代码自动格式化，支持Java、JavaScript、Python等多种语言',
        category: 'productivity',
        version: '1.2.1',
        downloads: 6789,
        rating: 4.8,
        author: 'Community',
        official: false,
        installed: true,
        hasUpdate: true,
        latestVersion: '1.3.0'
    }
];

function initPluginTabs() {
    const pluginTabs = document.querySelectorAll('.plugin-tab');
    const pluginViews = document.querySelectorAll('.plugins-view');
    
    if (pluginTabs.length === 0) return;
    
    pluginTabs.forEach(tab => {
        tab.addEventListener('click', function() {
            const targetView = this.dataset.view;
            
            pluginTabs.forEach(t => t.classList.remove('active'));
            pluginViews.forEach(v => v.classList.remove('active'));
            
            this.classList.add('active');
            document.getElementById('plugins' + targetView.charAt(0).toUpperCase() + targetView.slice(1)).classList.add('active');
        });
    });
}

function loadPlugins() {
    renderPlugins(allPlugins, 'pluginsGrid');
    renderPlugins(allPlugins.filter(p => p.installed), 'installedPluginsGrid');
    renderPlugins(allPlugins.filter(p => p.hasUpdate), 'updatesPluginsGrid');
}

function renderPlugins(plugins, containerId) {
    const container = document.getElementById(containerId);
    if (!container) return;
    
    if (plugins.length === 0) {
        container.innerHTML = `
            <div style="text-align: center; color: #718096; padding: 3rem; grid-column: 1 / -1;">
                <p style="font-size: 1.1rem;">暂无插件</p>
            </div>
        `;
        return;
    }
    
    container.innerHTML = plugins.map(plugin => createPluginCard(plugin)).join('');
}

function createPluginCard(plugin) {
    let badgeClass = '';
    let badgeText = '';
    
    if (plugin.official) {
        badgeClass = 'official';
        badgeText = '官方';
    } else if (plugin.installed) {
        badgeClass = 'installed';
        badgeText = '已安装';
    }
    
    if (plugin.hasUpdate) {
        badgeClass = 'update';
        badgeText = '可更新';
    }
    
    let actionButtons = '';
    if (plugin.installed) {
        if (plugin.hasUpdate) {
            actionButtons = `
                <button class="btn btn-warning" onclick="updatePlugin('${plugin.id}')">更新</button>
                <button class="btn btn-secondary" onclick="uninstallPlugin('${plugin.id}')">卸载</button>
            `;
        } else {
            actionButtons = `
                <button class="btn btn-secondary" onclick="uninstallPlugin('${plugin.id}')">卸载</button>
            `;
        }
    } else {
        actionButtons = `
            <button class="btn btn-success" onclick="installPlugin('${plugin.id}')">安装</button>
        `;
    }
    
    return `
        <div class="plugin-card">
            <div class="plugin-card-header">
                <div class="plugin-icon">${plugin.icon}</div>
                ${badgeText ? `<span class="plugin-badge ${badgeClass}">${badgeText}</span>` : ''}
            </div>
            <h3>${plugin.name}</h3>
            <p>${plugin.description}</p>
            <div class="plugin-meta">
                <span class="plugin-meta-item">📦 v${plugin.version}</span>
                <span class="plugin-meta-item">⬇️ ${formatNumber(plugin.downloads)}</span>
                <span class="plugin-meta-item">⭐ ${plugin.rating}</span>
                <span class="plugin-meta-item">👤 ${plugin.author}</span>
            </div>
            <div class="plugin-actions">
                ${actionButtons}
            </div>
        </div>
    `;
}

function formatNumber(num) {
    if (num >= 10000) {
        return (num / 10000).toFixed(1) + 'w';
    } else if (num >= 1000) {
        return (num / 1000).toFixed(1) + 'k';
    }
    return num.toString();
}

function initPluginSearch() {
    const searchBtn = document.getElementById('pluginSearchBtn');
    const searchInput = document.getElementById('pluginSearchInput');
    const categorySelect = document.getElementById('pluginCategory');
    const sortSelect = document.getElementById('pluginSort');
    
    if (!searchBtn) return;
    
    const performSearch = () => {
        const query = searchInput.value.toLowerCase().trim();
        const category = categorySelect.value;
        const sort = sortSelect.value;
        
        let filtered = allPlugins.filter(plugin => {
            const matchesQuery = !query || 
                plugin.name.toLowerCase().includes(query) || 
                plugin.description.toLowerCase().includes(query);
            const matchesCategory = category === 'all' || plugin.category === category;
            return matchesQuery && matchesCategory;
        });
        
        filtered = sortPlugins(filtered, sort);
        renderPlugins(filtered, 'pluginsGrid');
    };
    
    searchBtn.addEventListener('click', performSearch);
    searchInput.addEventListener('keydown', function(e) {
        if (e.key === 'Enter') {
            performSearch();
        }
    });
    categorySelect.addEventListener('change', performSearch);
    sortSelect.addEventListener('change', performSearch);
}

function sortPlugins(plugins, sortBy) {
    const sorted = [...plugins];
    switch (sortBy) {
        case 'popular':
            sorted.sort((a, b) => b.downloads - a.downloads);
            break;
        case 'latest':
            sorted.sort((a, b) => b.version.localeCompare(a.version));
            break;
        case 'rating':
            sorted.sort((a, b) => b.rating - a.rating);
            break;
    }
    return sorted;
}

function installPlugin(pluginId) {
    const plugin = allPlugins.find(p => p.id === pluginId);
    if (!plugin) return;
    
    if (confirm(`确定要安装插件 "${plugin.name}" 吗？`)) {
        plugin.installed = true;
        loadPlugins();
        alert(`插件 "${plugin.name}" 安装成功！`);
    }
}

function uninstallPlugin(pluginId) {
    const plugin = allPlugins.find(p => p.id === pluginId);
    if (!plugin) return;
    
    if (confirm(`确定要卸载插件 "${plugin.name}" 吗？`)) {
        plugin.installed = false;
        plugin.hasUpdate = false;
        loadPlugins();
        alert(`插件 "${plugin.name}" 卸载成功！`);
    }
}

function updatePlugin(pluginId) {
    const plugin = allPlugins.find(p => p.id === pluginId);
    if (!plugin) return;
    
    if (confirm(`确定要更新插件 "${plugin.name}" 到 v${plugin.latestVersion} 吗？`)) {
        plugin.version = plugin.latestVersion;
        plugin.hasUpdate = false;
        loadPlugins();
        alert(`插件 "${plugin.name}" 更新成功！`);
    }
}
