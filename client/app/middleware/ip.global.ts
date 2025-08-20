import { defineNuxtRouteMiddleware, useRequestHeaders, useCookie } from "#app";
import type { User } from "~/types/User";

export default defineNuxtRouteMiddleware(async (to, from) => {
  if (import.meta.server) {
    const headers = useRequestHeaders([
      "x-forwarded-for",
      "x-real-ip",
      "cf-connecting-ip",
    ]);

    let ip =
      headers["x-forwarded-for"]?.split(",")[0]?.trim() ||
      headers["x-real-ip"] ||
      headers["cf-connecting-ip"] ||
      "0.0.0.0";

    console.log("Client IP:", ip);

    if (to.path.startsWith("/admin")) {
      const token = useCookie("token").value;

      if (!token) {
        throw createError({
          statusCode: 404,
          statusMessage: `Page not found: ${to.fullPath}`,
        });
      }
      // Call the /api/me endpoint to get user data
      const userRes = await fetch("http://localhost:8080/me", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
        credentials: "include",
      });
      if (!userRes.ok) {
        throw createError({
          statusCode: 404,
          statusMessage: `Page not found: ${to.fullPath}`,
        });
      }
      const user: User = (await userRes.json()) as User;

      console.table(user);
      // Check if the user has the ROLE_admin role
      if (!user.roles.includes("ROLE_admin")) {
        throw createError({
          statusCode: 404,
          statusMessage: `Page not found: ${to.fullPath}`,
        });
      }
      return;
    }
  }
});
