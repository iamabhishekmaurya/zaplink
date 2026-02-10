import { ScrollArea } from "@/components/ui/scroll-area";
import { templates, BioPageTemplate } from "@/features/bio-page/lib/templates";
import { TemplateCard } from "@/features/bio-page/ui/template-gallery/template-card";
import { ThemeConfig } from "@/ui/design-system/theme-utils";

interface TemplateGalleryProps {
    currentTheme: ThemeConfig;
    onApply: (template: BioPageTemplate) => void;
}

export function TemplateGallery({ currentTheme, onApply }: TemplateGalleryProps) {
    // Simple check for equality might be complex, so we check "active" based on applied ID?
    // But we don't store "applied template ID" in ThemeConfig usually.
    // We can just highlight if user clicks applying. Or maybe pass "selectedTemplateId" prop.
    // Assuming minimal check or just visual apply.

    return (
        <div className="h-full flex flex-col">
            <div className="mb-4 px-1">
                <h3 className="text-lg font-medium">Choose a Template</h3>
                <p className="text-sm text-muted-foreground">
                    Start with a professionally designed look and customize it.
                </p>
            </div>

            <ScrollArea className="h-full pr-4">
                <div className="grid grid-cols-2 gap-4 pb-20">
                    {templates.map((template) => (
                        <div key={template.id} className="relative">
                            <TemplateCard
                                template={template}
                                onApply={onApply}
                                isActive={JSON.stringify(template.theme) === JSON.stringify(currentTheme) /* Fallback check */}
                            />
                        </div>
                    ))}
                </div>
            </ScrollArea>
        </div>
    );
}
