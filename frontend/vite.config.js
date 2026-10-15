import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'

// Dev: the browser calls /api/* on the Vite server, which proxies to the API Gateway (no CORS).
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  const target = env.VITE_API_BASE_URL || 'http://localhost:8080'
  return {
    plugins: [react()],
    server: {
      port: 5173,
      proxy: { '/api': { target, changeOrigin: true, rewrite: (p) => p.replace(/^\/api/, '') } },
    },
  }
})
