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
} from "@/components/ui/form"
import { Switch } from "@/components/ui/switch"
import { toast } from "sonner"
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"

const privacyFormSchema = z.object({
    public_profile: z.boolean(),
    data_sharing: z.boolean(),
    marketing_emails: z.boolean(),
})

type PrivacyFormValues = z.infer<typeof privacyFormSchema>

const defaultValues: PrivacyFormValues = {
    public_profile: false,
    data_sharing: false,
    marketing_emails: false,
}

export function PrivacyForm() {
    const form = useForm<PrivacyFormValues>({
        resolver: zodResolver(privacyFormSchema),
        defaultValues,
    })

    function onSubmit(data: PrivacyFormValues) {
        toast.success("Privacy settings updated", {
            description: "Your privacy preferences have been saved."
        })
    }

    return (
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
                <Card>
                    <CardHeader>
                        <CardTitle>Privacy</CardTitle>
                        <CardDescription>
                            Manage your privacy settings.
                        </CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-6">
                        <div>
                            <h3 className="mb-4 text-lg font-medium">Visibility</h3>
                            <div className="space-y-4">
                                <FormField
                                    control={form.control}
                                    name="public_profile"
                                    render={({ field }) => (
                                        <FormItem className="flex flex-row items-center justify-between rounded-lg border p-4">
                                            <div className="space-y-0.5">
                                                <FormLabel className="text-base">
                                                    Public Profile
                                                </FormLabel>
                                                <FormDescription>
                                                    Allow your profile to be visible to search engines and public users.
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
                        <div>
                            <h3 className="mb-4 text-lg font-medium">Data & Consent</h3>
                            <div className="space-y-4">
                                <FormField
                                    control={form.control}
                                    name="data_sharing"
                                    render={({ field }) => (
                                        <FormItem className="flex flex-row items-center justify-between rounded-lg border p-4">
                                            <div className="space-y-0.5">
                                                <FormLabel className="text-base">Data Sharing</FormLabel>
                                                <FormDescription>
                                                    Allow us to share anonymized data with partners for better ad targeting.
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
                                <FormField
                                    control={form.control}
                                    name="marketing_emails"
                                    render={({ field }) => (
                                        <FormItem className="flex flex-row items-center justify-between rounded-lg border p-4">
                                            <div className="space-y-0.5">
                                                <FormLabel className="text-base">
                                                    Marketing Emails
                                                </FormLabel>
                                                <FormDescription>
                                                    Receive emails about new products, features, and more.
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
                    <CardFooter>
                        <Button type="submit">Update privacy settings</Button>
                    </CardFooter>
                </Card>
            </form>
        </Form>
    )
}
