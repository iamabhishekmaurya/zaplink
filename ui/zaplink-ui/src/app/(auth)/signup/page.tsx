'use client'
import { SignupForm } from "@/components/(form)/signup-form"
import { Particles } from "@/components/ui/particles"
import { useTheme } from "next-themes"
import Image from "next/image"
import { useEffect, useState } from "react"

export default function SignupPage() {
    const { resolvedTheme } = useTheme()
    const [color, setColor] = useState("#ffffff")
    useEffect(() => {
        setColor(resolvedTheme === "dark" ? "#ffffff" : "#000000")
    }, [resolvedTheme])
    return (
        <div className="bg-muted flex min-h-svh flex-col items-center justify-center p-6 md:p-10">
            <div className="flex flex-col gap-6 w-full max-w-sm md:max-w-4xl z-10">
                <a href="/" className="flex items-center gap-2 self-center font-medium">
                    <Image src="/logo-light.png" alt="Logo" width={24} height={24} className="block dark:hidden" />
                    <Image src="/logo-dark.png" alt="Logo" width={24} height={24} className="hidden dark:block" />
                    <span className="text-base font-semibold text-foreground">zaipme</span>
                </a>
                <SignupForm />
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
