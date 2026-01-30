"use client"

import { useState } from "react"
import { ThemeProvider } from "next-themes"
import { Provider } from "react-redux"
import { QueryClient, QueryClientProvider } from "@tanstack/react-query"
import { Toaster } from "@/components/ui/sonner"
import store from "@/store/index"
import { AuthInitializer } from "@/components/auth/AuthInitializer"

interface ProvidersProps {
    children: React.ReactNode
}

/**
 * Root providers component that wraps the entire application.
 * Consolidates: React Query, Redux, Theme, and Toast providers.
 * 
 * @refactored Consolidated ReactQueryProvider inline (previously in src/providers/)
 */
export function Providers({ children }: ProvidersProps) {
    const [queryClient] = useState(() => new QueryClient({
        defaultOptions: {
            queries: {
                staleTime: 60 * 1000, // 1 minute
                retry: 1,
            },
        },
    }))

    return (
        <QueryClientProvider client={queryClient}>
            <Provider store={store}>
                <ThemeProvider attribute="class" defaultTheme="system" enableSystem disableTransitionOnChange>
                    <AuthInitializer />
                    {children}
                    <Toaster />
                </ThemeProvider>
            </Provider>
        </QueryClientProvider>
    )
}