"use client"

import { useState, useEffect } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog"
import { Textarea } from "@/components/ui/textarea"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "@/components/ui/dropdown-menu"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Plus, MoreHorizontal, Send, CheckCircle, XCircle, Eye, Edit, Clock, FileText } from "lucide-react"
import { toast } from "sonner"

interface Post {
  id: number
  title: string
  content?: string
  campaignId?: number
  campaignName?: string
  authorId: number
  authorName: string
  authorEmail: string
  status: string
  submittedAt?: string
  reviewedBy?: number
  reviewerName?: string
  reviewedAt?: string
  reviewComments?: string
  publishedAt?: string
  createdAt: string
  updatedAt: string
}

interface WorkflowManagementProps {
  organizationId: number
  userRole: string
  userId: number
}

const statusConfig = {
  DRAFT: { label: "Draft", color: "bg-gray-100 text-gray-800", icon: FileText },
  SUBMITTED: { label: "Submitted", color: "bg-yellow-100 text-yellow-800", icon: Clock },
  APPROVED: { label: "Approved", color: "bg-green-100 text-green-800", icon: CheckCircle },
  REJECTED: { label: "Rejected", color: "bg-red-100 text-red-800", icon: XCircle },
  PUBLISHED: { label: "Published", color: "bg-blue-100 text-blue-800", icon: Eye }
}

