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
    case 'PRODUCT': return 'bg-emerald-500/10 text-emerald-500 border-emerald-500/20'
    case 'VIDEO': return 'bg-red-500/10 text-red-500 border-red-500/20'
    case 'MUSIC': return 'bg-purple-500/10 text-purple-500 border-purple-500/20'
    case 'SOCIAL': return 'bg-blue-500/10 text-blue-500 border-blue-500/20'
    case 'SECTION': return 'bg-amber-500/10 text-amber-500 border-amber-500/20'
    case 'EMBED': return 'bg-pink-500/10 text-pink-500 border-pink-500/20'
    default: return 'bg-slate-500/10 text-slate-500 border-slate-500/20'
  }
}

interface BioLinkManagerProps {
  pageId: string
  links: BioLink[]
  onLinksUpdate: () => void
}

import { Switch } from '@/components/ui/switch'

function SortableLinkItem({ link, onEdit, onDelete, onToggleActive, index }: {
  link: BioLink
  onEdit: (link: BioLink) => void
  onDelete: (linkId: string) => void
  onToggleActive: (linkId: string, isActive: boolean) => void
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
        "group relative flex items-center justify-between p-4 bg-card border border-border/60 rounded-2xl shadow-sm hover:shadow-md transition-all duration-200",
        isDragging && "shadow-2xl scale-[1.02] z-50 ring-2 ring-violet-500/20 rotate-1"
      )}
    >
      <div className="flex items-center gap-4 flex-1 min-w-0">
        {/* Drag Handle */}
        <div
          {...attributes}
          {...listeners}
          className="cursor-grab active:cursor-grabbing p-2 -ml-2 rounded-lg text-muted-foreground/40 hover:text-foreground hover:bg-muted/50 transition-colors"
        >
          <GripVertical className="h-5 w-5" />
        </div>

        {/* Link Icon/Thumbnail */}
        <div className="shrink-0">
          {link.thumbnailUrl ? (
            <div className="h-12 w-12 rounded-xl bg-cover bg-center border shadow-sm" style={{ backgroundImage: `url(${link.thumbnailUrl})` }} />
          ) : (
            <div className={cn(
              "h-12 w-12 rounded-xl flex items-center justify-center border shadow-sm",
              getLinkTypeColor(link.type)
            )}>
              {getLinkTypeIcon(link.type)}
            </div>
          )}
        </div>

        {/* Link Content */}
        <div className="flex flex-col gap-1 min-w-0">
          <div className="flex items-center gap-2">
            <h4 className="font-semibold text-base text-foreground truncate">{link.title}</h4>
            <Badge
              variant="secondary"
              className={cn("text-[10px] px-2 py-0.5 h-auto font-semibold tracking-wide uppercase rounded-md border",
                link.type === 'SOCIAL' ? 'bg-blue-500/10 text-blue-500 border-blue-500/20' :
                  link.type === 'PRODUCT' ? 'bg-emerald-500/10 text-emerald-500 border-emerald-500/20' :
                    link.type === 'VIDEO' ? 'bg-red-500/10 text-red-500 border-red-500/20' :
                      'bg-slate-500/10 text-slate-500 border-slate-500/20'
              )}
            >
              {link.type}
            </Badge>
          </div>

          {link.url && (
            <a
              href={link.url}
              target="_blank"
              rel="noopener noreferrer"
              className="text-sm text-muted-foreground truncate hover:text-primary hover:underline flex items-center gap-1 group-hover/link:text-primary"
            >
              {link.url.replace(/^https?:\/\//, '')}
              <ExternalLink className="w-3 h-3 opacity-0 group-hover:opacity-50 transition-opacity" />
            </a>
          )}
        </div>
      </div>

      {/* Actions */}
      <div className="flex items-center gap-4 pl-6 border-l border-border/50 h-10 ml-4 shrink-0">
        <Switch
          checked={link.isActive}
          onCheckedChange={(checked) => onToggleActive(link.id, checked)}
          className="data-[state=checked]:bg-primary"
        />

        <div className="flex items-center gap-1">
          <Button
            variant="ghost"
            size="icon"
            className="h-9 w-9 text-muted-foreground hover:text-foreground hover:bg-muted rounded-full"
            onClick={() => onEdit(link)}
          >
            <Edit2 className="h-4 w-4" />
          </Button>

          <Button
            variant="ghost"
            size="icon"
            className="h-9 w-9 text-muted-foreground hover:text-red-500 hover:bg-red-500/10 rounded-full"
            onClick={() => onDelete(link.id)}
          >
            <Trash2 className="h-4 w-4" />
          </Button>
        </div>
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

      const { page_id, ...payload } = requestData;
      await bioPageService.createBioLink(page_id, payload);

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

  const handleUpdateLink = async (linkId: string, linkData: any) => {
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

  const handleDeleteLink = async (linkId: string) => {
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
    <div className="space-y-4 pb-20">
      {/* Header */}
      <motion.div
        initial={{ opacity: 0, y: -10 }}
        animate={{ opacity: 1, y: 0 }}
        className="flex items-center justify-between pb-4 border-b"
      >
        <div className="flex items-center gap-3">
          <div className="p-2 bg-primary/10 rounded-xl">
            <Link2 className="w-5 h-5 text-primary" />
          </div>
          <div>
            <h3 className="text-lg font-semibold text-foreground">
              Your Links
            </h3>
            <p className="text-sm text-muted-foreground">
              Manage and organize your links ({bioLinks.length})
            </p>
          </div>
        </div>
        <Badge variant="secondary" className="text-xs">
          Drag to reorder
        </Badge>
      </motion.div>

      {/* Empty State */}
      {bioLinks.length === 0 && (
        <motion.div
          initial={{ opacity: 0, scale: 0.95 }}
          animate={{ opacity: 1, scale: 1 }}
          className="mb-4"
        >
          <Card className="border-dashed border-2 bg-gradient-to-br from-muted/50 to-muted/20">
            <CardContent className="flex flex-col items-center justify-center py-12 px-8 text-center">
              <div className="w-16 h-16 rounded-full bg-primary/10 flex items-center justify-center mb-4">
                <Link2 className="w-8 h-8 text-primary" />
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
                  onToggleActive={(id, isActive) => handleUpdateLink(id, { ...link, isActive })}
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
