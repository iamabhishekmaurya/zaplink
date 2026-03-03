import { useState, useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Dialog, DialogContent, DialogTitle } from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { X, ChevronLeft, ChevronRight, Download, Link, Star, Trash2 } from 'lucide-react';
import { MediaItem } from '../../types';

interface MediaLightboxProps {
    media: MediaItem[];
    initialIndex: number;
    open: boolean;
    onOpenChange: (open: boolean) => void;
    onDelete?: (item: MediaItem) => void;
    onFavorite?: (item: MediaItem) => void;
}

export function MediaLightbox({ media, initialIndex, open, onOpenChange, onDelete, onFavorite }: MediaLightboxProps) {
    const [currentIndex, setCurrentIndex] = useState(initialIndex);

    // Reset index when opened with a new item
    useEffect(() => {
        if (open) {
            setCurrentIndex(initialIndex);
        }
    }, [open, initialIndex]);

    if (!media || media.length === 0 || currentIndex < 0 || currentIndex >= media.length) {
        return null;
    }

    const currentItem = media[currentIndex];
    const isImage = currentItem.type?.startsWith('image/');
    const isVideo = currentItem.type?.startsWith('video/');

    const handleNext = (e: React.MouseEvent) => {
        e.stopPropagation();
        if (currentIndex < media.length - 1) {
            setCurrentIndex(currentIndex + 1);
        }
    };

    const handlePrev = (e: React.MouseEvent) => {
        e.stopPropagation();
        if (currentIndex > 0) {
            setCurrentIndex(currentIndex - 1);
        }
    };

    const handleCopy = () => {
        navigator.clipboard.writeText(currentItem.url);
    };

    // Keyboard navigation
    useEffect(() => {
        const handleKeyDown = (e: KeyboardEvent) => {
            if (!open) return;
            if (e.key === 'ArrowRight' && currentIndex < media.length - 1) {
                setCurrentIndex(prev => prev + 1);
            }
            if (e.key === 'ArrowLeft' && currentIndex > 0) {
                setCurrentIndex(prev => prev - 1);
            }
        };
        window.addEventListener('keydown', handleKeyDown);
        return () => window.removeEventListener('keydown', handleKeyDown);
    }, [open, currentIndex, media.length]);

    return (
        <Dialog open={open} onOpenChange={onOpenChange}>
            <DialogContent className="max-w-[95vw] w-full h-[95vh] p-0 bg-background/95 backdrop-blur-md border-0 flex flex-col overflow-hidden [&>button]:hidden">
                <DialogTitle className="sr-only">Media Preview Lightbox</DialogTitle>
                {/* Header Actions */}
                <div className="flex items-center justify-between p-4 bg-gradient-to-b from-black/50 to-transparent absolute top-0 left-0 right-0 z-50 text-white">
                    <div className="flex items-center gap-3">
                        <span className="font-medium truncate max-w-[200px] md:max-w-md drop-shadow-md">
                            {currentItem.name}
                        </span>
                        <span className="text-xs opacity-70 drop-shadow-md bg-black/30 px-2 py-1 rounded">
                            {(currentItem.size / 1024 / 1024).toFixed(2)} MB
                        </span>
                    </div>

                    <div className="flex items-center gap-2">
                        {onFavorite && (
                            <Button variant="ghost" size="icon" className="text-white hover:bg-white/20" onClick={() => onFavorite(currentItem)}>
                                <Star className={`w-5 h-5 ${currentItem.isFavorite ? 'fill-yellow-400 text-yellow-400' : ''}`} />
                            </Button>
                        )}
                        <Button variant="ghost" size="icon" className="text-white hover:bg-white/20" onClick={handleCopy} title="Copy URL">
                            <Link className="w-5 h-5" />
                        </Button>
                        <Button variant="ghost" size="icon" className="text-white hover:bg-white/20" asChild title="Download original">
                            <a href={currentItem.url} target="_blank" rel="noopener noreferrer" download>
                                <Download className="w-5 h-5" />
                            </a>
                        </Button>
                        {onDelete && (
                            <Button variant="ghost" size="icon" className="text-white hover:bg-red-500/80" onClick={() => onDelete(currentItem)}>
                                <Trash2 className="w-5 h-5" />
                            </Button>
                        )}
                        <div className="w-px h-6 bg-white/20 mx-2" />
                        <Button variant="ghost" size="icon" className="text-white hover:bg-white/20" onClick={() => onOpenChange(false)}>
                            <X className="w-6 h-6" />
                        </Button>
                    </div>
                </div>

                {/* Main View Area */}
                <div className="flex-1 flex items-center justify-center relative bg-black/90">
                    <AnimatePresence mode="wait">
                        <motion.div
                            key={currentItem.id}
                            initial={{ opacity: 0, scale: 0.98 }}
                            animate={{ opacity: 1, scale: 1 }}
                            exit={{ opacity: 0, scale: 1.02 }}
                            transition={{ duration: 0.2 }}
                            className="w-full h-full flex items-center justify-center p-8 md:p-16"
                        >
                            {isImage ? (
                                <img
                                    src={currentItem.url}
                                    alt={currentItem.name}
                                    className="max-w-full max-h-full object-contain rounded drop-shadow-2xl"
                                />
                            ) : isVideo ? (
                                <video
                                    key={currentItem.url}
                                    src={currentItem.url}
                                    controls
                                    autoPlay
                                    className="max-w-full max-h-full rounded drop-shadow-2xl"
                                    style={{ maxHeight: 'calc(95vh - 10rem)' }}
                                />
                            ) : currentItem.type === 'application/pdf' ? (
                                <iframe
                                    key={currentItem.url}
                                    src={currentItem.url}
                                    className="w-full rounded"
                                    style={{ height: 'calc(95vh - 10rem)' }}
                                    title={currentItem.name}
                                />
                            ) : (
                                <div className="text-center text-white">
                                    <h3 className="text-xl mb-4">No Preview Available</h3>
                                    <p className="text-white/60 mb-6">This file type ({currentItem.type}) cannot be previewed directly.</p>
                                    <Button asChild variant="secondary">
                                        <a href={currentItem.url} target="_blank" rel="noopener noreferrer">
                                            Download to View
                                        </a>
                                    </Button>
                                </div>
                            )}
                        </motion.div>
                    </AnimatePresence>

                    {/* Navigation Arrows */}
                    {currentIndex > 0 && (
                        <Button
                            variant="ghost"
                            size="icon"
                            className="absolute left-4 top-1/2 -translate-y-1/2 h-14 w-14 rounded-full bg-black/20 hover:bg-black/40 text-white backdrop-blur-sm border border-white/10"
                            onClick={handlePrev}
                        >
                            <ChevronLeft className="w-8 h-8" />
                        </Button>
                    )}

                    {currentIndex < media.length - 1 && (
                        <Button
                            variant="ghost"
                            size="icon"
                            className="absolute right-4 top-1/2 -translate-y-1/2 h-14 w-14 rounded-full bg-black/20 hover:bg-black/40 text-white backdrop-blur-sm border border-white/10"
                            onClick={handleNext}
                        >
                            <ChevronRight className="w-8 h-8" />
                        </Button>
                    )}
                </div>

                {/* Thumbnails Footer (Optional Desktop only) */}
                <div className="h-24 bg-black/95 border-t border-white/10 flex items-center justify-center gap-2 px-4 overflow-x-auto hidden md:flex">
                    {media.map((m, idx) => (
                        <div
                            key={m.id}
                            onClick={() => setCurrentIndex(idx)}
                            className={`h-16 w-16 shrink-0 rounded overflow-hidden cursor-pointer transition-all ${idx === currentIndex ? 'ring-2 ring-primary ring-offset-2 ring-offset-black opacity-100' : 'opacity-50 hover:opacity-100'}`}
                        >
                            {m.type?.startsWith('image/') ? (
                                <img src={m.url} className="w-full h-full object-cover" />
                            ) : m.type?.startsWith('video/') ? (
                                <video src={m.url} className="w-full h-full object-cover" preload="metadata" muted />
                            ) : (
                                <div className="w-full h-full bg-white/10 flex items-center justify-center">
                                    <span className="text-[10px] text-white/50">{m.type?.split('/')[1] || 'FILE'}</span>
                                </div>
                            )}
                        </div>
                    ))}
                </div>

            </DialogContent>
        </Dialog>
    );
}
