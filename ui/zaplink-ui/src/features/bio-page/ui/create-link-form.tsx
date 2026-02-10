"use client";

import { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { Label } from '@/components/ui/label';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Form } from '@/components/ui/form';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { linkFormSchema, LinkFormValues } from '@/features/bio-page/lib/validators';
import { BioPageLinkType } from '@/features/bio-page/types/index';
import { transformFormDataToApiRequest, validateLinkFormData } from '@/features/bio-page/lib/form-data-transformer';
import { toast } from 'sonner';
import { 
  Link2, 
  Share2, 
  ShoppingBag, 
  Video, 
  Calendar, 
  Lock,
  X,
  Plus,
  Check,
  Sparkles
} from 'lucide-react';
import { cn } from '@/lib/utils';

interface CreateLinkFormProps {
  onCreateLink: (linkData: any) => void;
  pageId: number;
  currentLinksCount: number;
}

const linkTypes = [
  { value: 'LINK', label: 'Regular Link', icon: Link2, color: 'bg-blue-500', description: 'Any website URL' },
  { value: 'SOCIAL', label: 'Social Media', icon: Share2, color: 'bg-pink-500', description: 'Instagram, Twitter, etc.' },
  { value: 'PRODUCT', label: 'Product', icon: ShoppingBag, color: 'bg-emerald-500', description: 'With pricing info' },
  { value: 'EMBED', label: 'Music/Video', icon: Video, color: 'bg-purple-500', description: 'YouTube, Spotify embed' },
  { value: 'SCHEDULED', label: 'Scheduled', icon: Calendar, color: 'bg-amber-500', description: 'Timed visibility' },
  { value: 'GATED', label: 'Gated Content', icon: Lock, color: 'bg-rose-500', description: 'Password protected' },
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

  const TypeSelector = () => (
    <div className="grid grid-cols-2 sm:grid-cols-3 gap-2">
      {linkTypes.map((type) => {
        const Icon = type.icon;
        const isSelected = currentType === type.value;
        return (
          <button
            key={type.value}
            type="button"
            onClick={() => {
              setValue('type', type.value as BioPageLinkType, { shouldValidate: true });
              setSelectedType(type.value as BioPageLinkType);
            }}
            className={cn(
              "flex items-center gap-2 p-3 rounded-xl border-2 transition-all duration-200 text-left",
              isSelected 
                ? `border-current bg-opacity-10` 
                : "border-transparent hover:border-muted bg-muted/50 hover:bg-muted"
            )}
            style={{ color: isSelected ? 'var(--color)' : undefined }}
          >
            <div className={cn(
              "w-8 h-8 rounded-lg flex items-center justify-center text-white",
              type.color
            )}>
              <Icon className="w-4 h-4" />
            </div>
            <div className="flex-1 min-w-0">
              <p className="font-medium text-sm truncate">{type.label}</p>
              <p className="text-xs text-muted-foreground truncate">{type.description}</p>
            </div>
            {isSelected && (
              <motion.div
                initial={{ scale: 0 }}
                animate={{ scale: 1 }}
                className="w-5 h-5 rounded-full bg-primary flex items-center justify-center"
              >
                <Check className="w-3 h-3 text-white" />
              </motion.div>
            )}
          </button>
        );
      })}
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
              "bg-gradient-to-r from-violet-50/50 to-indigo-50/50",
              "hover:from-violet-100/50 hover:to-indigo-100/50",
              "transition-all duration-300 group"
            )}
          >
            <div className="flex items-center justify-center gap-3">
              <div className="w-10 h-10 rounded-full bg-gradient-to-r from-violet-500 to-indigo-500 flex items-center justify-center shadow-lg group-hover:scale-110 transition-transform">
                <Plus className="w-5 h-5 text-white" />
              </div>
              <div className="text-left">
                <p className="font-semibold text-violet-700 group-hover:text-violet-800">Add New Link</p>
                <p className="text-sm text-violet-500/70">Click to create a new link</p>
              </div>
              <Sparkles className="w-5 h-5 text-violet-400 opacity-0 group-hover:opacity-100 transition-opacity" />
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
            className="overflow-hidden"
          >
            <Form {...form}>
              <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                {/* Header */}
                <div className="flex items-center justify-between pb-2 border-b">
                  <div className="flex items-center gap-2">
                    <div className="w-8 h-8 rounded-lg bg-gradient-to-r from-violet-500 to-indigo-500 flex items-center justify-center">
                      <Plus className="w-4 h-4 text-white" />
                    </div>
                    <div>
                      <h3 className="font-semibold">Create New Link</h3>
                      <p className="text-xs text-muted-foreground">Choose a type and fill the details</p>
                    </div>
                  </div>
                  <Button
                    type="button"
                    variant="ghost"
                    size="icon"
                    onClick={handleClose}
                    className="h-8 w-8"
                  >
                    <X className="w-4 h-4" />
                  </Button>
                </div>

                {/* Type Selector */}
                <div className="space-y-2">
                  <Label className="text-sm font-medium">What type of link?</Label>
                  <TypeSelector />
                </div>

                {/* Dynamic Form Fields */}
                <div className="space-y-4 pt-2">
                  {/* Common Fields */}
                  <div className="space-y-2">
                    <Label htmlFor="title">Title *</Label>
                    <Input
                      id="title"
                      placeholder="Enter link title"
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
                        placeholder={currentType === 'EMBED' ? "YouTube or Spotify URL" : "https://..."}
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
                          placeholder="https://..."
                          {...form.register('url')}
                        />
                      </div>
                      <div className="grid grid-cols-2 gap-3">
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
                        <Label htmlFor="url">URL *</Label>
                        <Input
                          id="url"
                          placeholder="https://..."
                          {...form.register('url')}
                          className={cn(errors.url && "border-red-500")}
                        />
                      </div>
                      <div className="grid grid-cols-2 gap-3">
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
                        <Label htmlFor="url">Content URL *</Label>
                        <Input
                          id="url"
                          placeholder="https://..."
                          {...form.register('url')}
                          className={cn(errors.url && "border-red-500")}
                        />
                      </div>
                      <div className="space-y-2">
                        <Label>Gate Type</Label>
                        <Select
                          value={watch('gateType') || 'email'}
                          onValueChange={(val) => setValue('gateType', val as any)}
                        >
                          <SelectTrigger>
                            <SelectValue />
                          </SelectTrigger>
                          <SelectContent>
                            <SelectItem value="email">Email Required</SelectItem>
                            <SelectItem value="password">Password Protected</SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                    </>
                  )}

                  {/* Embed Fields */}
                  {currentType === 'EMBED' && (
                    <div className="space-y-2">
                      <Label htmlFor="embedCode">Embed Code (optional)</Label>
                      <textarea
                        id="embedCode"
                        placeholder="Paste embed code here..."
                        {...form.register('embedCode')}
                        className="w-full min-h-[80px] p-3 rounded-md border border-input bg-transparent text-sm"
                      />
                    </div>
                  )}
                </div>

                {/* Actions */}
                <div className="flex justify-end gap-2 pt-4 border-t">
                  <Button 
                    type="button" 
                    variant="outline" 
                    onClick={handleClose}
                    disabled={isSubmitting}
                  >
                    Cancel
                  </Button>
                  <Button 
                    type="submit" 
                    disabled={isSubmitting}
                    className="bg-gradient-to-r from-violet-600 to-indigo-600 hover:from-violet-700 hover:to-indigo-700"
                  >
                    {isSubmitting ? (
                      <>
                        <motion.div
                          animate={{ rotate: 360 }}
                          transition={{ duration: 1, repeat: Infinity, ease: "linear" }}
                          className="w-4 h-4 border-2 border-white/30 border-t-white rounded-full mr-2"
                        />
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
              </form>
            </Form>
          </motion.div>
        )}
      </AnimatePresence>
    </div>
  );
}
