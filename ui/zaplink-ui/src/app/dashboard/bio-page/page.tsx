"use client"

import { useEffect, useState } from "react"
import { useRouter } from "next/navigation"
import { bioPageService, BioPage } from "@/services/bioPageService"
import { useAuth } from "@/hooks/useAuth"
import { Button } from "@/components/ui/button"
import { Plus, ExternalLink, Edit, Trash2, Loader2 } from "lucide-react"
import { Card, CardContent, CardHeader, CardTitle, CardDescription, CardFooter } from "@/components/ui/card"
import { toast } from "sonner"
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from "@/components/ui/alert-dialog"

export default function BioPagesDashboard() {
  const [pages, setPages] = useState<BioPage[]>([])
  const [loading, setLoading] = useState(true)
  const { user } = useAuth()
  const router = useRouter()

  useEffect(() => {
    if (user?.id) {
      loadPages()
    }
  }, [user?.id])

  const loadPages = async () => {
    try {
      setLoading(true)
      // Assuming user.id is the ownerId
      if (user?.id) {
        const data = await bioPageService.getBioPagesByOwnerId(String(user.id))
        setPages(data)
      }
    } catch (error) {
      toast.error("Failed to load bio pages")
    } finally {
      setLoading(false)
    }
  }

  const handleDelete = async (id: number) => {
    try {
      await bioPageService.deleteBioPage(id)
      toast.success("Bio page deleted")
      loadPages()
    } catch (error) {
      toast.error("Failed to delete bio page")
    }
  }

  if (loading) {
    return (
      <div className="flex h-full items-center justify-center p-8">
        <Loader2 className="h-8 w-8 animate-spin text-primary" />
      </div>
    )
  }

  return (
    <div className="container p-6 space-y-8 max-w-7xl">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Bio Pages</h1>
          <p className="text-muted-foreground mt-2">
            Manage your Link-in-Bio pages.
          </p>
        </div>
        <Button onClick={() => router.push("/dashboard/bio-page/create")}>
          <Plus className="mr-2 h-4 w-4" /> Create Page
        </Button>
      </div>

      {pages.length === 0 ? (
        <Card className="border-dashed">
          <CardContent className="flex flex-col items-center justify-center p-12 text-center">
            <div className="rounded-full bg-primary/10 p-4 mb-4">
              <Plus className="h-8 w-8 text-primary" />
            </div>
            <h3 className="text-lg font-semibold mb-2">No Bio Pages Yet</h3>
            <p className="text-muted-foreground mb-6 max-w-sm">
              Create your first Link-in-Bio page to showcase your links and profile.
            </p>
            <Button onClick={() => router.push("/dashboard/bio-page/create")}>
              Create Page
            </Button>
          </CardContent>
        </Card>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {pages.map((page) => (
            <Card key={page.id} className="group hover:shadow-lg transition-shadow">
              <CardHeader>
                <CardTitle className="flex justify-between items-start">
                  <span className="truncate">@{page.username}</span>
                </CardTitle>
                <CardDescription className="line-clamp-2">
                  {page.bioText || "No bio description"}
                </CardDescription>
              </CardHeader>
              <CardContent>
                <div className="text-sm text-muted-foreground">
                  {page.bioLinks?.length || 0} links
                </div>
              </CardContent>
              <CardFooter className="flex justify-between border-t p-4 bg-muted/50">
                <Button
                  variant="ghost"
                  size="sm"
                  className="text-muted-foreground hover:text-primary"
                  onClick={() => window.open(`/${page.username}`, '_blank')}
                >
                  <ExternalLink className="h-4 w-4 mr-2" /> View
                </Button>
                <div className="flex gap-2">
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => router.push(`/dashboard/bio-page/${page.id}`)}
                  >
                    <Edit className="h-4 w-4 mr-2" /> Edit
                  </Button>

                  <AlertDialog>
                    <AlertDialogTrigger asChild>
                      <Button variant="destructive" size="sm">
                        <Trash2 className="h-4 w-4" />
                      </Button>
                    </AlertDialogTrigger>
                    <AlertDialogContent>
                      <AlertDialogHeader>
                        <AlertDialogTitle>Are you sure?</AlertDialogTitle>
                        <AlertDialogDescription>
                          This will permanently delete your bio page and all its links.
                          This action cannot be undone.
                        </AlertDialogDescription>
                      </AlertDialogHeader>
                      <AlertDialogFooter>
                        <AlertDialogCancel>Cancel</AlertDialogCancel>
                        <AlertDialogAction onClick={() => handleDelete(page.id)}>
                          Delete
                        </AlertDialogAction>
                      </AlertDialogFooter>
                    </AlertDialogContent>
                  </AlertDialog>
                </div>
              </CardFooter>
            </Card>
          ))}
        </div>
      )}
    </div>
  )
}
