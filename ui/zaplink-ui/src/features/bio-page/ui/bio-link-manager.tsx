"use client"

import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { transformFormDataToApiRequest } from '@/features/bio-page/lib/form-data-transformer'
import { CreateLinkForm } from '@/features/bio-page/ui/create-link-form'
import { EditBioLinkDialog } from '@/features/bio-page/ui/edit-bio-link-dialog'
import { cn } from '@/lib/utils'
import { BioLink, bioPageService } from '@/services/bioPageService'
import {
  closestCenter,
  DndContext,
  DragEndEvent,
  KeyboardSensor,
  PointerSensor,
  useSensor,
  useSensors,
} from '@dnd-kit/core'
import {
  arrayMove,
  SortableContext,
  sortableKeyboardCoordinates,
  useSortable,
  verticalListSortingStrategy,
} from '@dnd-kit/sortable'
import { restrictToVerticalAxis } from '@dnd-kit/modifiers'
import { CSS } from '@dnd-kit/utilities'
import { motion } from 'framer-motion'
import {
  Edit2,
  ExternalLink,
  FileText,
  GripVertical,
  Link2,
  Music,
  Package,
  ShoppingBag,
  Trash2,
  Video
} from 'lucide-react'
import { useEffect, useState } from 'react'
import { toast } from 'sonner'

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

// Extract domain from URL for favicon fetching
function getDomainFromUrl(url: string | undefined): string | null {
  if (!url) return null;
  try {
    const parsed = new URL(url.startsWith('http') ? url : `https://${url}`);
    return parsed.hostname;
  } catch {
    return null;
  }
}

// Component to handle fallback from Favicon -> Default Icon
function LinkIconWithFallback({ link }: { link: BioLink }) {
  const [imgError, setImgError] = useState(false);
  const domain = getDomainFromUrl(link.url);

  if (domain && !imgError) {
    const faviconUrl = `https://www.google.com/s2/favicons?domain=${domain}&sz=128`;
    return (
      <div className="h-14 w-14 rounded-[14px] flex items-center justify-center border shadow-sm ring-1 ring-border/50 bg-background overflow-hidden relative">
        <img
          src={faviconUrl}
          alt={domain}
          className="w-8 h-8 object-contain"
          onError={() => setImgError(true)}
        />
      </div>
    );
  }

  // Fallback to generic icon type
  return (
    <div className={cn(
      "h-10 w-10 sm:h-14 sm:w-14 rounded-[10px] sm:rounded-[14px] flex items-center justify-center border shadow-sm ring-1 ring-border/50 transition-colors",
      getLinkTypeColor(link.type)
    )}>
      <div className="scale-75 sm:scale-100">
        {getLinkTypeIcon(link.type)}
      </div>
    </div>
  );
}

interface BioLinkManagerProps {
  pageId: string
  links: BioLink[]
  onLinksUpdate: () => void
  onLinksReorder?: (newLinks: BioLink[]) => void
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
    <div
      ref={setNodeRef}
      style={style}
      className={cn(
        "group relative flex items-center justify-between p-3 sm:p-4 mb-2 sm:mb-3 bg-card border rounded-md sm:rounded-xl shadow-sm hover:shadow-md transition-all duration-300",
        isDragging && "shadow-2xl scale-[1.02] z-50 ring-2 ring-primary/20 rotate-1 border-primary/30 bg-card/90 backdrop-blur-sm"
      )}
    >
      <div className="flex items-center gap-2 sm:gap-4 flex-1 min-w-0">
        {/* Drag Handle */}
        <div
          {...attributes}
          {...listeners}
          className="cursor-grab active:cursor-grabbing p-2.5 -ml-2 rounded-xl text-muted-foreground/40 hover:text-foreground hover:bg-muted/80 transition-colors touch-none"
        >
          <GripVertical className="h-5 w-5" />
        </div>

