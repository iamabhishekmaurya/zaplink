"use client"

import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Progress } from "@/components/ui/progress"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Calendar, Target, Users, TrendingUp, Eye, Clock, CheckCircle } from "lucide-react"
import { toast } from "sonner"

interface Campaign {
  id: number
  campaignId: number
  campaignName: string
  campaignDescription: string
  campaignStatus: string
  startDate?: string
  endDate?: string
  teamId: number
  teamName: string
  organizationId: number
  organizationName: string
  assignmentStatus: string
  assignedAt: string
  completedAt?: string
  lastUpdated: string
}

interface InfluencerManagementProps {
  organizationId: number
  userId: number
  userRole: string
}

const campaignStatusConfig = {
  DRAFT: { label: "Draft", color: "bg-gray-100 text-gray-800" },
  ACTIVE: { label: "Active", color: "bg-green-100 text-green-800" },
  PAUSED: { label: "Paused", color: "bg-yellow-100 text-yellow-800" },
  COMPLETED: { label: "Completed", color: "bg-blue-100 text-blue-800" }
}

const assignmentStatusConfig = {
  ASSIGNED: { label: "Assigned", color: "bg-blue-100 text-blue-800" },
  IN_PROGRESS: { label: "In Progress", color: "bg-yellow-100 text-yellow-800" },
  COMPLETED: { label: "Completed", color: "bg-green-100 text-green-800" }
}

