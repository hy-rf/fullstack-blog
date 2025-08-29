import type Author from "./Author";

export default interface Post {
  id: number;
  content: string;
  createdAt: string;
  author: Author;
  posts: Post[];
  replyCount: number;
}
