"use client"

import { toast } from "@/lib/toast"

export interface Toast {
  id?: string
  title?: string
  description?: string
  variant?: "default" | "destructive" | "success" | "warning"
  action?: {
    label: string
    onClick: () => void
  }
}

export interface UseToastReturn {
  toast: (props: Toast) => void
  dismiss: (toastId?: string) => void
}

export function useToast(): UseToastReturn {
  const dismiss = (toastId?: string) => {
    toast.dismiss(toastId)
  }

  const showToast = (props: Toast) => {
    const { title, description, action } = props

    if (!title) {
      return toast.info("", { description, action })
    }

    if (title.toLowerCase().includes("error") || title.toLowerCase().includes("failed")) {
      return toast.error(title, { description, action })
    } else if (title.toLowerCase().includes("warning")) {
      return toast.warning(title, { description, action })
    } else if (title.toLowerCase().includes("loading") || title.toLowerCase().includes("processing")) {
      return toast.loading(title, { description, action })
    } else {
      return toast.success(title, { description, action })
    }
  }

  return {
    toast: showToast,
    dismiss
  }
}
