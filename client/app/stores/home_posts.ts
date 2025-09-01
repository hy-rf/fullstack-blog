import { defineStore } from "pinia";
import type PostSummary from "../types/PostSummary";

export const useHomePostsStore = defineStore("homePost", {
  state: (): { posts: PostSummary[] } => ({ posts: [] }),
  getters: {},
  actions: {
    init(posts: PostSummary[]) {
      this.posts = posts;
    },
    append(newPosts: PostSummary | PostSummary[]) {
      if (Array.isArray(newPosts)) {
        this.posts.push(...newPosts);
      } else {
        this.posts.push(newPosts);
      }
    },
  },
});
