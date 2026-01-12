"use client"

import { toast as sonnerToast } from "sonner"

interface ToastOptions {
  description?: string
  action?: {
    label: string
    onClick: () => void
  }
}

export const toast = {
  success: (message: string, options?: ToastOptions) => {
    return sonnerToast.success(message, {
      description: options?.description,
      action: options?.action,
    })
  },

  error: (message: string, options?: ToastOptions) => {
    return sonnerToast.error(message, {
      description: options?.description,
      action: options?.action,
    })
  },

  warning: (message: string, options?: ToastOptions) => {
    return sonnerToast.warning(message, {
      description: options?.description,
      action: options?.action,
    })
  },

  info: (message: string, options?: ToastOptions) => {
    return sonnerToast.info(message, {
      description: options?.description,
      action: options?.action,
    })
  },

  loading: (message: string, options?: ToastOptions) => {
    return sonnerToast.loading(message, {
      description: options?.description,
    })
  },

  dismiss: (id?: string | number) => {
    return sonnerToast.dismiss(id)
  }
}

// Helper functions for common use cases
export const showSuccessToast = (title: string, description?: string) => {
  return toast.success(title, { description })
}

export const showErrorToast = (title: string, description?: string) => {
  return toast.error(title, { description })
}

export const showWarningToast = (title: string, description?: string) => {
  return toast.warning(title, { description })
}

export const showInfoToast = (title: string, description?: string) => {
  return toast.info(title, { description })
}

export const showLoadingToast = (title: string, description?: string) => {
  return toast.loading(title, { description })
}
