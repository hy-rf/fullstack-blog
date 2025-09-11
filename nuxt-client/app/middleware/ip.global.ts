import { defineNuxtRouteMiddleware, useCookie } from "#app";
import type { User } from "~/types/User";

export default defineNuxtRouteMiddleware(async (to) => {
  if (import.meta.client) {
    const userStore = useUserStore();
    if (
      to.path.startsWith("/me") ||
      to.path.startsWith("/new") ||
      to.path.startsWith("/follow")
    ) {
      if (!userStore.isUser) {
        return navigateTo("/login");
      }
    }
  }
  if (import.meta.server) {
    if (
      to.path.startsWith("/me") ||
      to.path.startsWith("/new") ||
      to.path.startsWith("/follow")
    ) {
      const token = useCookie("token").value;
      if (!token) {
        return navigateTo("/login");
      }
    }

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
