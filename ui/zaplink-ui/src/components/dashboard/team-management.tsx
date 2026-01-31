"use client"

import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "@/components/ui/dropdown-menu"
import { Plus, MoreHorizontal, UserPlus, Settings, Trash2, Crown, Edit, Eye, CheckCircle, XCircle } from "lucide-react"
import { toast } from "sonner"

interface TeamMember {
  id: number
  teamId: number
  teamName: string
  userId: number
  username: string
  email: string
  firstName: string
  lastName: string
  role: string
  status: string
  invitedAt: string
  joinedAt?: string
  organizationId: number
  organizationName: string
}

interface TeamManagementProps {
  organizationId: number
  userRole: string
}

const roleConfig = {
  ADMIN: { label: "Admin", color: "bg-purple-100 text-purple-800", icon: Crown },
  EDITOR: { label: "Editor", color: "bg-blue-100 text-blue-800", icon: Edit },
  APPROVER: { label: "Approver", color: "bg-green-100 text-green-800", icon: CheckCircle },
  VIEWER: { label: "Viewer", color: "bg-gray-100 text-gray-800", icon: Eye },
  INFLUENCER: { label: "Influencer", color: "bg-orange-100 text-orange-800", icon: UserPlus }
}

const statusConfig = {
  ACTIVE: { label: "Active", color: "bg-green-100 text-green-800" },
  PENDING: { label: "Pending", color: "bg-yellow-100 text-yellow-800" },
  INACTIVE: { label: "Inactive", color: "bg-red-100 text-red-800" }
}

