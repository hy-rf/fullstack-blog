import type Role from "./Role";

export default interface Author {
  id: number;
  username: string;
  email: string;
  roles: Role[];
}
