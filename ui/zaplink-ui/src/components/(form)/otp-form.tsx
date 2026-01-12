
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
    FieldDescription,
    FieldGroup,
    FieldLabel,
} from "@/components/ui/field"
import {
    InputOTP,
    InputOTPGroup,
    InputOTPSeparator,
    InputOTPSlot,
} from "@/components/ui/input-otp"
import { cn } from "@/lib/utils"
import { useDispatch, useSelector } from 'react-redux';
import { verifyEmail, resendVerification } from '@/store/slices/authSlice';
import { AppDispatch, RootState } from '@/store';
import { Loader2 } from 'lucide-react';
import { showSuccessToast, showErrorToast } from '@/lib/toast';
import { useRouter, useSearchParams } from 'next/navigation';
import { useEffect } from 'react';

const otpSchema = z.object({
    code: z.string().min(6, { message: "Verification code must be 6 characters" }),
});

type OtpSchema = z.infer<typeof otpSchema>;

export function OTPForm({ className, email: propEmail, ...props }: React.ComponentProps<"div"> & { email?: string | null }) {
    const dispatch = useDispatch<AppDispatch>();
    const router = useRouter();
    const searchParams = useSearchParams();
    const urlToken = searchParams.get('token');
    const email = propEmail || searchParams.get('email'); // Use prop email first, then fallback to URL param
    const { isLoading } = useSelector((state: RootState) => state.auth);

    const { setValue, handleSubmit, watch, formState: { errors } } = useForm<OtpSchema>({
        resolver: zodResolver(otpSchema),
        defaultValues: {
            code: urlToken || '',
        }
    });

    // Auto-fill if token is in URL (and handle auto-submit if desired, but user interaction is safer for now)
    useEffect(() => {
        if (urlToken) {
            setValue('code', urlToken);
        }
    }, [urlToken, setValue]);

    const onSubmit = async (data: OtpSchema) => {
        try {
            const resultAction = await dispatch(verifyEmail(data.code));
            if (verifyEmail.fulfilled.match(resultAction)) {
                showSuccessToast("Account verified successfully!");
                router.push('/dashboard');
            } else {
                showErrorToast(resultAction.payload as string || "Verification failed");
            }
        } catch (err) {
            showErrorToast("An unexpected error occurred");
        }
    };

    const handleResend = async () => {
        if (!email) {
            showErrorToast("Email address is missing to resend code.");
            return;
        }
        try {
            const resultAction = await dispatch(resendVerification(email));
            if (resendVerification.fulfilled.match(resultAction)) {
                showSuccessToast("Verification code resent!");
            } else {
                showErrorToast(resultAction.payload as string || "Failed to resend code");
            }
        } catch (err) {
            showErrorToast("An unexpected error occurred");
        }
    }

    return (
        <div className={cn("flex flex-col gap-6", className)} {...props}>
            <Card {...props}>
                <CardHeader className="text-center">
                    <CardTitle className="text-xl">Enter verification code</CardTitle>
                    <CardDescription>We sent a 6-digit code to {email || 'your email'}.</CardDescription>
                </CardHeader>
                <CardContent>
                    <form onSubmit={handleSubmit(onSubmit)}>
                        <FieldGroup>
                            <Field>
                                <FieldLabel htmlFor="otp" className="sr-only">
                                    Verification code
                                </FieldLabel>
                                <InputOTP
                                    maxLength={6}
                                    id="otp"
                                    value={watch('code')}
                                    onChange={(val) => setValue('code', val)}
                                    containerClassName="gap-4 justify-center"
                                >
                                    <InputOTPGroup className="gap-2.5 *:data-[slot=input-otp-slot]:h-16 *:data-[slot=input-otp-slot]:w-12 *:data-[slot=input-otp-slot]:rounded-md *:data-[slot=input-otp-slot]:border *:data-[slot=input-otp-slot]:text-xl">
                                        <InputOTPSlot index={0} />
                                        <InputOTPSlot index={1} />
                                        <InputOTPSlot index={2} />
                                    </InputOTPGroup>
                                    <InputOTPSeparator />
                                    <InputOTPGroup className="gap-2.5 *:data-[slot=input-otp-slot]:h-16 *:data-[slot=input-otp-slot]:w-12 *:data-[slot=input-otp-slot]:rounded-md *:data-[slot=input-otp-slot]:border *:data-[slot=input-otp-slot]:text-xl">
                                        <InputOTPSlot index={3} />
                                        <InputOTPSlot index={4} />
                                        <InputOTPSlot index={5} />
                                    </InputOTPGroup>
                                </InputOTP>
                                {errors.code && (
                                    <span className="text-xs text-red-500 text-center block mt-2">{errors.code.message}</span>
                                )}
                                <FieldDescription className="text-center mt-2">
                                    Didn&apos;t receive the code? <button type="button" onClick={handleResend} className="underline underline-offset-2">Resend</button>
                                </FieldDescription>
                            </Field>
                            <Field>
                                <Button type="submit" className="w-full" disabled={isLoading}>
                                    {isLoading ? <Loader2 className="mr-2 h-4 w-4 animate-spin" /> : null}
                                    Verify
                                </Button>
                            </Field>
                        </FieldGroup>
                    </form>
                </CardContent>
            </Card>
            <FieldDescription className="px-6 text-center">
                By clicking continue, you agree to our <a href="#">Terms of Service</a>{" "}
                and <a href="#">Privacy Policy</a>.
            </FieldDescription>
        </div>
    )
}
