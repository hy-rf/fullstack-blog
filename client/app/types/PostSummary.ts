import type Author from "./Author";

export default interface PostSummary {
  id: number;
  content: string;
  createdAt: string;
  author: Author;
  postCount: number;
}
