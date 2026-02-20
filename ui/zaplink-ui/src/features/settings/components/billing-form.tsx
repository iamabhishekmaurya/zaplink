"use client"

import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import * as z from "zod"

import { Button } from "@/components/ui/button"
import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"
import { toast } from "sonner"
import { RadioGroup, RadioGroupItem } from "@/components/ui/radio-group"
import { Label } from "@/components/ui/label"


const billingFormSchema = z.object({
    plan: z.enum(["free", "pro", "enterprise"]),
})

type BillingFormValues = z.infer<typeof billingFormSchema>

// This can come from your database or API.
const defaultValues: Partial<BillingFormValues> = {
    plan: "free",
}

export function BillingForm() {
    const form = useForm<BillingFormValues>({
        resolver: zodResolver(billingFormSchema),
        defaultValues,
    })

    function onSubmit(data: BillingFormValues) {
        toast.success("Billing settings updated", {
            description: "Your plan has been updated."
        })
    }

    return (
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
            <Card>
                <CardHeader>
                    <CardTitle>Subscription Plan</CardTitle>
                    <CardDescription>
                        You are currently on the <strong>Free</strong> plan. Upgrade to unlock
                        more features.
                    </CardDescription>
                </CardHeader>
                <CardContent className="grid gap-6">
                    <RadioGroup defaultValue="free" className="grid grid-cols-3 gap-4">
                        <div>
                            <RadioGroupItem value="free" id="free" className="peer sr-only" />
                            <Label
                                htmlFor="free"
                                className="border-muted bg-popover hover:bg-accent hover:text-accent-foreground peer-data-[state=checked]:border-primary [&:has([data-state=checked])]:border-primary flex flex-col items-center justify-between rounded-md border-2 p-4 pt-10 pb-4 h-full"
                            >
                                <span className="mt-2 font-semibold">Free</span>
                                <span className="text-sm font-normal text-muted-foreground">$0 / month</span>
                            </Label>
                        </div>
                        <div>
                            <RadioGroupItem value="pro" id="pro" className="peer sr-only" />
                            <Label
                                htmlFor="pro"
                                className="border-muted bg-popover hover:bg-accent hover:text-accent-foreground peer-data-[state=checked]:border-primary [&:has([data-state=checked])]:border-primary flex flex-col items-center justify-between rounded-md border-2 p-4 pt-10 pb-4 h-full"
                            >
                                <span className="mt-2 font-semibold">Pro</span>
                                <span className="text-sm font-normal text-muted-foreground">$29 / month</span>
                            </Label>
                        </div>
                        <div>
                            <RadioGroupItem value="enterprise" id="enterprise" className="peer sr-only" />
                            <Label
                                htmlFor="enterprise"
                                className="border-muted bg-popover hover:bg-accent hover:text-accent-foreground peer-data-[state=checked]:border-primary [&:has([data-state=checked])]:border-primary flex flex-col items-center justify-between rounded-md border-2 p-4 pt-10 pb-4 h-full"
                            >
                                <span className="mt-2 font-semibold">Enterprise</span>
                                <span className="text-sm font-normal text-muted-foreground">Custom</span>
                            </Label>
                        </div>
                    </RadioGroup>
                </CardContent>
                <CardFooter>
                    <Button className="w-full">Upgrade Plan</Button>
                </CardFooter>
            </Card>

            <Card>
                <CardHeader>
                    <CardTitle>Payment Method</CardTitle>
                    <CardDescription>
                        Manage your payment methods.
                    </CardDescription>
                </CardHeader>
                <CardContent>
                    <div className="flex items-center space-x-4 rounded-md border p-4">
                        <div className="flex-1 space-y-1">
                            <p className="text-sm font-medium leading-none">
                                Visa ending in 4242
                            </p>
                            <p className="text-sm text-muted-foreground">
                                Expires 12/2024
                            </p>
                        </div>
                        <Button variant="outline" size="sm">Update</Button>
                    </div>
                </CardContent>
            </Card>
        </form>
    )
}
