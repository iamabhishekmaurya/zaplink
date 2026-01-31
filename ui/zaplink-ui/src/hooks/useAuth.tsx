"use client"

import { useState, useEffect, createContext, useContext, ReactNode } from "react"

interface User {
  id: number
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
  userId: number
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
  const [user, setUser] = useState<User | null>(null)
  const [userRole, setUserRole] = useState<string>("VIEWER")
  const [organizationId, setOrganizationId] = useState<number>(0)
  const [teamId, setTeamId] = useState<number | undefined>(undefined)
  const [userId, setUserId] = useState<number>(0)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    // In a real app, this would parse JWT token from localStorage or cookies
    // For demo purposes, we'll simulate user data
    const initializeAuth = () => {
      try {
        // Get JWT token from localStorage
        const token = localStorage.getItem("auth_token")
        if (token) {
          // Parse JWT token (in real app, use jwt-decode library)
          const payload = parseJWT(token)
          setUser(payload.user)
          setUserRole(payload.role || "VIEWER")
          setOrganizationId(payload.orgId || 1)
          setTeamId(payload.teamId)
          setUserId(payload.user?.id || 1)
        }
      } catch (error) {
        console.error("Failed to initialize auth:", error)
        // Set default values for demo
        const demoUser = {
          id: 1,
          username: "demo_user",
          email: "demo@example.com",
          firstName: "Demo",
          lastName: "User"
        }
        setUser(demoUser)
        setUserRole("ADMIN") // Default to ADMIN for demo
        setOrganizationId(1)
        setUserId(demoUser.id)
      } finally {
        setIsLoading(false)
      }
    }

    initializeAuth()
  }, [])

  const parseJWT = (token: string) => {
    // Simplified JWT parsing for demo
    // In production, use a proper JWT library
    try {
      const base64Url = token.split('.')[1]
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      )
      return JSON.parse(jsonPayload)
    } catch (error) {
      // Return demo payload if parsing fails
      return {
        user: { id: 1, username: "demo_user", email: "demo@example.com" },
        role: "ADMIN",
        orgId: 1,
        teamId: 1
      }
    }
  }

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
    user,
    userRole,
    organizationId,
    teamId,
    userId,
    isLoading,
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
