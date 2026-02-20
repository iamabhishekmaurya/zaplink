"use client"

import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import * as z from "zod"

import { Button } from "@/components/ui/button"
import {
    Form,
    FormControl,
    FormDescription,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form"
import { Switch } from "@/components/ui/switch"
import { toast } from "sonner"
import { Input } from "@/components/ui/input"
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"

const advancedFormSchema = z.object({
    experimental_features: z.boolean().default(false).optional(),
    api_key: z.string().optional(),
})

type AdvancedFormValues = z.infer<typeof advancedFormSchema>

const defaultValues: Partial<AdvancedFormValues> = {
    experimental_features: false,
    api_key: "sk_test_1234567890abcdef",
}

export function AdvancedForm() {
    const form = useForm<AdvancedFormValues>({
        resolver: zodResolver(advancedFormSchema),
        defaultValues,
    })

    function onSubmit(data: AdvancedFormValues) {
        toast.success("Advanced settings updated", {
            description: "Your advanced settings have been updated."
        })
    }

    return (
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
                <Card>
                    <CardHeader>
                        <CardTitle>Advanced Settings</CardTitle>
                        <CardDescription>
                            Configure advanced and experimental settings.
                        </CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-6">
                        <div>
                            <h3 className="mb-4 text-lg font-medium">Developer Settings</h3>
                            <div className="space-y-4">
                                <FormField
                                    control={form.control}
                                    name="api_key"
                                    render={({ field }) => (
                                        <FormItem>
                                            <FormLabel>API Key</FormLabel>
                                            <FormControl>
                                                <Input readOnly {...field} />
                                            </FormControl>
                                            <FormDescription>
                                                Your secret API key. Do not share this with anyone.
                                            </FormDescription>
                                            <FormMessage />
                                        </FormItem>
                                    )}
                                />
                            </div>
                        </div>
                        <div>
                            <h3 className="mb-4 text-lg font-medium">Experimental Features</h3>
                            <div className="space-y-4">
                                <FormField
                                    control={form.control}
                                    name="experimental_features"
                                    render={({ field }) => (
                                        <FormItem className="flex flex-row items-center justify-between rounded-lg border p-4">
                                            <div className="space-y-0.5">
                                                <FormLabel className="text-base">
                                                    Enable Experimental Features
                                                </FormLabel>
                                                <FormDescription>
                                                    Get early access to new features before they are released.
                                                </FormDescription>
                                            </div>
                                            <FormControl>
                                                <Switch
                                                    checked={field.value}
                                                    onCheckedChange={field.onChange}
                                                />
                                            </FormControl>
                                        </FormItem>
                                    )}
                                />
                            </div>
                        </div>
                    </CardContent>
                    <CardFooter className="justify-between border-t px-6 py-4">
                        <div className="text-sm text-muted-foreground">
                            Be careful with these settings.
                        </div>
                        <Button type="submit" variant="destructive">Delete Account</Button>
                    </CardFooter>
                </Card>
            </form>
        </Form>
    )
}
