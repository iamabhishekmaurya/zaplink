import { useFormContext } from "react-hook-form";
import { FormControl, FormField, FormItem, FormLabel, FormMessage, FormDescription } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { LinkFormData } from "@/features/bio-page/types/index";

export function ScheduledLinkForm() {
    const { control } = useFormContext<LinkFormData>();

    return (
        <div className="space-y-4">
            <FormField
                control={control}
                name="title"
                render={({ field }) => (
                    <FormItem>
                        <FormLabel>Title</FormLabel>
                        <FormControl>
                            <Input placeholder="Event Name" {...field} />
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                )}
            />

            <div className="grid grid-cols-2 gap-4">
                <FormField
                    control={control}
                    name="scheduleFrom"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Start Date & Time</FormLabel>
                            <FormControl>
                                <Input type="datetime-local" {...field} value={formatDateForInput(field.value)} onChange={(e) => field.onChange(e.target.value ? new Date(e.target.value) : null)} />
                            </FormControl>
                            <FormDescription>Link appears after this time.</FormDescription>
                            <FormMessage />
                        </FormItem>
                    )}
                />

                <FormField
                    control={control}
                    name="scheduleTo"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>End Date & Time</FormLabel>
                            <FormControl>
                                <Input type="datetime-local" {...field} value={formatDateForInput(field.value)} onChange={(e) => field.onChange(e.target.value ? new Date(e.target.value) : null)} />
                            </FormControl>
                            <FormDescription>Link disappears after this time.</FormDescription>
                            <FormMessage />
                        </FormItem>
                    )}
                />
            </div>

            <FormField
                control={control}
                name="url"
                render={({ field }) => (
                    <FormItem>
                        <FormLabel>Destination URL</FormLabel>
                        <FormControl>
                            <Input placeholder="https://example.com/event" {...field} />
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                )}
            />
        </div>
    );
}

function formatDateForInput(date?: Date | string | null): string {
    if (!date) return '';
    const d = new Date(date);
    if (isNaN(d.getTime())) return '';
    // datetime-local expects YYYY-MM-DDThh:mm
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    const hours = String(d.getHours()).padStart(2, '0');
    const minutes = String(d.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}`;
}
