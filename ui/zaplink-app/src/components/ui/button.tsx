import * as React from "react"
import { Slot } from "@radix-ui/react-slot"
import { cva, type VariantProps } from "class-variance-authority"

import { cn } from "@/lib/utils"

const buttonVariants = cva(
  "inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-xl text-sm font-medium transition-all duration-300 disabled:pointer-events-none disabled:opacity-50 [&_svg]:pointer-events-none [&_svg:not([class*='size-'])]:size-4 shrink-0 [&_svg]:shrink-0 outline-none focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px] aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive",
  {
    variants: {
      variant: {
        default: "gradient-primary text-primary-foreground hover:shadow-lg hover:scale-105 border-0",
        destructive:
          "bg-gradient-to-r from-rose-500 to-red-500 text-white hover:from-rose-600 hover:to-red-600 focus-visible:ring-destructive/20 dark:focus-visible:ring-destructive/40 shadow-lg",
        outline:
          "border-2 bg-background/50 backdrop-blur-sm hover:bg-accent hover:text-accent-foreground hover:border-accent hover:shadow-md dark:bg-input/30 dark:border-input dark:hover:bg-input/50",
        secondary:
          "bg-gradient-to-r from-slate-100 to-slate-200 text-slate-900 hover:from-slate-200 hover:to-slate-300 shadow-md hover:shadow-lg",
        ghost:
          "hover:bg-accent/50 hover:text-accent-foreground dark:hover:bg-accent/20",
        link: "text-primary underline-offset-4 hover:underline hover:text-primary/80",
        glass: "glass-effect border border-white/20 text-white hover:bg-white/20 hover:shadow-lg",
        gradient: "bg-gradient-to-r from-primary to-accent text-white hover:shadow-lg hover:scale-105 border-0",
      },
      size: {
        default: "h-10 px-6 py-2 rounded-xl",
        sm: "h-8 rounded-lg px-4 text-sm",
        lg: "h-12 rounded-xl px-8 text-lg font-bold",
        icon: "size-10 rounded-xl",
        "icon-sm": "size-8 rounded-lg",
        "icon-lg": "size-12 rounded-xl",
      },
    },
    defaultVariants: {
      variant: "default",
      size: "default",
    },
  }
)

function Button({
  className,
  variant = "default",
  size = "default",
  asChild = false,
  ...props
}: React.ComponentProps<"button"> &
  VariantProps<typeof buttonVariants> & {
    asChild?: boolean
  }) {
  const Comp = asChild ? Slot : "button"

  return (
    <Comp
      data-slot="button"
      data-variant={variant}
      data-size={size}
      className={cn(buttonVariants({ variant, size, className }))}
      {...props}
    />
  )
}

export { Button, buttonVariants }
