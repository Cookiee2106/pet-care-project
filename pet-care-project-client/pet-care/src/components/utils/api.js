import axios from "axios";

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "http://localhost:9193/api/v1",
  timeout: 180000, // 180 seconds (3 minutes) for Render free tier cold start
  headers: {
    "Content-Type": "application/json",
  },
});
