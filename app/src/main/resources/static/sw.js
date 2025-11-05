importScripts('/js/pwa/open-window.js');
importScripts('/js/pwa/resolve.js');

const CACHE_NAME = "trainer-advisor-${APP_VERSION}";

const urlsToCache = [
    '/',
    OFFLINE_URL,
    '/styles/styles-qyoga.css',
    '/vendor/bootstrap/css/bootstrap.css',
    '/vendor/bootstrap/js/bootstrap.bundle.min.js',
    '/vendor/fontawesome-6.5.2/css/all.min.css',
    '/vendor/fontawesome-6.5.2/webfonts/fa-solid-900.woff2',
    '/vendor/fontawesome-6.5.2/webfonts/fa-regular-400.woff2',
    '/vendor/fontawesome-6.5.2/js/fontawesome.min.js',
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
    if (event.request.method !== 'GET') {
        return;
    }

    const resp = resolveRequest(event.request);

    event.respondWith(resp);
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
        await focusOrOpenWindow(targetUrl);
    })());
});
