document.addEventListener('DOMContentLoaded', function() {
    initTabs();
    loadSkills();
    loadTools();
    initChat();
    initSearch();
    initConfig();
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
