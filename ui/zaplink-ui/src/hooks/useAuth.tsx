"use client"

import { useState, useEffect, createContext, useContext, ReactNode } from "react"
import { useSelector } from "react-redux"
import { RootState } from "@/store"

interface User {
  id: string  // Changed from number to string to match UserInfo
  username: string
  email: string
  firstName?: string
  lastName?: string
}

interface AuthContextType {
  user: User | null
  userRole: string
  organizationId: number
  teamId?: number
  userId: string  // Changed from number to string to match User.id
  isLoading: boolean
  hasPermission: (permission: string) => boolean
  hasRole: (role: string | string[]) => boolean
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

interface PermissionConfig {
  [key: string]: string[]
}

const ROLE_PERMISSIONS: PermissionConfig = {
  ADMIN: [
    "team:invite",
    "team:remove",
    "team:change_role",
    "workflow:submit",
    "workflow:review",
    "workflow:approve",
    "workflow:reject",
    "campaign:create",
    "campaign:manage",
    "billing:manage",
    "settings:manage"
  ],
  EDITOR: [
    "workflow:submit",
    "content:create",
    "content:edit"
  ],
  APPROVER: [
    "workflow:review",
    "workflow:approve",
    "workflow:reject"
  ],
  VIEWER: [
    "content:view",
    "analytics:view"
  ],
  INFLUENCER: [
    "campaign:view_assigned",
    "campaign:update_progress"
  ]
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const authState = useSelector((state: RootState) => state.auth)
  const [userRole, setUserRole] = useState<string>("ADMIN")
  const [organizationId, setOrganizationId] = useState<number>(1)
  const [teamId, setTeamId] = useState<number | undefined>(undefined)

  // Set default role when user changes
  useEffect(() => {
    if (authState.user) {
      setUserRole("ADMIN") // Default to ADMIN for demo
      setOrganizationId(1)
      setTeamId(undefined)
    }
  }, [authState.user])

  const hasPermission = (permission: string): boolean => {
    if (!userRole) return false
    return ROLE_PERMISSIONS[userRole]?.includes(permission) || false
  }

  const hasRole = (role: string | string[]): boolean => {
    if (!userRole) return false
    if (Array.isArray(role)) {
      return role.includes(userRole)
    }
    return userRole === role
  }

  const value: AuthContextType = {
    user: authState.user,
    userRole,
    organizationId,
    teamId,
    userId: authState.user?.id || "", // Changed from 0 to empty string to match string type
    isLoading: authState.isLoading || !authState.isInitialized,
    hasPermission,
    hasRole
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth(): AuthContextType {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider")
  }
  return context
}

// Higher-order component for protecting routes based on role
export function withRoleProtection(
  Component: React.ComponentType<any>,
  allowedRoles: string[]
) {
  return function ProtectedComponent(props: any) {
    const { userRole, isLoading } = useAuth()

    if (isLoading) {
      return <div>Loading...</div>
    }

    if (!allowedRoles.includes(userRole)) {
      return (
        <div className="flex items-center justify-center min-h-screen">
          <div className="text-center">
            <h2 className="text-2xl font-bold text-gray-900">Access Denied</h2>
            <p className="mt-2 text-gray-600">
              You don't have permission to access this page.
            </p>
          </div>
        </div>
      )
    }

    return <Component {...props} />
  }
}

// Hook for checking specific permissions
export function usePermission(permission: string) {
  const { hasPermission } = useAuth()
  return hasPermission(permission)
}

// Hook for checking user role
export function useRole(role: string | string[]) {
  const { hasRole } = useAuth()
  return hasRole(role)
}
