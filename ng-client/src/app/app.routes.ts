import { Routes } from "@angular/router";
import { Home } from "@app/features/home/home";
import { Login } from "@app/features/login/login";

export const routes: Routes = [
  { path: "", component: Home },
  { path: "login", component: Login },
];
