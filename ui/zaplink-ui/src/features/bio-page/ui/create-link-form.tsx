"use client";

import { Button } from '@/components/ui/button';
import { Form } from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { transformFormDataToApiRequest, validateLinkFormData } from '@/features/bio-page/lib/form-data-transformer';
import { linkFormSchema, LinkFormValues } from '@/features/bio-page/lib/validators';
import { BioPageLinkType } from '@/features/bio-page/types/index';
import { cn } from '@/lib/utils';
import { zodResolver } from '@hookform/resolvers/zod';
import { AnimatePresence, motion } from 'framer-motion';
import {
  Calendar,
  Globe,
  Link2,
  Loader2 as LucideLoader2,
  Lock,
  Plus,
  Share2,
  ShoppingBag,
  Sparkles,
  Video,
  X,
  ChevronLeft,
  ChevronRight
} from 'lucide-react';
import { useCallback, useEffect, useRef, useState } from 'react';
import { useForm } from 'react-hook-form';
import { toast } from 'sonner';

interface UrlMetadata {
  title: string | null;
  description: string | null;
  image: string | null;
  images?: string[];
  videos?: string[];
  price: number | null;
  currency: string | null;
  siteName: string | null;
  favicon: string | null;
}

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
  const [urlMetadata, setUrlMetadata] = useState<UrlMetadata | null>(null);
  const [selectedImageIndex, setSelectedImageIndex] = useState(0);
  const [isFetchingMeta, setIsFetchingMeta] = useState(false);
  const fetchTimeoutRef = useRef<NodeJS.Timeout | null>(null);
  const lastFetchedUrl = useRef<string>('');

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
    });
    form.clearErrors();
    setSelectedType('LINK');
    setUrlMetadata(null);
    setSelectedImageIndex(0);
    setIsFetchingMeta(false);
    lastFetchedUrl.current = '';
    if (fetchTimeoutRef.current) clearTimeout(fetchTimeoutRef.current);
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



  const handleSelectType = (typeValue: BioPageLinkType) => {
    setValue('type', typeValue, { shouldValidate: true });
    setSelectedType(typeValue);
    setUrlMetadata(null);
    setSelectedImageIndex(0);
    lastFetchedUrl.current = '';
  };

  // Fetch URL metadata for Product links
  const fetchUrlMetadata = useCallback(async (url: string) => {
    if (!url || lastFetchedUrl.current === url) return;
    try {
      new URL(url);
    } catch {
      return; // Not a valid URL yet
    }

    lastFetchedUrl.current = url;
    setIsFetchingMeta(true);
    try {
      const res = await fetch('/api/scraper/metadata', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-API-Version': '1'
        },
        body: JSON.stringify({ url }),
      });
      if (!res.ok) throw new Error('Fetch failed');
      const data: UrlMetadata = await res.json();
      setUrlMetadata(data);
      setSelectedImageIndex(0);

      // Auto-populate empty fields
      const currentTitle = form.getValues('title');
      if (!currentTitle && data.title) {
        setValue('title', data.title);
      }
      if (data.price && !form.getValues('price')) {
        setValue('price', data.price);
      }
      if (data.currency) {
        setValue('currency', data.currency);
      }

      // Prefer array of images if available
      const primaryImage = data.images && data.images.length > 0 ? data.images[0] : data.image;
      if (primaryImage) {
        setValue('thumbnailUrl', primaryImage);
      }
    } catch {
      // Silently fail - user can still fill in fields manually
    } finally {
      setIsFetchingMeta(false);
    }
  }, [setValue, form]);

  // Debounced trigger for Product URL changes
  const debouncedFetchMeta = useCallback((url: string) => {
    if (fetchTimeoutRef.current) clearTimeout(fetchTimeoutRef.current);
    fetchTimeoutRef.current = setTimeout(() => {
      fetchUrlMetadata(url);
    }, 800);
  }, [fetchUrlMetadata]);

  // URL field register with custom onChange for Product type
  const urlRegister = form.register('url');
  const handleProductUrlChange = useCallback((e: React.ChangeEvent<HTMLInputElement>) => {
    urlRegister.onChange(e); // update react-hook-form
    debouncedFetchMeta(e.target.value); // trigger debounced fetch
  }, [urlRegister, debouncedFetchMeta]);

  return (
    <div className="w-full relative z-20">
      {/* Collapsed State - Add Button */}
      <AnimatePresence mode="wait">
        {!isExpanded && (
          <motion.button
            initial={{ opacity: 0, y: 10 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -10 }}
            onClick={handleExpand}
            className={cn(
              "w-full p-4 sm:p-5 rounded-xl border border-dashed",
              "border-primary/30 hover:border-primary/60",
              "bg-gradient-to-tr from-primary/5 via-transparent to-primary/5",
              "hover:from-primary/10 hover:to-primary/10",
              "transition-all duration-300 group shadow-sm hover:shadow-md"
            )}
          >
            <div className="flex items-center justify-center gap-3 sm:gap-4">
              <div className="w-10 h-10 sm:w-12 sm:h-12 rounded-xl bg-background flex items-center justify-center shadow-sm border border-border/50 group-hover:scale-110 group-hover:bg-primary/5 transition-all duration-300 shrink-0">
                <Plus className="w-5 h-5 sm:w-6 sm:h-6 text-primary group-hover:rotate-90 transition-transform duration-500" />
              </div>
              <div className="text-left flex-1 min-w-0">
                <p className="font-bold text-base sm:text-lg tracking-tight text-foreground group-hover:text-primary transition-colors truncate">Add New Link</p>
                <p className="text-xs sm:text-sm text-muted-foreground/80 truncate">Expand your digital presence</p>
              </div>
              <Sparkles className="w-4 h-4 sm:w-5 sm:h-5 text-primary/40 opacity-0 group-hover:opacity-100 transition-opacity shrink-0" />
            </div>
          </motion.button>
        )}
      </AnimatePresence>

      {/* Expanded State - Premium Inline Form */}
      <AnimatePresence>
        {isExpanded && (
          <motion.div
            initial={{ opacity: 0, height: 0, scale: 0.95 }}
            animate={{ opacity: 1, height: 'auto', scale: 1 }}
            exit={{ opacity: 0, height: 0, scale: 0.95 }}
            transition={{ duration: 0.4, ease: [0.16, 1, 0.3, 1] }}
            className="overflow-hidden bg-card/60 backdrop-blur-3xl rounded-xl border shadow-xl relative"
          >
            {/* Subtle top highlight */}
            <div className="absolute top-0 inset-x-0 h-px bg-gradient-to-r from-transparent via-primary/30 to-transparent" />

            <Form {...form}>
              <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col p-6 sm:p-8 relative z-10 w-full">

                {/* Header & Close */}
                <div className="flex items-center justify-between mb-8">
                  <div>
                    <h3 className="font-bold text-2xl tracking-tight flex items-center gap-3">
                      New Link
                    </h3>
                    <p className="text-sm text-muted-foreground mt-1">Choose a type and configure your link below</p>
                  </div>
                  <Button
                    type="button"
                    variant="ghost"
                    size="icon"
                    onClick={handleClose}
                    className="h-10 w-10 shrink-0 text-muted-foreground hover:bg-muted hover:text-foreground rounded-full transition-colors bg-background border shadow-sm"
                  >
                    <X className="w-5 h-5" />
                  </Button>
                </div>

                {/* Horizontal Type Selector */}
                <div className="flex w-full gap-2 sm:gap-3 overflow-x-auto pb-4 no-scrollbar mb-6 snap-x">
                  {linkTypes.map((type) => {
                    const Icon = type.icon;
                    const isSelected = currentType === type.value;
                    return (
                      <button
                        key={type.value}
                        type="button"
                        onClick={() => handleSelectType(type.value as BioPageLinkType)}
                        className={cn(
                          "flex flex-col items-center justify-center shrink-0 w-[100px] h-[90px] rounded-xl border transition-all duration-300 snap-center relative overflow-hidden",
                          isSelected
                            ? "border-primary shadow-sm"
                            : "border-border/50 bg-background/50 hover:bg-muted/50 hover:border-border text-muted-foreground"
                        )}
                      >
                        <div className={cn(
                          "w-10 h-10 rounded-xl flex items-center justify-center mb-2 shadow-sm transition-colors",
                          isSelected ? "bg-primary/10 text-primary" : "bg-muted"
                        )}>
                          <Icon className="w-5 h-5" />
                        </div>
                        <span className={cn("text-[11px] font-semibold tracking-wide", isSelected ? "text-foreground" : "")}>{type.label}</span>
                      </button>
                    );
                  })}
                </div>

                {/* Form Fields using Floating Labels */}
                <div className="space-y-6 flex-1 w-full bg-background/40 border border-border/50 rounded-xl p-6 sm:p-8">

                  {/* Title Field */}
                  <div className="relative group">
                    <Input
                      id="title"
                      {...form.register('title')}
                      placeholder=" "
                      className={cn(
                        "block px-6 pb-4 pt-8 w-full text-base bg-background/50 hover:bg-background/80 border-border/50 focus:bg-background rounded-xl appearance-none focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary peer transition-all shadow-sm h-16",
                        errors.title && "border-red-500 focus:ring-red-500/20 focus:border-red-500"
                      )}
                    />
                    <Label
                      htmlFor="title"
                      className={cn(
                        "absolute text-sm text-muted-foreground duration-300 transform -translate-y-4 scale-[0.85] top-5 z-10 origin-[0] left-6 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-[0.85] peer-focus:-translate-y-4 peer-focus:text-primary pointer-events-none",
                        errors.title && "text-red-500 peer-focus:text-red-500"
                      )}
                    >
                      Link Title *
                    </Label>
                    {errors.title && (
                      <p className="absolute -bottom-5 left-2 text-[10px] text-red-500 font-medium">{errors.title.message}</p>
                    )}
                  </div>

                  {/* URL Field (most types) */}
                  {(currentType === 'LINK' || currentType === 'SOCIAL' || currentType === 'EMBED') && (
                    <div className="relative group">
                      <Input
                        id="url"
                        {...form.register('url')}
                        placeholder=" "
                        className={cn(
                          "block px-6 pb-4 pt-8 w-full text-base bg-background/50 hover:bg-background/80 border-border/50 focus:bg-background rounded-xl appearance-none focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary peer transition-all shadow-sm h-16",
                          errors.url && "border-red-500 focus:ring-red-500/20 focus:border-red-500"
                        )}
                      />
                      <Label
                        htmlFor="url"
                        className={cn(
                          "absolute text-sm text-muted-foreground duration-300 transform -translate-y-4 scale-[0.85] top-5 z-10 origin-[0] left-6 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-[0.85] peer-focus:-translate-y-4 peer-focus:text-primary pointer-events-none",
                          errors.url && "text-red-500 peer-focus:text-red-500"
                        )}
                      >
                        {currentType === 'EMBED' ? 'YouTube/Spotify Embed URL *' : 'Target URL *'}
                      </Label>
                      {errors.url && (
                        <p className="absolute -bottom-5 left-2 text-[10px] text-red-500 font-medium">{errors.url.message}</p>
                      )}
                    </div>
                  )}

                  {/* Product Fields */}
                  {currentType === 'PRODUCT' && (
                    <div className="space-y-6">
                      <div className="relative group">
                        <Input
                          id="url"
                          {...urlRegister}
                          onChange={handleProductUrlChange}
                          onBlur={(e) => { urlRegister.onBlur(e); debouncedFetchMeta(e.target.value); }}
                          placeholder=" "
                          className="block px-6 pb-4 pt-8 w-full text-base bg-background/50 hover:bg-background/80 border-border/50 focus:bg-background rounded-xl appearance-none focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary peer transition-all shadow-sm h-16"
                        />
                        <Label
                          htmlFor="url"
                          className="absolute text-sm text-muted-foreground duration-300 transform -translate-y-4 scale-[0.85] top-5 z-10 origin-[0] left-6 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-[0.85] peer-focus:-translate-y-4 peer-focus:text-primary pointer-events-none"
                        >
                          Product URL (Optional)
                        </Label>
                        {isFetchingMeta && (
                          <div className="absolute right-4 top-1/2 -translate-y-1/2">
                            <LucideLoader2 className="w-4 h-4 animate-spin text-primary" />
                          </div>
                        )}
                      </div>

                      {/* URL Preview Card */}
                      <AnimatePresence>
                        {urlMetadata && (urlMetadata.image || urlMetadata.images?.length || urlMetadata.title || urlMetadata.description) && (
                          <motion.div
                            initial={{ opacity: 0, y: -8 }}
                            animate={{ opacity: 1, y: 0 }}
                            exit={{ opacity: 0, y: -8 }}
                            className="rounded-xl border border-border/60 bg-background/60 overflow-hidden shadow-sm flex flex-col"
                          >
                            {(urlMetadata.images?.length ? urlMetadata.images.length > 0 : urlMetadata.image) && (
                              <div className="relative w-full h-40 bg-muted group/preview">
                                <img
                                  src={urlMetadata.images && urlMetadata.images.length > 0 ? urlMetadata.images[selectedImageIndex] : urlMetadata.image!}
                                  alt=""
                                  className="w-full h-full object-cover"
                                  onError={(e) => (e.currentTarget.style.display = 'none')}
                                />
                                {urlMetadata.images && urlMetadata.images.length > 1 && (
                                  <>
                                    <div className="absolute inset-0 bg-black/20 opacity-0 group-hover/preview:opacity-100 transition-opacity flex items-center justify-between px-2">
                                      <Button
                                        variant="secondary"
                                        size="icon"
                                        className="h-8 w-8 rounded-full bg-background/80 hover:bg-background text-foreground"
                                        onClick={(e) => {
                                          e.preventDefault();
                                          const newIndex = selectedImageIndex > 0 ? selectedImageIndex - 1 : urlMetadata.images!.length - 1;
                                          setSelectedImageIndex(newIndex);
                                          setValue('thumbnailUrl', urlMetadata.images![newIndex]);
                                        }}
                                      >
                                        <ChevronLeft className="w-5 h-5" />
                                      </Button>
                                      <Button
                                        variant="secondary"
                                        size="icon"
                                        className="h-8 w-8 rounded-full bg-background/80 hover:bg-background text-foreground"
                                        onClick={(e) => {
                                          e.preventDefault();
                                          const newIndex = selectedImageIndex < urlMetadata.images!.length - 1 ? selectedImageIndex + 1 : 0;
                                          setSelectedImageIndex(newIndex);
                                          setValue('thumbnailUrl', urlMetadata.images![newIndex]);
                                        }}
                                      >
                                        <ChevronRight className="w-5 h-5" />
                                      </Button>
                                    </div>
                                    <div className="absolute bottom-2 right-2 bg-black/60 text-white text-[10px] px-2 py-0.5 rounded-full font-medium">
                                      {selectedImageIndex + 1} / {urlMetadata.images.length}
                                    </div>
                                  </>
                                )}
                              </div>
                            )}
                            <div className="p-4 space-y-1.5 flex-1">
                              <div className="flex items-center gap-2">
                                {urlMetadata.favicon ? (
                                  <img src={urlMetadata.favicon} alt="" className="w-4 h-4 rounded-sm" onError={(e) => (e.currentTarget.style.display = 'none')} />
                                ) : (
                                  <Globe className="w-4 h-4 text-muted-foreground" />
                                )}
                                <span className="text-[11px] text-muted-foreground font-medium truncate">
                                  {urlMetadata.siteName || (
                                    (() => {
                                      const u = watch('url');
                                      if (typeof u === 'string' && u.length > 0) {
                                        try { return new URL(u).hostname; } catch { return ''; }
                                      }
                                      return '';
                                    })()
                                  )}
                                </span>
                              </div>
                              {urlMetadata.title && (
                                <p className="text-sm font-semibold text-foreground leading-snug line-clamp-2">{urlMetadata.title}</p>
                              )}
                              {urlMetadata.description && (
                                <p className="text-xs text-muted-foreground leading-relaxed line-clamp-2">{urlMetadata.description}</p>
                              )}
                              {urlMetadata.price && (
                                <p className="text-sm font-bold text-emerald-600 mt-1">
                                  {urlMetadata.currency || '$'}{urlMetadata.price.toFixed(2)}
                                </p>
                              )}
                            </div>
                          </motion.div>
                        )}
                      </AnimatePresence>

                      <div className="grid grid-cols-2 gap-4">
                        <div className="relative group">
                          <Input
                            id="price"
                            type="number"
                            step="0.01"
                            {...form.register('price', { valueAsNumber: true })}
                            placeholder=" "
                            className={cn(
                              "block px-6 pb-4 pt-8 w-full text-base bg-background/50 hover:bg-background/80 border-border/50 focus:bg-background rounded-xl appearance-none focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary peer transition-all shadow-sm h-16",
                              errors.price && "border-red-500 focus:ring-red-500/20 focus:border-red-500"
                            )}
                          />
                          <Label
                            htmlFor="price"
                            className={cn(
                              "absolute text-sm text-muted-foreground duration-300 transform -translate-y-4 scale-[0.85] top-5 z-10 origin-[0] left-6 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-[0.85] peer-focus:-translate-y-4 peer-focus:text-primary pointer-events-none",
                              errors.price && "text-red-500 peer-focus:text-red-500"
                            )}
                          >
                            Price *
                          </Label>
                        </div>
                        <div className="relative group flex flex-col justify-end">
                          <span className="absolute left-4 top-2 text-[10px] font-bold uppercase tracking-widest text-muted-foreground pointer-events-none z-10">Currency</span>
                          <Select value={watch('currency') || 'USD'} onValueChange={(val) => setValue('currency', val)}>
                            <SelectTrigger className="h-16 px-6 pt-5 bg-background/50 border-border/50 rounded-xl shadow-sm focus:ring-2 focus:ring-primary/20">
                              <SelectValue />
                            </SelectTrigger>
                            <SelectContent className="rounded-xl">
                              <SelectItem value="USD">USD ($)</SelectItem>
                              <SelectItem value="EUR">EUR (€)</SelectItem>
                              <SelectItem value="GBP">GBP (£)</SelectItem>
                              <SelectItem value="INR">INR (₹)</SelectItem>
                            </SelectContent>
                          </Select>
                        </div>
                      </div>
                    </div>
                  )}

                  {/* Scheduled Fields */}
                  {currentType === 'SCHEDULED' && (
                    <div className="space-y-6">
                      <div className="relative group">
                        <Input
                          id="url"
                          {...form.register('url')}
                          placeholder=" "
                          className={cn(
                            "block px-6 pb-4 pt-8 w-full text-base bg-background/50 hover:bg-background/80 border-border/50 focus:bg-background rounded-xl appearance-none focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary peer transition-all shadow-sm h-16",
                            errors.url && "border-red-500 focus:ring-red-500/20 focus:border-red-500"
                          )}
                        />
                        <Label
                          htmlFor="url"
                          className={cn(
                            "absolute text-sm text-muted-foreground duration-300 transform -translate-y-4 scale-[0.85] top-5 z-10 origin-[0] left-6 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-[0.85] peer-focus:-translate-y-4 peer-focus:text-primary pointer-events-none",
                            errors.url && "text-red-500 peer-focus:text-red-500"
                          )}
                        >
                          Target URL *
                        </Label>
                      </div>
                      <div className="grid grid-cols-2 gap-4">
                        <div className="relative group">
                          <span className="absolute left-4 top-2 text-[10px] font-bold uppercase tracking-widest text-muted-foreground pointer-events-none z-10">Visible From</span>
                          <Input type="datetime-local" {...form.register('scheduleFrom')} className="h-16 px-4 pt-5 bg-background/50 border-border/50 rounded-xl text-sm" />
                        </div>
                        <div className="relative group">
                          <span className="absolute left-4 top-2 text-[10px] font-bold uppercase tracking-widest text-muted-foreground pointer-events-none z-10">Visible Until</span>
                          <Input type="datetime-local" {...form.register('scheduleTo')} className="h-16 px-4 pt-5 bg-background/50 border-border/50 rounded-xl text-sm" />
                        </div>
                      </div>
                    </div>
                  )}

                  {/* Gated Fields */}
                  {currentType === 'GATED' && (
                    <div className="space-y-6">
                      <div className="relative group">
                        <Input
                          id="url"
                          {...form.register('url')}
                          placeholder=" "
                          className={cn(
                            "block px-6 pb-4 pt-8 w-full text-base bg-background/50 hover:bg-background/80 border-border/50 focus:bg-background rounded-xl appearance-none focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary peer transition-all shadow-sm h-16",
                            errors.url && "border-red-500 focus:ring-red-500/20 focus:border-red-500"
                          )}
                        />
                        <Label
                          htmlFor="url"
                          className={cn(
                            "absolute text-sm text-muted-foreground duration-300 transform -translate-y-4 scale-[0.85] top-5 z-10 origin-[0] left-6 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-[0.85] peer-focus:-translate-y-4 peer-focus:text-primary pointer-events-none",
                            errors.url && "text-red-500 peer-focus:text-red-500"
                          )}
                        >
                          Private Content URL *
                        </Label>
                      </div>
                      <div className="relative group">
                        <span className="absolute left-6 pb-2 top-1.5 text-[10px] font-bold uppercase tracking-widest text-muted-foreground pointer-events-none z-10">Protection Method</span>
                        <Select value={watch('gateType') || 'email'} onValueChange={(val) => setValue('gateType', val as any)}>
                          <SelectTrigger className="h-16 w-full px-6 pt-7 pb-3 text-base bg-background/50 hover:bg-background/80 border-border/50 rounded-xl shadow-sm focus:ring-2 focus:ring-primary/20 focus:border-primary transition-all">
                            <SelectValue />
                          </SelectTrigger>
                          <SelectContent className="rounded-xl border-border/50 shadow-lg">
                            <SelectItem value="email" className="rounded-lg py-2.5 px-4 focus:bg-primary/5">Email Capture (Lead Gen)</SelectItem>
                            <SelectItem value="password" className="rounded-lg py-2.5 px-4 focus:bg-primary/5">Password Required</SelectItem>
                          </SelectContent>
                        </Select>
                      </div>
                    </div>
                  )}

                  {/* Embed Fields */}
                  {currentType === 'EMBED' && (
                    <div className="relative group">
                      <textarea
                        id="embedCode"
                        {...form.register('embedCode')}
                        placeholder=" "
                        className="block px-6 pb-4 pt-10 w-full text-base bg-background/50 hover:bg-background/80 border-border/50 focus:bg-background rounded-xl appearance-none focus:outline-none focus:ring-2 focus:ring-primary/20 focus:border-primary peer transition-all shadow-sm min-h-[140px] resize-none font-mono text-sm leading-relaxed"
                      />
                      <Label
                        htmlFor="embedCode"
                        className="absolute text-sm text-muted-foreground duration-300 transform -translate-y-4 scale-[0.85] top-6 z-10 origin-[0] left-6 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-[0.85] peer-focus:-translate-y-4 peer-focus:text-primary pointer-events-none"
                      >
                        Custom Embed Code (HTML/Iframe/Scripts)
                      </Label>
                    </div>
                  )}
                </div>

                {/* Actions */}
                <div className="flex justify-end gap-3 mt-8 pt-6 border-t border-border/50">
                  <Button
                    type="button"
                    variant="ghost"
                    onClick={handleClose}
                    disabled={isSubmitting}
                    className="h-12 px-6 rounded-xl hover:bg-muted font-medium transition-colors"
                  >
                    Cancel
                  </Button>
                  <Button
                    type="submit"
                    disabled={isSubmitting}
                    className="h-12 px-8 rounded-xl bg-primary hover:bg-primary/90 hover:scale-[1.02] shadow-xl shadow-primary/20 font-bold transition-all text-primary-foreground tracking-wide flex items-center gap-2"
                  >
                    {isSubmitting ? (
                      <>
                        <LucideLoader2 className="w-5 h-5 animate-spin" />
                        Saving...
                      </>
                    ) : (
                      <>
                        <Sparkles className="w-5 h-5 opacity-80" />
                        Create Link
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


