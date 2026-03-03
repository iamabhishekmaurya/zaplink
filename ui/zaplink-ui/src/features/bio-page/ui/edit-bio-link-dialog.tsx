"use client"

import { Button } from '@/components/ui/button'
import { Form } from '@/components/ui/form'
import { Label } from '@/components/ui/label'
import { Sheet, SheetContent, SheetDescription, SheetHeader, SheetTitle } from '@/components/ui/sheet'
import { linkFormSchema } from '@/features/bio-page/lib/validators'
import { BioPageLinkType } from '@/features/bio-page/types/index'
import { EmbedLinkForm } from '@/features/bio-page/ui/link-forms/embed-link-form'
import { GatedLinkForm } from '@/features/bio-page/ui/link-forms/gated-link-form'
import { LinkForm } from '@/features/bio-page/ui/link-forms/link-form'
import { ProductLinkForm } from '@/features/bio-page/ui/link-forms/product-link-form'
import { ScheduledLinkForm } from '@/features/bio-page/ui/link-forms/scheduled-link-form'
import { SocialLinkForm } from '@/features/bio-page/ui/link-forms/social-link-form'
import { BioLink } from '@/services/bioPageService'
import { zodResolver } from '@hookform/resolvers/zod'
import { FileText, Link2, Music, Package, ShoppingBag, Video } from 'lucide-react'
import { useEffect } from 'react'
import { useForm } from 'react-hook-form'
import { toast } from 'sonner'

// Get icon from type
function getTypeIcon(type: string) {
  switch (type) {
    case 'PRODUCT': return <ShoppingBag className="w-5 h-5" />
    case 'VIDEO': return <Video className="w-5 h-5" />
    case 'MUSIC': return <Music className="w-5 h-5" />
    case 'SOCIAL': return <Link2 className="w-5 h-5" />
    case 'SECTION': return <Package className="w-5 h-5" />
    case 'EMBED': return <FileText className="w-5 h-5" />
    default: return <Link2 className="w-5 h-5" />
  }
}

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
          // Metadata is already an object in BioLink interface
          const meta = typeof link.metadata === 'string' ? JSON.parse(link.metadata) : link.metadata;

          if (meta.gatedContent) {
            gateType = meta.gatedContent.type;
            gateValue = meta.gatedContent.value;
            gateMessage = meta.gatedContent.gateMessage;
          }
          if (meta.embedCode) {
            embedCode = meta.embedCode;
          }
        } catch {
          // Silent fail
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
    <Sheet open={open} onOpenChange={onOpenChange}>
      <SheetContent className="sm:max-w-md md:max-w-lg lg:max-w-xl overflow-y-auto p-0 flex flex-col h-full bg-card/95 backdrop-blur-xl border-l border-border/50 mobile-scrollbar pr-1">
        <SheetHeader className="p-6 border-b bg-background/50 sticky top-0 z-10">
          <div className="flex items-center gap-3">
            <div className="p-2.5 bg-primary/10 rounded-xl text-primary shrink-0">
              {getTypeIcon(currentType || link.type)}
            </div>
            <div className="text-left flex-1 min-w-0">
              <SheetTitle className="text-xl font-bold truncate">{watch('title') || 'Edit Link'}</SheetTitle>
              <SheetDescription className="truncate">
                {watch('url') ? watch('url') : 'Configure your link details'}
              </SheetDescription>
            </div>
          </div>
        </SheetHeader>

        <div className="p-6 flex-1">
          <Form {...form}>
            <form id="edit-link-form" onSubmit={handleSubmit(onSubmit)} className="space-y-6 pb-24">

              <div className="grid gap-3">
                <Label className="text-sm font-semibold">Change Link Type</Label>
                <div className="grid grid-cols-3 gap-2">
                  {[
                    { value: 'LINK', label: 'Link', icon: '🔗' },
                    { value: 'SOCIAL', label: 'Social', icon: '📱' },
                    { value: 'PRODUCT', label: 'Product', icon: '🛍️' },
                    { value: 'EMBED', label: 'Media', icon: '🎵' },
                    { value: 'SCHEDULED', label: 'Schedule', icon: '📅' },
                    { value: 'GATED', label: 'Gated', icon: '🔒' }
                  ].map((type) => (
                    <div
                      key={type.value}
                      onClick={() => setValue('type', type.value as BioPageLinkType)}
                      className={`
                        cursor-pointer flex flex-col items-center justify-center p-3 rounded-xl border-2 transition-all duration-200
                        ${currentType === type.value
                          ? 'border-primary bg-primary/10 text-primary shadow-sm scale-[1.02]'
                          : 'border-transparent bg-muted/50 hover:bg-muted hover:scale-[1.01]'}
                      `}
                    >
                      <span className="text-xl mb-1.5">{type.icon}</span>
                      <span className="text-[11px] font-semibold tracking-wide uppercase">{type.label}</span>
                    </div>
                  ))}
                </div>
              </div>

              <div className="h-px w-full bg-border" />

              {/* Form Content */}
              <div className="animate-in fade-in slide-in-from-bottom-2 duration-300">
                {currentType === 'LINK' && <LinkForm />}
                {currentType === 'SOCIAL' && <SocialLinkForm />}
                {currentType === 'PRODUCT' && <ProductLinkForm />}
                {currentType === 'EMBED' && <EmbedLinkForm />}
                {currentType === 'SCHEDULED' && <ScheduledLinkForm />}
                {currentType === 'GATED' && <GatedLinkForm />}
              </div>

            </form>
          </Form>
        </div>

        {/* Floating Action Bar at the bottom of the sheet */}
        <div className="p-4 border-t bg-background/80 backdrop-blur-md sticky bottom-0 z-10 flex justify-end gap-3 rounded-tl-xl mt-auto">
          <Button type="button" variant="outline" onClick={() => onOpenChange(false)} className="rounded-xl px-6">
            Cancel
          </Button>
          <Button type="submit" form="edit-link-form" disabled={form.formState.isSubmitting} className="rounded-xl px-8 shadow-md hover:shadow-lg transition-shadow">
            {form.formState.isSubmitting ? "Saving..." : "Save Changes"}
          </Button>
        </div>

      </SheetContent>
    </Sheet>
  );
}
