// 后端 API 基础路径
const API_BASE = '/api';

let currentUser = null;

// --- API 通用请求方法 ---
async function api(method, path, body) {
    const opts = { method, headers: { 'Content-Type': 'application/json' } };
    if (body) opts.body = JSON.stringify(body);
    const res = await fetch(API_BASE + path, opts);
    if (!res.ok) {
        const err = await res.json().catch(() => ({ message: res.statusText }));
        throw new Error(err.message || '请求失败');
    }
    return res.status === 204 ? null : res.json();
}

// --- 登录逻辑 ---
const loginForm = document.getElementById('login-form');
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const phone = document.getElementById('phone').value;
        const password = document.getElementById('password').value;
        try {
            const user = await api('POST', '/auth/login', { phone, password });
            setCurrentUser(user);
        } catch (err) {
            alert(err.message);
        }
    });
}

// 登录成功后保存用户信息，切换到主界面
function setCurrentUser(user) {
    currentUser = user;
    localStorage.setItem('userId', user.id);
    localStorage.setItem('userInfo', JSON.stringify(user));
    document.getElementById('login-section').style.display = 'none';
    document.getElementById('main-section').style.display = 'block';
    document.getElementById('user-info').textContent = '欢迎, ' + user.nickname;
}

// --- 自动登录（从 localStorage 恢复） ---
const savedId = localStorage.getItem('userId');
if (savedId) {
    document.getElementById('login-section').style.display = 'none';
    document.getElementById('main-section').style.display = 'block';
    const info = localStorage.getItem('userInfo');
    if (info) {
        currentUser = JSON.parse(info);
        document.getElementById('user-info').textContent = '欢迎, ' + currentUser.nickname;
    } else {
        currentUser = { id: parseInt(savedId) };
    }
}

// --- 模块加载系统 ---
// 已注册的模块缓存
const modules = {};

// 模块注册函数，由各个模块 JS 调用
function registerModule(name, initFn) {
    modules[name] = initFn;
}

// 点击模块卡片触发加载
document.querySelectorAll('.module-card').forEach(card => {
    card.addEventListener('click', () => {
        const name = card.dataset.module;
        loadModule(name);
    });
});

// 动态加载模块 JS 并渲染
async function loadModule(name) {
    const container = document.getElementById('module-content');
    // 已注册则直接渲染
    if (modules[name]) {
        await modules[name](container);
        return;
    }
    container.innerHTML = '<div class="game-companion-loading">加载中...</div>';
    try {
        const res = await fetch(`/js/${name}.js`);
        if (!res.ok) throw new Error('模块加载失败');
        const code = await res.text();
        // 在沙箱环境中执行模块代码，该代码会调用 registerModule 注册自身
        const fn = new Function('container', 'api', 'currentUser', 'registerModule', code);
        fn(container, api, currentUser, registerModule);
        // 注册完成后渲染
        if (modules[name]) {
            await modules[name](container);
        }
    } catch (err) {
        container.innerHTML = '<p style="color:red;text-align:center;padding:40px">模块加载失败: ' + err.message + '</p>';
    }
}
