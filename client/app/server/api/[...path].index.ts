import { defineEventHandler, readBody, getQuery } from "h3";
import { API_BASE_URL } from "~/constants/api_ constants";

export default defineEventHandler(async (event) => {
  const path = event.node.req.url?.split("api")[1];
  let body: any;
  if (event.node.req.method != "GET") {
    body = await readBody(event);
  }
  const token = getCookie(event, "token");
  const refreshToken = getCookie(event, "refresh");

  const backendUrl = new URL(`${API_BASE_URL}${path}`);

  const headers: Record<string, string> = {
    "Content-Type": "application/json",
  };
  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }
  if (refreshToken) {
    headers["X-Refresh-Token"] = `Bearer ${refreshToken}`;
  }

  const options: {
    method: string | undefined;
    headers: Record<string, string>;
    body?: string;
  } = {
    method: event.node.req.method,
    headers,
  };

  if (event.node.req.method != "GET") {
    options.body = JSON.stringify(body);
  }

  try {
    const res = await fetch(backendUrl.toString(), options);
    return res;
  } catch {
    return createError({
      statusCode: 502,
      statusMessage: "Bad Gateway",
      data: { success: false, message: "Upstream service unreachable" },
    });
  }
});
