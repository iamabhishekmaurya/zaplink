"use client";

import { Button } from '@/components/ui/button';
import { Form } from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";
import { transformFormDataToApiRequest, validateLinkFormData } from '@/features/bio-page/lib/form-data-transformer';
import { linkFormSchema, LinkFormValues } from '@/features/bio-page/lib/validators';
import { BioPageLinkType } from '@/features/bio-page/types/index';
import { cn } from '@/lib/utils';
import { zodResolver } from '@hookform/resolvers/zod';
import { AnimatePresence, motion } from 'framer-motion';
import {
  Calendar,
  Link2,
  Lock,
  Plus,
  Share2,
  ShoppingBag,
  Sparkles,
  Video,
  X
} from 'lucide-react';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { toast } from 'sonner';

interface CreateLinkFormProps {
  onCreateLink: (linkData: any) => void;
  pageId: string;
  currentLinksCount: number;
}

const linkTypes = [
  { value: 'LINK', label: 'Regular Link', icon: Link2, color: 'text-blue-500', bg: 'bg-blue-500/10', description: 'Any website URL' },
  { value: 'SOCIAL', label: 'Social Media', icon: Share2, color: 'text-pink-500', bg: 'bg-pink-500/10', description: 'Instagram, Twitter, etc.' },
  { value: 'PRODUCT', label: 'Product', icon: ShoppingBag, color: 'text-emerald-500', bg: 'bg-emerald-500/10', description: 'With pricing info' },
  { value: 'EMBED', label: 'Music/Video', icon: Video, color: 'text-purple-500', bg: 'bg-purple-500/10', description: 'YouTube, Spotify embed' },
  { value: 'SCHEDULED', label: 'Scheduled', icon: Calendar, color: 'text-amber-500', bg: 'bg-amber-500/10', description: 'Timed visibility' },
  { value: 'GATED', label: 'Gated Content', icon: Lock, color: 'text-rose-500', bg: 'bg-rose-500/10', description: 'Password protected' },
] as const;

