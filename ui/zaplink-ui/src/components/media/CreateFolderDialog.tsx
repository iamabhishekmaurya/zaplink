"use client"

import { Button } from "@/components/ui/button"
import {
    Dialog,
    DialogContent,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { useCreateFolder } from "@/hooks/useMedia"
import { FolderPlus } from "lucide-react"
import { useState } from "react"
import { toast } from "sonner"

interface CreateFolderDialogProps {
    ownerId: string;
    parentId?: string;
}

export function CreateFolderDialog({ ownerId, parentId }: CreateFolderDialogProps) {
    const [open, setOpen] = useState(false);
    const [name, setName] = useState("");
    const createFolder = useCreateFolder();

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (!name.trim()) return;

        createFolder.mutate({ name, ownerId, parentId }, {
            onSuccess: () => {
                setOpen(false);
                setName("");
                toast.success("Folder created");
            },
            onError: (err) => {
                toast.error("Failed to create folder");
                console.error(err);
            }
        });
    }

    return (
        <Dialog open={open} onOpenChange={setOpen}>
            <DialogTrigger asChild>
                <Button variant="outline" size="sm">
                    <FolderPlus className="mr-2 h-4 w-4" /> New Folder
                </Button>
            </DialogTrigger>
            <DialogContent className="sm:max-w-[425px]">
                <DialogHeader>
                    <DialogTitle>Create New Folder</DialogTitle>
                </DialogHeader>
                <form onSubmit={handleSubmit} className="grid gap-4 py-4">
                    <div className="grid grid-cols-4 items-center gap-4">
                        <Label htmlFor="name" className="text-right">
                            Name
                        </Label>
                        <Input
                            id="name"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            className="col-span-3"
                            autoFocus
                        />
                    </div>
                    <DialogFooter>
                        <Button type="submit" disabled={createFolder.isPending}>
                            {createFolder.isPending ? "Creating..." : "Create"}
                        </Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    )
}
