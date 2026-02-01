"use client"

import { InfluencerManagement } from '@/features/dashboard/ui/influencer-management'
import { useAuth } from "@/hooks/useAuth"

export default function InfluencerManagementPage() {
  const { userRole, organizationId, userId, isLoading } = useAuth()

  if (isLoading) {
    return <div className="flex items-center justify-center min-h-screen">Loading...</div>
  }

  return (
    <div className="flex-1 space-y-4 p-8 pt-6">
      <InfluencerManagement 
        organizationId={organizationId} 
        userId={userId} 
        userRole={userRole} 
      />
    </div>
  )
}
