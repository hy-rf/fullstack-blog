import type PostList from "~/types/PostList";
import type SearchQuery from "~/types/SearchQuery";

export async function fetchPosts(params: SearchQuery) {
  const url = import.meta.server
    ? `${useRuntimeConfig().URL}/posts/search`
    : `${useRuntimeConfig().public.GATEWAY_URL}/posts/search`;
  return await $fetch<PostList>(url, {
    query: params,
    credentials: "include",
  });
}
