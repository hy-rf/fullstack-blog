import type PostViewModel from "~/types/PostViewModel";

export async function fetchPosts(params: Record<string, any>) {
  return await $fetch<PostViewModel>("/api/posts/search", {
    query: params,
  });
}
