"use client"

import { useState } from 'react'
import { Button } from '@/components/ui/button'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Switch } from '@/components/ui/switch'
import { toast } from 'sonner'

interface CreateBioLinkDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  onCreateLink: (linkData: any) => void
}

export function CreateBioLinkDialog({ open, onOpenChange, onCreateLink }: CreateBioLinkDialogProps) {
  const [formData, setFormData] = useState({
    title: '',
    url: '',
    type: 'LINK',
    isActive: true,
    sortOrder: 0,
    price: '',
    currency: 'USD'
  })
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()

    if (!formData.title.trim()) {
      toast.error("Error", {
        description: "Title is required",
      })
      return
    }

    if (formData.type !== 'PRODUCT' && !formData.url.trim()) {
      toast.error("Error", {
        description: "URL is required for this link type",
      })
      return
    }

    if (formData.type === 'PRODUCT') {
      if (!formData.price || parseFloat(formData.price) <= 0) {
        toast.error("Error", {
          description: "Valid price is required for product links",
        })
        return
      }
    }

    setLoading(true)
    try {
      const linkData = {
        ...formData,
        price: formData.type === 'PRODUCT' ? parseFloat(formData.price) : undefined,
        currency: formData.type === 'PRODUCT' ? formData.currency : undefined,
        url: formData.type === 'PRODUCT' ? undefined : formData.url
      }

      await onCreateLink(linkData)
      setFormData({
        title: '',
        url: '',
        type: 'LINK',
        isActive: true,
        sortOrder: 0,
        price: '',
        currency: 'USD'
      })
    } finally {
      setLoading(false)
    }
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Add New Link</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="title">Title *</Label>
            <Input
              id="title"
              placeholder="My Awesome Link"
              value={formData.title}
              onChange={(e) => setFormData(prev => ({ ...prev, title: e.target.value }))}
              required
              maxLength={200}
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="type">Type *</Label>
            <Select value={formData.type} onValueChange={(value) => setFormData(prev => ({ ...prev, type: value }))}>
              <SelectTrigger>
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="LINK">Website Link</SelectItem>
                <SelectItem value="PRODUCT">Product</SelectItem>
                <SelectItem value="SOCIAL">Social Media</SelectItem>
                <SelectItem value="EMAIL">Email</SelectItem>
                <SelectItem value="PHONE">Phone</SelectItem>
              </SelectContent>
            </Select>
          </div>

          {formData.type !== 'PRODUCT' && (
            <div className="space-y-2">
              <Label htmlFor="url">URL *</Label>
              <Input
                id="url"
                type="url"
                placeholder="https://example.com"
                value={formData.url}
                onChange={(e) => setFormData(prev => ({ ...prev, url: e.target.value }))}
                required={formData.type !== 'PRODUCT'}
                maxLength={2048}
              />
            </div>
          )}

          {formData.type === 'PRODUCT' && (
            <>
              <div className="space-y-2">
                <Label htmlFor="price">Price *</Label>
                <Input
                  id="price"
                  type="number"
                  step="0.01"
                  min="0"
                  placeholder="29.99"
                  value={formData.price}
                  onChange={(e) => setFormData(prev => ({ ...prev, price: e.target.value }))}
                  required
                />
              </div>

              <div className="space-y-2">
                <Label htmlFor="currency">Currency *</Label>
                <Select value={formData.currency} onValueChange={(value) => setFormData(prev => ({ ...prev, currency: value }))}>
                  <SelectTrigger>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="USD">USD ($)</SelectItem>
                    <SelectItem value="EUR">EUR (€)</SelectItem>
                    <SelectItem value="GBP">GBP (£)</SelectItem>
                    <SelectItem value="INR">INR (₹)</SelectItem>
                  </SelectContent>
                </Select>
              </div>
            </>
          )}

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
              {loading ? "Creating..." : "Add Link"}
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  )
}
