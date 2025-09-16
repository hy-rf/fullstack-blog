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
    "@nuxt/eslint",
    "@nuxt/image",
    "@nuxt/icon",
    "@pinia/nuxt",
    "@nuxtjs/i18n",
    "nuxt-gtag",
  ],

  runtimeConfig: {
    URL: process.env.URL,
    public: {
      GATEWAY_URL: process.env.GATEWAY_URL,
      VERSION: process.env.VERSION,
      FILES_PREFIX: process.env.FILES_PREFIX,
    },
  },

  i18n: {
    strategy: "no_prefix",
    langDir: "../app/i18n/locales",
    locales: [
      { code: "en-US", name: "English", file: "en-US.json" },
      { code: "zh-TW", name: "繁體中文", file: "zh-TW.json" },
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
