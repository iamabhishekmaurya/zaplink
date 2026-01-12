'use client'
import { ForgotPasswordForm } from "@/components/(form)/forgot-password-form"
import { LoginForm } from "@/components/(form)/login-form"
import { Particles } from "@/components/ui/particles"
import { GalleryVerticalEnd } from "lucide-react"
import { useTheme } from "next-themes"
import { useEffect, useState } from "react"

export default function LoginPage() {
  const { resolvedTheme } = useTheme()
  const [color, setColor] = useState("#ffffff")
  useEffect(() => {
    setColor(resolvedTheme === "dark" ? "#ffffff" : "#000000")
  }, [resolvedTheme])
  return (
    <div className="bg-muted flex min-h-svh flex-col items-center justify-center p-6 md:p-10">
      <div className="flex w-full max-w-md flex-col gap-6 z-10">
        <a href="/" className="flex items-center gap-2 self-center font-medium">
          <div className="bg-primary text-primary-foreground flex size-6 items-center justify-center rounded-md">
            <GalleryVerticalEnd className="size-4" />
          </div>
          Acme Inc.
        </a>
        <ForgotPasswordForm />
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
