import type Author from "./Author";
import type Reply from "./Reply";

export default interface Post {
  id: number;
  title: string;
  content: string;
  createdAt: string;
  updatedAt: string;
  author: Author;
  replies: Reply[];
  replyCount: number;
}