export function CreateLinkForm({ onCreateLink, pageId, currentLinksCount }: CreateLinkFormProps) {
  const [isExpanded, setIsExpanded] = useState(false);
  const [selectedType, setSelectedType] = useState<BioPageLinkType>('LINK');

  const form = useForm({
    resolver: zodResolver(linkFormSchema),
    defaultValues: {
      type: 'LINK',
      isActive: true,
      sortOrder: 0,
      currency: 'USD',
      title: '',
      url: '',
      price: undefined,
      scheduleFrom: null,
      scheduleTo: null,
      thumbnailUrl: '',
      iconUrl: '',
      embedCode: '',
      gateType: undefined,
      gateValue: '',
      gateMessage: ''
    }
  });

  const { reset, setValue, watch, handleSubmit, formState: { isSubmitting, errors } } = form;
  const currentType = watch('type');

  const handleExpand = () => {
    setIsExpanded(true);
  };

  const handleClose = () => {
    setIsExpanded(false);
    reset({
      type: 'LINK',
      isActive: true,
      sortOrder: 0,
      currency: 'USD',
      title: '',
      url: ''
    });
    setSelectedType('LINK');
  };

  const onSubmit = async (data: LinkFormValues) => {
    try {
      const validationErrors = validateLinkFormData(data);
      if (validationErrors) {
        Object.entries(validationErrors).forEach(([field, message]) => {
          form.setError(field as any, { message });
        });
        return;
      }

      const requestData = transformFormDataToApiRequest(data, pageId, currentLinksCount);
      await onCreateLink(requestData);
      handleClose();
    } catch {
      toast.error("Failed to create link");
    }
  };

  const TypeSelectorSidebar = () => (
    <div className="flex flex-col gap-2 p-2 border-r bg-muted/20 w-[72px] items-center">
      <TooltipProvider delayDuration={0}>
        {linkTypes.map((type) => {
          const Icon = type.icon;
          const isSelected = currentType === type.value;
          return (
            <Tooltip key={type.value}>
              <TooltipTrigger asChild>
                <button
                  type="button"
                  onClick={() => {
                    setValue('type', type.value as BioPageLinkType, { shouldValidate: true });
                    setSelectedType(type.value as BioPageLinkType);
                  }}
                  className={cn(
                    "relative w-12 h-12 rounded-xl flex items-center justify-center transition-all duration-200",
                    isSelected
                      ? cn("bg-background shadow-sm ring-1 ring-border", type.color)
                      : "text-muted-foreground hover:bg-muted hover:text-foreground"
                  )}
                >
                  <Icon className="w-5 h-5" />
                  {isSelected && (
                    <motion.div
                      layoutId="active-indicator"
                      className={cn("absolute inset-0 rounded-xl opacity-20", type.bg.replace('/10', '/20'))}
                    />
                  )}
                </button>
              </TooltipTrigger>
              <TooltipContent side="right" className="flex flex-col gap-1 p-3">
                <p className="font-semibold">{type.label}</p>
                <p className="text-xs text-muted-foreground">{type.description}</p>
              </TooltipContent>
            </Tooltip>
          );
        })}
      </TooltipProvider>
    </div>
  );

  return (
    <div className="w-full">
      {/* Collapsed State - Add Button */}
      <AnimatePresence mode="wait">
        {!isExpanded && (
          <motion.button
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -10 }}
            onClick={handleExpand}
            className={cn(
              "w-full p-4 rounded-xl border-2 border-dashed",
              "border-violet-300 hover:border-violet-500",
              "bg-gradient-to-r from-violet-500/5 to-indigo-500/5",
              "hover:from-violet-500/10 hover:to-indigo-500/10",
              "transition-all duration-300 group"
            )}
          >
            <div className="flex items-center justify-center gap-3">
              <div className="w-10 h-10 rounded-full bg-primary/10 flex items-center justify-center shadow-lg group-hover:scale-110 transition-transform">
                <Plus className="w-5 h-5 text-primary" />
              </div>
              <div className="text-left">
                <p className="font-semibold text-primary group-hover:text-primary/90">Add New Link</p>
                <p className="text-sm text-muted-foreground">Click to create a new link</p>
              </div>
              <Sparkles className="w-5 h-5 text-primary/60 opacity-0 group-hover:opacity-100 transition-opacity" />
            </div>
          </motion.button>
        )}
      </AnimatePresence>

      {/* Expanded State - Inline Form */}
      <AnimatePresence>
        {isExpanded && (
          <motion.div
            initial={{ opacity: 0, height: 0 }}
            animate={{ opacity: 1, height: 'auto' }}
            exit={{ opacity: 0, height: 0 }}
            transition={{ duration: 0.3, ease: [0.25, 0.46, 0.45, 0.94] }}
            className="overflow-hidden border rounded-xl bg-card shadow-sm"
          >
            <Form {...form}>
              <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col">
                {/* Structure: Sidebar + Content */}
                <div className="flex h-full min-h-[400px]">

                  {/* Left Sidebar */}
                  <TypeSelectorSidebar />

                  {/* Right Content Area */}
                  <div className="flex-1 flex flex-col p-6 space-y-6">
                    {/* Header */}
                    <div className="flex items-center justify-between pb-2 border-b">
                      <div>
                        <h3 className="font-semibold text-lg flex items-center gap-2">
                          {linkTypes.find(t => t.value === currentType)?.label || 'Create Link'}
                          <span className="text-xs font-normal text-muted-foreground px-2 py-0.5 rounded-full bg-muted">
                            {linkTypes.find(t => t.value === currentType)?.description}
                          </span>
                        </h3>
                      </div>
                      <Button
                        type="button"
                        variant="ghost"
                        size="icon"
                        onClick={handleClose}
                        className="h-8 w-8 -mr-2"
                      >
                        <X className="w-4 h-4" />
                      </Button>
                    </div>

                    {/* Dynamic Form Fields */}
                    <div className="space-y-5 flex-1">
                      {/* Common Fields */}
                      <div className="space-y-2">
                        <Label htmlFor="title">Title *</Label>
                        <Input
                          id="title"
                          placeholder="Link Title (e.g., My Portfolio)"
                          {...form.register('title')}
                          className={cn(errors.title && "border-red-500")}
                        />
                        {errors.title && (
                          <p className="text-xs text-red-500">{errors.title.message}</p>
                        )}
                      </div>

                      {/* URL Field (most types) */}
                      {(currentType === 'LINK' || currentType === 'SOCIAL' || currentType === 'EMBED') && (
                        <div className="space-y-2">
                          <Label htmlFor="url">URL *</Label>
                          <Input
                            id="url"
                            placeholder={currentType === 'EMBED' ? "Paste YouTube/Spotify link here" : "https://website.com"}
                            {...form.register('url')}
                            className={cn(errors.url && "border-red-500")}
                          />
                          {errors.url && (
                            <p className="text-xs text-red-500">{errors.url.message}</p>
                          )}
                        </div>
                      )}

                      {/* Product Fields */}
                      {currentType === 'PRODUCT' && (
                        <>
                          <div className="space-y-2">
                            <Label htmlFor="url">Product URL (optional)</Label>
                            <Input
                              id="url"
                              placeholder="https://store.com/product"
                              {...form.register('url')}
                            />
                          </div>
                          <div className="grid grid-cols-2 gap-4">
                            <div className="space-y-2">
                              <Label htmlFor="price">Price *</Label>
                              <Input
                                id="price"
                                type="number"
                                step="0.01"
                                placeholder="0.00"
                                {...form.register('price', { valueAsNumber: true })}
                                className={cn(errors.price && "border-red-500")}
                              />
                            </div>
                            <div className="space-y-2">
                              <Label htmlFor="currency">Currency</Label>
                              <Select
                                value={watch('currency') || 'USD'}
                                onValueChange={(val) => setValue('currency', val)}
                              >
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
                          </div>
                        </>
                      )}

                      {/* Scheduled Fields */}
                      {currentType === 'SCHEDULED' && (
                        <>
                          <div className="space-y-2">
                            <Label htmlFor="url">Target URL *</Label>
                            <Input
                              id="url"
                              placeholder="https://event.com"
                              {...form.register('url')}
                              className={cn(errors.url && "border-red-500")}
                            />
                          </div>
                          <div className="grid grid-cols-2 gap-4">
                            <div className="space-y-2">
                              <Label>Visible From</Label>
                              <Input
                                type="datetime-local"
                                {...form.register('scheduleFrom')}
                              />
                            </div>
                            <div className="space-y-2">
                              <Label>Visible Until</Label>
                              <Input
                                type="datetime-local"
                                {...form.register('scheduleTo')}
                              />
                            </div>
                          </div>
                        </>
                      )}

                      {/* Gated Fields */}
                      {currentType === 'GATED' && (
                        <>
                          <div className="space-y-2">
                            <Label htmlFor="url">Private Content URL *</Label>
                            <Input
                              id="url"
                              placeholder="https://secret-content.com"
                              {...form.register('url')}
                              className={cn(errors.url && "border-red-500")}
                            />
                          </div>
                          <div className="space-y-2">
                            <Label>Protection Method</Label>
                            <Select
                              value={watch('gateType') || 'email'}
                              onValueChange={(val) => setValue('gateType', val as any)}
                            >
                              <SelectTrigger>
                                <SelectValue />
                              </SelectTrigger>
                              <SelectContent>
                                <SelectItem value="email">Email Required (Capture Leads)</SelectItem>
                                <SelectItem value="password">Password Protected</SelectItem>
                              </SelectContent>
                            </Select>
                          </div>
                        </>
                      )}

                      {/* Embed Fields */}
                      {currentType === 'EMBED' && (
                        <div className="space-y-2">
                          <Label htmlFor="embedCode">Custom Embed Code (optional)</Label>
                          <textarea
                            id="embedCode"
                            placeholder="<iframe src='...'></iframe>"
                            {...form.register('embedCode')}
                            className="w-full min-h-[100px] p-3 rounded-md border border-input bg-transparent text-sm focus:ring-2 focus:ring-ring"
                          />
                        </div>
                      )}
                    </div>

                    {/* Actions */}
                    <div className="flex justify-end gap-3 pt-2">
                      <Button
                        type="button"
                        variant="ghost"
                        onClick={handleClose}
                        disabled={isSubmitting}
                      >
                        Cancel
                      </Button>
                      <Button
                        type="submit"
                        disabled={isSubmitting}
                        className="bg-primary hover:bg-primary/90 min-w-[120px]"
                      >
                        {isSubmitting ? (
                          <>
                            <Loader2 className="w-4 h-4 mr-2 animate-spin" />
                            Creating...
                          </>
                        ) : (
                          <>
                            <Plus className="w-4 h-4 mr-2" />
                            Add Link
                          </>
                        )}
                      </Button>
                    </div>
                  </div>
                </div>
              </form>
            </Form>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
}

function Loader2(props: any) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width="24"
      height="24"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth="2"
      strokeLinecap="round"
      strokeLinejoin="round"
      {...props}
    >
      <path d="M21 12a9 9 0 1 1-6.219-8.56" />
    </svg>
  );
}
