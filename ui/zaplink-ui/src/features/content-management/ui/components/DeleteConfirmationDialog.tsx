import { useState } from 'react';
import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
} from "@/components/ui/alert-dialog"

interface DeleteConfirmationDialogProps {
    open: boolean;
    onOpenChange: (open: boolean) => void;
    itemName: string;
    isFolder?: boolean;
    onConfirm: () => Promise<void>;
}

export function DeleteConfirmationDialog({ open, onOpenChange, itemName, isFolder, onConfirm }: DeleteConfirmationDialogProps) {
    const [isDeleting, setIsDeleting] = useState(false);

    const handleConfirm = async (e: React.MouseEvent) => {
        e.preventDefault(); // Prevent immediate closing
        setIsDeleting(true);
        try {
            await onConfirm();
            onOpenChange(false);
        } finally {
            setIsDeleting(false);
        }
    }

    return (
        <AlertDialog open={open} onOpenChange={onOpenChange}>
            <AlertDialogContent>
                <AlertDialogHeader>
                    <AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
                    <AlertDialogDescription>
                        This will move <strong>{itemName}</strong> to the Trash.
                        {isFolder && " Any files and subfolders inside this folder will also be moved to Trash."}
                    </AlertDialogDescription>
                </AlertDialogHeader>
                <AlertDialogFooter>
                    <AlertDialogCancel disabled={isDeleting}>Cancel</AlertDialogCancel>
                    <AlertDialogAction
                        onClick={handleConfirm}
                        className="bg-destructive hover:bg-destructive/90 text-destructive-foreground disabled:opacity-50"
                        disabled={isDeleting}
                    >
                        {isDeleting ? "Moving to Trash..." : "Move to Trash"}
                    </AlertDialogAction>
                </AlertDialogFooter>
            </AlertDialogContent>
        </AlertDialog>
    );
}
