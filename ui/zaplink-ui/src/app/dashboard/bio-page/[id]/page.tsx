"use client"

import { useEffect, useState } from "react"
import { useParams, useRouter } from "next/navigation"
import { bioPageService, BioPage } from "@/services/bioPageService"
import { EditorLayout } from "@/features/bio-page/ui/editor-layout"
import { Loader2, AlertCircle } from "lucide-react"
import { handleApiError } from "@/lib/error-handler"
import { Button } from "@/components/ui/button"
import { Alert, AlertDescription } from "@/components/ui/alert"

export default function EditBioPage() {
    const params = useParams()
    const router = useRouter()
    const id = params.id as string
    const [page, setPage] = useState<BioPage | null>(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        if (id) {
            loadPage()
        }
    }, [id])

    const loadPage = async () => {
        try {
            setLoading(true)
            setError(null)
            const data = await bioPageService.getBioPageById(id)
            setPage(data)
        } catch (error) {
            const errorMessage = error instanceof Error ? error.message : 'Failed to load page'
            setError(errorMessage)
            handleApiError(error, 'Failed to load bio page')
        } finally {
            setLoading(false)
        }
    }

    const handleRetry = () => {
        loadPage()
    }

    const handleGoBack = () => {
        router.push('/dashboard/bio-page')
    }

    if (loading) {
        return (
            <div className="flex h-[calc(100vh-4rem)] items-center justify-center">
                <div className="text-center space-y-4">
                    <Loader2 className="h-8 w-8 animate-spin mx-auto" />
                    <p className="text-muted-foreground">Loading bio page...</p>
                </div>
            </div>
        )
    }

    if (error) {
        return (
            <div className="flex h-[calc(100vh-4rem)] items-center justify-center p-6">
                <div className="max-w-md w-full space-y-6">
                    <Alert variant="destructive">
                        <AlertCircle className="h-4 w-4" />
                        <AlertDescription>
                            {error}
                        </AlertDescription>
                    </Alert>
                    
                    <div className="flex gap-3">
                        <Button onClick={handleRetry} variant="outline">
                            Try Again
                        </Button>
                        <Button onClick={handleGoBack}>
                            Go Back
                        </Button>
                    </div>
                </div>
            </div>
        )
    }

    if (!page) {
        return (
            <div className="flex h-[calc(100vh-4rem)] items-center justify-center p-6">
                <div className="text-center space-y-6">
                    <AlertCircle className="h-12 w-12 text-muted-foreground mx-auto" />
                    <div>
                        <h2 className="text-lg font-semibold">Page Not Found</h2>
                        <p className="text-muted-foreground">The bio page you're looking for doesn't exist.</p>
                    </div>
                    <Button onClick={handleGoBack}>
                        Back to Bio Pages
                    </Button>
                </div>
            </div>
        )
    }

    return <EditorLayout initialData={page} />
}
