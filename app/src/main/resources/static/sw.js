importScripts('/js/pwa/open-window.js');

const CACHE_NAME = "trainer-advisor-${APP_VERSION}";
const OFFLINE_URL = '/offline.html';

const urlsToCache = [
    '/',
    OFFLINE_URL,
    '/styles/styles-qyoga.css',
    '/vendor/bootstrap/css/bootstrap.css',
    '/vendor/bootstrap/js/bootstrap.bundle.min.js',
    '/img/icon.png',
    '/img/icon-192x192.png',
    '/img/icon-512x512.png',
    '/manifest.json'
];

self.addEventListener('install', (event) => {
    event.waitUntil((async () => {
        const cache = await caches.open(CACHE_NAME)
        await cache.addAll(urlsToCache)
    })())
    self.skipWaiting()
})

self.addEventListener('activate', (event) => {
    event.waitUntil((async () => {
        const names = await caches.keys()
        await Promise.all(names.map(n => n === CACHE_NAME ? null : caches.delete(n)))
        await self.clients.claim()
    })())
})

self.addEventListener('fetch', (event) => {
    const req = event.request
    if (req.method !== 'GET') {
        return
    }

    if (req.mode === 'navigate' || (req.destination === 'document')) {
        event.respondWith((async () => {
            try {
                const fresh = await fetch(req)
                if (fresh && (fresh.ok || fresh.type === 'opaqueredirect' || (fresh.status >= 300 && fresh.status < 400))) {
                    return fresh
                }

                const cached = await caches.match(req)
                if (cached) {
                    return cached
                }
                return await caches.match(OFFLINE_URL)
            } catch (_) {
                const cached = await caches.match(req)
                if (cached) {
                    return cached
                }
                return await caches.match(OFFLINE_URL)
            }
        })())
        return
    }

    event.respondWith((async () => {
        try {
            const fresh = await fetch(req)
            const cache = await caches.open(CACHE_NAME)
            cache.put(req, fresh.clone())
                .catch(() => {
                })
            return fresh
        } catch (_) {
            const cached = await caches.match(req)
            if (cached) {
                return cached
            }
            return new Response('Offline', {status: 503})
        }
    })())
})

self.addEventListener('push', event => {
    console.debug("Got push event: ", event.data?.json())
    let payload = event.data?.json() ?? {};

    const title = payload.title;
    const body = payload.body;
    if (title == null || body == null) {
        return
    }

    const options = {
        body: body,
        icon: '/img/icon-192x192.png',
        data: payload.data || {},
    };

    event.waitUntil(self.registration.showNotification(title, options));
});

self.addEventListener('notificationclick', (event) => {
    event.notification.close();

    const deepLink = event.notification?.data?.deepLink;
    if (deepLink == null) {
        return
    }

    const targetUrl = new URL(deepLink, self.location.origin).href;

    event.waitUntil((async () => {
        await openOrFocusWindow(targetUrl);
    })());
});
