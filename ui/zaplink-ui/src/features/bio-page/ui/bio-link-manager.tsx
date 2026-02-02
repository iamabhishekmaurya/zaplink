"use client"

import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Switch } from '@/components/ui/switch'
import { Badge } from '@/components/ui/badge'
import { Plus, Edit, Trash2, GripVertical, ExternalLink } from 'lucide-react'
import { CreateBioLinkDialog } from '@/features/bio-page/ui/create-bio-link-dialog'
import { EditBioLinkDialog } from '@/features/bio-page/ui/edit-bio-link-dialog'
import { toast } from 'sonner'
import {
  DndContext,
  closestCenter,
  KeyboardSensor,
  PointerSensor,
  useSensor,
  useSensors,
  DragEndEvent,
} from '@dnd-kit/core'
import {
  arrayMove,
  SortableContext,
  sortableKeyboardCoordinates,
  verticalListSortingStrategy,
} from '@dnd-kit/sortable'
import {
  useSortable,
} from '@dnd-kit/sortable'
import { CSS } from '@dnd-kit/utilities'
import { BioLink } from '@/services/bioPageService'

interface BioLinkManagerProps {
  pageId: number
  links: BioLink[]
  onLinksUpdate: () => void
}

function SortableLinkItem({ link, onEdit, onDelete }: {
  link: BioLink
  onEdit: (link: BioLink) => void
  onDelete: (linkId: number) => void
}) {
  const {
    attributes,
    listeners,
    setNodeRef,
    transform,
    transition,
  } = useSortable({ id: link.id })

  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
  }

  return (
    <div ref={setNodeRef} style={style} className="flex items-center space-x-4 p-4 border rounded-lg bg-card">
      <div {...attributes} {...listeners} className="cursor-grab">
        <GripVertical className="h-4 w-4 text-muted-foreground" />
      </div>

      <div className="flex-1 space-y-2">
        <div className="flex items-center justify-between">
          <h4 className="font-medium">{link.title}</h4>
          <div className="flex items-center space-x-2">
            <Badge variant={link.isActive ? "default" : "secondary"}>
              {link.isActive ? "Active" : "Inactive"}
            </Badge>
            <Badge variant="outline">{link.type}</Badge>
          </div>
        </div>

        {link.url && (
          <p className="text-sm text-muted-foreground truncate">{link.url}</p>
        )}

        {link.type === 'PRODUCT' && link.price && (
          <p className="text-sm font-medium">
            {link.currency} {link.price.toFixed(2)}
          </p>
        )}
      </div>

      <div className="flex items-center space-x-2">
        {link.url && (
          <Button
            variant="outline"
            size="sm"
            onClick={() => window.open(link.url, '_blank')}
          >
            <ExternalLink className="h-4 w-4" />
          </Button>
        )}
        <Button
          variant="outline"
          size="sm"
          onClick={() => onEdit(link)}
        >
          <Edit className="h-4 w-4" />
        </Button>
        <Button
          variant="outline"
          size="sm"
          onClick={() => onDelete(link.id)}
        >
          <Trash2 className="h-4 w-4" />
        </Button>
      </div>
    </div>
  )
}

