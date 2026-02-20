"use client"

import { Button } from '@/components/ui/button'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Form } from '@/components/ui/form'
import { Label } from '@/components/ui/label'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { transformFormDataToApiRequest, validateLinkFormData } from '@/features/bio-page/lib/form-data-transformer'
import { linkFormSchema, LinkFormValues } from '@/features/bio-page/lib/validators'
import { BioPageLinkType } from '@/features/bio-page/types/index'
import { EmbedLinkForm } from '@/features/bio-page/ui/link-forms/embed-link-form'
import { GatedLinkForm } from '@/features/bio-page/ui/link-forms/gated-link-form'
import { LinkForm } from '@/features/bio-page/ui/link-forms/link-form'
import { ProductLinkForm } from '@/features/bio-page/ui/link-forms/product-link-form'
import { ScheduledLinkForm } from '@/features/bio-page/ui/link-forms/scheduled-link-form'
import { SocialLinkForm } from '@/features/bio-page/ui/link-forms/social-link-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form'
import { toast } from 'sonner'

interface CreateBioLinkDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  onCreateLink: (linkData: any) => void
  pageId: string
  currentLinksCount: number
}

export function CreateBioLinkDialog({ open, onOpenChange, onCreateLink, pageId, currentLinksCount }: CreateBioLinkDialogProps) {
  const [selectedType, setSelectedType] = useState<BioPageLinkType>('LINK');

  const form = useForm({
    resolver: zodResolver(linkFormSchema),
    defaultValues: {
      type: 'LINK',
      isActive: true, // Explicit true matches boolean schema
      sortOrder: 0,
      currency: 'USD',
      title: '',
      url: '',
      price: undefined,
      scheduleFrom: null, // nullable in schema
      scheduleTo: null,
      thumbnailUrl: '',
      iconUrl: '',
      embedCode: '',
      gateType: undefined,
      gateValue: '',
      gateMessage: ''
    }
  });

  const { reset, setValue, watch, handleSubmit } = form;

  // Watch type change to update local state if needed for UI switching
  // Actually we should rely on form value if we sync them. 
  // But type selector is outside the sub-forms usually, or part of the main form.

  // When dialog opens/closes, reset form
  useEffect(() => {
    if (open) {
      reset({
        type: 'LINK',
        isActive: true,
        sortOrder: 0,
        currency: 'USD',
        title: '',
        url: ''
      });
      setSelectedType('LINK');
    }
  }, [open, reset]);

  const onSubmit = async (data: LinkFormValues) => {
    try {
      // Validate form data based on type
      const validationErrors = validateLinkFormData(data);
      if (validationErrors) {
        // Display validation errors
        Object.entries(validationErrors).forEach(([field, message]) => {
          form.setError(field as any, { message });
        });
        return;
      }

      // Transform form data to API request format
      const requestData = transformFormDataToApiRequest(data, pageId, currentLinksCount);

      await onCreateLink(requestData);
      onOpenChange(false);
    } catch {
      toast.error("Failed to create link");
    }
  };

  const currentType = watch('type');

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px] max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle>Add New Link</DialogTitle>
        </DialogHeader>

        <Form {...form}>
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">

            <div className="space-y-2">
              <Label>Link Type</Label>
              <Select
                value={currentType || 'LINK'}
                onValueChange={(val) => {
                  setValue('type', val as BioPageLinkType, { shouldValidate: true });
                  setSelectedType(val as BioPageLinkType);
                }}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Select link type" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="LINK">Link</SelectItem>
                  <SelectItem value="SOCIAL">Social</SelectItem>
                  <SelectItem value="PRODUCT">Product</SelectItem>
                  <SelectItem value="EMBED">Music/Video</SelectItem>
                  <SelectItem value="SCHEDULED">Scheduled</SelectItem>
                  <SelectItem value="GATED">Gated Content</SelectItem>
                </SelectContent>
              </Select>
            </div>

            {currentType === 'LINK' && <LinkForm />}
            {currentType === 'SOCIAL' && <SocialLinkForm />}
            {currentType === 'PRODUCT' && <ProductLinkForm />}
            {currentType === 'EMBED' && <EmbedLinkForm />}
            {currentType === 'SCHEDULED' && <ScheduledLinkForm />}
            {currentType === 'GATED' && <GatedLinkForm />}

            <div className="flex justify-end gap-2 pt-4">
              <Button type="button" variant="outline" onClick={() => onOpenChange(false)}>
                Cancel
              </Button>
              <Button type="submit" disabled={form.formState.isSubmitting}>
                {form.formState.isSubmitting ? "Creating..." : "Add Link"}
              </Button>
            </div>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
}
