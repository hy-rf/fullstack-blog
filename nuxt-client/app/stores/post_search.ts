import { defineStore } from "pinia";

export const usePostSearchStore = defineStore("postSearch", {
  state: () => ({
    keyword: "",
    authorName: "",
    createdAfter: "",
    createdBefore: "",
    sortBy: "likeCount",
    order: "desc",
    page: 1,
    size: 10,
  }),
  getters: {
    queryParams: (state) => ({
      keyword: state.keyword || undefined,
      authorName: state.authorName || undefined,
      createdAfter: state.createdAfter || undefined,
      createdBefore: state.createdBefore || undefined,
      sortBy: state.sortBy,
      order: state.order,
      page: state.page,
      size: state.size,
    }),
  },
  actions: {
    setFromRoute(query: Record<string, any>) {
      this.keyword = query.keyword || "";
      this.authorName = query.authorName || "";
      this.createdAfter = query.createdAfter || "";
      this.createdBefore = query.createdBefore || "";
      this.sortBy = query.sortBy || "likeCount";
      this.order = query.order || "desc";
      this.page = Number(query.page) || 1;
      this.size = Number(query.size) || 10;
    },
  },
});
