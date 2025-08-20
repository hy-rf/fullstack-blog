import { API_BASE_URL } from "~/constants/api_ constants";

export default defineEventHandler(async (event) => {
  const body = await readBody(event);
  const token = getCookie(event, "token");
  const backendUrl = `${API_BASE_URL}/post`;

  const headers: Record<string, string> = {
    "Content-Type": "application/json",
  };
  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  const backendRes = await fetch(backendUrl, {
    method: "POST",
    headers,
    body: JSON.stringify(body)
  });

  return backendRes;
});
