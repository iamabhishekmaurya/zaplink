"use client"

import { ThemeProvider } from "next-themes"
import { Provider } from "react-redux"
import { Toaster } from "@/components/ui/sonner"
import store from "../../store/index"

interface ProvidersProps {
    children: React.ReactNode
}

export function Providers({ children }: ProvidersProps) {
    return (
        <Provider store={store}>
            <ThemeProvider attribute="class" defaultTheme="system" enableSystem disableTransitionOnChange>
                {children}
                <Toaster />
            </ThemeProvider>
        </Provider>
    )
}