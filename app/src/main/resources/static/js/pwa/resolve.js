self.OFFLINE_URL = '/offline.html';

self.resolveRequest = async function resolveRequest(req) {
    console.debug("Fetching " + req)

    const dest = req.destination || '';

    const isAsset = ['script', 'style', 'image', 'font', 'manifest'].includes(dest);

    let res = await fetchOrGetFromCache(req);
    if (!res) {
        res = offlineFor(req);
    }

    if (isAsset && res.ok && res.type === 'basic') {
        let cache = await caches.open(CACHE_NAME)
        try {
            await cache.put(req, res.clone());
        } catch (reason) {
            console.warn("Caching failed: " + reason)
        }
    }

    return res;
}

async function fetchOrGetFromCache(req) {
    try {
        return await fetch(req);
    } catch (e) {
        console.debug("Fetching", req.url, "destination:", req.destination, "mode:", req.mode);
        const cached = await caches.match(req);
        return cached || null;
    }
}

function offlineFor(req) {
    if (isNavRequest(req)) {
        return caches.match(OFFLINE_URL);
    } else {
        return new Response('Offline', {
            status: 503,
            headers: {'Content-Type': 'text/plain; charset=utf-8'}
        });
    }
}

function isNavRequest(req) {
    return req.mode === 'navigate' || req.destination === 'document';
}
