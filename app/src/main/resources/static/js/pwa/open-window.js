self.openOrFocusWindow = async function openOrFocusWindow(url) {
    const clientList = await clients.matchAll({
        type: 'window',
        includeUncontrolled: true
    });

    let wasFocused = await tryFocusExactMatch(clientList, url);
    if (wasFocused) {
        return
    }

    let wasNavigated = await tryNavigateAnyWindow(clientList, url);
    if (wasNavigated) {
        return
    }

    await clients.openWindow(url);
}

async function tryFocusExactMatch(clientList, url) {
    const exactMatch = clientList.find(c => c.url && c.url.endsWith(url));
    if (!exactMatch) {
        return false;
    }
    return await tryFocusWindow(exactMatch);
}

async function tryNavigateAnyWindow(clientList, url) {
    const anyWindow = clientList.find(c => c.type === 'window');
    if (!anyWindow) {
        return false;
    }

    let focused = await tryFocusWindow(anyWindow);
    if (!focused) {
        return false
    }

    return await tryNavigateTo(anyWindow, url);
}

async function tryFocusWindow(window) {
    try {
        await window.focus();
        return true;
    } catch (e) {
        console.error(`Failed to focus ${window}`, window, e);
        return false
    }
}

async function tryNavigateTo(window, url) {
    try {
        if (typeof window.navigate === 'function') {
            await window.navigate(url);
        } else {
            window.postMessage({action: 'navigate', targetUrl: url}, '*');
        }
        return true
    } catch (e) {
        console.error(`Failed to navigate ${window} to ${url}`, e);
        return false;
    }
}
