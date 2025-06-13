if ("serviceWorker" in navigator) {
    navigator.serviceWorker.register("/js/pwa/sw.js").then(
        (registration) => {
            console.log("Service worker registration successful:", registration);
        },
        (error) => {
            console.error(`Service worker registration failed: ${error}`);
        },
    );
} else {
    console.error("Service workers are not supported.");
}
