// File: server/api/posts/search.get.ts
import { defineEventHandler, getQuery } from 'h3'
import { API_BASE_URL } from '~/constants/api_ constants'

export default defineEventHandler(async (event) => {
  const query = getQuery(event);

  // Make server-side fetch
  const res = await $fetch('/posts/search', {
    baseURL: API_BASE_URL,
    params: query
  })

  return res
})
