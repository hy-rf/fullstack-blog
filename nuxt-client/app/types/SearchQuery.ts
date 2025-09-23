export default interface SearchQuery {
  keyword: string;
  authorName: string;
  createdAfter: string;
  createdBefore: string;
  sortBy: string;
  order: string;
  page: number;
  size: number;
}
