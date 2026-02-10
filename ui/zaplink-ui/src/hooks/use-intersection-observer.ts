import { useEffect, useRef, useState } from 'react'

/**
 * Custom hook for intersection observer
 * @param options - IntersectionObserver options
 * @returns [ref, isIntersecting]
 */
export function useIntersectionObserver(
  options: IntersectionObserverInit = {}
): [React.RefObject<Element>, boolean] {
  const [isIntersecting, setIsIntersecting] = useState<boolean>(false)
  const ref = useRef<Element | null>(null)

  useEffect(() => {
    const element = ref.current
    if (!element) return

    const observer = new IntersectionObserver(([entry]) => {
      setIsIntersecting(entry.isIntersecting)
    }, options)

    observer.observe(element)

    return () => {
      observer.unobserve(element)
    }
  }, [options.threshold, options.rootMargin, options.root])

  return [ref as React.RefObject<Element>, isIntersecting]
}

/**
 * Custom hook for lazy loading images
 * @param src - The image source
 * @param options - IntersectionObserver options
 * @returns [ref, src, isLoading]
 */
export function useLazyImage(
  src: string,
  options: IntersectionObserverInit = {}
): [React.RefObject<HTMLImageElement>, string, boolean] {
  const [imageSrc, setImageSrc] = useState<string>('')
  const [isLoading, setIsLoading] = useState<boolean>(true)
  const [ref, isIntersecting] = useIntersectionObserver(options) as [React.RefObject<HTMLImageElement>, boolean]

  useEffect(() => {
    if (isIntersecting && !imageSrc) {
      setImageSrc(src)
    }
  }, [isIntersecting, src, imageSrc])

  useEffect(() => {
    if (imageSrc && ref.current) {
      const img = ref.current
      img.onload = () => setIsLoading(false)
      img.onerror = () => {
        setIsLoading(false)
        setImageSrc('') // Reset on error
      }
    }
  }, [imageSrc, ref])

  return [ref as React.RefObject<HTMLImageElement>, imageSrc, isLoading]
}
