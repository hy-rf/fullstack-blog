import { defineStore } from "pinia";
import type { User } from "~/types/User";

export const useUserStore = defineStore("user", {
  state: () => ({
    user: {} as User,
    loaded: false,
  }),
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
  },
});
