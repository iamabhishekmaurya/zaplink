"use client"

import { useState, useEffect } from "react"
import { useParams } from "next/navigation"
import { Card, CardContent } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { ExternalLink, Loader2, User } from "lucide-react"

interface BioPageData {
  username: string
  avatar_url?: string
  bio_text?: string
  theme_config?: string
  links: Array<{
    title: string
    url?: string
    type: string
    is_active: boolean
    sort_order: number
    price?: number
    currency?: string
  }>
}

export default function BioPage() {
  const params = useParams()
  const username = params.username as string
  const [bioData, setBioData] = useState<BioPageData | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchBioData = async () => {
      try {
        setLoading(true)
        const response = await fetch(`/v1/bio/${username}`)
        if (response.ok) {
          const data = await response.json()
          setBioData(data)
        } else if (response.status === 404) {
          setError("Bio page not found")
        } else {
          setError("Failed to load bio page")
        }
      } catch (err) {
        setError("Error loading bio page")
      } finally {
        setLoading(false)
      }
    }

    if (username) {
      fetchBioData()
    }
  }, [username])

  if (loading) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center">
        <div className="text-center">
          <Loader2 className="h-8 w-8 animate-spin mx-auto mb-4" />
          <p className="text-muted-foreground">Loading bio page...</p>
        </div>
      </div>
    )
  }

  if (error || !bioData) {
    return (
      <div className="min-h-screen bg-background flex items-center justify-center">
        <Card className="max-w-md w-full mx-4">
          <CardContent className="p-8 text-center">
            <User className="h-16 w-16 mx-auto mb-4 text-muted-foreground" />
            <h1 className="text-2xl font-bold mb-2">Page Not Found</h1>
            <p className="text-muted-foreground mb-4">
              {error || "This bio page doesn't exist or has been removed."}
            </p>
            <a 
              href="/" 
              className="inline-flex items-center justify-center rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50 bg-primary text-primary-foreground hover:bg-primary/90 h-10 px-4 py-2"
            >
              Go Home
            </a>
          </CardContent>
        </Card>
      </div>
    )
  }

  // Parse theme config or use defaults
  let theme = {
    primaryColor: "#000000",
    backgroundColor: "#ffffff",
    textColor: "#000000"
  }

  try {
    if (bioData.theme_config) {
      theme = { ...theme, ...JSON.parse(bioData.theme_config) }
    }
  } catch (e) {
    // Use default theme if JSON is invalid
  }

  return (
    <div 
      className="min-h-screen"
      style={{ backgroundColor: theme.backgroundColor }}
    >
      <div className="container mx-auto px-4 py-8 max-w-md">
        <Card className="border-0 shadow-lg">
          <CardContent className="p-8">
            {/* Avatar and Bio */}
            <div className="text-center mb-8">
              {bioData.avatar_url ? (
                <img
                  src={bioData.avatar_url}
                  alt={`${bioData.username}'s avatar`}
                  className="w-24 h-24 rounded-full mx-auto mb-4 object-cover border-4 border-white shadow-lg"
                />
              ) : (
                <div className="w-24 h-24 rounded-full mx-auto mb-4 bg-muted flex items-center justify-center">
                  <User className="h-12 w-12 text-muted-foreground" />
                </div>
              )}
              <h1 
                className="text-3xl font-bold mb-2"
                style={{ color: theme.primaryColor }}
              >
                @{bioData.username}
              </h1>
              {bioData.bio_text && (
                <p 
                  className="text-lg leading-relaxed"
                  style={{ color: theme.textColor }}
                >
                  {bioData.bio_text}
                </p>
              )}
            </div>

            {/* Links */}
            <div className="space-y-4">
              {bioData.links
                .filter(link => link.is_active)
                .sort((a, b) => a.sort_order - b.sort_order)
                .map((link, index) => (
                  <div key={index}>
                    {link.url ? (
                      <a
                        href={link.url}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="block"
                      >
                        <div 
                          className="p-4 rounded-lg border hover:shadow-md transition-all duration-200 transform hover:scale-[1.02] cursor-pointer"
                          style={{ 
                            borderColor: theme.primaryColor,
                            backgroundColor: theme.backgroundColor
                          }}
                        >
                          <div className="flex items-center justify-between">
                            <div className="flex-1">
                              <div className="flex items-center gap-2 mb-1">
                                <span 
                                  className="font-semibold text-lg"
                                  style={{ color: theme.primaryColor }}
                                >
                                  {link.title}
                                </span>
                                <Badge 
                                  variant="secondary" 
                                  className="text-xs"
                                >
                                  {link.type}
                                </Badge>
                              </div>
                              {link.type === 'PRODUCT' && link.price && (
                                <p className="text-sm font-bold text-green-600">
                                  {link.currency} {link.price.toFixed(2)}
                                </p>
                              )}
                            </div>
                            <ExternalLink 
                              className="h-5 w-5 flex-shrink-0" 
                              style={{ color: theme.primaryColor }}
                            />
                          </div>
                        </div>
                      </a>
                    ) : (
                      <div 
                        className="p-4 rounded-lg border opacity-75"
                        style={{ 
                          borderColor: theme.primaryColor,
                          backgroundColor: theme.backgroundColor
                        }}
                      >
                        <div className="flex items-center justify-between">
                          <div className="flex-1">
                            <div className="flex items-center gap-2 mb-1">
                              <span 
                                className="font-semibold text-lg"
                                style={{ color: theme.primaryColor }}
                              >
                                {link.title}
                              </span>
                              <Badge 
                                variant="secondary" 
                                className="text-xs"
                              >
                                {link.type}
                              </Badge>
                            </div>
                            {link.type === 'PRODUCT' && link.price && (
                              <p className="text-sm font-bold text-green-600">
                                {link.currency} {link.price.toFixed(2)}
                              </p>
                            )}
                          </div>
                        </div>
                      </div>
                    )}
                  </div>
                ))}
            </div>

            {bioData.links.filter(link => link.is_active).length === 0 && (
              <div className="text-center py-12">
                <p 
                  className="text-muted-foreground"
                  style={{ color: theme.textColor }}
                >
                  No active links yet
                </p>
              </div>
            )}

            {/* Footer */}
            <div className="text-center mt-12 pt-8 border-t">
              <p className="text-sm text-muted-foreground">
                Powered by{" "}
                <a 
                  href="/" 
                  className="font-medium hover:underline"
                  style={{ color: theme.primaryColor }}
                >
                  Zaplink
                </a>
              </p>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
