"use client"

import { useEffect, useState } from 'react'
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'
import { Label } from '@/components/ui/label'
import { Button } from '@/components/ui/button'
import { Form } from '@/components/ui/form'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { linkFormSchema, LinkFormValues } from '@/features/bio-page/lib/validators'
import { LinkForm } from '@/features/bio-page/ui/link-forms/link-form'
import { SocialLinkForm } from '@/features/bio-page/ui/link-forms/social-link-form'
import { ProductLinkForm } from '@/features/bio-page/ui/link-forms/product-link-form'
import { EmbedLinkForm } from '@/features/bio-page/ui/link-forms/embed-link-form'
import { ScheduledLinkForm } from '@/features/bio-page/ui/link-forms/scheduled-link-form'
import { GatedLinkForm } from '@/features/bio-page/ui/link-forms/gated-link-form'
import { BioLink } from '@/services/bioPageService'
import { BioPageLinkType } from '@/features/bio-page/types/index'
import { toast } from 'sonner'

interface EditBioLinkDialogProps {
  link: BioLink
  open: boolean
  onOpenChange: (open: boolean) => void
  onUpdateLink: (linkData: any) => void
}

export function EditBioLinkDialog({ link, open, onOpenChange, onUpdateLink }: EditBioLinkDialogProps) {
  const form = useForm({
    resolver: zodResolver(linkFormSchema),
    defaultValues: {
      title: link.title,
      url: link.url || '',
      type: link.type as BioPageLinkType,
      isActive: link.isActive,
      sortOrder: link.sortOrder,
      price: link.price,
      currency: link.currency || 'USD',
      scheduleFrom: link.scheduleFrom ? new Date(link.scheduleFrom) : null,
      scheduleTo: link.scheduleTo ? new Date(link.scheduleTo) : null,
      thumbnailUrl: link.thumbnailUrl || '',
      iconUrl: link.iconUrl || '',
      embedCode: '',
      gateType: undefined,
      gateValue: '',
      gateMessage: ''
    }
  });

  const { reset, handleSubmit, setValue, watch, control } = form;
  const currentType = watch('type') as BioPageLinkType; // Cast to ensure type

  useEffect(() => {
    if (open && link) {
      // Parse metadata
      let gateType = undefined;
      let gateValue = '';
      let gateMessage = '';
      let embedCode = '';

      if (link.metadata) {
        try {
          const meta = JSON.parse(link.metadata);
          if (meta.gatedContent) {
            gateType = meta.gatedContent.type;
            gateValue = meta.gatedContent.value;
            gateMessage = meta.gatedContent.gateMessage;
          }
          if (meta.embedCode) {
            embedCode = meta.embedCode;
          }
        } catch {
          // Silent fail for metadata parsing
        }
      }

      reset({
        title: link.title,
        url: link.url || '',
        type: link.type as BioPageLinkType,
        isActive: link.isActive,
        sortOrder: link.sortOrder,
        price: link.price,
        currency: link.currency || 'USD',
        scheduleFrom: link.scheduleFrom ? new Date(link.scheduleFrom) : null,
        scheduleTo: link.scheduleTo ? new Date(link.scheduleTo) : null,
        thumbnailUrl: link.thumbnailUrl || '',
        iconUrl: link.iconUrl || '',
        embedCode: embedCode,
        gateType: gateType,
        gateValue: gateValue,
        gateMessage: gateMessage
      });
    }
  }, [open, link, reset]);

  const onSubmit = async (data: any) => {
    try {
      const submissionData: any = { ...data };

      const metadataObj: any = {};

      // Preserve existing metadata if any, but we are parsing/rebuilding it.
      // Better to rebuild.

      if (data.gateType) {
        metadataObj.gatedContent = {
          type: data.gateType,
          value: data.gateValue,
          gateMessage: data.gateMessage
        };
      }
      if (data.embedCode) {
        metadataObj.embedCode = data.embedCode;
      }

      if (Object.keys(metadataObj).length > 0) {
        submissionData.metadata = JSON.stringify(metadataObj);
      } else {
        submissionData.metadata = undefined; // Or null to clear?
      }

      await onUpdateLink(submissionData);
      onOpenChange(false);
    } catch (error) {
      toast.error("Failed to update link");
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px] max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle>Edit Link</DialogTitle>
        </DialogHeader>

        <Form {...form}>
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            {/* Type selector usually disabled or omitted in edit mode if type change is not supported properly. 
                 But user might want to change "Social" platform (which is just title change in my SocialLinkForm logic? No, type is SOCIAL).
                 If I change type from LINK to PRODUCT, new fields appear.
                 I'll allow type change.
             */}
            <div className="space-y-2">
              <Label>Link Type</Label>
              <Select
                value={currentType}
                onValueChange={(val) => {
                  setValue('type', val as BioPageLinkType);
                }}
              >
                <SelectTrigger>
                  <SelectValue />
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
                {form.formState.isSubmitting ? "Updating..." : "Update Link"}
              </Button>
            </div>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
}
