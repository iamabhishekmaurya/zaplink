"use client"

import { TeamManagement } from "@/components/dashboard/team-management"
import { useAuth } from "@/hooks/useAuth"

export default function TeamManagementPage() {
  const { userRole, organizationId, isLoading } = useAuth()

  if (isLoading) {
    return <div className="flex items-center justify-center min-h-screen">Loading...</div>
  }

  return (
    <div className="flex-1 space-y-4 p-8 pt-6">
      <TeamManagement organizationId={organizationId} userRole={userRole} />
    </div>
  )
}
