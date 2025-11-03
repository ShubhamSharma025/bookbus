// services/bookingService.js
import axios from "axios";

// ---------------------------------------------------------------------
// 1. Point directly at the local dev server
// ---------------------------------------------------------------------
const api = axios.create({
  baseURL: "http://localhost:8080",   // <-- the real backend
  withCredentials: true,
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

// ---------------------------------------------------------------------
// 2. End-points stay exactly the same – only the base URL changed
// ---------------------------------------------------------------------
export const getUserBookings = async () => {
  const res = await api.get("/bookings");
  return res.data;
};

export const getBookingById = async (id) => {
  const res = await api.get(`/bookings/${id}`);
  return res.data;
};

export const cancelBooking = async (bookingId) => {
  await api.post(`/bookings/${bookingId}/cancel`);
};