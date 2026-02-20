"use client"

import React, { useState, useEffect } from 'react';
import {
    FolderPlus, Upload, LayoutGrid, List, Search,
    MoreVertical, Star, Trash2, Folder as FolderIcon,
    Image as ImageIcon, File as FileIcon, Move, Edit2
} from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Card, CardContent } from '@/components/ui/card';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { ContentApi } from '../api/contentApi';

export default function ContentManager() {
    const [folders, setFolders] = useState<any[]>([]);
    const [media, setMedia] = useState<any[]>([]);
    const [currentFolderId, setCurrentFolderId] = useState<string | undefined>(undefined);
    const [viewMode, setViewMode] = useState<'grid' | 'list'>('grid');
    const [searchQuery, setSearchQuery] = useState('');

    // Fetch data
    const loadContent = async (folderId?: string) => {
        try {
            const [fRes, mRes] = await Promise.all([
                ContentApi.listFolders(folderId),
                ContentApi.listMedia(folderId)
            ]);
            setFolders(fRes.data || []);
            setMedia(mRes.data || []);
        } catch (e) {
            console.error("Failed to load content", e);
        }
    };

    useEffect(() => {
        loadContent(currentFolderId);
    }, [currentFolderId]);

    return (
        <div className="flex flex-col md:flex-row h-[70vh] gap-6">
            {/* Sidebar Folder Tree */}
            <Card className="w-full md:w-1/4 h-full bg-card/50 backdrop-blur border-border/50">
                <CardContent className="p-4 flex flex-col h-full gap-4">
                    <div className="flex justify-between items-center">
                        <h3 className="font-semibold text-lg flex items-center gap-2">
                            <FolderIcon className="w-5 h-5 text-primary" /> Folders
                        </h3>
                        <Button variant="ghost" size="icon"><FolderPlus className="w-4 h-4" /></Button>
                    </div>
                    <div className="flex-1 overflow-y-auto space-y-2">
                        <Button
                            variant={currentFolderId === undefined ? "secondary" : "ghost"}
                            className="w-full justify-start"
                            onClick={() => setCurrentFolderId(undefined)}
                        >
                            <FolderIcon className="w-4 h-4 mr-2" /> Root
                        </Button>
                        {folders.map(f => (
                            <Button
                                key={f.id}
                                variant={currentFolderId === f.id ? "secondary" : "ghost"}
                                className="w-full justify-start ml-4"
                                onClick={() => setCurrentFolderId(f.id)}
                            >
                                <FolderIcon className="w-4 h-4 mr-2 text-muted-foreground" /> {f.name}
                            </Button>
                        ))}
                    </div>
                </CardContent>
            </Card>

            {/* Main Content Area */}
            <Card className="flex-1 flex flex-col bg-card/50 backdrop-blur border-border/50 overflow-hidden">
                <div className="p-4 border-b border-border/50 flex flex-wrap gap-4 items-center justify-between">
                    <div className="relative w-full md:w-64">
                        <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
                        <Input
                            placeholder="Search content..."
                            className="pl-8 bg-background/50"
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                        />
                    </div>
                    <div className="flex items-center gap-2">
                        <Tabs value={viewMode} onValueChange={(v: any) => setViewMode(v)}>
                            <TabsList className="grid w-full grid-cols-2">
                                <TabsTrigger value="grid"><LayoutGrid className="w-4 h-4" /></TabsTrigger>
                                <TabsTrigger value="list"><List className="w-4 h-4" /></TabsTrigger>
                            </TabsList>
                        </Tabs>
                        <Button><Upload className="w-4 h-4 mr-2" /> Upload</Button>
                    </div>
                </div>

                <CardContent className="flex-1 p-6 overflow-y-auto">
                    {media.length === 0 && folders.length === 0 ? (
                        <div className="h-full flex flex-col items-center justify-center text-muted-foreground">
                            <ImageIcon className="w-12 h-12 mb-4 opacity-50" />
                            <p>This folder is empty.</p>
                            <Button variant="outline" className="mt-4"><Upload className="w-4 h-4 mr-2" /> Upload Media</Button>
                        </div>
                    ) : (
                        <div className={viewMode === 'grid' ? "grid grid-cols-2 md:grid-cols-4 gap-4" : "flex flex-col gap-2"}>
                            {/* Render Subfolders in main view too */}
                            {folders.map(f => (
                                <Card key={f.id} className="cursor-pointer hover:bg-muted/50 transition flex items-center p-4 gap-3">
                                    <FolderIcon className="w-8 h-8 text-blue-500" />
                                    <div className="flex-1 overflow-hidden text-ellipsis whitespace-nowrap font-medium">{f.name}</div>
                                    <Button variant="ghost" size="icon"><MoreVertical className="w-4 h-4" /></Button>
                                </Card>
                            ))}
                            {/* Render Media */}
                            {media.filter(m => m.name.toLowerCase().includes(searchQuery.toLowerCase())).map(m => (
                                <Card key={m.id} className="group relative overflow-hidden cursor-pointer hover:border-primary/50 transition">
                                    {viewMode === 'grid' ? (
                                        <>
                                            <div className="aspect-square bg-muted/30 flex items-center justify-center relative">
                                                {m.type?.startsWith('image/') ? (
                                                    <img src={m.url} alt={m.name} className="object-cover w-full h-full" />
                                                ) : (
                                                    <FileIcon className="w-12 h-12 text-muted-foreground" />
                                                )}
                                                <div className="absolute top-2 right-2 opacity-0 group-hover:opacity-100 transition flex gap-1">
                                                    <Button variant="secondary" size="icon" className="h-7 w-7"><Star className="w-3 h-3" /></Button>
                                                    <Button variant="destructive" size="icon" className="h-7 w-7"><Trash2 className="w-3 h-3" /></Button>
                                                </div>
                                            </div>
                                            <div className="p-3">
                                                <p className="text-sm font-medium truncate">{m.name}</p>
                                                <p className="text-xs text-muted-foreground mt-1">{(m.size / 1024 / 1024).toFixed(2)} MB</p>
                                            </div>
                                        </>
                                    ) : (
                                        <div className="flex items-center p-3 gap-4">
                                            <div className="w-10 h-10 bg-muted/30 rounded flex items-center justify-center">
                                                {m.type?.startsWith('image/') ? (
                                                    <img src={m.url} alt={m.name} className="object-cover w-full h-full rounded" />
                                                ) : (
                                                    <FileIcon className="w-5 h-5 text-muted-foreground" />
                                                )}
                                            </div>
                                            <div className="flex-1">
                                                <p className="text-sm font-medium truncate">{m.name}</p>
                                            </div>
                                            <div className="w-24 text-right text-xs text-muted-foreground">{(m.size / 1024 / 1024).toFixed(2)} MB</div>
                                            <Button variant="ghost" size="icon"><MoreVertical className="w-4 h-4" /></Button>
                                        </div>
                                    )}
                                </Card>
                            ))}
                        </div>
                    )}
                </CardContent>
            </Card>
        </div>
    );
}
