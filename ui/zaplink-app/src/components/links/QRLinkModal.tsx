import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
} from "@/components/ui/dialog";
import { QRGenerator } from "@/components/qr/QRGenerator";

interface QRLinkModalProps {
    isOpen: boolean;
    onClose: () => void;
    url: string;
    alias?: string;
}

export function QRLinkModal({ isOpen, onClose, url, alias }: QRLinkModalProps) {
    return (
        <Dialog open={isOpen} onOpenChange={(open) => !open && onClose()}>
            <DialogContent className="max-w-4xl h-[90vh] flex flex-col p-6 overflow-hidden">
                <DialogHeader className="pb-4 border-b">
                    <DialogTitle>QR Code Studio</DialogTitle>
                    <DialogDescription>
                        Customize and download a high-quality QR code for <span className="font-mono text-primary font-semibold">{alias}</span>
                    </DialogDescription>
                </DialogHeader>
                <div className="flex-1 overflow-hidden pt-6">
                    <QRGenerator defaultUrl={url} />
                </div>
            </DialogContent>
        </Dialog>
    );
}
