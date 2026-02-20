"use client"

import { motion } from "framer-motion";
import { ThemeConfig } from "@/ui/design-system/theme-utils";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import { ScrollArea } from "@/components/ui/scroll-area";
import { FontSelector } from "./font-selector";
import { BackgroundPicker } from "./background-picker";
import { ButtonStylePicker } from "./button-style-picker";
import { ColorPicker } from "@/components/ui/color-picker";

import {
    Palette,
    Type,
    Image as ImageIcon,
    LayoutTemplate,
    Sparkles,
    Eye
} from "lucide-react";
import { cn } from "@/lib/utils";

interface ThemeEditorProps {
    theme: ThemeConfig;
    onChange: (theme: ThemeConfig) => void;
}

const accordionItemVariants = {
    hidden: { opacity: 0, y: 10 },
    visible: (i: number) => ({
        opacity: 1,
        y: 0,
        transition: {
            delay: i * 0.1,
            duration: 0.3,
        },
    }),
};

export function ThemeEditor({ theme, onChange }: ThemeEditorProps) {
    const updateColors = (colors: Partial<ThemeConfig['colors']>) => {
        onChange({
            ...theme,
            colors: { ...theme.colors, ...colors },
        });
    };

    const updateTypography = (typography: Partial<ThemeConfig['typography']>) => {
        onChange({
            ...theme,
            typography: { ...theme.typography, ...typography },
        });
    };

    const updateLayout = (layout: Partial<ThemeConfig['layout']>) => {
        onChange({
            ...theme,
            layout: { ...theme.layout, ...layout },
        });
    };

    const updateEffects = (effects: Partial<ThemeConfig['effects']>) => {
        onChange({
            ...theme,
            effects: { ...theme.effects, ...effects },
        });
    };

    return (
        <ScrollArea className="h-full pr-4">
            <div className="space-y-6 pb-20">
                {/* Header */}
                <motion.div
                    initial={{ opacity: 0, y: -10 }}
                    animate={{ opacity: 1, y: 0 }}
                    className="flex items-center gap-3 pb-4 border-b"
                >
                    <div className="p-2 bg-primary/10 rounded-xl">
                        <Sparkles className="w-5 h-5 text-primary" />
                    </div>
                    <div>
                        <h3 className="text-lg font-semibold text-foreground">
                            Design Your Page
                        </h3>
                        <p className="text-sm text-muted-foreground">
                            Customize colors, fonts, and background to match your style
                        </p>
                    </div>
                </motion.div>

                <Accordion type="multiple" defaultValue={['background', 'colors', 'typography']} className="space-y-4">

                    <motion.div
                        custom={0}
                        initial="hidden"
                        animate="visible"
                        variants={accordionItemVariants}
                    >
                        <AccordionItem value="background" className="border rounded-xl px-4 bg-gradient-to-r from-card to-muted/20 shadow-sm">
                            <AccordionTrigger className="hover:no-underline py-4">
                                <div className="flex items-center gap-3">
                                    <div className="p-2 bg-blue-500/10 rounded-lg">
                                        <ImageIcon className="w-4 h-4 text-blue-500" />
                                    </div>
                                    <div className="text-left">
                                        <span className="font-semibold text-sm">Background</span>
                                        <p className="text-xs text-muted-foreground font-normal">Set colors, gradients, or images</p>
                                    </div>
                                </div>
                            </AccordionTrigger>
                            <AccordionContent className="pb-4">
                                <BackgroundPicker
                                    config={theme}
                                    onChange={(effects, colors) => {
                                        if (effects) updateEffects(effects);
                                        if (colors) updateColors(colors);
                                    }}
                                />
                            </AccordionContent>
                        </AccordionItem>
                    </motion.div>

                    <motion.div
                        custom={1}
                        initial="hidden"
                        animate="visible"
                        variants={accordionItemVariants}
                    >
                        <AccordionItem value="colors" className="border rounded-xl px-4 bg-gradient-to-r from-card to-muted/20 shadow-sm">
                            <AccordionTrigger className="hover:no-underline py-4">
                                <div className="flex items-center gap-3">
                                    <div className="p-2 bg-pink-500/10 rounded-lg">
                                        <Palette className="w-4 h-4 text-pink-500" />
                                    </div>
                                    <div className="text-left">
                                        <span className="font-semibold text-sm">Colors</span>
                                        <p className="text-xs text-muted-foreground font-normal">Customize your color scheme</p>
                                    </div>
                                </div>
                            </AccordionTrigger>
                            <AccordionContent className="pb-4 space-y-4">
                                <ColorPicker
                                    label="Primary / Buttons"
                                    value={theme.colors.primary}
                                    onChange={(c) => updateColors({ primary: c, button: c })}
                                />
                                <ColorPicker
                                    label="Text Color"
                                    value={theme.colors.text}
                                    onChange={(c) => updateColors({ text: c })}
                                />
                                <ColorPicker
                                    label="Button Text"
                                    value={theme.colors.buttonText}
                                    onChange={(c) => updateColors({ buttonText: c })}
                                />
                            </AccordionContent>
                        </AccordionItem>
                    </motion.div>

                    <motion.div
                        custom={2}
                        initial="hidden"
                        animate="visible"
                        variants={accordionItemVariants}
                    >
                        <AccordionItem value="typography" className="border rounded-xl px-4 bg-gradient-to-r from-card to-muted/20 shadow-sm">
                            <AccordionTrigger className="hover:no-underline py-4">
                                <div className="flex items-center gap-3">
                                    <div className="p-2 bg-amber-500/10 rounded-lg">
                                        <Type className="w-4 h-4 text-amber-500" />
                                    </div>
                                    <div className="text-left">
                                        <span className="font-semibold text-sm">Typography</span>
                                        <p className="text-xs text-muted-foreground font-normal">Choose your font style</p>
                                    </div>
                                </div>
                            </AccordionTrigger>
                            <AccordionContent className="pb-4">
                                <FontSelector
                                    value={theme.typography.fontFamily}
                                    onChange={(f) => updateTypography({ fontFamily: f })}
                                />
                            </AccordionContent>
                        </AccordionItem>
                    </motion.div>

                    <motion.div
                        custom={3}
                        initial="hidden"
                        animate="visible"
                        variants={accordionItemVariants}
                    >
                        <AccordionItem value="buttons" className="border rounded-xl px-4 bg-gradient-to-r from-card to-muted/20 shadow-sm">
                            <AccordionTrigger className="hover:no-underline py-4">
                                <div className="flex items-center gap-3">
                                    <div className="p-2 bg-emerald-500/10 rounded-lg">
                                        <LayoutTemplate className="w-4 h-4 text-emerald-500" />
                                    </div>
                                    <div className="text-left">
                                        <span className="font-semibold text-sm">Buttons & Cards</span>
                                        <p className="text-xs text-muted-foreground font-normal">Customize button styles and shapes</p>
                                    </div>
                                </div>
                            </AccordionTrigger>
                            <AccordionContent className="pb-4">
                                <ButtonPickerWrapper wrapperLayout={theme.layout} wrapperUpdate={updateLayout} />
                            </AccordionContent>
                        </AccordionItem>
                    </motion.div>

                </Accordion>

                {/* Live Preview Hint */}
                <motion.div
                    initial={{ opacity: 0 }}
                    animate={{ opacity: 1 }}
                    transition={{ delay: 0.5 }}
                    className="flex items-center gap-2 p-4 bg-primary/5 rounded-xl border border-primary/10"
                >
                    <Eye className="w-4 h-4 text-primary" />
                    <p className="text-sm text-primary">
                        Changes apply instantly to the preview on the right
                    </p>
                </motion.div>
            </div>
        </ScrollArea>
    );
}

function ButtonPickerWrapper({ wrapperLayout, wrapperUpdate }: { wrapperLayout: ThemeConfig['layout'], wrapperUpdate: (l: Partial<ThemeConfig['layout']>) => void }) {
    return <ButtonStylePicker layout={wrapperLayout} onChange={wrapperUpdate} />;
}
