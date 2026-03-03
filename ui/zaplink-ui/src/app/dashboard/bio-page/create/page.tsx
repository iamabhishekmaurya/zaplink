"use client"

import { useState, useMemo } from "react"
import { useRouter } from "next/navigation"
import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import * as z from "zod"
import { motion, AnimatePresence } from "framer-motion"
import { bioPageService } from "@/services/bioPageService"
import { useAuth } from "@/hooks/useAuth"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Textarea } from "@/components/ui/textarea"
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form"
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card"
import { handleApiError, showSuccessToast } from "@/lib/error-handler"
import { Loader2, ArrowRight, ArrowLeft, CheckCircle2, Sparkles, AlertCircle } from "lucide-react"
import { templates, BioPageTemplate } from "@/features/bio-page/lib/templates"
import { cn } from "@/lib/utils"
import { ThemePreviewCard } from "./theme-preview-card"
import { ModernBioPageRenderer } from "@/features/bio-page/ui/preview-panel/modern-bio-page-renderer"
import { BioPageWithTheme } from "@/features/bio-page/types"

const schema = z.object({
  username: z.string().min(3, "Username must be at least 3 characters").max(50).regex(/^[a-zA-Z0-9_-]+$/, "Alphanumeric, dashes, and underscores only"),
  bioText: z.string().max(500).optional()
})

