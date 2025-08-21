// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: "2025-05-15",
  devtools: { enabled: true },
  ssr: true,
  sourcemap: {
    server: true,
    client: process.env.NODE_ENV !== "production",
  },
  srcDir: "app/",

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
    "@nuxt/fonts",
    "@nuxt/eslint",
    // "@nuxt/ui",
    "@pinia/nuxt",
    "@nuxtjs/i18n",
    "nuxt-tiptap-editor",
    "nuxt-gtag",
  ],

  runtimeConfig: {
    URL: process.env.URL,
    public: {
      VERSION: process.env.VERSION,
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

  tiptap: {
    prefix: "Tiptap", //prefix for Tiptap imports, composables not included
  },

  gtag: {
    id: "G-E001KP51ZT",
  },
});
