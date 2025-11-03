import axios from "axios";

export const API_URL = "http://localhost:8080"; // Backend auth base URL

const api = axios.create({
  baseURL: API_URL,
  headers: { "Content-Type": "application/json" },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
// Signup
export const signup = async (userData) => {
  const response = await api.post("/auth/signup", userData);
  return response.data;
};

// Login
export const login = async (credentials) => {
  const response = await api.post("/auth/login", credentials);
  if (response.data && response.data.token) {
    localStorage.setItem("token", response.data.token);
  }
  return response.data;
};

// Logout
export const logout = () => {
  localStorage.removeItem("token");
};
