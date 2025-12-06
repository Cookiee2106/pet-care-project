import axios from "axios";

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:9193/api/v1",
  timeout: 120000, // 120 seconds for Render free tier cold start
  headers: {
    "Content-Type": "application/json",
  },
});
