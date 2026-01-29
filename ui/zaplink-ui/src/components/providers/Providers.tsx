"use client"

import { ThemeProvider } from "next-themes"
import { Provider } from "react-redux"
import { Toaster } from "@/components/ui/sonner"
import store from "../../store/index"
import { AuthInitializer } from "../auth/AuthInitializer"

import ReactQueryProvider from "@/providers/ReactQueryProvider"

interface ProvidersProps {
    children: React.ReactNode
}

export function Providers({ children }: ProvidersProps) {
    return (
        <ReactQueryProvider>
            <Provider store={store}>
                <ThemeProvider attribute="class" defaultTheme="system" enableSystem disableTransitionOnChange>
                    <AuthInitializer />
                    {children}
                    <Toaster />
                </ThemeProvider>
            </Provider>
        </ReactQueryProvider>
    )
}