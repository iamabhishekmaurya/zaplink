"use client"

import { useState, useEffect } from 'react'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { showSuccessToast, showErrorToast } from '@/lib/toast'

interface BioPageData {
  id: string
  username: string
  avatar_url?: string
  bio_text?: string
  theme_config?: string
}

interface BioPageSettingsProps {
  pageId: string
  onPageUpdate: () => void
}

export function BioPageSettings({ pageId, onPageUpdate }: BioPageSettingsProps) {
  const [bioData, setBioData] = useState<BioPageData | null>(null)
  const [formData, setFormData] = useState({
    bioText: '',
    avatarUrl: '',
    themeConfig: ''
  })
  const [loading, setLoading] = useState(false)
  const [saving, setSaving] = useState(false)

  useEffect(() => {
    const fetchBioData = async () => {
      try {
        setLoading(true)
        const response = await fetch(`/api/v1/bio-pages/${pageId}`)
        if (response.ok) {
          const data = await response.json()
          setBioData(data)
          setFormData({
            bioText: data.bio_text || '',
            avatarUrl: data.avatar_url || '',
            themeConfig: data.theme_config || ''
          })
        }
      } catch (err) {
        showErrorToast("Error", "Failed to load bio page data")
      } finally {
        setLoading(false)
      }
    }

    fetchBioData()
  }, [pageId])

  const handleSave = async () => {
    try {
      setSaving(true)
      const response = await fetch(`/api/v1/bio-pages/${pageId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          bio_text: formData.bioText,
          avatar_url: formData.avatarUrl,
          theme_config: formData.themeConfig
        }),
      })

      if (response.ok) {
        showSuccessToast("Success", "Bio page updated successfully")
        onPageUpdate()
      } else {
        showErrorToast("Error", "Failed to update bio page")
      }
    } catch (err) {
      showErrorToast("Error", "Failed to update bio page")
    } finally {
      setSaving(false)
    }
  }

  if (loading) {
    return <div>Loading settings...</div>
  }

  if (!bioData) {
    return <div>No data available</div>
  }

  return (
    <div className="space-y-6">
      <div>
        <h3 className="text-lg font-semibold mb-4">Page Settings</h3>
        <p className="text-sm text-muted-foreground mb-6">
          Customize your bio page appearance and content
        </p>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Basic Information</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div>
            <Label>Username</Label>
            <Input value={bioData.username} disabled />
            <p className="text-xs text-muted-foreground mt-1">
              Username cannot be changed
            </p>
          </div>

          <div>
            <Label htmlFor="bioText">Bio</Label>
            <Textarea
              id="bioText"
              placeholder="Tell people about yourself..."
              value={formData.bioText}
              onChange={(e) => setFormData(prev => ({ ...prev, bioText: e.target.value }))}
              maxLength={500}
              rows={3}
            />
            <p className="text-xs text-muted-foreground mt-1">
              {formData.bioText.length}/500 characters
            </p>
          </div>

          <div>
            <Label htmlFor="avatarUrl">Avatar URL</Label>
            <Input
              id="avatarUrl"
              placeholder="https://example.com/avatar.jpg"
              value={formData.avatarUrl}
              onChange={(e) => setFormData(prev => ({ ...prev, avatarUrl: e.target.value }))}
              maxLength={500}
            />
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Theme Configuration</CardTitle>
        </CardHeader>
        <CardContent>
          <div>
            <Label htmlFor="themeConfig">Theme JSON</Label>
            <Textarea
              id="themeConfig"
              placeholder='{"primaryColor": "#000000", "backgroundColor": "#ffffff"}'
              value={formData.themeConfig}
              onChange={(e) => setFormData(prev => ({ ...prev, themeConfig: e.target.value }))}
              rows={4}
            />
            <p className="text-xs text-muted-foreground mt-1">
              JSON configuration for customizing the appearance
            </p>
          </div>
        </CardContent>
      </Card>

      <div className="flex justify-end">
        <Button onClick={handleSave} disabled={saving}>
          {saving ? "Saving..." : "Save Changes"}
        </Button>
      </div>
    </div>
  )
}
