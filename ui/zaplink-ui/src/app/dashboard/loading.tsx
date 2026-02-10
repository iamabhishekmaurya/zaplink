import { Metadata } from 'next'

export const metadata: Metadata = {
  title: 'Loading Dashboard | Zaplink',
  description: 'Loading your dashboard...',
}

export default function DashboardLoading() {
  return (
    <div className="flex h-[calc(100vh-4rem)] items-center justify-center">
      <div className="text-center space-y-4">
        <div className="flex items-center justify-center">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
        </div>
        <p className="text-muted-foreground">Loading dashboard...</p>
      </div>
    </div>
  )
}
