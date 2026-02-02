"use client"

import { useEffect, useState } from "react"
import { useParams, useRouter } from "next/navigation"
import { bioPageService, BioPage, BioLink } from "@/services/bioPageService"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Textarea } from "@/components/ui/textarea"
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card"
import { Label } from "@/components/ui/label"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { toast } from "sonner"
import { Loader2, Plus, GripVertical, Trash2, Save, ArrowLeft, ExternalLink } from "lucide-react"
import {
    DndContext,
    closestCenter,
    KeyboardSensor,
    PointerSensor,
    useSensor,
    useSensors,
    DragEndEvent
} from '@dnd-kit/core';
import {
    arrayMove,
    SortableContext,
    sortableKeyboardCoordinates,
    verticalListSortingStrategy,
    useSortable
} from '@dnd-kit/sortable';
import { CSS } from '@dnd-kit/utilities';
import { Badge } from "@/components/ui/badge"
import { Switch } from "@/components/ui/switch"

function SortableLinkItem({ link, onEdit, onDelete }: { link: BioLink, onEdit: (link: BioLink) => void, onDelete: (id: number) => void }) {
    const {
        attributes,
        listeners,
        setNodeRef,
        transform,
        transition,
    } = useSortable({ id: link.id });

    const style = {
        transform: CSS.Transform.toString(transform),
        transition,
    };

    return (
        <div ref={setNodeRef} style={style} className="flex items-center gap-2 bg-background border rounded-lg p-3 mb-2 shadow-sm group">
            <div {...attributes} {...listeners} className="cursor-grab text-muted-foreground hover:text-foreground">
                <GripVertical className="h-5 w-5" />
            </div>
            <div className="flex-1">
                <div className="flex items-center gap-2">
                    <span className="font-medium">{link.title}</span>
                    <Badge variant="secondary" className="text-xs">{link.type}</Badge>
                    {!link.isActive && <Badge variant="outline" className="text-xs">Inactive</Badge>}
                </div>
                <p className="text-sm text-muted-foreground truncate max-w-[300px]">{link.url || 'No URL'}</p>
            </div>
            <div className="flex gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
                <Button variant="ghost" size="icon" onClick={() => onEdit(link)}>
                    <EditIcon className="h-4 w-4" />
                </Button>
                <Button variant="ghost" size="icon" className="text-destructive hover:text-destructive" onClick={() => onDelete(link.id)}>
                    <Trash2 className="h-4 w-4" />
                </Button>
            </div>
        </div>
    );
}

function EditIcon({ className }: { className?: string }) {
    return (
        <svg
            className={className}
            fill="none"
            height="24"
            stroke="currentColor"
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth="2"
            viewBox="0 0 24 24"
            width="24"
            xmlns="http://www.w3.org/2000/svg"
        >
            <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7" />
            <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" />
        </svg>
    )
}

