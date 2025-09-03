import { defineStore } from "pinia";
import type PostSummary from "../types/PostSummary";

export const useHomePostsStore = defineStore("homePost", {
  state: (): { posts: PostSummary[]; offset: number; scroll: number } => ({
    posts: [],
    offset: 50,
    scroll: 0
  }),
  getters: {},
  actions: {
    init(posts: PostSummary[]) {
      this.posts = posts;
      this.offset = 50;
      this.scroll = 0;
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
