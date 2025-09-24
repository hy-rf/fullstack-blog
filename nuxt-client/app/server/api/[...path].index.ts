import { defineEventHandler, readBody } from "h3";

export default defineEventHandler(async (event) => {
  const runtimeConfig = useRuntimeConfig();
  const path = event.node.req.url?.split("api")[1];

  const token = getCookie(event, "token");
  const refreshToken = getCookie(event, "refresh");

  const backendUrl = new URL(`${runtimeConfig.URL}${path}`);

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

  let body: object;
  if (event.node.req.method != "GET") {
    body = await readBody(event);
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
