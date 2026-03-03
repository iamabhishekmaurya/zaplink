import { ImageIcon } from 'lucide-react';
import { motion } from 'framer-motion';

interface EmptyStateProps {
    message?: string;
    subMessage?: string;
    action?: React.ReactNode;
}

export function EmptyState({
    message = "This folder is empty",
    subMessage = "Upload media or create a folder to get started",
    action
}: EmptyStateProps) {
    return (
        <motion.div
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            className="h-full w-full flex flex-col items-center justify-center p-8 border-2 border-dashed border-border/50 rounded-2xl"
        >
            <div className="bg-muted/30 p-4 rounded-full mb-4">
                <ImageIcon className="w-10 h-10 text-muted-foreground opacity-70" />
            </div>
            <h3 className="text-lg font-medium mb-1">{message}</h3>
            <p className="text-sm text-muted-foreground text-center mb-6 max-w-sm">
                {subMessage}
            </p>
            {action && (
                <div className="flex gap-3">
                    {action}
                </div>
            )}
        </motion.div>
    );
}
