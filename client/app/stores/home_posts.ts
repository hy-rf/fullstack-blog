import { defineStore } from "pinia";
import type PostSummary from "../types/PostSummary";

export const useHomePostsStore = defineStore("homePost", {
  state: (): { posts: PostSummary[]; offset: number; scrollY: number } => ({
    posts: [],
    offset: 50,
    scrollY: 0
  }),
  getters: {},
  actions: {
    append(newPosts: PostSummary | PostSummary[]) {
      if (Array.isArray(newPosts)) {
        this.posts.push(...newPosts);
      } else {
        this.posts.push(newPosts);
      }
    },
  },
});
