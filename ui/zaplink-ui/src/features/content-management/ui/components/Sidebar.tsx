import { FolderIcon, Star, Trash2, ChevronRight, ChevronDown, FileIcon, ImageIcon, VideoIcon } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Card, CardContent } from '@/components/ui/card';
import { Folder, MediaItem, ViewTab } from '../../types';
import { motion, AnimatePresence } from 'framer-motion';
import { cn } from '@/lib/utils';
import { useState, useMemo } from 'react';

interface SidebarProps {
    folders: Folder[]; // This is now allTreeFolders (flat list)
    media: MediaItem[]; // flat list of all media
    currentFolderId?: string;
    activeTab: ViewTab;
    onNavigate: (folderId?: string) => void;
    onTabChange: (tab: ViewTab) => void;
}

// Helper to build a tree from the flat list
interface TreeNode {
    id: string;
    name: string;
    isFolder: boolean;
    children?: TreeNode[];
    item: Folder | MediaItem;
}

function buildTree(folders: Folder[], media: MediaItem[]): TreeNode[] {
    const map = new Map<string, TreeNode>();
    const roots: TreeNode[] = [];

    folders.forEach(f => {
        map.set(f.id, { id: f.id, name: f.name, isFolder: true, children: [], item: f });
    });

    folders.forEach(f => {
        const node = map.get(f.id)!;
        if (f.parent && f.parent.id) {
            const parentNode = map.get(f.parent.id);
            if (parentNode && parentNode.children) {
                parentNode.children.push(node);
            } else {
                roots.push(node); // Parent not in list
            }
        } else {
            roots.push(node);
        }
    });

    media.forEach(m => {
        const node: TreeNode = { id: m.id, name: m.name, isFolder: false, item: m };
        // Backend returns either folderId or a nested folder object
        const mediaFolderId = m.folderId || (m as any).folder?.id;

        if (mediaFolderId) {
            const parentNode = map.get(mediaFolderId);
            if (parentNode && parentNode.children) {
                parentNode.children.push(node);
            } else {
                // If parent isn't found in current tree chunk, 
                // gracefully skip or append to root depending on requirements.
                // Usually it shouldn't be added to roots if it belongs to a folder.
            }
        } else {
            roots.push(node); // It's truly a root media item
        }
    });

    // Sort children
    const sortNodes = (nodes: TreeNode[]) => {
        nodes.sort((a, b) => {
            if (a.isFolder && !b.isFolder) return -1;
            if (!a.isFolder && b.isFolder) return 1;
            return a.name.localeCompare(b.name);
        });
        nodes.forEach(n => {
            if (n.children) sortNodes(n.children);
        });
    };
    sortNodes(roots);

    return roots;
}

// Recursive Tree Node Component
const TreeNodeComponent = ({
    node,
    level = 0,
    currentFolderId,
    onNavigate
}: {
    node: TreeNode,
    level?: number,
    currentFolderId?: string,
    onNavigate: (id: string, isFolder: boolean) => void
}) => {
    // Determine if we should be auto-expanded because we or a child is the current folder
    const hasActiveDescendant = useMemo(() => {
        const checkActive = (n: TreeNode): boolean => {
            if (n.id === currentFolderId) return true;
            if (n.children) return n.children.some(checkActive);
            return false;
        };
        return checkActive(node);
    }, [node, currentFolderId]);

    const [isExpanded, setIsExpanded] = useState(hasActiveDescendant);
    const isActive = currentFolderId === node.id;
    const hasChildren = node.isFolder && node.children && node.children.length > 0;

    const Icon = () => {
        if (node.isFolder) return <FolderIcon className={cn("w-4 h-4 mr-2 shrink-0", isActive ? "text-primary fill-primary/20" : "")} />;
        const m = node.item as MediaItem;
        if (m.type?.startsWith('image/')) return <ImageIcon className="w-4 h-4 mr-2 shrink-0 opacity-70" />;
        if (m.type?.startsWith('video/')) return <VideoIcon className="w-4 h-4 mr-2 shrink-0 opacity-70" />;
        return <FileIcon className="w-4 h-4 mr-2 shrink-0 opacity-70" />;
    };

    return (
        <div className="flex flex-col min-w-0 w-full overflow-hidden">
            <div
                className={cn(
                    "flex items-center py-1.5 px-2 rounded-md cursor-pointer hover:bg-muted/50 transition-colors group min-w-0 w-full",
                    isActive ? "bg-secondary text-secondary-foreground" : "text-muted-foreground"
                )}
                style={{ paddingLeft: `${(level * 12) + 8}px` }}
                onClick={() => onNavigate(node.id, node.isFolder)}
            >
                <div
                    className="w-4 h-4 mr-1.5 flex items-center justify-center shrink-0"
                    onClick={(e) => {
                        if (hasChildren) {
                            e.stopPropagation();
                            setIsExpanded(!isExpanded);
                        }
                    }}
                >
                    {node.isFolder && (
                        isExpanded ?
                            <ChevronDown className="w-3.5 h-3.5 opacity-40 group-hover:opacity-100 transition-opacity" /> :
                            <ChevronRight className="w-3.5 h-3.5 opacity-40 group-hover:opacity-100 transition-opacity" />
                    )}
                </div>
                <Icon />
                <span className="truncate text-sm flex-1 break-all">{node.name}</span>
            </div>

            <AnimatePresence initial={false}>
                {isExpanded && node.children && (
                    <motion.div
                        initial={{ height: 0, opacity: 0 }}
                        animate={{ height: "auto", opacity: 1 }}
                        exit={{ height: 0, opacity: 0 }}
                        transition={{ duration: 0.2, ease: "easeInOut" }}
                        className="overflow-hidden"
                    >
                        {node.children.map(child => (
                            <TreeNodeComponent
                                key={child.id}
                                node={child}
                                level={level + 1}
                                currentFolderId={currentFolderId}
                                onNavigate={onNavigate}
                            />
                        ))}
                    </motion.div>
                )}
            </AnimatePresence>
        </div>
    );
};

