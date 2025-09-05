// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: "2025-05-15",
  devtools: { enabled: true },
  ssr: true,
  sourcemap: {
    server: true,
    client: process.env.NODE_ENV !== "production",
  },
  srcDir: "app",

  routeRules: {
    "/admin/**": { ssr: false },
  },

  vite: {
    server: {
      allowedHosts: ["localhost", "frontend"],
    },
  },
  css: ["normalize.css/normalize.css"],

  modules: [
    "@nuxt/test-utils",
    "@nuxt/image",
    "@nuxt/icon",
    "@pinia/nuxt",
    "@nuxtjs/i18n",
    "nuxt-gtag",
  ],

  runtimeConfig: {
    URL: process.env.URL,
    public: {
      VERSION: process.env.VERSION,
      isDev: process.env.NODE_ENV !== "production",
    },
  },

  i18n: {
    strategy: "no_prefix",
    langDir: "../app/i18n/locales",
    locales: [
      { code: "en-US", name: "English(US)", file: "en-US.json" },
      { code: "jp-JP", name: "日本語", file: "jp-JP.json" },
      { code: "zh-TW", name: "繁體中文", file: "zh-TW.json" },
      { code: "zh-CN", name: "中文", file: "zh-CN.json" },
    ],
    defaultLocale: "en-US",
  },

  icon: {
    mode: "svg",
  },

  gtag: {
    id: "G-E001KP51ZT",
  },
});
