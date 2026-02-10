import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { BioPageTemplate } from "@/features/bio-page/lib/templates";

interface TemplateCardProps {
    template: BioPageTemplate;
    onApply: (template: BioPageTemplate) => void;
    isActive?: boolean;
}

export function TemplateCard({ template, onApply, isActive }: TemplateCardProps) {
    return (
        <Card className={`relative overflow-hidden transition-all hover:shadow-md ${isActive ? 'ring-2 ring-primary border-primary' : ''}`}>
            <div className="aspect-[9/16] w-full bg-muted relative group">
                {/* Placeholder for preview - in real app, might render iframe or image */}
                <div
                    className="absolute inset-0 flex items-center justify-center text-muted-foreground bg-cover bg-center"
                    style={{
                        background: template.theme.effects.backgroundType === 'gradient'
                            ? template.theme.effects.backgroundGradient
                            : template.theme.colors.background
                    }}
                >
                    {/* Sample link/button preview */}
                    <div className="flex flex-col gap-2 w-1/2 opacity-50">
                        <div
                            className="h-8 rounded w-full"
                            style={{
                                backgroundColor: template.theme.colors.button,
                                borderRadius: template.theme.layout.buttonShape === 'pill' ? '999px' : '4px'
                            }}
                        />
                        <div
                            className="h-8 rounded w-full"
                            style={{
                                backgroundColor: template.theme.colors.button,
                                borderRadius: template.theme.layout.buttonShape === 'pill' ? '999px' : '4px'
                            }}
                        />
                    </div>
                </div>

                {/* Hover overlay */}
                <div className="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center p-4">
                    <Button size="sm" onClick={() => onApply(template)} variant="secondary" className="w-full">
                        Apply Template
                    </Button>
                </div>
            </div>

            <CardFooter className="p-3 flex justify-between items-center text-xs">
                <span className="font-medium truncate">{template.name}</span>
                {isActive && <Badge variant="secondary" className="text-[10px] h-5">Active</Badge>}
            </CardFooter>
        </Card>
    );
}
