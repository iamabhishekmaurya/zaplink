import Navbar from '@/components/layout/Navbar';
import { FooterSection } from '@/components/landing/FooterSection';
import BlogListing from '@/components/blog/BlogListing';
import { CTASection } from '@/components/landing/CTASection';

export default function AllArticlesPage() {
    return (
        <div className="flex min-h-screen flex-col bg-background font-sans">
            <Navbar />
            <main className="flex-1 pt-24">
                <div className="max-w-7xl mx-auto px-4 sm:px-6 mb-8 text-center">
                    <h1 className="text-4xl md:text-5xl font-bold tracking-tight mb-4">All <span className="text-primary font-[family-name:var(--font-script)]">Articles</span></h1>
                    <p className="text-muted-foreground text-lg max-w-2xl mx-auto">Explore our entire library of guides, tutorials, and insights.</p>
                </div>
                <BlogListing showViewAllLink={false} />
                <CTASection />
            </main>
            <FooterSection />
        </div>
    );
}
