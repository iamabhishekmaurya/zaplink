"use client"

import { useEffect, useState } from "react"
import { useRouter } from "next/navigation"
import { bioPageService } from "@/services/bioPageService"
import { useAuth } from "@/hooks/useAuth"
import { Button } from "@/components/ui/button"
import { Plus, ExternalLink, Edit, Trash2, Loader2, Search, MoreVertical, Eye } from "lucide-react"
import { Card, CardContent } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import { toast } from "sonner"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { MiniBioPreview } from "@/features/bio-page/ui/components/mini-bio-preview"
import { parseThemeConfig, defaultTheme } from "@/ui/design-system/theme-utils"
import { BioPageWithTheme } from "@/features/bio-page/types"

export default function BioPagesDashboard() {
  const [pages, setPages] = useState<BioPageWithTheme[]>([])
  const [loading, setLoading] = useState(true)
  const [searchQuery, setSearchQuery] = useState("")
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
      if (user?.id) {
        const data = await bioPageService.getBioPagesByOwnerId(String(user.id))
        // Parse themes for preview
        const pagesWithTheme = data.map(page => ({
          ...page,
          parsedTheme: { ...defaultTheme, ...(page.themeConfig as any) }
        }))
        setPages(pagesWithTheme)
      }
    } catch (error) {
      toast.error("Failed to load bio pages")
    } finally {
      setLoading(false)
    }
  }

  const handleDelete = async (id: string) => {
    try {
      await bioPageService.deleteBioPage(id)
      toast.success("Bio page deleted")
      loadPages() // Reload to refresh list
    } catch (error) {
      toast.error("Failed to delete bio page")
    }
  }

  const filteredPages = pages.filter(page =>
    page.username.toLowerCase().includes(searchQuery.toLowerCase()) ||
    (page.title?.toLowerCase() || "").includes(searchQuery.toLowerCase())
  )

  if (loading) {
    return (
      <div className="flex h-full items-center justify-center p-8">
        <Loader2 className="h-8 w-8 animate-spin text-primary" />
      </div>
    )
  }

  return (
    <div className="container p-6 space-y-8 max-w-7xl mx-auto">
      {/* Header & Toolbar */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Bio Pages</h1>
          <p className="text-muted-foreground mt-1">
            Manage and customize your Link-in-Bio pages.
          </p>
        </div>
        <div className="flex items-center gap-3">
          <div className="relative w-full md:w-64">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
            <Input
              placeholder="Search pages..."
              className="pl-9"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
            />
          </div>
          <Button onClick={() => router.push("/dashboard/bio-page/create")} className="shrink-0">
            <Plus className="mr-2 h-4 w-4" /> Create Page
          </Button>
        </div>
      </div>

      {pages.length === 0 ? (
        <Card className="border-dashed border-2">
          <CardContent className="flex flex-col items-center justify-center p-12 text-center py-24">
            <div className="rounded-full bg-primary/10 p-6 mb-4">
              <Plus className="h-10 w-10 text-primary" />
            </div>
            <h3 className="text-xl font-semibold mb-2">No Bio Pages Yet</h3>
            <p className="text-muted-foreground mb-6 max-w-sm">
              Create your first Link-in-Bio page to simplify your online presence.
            </p>
            <Button onClick={() => router.push("/dashboard/bio-page/create")}>
              Create Page
            </Button>
          </CardContent>
        </Card>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {filteredPages.map((page) => (
            <Card key={page.id} className="group overflow-hidden border hover:border-primary/50 transition-all hover:shadow-lg flex flex-col">
              {/* Preview Section */}
              <div
                className="relative aspect-[4/3] bg-muted/30 overflow-hidden cursor-pointer group-hover:bg-muted/50 transition-colors"
                onClick={() => router.push(`/dashboard/bio-page/${page.id}`)}
              >
                <div className="absolute inset-0 p-6 flex items-center justify-center transform group-hover:scale-105 transition-transform duration-500">
                  <div className="w-full max-w-[160px] shadow-2xl rounded-lg overflow-hidden ring-4 ring-background/80">
                    <MiniBioPreview page={page} />
                  </div>
                </div>

                {/* Hover Overlay */}
                <div className="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center gap-2">
                  <Button size="sm" variant="secondary" className="h-8 shadow-lg" onClick={(e) => {
                    e.stopPropagation();
                    router.push(`/dashboard/bio-page/${page.id}`);
                  }}>
                    <Edit className="h-3.5 w-3.5 mr-1.5" /> Edit
                  </Button>
                  <Button size="sm" variant="secondary" className="h-8 shadow-lg" onClick={(e) => {
                    e.stopPropagation();
                    window.open(`/${page.username}`, '_blank');
                  }}>
                    <ExternalLink className="h-3.5 w-3.5 mr-1.5" /> View
                  </Button>
                </div>
              </div>

              {/* Content Section */}
              <div className="p-4 flex-1 flex flex-col">
                <div className="flex items-start justify-between mb-2">
                  <div className="min-w-0 pr-2">
                    <h3 className="font-semibold text-base truncate" title={page.title || "@" + page.username}>
                      {page.title || "@" + page.username}
                    </h3>
                    <p className="text-sm text-muted-foreground truncate">@{page.username}</p>
                  </div>
                  <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                      <Button variant="ghost" size="icon" className="h-8 w-8 -mr-2 text-muted-foreground shrink-0">
                        <MoreVertical className="h-4 w-4" />
                      </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent align="end">
                      <DropdownMenuItem onClick={() => router.push(`/dashboard/bio-page/${page.id}`)}>
                        <Edit className="h-4 w-4 mr-2" /> Edit Page
                      </DropdownMenuItem>
                      <DropdownMenuItem onClick={() => window.open(`/${page.username}`, '_blank')}>
                        <ExternalLink className="h-4 w-4 mr-2" /> Visit Link
                      </DropdownMenuItem>
                      <DropdownMenuItem className="text-destructive focus:text-destructive" onClick={() => handleDelete(page.id)}>
                        <Trash2 className="h-4 w-4 mr-2" /> Delete
                      </DropdownMenuItem>
                    </DropdownMenuContent>
                  </DropdownMenu>
                </div>

                <div className="mt-auto flex items-center justify-between text-xs text-muted-foreground pt-3 border-t">
                  <div className="flex items-center gap-1.5">
                    <Badge variant="outline" className="font-normal text-[10px] h-5">
                      {page.bioLinks?.length || 0} Links
                    </Badge>
                    {page.isPublic && (
                      <Badge variant="secondary" className="bg-green-500/10 text-green-600 hover:bg-green-500/20 font-normal text-[10px] h-5 border-0">
                        Active
                      </Badge>
                    )}
                  </div>
                  <div className="flex items-center gap-1">
                    <Eye className="h-3 w-3" /> 0 views
                  </div>
                </div>
              </div>
            </Card>
          ))}
        </div>
      )}
    </div>
  )
}
