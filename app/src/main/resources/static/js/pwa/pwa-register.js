if ('serviceWorker' in navigator) {
    (async () => {
        try {
            const regs = await navigator.serviceWorker.getRegistrations()
            const hasRoot = regs.some(r => r.scope.endsWith('/'))
            if (hasRoot) {
                return;
            }

            const registration = await navigator.serviceWorker.register('/sw.js', {scope: '/'})
            console.log('Service worker registration successful:', registration)
        } catch (error) {
            console.error(`Service worker registration failed: ${error}`)
        }
    })()
} else {
    console.error('Service workers are not supported.')
}
