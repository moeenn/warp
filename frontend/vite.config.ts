import { defineConfig } from "vite"
import react from "@vitejs/plugin-react-swc"
import path from "node:path"
import tailwindcss from "@tailwindcss/vite"
import flowbiteReact from "flowbite-react/plugin/vite";

export default defineConfig({
  server: {
    port: 3000,
  },
  plugins: [react(), tailwindcss(), flowbiteReact()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
})