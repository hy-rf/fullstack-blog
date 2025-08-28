import { defineStore } from "pinia";

export const useHomePostStore = defineStore("homePost", {
  state: () => ({
    sortBy: "createdAt",
    order: "desc",
    page: 1,
    size: 50,
  }),
  getters: {
    queryParams: (state) => ({
      sortBy: state.sortBy,
      order: state.order,
      page: state.page,
      size: state.size,
    }),
  },
  actions: {
    setFromRoute(query: Record<string, any>) {
      this.sortBy = query.sortBy || "createdAt";
      this.order = query.order || "desc";
      this.page = Number(query.page) || 1;
      this.size = Number(query.size) || 50;
    },
  },
});
