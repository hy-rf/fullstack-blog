import type Author from "./Author"

export default interface Post {
  id: number
  title: string
  content: string
  createdAt: string
  updatedAt: string
  author: Author
}