export default interface PostSummary {
  id: number;
  content: string;
  createdAt: string;
  authorId: string;
  authorName: string;
  postCount: number;
  likeCount: number;
  saveCount: number;
  urls: string;
  imageUrls: string[];
  rootPostId?: number;
  postId?: number;
}
