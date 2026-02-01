"use client"

import { useState, useEffect } from 'react'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Badge } from '@/components/ui/badge'
import { ExternalLink, Loader2 } from 'lucide-react'

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

interface BioPagePreviewProps {
  pageId: string
}

export function BioPagePreview({ pageId }: BioPagePreviewProps) {
  const [bioData, setBioData] = useState<BioPageData | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchBioData = async () => {
      try {
        setLoading(true)
        const response = await fetch(`/api/v1/bio-pages/${pageId}`)
        if (response.ok) {
          const data = await response.json()
          setBioData(data)
        } else {
          setError("Failed to load bio page data")
        }
      } catch (err) {
        setError("Error loading bio page")
      } finally {
        setLoading(false)
      }
    }

    fetchBioData()
  }, [pageId])

  if (loading) {
    return (
      <div className="flex items-center justify-center py-12">
        <Loader2 className="h-8 w-8 animate-spin" />
      </div>
    )
  }

  if (error || !bioData) {
    return (
      <Card>
        <CardContent className="flex items-center justify-center py-12">
          <p className="text-muted-foreground">{error || "No data available"}</p>
        </CardContent>
      </Card>
    )
  }

  return (
    <div className="space-y-6">
      <div className="text-center">
        <h3 className="text-2xl font-bold mb-2">Preview: @{bioData.username}</h3>
        <p className="text-sm text-muted-foreground">zap.link/{bioData.username}</p>
      </div>

      <Card className="max-w-md mx-auto">
        <CardContent className="p-6">
          {/* Avatar and Bio */}
          <div className="text-center mb-6">
            {bioData.avatar_url && (
              <img
                src={bioData.avatar_url}
                alt="Avatar"
                className="w-20 h-20 rounded-full mx-auto mb-4 object-cover"
              />
            )}
            <h4 className="text-xl font-semibold mb-2">@{bioData.username}</h4>
            {bioData.bio_text && (
              <p className="text-muted-foreground">{bioData.bio_text}</p>
            )}
          </div>

          {/* Links */}
          <div className="space-y-3">
            {bioData.links
              .filter(link => link.is_active)
              .sort((a, b) => a.sort_order - b.sort_order)
              .map((link, index) => (
                <div
                  key={index}
                  className="flex items-center justify-between p-3 border rounded-lg hover:bg-muted/50 transition-colors"
                >
                  <div className="flex-1">
                    <div className="flex items-center gap-2">
                      <span className="font-medium">{link.title}</span>
                      <Badge variant="outline" className="text-xs">
                        {link.type}
                      </Badge>
                    </div>
                    {link.type === 'PRODUCT' && link.price && (
                      <p className="text-sm font-medium text-green-600">
                        {link.currency} {link.price.toFixed(2)}
                      </p>
                    )}
                  </div>
                  {link.url && (
                    <button
                      onClick={() => window.open(link.url, '_blank')}
                      className="p-2 hover:bg-muted rounded-md transition-colors"
                    >
                      <ExternalLink className="h-4 w-4" />
                    </button>
                  )}
                </div>
              ))}
          </div>

          {bioData.links.filter(link => link.is_active).length === 0 && (
            <div className="text-center py-8">
              <p className="text-muted-foreground">No active links yet</p>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}
