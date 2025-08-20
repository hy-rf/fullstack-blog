import { defineEventHandler } from "h3";
import { API_BASE_URL } from "~/constants/api_ constants";

export default defineEventHandler(async (event) => {
  const id = event.context.params?.id;
  if (!id) {
    return { error: "Missing reply id" };
  }

  // Make server-side fetch
  const res = await $fetch(`/reply/${id}/replies`, {
    baseURL: API_BASE_URL,
  });

  return res;
});
