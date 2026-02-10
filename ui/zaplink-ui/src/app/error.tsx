'use client'

export default function Error({
  error,
  reset,
}: {
  error: Error & { digest?: string }
  reset: () => void
}) {
  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-background">
      <div className="text-center space-y-6 p-8 max-w-lg">
        <div className="space-y-2">
          <h1 className="text-6xl font-bold text-destructive">500</h1>
          <h2 className="text-2xl font-semibold text-foreground">Something went wrong</h2>
        </div>
        
        <div className="space-y-4">
          <p className="text-muted-foreground">
            We apologize for the inconvenience. An unexpected error occurred while processing your request.
          </p>
          
          {process.env.NODE_ENV === 'development' && (
            <details className="text-left">
              <summary className="cursor-pointer text-sm font-mono text-muted-foreground hover:text-foreground">
                Error Details
              </summary>
              <pre className="mt-2 p-4 bg-muted rounded-md text-xs overflow-auto">
                {error.message}
                {error.stack}
              </pre>
            </details>
          )}
          
          <div className="flex flex-col sm:flex-row gap-3 justify-center">
            <button
              onClick={reset}
              className="inline-flex items-center justify-center px-4 py-2 bg-primary text-primary-foreground rounded-md hover:bg-primary/90 transition-colors"
            >
              Try Again
            </button>
            <a 
              href="/dashboard" 
              className="inline-flex items-center justify-center px-4 py-2 border border-input bg-background hover:bg-accent hover:text-accent-foreground rounded-md transition-colors"
            >
              Go to Dashboard
            </a>
          </div>
        </div>

        <div className="pt-8">
          <p className="text-sm text-muted-foreground">
            If the problem persists, please contact our support team.
          </p>
        </div>
      </div>
    </div>
  )
}