export function BioLinkManager({ pageId, links, onLinksUpdate }: BioLinkManagerProps) {
  const [bioLinks, setBioLinks] = useState<BioLink[]>(links.sort((a, b) => a.sortOrder - b.sortOrder))
  const [showCreateDialog, setShowCreateDialog] = useState(false)
  const [editingLink, setEditingLink] = useState<BioLink | null>(null)
  // const { toast } = useToast()

  const sensors = useSensors(
    useSensor(PointerSensor),
    useSensor(KeyboardSensor, {
      coordinateGetter: sortableKeyboardCoordinates,
    })
  )

  const handleDragEnd = async (event: DragEndEvent) => {
    const { active, over } = event

    if (active.id !== over?.id) {
      const oldIndex = bioLinks.findIndex(link => link.id === active.id)
      const newIndex = bioLinks.findIndex(link => link.id === over?.id)

      const newLinks = arrayMove(bioLinks, oldIndex, newIndex)
      setBioLinks(newLinks)

      // Update sort orders in backend
      const reorderedLinks = newLinks.map((link, index) => ({
        linkId: link.id,
        sortOrder: index
      }))

      try {
        const response = await fetch(`/api/wr/bio-pages/${pageId}/links/reorder`, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(reorderedLinks.map(r => r.linkId)),
        })

        if (response.ok) {
          onLinksUpdate()
          toast.success("Success", {
            description: "Links reordered successfully",
          })
        }
      } catch (error) {
        toast.error("Error", {
          description: "Failed to reorder links",
        })
        // Revert to original order
        setBioLinks(bioLinks)
      }
    }
  }

  const handleCreateLink = async (linkData: any) => {
    try {
      const response = await fetch('/api/wr/bio-links', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          ...linkData,
          page_id: pageId,
          sort_order: bioLinks.length
        }),
      })

      if (response.ok) {
        const newLink = await response.json()
        setBioLinks(prev => [...prev, newLink])
        setShowCreateDialog(false)
        onLinksUpdate()
        toast.success("Success", {
          description: "Link created successfully",
        })
      }
    } catch (error) {
      toast.error("Error", {
        description: "Failed to create link",
      })
    }
  }

  const handleUpdateLink = async (linkId: number, linkData: any) => {
    try {
      const response = await fetch(`/api/wr/bio-links/${linkId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(linkData),
      })

      if (response.ok) {
        const updatedLink = await response.json()
        setBioLinks(prev => prev.map(link =>
          link.id === linkId ? updatedLink : link
        ))
        setEditingLink(null)
        onLinksUpdate()
        toast.success("Success", {
          description: "Link updated successfully",
        })
      }
    } catch (error) {
      toast.error("Error", {
        description: "Failed to update link",
      })
    }
  }

  const handleDeleteLink = async (linkId: number) => {
    if (!confirm('Are you sure you want to delete this link?')) return

    try {
      const response = await fetch(`/api/wr/bio-links/${linkId}`, {
        method: 'DELETE',
      })

      if (response.ok) {
        setBioLinks(prev => prev.filter(link => link.id !== linkId))
        onLinksUpdate()
        toast.success("Success", {
          description: "Link deleted successfully",
        })
      }
    } catch (error) {
      toast.error("Error", {
        description: "Failed to delete link",
      })
    }
  }

  return (
    <div className="space-y-4">
      <div className="flex justify-between items-center">
        <h4 className="text-lg font-semibold">Links ({bioLinks.length})</h4>
        <Button onClick={() => setShowCreateDialog(true)}>
          <Plus className="h-4 w-4 mr-2" />
          Add Link
        </Button>
      </div>

      {bioLinks.length === 0 ? (
        <Card>
          <CardContent className="flex flex-col items-center justify-center py-12">
            <p className="text-muted-foreground text-center mb-4">
              No links added yet. Create your first link to get started.
            </p>
            <Button onClick={() => setShowCreateDialog(true)}>
              <Plus className="h-4 w-4 mr-2" />
              Add Your First Link
            </Button>
          </CardContent>
        </Card>
      ) : (
        <DndContext
          sensors={sensors}
          collisionDetection={closestCenter}
          onDragEnd={handleDragEnd}
        >
          <SortableContext
            items={bioLinks.map(link => link.id)}
            strategy={verticalListSortingStrategy}
          >
            <div className="space-y-2">
              {bioLinks.map((link) => (
                <SortableLinkItem
                  key={link.id}
                  link={link}
                  onEdit={setEditingLink}
                  onDelete={handleDeleteLink}
                />
              ))}
            </div>
          </SortableContext>
        </DndContext>
      )}

      <CreateBioLinkDialog
        open={showCreateDialog}
        onOpenChange={setShowCreateDialog}
        onCreateLink={handleCreateLink}
      />

      {editingLink && (
        <EditBioLinkDialog
          link={editingLink}
          open={!!editingLink}
          onOpenChange={(open) => !open && setEditingLink(null)}
          onUpdateLink={(linkData) => handleUpdateLink(editingLink.id, linkData)}
        />
      )}
    </div>
  )
}
