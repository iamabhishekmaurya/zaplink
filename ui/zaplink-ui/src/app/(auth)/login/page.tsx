'use client'
import { LoginForm } from "@/components/(form)/login-form"
import { Particles } from "@/components/ui/particles"
import { useTheme } from "next-themes"
import Image from "next/image"
import { useEffect, useState } from "react"
import { useRouter } from "next/navigation"

export default function LoginPage() {
  const { resolvedTheme } = useTheme()
  const [color, setColor] = useState("#ffffff")
  const router = useRouter()
  
  useEffect(() => {
    setColor(resolvedTheme === "dark" ? "#ffffff" : "#000000")
  }, [resolvedTheme])

  // Check if user is already authenticated and redirect
  useEffect(() => {
    // This would typically check auth state
    // For now, we'll assume no redirect is needed for login page
  }, [router])

  return (
    <div className="bg-muted flex min-h-svh flex-col items-center justify-center p-6 md:p-10">
      <div className="flex flex-col w-full gap-6 max-w-sm md:max-w-4xl z-10">
        <a href="/" className="flex items-center gap-2 self-center font-medium">
          <Image 
            src="/logo.png" 
            alt="Zaplink Logo" 
            width={24} 
            height={24}
            className="transition-transform hover:scale-105"
            onError={(e) => {
              // Fallback for missing logo
              e.currentTarget.src = "/api/placeholder/24x24.png"
            }}
          />
          <span className="text-base font-semibold text-foreground">Zaplink</span>
        </a>
        <LoginForm />
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