export function InfluencerManagement({ organizationId, userId, userRole }: InfluencerManagementProps) {
  const [campaigns, setCampaigns] = useState<Campaign[]>([])
  const [loading, setLoading] = useState(true)
  const [stats, setStats] = useState({
    totalCampaigns: 0,
    activeCampaigns: 0,
    completedCampaigns: 0,
    inProgressCampaigns: 0
  })

  useEffect(() => {
    if (userRole === "INFLUENCER") {
      fetchInfluencerCampaigns()
    }
  }, [organizationId, userId, userRole])

  const fetchInfluencerCampaigns = async () => {
    try {
      const response = await fetch(`/api/rd/influencers/campaigns`, {
        headers: {
          "X-Org-Id": organizationId.toString(),
          "X-User-Id": userId.toString(),
          "X-API-Version": "1"
        }
      })
      if (response.ok) {
        const data = await response.json()
        setCampaigns(data)
        
        // Calculate stats
        const stats = {
          totalCampaigns: data.length,
          activeCampaigns: data.filter((c: Campaign) => c.campaignStatus === "ACTIVE").length,
          completedCampaigns: data.filter((c: Campaign) => c.assignmentStatus === "COMPLETED").length,
          inProgressCampaigns: data.filter((c: Campaign) => c.assignmentStatus === "IN_PROGRESS").length
        }
        setStats(stats)
      }
    } catch (error) {
      toast.error("Failed to fetch campaigns")
    } finally {
      setLoading(false)
    }
  }

  const calculateProgress = (campaign: Campaign) => {
    if (campaign.assignmentStatus === "COMPLETED") return 100
    if (campaign.assignmentStatus === "IN_PROGRESS") return 50
    return 0
  }

  const getDaysRemaining = (endDate?: string) => {
    if (!endDate) return null
    const end = new Date(endDate)
    const now = new Date()
    const diffTime = end.getTime() - now.getTime()
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
    return diffDays > 0 ? diffDays : 0
  }

  if (userRole !== "INFLUENCER") {
    return (
      <div className="text-center py-12">
        <Users className="mx-auto h-12 w-12 text-gray-400" />
        <h3 className="mt-2 text-sm font-semibold text-gray-900">Influencer Access Only</h3>
        <p className="mt-1 text-sm text-gray-500">
          This section is only accessible to users with INFLUENCER role.
        </p>
      </div>
    )
  }

  if (loading) {
    return <div className="flex items-center justify-center h-64">Loading...</div>
  }

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-2xl font-bold tracking-tight">Influencer Dashboard</h2>
        <p className="text-muted-foreground">
          Manage your assigned campaigns and track performance
        </p>
      </div>

      {/* Stats Cards */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total Campaigns</CardTitle>
            <Target className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.totalCampaigns}</div>
            <p className="text-xs text-muted-foreground">
              Assigned campaigns
            </p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Active</CardTitle>
            <TrendingUp className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.activeCampaigns}</div>
            <p className="text-xs text-muted-foreground">
              Currently active
            </p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">In Progress</CardTitle>
            <Clock className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.inProgressCampaigns}</div>
            <p className="text-xs text-muted-foreground">
              Working on
            </p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Completed</CardTitle>
            <CheckCircle className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.completedCampaigns}</div>
            <p className="text-xs text-muted-foreground">
              Finished campaigns
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Campaigns List */}
      <div className="space-y-4">
        <h3 className="text-lg font-semibold">Your Campaigns</h3>
        {campaigns.length === 0 ? (
          <Card>
            <CardContent className="flex flex-col items-center justify-center py-12">
              <Target className="h-12 w-12 text-gray-400 mb-4" />
              <h3 className="text-lg font-semibold text-gray-900 mb-2">No Campaigns Assigned</h3>
              <p className="text-gray-500 text-center max-w-md">
                You haven't been assigned to any campaigns yet. Contact your team administrator for campaign assignments.
              </p>
            </CardContent>
          </Card>
        ) : (
          <div className="space-y-4">
            {campaigns.map((campaign) => (
              <Card key={campaign.id}>
                <CardHeader>
                  <div className="flex items-center justify-between">
                    <div>
                      <CardTitle className="text-lg">{campaign.campaignName}</CardTitle>
                      <CardDescription className="mt-1">
                        {campaign.campaignDescription}
                      </CardDescription>
                    </div>
                    <div className="flex space-x-2">
                      <Badge className={campaignStatusConfig[campaign.campaignStatus as keyof typeof campaignStatusConfig]?.color}>
                        {campaignStatusConfig[campaign.campaignStatus as keyof typeof campaignStatusConfig]?.label}
                      </Badge>
                      <Badge className={assignmentStatusConfig[campaign.assignmentStatus as keyof typeof assignmentStatusConfig]?.color}>
                        {assignmentStatusConfig[campaign.assignmentStatus as keyof typeof assignmentStatusConfig]?.label}
                      </Badge>
                    </div>
                  </div>
                </CardHeader>
                <CardContent>
                  <div className="space-y-4">
                    {/* Progress */}
                    <div>
                      <div className="flex items-center justify-between text-sm">
                        <span className="text-muted-foreground">Progress</span>
                        <span className="font-medium">{calculateProgress(campaign)}%</span>
                      </div>
                      <Progress value={calculateProgress(campaign)} className="mt-1" />
                    </div>

                    {/* Campaign Details */}
                    <div className="grid grid-cols-2 gap-4 text-sm">
                      <div className="flex items-center space-x-2">
                        <Calendar className="h-4 w-4 text-muted-foreground" />
                        <span className="text-muted-foreground">Duration:</span>
                        <span>
                          {campaign.startDate && campaign.endDate
                            ? `${new Date(campaign.startDate).toLocaleDateString()} - ${new Date(campaign.endDate).toLocaleDateString()}`
                            : "No dates set"}
                        </span>
                      </div>
                      <div className="flex items-center space-x-2">
                        <Users className="h-4 w-4 text-muted-foreground" />
                        <span className="text-muted-foreground">Team:</span>
                        <span>{campaign.teamName}</span>
                      </div>
                    </div>

                    {/* Time Remaining */}
                    {campaign.endDate && campaign.campaignStatus === "ACTIVE" && (
                      <div className="flex items-center space-x-2 text-sm">
                        <Clock className="h-4 w-4 text-muted-foreground" />
                        <span className="text-muted-foreground">Time remaining:</span>
                        <span className="font-medium">
                          {getDaysRemaining(campaign.endDate)} days
                        </span>
                      </div>
                    )}

                    {/* Assignment Details */}
                    <div className="flex items-center justify-between text-sm text-muted-foreground border-t pt-4">
                      <span>Assigned: {new Date(campaign.assignedAt).toLocaleDateString()}</span>
                      {campaign.completedAt && (
                        <span>Completed: {new Date(campaign.completedAt).toLocaleDateString()}</span>
                      )}
                    </div>

                    {/* Actions */}
                    <div className="flex space-x-2 pt-4">
                      <Button variant="outline" size="sm" className="flex-1">
                        <Eye className="mr-2 h-4 w-4" />
                        View Details
                      </Button>
                      {campaign.assignmentStatus === "ASSIGNED" && (
                        <Button size="sm" className="flex-1">
                          Start Campaign
                        </Button>
                      )}
                      {campaign.assignmentStatus === "IN_PROGRESS" && (
                        <Button size="sm" className="flex-1">
                          Update Progress
                        </Button>
                      )}
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        )}
      </div>
    </div>
  )
}
