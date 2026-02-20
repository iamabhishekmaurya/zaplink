import { useFormContext } from "react-hook-form";
import { FormControl, FormField, FormItem, FormLabel, FormMessage, FormDescription } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { LinkFormData } from "@/features/bio-page/types/index";
import { Textarea } from "@/components/ui/textarea";

export function EmbedLinkForm() {
    const { control } = useFormContext<LinkFormData>();

    return (
        <div className="space-y-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <FormField
                    control={control}
                    name="title"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Title (Optional)</FormLabel>
                            <FormControl>
                                <Input placeholder="My Favorite Song" {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />

                <FormField
                    control={control}
                    name="url"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Fallback URL (Optional)</FormLabel>
                            <FormControl>
                                <Input placeholder="https://..." {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />
            </div>

            <FormField
                control={control}
                name="embedCode"
                render={({ field }) => (
                    <FormItem>
                        <FormLabel>Embed Code or URL</FormLabel>
                        <FormControl>
                            <Textarea
                                placeholder="<iframe src='...'></iframe> or https://youtube.com/..."
                                className="font-mono text-sm min-h-[120px]"
                                {...field}
                            />
                        </FormControl>
                        <FormDescription>Paste the embed code from YouTube, Spotify, SoundCloud, etc.</FormDescription>
                        <FormMessage />
                    </FormItem>
                )}
            />
        </div>
    );
}
