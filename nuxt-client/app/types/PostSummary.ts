export default interface PostSummary {
  id: number;
  content: string;
  createdAt: string;
  authorId: string;
  authorName: string;
  postCount: number;
  likeCount: number;
  saveCount: number;
}
