import { cn } from "@/lib/utils";
import { ReactNode } from "react";

interface DeviceFrameProps {
    children: ReactNode;
    mode?: "mobile" | "desktop";
    className?: string;
}

export function DeviceFrame({ children, mode = "mobile", className }: DeviceFrameProps) {
    if (mode === "desktop") {
        return (
            <div className={cn("w-full h-full border rounded-lg bg-background overflow-hidden", className)}>
                {/* Simple desktop browser chrome simulation */}
                <div className="h-8 bg-muted flex items-center px-4 space-x-2 border-b">
                    <div className="w-3 h-3 rounded-full bg-red-400" />
                    <div className="w-3 h-3 rounded-full bg-yellow-400" />
                    <div className="w-3 h-3 rounded-full bg-green-400" />
                    <div className="flex-1 ml-4 bg-background rounded h-5 text-xs flex items-center px-2 text-muted-foreground">
                        zap.link/username
                    </div>
                </div>
                <div className="h-[calc(100%-2rem)] overflow-y-auto w-full">
                    {children}
                </div>
            </div>
        );
    }

    return (
        <div className={cn("mx-auto relative", className)} style={{ width: '375px', height: '667px' }}>
            <div className="absolute inset-0 border-[8px] border-slate-900/90 rounded-[3rem] overflow-hidden shadow-2xl bg-background">
                {/* Notch */}
                <div className="absolute top-0 left-1/2 transform -translate-x-1/2 w-40 h-6 bg-gray-900 rounded-b-2xl z-20"></div>
                {/* Screen */}
                <div className="w-full h-full overflow-y-auto mobile-scrollbar bg-background">
                    {children}
                </div>
            </div>
            {/* Power Button */}
            <div className="absolute top-24 -right-1 w-1 h-10 bg-gray-800 rounded-r-md"></div>
            {/* Volume Buttons */}
            <div className="absolute top-24 -left-1 w-1 h-8 bg-gray-800 rounded-l-md"></div>
            <div className="absolute top-36 -left-1 w-1 h-8 bg-gray-800 rounded-l-md"></div>
        </div>
    );
}
