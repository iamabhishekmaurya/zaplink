import Navbar from '@/components/layout/Navbar';
import { FooterSection } from '@/features/landing/ui/FooterSection';
import BlogHero from '@/features/blog/ui/BlogHero';
import FeaturedPost from '@/features/blog/ui/FeaturedPost';
import BlogListing from '@/features/blog/ui/BlogListing';
import BlogNewsletter from '@/features/blog/ui/BlogNewsletter';

export default function BlogPage() {
    return (
        <div className="flex min-h-screen flex-col bg-background font-sans">
            <Navbar />
            <main className="flex-1">
                <BlogHero />
                <FeaturedPost />
                <BlogListing />
                <BlogNewsletter />
            </main>
            <FooterSection />
        </div>
    );
}
