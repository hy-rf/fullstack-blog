import { defineStore } from "pinia";
import type { User } from "~/types/User";

export const useUserStore = defineStore("user", {
  state: () => ({
    user: {} as User,
    loaded: false,
    savedPosts: [] as number[],
    likedPosts: [] as number[],
  }),
  getters: {
    isUser: (state) => state.user.roles.includes("ROLE_user"),
    isAdmin: (state) => state.user.roles.includes("ROLE_admin"),
  },
  actions: {
    async fetchUser() {
      const res = await fetch("/api/me");
      if (!res.ok) {
        this.user = this.getInitialUser();
        this.loaded = true;
        return;
      }
      const data = await res.json();
      const user: User = {
        id: data.id,
        username: data.username,
        roles: data.roles,
      };
      this.user = user;
      this.loaded = true;
    },
    init(user: User) {
      this.user = user;
      this.loaded = true;
    },
    async logout() {
      await fetch("/api/leave");
      this.user = this.getInitialUser();
    },
    getInitialUser() {
      const user: User = {
        username: "Guest",
        roles: [],
      };
      return user;
    },
    loadPreferences() {
      this.loadLikedPosts();
      this.loadSavedPosts();
      this.loadLocale();
    },
    async loadSavedPosts() {
      const savedPosts: string | null = localStorage.getItem("saved-posts");
      if (savedPosts === null) {
        const r: { postId: number; userId: number }[] = await (
          await fetch("/api/saved-posts-summary")
        ).json();
        localStorage.setItem("saved-posts", r.map((e) => e.postId).join(","));
      } else {
        this.savedPosts = savedPosts.split(",").map((e) => parseInt(e));
      }
    },
    async loadLikedPosts() {
      const likedPosts: string | null = localStorage.getItem("liked-posts");
      if (likedPosts === null) {
        const r: { postId: number; userId: number }[] = await (
          await fetch("/api/liked-posts-summary")
        ).json();
        localStorage.setItem("liked-posts", r.map((e) => e.postId).join(","));
      } else {
        this.likedPosts = likedPosts.split(",").map((e) => parseInt(e));
      }
    },
    loadLocale() {},
  },
});
