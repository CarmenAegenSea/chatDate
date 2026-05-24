// Posts Module - 广场
// 该模块在用户点击"广场"时动态加载，展示帖子列表
(async function init(container, api, currentUser, registerModule) {
    registerModule('posts', async (el) => {
        el.innerHTML = '<div class="game-companion-loading">加载中...</div>';
        try {
            // 获取帖子列表，可选传 userId 获取点赞状态
            const posts = await api('GET', '/posts?userId=' + (currentUser ? currentUser.id : ''));
            el.innerHTML = '<div class="posts-list"></div>';
            const list = el.querySelector('.posts-list');

            if (posts.length === 0) {
                list.innerHTML = '<p style="text-align:center;padding:40px;color:#888">暂无帖子</p>';
                return;
            }

            // 渲染每条帖子卡片
            posts.forEach(post => {
                const card = document.createElement('div');
                card.className = 'post-card';
                card.innerHTML = `
                    <div class="post-header">
                        <span class="post-author">${post.author ? post.author.nickname : '匿名'}</span>
                        <span class="post-time">${new Date(post.createdAt).toLocaleString()}</span>
                    </div>
                    <h3 class="post-title">${escapeHtml(post.title)}</h3>
                    <p class="post-content">${escapeHtml(post.content.substring(0, 200))}${post.content.length > 200 ? '...' : ''}</p>
                    <div class="post-footer">
                        <span>❤ ${post.likeCount || 0}</span>
                        <span>💬 ${post.commentCount || 0}</span>
                        <span>🔖 ${post.bookmarkCount || 0}</span>
                    </div>
                `;
                list.appendChild(card);
            });
        } catch (err) {
            el.innerHTML = '<p style="color:red;text-align:center;padding:40px">' + err.message + '</p>';
        }
    });

    // HTML 转义，防止 XSS
    function escapeHtml(str) {
        if (!str) return '';
        return str.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
    }

})(container, api, currentUser, registerModule);
