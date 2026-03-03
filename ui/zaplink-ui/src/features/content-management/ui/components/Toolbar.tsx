import { useRef, useCallback } from 'react';
import { LayoutGrid, List, Search, Upload, FolderPlus, CheckSquare, Square, ChevronLeft, ChevronRight } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Tabs, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { ViewMode } from '../../types';

interface ToolbarProps {
    searchQuery: string;
    onSearchChange: (query: string) => void;
    viewMode: ViewMode;
    onViewModeChange: (mode: ViewMode) => void;
    onUploadClick: () => void;
    onCreateFolderClick: () => void;
    isReadOnly?: boolean;
    folderPath?: { id: string; name: string }[];
    onNavigate?: (folderId?: string) => void;
    isSelectMode?: boolean;
    onToggleSelectMode?: () => void;
}

export function Toolbar({
    searchQuery,
    onSearchChange,
    viewMode,
    onViewModeChange,
    onUploadClick,
    onCreateFolderClick,
    isReadOnly = false,
    folderPath = [],
    onNavigate,
    isSelectMode = false,
    onToggleSelectMode,
}: ToolbarProps) {

    // Navigation history for forward traversal
    const forwardStack = useRef<(string | undefined)[]>([]);

    const canGoBack = folderPath.length > 0;
    const canGoForward = forwardStack.current.length > 0;

    const handleBack = useCallback(() => {
        if (!canGoBack || !onNavigate) return;
        // Push current folder to forward stack
        const currentFolderId = folderPath[folderPath.length - 1]?.id;
        forwardStack.current.push(currentFolderId);
        // Navigate to parent
        if (folderPath.length <= 1) {
            onNavigate(undefined); // Go to root
        } else {
            onNavigate(folderPath[folderPath.length - 2].id);
        }
    }, [canGoBack, folderPath, onNavigate]);

    const handleForward = useCallback(() => {
        if (!canGoForward || !onNavigate) return;
        const nextFolderId = forwardStack.current.pop();
        onNavigate(nextFolderId);
    }, [canGoForward, onNavigate]);

    // Clear forward stack when user navigates manually (via breadcrumb or folder click)
    const handleBreadcrumbNavigate = useCallback((folderId?: string) => {
        forwardStack.current = [];
        onNavigate?.(folderId);
    }, [onNavigate]);

    return (
        <div className="p-3 sm:p-4 border-b border-border/50 flex flex-wrap gap-2 sm:gap-4 items-center justify-between bg-card/30">

            <div className="flex items-center gap-1 sm:gap-2 flex-grow overflow-hidden mr-2 sm:mr-4">
                {/* Back / Forward */}
                <div className="flex items-center gap-0.5 shrink-0">
                    <Button
                        variant="ghost"
                        size="icon"
                        className="h-7 w-7 sm:h-8 sm:w-8 rounded-lg"
                        onClick={handleBack}
                        disabled={!canGoBack}
                        title="Go Back"
                    >
                        <ChevronLeft className="w-4 h-4" />
                    </Button>
                    <Button
                        variant="ghost"
                        size="icon"
                        className="h-7 w-7 sm:h-8 sm:w-8 rounded-lg"
                        onClick={handleForward}
                        disabled={!canGoForward}
                        title="Go Forward"
                    >
                        <ChevronRight className="w-4 h-4" />
                    </Button>
                </div>

                {/* Breadcrumbs */}
                <div className="flex items-center text-xs sm:text-sm text-foreground font-medium whitespace-nowrap overflow-x-auto no-scrollbar mask-gradient-right pb-1">
                    <button
                        onClick={() => handleBreadcrumbNavigate(undefined)}
                        className="hover:text-primary transition-colors cursor-pointer"
                    >
                        Root
                    </button>
                    {folderPath.map((folder, index) => (
                        <div key={folder.id} className="flex items-center">
                            <span className="mx-1.5 sm:mx-2 text-muted-foreground/50">/</span>
                            <button
                                onClick={() => index < folderPath.length - 1 && handleBreadcrumbNavigate(folder.id)}
                                className={`transition-colors truncate max-w-[100px] sm:max-w-[150px] ${index === folderPath.length - 1
                                    ? 'text-foreground cursor-default'
                                    : 'text-muted-foreground hover:text-primary transition-colors cursor-pointer'
                                    }`}
                                title={folder.name}
                            >
                                {folder.name}
                            </button>
                        </div>
                    ))}
                </div>
            </div>

            <div className="flex items-center gap-2 sm:gap-3 ml-auto flex-wrap">
                <div className="relative hidden md:block w-48 lg:w-64">
                    <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
                    <Input
                        placeholder="Search content..."
                        className="pl-8 bg-background/50 h-9"
                        value={searchQuery}
                        onChange={(e) => onSearchChange(e.target.value)}
                    />
                </div>

                <div className="flex items-center gap-2 sm:gap-3">
                    <Tabs value={viewMode} onValueChange={(v: string) => onViewModeChange(v as ViewMode)}>
                        <TabsList className="h-8 sm:h-9">
                            <TabsTrigger value="grid" className="px-2.5 sm:px-3"><LayoutGrid className="w-3.5 h-3.5 sm:w-4 sm:h-4" /></TabsTrigger>
                            <TabsTrigger value="list" className="px-2.5 sm:px-3"><List className="w-3.5 h-3.5 sm:w-4 sm:h-4" /></TabsTrigger>
                        </TabsList>
                    </Tabs>

                    {onToggleSelectMode && (
                        <Button
                            variant={isSelectMode ? 'default' : 'outline'}
                            size="sm"
                            className="h-8 sm:h-9 gap-1.5"
                            onClick={onToggleSelectMode}
                            title={isSelectMode ? 'Exit selection mode' : 'Select items'}
                        >
                            {isSelectMode ? <CheckSquare className="w-4 h-4" /> : <Square className="w-4 h-4" />}
                            <span className="hidden sm:inline">{isSelectMode ? 'Selected' : 'Select All'}</span>
                        </Button>
                    )}

                    <div className="flex gap-1.5 sm:gap-2 pl-2 border-l border-border/50">
                        <Button
                            variant="secondary"
                            size="sm"
                            onClick={onCreateFolderClick}
                            disabled={isReadOnly}
                            className="h-8 sm:h-9 px-2 sm:px-3"
                        >
                            <FolderPlus className="w-4 h-4 sm:mr-2" />
                            <span className="hidden sm:inline">New Folder</span>
                        </Button>
                        <Button
                            size="sm"
                            onClick={onUploadClick}
                            disabled={isReadOnly}
                            className="h-8 sm:h-9 px-2 sm:px-3"
                        >
                            <Upload className="w-4 h-4 sm:mr-2" />
                            <span className="hidden sm:inline">Upload</span>
                        </Button>
                    </div>
                </div>
            </div>
        </div>
    );
}
