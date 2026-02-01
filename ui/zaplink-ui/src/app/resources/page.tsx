import Navbar from '@/components/layout/Navbar';
import { FooterSection } from '@/features/landing/ui/FooterSection';
import { CTASection } from '@/features/landing/ui/CTASection';
import ResourcesHero from '@/features/resources/ui/ResourcesHero';
import FeaturedResource from '@/features/resources/ui/FeaturedResource';
import ResourceGrid from '@/features/resources/ui/ResourceGrid';
import ResourcesNewsletter from '@/features/resources/ui/ResourcesNewsletter';

export default function ResourcesPage() {
    return (
        <div className="flex min-h-screen flex-col bg-background font-sans">
            <Navbar />
            <main className="flex-1">
                <ResourcesHero />
                <FeaturedResource />
                <ResourceGrid />
                <ResourcesNewsletter />
                <CTASection />
            </main>
            <FooterSection />
        </div>
    );
}
