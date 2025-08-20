import type Role from "./Role"

export default interface Author {
  id: number
  username: string
  email: string
  passwordHash: string
  fullName: string
  isActive: boolean
  emailVerified: boolean
  twoFactorEnabled: boolean
  twoFactorSecret: string
  createdAt: string
  updatedAt: string
  roles: Role[]
}