"use client"

import {
  CircleCheckIcon,
  InfoIcon,
  Loader2Icon,
  OctagonXIcon,
  TriangleAlertIcon,
} from "lucide-react"
import { useTheme } from "next-themes"
import { Toaster as Sonner, type ToasterProps } from "sonner"

const Toaster = ({ ...props }: ToasterProps) => {
  const { theme = "system" } = useTheme()

  return (
    <Sonner
      theme={theme as ToasterProps["theme"]}
      className="toaster group"
      position="top-right"
      icons={{
        success: <CircleCheckIcon className="size-4 text-green-600 dark:text-green-400" />,
        info: <InfoIcon className="size-4 text-blue-600 dark:text-blue-400" />,
        warning: <TriangleAlertIcon className="size-4 text-yellow-600 dark:text-yellow-400" />,
        error: <OctagonXIcon className="size-4 text-red-600 dark:text-red-400" />,
        loading: <Loader2Icon className="size-4 animate-spin text-blue-600 dark:text-blue-400" />,
      }}
      style={
        {
          "--normal-bg": "var(--popover)",
          "--normal-text": "var(--popover-foreground)",
          "--normal-border": "var(--border)",
          "--success-bg": "hsl(142 76% 96%)",
          "--success-text": "hsl(142 76% 28%)",
          "--success-border": "hsl(142 76% 88%)",
          "--info-bg": "hsl(214 95% 96%)",
          "--info-text": "hsl(214 95% 28%)",
          "--info-border": "hsl(214 95% 88%)",
          "--warning-bg": "hsl(38 95% 96%)",
          "--warning-text": "hsl(38 95% 28%)",
          "--warning-border": "hsl(38 95% 88%)",
          "--error-bg": "hsl(0 93% 96%)",
          "--error-text": "hsl(0 93% 28%)",
          "--error-border": "hsl(0 93% 88%)",
          "--border-radius": "var(--radius)",
        } as React.CSSProperties
      }
      {...props}
    />
  )
}

export { Toaster }
