"use client"

import { useState } from "react"
import { useRouter } from "next/navigation"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { ArrowLeft, Users } from "lucide-react"
import { CreateBioPageDialog } from '@/features/bio-page/ui/create-bio-page-dialog'
import { showSuccessToast, showErrorToast } from "@/lib/toast"

export default function CreateBioPage() {
  const router = useRouter()
  const [showCreateDialog, setShowCreateDialog] = useState(false)

  const handleCreatePage = async (pageData: any) => {
    try {
      const response = await fetch('/api/v1/bio-pages', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          ...pageData,
          ownerId: 'user123' // TODO: Get from auth
        }),
      })

      if (response.ok) {
        showSuccessToast("Success", "Bio page created successfully")
        router.push('/dashboard/bio-page')
      }
    } catch (error) {
      showErrorToast("Error", "Failed to create bio page")
    }
  }

  return (
    <div className="flex-1 space-y-4 p-4 md:p-8 pt-6">
      <div className="flex items-center gap-4">
        <Button
          variant="outline"
          size="sm"
          onClick={() => router.back()}
        >
          <ArrowLeft className="h-4 w-4 mr-2" />
          Back
        </Button>
        <div>
          <h2 className="text-3xl font-bold tracking-tight">Create New Bio Page</h2>
          <p className="text-muted-foreground">
            Create a new bio page to share your links and content
          </p>
        </div>
      </div>

      <div className="max-w-2xl">
        <Card>
          <CardContent className="flex flex-col items-center justify-center py-12">
            <Users className="h-12 w-12 text-muted-foreground mb-4" />
            <h3 className="text-lg font-semibold mb-2">Create Your Bio Page</h3>
            <p className="text-muted-foreground text-center mb-6">
              Set up your personalized bio page to share all your important links in one place
            </p>
            <Button 
              onClick={() => setShowCreateDialog(true)}
              size="lg"
            >
              Create Bio Page
            </Button>
          </CardContent>
        </Card>
      </div>

      <CreateBioPageDialog
        open={showCreateDialog}
        onOpenChange={setShowCreateDialog}
        onCreatePage={handleCreatePage}
      />
    </div>
  )
}
