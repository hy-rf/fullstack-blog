export default interface PostPage {
  id: number;
  content: string;
  createdAt: string;
  authorId: string;
  authorName: string;
  rootPostId: number;
  parentPostId: number;
  postCount: number;
  likeCount: number;
}
