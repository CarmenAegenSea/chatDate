// 游戏陪玩模块（按需加载：new Function 调用，参数已注入）
// 参数: currentUser, container, toast, api

// 获取或创建陪玩账号
async function getOrCreatePlayer() {
    var res = await fetch('/api/player/get-or-create?userId=' + currentUser.id, { method: 'POST' });
    var data = await res.json();
    if (!res.ok) throw new Error(data.message || '请求失败');
    return data;
}

// 获取游戏选项列表
async function getGameOptions() {
    return api('GET', '/api/game-options');
}

// 保存游戏选择
async function saveGames(playerId, games) {
    return api('PUT', '/api/player/' + playerId + '/games', { games: games });
}

// 获取玩家已选游戏
async function getPlayerGames(playerId) {
    return api('GET', '/api/player/' + playerId + '/games');
}

// 启动
(async function() {
    try {
        var player = await getOrCreatePlayer();
        if (!player.gamesSetup) {
            showGameSetup(container, player.id);
        } else {
            showDashboard(container, player);
        }
    } catch (e) {
        container.innerHTML = '<div class="empty">加载失败: ' + e.message + '</div>';
    }
})();

// 游戏选择界面（平铺所有游戏，无二级选项卡）
async function showGameSetup(el, playerId) {
    el.innerHTML = '<div class="loading">加载游戏列表...</div>';
    try {
        var games = await getGameOptions();
        el.innerHTML = '' +
            '<div class="game-setup-banner">首次使用，请选择你喜欢玩的游戏</div>' +
            '<div id="gcGameList" style="margin-bottom:16px;"></div>' +
            '<button class="save-games-btn" id="gcSaveBtn">保存选择</button>';
        var list = document.getElementById('gcGameList');
        games.forEach(function(g) {
            var label = document.createElement('label');
            label.className = 'game-checkbox';
            label.dataset.code = g.code;
            label.innerHTML = '' +
                '<input type="checkbox" value="' + g.code + '">' +
                '<span>' + g.name + '</span>' +
                '<select class="skill-select" style="display:none">' +
                '  <option value="入门">入门</option>' +
                '  <option value="熟悉" selected>熟悉</option>' +
                '  <option value="精通">精通</option>' +
                '  <option value="大神">大神</option>' +
                '</select>';
            label.querySelector('input').addEventListener('change', function() {
                label.classList.toggle('selected');
                var select = label.querySelector('.skill-select');
                select.style.display = label.classList.contains('selected') ? 'inline-block' : 'none';
            });
            list.appendChild(label);
        });
        document.getElementById('gcSaveBtn').addEventListener('click', async function() {
            var items = [];
            document.querySelectorAll('#gameCompanionContent .game-checkbox.selected').forEach(function(label) {
                items.push({
                    gameCode: label.dataset.code,
                    skillLevel: label.querySelector('.skill-select').value
                });
            });
            if (items.length === 0) { toast('请至少选择一个游戏'); return; }
            try {
                await saveGames(playerId, items);
                toast('保存成功！');
                var p = await getOrCreatePlayer();
                el.innerHTML = '';
                showDashboard(el, p);
            } catch (e) { toast('保存失败: ' + e.message); }
        });
    } catch (e) {
        el.innerHTML = '<div class="empty">加载失败: ' + e.message + '</div>';
    }
}

// 陪玩主页
async function showDashboard(el, player) {
    try {
        var games = await getPlayerGames(player.id);
        var allGames = await getGameOptions();
        var gameMap = {};
        allGames.forEach(function(g) { gameMap[g.code] = g.name; });
        var gameTags = games.map(function(g) {
            return '<span class="game-tag">' + (gameMap[g.gameCode] || g.gameCode) + ' (' + (g.skillLevel || '熟悉') + ')</span>';
        }).join('');

        el.innerHTML = '' +
            '<div class="game-dashboard">' +
            '  <div class="info-row"><span class="label">昵称</span><span>' + esc(player.nickname) + '</span></div>' +
            '  <div class="info-row"><span class="label">手机</span><span>' + esc(player.phone) + '</span></div>' +
            '  <div class="info-row"><span class="label">简介</span><span>' + esc(player.bio || '未设置') + '</span></div>' +
            '  <div class="info-row"><span class="label">时薪</span><span>' + (player.hourlyRate ? player.hourlyRate + ' 元' : '未设置') + '</span></div>' +
            '  <div class="info-row"><span class="label">状态</span><span>' + (player.status === 'ONLINE' ? '在线' : '离线') + '</span></div>' +
            '  <div class="info-row"><span class="label">擅长的游戏</span></div>' +
            '  <div class="game-tags">' + gameTags + '</div>' +
            '  <p style="margin-top:12px;font-size:12px;color:#999">创建时间: ' + fmt(player.createdAt) + '</p>' +
            '</div>';
    } catch (e) {
        el.innerHTML = '<div class="empty">加载失败: ' + e.message + '</div>';
    }
}

function esc(s) { var d = document.createElement('div'); d.textContent = s; return d.innerHTML; }
function fmt(t) { return t ? t.replace('T',' ').substring(0,16) : ''; }
