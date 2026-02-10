"use client"

import { motion } from 'framer-motion'
import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { Card, CardContent } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { 
  Plus, 
  Edit2, 
  Trash2, 
  GripVertical, 
  ExternalLink, 
  Link2,
  Package,
  Video,
  Music,
  FileText,
  ShoppingBag,
  Image as ImageIcon,
  MoreHorizontal
} from 'lucide-react'
import { CreateLinkForm } from '@/features/bio-page/ui/create-link-form'
import { EditBioLinkDialog } from '@/features/bio-page/ui/edit-bio-link-dialog'
import { transformFormDataToApiRequest } from '@/features/bio-page/lib/form-data-transformer'
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
import { BioLink, bioPageService } from '@/services/bioPageService'
import { cn } from '@/lib/utils'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'

// Get icon based on link type
function getLinkTypeIcon(type: string) {
  switch (type) {
    case 'PRODUCT': return <ShoppingBag className="w-4 h-4" />
    case 'VIDEO': return <Video className="w-4 h-4" />
    case 'MUSIC': return <Music className="w-4 h-4" />
    case 'SOCIAL': return <Link2 className="w-4 h-4" />
    case 'SECTION': return <Package className="w-4 h-4" />
    case 'EMBED': return <FileText className="w-4 h-4" />
    default: return <Link2 className="w-4 h-4" />
  }
}

// Get color based on link type
function getLinkTypeColor(type: string) {
  switch (type) {
    case 'PRODUCT': return 'bg-emerald-100 text-emerald-600 border-emerald-200'
    case 'VIDEO': return 'bg-red-100 text-red-600 border-red-200'
    case 'MUSIC': return 'bg-purple-100 text-purple-600 border-purple-200'
    case 'SOCIAL': return 'bg-blue-100 text-blue-600 border-blue-200'
    case 'SECTION': return 'bg-amber-100 text-amber-600 border-amber-200'
    case 'EMBED': return 'bg-pink-100 text-pink-600 border-pink-200'
    default: return 'bg-slate-100 text-slate-600 border-slate-200'
  }
}

interface BioLinkManagerProps {
  pageId: number
  links: BioLink[]
  onLinksUpdate: () => void
}

function SortableLinkItem({ link, onEdit, onDelete, index }: {
  link: BioLink
  onEdit: (link: BioLink) => void
  onDelete: (linkId: number) => void
  index: number
}) {
  const {
    attributes,
    listeners,
    setNodeRef,
    transform,
    transition,
    isDragging,
  } = useSortable({ id: link.id })

  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
  }

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: index * 0.05 }}
      ref={setNodeRef}
      style={style}
      className={cn(
        "group flex items-center gap-3 p-4 border rounded-xl bg-gradient-to-r from-white to-muted/20",
        "hover:shadow-lg hover:border-violet-300 transition-all duration-300",
        isDragging && "shadow-2xl scale-105 z-50 ring-2 ring-violet-500/50"
      )}
    >
      {/* Drag Handle */}
      <div 
        {...attributes} 
        {...listeners} 
        className="cursor-grab active:cursor-grabbing p-2 rounded-lg hover:bg-muted transition-colors"
      >
        <GripVertical className="h-5 w-5 text-muted-foreground" />
      </div>

      {/* Link Type Icon */}
      <div className={cn(
        "p-2.5 rounded-xl border-2",
        getLinkTypeColor(link.type)
      )}>
        {getLinkTypeIcon(link.type)}
      </div>

      {/* Link Content */}
      <div className="flex-1 min-w-0 space-y-1">
        <div className="flex items-center gap-2">
          <h4 className="font-semibold text-sm truncate">{link.title}</h4>
          {link.isActive ? (
            <span className="w-2 h-2 rounded-full bg-emerald-500" title="Active" />
          ) : (
            <span className="w-2 h-2 rounded-full bg-slate-300" title="Inactive" />
          )}
        </div>

        {link.url && (
          <p className="text-xs text-muted-foreground truncate flex items-center gap-1">
            <ExternalLink className="w-3 h-3" />
            {link.url}
          </p>
        )}

        <div className="flex items-center gap-2 pt-1">
          <Badge 
            variant="secondary" 
            className={cn("text-[10px] px-1.5 py-0.5", getLinkTypeColor(link.type))}
          >
            {link.type}
          </Badge>
          {link.type === 'PRODUCT' && link.price && (
            <span className="text-xs font-medium text-emerald-600">
              {link.currency} {link.price.toFixed(2)}
            </span>
          )}
        </div>
      </div>

      {/* Actions */}
      <div className="flex items-center gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
        {link.url && (
          <Button
            variant="ghost"
            size="icon"
            className="h-8 w-8 text-muted-foreground hover:text-foreground"
            onClick={() => window.open(link.url, '_blank')}
          >
            <ExternalLink className="h-4 w-4" />
          </Button>
        )}
        <Button
          variant="ghost"
          size="icon"
          className="h-8 w-8 text-muted-foreground hover:text-violet-600"
          onClick={() => onEdit(link)}
        >
          <Edit2 className="h-4 w-4" />
        </Button>
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="ghost" size="icon" className="h-8 w-8 text-muted-foreground hover:text-red-500">
              <Trash2 className="h-4 w-4" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end">
            <DropdownMenuItem 
              className="text-red-600 focus:text-red-600"
              onClick={() => onDelete(link.id)}
            >
              <Trash2 className="h-4 w-4 mr-2" />
              Delete Link
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
    </motion.div>
  )
}