        {/* Link Icon/Thumbnail */}
        <div className="shrink-0 relative">
          {link.thumbnailUrl ? (
            <div className="h-10 w-10 sm:h-14 sm:w-14 rounded-[10px] sm:rounded-[14px] bg-cover bg-center border shadow-sm ring-1 ring-border/50" style={{ backgroundImage: `url(${link.thumbnailUrl})` }} />
          ) : (
            <LinkIconWithFallback link={link} />
          )}
          {/* Active indicator dot on top left */}
          <div className={cn(
            "absolute -top-1 -left-1 w-3.5 h-3.5 rounded-full border-2 border-background shadow-sm transition-colors",
            link.isActive ? "bg-emerald-500" : "bg-muted-foreground/30"
          )} />
        </div>

        {/* Link Content */}
        <div className="flex flex-col gap-0.5 sm:gap-1.5 min-w-0">
          <div className="flex items-center gap-2 sm:gap-2.5">
            <h4 className="font-semibold text-[13px] sm:text-[15px] text-foreground truncate leading-none">{link.title || 'Untitled Link'}</h4>
            <Badge
              variant="secondary"
              className={cn("text-[8px] sm:text-[9px] px-1.5 sm:px-2 py-0 h-[16px] sm:h-[18px] font-bold tracking-wider uppercase rounded sm:rounded-md border transition-colors",
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
              className="text-[11px] sm:text-[13px] text-muted-foreground truncate hover:text-primary transition-colors flex items-center gap-1 sm:gap-1.5 group/link mt-0.5"
            >
              {link.url.replace(/^https?:\/\//, '')}
              <ExternalLink className="w-2.5 h-2.5 sm:w-3 sm:h-3 opacity-0 group-hover/link:opacity-100 transition-all -ml-2 group-hover/link:ml-0" />
            </a>
          )}
        </div>
      </div>

      {/* Actions */}
      <div className="flex items-center gap-1.5 sm:gap-3 pl-2 sm:pl-5 border-l border-border/60 h-8 sm:h-10 ml-2 sm:ml-4 shrink-0">
        <Switch
          checked={link.isActive}
          onCheckedChange={(checked) => onToggleActive(link.id, checked)}
          className="data-[state=checked]:bg-emerald-500 shadow-sm scale-75 sm:scale-100"
        />

        <div className="flex items-center gap-1">
          <Button
            variant="ghost"
            size="icon"
            className="h-9 w-9 text-muted-foreground hover:text-primary hover:bg-primary/10 rounded-xl transition-colors"
            onClick={() => onEdit(link)}
          >
            <Edit2 className="h-4 w-4" />
          </Button>

          <Button
            variant="ghost"
            size="icon"
            className="h-9 w-9 text-muted-foreground hover:text-red-500 hover:bg-red-500/10 rounded-xl transition-colors"
            onClick={() => onDelete(link.id)}
          >
            <Trash2 className="h-4 w-4" />
          </Button>
        </div>
      </div>
    </div>
  )
}

export function BioLinkManager({ pageId, links, onLinksUpdate, onLinksReorder }: BioLinkManagerProps) {
  // Use local state for immediate optimistic updates during drag and drop
  const [localLinks, setLocalLinks] = useState<BioLink[]>([...links].sort((a, b) => a.sortOrder - b.sortOrder))
  const [editingLink, setEditingLink] = useState<BioLink | null>(null)

  // Sync with parent when remote links update
  useEffect(() => {
    setLocalLinks([...links].sort((a, b) => a.sortOrder - b.sortOrder))
  }, [links])

  const sensors = useSensors(
    useSensor(PointerSensor),
    useSensor(KeyboardSensor, {
      coordinateGetter: sortableKeyboardCoordinates,
    })
  )

  const handleDragEnd = async (event: DragEndEvent) => {
    const { active, over } = event

    if (active.id !== over?.id) {
      const oldIndex = localLinks.findIndex(link => link.id === active.id)
      const newIndex = localLinks.findIndex(link => link.id === over?.id)

      const newLinks = arrayMove(localLinks, oldIndex, newIndex)

      // Optimistically update the UI to prevent snapping back
      setLocalLinks(newLinks)

      if (onLinksReorder) {
        onLinksReorder(newLinks);
        return;
      }

      try {
        await bioPageService.reorderLinks(pageId, newLinks.map(l => l.id));
        onLinksUpdate()
        toast.success("Success", {
          description: "Links reordered successfully",
        })
      } catch (error) {
        // Revert local state on error
        setLocalLinks([...links].sort((a, b) => a.sortOrder - b.sortOrder))
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
      const requestData = transformFormDataToApiRequest(linkData, pageId, localLinks.length);

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
    <div className="space-y-8 pb-32 max-w-4xl mx-auto mt-4 px-2 sm:px-0">
      {/* Premium Header */}
      <motion.div
        initial={{ opacity: 0, y: -10 }}
        animate={{ opacity: 1, y: 0 }}
        className="flex items-center justify-between pb-4"
      >
        <div className="flex items-center gap-4">
          <div className="p-3 bg-gradient-to-br from-primary/20 to-primary/5 rounded-2xl shadow-sm border border-primary/10">
            <Link2 className="w-6 h-6 text-primary" />
          </div>
          <div>
            <h3 className="text-xl sm:text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-foreground to-foreground/70 tracking-tight">
              Links & Content
            </h3>
            <p className="text-sm text-muted-foreground mt-1">
              Add, organize, and manage your amazing content ({localLinks.length})
            </p>
          </div>
        </div>

        {localLinks.length > 0 && (
          <Badge variant="outline" className="hidden sm:inline-flex text-[10px] font-bold tracking-widest uppercase bg-card/40 backdrop-blur-md shadow-sm border-border/60 text-muted-foreground px-4 py-1.5 rounded-full cursor-help">
            <GripVertical className="w-3 h-3 mr-1 opacity-50" /> Drag handles to reorder
          </Badge>
        )}
      </motion.div>

      {/* Empty State */}
      {localLinks.length === 0 && (
        <motion.div
          initial={{ opacity: 0, scale: 0.98 }}
          animate={{ opacity: 1, scale: 1 }}
          className="mb-10"
        >
          <div className="relative rounded-xl border border-border/50 bg-card/40 backdrop-blur-2xl shadow-xl overflow-hidden group hover:border-primary/30 transition-colors duration-500">
            {/* Decorative background glow */}
            <div className="absolute inset-0 bg-gradient-to-b from-primary/5 to-transparent pointer-events-none" />

            <div className="flex flex-col items-center justify-center py-20 px-8 text-center relative z-10">
              <div className="relative w-24 h-24 mb-6">
                {/* Animated rings */}
                <div className="absolute inset-0 rounded-full border border-primary/20 animate-[ping_3s_cubic-bezier(0,0,0.2,1)_infinite]" />
                <div className="absolute inset-2 rounded-full border border-primary/30 animate-[ping_3s_cubic-bezier(0,0,0.2,1)_infinite_1s]" />

                <div className="absolute inset-0 rounded-full bg-primary/10 blur-xl group-hover:bg-primary/20 transition-all duration-500" />
                <div className="relative w-full h-full rounded-full bg-gradient-to-br from-background to-muted flex items-center justify-center shadow-lg border border-border/50">
                  <Link2 className="w-10 h-10 text-primary drop-shadow-md" />
                </div>
              </div>

              <h3 className="text-xl sm:text-2xl font-bold mb-2 sm:mb-3 tracking-tight text-foreground">A Blank Canvas Awaits</h3>
              <p className="text-muted-foreground text-center max-w-sm text-sm sm:text-base leading-relaxed">
                Add your very first link below to kickstart your personalized, beautiful bio page experience.
              </p>
            </div>
          </div>
        </motion.div>
      )}

      {/* Links List */}
      {localLinks.length > 0 && (
        <DndContext
          sensors={sensors}
          collisionDetection={closestCenter}
          onDragEnd={handleDragEnd}
          modifiers={[restrictToVerticalAxis]}
        >
          <SortableContext
            items={localLinks.map(link => link.id)}
            strategy={verticalListSortingStrategy}
          >
            <div className="space-y-4 mb-10">
              {localLinks.map((link, index) => (
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
        currentLinksCount={localLinks.length}
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
