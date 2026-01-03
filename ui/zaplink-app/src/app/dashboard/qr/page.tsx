'use client';

import { QRGenerator } from '@/components/qr/QRGenerator';

export default function QRPage() {
    return (
        <div className="container mx-auto px-4 py-8 h-[calc(100vh-theme(spacing.24))]">
            <div className="mb-8">
                <h1 className="text-3xl font-bold font-display tracking-tight mb-2">QR Code Generator</h1>
                <p className="text-muted-foreground">Create styling, custom QR codes for your links or any text.</p>
            </div>

            <div className="h-full">
                <QRGenerator />
            </div>
        </div>
    );
}
