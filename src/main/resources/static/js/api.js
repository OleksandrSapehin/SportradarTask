let API = localStorage.getItem("apiUrl") || "http://localhost:8080/api";

export function getApiUrl() {
    return API;
}

export function setApiUrl(newUrl) {
    API = newUrl;
    localStorage.setItem("apiUrl", API);
    return API;
}

export async function fetchJSON(url, options = {}) {
    const response = await fetch(`${getApiUrl()}${url}`, options);
    if (!response.ok) {
        const text = await response.text();
        throw new Error(text);
    }
    const contentType = response.headers.get("content-type");
    if (contentType && contentType.includes("application/json")) {
        return await response.json();
    }
    return null;
}