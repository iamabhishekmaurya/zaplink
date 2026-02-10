import { useState } from "react";
import { BioPageWithTheme } from "@/features/bio-page/types/index";
import { DeviceFrame } from "@/features/bio-page/ui/preview-panel/device-frame";
import { ModernBioPageRenderer } from "@/features/bio-page/ui/preview-panel/modern-bio-page-renderer";
import { Button } from "@/components/ui/button";
import { Monitor, Smartphone } from "lucide-react";

interface PreviewPanelProps {
    page: BioPageWithTheme;
    className?: string;
}

export function PreviewPanel({ page, className }: PreviewPanelProps) {
    const [viewMode, setViewMode] = useState<"mobile" | "desktop">("mobile");

    return (
        <div className={`flex flex-col h-full bg-muted/30 ${className}`}>
            {/* Toolbar */}
            <div className="flex items-center justify-center p-4 border-b bg-background/50 backdrop-blur-sm sticky top-0 z-10">
                <div className="flex bg-muted rounded-lg p-1">
                    <Button
                        variant={viewMode === 'mobile' ? 'secondary' : 'ghost'}
                        size="sm"
                        onClick={() => setViewMode('mobile')}
                        title="Mobile View"
                    >
                        <Smartphone className="w-4 h-4 mr-2" />
                        Mobile
                    </Button>
                    <Button
                        variant={viewMode === 'desktop' ? 'secondary' : 'ghost'}
                        size="sm"
                        onClick={() => setViewMode('desktop')}
                        title="Desktop View"
                    >
                        <Monitor className="w-4 h-4 mr-2" />
                        Desktop
                    </Button>
                </div>
            </div>

            {/* Content Area */}
            <div className="flex-1 overflow-hidden relative p-8 flex items-center justify-center">
                <DeviceFrame mode={viewMode}>
                    <ModernBioPageRenderer page={page} previewMode={true} />
                </DeviceFrame>
            </div>
        </div>
    );
}
