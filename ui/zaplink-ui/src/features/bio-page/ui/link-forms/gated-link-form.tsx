import { useFormContext } from "react-hook-form";
import { FormControl, FormField, FormItem, FormLabel, FormMessage, FormDescription } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Textarea } from "@/components/ui/textarea"; // Assuming Textarea component exists, if not use Input
import { LinkFormData } from "@/features/bio-page/types/index";

export function GatedLinkForm() {
    const { control, watch } = useFormContext<LinkFormData>();
    const gateType = watch("gateType");

    return (
        <div className="space-y-4">
            <FormField
                control={control}
                name="title"
                render={({ field }) => (
                    <FormItem>
                        <FormLabel>Title</FormLabel>
                        <FormControl>
                            <Input placeholder="Exclusive Content" {...field} />
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                )}
            />

            <FormField
                control={control}
                name="gateType"
                render={({ field }) => (
                    <FormItem>
                        <FormLabel>Gate Type</FormLabel>
                        <Select onValueChange={field.onChange} defaultValue={field.value}>
                            <FormControl>
                                <SelectTrigger>
                                    <SelectValue placeholder="Select gate type" />
                                </SelectTrigger>
                            </FormControl>
                            <SelectContent>
                                <SelectItem value="email">Email Collection</SelectItem>
                                <SelectItem value="password">Password Protection</SelectItem>
                                <SelectItem value="payment">One-time Payment</SelectItem>
                            </SelectContent>
                        </Select>
                        <FormDescription>How users can access the content.</FormDescription>
                        <FormMessage />
                    </FormItem>
                )}
            />

            {gateType === 'password' && (
                <FormField
                    control={control}
                    name="gateValue"
                    render={({ field }) => (
                        <FormItem>
                            <FormLabel>Password</FormLabel>
                            <FormControl>
                                <Input type="text" placeholder="Secret123" {...field} />
                            </FormControl>
                            <FormMessage />
                        </FormItem>
                    )}
                />
            )}

            {gateType === 'payment' && (
                <div className="grid grid-cols-2 gap-4">
                    <FormField
                        control={control}
                        name="price"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Price</FormLabel>
                                <FormControl>
                                    <Input type="number" step="0.01" placeholder="9.99" {...field} onChange={(e) => field.onChange(parseFloat(e.target.value))} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                    <FormField
                        control={control}
                        name="currency"
                        render={({ field }) => (
                            <FormItem>
                                <FormLabel>Currency</FormLabel>
                                <FormControl>
                                    <Input placeholder="USD" {...field} />
                                </FormControl>
                                <FormMessage />
                            </FormItem>
                        )}
                    />
                </div>
            )}

            <FormField
                control={control}
                name="gateMessage"
                render={({ field }) => (
                    <FormItem>
                        <FormLabel>Gate Message</FormLabel>
                        <FormControl>
                            {/* Using Input as Textarea backup if imports fail */}
                            <Input placeholder="Unlock exclusive content..." {...field} />
                        </FormControl>
                        <FormDescription>Message displayed before unlocking.</FormDescription>
                        <FormMessage />
                    </FormItem>
                )}
            />

            <FormField
                control={control}
                name="url"
                render={({ field }) => (
                    <FormItem>
                        <FormLabel>Content URL</FormLabel>
                        <FormControl>
                            <Input placeholder="https://example.com/secret.pdf" {...field} />
                        </FormControl>
                        <FormMessage />
                    </FormItem>
                )}
            />
        </div>
    );
}