export function WorkflowManagement({ organizationId, userRole, userId }: WorkflowManagementProps) {
  const [posts, setPosts] = useState<Post[]>([])
  const [pendingPosts, setPendingPosts] = useState<Post[]>([])
  const [loading, setLoading] = useState(true)
  const [createDialogOpen, setCreateDialogOpen] = useState(false)
  const [reviewDialogOpen, setReviewDialogOpen] = useState(false)
  const [selectedPost, setSelectedPost] = useState<Post | null>(null)
  
  // Form states
  const [postTitle, setPostTitle] = useState("")
  const [postContent, setPostContent] = useState("")
  const [selectedCampaignId, setSelectedCampaignId] = useState("")
  const [reviewDecision, setReviewDecision] = useState("")
  const [reviewComments, setReviewComments] = useState("")

  useEffect(() => {
    fetchPosts()
    fetchPendingPosts()
  }, [organizationId])

  const fetchPosts = async () => {
    try {
      const response = await fetch(`/api/rd/workflow/posts/author/${userId}`, {
        headers: {
          "X-Org-Id": organizationId.toString(),
          "X-API-Version": "1"
        }
      })
      if (response.ok) {
        const data = await response.json()
        setPosts(data)
      }
    } catch (error) {
      toast.error("Failed to fetch posts")
    } finally {
      setLoading(false)
    }
  }

  const fetchPendingPosts = async () => {
    try {
      const response = await fetch(`/api/rd/workflow/pending`, {
        headers: {
          "X-Org-Id": organizationId.toString(),
          "X-API-Version": "1"
        }
      })
      if (response.ok) {
        const data = await response.json()
        setPendingPosts(data)
      }
    } catch (error) {
      toast.error("Failed to fetch pending posts")
    }
  }

  const handleSubmitPost = async () => {
    if (!postTitle.trim()) {
      toast.error("Please enter a post title")
      return
    }

    try {
      const response = await fetch(`/api/wr/workflow/submit`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "X-Org-Id": organizationId.toString(),
          "X-API-Version": "1"
        },
        body: JSON.stringify({
          title: postTitle,
          content: postContent,
          campaignId: selectedCampaignId ? parseInt(selectedCampaignId) : null,
          authorId: userId
        })
      })

      if (response.ok) {
        toast.success("Post submitted for approval")
        setCreateDialogOpen(false)
        setPostTitle("")
        setPostContent("")
        setSelectedCampaignId("")
        fetchPosts()
        fetchPendingPosts()
      } else {
        const error = await response.json()
        toast.error(error.message || "Failed to submit post")
      }
    } catch (error) {
      toast.error("Failed to submit post")
    }
  }

  const handleReviewPost = async () => {
    if (!selectedPost || !reviewDecision) {
      toast.error("Please select a decision")
      return
    }

    try {
      const response = await fetch(`/api/wr/workflow/approve-reject`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "X-Org-Id": organizationId.toString(),
          "X-API-Version": "1"
        },
        body: JSON.stringify({
          postId: selectedPost.id,
          decision: reviewDecision.toUpperCase(),
          comments: reviewComments,
          reviewerId: userId
        })
      })

      if (response.ok) {
        toast.success(`Post ${reviewDecision.toLowerCase()} successfully`)
        setReviewDialogOpen(false)
        setSelectedPost(null)
        setReviewDecision("")
        setReviewComments("")
        fetchPosts()
        fetchPendingPosts()
      } else {
        const error = await response.json()
        toast.error(error.message || "Failed to review post")
      }
    } catch (error) {
      toast.error("Failed to review post")
    }
  }

  const canSubmitPosts = userRole === "EDITOR" || userRole === "ADMIN"
  const canReviewPosts = userRole === "APPROVER" || userRole === "ADMIN"

  if (loading) {
    return <div className="flex items-center justify-center h-64">Loading...</div>
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-2xl font-bold tracking-tight">Workflow Management</h2>
          <p className="text-muted-foreground">
            Manage content approval workflows
          </p>
        </div>
        {canSubmitPosts && (
          <Dialog open={createDialogOpen} onOpenChange={setCreateDialogOpen}>
            <DialogTrigger asChild>
              <Button>
                <Plus className="mr-2 h-4 w-4" />
                Create Post
              </Button>
            </DialogTrigger>
            <DialogContent className="max-w-2xl">
              <DialogHeader>
                <DialogTitle>Create New Post</DialogTitle>
                <DialogDescription>
                  Create a new post and submit it for approval
                </DialogDescription>
              </DialogHeader>
              <div className="space-y-4">
                <div>
                  <Label htmlFor="title">Title</Label>
                  <Input
                    id="title"
                    placeholder="Enter post title"
                    value={postTitle}
                    onChange={(e) => setPostTitle(e.target.value)}
                  />
                </div>
                <div>
                  <Label htmlFor="content">Content</Label>
                  <Textarea
                    id="content"
                    placeholder="Enter post content"
                    value={postContent}
                    onChange={(e) => setPostContent(e.target.value)}
                    rows={6}
                  />
                </div>
                <div>
                  <Label htmlFor="campaign">Campaign (Optional)</Label>
                  <Select value={selectedCampaignId} onValueChange={setSelectedCampaignId}>
                    <SelectTrigger>
                      <SelectValue placeholder="Select a campaign" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="1">Summer Campaign 2024</SelectItem>
                      <SelectItem value="2">Product Launch</SelectItem>
                      {/* Add more campaigns as needed */}
                    </SelectContent>
                  </Select>
                </div>
                <Button onClick={handleSubmitPost} className="w-full">
                  <Send className="mr-2 h-4 w-4" />
                  Submit for Approval
                </Button>
              </div>
            </DialogContent>
          </Dialog>
        )}
      </div>

      <Tabs defaultValue="my-posts" className="space-y-4">
        <TabsList>
          <TabsTrigger value="my-posts">My Posts</TabsTrigger>
          {canReviewPosts && <TabsTrigger value="pending-approval">Pending Approval</TabsTrigger>}
        </TabsList>
        
        <TabsContent value="my-posts" className="space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>My Posts</CardTitle>
              <CardDescription>
                Posts you have created and their current status
              </CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Title</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Campaign</TableHead>
                    <TableHead>Created</TableHead>
                    <TableHead>Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {posts.map((post) => (
                    <TableRow key={post.id}>
                      <TableCell className="font-medium">{post.title}</TableCell>
                      <TableCell>
                        <Badge className={statusConfig[post.status as keyof typeof statusConfig]?.color}>
                          {statusConfig[post.status as keyof typeof statusConfig]?.label}
                        </Badge>
                      </TableCell>
                      <TableCell>{post.campaignName || "No campaign"}</TableCell>
                      <TableCell>
                        {new Date(post.createdAt).toLocaleDateString()}
                      </TableCell>
                      <TableCell>
                        <DropdownMenu>
                          <DropdownMenuTrigger asChild>
                            <Button variant="ghost" className="h-8 w-8 p-0">
                              <MoreHorizontal className="h-4 w-4" />
                            </Button>
                          </DropdownMenuTrigger>
                          <DropdownMenuContent align="end">
                            <DropdownMenuItem>
                              <Eye className="mr-2 h-4 w-4" />
                              View Details
                            </DropdownMenuItem>
                            {post.status === "DRAFT" && canSubmitPosts && (
                              <DropdownMenuItem>
                                <Edit className="mr-2 h-4 w-4" />
                                Edit Post
                              </DropdownMenuItem>
                            )}
                          </DropdownMenuContent>
                        </DropdownMenu>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        {canReviewPosts && (
          <TabsContent value="pending-approval" className="space-y-4">
            <Card>
              <CardHeader>
                <CardTitle>Pending Approval</CardTitle>
                <CardDescription>
                  Posts waiting for your review and approval
                </CardDescription>
              </CardHeader>
              <CardContent>
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>Title</TableHead>
                      <TableHead>Author</TableHead>
                      <TableHead>Submitted</TableHead>
                      <TableHead>Actions</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {pendingPosts.map((post) => (
                      <TableRow key={post.id}>
                        <TableCell className="font-medium">{post.title}</TableCell>
                        <TableCell>
                          <div className="flex items-center space-x-2">
                            <Avatar className="h-6 w-6">
                              <AvatarImage src="" />
                              <AvatarFallback>
                                {post.authorName?.[0] || "A"}
                              </AvatarFallback>
                            </Avatar>
                            <div>
                              <div className="text-sm font-medium">{post.authorName}</div>
                              <div className="text-xs text-muted-foreground">{post.authorEmail}</div>
                            </div>
                          </div>
                        </TableCell>
                        <TableCell>
                          {post.submittedAt
                            ? new Date(post.submittedAt).toLocaleDateString()
                            : "Unknown"}
                        </TableCell>
                        <TableCell>
                          <DropdownMenu>
                            <DropdownMenuTrigger asChild>
                              <Button variant="ghost" className="h-8 w-8 p-0">
                                <MoreHorizontal className="h-4 w-4" />
                              </Button>
                            </DropdownMenuTrigger>
                            <DropdownMenuContent align="end">
                              <DropdownMenuItem
                                onClick={() => {
                                  setSelectedPost(post)
                                  setReviewDialogOpen(true)
                                }}
                              >
                                <CheckCircle className="mr-2 h-4 w-4" />
                                Review Post
                              </DropdownMenuItem>
                            </DropdownMenuContent>
                          </DropdownMenu>
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </CardContent>
            </Card>
          </TabsContent>
        )}
      </Tabs>

      {/* Review Dialog */}
      <Dialog open={reviewDialogOpen} onOpenChange={setReviewDialogOpen}>
        <DialogContent className="max-w-2xl">
          <DialogHeader>
            <DialogTitle>Review Post</DialogTitle>
            <DialogDescription>
              Review and approve or reject this post
            </DialogDescription>
          </DialogHeader>
          {selectedPost && (
            <div className="space-y-4">
              <div>
                <Label>Post Title</Label>
                <p className="font-medium">{selectedPost.title}</p>
              </div>
              <div>
                <Label>Author</Label>
                <p>{selectedPost.authorName} ({selectedPost.authorEmail})</p>
              </div>
              <div>
                <Label htmlFor="decision">Decision</Label>
                <Select value={reviewDecision} onValueChange={setReviewDecision}>
                  <SelectTrigger>
                    <SelectValue placeholder="Select decision" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="approve">
                      <div className="flex items-center">
                        <CheckCircle className="mr-2 h-4 w-4 text-green-600" />
                        Approve
                      </div>
                    </SelectItem>
                    <SelectItem value="reject">
                      <div className="flex items-center">
                        <XCircle className="mr-2 h-4 w-4 text-red-600" />
                        Reject
                      </div>
                    </SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div>
                <Label htmlFor="comments">Comments</Label>
                <Textarea
                  id="comments"
                  placeholder="Add your review comments..."
                  value={reviewComments}
                  onChange={(e) => setReviewComments(e.target.value)}
                  rows={4}
                />
              </div>
              <div className="flex space-x-2">
                <Button onClick={handleReviewPost} className="flex-1">
                  Submit Review
                </Button>
                <Button
                  variant="outline"
                  onClick={() => setReviewDialogOpen(false)}
                  className="flex-1"
                >
                  Cancel
                </Button>
              </div>
            </div>
          )}
        </DialogContent>
      </Dialog>
    </div>
  )
}
