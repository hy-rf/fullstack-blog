import { getCookie, readBody } from "h3";
import { API_BASE_URL } from "~/constants/api_ constants";

export default defineEventHandler(async (event) => {
  const body = await readBody(event);
  const token = getCookie(event, "token");
  const backendUrl = `${API_BASE_URL}/login`;

  const headers: Record<string, string> = {
    "Content-Type": "application/json",
  };
  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  const backendRes = await fetch(backendUrl, {
    method: "POST",
    headers,
    body: JSON.stringify(body),
    credentials: "include",
  });

  // Get the Set-Cookie header from the backend response
  const setCookieHeader = backendRes.headers.get("set-cookie");
  const userAgent = event.node.req.headers["user-agent"] || "";
  const isSafari = /Safari/.test(userAgent) && !/Chrome/.test(userAgent);
  const isLocalEnv = process.env.NODE_ENV !== "production";

  if (setCookieHeader) {
    // Check if the client is Safari
    const userAgent = event.node.req.headers["user-agent"] || "";
    const isSafari = /Safari/.test(userAgent) && !/Chrome/.test(userAgent);

    // Check if the environment is local
    const isLocalEnv = process.env.NODE_ENV !== "production";

    if (setCookieHeader && isSafari && isLocalEnv) {
      // Modify the Set-Cookie header and set it in the response
      event.node.res.setHeader(
        "Set-Cookie",
        setCookieHeader
          .replace(/;\s*Secure/gi, "")
          .replace(/;\s*SameSite=[^;]+/gi, "")
          .replace(/;\s*HttpOnly/gi, "")
      );
      return await backendRes.text();
    }
  }

  return backendRes;
});