export function BioLinkManager({ pageId, links, onLinksUpdate }: BioLinkManagerProps) {
  // Fully controlled component - no internal state for links
  const bioLinks = links.sort((a, b) => a.sortOrder - b.sortOrder)
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

      try {
        await bioPageService.reorderLinks(pageId, newLinks.map(l => l.id));
        onLinksUpdate()
        toast.success("Success", {
          description: "Links reordered successfully",
        })
      } catch (error) {
        toast.error("Error", {
          description: "Failed to reorder links",
        })
      }
    }
  }

  const handleCreateLink = async (linkData: any) => {
    try {
      // Ensure type is a string before transforming
      if (!linkData.type || typeof linkData.type !== 'string') {
        linkData.type = 'LINK';
      }
      
      // Transform form data to API request format
      const requestData = transformFormDataToApiRequest(linkData, pageId, bioLinks.length);

      await bioPageService.createBioLink(requestData);

      onLinksUpdate()
      toast.success("Success", {
        description: "Link created successfully",
      })
    } catch (error) {
      toast.error("Error", {
        description: "Failed to create link",
      })
    }
  }

  const handleUpdateLink = async (linkId: number, linkData: any) => {
    try {
      const requestData = {
        title: linkData.title,
        url: linkData.url,
        type: linkData.type,
        is_active: linkData.isActive,
        price: linkData.price,
        currency: linkData.currency,
        metadata: linkData.metadata,
        schedule_from: linkData.scheduleFrom ? new Date(linkData.scheduleFrom).toISOString() : undefined,
        schedule_to: linkData.scheduleTo ? new Date(linkData.scheduleTo).toISOString() : undefined,
        icon_url: linkData.iconUrl,
        thumbnail_url: linkData.thumbnailUrl,
        embed_code: linkData.embedCode
      };

      await bioPageService.updateBioLink(linkId, requestData);

      setEditingLink(null)
      onLinksUpdate()
      toast.success("Success", {
        description: "Link updated successfully",
      })
    } catch (error) {
      toast.error("Error", {
        description: "Failed to update link",
      })
    }
  }

  const handleDeleteLink = async (linkId: number) => {
    if (!confirm('Are you sure you want to delete this link?')) return

    try {
      await bioPageService.deleteBioLink(linkId);

      onLinksUpdate()
      toast.success("Success", {
        description: "Link deleted successfully",
      })
    } catch (error) {
      toast.error("Error", {
        description: "Failed to delete link",
      })
    }
  }

  return (
    <div className="space-y-4">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div className="flex items-center gap-2">
          <h4 className="text-lg font-semibold">Links ({bioLinks.length})</h4>
          <Badge variant="secondary" className="text-xs">
            Drag to reorder
          </Badge>
        </div>
      </div>

      {/* Empty State */}
      {bioLinks.length === 0 && (
        <motion.div
          initial={{ opacity: 0, scale: 0.95 }}
          animate={{ opacity: 1, scale: 1 }}
          className="mb-4"
        >
          <Card className="border-dashed border-2 bg-gradient-to-br from-muted/50 to-muted/20">
            <CardContent className="flex flex-col items-center justify-center py-12 px-8 text-center">
              <div className="w-16 h-16 rounded-full bg-gradient-to-br from-violet-100 to-indigo-100 flex items-center justify-center mb-4">
                <Link2 className="w-8 h-8 text-violet-600" />
              </div>
              <h3 className="text-lg font-semibold mb-2">No links yet</h3>
              <p className="text-muted-foreground text-center max-w-xs text-sm">
                Start building your bio page by adding your first link below
              </p>
            </CardContent>
          </Card>
        </motion.div>
      )}

      {/* Links List */}
      {bioLinks.length > 0 && (
        <DndContext
          sensors={sensors}
          collisionDetection={closestCenter}
          onDragEnd={handleDragEnd}
        >
          <SortableContext
            items={bioLinks.map(link => link.id)}
            strategy={verticalListSortingStrategy}
          >
            <div className="space-y-3">
              {bioLinks.map((link, index) => (
                <SortableLinkItem
                  key={link.id}
                  link={link}
                  index={index}
                  onEdit={setEditingLink}
                  onDelete={handleDeleteLink}
                />
              ))}
            </div>
          </SortableContext>
        </DndContext>
      )}

      {/* Create Link Form - Inline */}
      <CreateLinkForm
        onCreateLink={handleCreateLink}
        pageId={pageId}
        currentLinksCount={bioLinks.length}
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
