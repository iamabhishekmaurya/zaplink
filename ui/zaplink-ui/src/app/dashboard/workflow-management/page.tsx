"use client"

import { WorkflowManagement } from '@/features/dashboard/ui/workflow-management'
import { useAuth } from "@/hooks/useAuth"

export default function WorkflowManagementPage() {
  const { userRole, organizationId, userId, isLoading } = useAuth()

  if (isLoading) {
    return <div className="flex items-center justify-center min-h-screen">Loading...</div>
  }

  return (
    <div className="flex-1 space-y-4 p-8 pt-6">
      <WorkflowManagement
        organizationId={organizationId}
        userRole={userRole}
        userId={Number(userId || 0)}
      />
    </div>
  )
}
