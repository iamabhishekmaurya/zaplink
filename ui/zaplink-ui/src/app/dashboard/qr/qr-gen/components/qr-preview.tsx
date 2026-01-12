import React, { useState } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { QrCode, Sparkles, Download, Palette, Save } from 'lucide-react'
import { cn } from '@/lib/utils'
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
} from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"

interface QrPreviewProps {
    previewUrl: string | null
    isGenerating: boolean
    onDownload: (format: 'png' | 'svg') => void
    onSave: (name: string) => void
}

export const QrPreview: React.FC<QrPreviewProps> = ({ previewUrl, isGenerating, onDownload, onSave }) => {
    const [name, setName] = useState('')
    const [isOpen, setIsOpen] = useState(false)

    const handleSave = () => {
        if (name.trim()) {
            onSave(name)
            setIsOpen(false)
            setName('')
        }
    }

    return (
        <div className="lg:sticky lg:top-8 space-y-6">
            <Card className="border-2 border-primary/10 overflow-hidden shadow-xl bg-muted/20 backdrop-blur-sm">
                <CardHeader><CardTitle className="text-center">Live Preview</CardTitle></CardHeader>
                <CardContent className="flex flex-col items-center">
                    <div className={cn("relative w-full aspect-square max-w-[300px] flex items-center justify-center rounded-xl overflow-hidden bg-white shadow-inner", isGenerating && "opacity-80 transition-opacity")}>
                        {previewUrl ? <img src={previewUrl} alt="QR Code" className="w-full h-full object-contain" /> : <div className="text-muted-foreground flex flex-col items-center gap-2"><QrCode className="w-12 h-12 opacity-20" /><span className="text-sm">Enter content to generate</span></div>}
                        {isGenerating && <div className="absolute inset-0 flex items-center justify-center bg-white/50 backdrop-blur-[1px]"><Sparkles className="w-8 h-8 text-primary animate-spin" /></div>}
                    </div>
                    <div className="grid grid-row w-full gap-3 mt-8">
                        <Button className="w-full h-12 text-base shadow-sm hover:shadow-primary/10 transition-all font-semibold" variant="outline" onClick={() => onDownload('png')} disabled={!previewUrl || isGenerating}>
                            <Download className="mr-2 h-4 w-4" /> PNG
                        </Button>
                        <Dialog open={isOpen} onOpenChange={setIsOpen}>
                            <DialogTrigger asChild>
                                <Button className="w-full h-12 text-base shadow-lg hover:shadow-primary/25 transition-all font-semibold" disabled={!previewUrl || isGenerating}>
                                    <Save className="mr-2 h-4 w-4" /> Save & Track
                                </Button>
                            </DialogTrigger>
                            <DialogContent className="sm:max-w-xl">
                                <DialogHeader>
                                    <DialogTitle>Save QR Code</DialogTitle>
                                    <DialogDescription>
                                        Give your QR code a name to track its performance later.
                                    </DialogDescription>
                                </DialogHeader>
                                <div className="grid gap-4 py-4">
                                    <div className="grid grid-col items-center gap-4">
                                        <Label htmlFor="name" className="text-left">
                                            Name
                                        </Label>
                                        <Input
                                            id="name"
                                            value={name}
                                            onChange={(e) => setName(e.target.value)}
                                            className="col-span-3"
                                            placeholder="e.g. Summer Campaign"
                                        />
                                    </div>
                                </div>
                                <DialogFooter>
                                    <Button onClick={handleSave} disabled={!name.trim()}>Save QR Code</Button>
                                </DialogFooter>
                            </DialogContent>
                        </Dialog>
                    </div>
                </CardContent>
            </Card>
            <Card className="bg-primary/5 border-none">
                <CardContent className="p-6">
                    <div className="flex gap-4">
                        <div className="p-3 bg-background rounded-xl shadow-sm"><Palette className="w-6 h-6 text-primary" /></div>
                        <div>
                            <h3 className="font-semibold mb-1">Brand Identity</h3>
                            <p className="text-sm text-muted-foreground">Customize colors and shapes to match your brand's unique style perfectly.</p>
                        </div>
                    </div>
                </CardContent>
            </Card>
        </div>
    )
}
