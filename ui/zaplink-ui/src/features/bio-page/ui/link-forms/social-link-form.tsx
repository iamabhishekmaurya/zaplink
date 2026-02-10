import { useFormContext } from "react-hook-form";
import { FormControl, FormField, FormItem, FormLabel, FormMessage, FormDescription } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { LinkFormData, SUPPORTED_SOCIAL_PLATFORMS } from "@/features/bio-page/types/index";

export function SocialLinkForm() {
    const { control } = useFormContext<LinkFormData>();

    return (
        <div className="space-y-4">
            <FormField
                control={control}
                name="title"
                render={({ field }) => (
                    <FormItem>
                        <FormLabel>Platform</FormLabel>
                        <FormControl>
                            <Select onValueChange={(val) => {
                                field.onChange(val.charAt(0).toUpperCase() + val.slice(1));
                                // Optionally try to set iconUrl here if we have a mapping
                            }} defaultValue={field.value?.toLowerCase()}>
                                <SelectTrigger>
                                    <SelectValue placeholder="Select platform" />
                                </SelectTrigger>
                                <SelectContent>
                                    {SUPPORTED_SOCIAL_PLATFORMS.map((platform) => (
                                        <SelectItem key={platform} value={platform}>
                                            {platform.charAt(0).toUpperCase() + platform.slice(1)}
                                        </SelectItem>
                                    ))}
                                </SelectContent>
                            </Select>
                        </FormControl>
                        <FormDescription>Select the social media platform.</FormDescription>
                        <FormMessage />
                    </FormItem>
                )}
            />

            <FormField
                control={control}
                name="url"
                render={({ field }) => (
                    <FormItem>
                        <FormLabel>Profile URL</FormLabel>
                        <FormControl>
                            <Input placeholder="https://instagram.com/username" {...field} />
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                )}
            />
        </div>
    );
}
