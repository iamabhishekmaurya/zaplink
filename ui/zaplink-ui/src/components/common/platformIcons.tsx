import { 
  Youtube, 
  Github, 
  Globe, 
  Twitter, 
  Linkedin, 
  Instagram, 
  Facebook,
  Link,
  Tag
} from 'lucide-react';
import { IconBrandYoutube } from '@tabler/icons-react';

export const getPlatformIcon = (platform: string) => {
  const normalizedPlatform = platform.toLowerCase();
  
  switch (normalizedPlatform) {
    case 'youtube':
      return IconBrandYoutube;
    case 'github':
      return Github;
    case 'twitter':
      return Twitter;
    case 'linkedin':
      return Linkedin;
    case 'instagram':
      return Instagram;
    case 'facebook':
      return Facebook;
    case 'website':
    case 'portfolio':
    case 'blog':
      return Globe;
    default:
      return Link;
  }
};

export const getPlatformColor = (platform: string) => {
  const normalizedPlatform = platform.toLowerCase();
  
  switch (normalizedPlatform) {
    case 'youtube':
      return 'bg-red-100 dark:bg-red-900/20 text-red-600 dark:text-red-400';
    case 'github':
      return 'bg-gray-100 dark:bg-gray-900/20 text-gray-600 dark:text-gray-400';
    case 'twitter':
      return 'bg-blue-100 dark:bg-blue-900/20 text-blue-600 dark:text-blue-400';
    case 'linkedin':
      return 'bg-blue-100 dark:bg-blue-900/20 text-blue-600 dark:text-blue-400';
    case 'instagram':
      return 'bg-pink-100 dark:bg-pink-900/20 text-pink-600 dark:text-pink-400';
    case 'facebook':
      return 'bg-blue-100 dark:bg-blue-900/20 text-blue-600 dark:text-blue-400';
    default:
      return 'bg-gray-100 dark:bg-gray-900/20 text-gray-600 dark:text-gray-400';
  }
};
