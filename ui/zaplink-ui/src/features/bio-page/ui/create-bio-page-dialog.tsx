"use client"

import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Textarea } from '@/components/ui/textarea'
import { showSuccessToast, showErrorToast } from '@/lib/toast'

interface CreateBioPageDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  onCreatePage: (pageData: any) => void
}

export function CreateBioPageDialog({ open, onOpenChange, onCreatePage }: CreateBioPageDialogProps) {
  const [formData, setFormData] = useState({
    username: '',
    bioText: '',
    avatarUrl: '',
    themeConfig: ''
  })
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!formData.username.trim()) {
      showErrorToast("Error", "Username is required")
      return
    }

    if (formData.username.length < 3 || formData.username.length > 50) {
      showErrorToast("Error", "Username must be between 3 and 50 characters")
      return
    }

    setLoading(true)
    try {
      await onCreatePage(formData)
      setFormData({
        username: '',
        bioText: '',
        avatarUrl: '',
        themeConfig: ''
      })
    } finally {
      setLoading(false)
    }
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Create New Bio Page</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="username">Username *</Label>
            <Input
              id="username"
              placeholder="johndoe"
              value={formData.username}
              onChange={(e) => setFormData(prev => ({ ...prev, username: e.target.value }))}
              required
              minLength={3}
              maxLength={50}
            />
            <p className="text-xs text-muted-foreground">
              This will be your unique URL: zap.link/{formData.username || 'username'}
            </p>
          </div>

          <div className="space-y-2">
            <Label htmlFor="bioText">Bio</Label>
            <Textarea
              id="bioText"
              placeholder="Tell people about yourself..."
              value={formData.bioText}
              onChange={(e) => setFormData(prev => ({ ...prev, bioText: e.target.value }))}
              maxLength={500}
              rows={3}
            />
            <p className="text-xs text-muted-foreground">
              {formData.bioText.length}/500 characters
            </p>
          </div>

          <div className="space-y-2">
            <Label htmlFor="avatarUrl">Avatar URL</Label>
            <Input
              id="avatarUrl"
              placeholder="https://example.com/avatar.jpg"
              value={formData.avatarUrl}
              onChange={(e) => setFormData(prev => ({ ...prev, avatarUrl: e.target.value }))}
              maxLength={500}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="themeConfig">Theme Configuration (JSON)</Label>
            <Textarea
              id="themeConfig"
              placeholder='{"primaryColor": "#000000", "backgroundColor": "#ffffff"}'
              value={formData.themeConfig}
              onChange={(e) => setFormData(prev => ({ ...prev, themeConfig: e.target.value }))}
              rows={3}
            />
          </div>

          <div className="flex justify-end gap-2">
            <Button
              type="button"
              variant="outline"
              onClick={() => onOpenChange(false)}
              disabled={loading}
            >
              Cancel
            </Button>
            <Button type="submit" disabled={loading}>
              {loading ? "Creating..." : "Create Page"}
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  )
}