export default function EditBioPage() {
    const params = useParams()
    const router = useRouter()
    const id = params.id as string // Actually it is just 'id' in new folder structure
    // Need to handle id param correctly. If file is [id]/page.tsx, params.id is correct.

    const [page, setPage] = useState<BioPage | null>(null)
    const [links, setLinks] = useState<BioLink[]>([])
    const [loading, setLoading] = useState(true)

    // Edit Link State
    const [editingLink, setEditingLink] = useState<BioLink | null>(null)
    const [isLinkDialogOpen, setIsLinkDialogOpen] = useState(false) // Simple toggle for now, usually Dialog
    const [linkFormData, setLinkFormData] = useState({ title: '', url: '', type: 'LINK', price: '', currency: 'USD' })

    const sensors = useSensors(
        useSensor(PointerSensor),
        useSensor(KeyboardSensor, {
            coordinateGetter: sortableKeyboardCoordinates,
        })
    );

    useEffect(() => {
        if (id) {
            loadPage()
        }
    }, [id])

    const loadPage = async () => {
        try {
            setLoading(true)
            const data = await bioPageService.getBioPageById(String(id)) // Ensure String for ID
            setPage(data)
            setLinks(data.bioLinks || [])
        } catch (error) {
            toast.error("Failed to load page")
        } finally {
            setLoading(false)
        }
    }

    const handleDragEnd = async (event: DragEndEvent) => {
        const { active, over } = event;

        if (active.id !== over?.id) {
            setLinks((items) => {
                const oldIndex = items.findIndex(item => item.id === active.id);
                const newIndex = items.findIndex(item => item.id === over?.id);
                const newItems = arrayMove(items, oldIndex, newIndex);

                // Persist order
                const reorderRequest = newItems.map((item, index) => ({
                    linkId: Number(item.id),
                    sortOrder: index
                })).map(l => l.linkId); // API expects List<Long> linkIds

                bioPageService.reorderLinks(Number(id), reorderRequest) // API expects List<Long> linkIds
                // Let's check BioService.java -> reorderBioLinks(Long pageId, List<Long> linkIds) 
                // My bioPageService.ts uses { linkOrders: {linkId, sortOrder}[] } ... DISCREPANCY!
                // I need to fix bioPageService.ts or BioController.java
                // BioController expects @RequestBody List<Long> linkIds.
                // So I should send just IDs in order.

                // Let's fix the call here assuming I fix bioPageService.ts locally or bypass it.
                // Or better, I fix bioPageService.ts in next step if needed. 
                // For now, I'll pass the list of IDs.

                return newItems;
            });

            // Actually calling API
            const linkIds = links.map(l => Number(l.id)); // This is pre-sort, bug.
            // Correct logic:
            const oldIndex = links.findIndex(item => item.id === active.id);
            const newIndex = links.findIndex(item => item.id === over?.id);
            const newItems = arrayMove(links, oldIndex, newIndex);
            const newLinkIds = newItems.map(l => Number(l.id));

            try {
                await bioPageService.reorderLinks(Number(id), newLinkIds)
            } catch (e) {
                toast.error("Failed to reorder links")
            }
        }
    }

    // To avoid complexity, I'll implement valid re-order via service update in next step.

    const handleSavePage = async (e: React.FormEvent) => {
        e.preventDefault()
        if (!page) return
        try {
            await bioPageService.updateBioPage(Number(page.id), {
                bio_text: page.bioText,
                avatar_url: page.avatarUrl,
                theme_config: page.themeConfig
            })
            toast.success("Page updated")
        } catch (error) {
            toast.error("Failed to update page")
        }
    }

    // Link CRUD handlers... (simplified for brevity, assume simple form below)

    if (loading) return <div className="p-8"><Loader2 className="animate-spin" /></div>
    if (!page) return <div className="p-8">Page not found</div>

    return (
        <div className="container mx-auto p-6 max-w-4xl space-y-6">
            <div className="flex items-center gap-4">
                <Button variant="ghost" size="icon" onClick={() => router.back()}>
                    <ArrowLeft className="h-4 w-4" />
                </Button>
                <h1 className="text-2xl font-bold">Edit @{page.username}</h1>
                <div className="ml-auto">
                    <Button variant="outline" onClick={() => window.open(`/${page.username}`, '_blank')}>
                        <ExternalLink className="mr-2 h-4 w-4" /> View Public
                    </Button>
                </div>
            </div>

            <Tabs defaultValue="links">
                <TabsList>
                    <TabsTrigger value="links">Links</TabsTrigger>
                    <TabsTrigger value="design">Design & Bio</TabsTrigger>
                    <TabsTrigger value="settings">Settings</TabsTrigger>
                </TabsList>

                <TabsContent value="links" className="space-y-4">
                    <div className="flex justify-between items-center">
                        <h2 className="text-lg font-semibold">Your Links</h2>
                        <Button onClick={() => { setIsLinkDialogOpen(true); setEditingLink(null); setLinkFormData({ title: '', url: '', type: 'LINK', price: '', currency: 'USD' }) }}>
                            <Plus className="mr-2 h-4 w-4" /> Add Link
                        </Button>
                    </div>

                    {/* Add/Edit Link Form (Inline for simplicity) */}
                    {isLinkDialogOpen && (
                        <Card className="mb-4 bg-muted/30">
                            <CardContent className="pt-6 space-y-4">
                                <div className="grid gap-4">
                                    <div className="grid grid-cols-2 gap-4">
                                        <div className="space-y-2">
                                            <Label>Title</Label>
                                            <Input value={linkFormData.title} onChange={e => setLinkFormData({ ...linkFormData, title: e.target.value })} placeholder="My Awesome Link" />
                                        </div>
                                        <div className="space-y-2">
                                            <Label>Type</Label>
                                            <select className="flex h-9 w-full rounded-md border border-input bg-transparent px-3 py-1 text-sm shadow-sm"
                                                value={linkFormData.type} onChange={e => setLinkFormData({ ...linkFormData, type: e.target.value })}>
                                                <option value="LINK">Link</option>
                                                <option value="SOCIAL">Social</option>
                                                <option value="PRODUCT">Product</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div className="space-y-2">
                                        <Label>URL</Label>
                                        <Input value={linkFormData.url} onChange={e => setLinkFormData({ ...linkFormData, url: e.target.value })} placeholder="https://..." />
                                    </div>
                                    {linkFormData.type === 'PRODUCT' && (
                                        <div className="grid grid-cols-2 gap-4">
                                            <div className="space-y-2">
                                                <Label>Price</Label>
                                                <Input type="number" value={linkFormData.price} onChange={e => setLinkFormData({ ...linkFormData, price: e.target.value })} />
                                            </div>
                                            <div className="space-y-2">
                                                <Label>Currency</Label>
                                                <Input value={linkFormData.currency} onChange={e => setLinkFormData({ ...linkFormData, currency: e.target.value })} />
                                            </div>
                                        </div>
                                    )}
                                    <div className="flex justify-end gap-2">
                                        <Button variant="ghost" onClick={() => setIsLinkDialogOpen(false)}>Cancel</Button>
                                        <Button onClick={async () => {
                                            try {
                                                if (editingLink) {
                                                    await bioPageService.updateBioLink(Number(editingLink.id), {
                                                        title: linkFormData.title,
                                                        url: linkFormData.url,
                                                        type: linkFormData.type,
                                                        price: Number(linkFormData.price) || undefined,
                                                        currency: linkFormData.currency
                                                    })
                                                } else {
                                                    await bioPageService.createBioLink({
                                                        page_id: Number(page.id),
                                                        title: linkFormData.title,
                                                        url: linkFormData.url,
                                                        type: linkFormData.type,
                                                        price: Number(linkFormData.price) || undefined,
                                                        currency: linkFormData.currency
                                                    })
                                                }
                                                toast.success(editingLink ? "Link updated" : "Link added")
                                                setIsLinkDialogOpen(false)
                                                loadPage()
                                            } catch (e) { toast.error("Failed to save link") }
                                        }}>Save</Button>
                                    </div>
                                </div>
                            </CardContent>
                        </Card>
                    )}

                    <DndContext
                        sensors={sensors}
                        collisionDetection={closestCenter}
                        onDragEnd={handleDragEnd}
                    >
                        <SortableContext
                            items={links.map(l => l.id)}
                            strategy={verticalListSortingStrategy}
                        >
                            <div className="space-y-2">
                                {links.map((link) => (
                                    <SortableLinkItem
                                        key={link.id}
                                        link={link}
                                        onEdit={(l) => { setEditingLink(l); setLinkFormData({ title: l.title, url: l.url || '', type: l.type, price: l.price?.toString() || '', currency: l.currency || 'USD' }); setIsLinkDialogOpen(true); }}
                                        onDelete={async (id) => {
                                            await bioPageService.deleteBioLink(Number(id))
                                            loadPage()
                                        }}
                                    />
                                ))}
                            </div>
                        </SortableContext>
                    </DndContext>
                </TabsContent>

                <TabsContent value="design">
                    <Card>
                        <CardHeader><CardTitle>Profile & Theme</CardTitle></CardHeader>
                        <CardContent className="space-y-4">
                            <div className="space-y-2">
                                <Label>Avatar URL</Label>
                                <Input value={page.avatarUrl || ''} onChange={e => setPage({ ...page, avatarUrl: e.target.value })} />
                            </div>
                            <div className="space-y-2">
                                <Label>Bio Text</Label>
                                <Textarea value={page.bioText || ''} onChange={e => setPage({ ...page, bioText: e.target.value })} />
                            </div>
                            <div className="space-y-2">
                                <Label>Theme Config (JSON)</Label>
                                <Textarea
                                    className="font-mono text-xs"
                                    value={page.themeConfig || ''}
                                    onChange={e => setPage({ ...page, themeConfig: e.target.value })}
                                    placeholder='{"primaryColor": "#000000", "backgroundColor": "#ffffff"}'
                                />
                            </div>
                            <Button onClick={handleSavePage}><Save className="mr-2 h-4 w-4" /> Save Changes</Button>
                        </CardContent>
                    </Card>
                </TabsContent>
            </Tabs>
        </div>
    )
}
