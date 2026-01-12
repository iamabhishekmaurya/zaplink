"use client"

import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import {
  showSuccessToast,
  showErrorToast,
  showWarningToast,
  showInfoToast,
  showLoadingToast,
  toast
} from "@/lib/toast"

export function ToastDemo() {
  const handleSuccess = () => {
    showSuccessToast("Operation successful!", "Your changes have been saved.")
  }

  const handleError = () => {
    showErrorToast("Operation failed!", "Please check your connection and try again.")
  }

  const handleWarning = () => {
    showWarningToast("Warning!", "This action cannot be undone.")
  }

  const handleInfo = () => {
    showInfoToast("Information", "New features are available in the settings.")
  }

  const handleLoading = () => {
    const id = showLoadingToast("Processing...", "Please wait while we process your request.")
    
    // Simulate async operation
    setTimeout(() => {
      toast.dismiss(id)
      showSuccessToast("Complete!", "Your request has been processed successfully.")
    }, 3000)
  }

  const handleWithAction = () => {
    toast.success("File uploaded!", {
      description: "Your file has been uploaded successfully.",
      action: {
        label: "View",
        onClick: () => console.log("View file")
      }
    })
  }

  return (
    <Card className="w-full max-w-md">
      <CardHeader>
        <CardTitle>Toast Notifications Demo</CardTitle>
        <CardDescription>
          Click the buttons below to see different toast notification types.
        </CardDescription>
      </CardHeader>
      <CardContent className="grid gap-4">
        <div className="grid grid-cols-2 gap-2">
          <Button onClick={handleSuccess} variant="default">
            Success
          </Button>
          <Button onClick={handleError} variant="destructive">
            Error
          </Button>
          <Button onClick={handleWarning} variant="secondary">
            Warning
          </Button>
          <Button onClick={handleInfo} variant="outline">
            Info
          </Button>
        </div>
        <div className="grid gap-2">
          <Button onClick={handleLoading} variant="outline">
            Loading (with auto-dismiss)
          </Button>
          <Button onClick={handleWithAction} variant="default">
            Success with Action
          </Button>
        </div>
      </CardContent>
    </Card>
  )
}
