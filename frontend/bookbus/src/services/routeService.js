// services/routeService.js
import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080",
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Match your backend EXACTLY
export const getAllRoutes = () => api.get("/routes");

export const searchRoutes = (from, to, date = null) => {
  const params = { from, to };
  if (date) params.date = date;
  return api.get("/routes/search", { params });
};

export const getPopularRoutes = () => api.get("/routes/popular");
export const getCities = () => api.get("/routes/cities");
export const getOperators = () => api.get("/routes/operators");

export const getCart = () => api.get("/cart");

export const addToCart = (routeId, seats) => 
  api.post(`/cart/${routeId}`, null, { params: { seats } });

export const removeFromCart = (cartItemId) => 
  api.delete(`/cart/${cartItemId}`);

export const clearCart = () => api.delete("/cart");