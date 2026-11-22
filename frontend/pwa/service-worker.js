// EduSecure - Service Worker DEV
// Cache uniquement les ressources publiques de l'interface.

const CACHE_NAME = "edusecure-static-v1";

const STATIC_ASSETS = [
  "/",
  "/index.html",
  "/manifest.json",
  "/icons/icon-192.png",
  "/icons/icon-512.png"
];

// Installation : mise en cache des fichiers statiques.
self.addEventListener("install", (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then((cache) => cache.addAll(STATIC_ASSETS))
      .then(() => self.skipWaiting())
  );
});

// Activation : suppression des anciennes versions du cache.
self.addEventListener("activate", (event) => {
  event.waitUntil(
    caches.keys()
      .then((keys) =>
        Promise.all(
          keys
            .filter((key) => key !== CACHE_NAME)
            .map((key) => caches.delete(key))
        )
      )
      .then(() => self.clients.claim())
  );
});

// Interception : seules les ressources statiques publiques
// explicitement autorisées peuvent provenir du cache.
self.addEventListener("fetch", (event) => {
  const request = event.request;
  const url = new URL(request.url);
if (
  request.method !== "GET" ||
  url.origin !== self.location.origin ||
  !STATIC_ASSETS.includes(url.pathname) ||
  request.headers.has("Authorization")
) {
  return;
}
  event.respondWith(
    caches.match(request).then((cached) => {
      return cached || fetch(request);
    })
  );
});