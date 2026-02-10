"use client"

import { useState } from "react"
import { useRouter } from "next/navigation"
import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import * as z from "zod"
import { bioPageService } from "@/services/bioPageService"
import { useAuth } from "@/hooks/useAuth"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Textarea } from "@/components/ui/textarea"
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form"
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card"
import { handleApiError, showSuccessToast } from "@/lib/error-handler"
import { Loader2, ArrowRight, LayoutTemplate, AlertCircle } from "lucide-react"
import { templates, BioPageTemplate } from "@/features/bio-page/lib/templates"
import { cn } from "@/lib/utils"

const schema = z.object({
  username: z.string().min(3, "Username must be at least 3 characters").max(50),
  bioText: z.string().max(500).optional(),
  // avatarUrl: z.string().url().optional().or(z.literal('')), // Removed for simplicity in step 1, can add later or edit in editor
})

export default function CreateBioPage() {
  const router = useRouter()
  const { user, isLoading: authLoading } = useAuth()
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [step, setStep] = useState<1 | 2>(1)
  const [selectedTemplate, setSelectedTemplate] = useState<BioPageTemplate>(templates[0])

  const form = useForm<z.infer<typeof schema>>({
    resolver: zodResolver(schema),
    defaultValues: {
      username: "",
      bioText: "",
    },
  })

  // Show loading state while auth is initializing
  if (authLoading) {
    return (
      <div className="flex h-full items-center justify-center p-8">
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

      // Create page with selected template theme - omit problematic fields entirely
      const requestData: any = {
        username: data.username,
        owner_id: String(user.id),
        is_public: true
      };
      
      // Only include these fields if they have actual values
      if (data.bioText && data.bioText.trim()) {
        requestData.bio_text = data.bioText;
      }
      
      const newPage = await bioPageService.createBioPage(requestData);

      showSuccessToast("Bio page created successfully")
      // Redirect DIRECTLY to the new Editor
      router.push(`/dashboard/bio-page/${newPage.id}`)
    } catch (error) {
      
      // Type-safe error handling
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

  // Step 1: Basic Info
  if (step === 1) {
    return (
      <div className="container mx-auto p-6 max-w-2xl h-full flex flex-col justify-center">
        <div className="mb-8 text-center">
          <h1 className="text-3xl font-bold tracking-tight mb-2">Create Your Bio Page</h1>
          <p className="text-muted-foreground">Claim your unique link and start building your brand.</p>
        </div>

        {error && (
          <div className="mb-6">
            <div className="flex items-center gap-2 p-3 border border-destructive bg-destructive/10 rounded-md">
              <AlertCircle className="h-4 w-4 text-destructive" />
              <p className="text-sm text-destructive">{error}</p>
            </div>
          </div>
        )}

        <Card className="border-2">
          <CardHeader>
            <CardTitle>Step 1: Profile Details</CardTitle>
            <CardDescription>
              Choose a username and write a short bio. You can change this later.
            </CardDescription>
          </CardHeader>
          <CardContent>
            <Form {...form}>
              <form onSubmit={(e) => {
                e.preventDefault();
                form.trigger().then((valid) => {
                  if (valid) setStep(2);
                });
              }} className="space-y-6">
                <FormField
                  control={form.control}
                  name="username"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Username</FormLabel>
                      <FormControl>
                        <div className="flex items-center">
                          <span className="bg-muted px-3 py-2 border border-r-0 rounded-l-md text-muted-foreground text-sm font-medium">
                            zap.link/
                          </span>
                          <Input className="rounded-l-none font-medium" placeholder="username" {...field} />
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
                      <FormLabel>Bio</FormLabel>
                      <FormControl>
                        <Textarea placeholder="Tell us about yourself..." className="resize-none" rows={3} {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <div className="flex justify-end gap-2">
                  <Button type="button" variant="ghost" onClick={() => router.back()}>
                    Cancel
                  </Button>
                  <Button type="submit">
                    Next: Choose Template <ArrowRight className="ml-2 h-4 w-4" />
                  </Button>
                </div>
              </form>
            </Form>
          </CardContent>
        </Card>
      </div>
    )
  }

  // Step 2: Template Selection
  return (
    <div className="container mx-auto p-6 max-w-5xl h-full">
      <div className="mb-8 flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight mb-2">Choose a Style</h1>
          <p className="text-muted-foreground">Select a template to start with. You can fully customize it later.</p>
        </div>
        <Button variant="outline" onClick={() => setStep(1)}>Back</Button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mb-8">
        {templates.map(template => (
          <div
            key={template.id}
            className={cn(
              "cursor-pointer rounded-xl border-2 transition-all overflow-hidden relative group",
              selectedTemplate.id === template.id ? "border-primary ring-2 ring-primary ring-offset-2" : "border-slate-200 hover:border-slate-300"
            )}
            onClick={() => setSelectedTemplate(template)}
          >
            <div className="aspect-[9/16] bg-slate-100 relative">
              {/* Mockup of template style */}
              <div className="absolute inset-0 flex items-center justify-center text-slate-400">
                {/* In real app, render a mini preview here */}
                <div className="text-center p-4">
                  <div className="w-16 h-16 rounded-full bg-slate-200 mx-auto mb-4" />
                  <div className="h-4 w-24 bg-slate-200 mx-auto mb-2 rounded" />
                  <div className="h-3 w-32 bg-slate-200 mx-auto rounded" />
                  <div className="mt-8 space-y-2">
                    <div className="h-10 w-full bg-slate-200 rounded-lg" />
                    <div className="h-10 w-full bg-slate-200 rounded-lg" />
                  </div>
                </div>
              </div>
              {/* Overlay active state */}
              {selectedTemplate.id === template.id && (
                <div className="absolute inset-0 bg-primary/10 flex items-center justify-center">
                  <div className="bg-primary text-primary-foreground px-3 py-1 rounded-full text-sm font-medium shadow-sm">
                    Selected
                  </div>
                </div>
              )}
            </div>
            <div className="p-4 border-t bg-card">
              <h3 className="font-semibold">{template.name}</h3>
              <p className="text-xs text-muted-foreground">{template.description}</p>
            </div>
          </div>
        ))}
      </div>

      <div className="flex justify-end pb-12">
        <Button 
          size="lg" 
          onClick={async () => {
            // Validate form first
            const isValid = await form.trigger()
            
            if (isValid) {
              // Get form values and submit
              const formData = form.getValues()
              onSubmit(formData)
            } else {
              // Show validation errors
              setError('Please fix the validation errors before proceeding')
            }
          }} 
          disabled={loading} 
          className="w-full md:w-auto min-w-[200px]"
        >
          {loading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
          Create & Edit Page
        </Button>
      </div>
    </div>
  )
}