export function TeamManagement({ organizationId, userRole }: TeamManagementProps) {
  const [teamMembers, setTeamMembers] = useState<TeamMember[]>([])
  const [loading, setLoading] = useState(true)
  const [inviteDialogOpen, setInviteDialogOpen] = useState(false)
  const [newMemberEmail, setNewMemberEmail] = useState("")
  const [newMemberRole, setNewMemberRole] = useState("")
  const [selectedTeamId, setSelectedTeamId] = useState("")

  useEffect(() => {
    fetchTeamMembers()
  }, [organizationId])

  const fetchTeamMembers = async () => {
    try {
      const response = await fetch(`/api/rd/teams/members`, {
        headers: {
          "X-Org-Id": organizationId.toString(),
          "X-API-Version": "1"
        }
      })
      if (response.ok) {
        const data = await response.json()
        setTeamMembers(data)
      }
    } catch (error) {
      toast.error("Failed to fetch team members")
    } finally {
      setLoading(false)
    }
  }

  const handleInviteMember = async () => {
    if (!newMemberEmail || !newMemberRole || !selectedTeamId) {
      toast.error("Please fill all fields")
      return
    }

    try {
      const response = await fetch(`/api/wr/teams/invite`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "X-Org-Id": organizationId.toString(),
          "X-API-Version": "1"
        },
        body: JSON.stringify({
          email: newMemberEmail,
          role: newMemberRole,
          teamId: parseInt(selectedTeamId)
        })
      })

      if (response.ok) {
        toast.success("Member invited successfully")
        setInviteDialogOpen(false)
        setNewMemberEmail("")
        setNewMemberRole("")
        setSelectedTeamId("")
        fetchTeamMembers()
      } else {
        const error = await response.json()
        toast.error(error.message || "Failed to invite member")
      }
    } catch (error) {
      toast.error("Failed to invite member")
    }
  }

  const handleChangeRole = async (memberId: number, newRole: string) => {
    try {
      const response = await fetch(`/api/wr/teams/members/${memberId}/role`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "X-Org-Id": organizationId.toString(),
          "X-API-Version": "1"
        },
        body: JSON.stringify({
          userId: memberId,
          newRole: newRole
        })
      })

      if (response.ok) {
        toast.success("Role changed successfully")
        fetchTeamMembers()
      } else {
        const error = await response.json()
        toast.error(error.message || "Failed to change role")
      }
    } catch (error) {
      toast.error("Failed to change role")
    }
  }

  const handleRemoveMember = async (memberId: number, teamId: number) => {
    if (!confirm("Are you sure you want to remove this member?")) {
      return
    }

    try {
      const response = await fetch(`/api/wr/teams/members/${memberId}?teamId=${teamId}`, {
        method: "DELETE",
        headers: {
          "X-Org-Id": organizationId.toString(),
          "X-API-Version": "1"
        }
      })

      if (response.ok) {
        toast.success("Member removed successfully")
        fetchTeamMembers()
      } else {
        const error = await response.json()
        toast.error(error.message || "Failed to remove member")
      }
    } catch (error) {
      toast.error("Failed to remove member")
    }
  }

  const canManageMembers = userRole === "ADMIN" || userRole === "APPROVER"

  if (loading) {
    return <div className="flex items-center justify-center h-64">Loading...</div>
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-2xl font-bold tracking-tight">Team Management</h2>
          <p className="text-muted-foreground">
            Manage your team members and their roles
          </p>
        </div>
        {canManageMembers && (
          <Dialog open={inviteDialogOpen} onOpenChange={setInviteDialogOpen}>
            <DialogTrigger asChild>
              <Button>
                <UserPlus className="mr-2 h-4 w-4" />
                Invite Member
              </Button>
            </DialogTrigger>
            <DialogContent>
              <DialogHeader>
                <DialogTitle>Invite Team Member</DialogTitle>
                <DialogDescription>
                  Send an invitation to join your team
                </DialogDescription>
              </DialogHeader>
              <div className="space-y-4">
                <div>
                  <Label htmlFor="email">Email Address</Label>
                  <Input
                    id="email"
                    type="email"
                    placeholder="Enter email address"
                    value={newMemberEmail}
                    onChange={(e) => setNewMemberEmail(e.target.value)}
                  />
                </div>
                <div>
                  <Label htmlFor="team">Team</Label>
                  <Select value={selectedTeamId} onValueChange={setSelectedTeamId}>
                    <SelectTrigger>
                      <SelectValue placeholder="Select a team" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="1">Default Team</SelectItem>
                      {/* Add more teams as needed */}
                    </SelectContent>
                  </Select>
                </div>
                <div>
                  <Label htmlFor="role">Role</Label>
                  <Select value={newMemberRole} onValueChange={setNewMemberRole}>
                    <SelectTrigger>
                      <SelectValue placeholder="Select a role" />
                    </SelectTrigger>
                    <SelectContent>
                      {Object.entries(roleConfig).map(([key, config]) => (
                        <SelectItem key={key} value={key}>
                          {config.label}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
                <Button onClick={handleInviteMember} className="w-full">
                  Send Invitation
                </Button>
              </div>
            </DialogContent>
          </Dialog>
        )}
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Team Members</CardTitle>
          <CardDescription>
            A list of all team members and their roles
          </CardDescription>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Member</TableHead>
                <TableHead>Role</TableHead>
                <TableHead>Status</TableHead>
                <TableHead>Team</TableHead>
                <TableHead>Joined</TableHead>
                {canManageMembers && <TableHead>Actions</TableHead>}
              </TableRow>
            </TableHeader>
            <TableBody>
              {teamMembers.map((member) => (
                <TableRow key={member.id}>
                  <TableCell>
                    <div className="flex items-center space-x-3">
                      <Avatar>
                        <AvatarImage src={""} />
                        <AvatarFallback>
                          {member.firstName?.[0] || member.username?.[0] || "U"}
                        </AvatarFallback>
                      </Avatar>
                      <div>
                        <div className="font-medium">
                          {member.firstName && member.lastName
                            ? `${member.firstName} ${member.lastName}`
                            : member.username}
                        </div>
                        <div className="text-sm text-muted-foreground">
                          {member.email}
                        </div>
                      </div>
                    </div>
                  </TableCell>
                  <TableCell>
                    <Badge className={roleConfig[member.role as keyof typeof roleConfig]?.color}>
                      {roleConfig[member.role as keyof typeof roleConfig]?.label}
                    </Badge>
                  </TableCell>
                  <TableCell>
                    <Badge className={statusConfig[member.status as keyof typeof statusConfig]?.color}>
                      {statusConfig[member.status as keyof typeof statusConfig]?.label}
                    </Badge>
                  </TableCell>
                  <TableCell>{member.teamName}</TableCell>
                  <TableCell>
                    {member.joinedAt
                      ? new Date(member.joinedAt).toLocaleDateString()
                      : "Not joined"}
                  </TableCell>
                  {canManageMembers && (
                    <TableCell>
                      <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                          <Button variant="ghost" className="h-8 w-8 p-0">
                            <MoreHorizontal className="h-4 w-4" />
                          </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent align="end">
                          <DropdownMenuItem
                            onClick={() => handleChangeRole(member.userId, "EDITOR")}
                          >
                            <Edit className="mr-2 h-4 w-4" />
                            Change Role
                          </DropdownMenuItem>
                          <DropdownMenuItem
                            onClick={() => handleRemoveMember(member.userId, member.teamId)}
                            className="text-red-600"
                          >
                            <Trash2 className="mr-2 h-4 w-4" />
                            Remove Member
                          </DropdownMenuItem>
                        </DropdownMenuContent>
                      </DropdownMenu>
                    </TableCell>
                  )}
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  )
}
