'use client';

import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from "@/components/ui/button"
import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"
import {
    Field,
    FieldGroup,
    FieldLabel,
    FieldSeparator, // Not typically used but kept for consistency if needed
} from "@/components/ui/field"
import { Input } from "@/components/ui/input"
import { cn } from "@/lib/utils"
import { useDispatch, useSelector } from 'react-redux';
import { resetPassword } from '@/store/slices/authSlice';
import { AppDispatch, RootState } from '@/store';
import { Loader2 } from 'lucide-react';
import { toast } from 'sonner';
import { useRouter, useSearchParams } from 'next/navigation';
import { Particles } from "@/components/ui/particles";
import { GalleryVerticalEnd } from "lucide-react";
import { useTheme } from "next-themes";
import { useEffect, useState } from "react";

const resetPasswordSchema = z.object({
    newPassword: z.string().min(8, { message: "Password must be at least 8 characters" }),
    confirmPassword: z.string()
}).refine((data) => data.newPassword === data.confirmPassword, {
    message: "Passwords don't match",
    path: ["confirmPassword"],
});

type ResetPasswordSchema = z.infer<typeof resetPasswordSchema>;

export default function ResetPasswordPage() {
    const dispatch = useDispatch<AppDispatch>();
    const router = useRouter();
    const searchParams = useSearchParams();
    const token = searchParams.get('token');
    const { isLoading } = useSelector((state: RootState) => state.auth);
    const { resolvedTheme } = useTheme();
    const [color, setColor] = useState("#ffffff");

    useEffect(() => {
        setColor(resolvedTheme === "dark" ? "#ffffff" : "#000000");
    }, [resolvedTheme]);

    const { register, handleSubmit, formState: { errors } } = useForm<ResetPasswordSchema>({
        resolver: zodResolver(resetPasswordSchema),
    });

    const onSubmit = async (data: ResetPasswordSchema) => {
        if (!token) {
            toast.error("Invalid reset token.");
            return;
        }
        try {
            const resultAction = await dispatch(resetPassword({
                token: token,
                newPassword: data.newPassword,
                confirmPassword: data.confirmPassword
            }));

            if (resetPassword.fulfilled.match(resultAction)) {
                toast.success("Password reset successful!");
                router.push('/login');
            } else {
                toast.error(resultAction.payload as string || "Password reset failed");
            }
        } catch (err) {
            toast.error("An unexpected error occurred");
        }
    };

    return (
        <div className="bg-muted flex min-h-svh flex-col items-center justify-center p-6 md:p-10">
            <div className="flex flex-col w-full gap-6 max-w-sm md:max-w-4xl z-10">
                <a href="/" className="flex items-center gap-2 self-center font-medium">
                    <div className="bg-primary text-primary-foreground flex size-6 items-center justify-center rounded-md">
                        <GalleryVerticalEnd className="size-4" />
                    </div>
                    Acme Inc.
                </a>

                <div className={cn("flex flex-col gap-6")}>
                    <Card>
                        <CardHeader className="text-center">
                            <CardTitle className="text-xl">Set new password</CardTitle>
                            <CardDescription>Enter your new password below.</CardDescription>
                        </CardHeader>
                        <CardContent>
                            <form onSubmit={handleSubmit(onSubmit)}>
                                <FieldGroup>
                                    <Field>
                                        <FieldLabel htmlFor="newPassword">New Password</FieldLabel>
                                        <Input
                                            id="newPassword"
                                            type="password"
                                            {...register("newPassword")}
                                        />
                                        {errors.newPassword && (
                                            <span className="text-xs text-red-500">{errors.newPassword.message}</span>
                                        )}
                                    </Field>
                                    <Field>
                                        <FieldLabel htmlFor="confirmPassword">Confirm Password</FieldLabel>
                                        <Input
                                            id="confirmPassword"
                                            type="password"
                                            {...register("confirmPassword")}
                                        />
                                        {errors.confirmPassword && (
                                            <span className="text-xs text-red-500">{errors.confirmPassword.message}</span>
                                        )}
                                    </Field>
                                    <Field>
                                        <Button type="submit" className="w-full" disabled={isLoading}>
                                            {isLoading ? <Loader2 className="mr-2 h-4 w-4 animate-spin" /> : null}
                                            Reset Password
                                        </Button>
                                    </Field>
                                </FieldGroup>
                            </form>
                        </CardContent>
                    </Card>
                    <p className="px-6 text-center text-sm text-muted-foreground">
                        Remember your password? <a href="/login" className="underline underline-offset-2">Sign in</a>
                    </p>
                </div>
            </div>
            <Particles
                className="absolute inset-0 z-0"
                quantity={100}
                ease={80}
                color={color}
                refresh
            />
        </div>
    )
}