export function Sidebar({ folders, media, currentFolderId, activeTab, onNavigate, onTabChange }: SidebarProps) {
    const tree = useMemo(() => buildTree(folders, media), [folders, media]);

    const handleNodeNavigate = (id: string, isFolder: boolean) => {
        if (isFolder) {
            onNavigate(id);
        } else {
            // For now, if clicking a file in the sidebar, we just make sure we go to its folder.
            const clickedMedia = media.find(m => m.id === id);
            if (clickedMedia && clickedMedia.folderId) {
                onNavigate(clickedMedia.folderId);
            }
        }
    };

    return (
        <Card className="w-full md:w-64 h-full bg-card/50 backdrop-blur border-border/50 hidden md:flex flex-col">
            <CardContent className="p-4 flex flex-col h-full gap-6">

                {/* Views Section */}
                <div className="space-y-1">
                    <h4 className="text-xs font-semibold text-muted-foreground uppercase tracking-wider mb-2 px-2 border-b border-border/50 pb-2">Navigation</h4>

                    <Button
                        variant={activeTab === 'all' && currentFolderId === undefined ? "secondary" : "ghost"}
                        className="w-full justify-start h-9 px-2"
                        onClick={() => { onTabChange('all'); onNavigate(undefined); }}
                    >
                        <FolderIcon className={cn("w-4 h-4 mr-2", activeTab === 'all' && currentFolderId === undefined ? "text-primary fill-primary/20" : "text-muted-foreground")} />
                        <span className="truncate">Root</span>
                    </Button>

                    <Button
                        variant={activeTab === 'favorites' ? "secondary" : "ghost"}
                        className="w-full justify-start h-9 px-2"
                        onClick={() => { onTabChange('favorites'); onNavigate(undefined); }}
                    >
                        <Star className={cn("w-4 h-4 mr-2", activeTab === 'favorites' ? "text-yellow-500 fill-yellow-500" : "text-muted-foreground")} />
                        Favorites
                    </Button>

                    <Button
                        variant={activeTab === 'trash' ? "secondary" : "ghost"}
                        className="w-full justify-start h-9 px-2"
                        onClick={() => { onTabChange('trash'); onNavigate(undefined); }}
                    >
                        <Trash2 className={cn("w-4 h-4 mr-2", activeTab === 'trash' ? "text-destructive" : "text-muted-foreground")} />
                        Trash
                    </Button>
                </div>

                {/* Folder Tree - Always visible, slightly dimmed when in special views */}
                <motion.div
                    initial={{ opacity: 0, height: 0 }}
                    animate={{ opacity: 1, height: 'auto' }}
                    className={`flex-1 overflow-y-auto min-h-0 flex flex-col border-t border-border/50 pt-4 transition-opacity ${activeTab !== 'all' ? 'opacity-40 pointer-events-none' : 'opacity-100'}`}
                >
                    <h4 className="text-xs font-semibold text-muted-foreground uppercase tracking-wider mb-2 px-2">Workspace</h4>

                    <div className="flex-1 overflow-y-auto overflow-x-hidden no-scrollbar pb-4 min-w-0">
                        {tree.length === 0 ? (
                            <p className="text-xs text-muted-foreground px-2 italic">Workspace empty</p>
                        ) : (
                            tree.map(node => (
                                <TreeNodeComponent
                                    key={node.id}
                                    node={node}
                                    currentFolderId={currentFolderId}
                                    onNavigate={handleNodeNavigate}
                                />
                            ))
                        )}
                    </div>
                </motion.div>
            </CardContent>
        </Card>
    );
}
