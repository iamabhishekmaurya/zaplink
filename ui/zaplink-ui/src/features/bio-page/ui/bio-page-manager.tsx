"use client"

import { useState, useEffect } from 'react'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { Badge } from '@/components/ui/badge'
import { Plus, Edit, Trash2, ExternalLink, Users } from 'lucide-react'
import { BioLinkManager } from '@/features/bio-page/ui/bio-link-manager'
import { CreateBioPageDialog } from '@/features/bio-page/ui/create-bio-page-dialog'
import { useAuth } from '@/hooks/useAuth'
import { handleApiError, showSuccessToast } from '@/lib/error-handler'
import { bioPageService, type BioPage, type BioLink, type CreateBioPageRequest } from '@/services/bioPageService'

interface BioPageManagerProps {
  onPageSelect: (pageId: string) => void
  onPageUpdate: () => void
}

export function BioPageManager({ onPageSelect, onPageUpdate }: BioPageManagerProps) {
  const [pages, setPages] = useState<BioPage[]>([])
  const [selectedPage, setSelectedPage] = useState<BioPage | null>(null)
  const [loading, setLoading] = useState(true)
  const [showCreateDialog, setShowCreateDialog] = useState(false)
  const { user } = useAuth()

  const fetchPages = async () => {
    if (!user?.id) return

    try {
      const pages = await bioPageService.getBioPagesByOwnerId(user.id.toString())
      setPages(pages)
    } catch (error) {
      handleApiError(error, 'Failed to fetch bio pages')
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchPages()
  }, [user?.id])

  const handleCreatePage = async (pageData: CreateBioPageRequest) => {
    if (!user?.id) {
      handleApiError({ message: 'User not authenticated' }, 'Create bio page')
      return
    }

    try {
      const newPage = await bioPageService.createBioPage({
        ...pageData,
        owner_id: user.id.toString()
      })

      setPages(prev => [...prev, newPage])
      showSuccessToast('Bio page created successfully')
      setShowCreateDialog(false)
      onPageUpdate()
    } catch (error) {
      handleApiError(error, 'Failed to create bio page')
    }
  }

  const handleDeletePage = async (pageId: string) => {
    if (!confirm('Are you sure you want to delete this bio page?')) return

    try {
      await bioPageService.deleteBioPage(pageId)
      setPages(prev => prev.filter(p => p.id !== pageId))
      if (selectedPage?.id === pageId) {
        setSelectedPage(null)
      }
      showSuccessToast('Bio page deleted successfully')
      onPageUpdate()
    } catch (error) {
      handleApiError(error, 'Failed to delete bio page')
    }
  }

  if (loading) {
    return <div>Loading...</div>
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h3 className="text-lg font-semibold">Your Bio Pages</h3>
        <Button onClick={() => setShowCreateDialog(true)}>
          <Plus className="h-4 w-4 mr-2" />
          Create New Page
        </Button>
      </div>

      {pages.length === 0 ? (
        <Card>
          <CardContent className="flex flex-col items-center justify-center py-12">
            <Users className="h-12 w-12 text-muted-foreground mb-4" />
            <h3 className="text-lg font-semibold mb-2">No bio pages yet</h3>
            <p className="text-muted-foreground text-center mb-4">
              Create your first bio page to start sharing your links
            </p>
            <Button onClick={() => setShowCreateDialog(true)}>
              <Plus className="h-4 w-4 mr-2" />
              Create Bio Page
            </Button>
          </CardContent>
        </Card>
      ) : (
        <div className="grid gap-4">
          {pages.map((page) => (
            <Card key={page.id} className="cursor-pointer hover:shadow-md transition-shadow">
              <CardHeader>
                <div className="flex justify-between items-start">
                  <div>
                    <CardTitle className="flex items-center gap-2">
                      {page.username}
                      <Badge variant="outline">
                        {page.bioLinks?.length || 0} links
                      </Badge>
                    </CardTitle>
                    <p className="text-sm text-muted-foreground mt-1">
                      zap.link/{page.username}
                    </p>
                  </div>
                  <div className="flex gap-2">
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => window.open(`/v1/bio/${page.username}`, '_blank')}
                    >
                      <ExternalLink className="h-4 w-4" />
                    </Button>
                    <Button
                      variant="outline"
                      size="sm"
                      onClick={() => handleDeletePage(page.id)}
                    >
                      <Trash2 className="h-4 w-4" />
                    </Button>
                  </div>
                </div>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  <div>
                    <Label className="text-sm font-medium">Bio</Label>
                    <p className="text-sm text-muted-foreground line-clamp-2">
                      {page.bioText || 'No bio added yet'}
                    </p>
                  </div>

                  <div className="flex gap-2">
                    <Button
                      variant="outline"
                      onClick={() => {
                        setSelectedPage(page)
                        onPageSelect(String(page.id))
                      }}
                    >
                      <Edit className="h-4 w-4 mr-2" />
                      Manage Links
                    </Button>
                  </div>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      {selectedPage && (
        <Card>
          <CardHeader>
            <CardTitle>Manage Links for {selectedPage.username}</CardTitle>
          </CardHeader>
          <CardContent>
            <BioLinkManager
              pageId={selectedPage.id}
              links={selectedPage.bioLinks || []}
              onLinksUpdate={() => {
                fetchPages()
                onPageUpdate()
              }}
            />
          </CardContent>
        </Card>
      )}

      <CreateBioPageDialog
        open={showCreateDialog}
        onOpenChange={setShowCreateDialog}
        onCreatePage={handleCreatePage}
      />
    </div>
  )
}
