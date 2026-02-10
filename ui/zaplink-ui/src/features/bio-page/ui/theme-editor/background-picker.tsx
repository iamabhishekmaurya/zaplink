import { Label } from "@/components/ui/label";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ColorPicker } from "@/components/ui/color-picker";
import { Input } from "@/components/ui/input";
import { ThemeConfig } from "@/ui/design-system/theme-utils";

interface BackgroundPickerProps {
    config: ThemeConfig;
    onChange: (effects?: Partial<ThemeConfig['effects']>, colors?: Partial<ThemeConfig['colors']>) => void;
}

export function BackgroundPicker({ config, onChange }: BackgroundPickerProps) {
    const currentType = config.effects.backgroundType;

    return (
        <div className="space-y-4">
            <Label>Background Style</Label>
            <Tabs
                defaultValue={currentType}
                onValueChange={(val) => onChange({ backgroundType: val as ThemeConfig['effects']['backgroundType'] })}
                className="w-full"
            >
                <TabsList className="grid w-full grid-cols-3">
                    <TabsTrigger value="solid">Solid</TabsTrigger>
                    <TabsTrigger value="gradient">Gradient</TabsTrigger>
                    <TabsTrigger value="image">Image</TabsTrigger>
                </TabsList>

                <TabsContent value="solid" className="pt-4">
                    <ColorPicker
                        label="Background Color"
                        value={config.colors.background}
                        onChange={(color) => onChange({ backgroundType: 'solid' }, { background: color })}
                    />
                </TabsContent>

                <TabsContent value="gradient" className="pt-4 space-y-4">
                    <div>
                        <Label className="mb-2 block">CSS Gradient</Label>
                        <Input
                            value={config.effects.backgroundGradient || ''}
                            onChange={(e) => onChange({ backgroundGradient: e.target.value })}
                            placeholder="linear-gradient(to right, #ff0000, #0000ff)"
                        />
                        <p className="text-xs text-muted-foreground mt-1">
                            Enter a valid CSS gradient string.
                        </p>
                    </div>
                </TabsContent>

                <TabsContent value="image" className="pt-4">
                    <div>
                        <Label className="mb-2 block">Image URL</Label>
                        <Input
                            value={config.effects.backgroundImage || ''}
                            onChange={(e) => onChange({ backgroundImage: e.target.value })}
                            placeholder="https://example.com/image.jpg"
                        />
                        {/* TODO: Add image upload picker */}
                    </div>
                </TabsContent>
            </Tabs>
        </div>
    );
}
