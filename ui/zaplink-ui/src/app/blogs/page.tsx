import Navbar from '@/components/layout/Navbar';
import { FooterSection } from '@/components/landing/FooterSection';
import BlogHero from '@/components/blog/BlogHero';
import FeaturedPost from '@/components/blog/FeaturedPost';
import BlogListing from '@/components/blog/BlogListing';
import BlogNewsletter from '@/components/blog/BlogNewsletter';

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
