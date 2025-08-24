import type Author from "./Author";
import type Post from "./Post";

export default interface Reply {
  id: number;
  content: string;
  author: Author;
  created: string;
  replies: Reply[];
}
