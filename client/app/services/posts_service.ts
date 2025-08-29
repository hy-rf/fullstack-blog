import type PostList from "~/types/PostList";

export async function fetchPosts(params: Record<string, any>) {
  return await $fetch<PostList>(`/api/posts/search`, {
    query: params,
    credentials: "include",
  });
}
