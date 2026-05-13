import axios from "axios";

const API = axios.create({
  baseURL: "/api",
});

API.interceptors.request.use(config => {
  const token = localStorage.getItem("accessToken");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

API.interceptors.response.use(
  res => res,
  async err => {
    const original = err.config;

    const skipRetry =
      original.url?.includes("/auth/") ||
      original.url === "/notifications/dispatch" ||
      original.url === "/notifications/preferences";

    if (
      (err.response?.status === 401 || err.response?.status === 403) &&
      !original._retry &&
      !skipRetry
    ) {
      original._retry = true;

      const refreshToken = localStorage.getItem("refreshToken");
      if (!refreshToken) {
        return Promise.reject(err);
      }

      try {
        const r = await axios.post("/api/auth/refresh", { refreshToken });
        localStorage.setItem("accessToken", r.data.accessToken);
        original.headers.Authorization = `Bearer ${r.data.accessToken}`;
        return API(original);
      } catch {
        return Promise.reject(err);
      }
    }
    return Promise.reject(err);
  }
);

function logout() {
  localStorage.clear();
  window.location.href = "/login";
}

export default API;