export default function CreateBioPage() {
  const router = useRouter()
  const { user, isLoading: authLoading } = useAuth()
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  // Steps: 1 (Basic Info) -> 2 (Template Selection)
  const [step, setStep] = useState<1 | 2>(1)
  const [selectedTemplate, setSelectedTemplate] = useState<BioPageTemplate>(templates[0])

  const form = useForm<z.infer<typeof schema>>({
    resolver: zodResolver(schema),
    defaultValues: {
      username: "",
      bioText: "",
    },
    mode: "onChange" // validates on every keystroke for real-time preview
  })

  // Watch form values for live preview
  const formUsername = form.watch("username");
  const formBioText = form.watch("bioText");

  // Construct Mock Page for Live Preview rendering
  const mockPage: BioPageWithTheme = useMemo(() => {
    return {
      id: "preview-id",
      ownerId: "preview-owner",
      username: formUsername || "yourusername",
      title: formUsername ? `@${formUsername}` : "Your Name Here",
      bioText: formBioText || "Welcome to my page! This is a live preview of how your text will look.",
      isPublic: true,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
      themeConfig: selectedTemplate.theme as any,
      parsedTheme: selectedTemplate.theme,
      effects: selectedTemplate.theme.effects as any || { backgroundType: 'solid' },
      bioLinks: [
        {
          id: '1', pageId: 'preview-id', title: 'My Portfolio', url: '#', type: 'LINK',
          isActive: true, sortOrder: 0,
          createdAt: new Date().toISOString(), updatedAt: new Date().toISOString(),
        },
        {
          id: 's1', pageId: 'preview-id', title: 'Twitter', url: '#', type: 'SOCIAL',
          isActive: true, sortOrder: 1,
          createdAt: new Date().toISOString(), updatedAt: new Date().toISOString(),
        },
        {
          id: 's2', pageId: 'preview-id', title: 'Instagram', url: '#', type: 'SOCIAL',
          isActive: true, sortOrder: 2,
          createdAt: new Date().toISOString(), updatedAt: new Date().toISOString(),
        }
      ] as any[]
    }
  }, [formUsername, formBioText, selectedTemplate]);

  if (authLoading) {
    return (
      <div className="flex h-screen items-center justify-center p-8">
        <Loader2 className="h-8 w-8 animate-spin text-primary" />
      </div>
    )
  }

  async function onSubmit(data: z.infer<typeof schema>) {
    if (!user?.id) {
      setError("You must be logged in to create a page")
      return
    }

    try {
      setLoading(true)
      setError(null)

      // Bug Fix: Attach theme_config JSON explicitly here
      const requestData: any = {
        username: data.username,
        owner_id: String(user.id),
        is_public: true,
        theme_config: JSON.stringify(selectedTemplate.theme) // PHASE 1 APPLIED
      };

      if (data.bioText && data.bioText.trim()) {
        requestData.bio_text = data.bioText;
      }

      const newPage = await bioPageService.createBioPage(requestData);

      showSuccessToast("Bio page created successfully")
      // Redirect DIRECTLY to the new Editor
      router.push(`/dashboard/bio-page/${newPage.id}`)
    } catch (error) {
      let errorMessage = 'Failed to create bio page';
      if (error && typeof error === 'object' && 'response' in error) {
        const axiosError = error as any;
        if (axiosError.response?.data?.message) {
          errorMessage = axiosError.response.data.message;
        } else if (axiosError.response?.data?.error) {
          errorMessage = axiosError.response.data.error;
        } else if (axiosError.response?.status === 500) {
          errorMessage = 'Server error occurred. Please check the backend logs for details.';
        }
      } else if (error instanceof Error) {
        errorMessage = error.message;
      }
      setError(errorMessage);
      handleApiError(error, 'Failed to create bio page');
    } finally {
      setLoading(false)
    }
  }

  // Animation variants
  const slideVariants = {
    enter: (direction: number) => ({
      x: direction > 0 ? 50 : -50,
      opacity: 0
    }),
    center: {
      zIndex: 1,
      x: 0,
      opacity: 1
    },
    exit: (direction: number) => ({
      zIndex: 0,
      x: direction < 0 ? 50 : -50,
      opacity: 0
    })
  };

  const direction = step === 1 ? -1 : 1;

  return (
    <div className="flex h-screen w-full bg-background overflow-hidden relative">

      {/* Left Panel: Wizard Controls form */}
      <div className="w-full lg:w-[50%] flex flex-col h-full bg-card border-r shadow-xl relative z-10">

        {/* Progress Header */}
        <div className="px-6 py-6 border-b flex items-center justify-between">
          <div className="flex flex-col">
            <h2 className="text-xl font-bold tracking-tight">Setup Bio Page</h2>
            <div className="flex items-center gap-2 mt-2">
              <div className={cn("h-2 w-12 rounded-full transition-colors", step === 1 ? "bg-primary" : "bg-primary/20")} />
              <div className={cn("h-2 w-12 rounded-full transition-colors", step === 2 ? "bg-primary" : "bg-primary/20")} />
            </div>
          </div>
          {step === 2 && (
            <Button variant="ghost" size="sm" onClick={() => setStep(1)} className="text-muted-foreground">
              <ArrowLeft className="w-4 h-4 mr-1" /> Back
            </Button>
          )}
        </div>

        {/* Form Content Area */}
        <div className="flex-1 overflow-y-auto px-6 py-8 relative">

          {error && (
            <motion.div initial={{ opacity: 0, y: -10 }} animate={{ opacity: 1, y: 0 }} className="mb-6">
              <div className="flex items-center gap-2 p-4 border border-destructive bg-destructive/10 text-destructive rounded-lg shadow-sm">
                <AlertCircle className="h-5 w-5" />
                <p className="text-sm font-medium">{error}</p>
              </div>
            </motion.div>
          )}

          <AnimatePresence mode="wait" custom={direction}>
            {step === 1 && (
              <motion.div
                key="step1"
                custom={direction}
                variants={slideVariants}
                initial="enter"
                animate="center"
                exit="exit"
                transition={{ duration: 0.3 }}
                className="space-y-6"
              >
                <div className="mb-6">
                  <h1 className="text-3xl font-extrabold tracking-tight flex items-center gap-2">
                    <Sparkles className="h-6 w-6 text-primary" /> Let's get started
                  </h1>
                  <p className="text-muted-foreground mt-2">Claim your unique URL and introduce yourself to the world.</p>
                </div>

                <Form {...form}>
                  <form className="space-y-8">
                    <FormField
                      control={form.control}
                      name="username"
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel className="text-base">Username</FormLabel>
                          <FormControl>
                            <div className="flex items-center focus-within:ring-2 ring-primary ring-offset-background ring-offset-2 rounded-md transition-all">
                              <span className="bg-muted px-4 py-3 border border-r-0 rounded-l-md text-muted-foreground font-semibold">
                                zap.link/
                              </span>
                              <Input
                                className="rounded-l-none font-medium text-lg py-3 h-auto border-l-0 focus-visible:ring-0 focus-visible:ring-offset-0"
                                placeholder="username"
                                {...field}
                              />
                            </div>
                          </FormControl>
                          <FormMessage />
                        </FormItem>
                      )}
                    />

                    <FormField
                      control={form.control}
                      name="bioText"
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel className="text-base">Short Bio</FormLabel>
                          <FormControl>
                            <Textarea
                              placeholder="I am a creator, developer, and builder..."
                              className="resize-none text-base py-3 min-h-[120px]"
                              {...field}
                            />
                          </FormControl>
                          <FormMessage />
                        </FormItem>
                      )}
                    />

                    <Button
                      type="button"
                      size="lg"
                      className="w-full text-lg h-12"
                      onClick={async () => {
                        const valid = await form.trigger();
                        if (valid) setStep(2);
                      }}
                    >
                      Next: Choose Template <ArrowRight className="ml-2 h-5 w-5" />
                    </Button>
                  </form>
                </Form>
              </motion.div>
            )}

            {step === 2 && (
              <motion.div
                key="step2"
                custom={direction}
                variants={slideVariants}
                initial="enter"
                animate="center"
                exit="exit"
                transition={{ duration: 0.3 }}
                className="space-y-6"
              >
                <div className="mb-6">
                  <h1 className="text-3xl font-extrabold tracking-tight">Choose a Vibe</h1>
                  <p className="text-muted-foreground mt-2">Select a starting template based on 10 premium layouts. You can always change this later!</p>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 pb-10">
                  {templates.map(template => (
                    <ThemePreviewCard
                      key={template.id}
                      template={template}
                      isSelected={selectedTemplate.id === template.id}
                      onClick={() => setSelectedTemplate(template)}
                    />
                  ))}
                </div>
              </motion.div>
            )}
          </AnimatePresence>
        </div>

        {/* Action Footer for Step 2 */}
        <AnimatePresence>
          {step === 2 && (
            <motion.div
              initial={{ y: 100 }}
              animate={{ y: 0 }}
              exit={{ y: 100 }}
              transition={{ type: "spring", stiffness: 300, damping: 30 }}
              className="p-6 border-t bg-card mt-auto shrink-0"
            >
              <Button
                size="lg"
                onClick={async () => {
                  const isValid = await form.trigger()
                  if (isValid) {
                    onSubmit(form.getValues())
                  } else {
                    setStep(1);
                  }
                }}
                disabled={loading}
                className="w-full text-lg h-14 font-semibold shadow-lg shadow-primary/20 hover:shadow-primary/40 transition-shadow"
              >
                {loading ? <Loader2 className="mr-2 h-5 w-5 animate-spin" /> : <CheckCircle2 className="mr-2 h-5 w-5" />}
                {loading ? "Creating your page..." : "Publish Page & Edit"}
              </Button>
            </motion.div>
          )}
        </AnimatePresence>
      </div>

      {/* Right Panel: Live Mobile Preview */}
      <div className="hidden lg:flex w-[50%] bg-muted/30 items-center justify-center p-8 relative overflow-hidden backdrop-blur-sm">

        {/* Background Decorative Blur */}
        <div className="absolute inset-0 z-0 flex items-center justify-center opacity-40 pointer-events-none">
          <div className="w-[500px] h-[500px] rounded-full bg-primary/20 blur-3xl blend-multiply" />
          <div className="w-[600px] h-[600px] rounded-full bg-secondary/20 blur-3xl blend-multiply -ml-40" />
        </div>

        {/* Mobile Device Mockup Frame */}
        <motion.div
          className="relative z-10 w-full max-w-[400px] h-[800px] max-h-[85vh] bg-black rounded-[3rem] p-3 shadow-2xl border-[8px] border-black/90 overflow-hidden ring-4 ring-white/10"
          initial={{ scale: 0.9, opacity: 0 }}
          animate={{ scale: 1, opacity: 1 }}
          transition={{ type: "spring", stiffness: 200, damping: 20 }}
        >
          {/* iPhone Notch Stub */}
          <div className="absolute top-0 left-1/2 -translate-x-1/2 w-[120px] h-[25px] bg-black rounded-b-2xl z-50" />

          {/* Render ModernBioPage inside the frame in preview mode */}
          <div className="w-full h-full bg-white dark:bg-zinc-950 rounded-[2.25rem] overflow-hidden overflow-y-auto no-scrollbar relative shadow-inner">
            <ModernBioPageRenderer page={mockPage} previewMode={true} />
          </div>
        </motion.div>

        {/* Label floating */}
        <div className="absolute bottom-10 right-10 bg-background/80 backdrop-blur-md px-4 py-2 rounded-full border shadow-sm flex items-center gap-2">
          <span className="relative flex h-3 w-3">
            <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-green-400 opacity-75"></span>
            <span className="relative inline-flex rounded-full h-3 w-3 bg-green-500"></span>
          </span>
          <span className="text-sm font-medium text-muted-foreground">Live Preview</span>
        </div>
      </div>
    </div>
  )
}
