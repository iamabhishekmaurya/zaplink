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
import { Input } from "@/components/ui/input"
import { toast } from "sonner"
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"

const securityFormSchema = z.object({
    two_factor_auth: z.boolean().default(false).optional(),
    current_password: z.string().min(1, "Current password is required"),
    new_password: z.string().min(8, "Password must be at least 8 characters"),
})

type SecurityFormValues = z.infer<typeof securityFormSchema>

const defaultValues: Partial<SecurityFormValues> = {
    two_factor_auth: false,
}

export function SecurityForm() {
    const form = useForm<SecurityFormValues>({
        resolver: zodResolver(securityFormSchema),
        defaultValues,
    })

    function onSubmit(data: SecurityFormValues) {
        toast.success("Security settings updated", {
            description: "Your security settings have been updated."
        })
    }

    return (
        <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
                <Card>
                    <CardHeader>
                        <CardTitle>Security</CardTitle>
                        <CardDescription>
                            Manage your account security settings.
                        </CardDescription>
                    </CardHeader>
                    <CardContent className="space-y-6">
                        <div>
                            <h3 className="mb-4 text-lg font-medium">Two-Factor Authentication</h3>
                            <div className="space-y-4">
                                <FormField
                                    control={form.control}
                                    name="two_factor_auth"
                                    render={({ field }) => (
                                        <FormItem className="flex flex-row items-center justify-between rounded-lg border p-4">
                                            <div className="space-y-0.5">
                                                <FormLabel className="text-base">
                                                    Two-factor Authentication
                                                </FormLabel>
                                                <FormDescription>
                                                    Add an extra layer of security to your account.
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
                            <h3 className="mb-4 text-lg font-medium">Change Password</h3>
                            <div className="space-y-4">
                                <FormField
                                    control={form.control}
                                    name="current_password"
                                    render={({ field }) => (
                                        <FormItem>
                                            <FormLabel>Current Password</FormLabel>
                                            <FormControl>
                                                <Input type="password" {...field} />
                                            </FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    )}
                                />
                                <FormField
                                    control={form.control}
                                    name="new_password"
                                    render={({ field }) => (
                                        <FormItem>
                                            <FormLabel>New Password</FormLabel>
                                            <FormControl>
                                                <Input type="password" {...field} />
                                            </FormControl>
                                            <FormMessage />
                                        </FormItem>
                                    )}
                                />
                            </div>
                        </div>
                    </CardContent>
                    <CardFooter>
                        <Button type="submit">Update security settings</Button>
                    </CardFooter>
                </Card>
            </form>
        </Form>
    )
}
