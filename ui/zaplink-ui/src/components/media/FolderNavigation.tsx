"use client"

import {
    Breadcrumb,
    BreadcrumbItem,
    BreadcrumbLink,
    BreadcrumbList,
    BreadcrumbPage,
    BreadcrumbSeparator,
} from "@/components/ui/breadcrumb"
import { Home } from "lucide-react"

interface Folder {
    id: string;
    name: string;
}

interface FolderNavigationProps {
    path: Folder[];
    onNavigate: (folderId: string | undefined) => void;
}

export function FolderNavigation({ path, onNavigate }: FolderNavigationProps) {
    return (
        <Breadcrumb>
            <BreadcrumbList>
                <BreadcrumbItem>
                    <BreadcrumbLink
                        className="cursor-pointer"
                        onClick={() => onNavigate(undefined)} // Root
                    >
                        <Home className="h-4 w-4" />
                    </BreadcrumbLink>
                </BreadcrumbItem>
                {path.length > 0 && <BreadcrumbSeparator />}

                {path.map((folder, index) => {
                    const isLast = index === path.length - 1;
                    return (
                        <div key={folder.id} className="flex items-center gap-1.5">
                            <BreadcrumbItem>
                                {isLast ? (
                                    <BreadcrumbPage>{folder.name}</BreadcrumbPage>
                                ) : (
                                    <BreadcrumbLink
                                        className="cursor-pointer"
                                        onClick={() => onNavigate(folder.id)}
                                    >
                                        {folder.name}
                                    </BreadcrumbLink>
                                )}
                            </BreadcrumbItem>
                            {!isLast && <BreadcrumbSeparator />}
                        </div>
                    )
                })}
            </BreadcrumbList>
        </Breadcrumb>
    )
}
