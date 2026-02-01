"use client"

import { RotateCw } from 'lucide-react'
import { useEffect, useState } from 'react'

const HomePage = () => {
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    const timer = setTimeout(() => {
      setIsLoading(false)
    }, 300)

    return () => clearTimeout(timer)
  }, [])

  if (isLoading) {
    return (
      <div className="flex flex-1 items-center justify-center">
        <div className="flex flex-col items-center gap-4">
          <RotateCw className="h-8 w-8 animate-spin text-primary" />
          <p className="text-xs text-muted-foreground">Loading...</p>
        </div>
      </div>
    )
  }

  return (
    <div>HomePage</div>
  )
}

export default HomePage