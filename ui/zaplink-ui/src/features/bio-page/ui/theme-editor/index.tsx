"use client"

import { motion, Variants } from "framer-motion";
import { ThemeConfig } from "@/ui/design-system/theme-utils";
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

interface ThemeEditorProps {
    theme: ThemeConfig;
    onChange: (theme: ThemeConfig) => void;
}

const cardVariants: Variants = {
    hidden: { opacity: 0, scale: 0.98, y: 15 },
    visible: (i: number) => ({
        opacity: 1,
        scale: 1,
        y: 0,
        transition: {
            delay: i * 0.1,
            duration: 0.4,
            ease: [0.25, 0.46, 0.45, 0.94]
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
        <div className="h-full overflow-y-auto mobile-scrollbar pr-4 pb-8">
            <div className="space-y-8 max-w-4xl mx-auto mt-2 px-2 sm:px-0">
                {/* Premium Header */}
                <motion.div
                    initial={{ opacity: 0, y: -10 }}
                    animate={{ opacity: 1, y: 0 }}
                    className="flex items-center gap-4 pb-2"
                >
                    <div className="p-3 bg-gradient-to-br from-primary/20 to-primary/5 rounded-2xl shadow-sm border border-primary/10">
                        <Sparkles className="w-6 h-6 text-primary" />
                    </div>
                    <div>
                        <h3 className="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-foreground to-foreground/70 tracking-tight">
                            Design Your Page
                        </h3>
                        <p className="text-sm text-muted-foreground mt-1">
                            Customize colors, fonts, and background to match your style
                        </p>
                    </div>
                </motion.div>

                {/* Live Preview Hint */}
                {/* <motion.div
                    initial={{ opacity: 0, scale: 0.95 }}
                    animate={{ opacity: 1, scale: 1 }}
                    transition={{ delay: 0.2 }}
                    className="flex items-center justify-center gap-3 p-4 bg-gradient-to-r from-primary/10 via-primary/5 to-transparent rounded-2xl border border-primary/10 shadow-sm relative overflow-hidden"
                >
                    <div className="absolute inset-y-0 left-0 w-1 bg-primary/40 rounded-l-2xl" />
                    <Eye className="w-5 h-5 text-primary opacity-80" />
                    <p className="text-sm font-semibold text-primary/90 tracking-wide">
                        Changes apply instantly to the live preview
                    </p>
                </motion.div> */}

                <div className="space-y-6">
                    {/* Background Card */}
                    <motion.div
                        custom={0}
                        initial="hidden"
                        animate="visible"
                        variants={cardVariants}
                        className="rounded-xl border border-border/50 bg-card/40 backdrop-blur-xl shadow-sm overflow-hidden relative group"
                    >
                        <div className="absolute top-0 inset-x-0 h-px bg-gradient-to-r from-transparent via-blue-500/30 to-transparent" />
                        <div className="p-8 relative z-10 flex flex-col gap-6">
                            <div className="flex items-center gap-4">
                                <div className="p-3 bg-blue-500/10 rounded-2xl ring-1 ring-blue-500/20 group-hover:scale-110 transition-transform">
                                    <ImageIcon className="w-4 h-4 text-blue-500" />
                                </div>
                                <div className="text-left flex-1 min-w-0">
                                    <h4 className="font-bold text-lg tracking-tight">Background</h4>
                                    <p className="text-sm text-muted-foreground">Set colors, gradients, or images</p>
                                </div>
                            </div>

                            <div className="pt-2">
                                <BackgroundPicker
                                    config={theme}
                                    onChange={(effects, colors) => {
                                        if (effects) updateEffects(effects);
                                        if (colors) updateColors(colors);
                                    }}
                                />
                            </div>
                        </div>
                    </motion.div>

                    {/* Colors Card */}
                    <motion.div
                        custom={1}
                        initial="hidden"
                        animate="visible"
                        variants={cardVariants}
                        className="rounded-xl border border-border/50 bg-card/40 backdrop-blur-xl shadow-sm overflow-hidden relative group"
                    >
                        <div className="absolute top-0 inset-x-0 h-px bg-gradient-to-r from-transparent via-pink-500/30 to-transparent" />
                        <div className="p-8 relative z-10 flex flex-col gap-6">
                            <div className="flex items-center gap-4">
                                <div className="p-3 bg-pink-500/10 rounded-2xl ring-1 ring-pink-500/20 group-hover:scale-110 transition-transform">
                                    <Palette className="w-4 h-4 text-pink-500" />
                                </div>
                                <div className="text-left flex-1 min-w-0">
                                    <h4 className="font-bold text-lg tracking-tight">Colors</h4>
                                    <p className="text-sm text-muted-foreground">Customize your color scheme</p>
                                </div>
                            </div>

                            <div className="space-y-4 pt-2">
                                <div className="p-5 rounded-2xl bg-background/50 border border-border/40 hover:bg-background/80 transition-colors shadow-sm">
                                    <ColorPicker
                                        label="Primary / Buttons"
                                        value={theme.colors.primary}
                                        onChange={(c) => updateColors({ primary: c, button: c })}
                                    />
                                </div>
                                <div className="p-5 rounded-2xl bg-background/50 border border-border/40 hover:bg-background/80 transition-colors shadow-sm">
                                    <ColorPicker
                                        label="Text Color"
                                        value={theme.colors.text}
                                        onChange={(c) => updateColors({ text: c })}
                                    />
                                </div>
                                <div className="p-5 rounded-2xl bg-background/50 border border-border/40 hover:bg-background/80 transition-colors shadow-sm">
                                    <ColorPicker
                                        label="Button Text"
                                        value={theme.colors.buttonText}
                                        onChange={(c) => updateColors({ buttonText: c })}
                                    />
                                </div>
                            </div>
                        </div>
                    </motion.div>

                    {/* Typography Card */}
                    <motion.div
                        custom={2}
                        initial="hidden"
                        animate="visible"
                        variants={cardVariants}
                        className="rounded-xl border border-border/50 bg-card/40 backdrop-blur-xl shadow-sm overflow-hidden relative group"
                    >
                        <div className="absolute top-0 inset-x-0 h-px bg-gradient-to-r from-transparent via-amber-500/30 to-transparent" />
                        <div className="p-8 relative z-10 flex flex-col gap-6">
                            <div className="flex items-center gap-4">
                                <div className="p-3 bg-amber-500/10 rounded-2xl ring-1 ring-amber-500/20 group-hover:scale-110 transition-transform">
                                    <Type className="w-4 h-4 text-amber-500" />
                                </div>
                                <div className="text-left flex-1 min-w-0">
                                    <h4 className="font-bold text-lg tracking-tight">Typography</h4>
                                    <p className="text-sm text-muted-foreground">Choose your font style</p>
                                </div>
                            </div>

                            <div className="pt-2">
                                <FontSelector
                                    value={theme.typography.fontFamily}
                                    onChange={(f) => updateTypography({ fontFamily: f })}
                                />
                            </div>
                        </div>
                    </motion.div>

                    {/* Buttons & Cards */}
                    <motion.div
                        custom={3}
                        initial="hidden"
                        animate="visible"
                        variants={cardVariants}
                        className="rounded-xl border border-border/50 bg-card/40 backdrop-blur-xl shadow-sm overflow-hidden relative group"
                    >
                        <div className="absolute top-0 inset-x-0 h-px bg-gradient-to-r from-transparent via-emerald-500/30 to-transparent" />
                        <div className="p-8 relative z-10 flex flex-col gap-6">
                            <div className="flex items-center gap-4">
                                <div className="p-3 bg-emerald-500/10 rounded-2xl ring-1 ring-emerald-500/20 group-hover:scale-110 transition-transform">
                                    <LayoutTemplate className="w-4 h-4 text-emerald-500" />
                                </div>
                                <div className="text-left flex-1 min-w-0">
                                    <h4 className="font-bold text-lg tracking-tight">Buttons & Cards</h4>
                                    <p className="text-sm text-muted-foreground">Customize shapes and styles</p>
                                </div>
                            </div>

                            <div className="pt-2 bg-background/30 p-6 rounded-2xl border border-border/30 shadow-inner">
                                <ButtonPickerWrapper wrapperLayout={theme.layout} wrapperUpdate={updateLayout} />
                            </div>
                        </div>
                    </motion.div>
                </div>
            </div>
        </div>
    );
}

function ButtonPickerWrapper({ wrapperLayout, wrapperUpdate }: { wrapperLayout: ThemeConfig['layout'], wrapperUpdate: (l: Partial<ThemeConfig['layout']>) => void }) {
    return <ButtonStylePicker layout={wrapperLayout} onChange={wrapperUpdate} />;
}
