import type PostList from "~/types/PostList";
import type SearchQuery from "~/types/SearchQuery";

export async function fetchPosts(params: SearchQuery) {
  console.log(params.authorName);

  return await $fetch<PostList>(`/api/posts/search`, {
    query: params,
    credentials: "include",
  });
}
