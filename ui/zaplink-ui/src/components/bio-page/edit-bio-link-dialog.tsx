"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Switch } from "@/components/ui/switch"
import { showSuccessToast, showErrorToast } from "@/lib/toast"

interface BioLink {
  id: string
  pageId: string
  title: string
  url?: string
  type: string
  isActive: boolean
  sortOrder: number
  price?: number
  currency?: string
}

interface EditBioLinkDialogProps {
  link: BioLink
  open: boolean
  onOpenChange: (open: boolean) => void
  onUpdateLink: (linkData: any) => void
}

export function EditBioLinkDialog({ link, open, onOpenChange, onUpdateLink }: EditBioLinkDialogProps) {
  const [formData, setFormData] = useState({
    title: link.title,
    url: link.url || '',
    type: link.type,
    isActive: link.isActive,
    price: link.price?.toString() || '',
    currency: link.currency || 'USD'
  })
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    
    if (!formData.title.trim()) {
      showErrorToast("Error", "Title is required")
      return
    }

    setLoading(true)
    try {
      const linkData = {
        ...formData,
        price: formData.type === 'PRODUCT' ? parseFloat(formData.price) : undefined,
        currency: formData.type === 'PRODUCT' ? formData.currency : undefined,
        url: formData.type === 'PRODUCT' ? undefined : formData.url
      }
      
      await onUpdateLink(linkData)
    } finally {
      setLoading(false)
    }
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Edit Link</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="title">Title *</Label>
            <Input
              id="title"
              value={formData.title}
              onChange={(e) => setFormData(prev => ({ ...prev, title: e.target.value }))}
              required
              maxLength={200}
            />
          </div>

          <div className="flex items-center space-x-2">
            <Switch
              id="isActive"
              checked={formData.isActive}
              onCheckedChange={(checked) => setFormData(prev => ({ ...prev, isActive: checked }))}
            />
            <Label htmlFor="isActive">Active</Label>
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
              {loading ? "Updating..." : "Update Link"}
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  )
}
