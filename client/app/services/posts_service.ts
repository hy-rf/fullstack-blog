import type PostListViewModel from "~/types/PostListViewModel";

export async function fetchPosts(params: Record<string, any>) {
  return await $fetch<PostListViewModel>(`/api/posts/search`, {
    query: params,
    credentials: "include",
  });
}
